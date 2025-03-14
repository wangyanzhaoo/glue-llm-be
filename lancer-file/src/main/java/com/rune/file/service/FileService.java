package com.rune.file.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.rune.exception.BadRequestException;
import com.rune.file.domain.dto.FileQuery;
import com.rune.file.domain.entity.Files;
import com.rune.file.domain.entity.QFiles;
import com.rune.file.domain.mapstruct.FileMapper;
import com.rune.file.domain.vo.FileView;
import com.rune.file.repository.FileResp;
import com.rune.querydsl.JPAQueryFactoryPrimary;
import com.rune.utils.RedisUtils;
import com.rune.utils.SecurityUtils;
import com.rune.utils.minio.MinioUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author 大方的脑壳
 * @date 2022/8/24 14:42
 * @description 文件管理实现
 */
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileResp fileResp;

    private final RedisUtils redisUtils;

    private final FileMapper fileMapper;

    private final MinioUtils minioUtils;

    final QFiles qFile = QFiles.files;
    private final JPAQueryFactoryPrimary queryFactory;

    public Page<FileView> queryAll(FileQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotBlank(query.getName())) builder.and(qFile.name.contains(query.getName()));
        if (StringUtils.isNotBlank(query.getPath())) builder.and(qFile.path.contains(query.getPath()));
        if (StringUtils.isNotBlank(query.getType())) builder.and(qFile.type.contains(query.getType()));
        if (Objects.nonNull(query.getStatus())) builder.and(qFile.status.eq(query.getStatus()));
        Page<Files> page = fileResp.findAll(builder, PageRequest.of(query.getCurrent(), query.getPageSize(), Sort.by(Sort.Direction.DESC, "id")));
        return page.map(fileMapper::toVo);
    }

    public HashMap<String, Object> create(Files fileInfo) {
        HashMap<String, Object> map = new HashMap<>();
        Files files = null;
        if (fileResp.countByMd5(fileInfo.getMd5()) == 0) {
            String[] split = fileInfo.getName().split("\\.");
            fileInfo.setType(split[split.length - 1]);
            files = fileResp.saveAndFlush(fileInfo);
            updateDownload();
        } else {
            files = fileResp.findByMd5(fileInfo.getMd5());
        }
        map.put("fileId", files.getId());
        map.put("filePath", files.getPath());
        return map;
    }

    @Transactional
    public void delete(Set<Long> ids) {
        List<Files> allById = fileResp.findAllById(ids);
        for (Files file : allById) {
            if (file.getCount() <= 1) {
                minioUtils.removeObject(file.getBucket(), file.getPath());
                fileResp.deleteById(file.getId());
            } else {
                throw new BadRequestException("文件名《" + file.getName() + "》被多次使用，请检查后在删除文件");
            }
        }
    }


    @Transactional
    public void updateStatus(Set<Long> ids) {
        ArrayList<Tuple> tuples = new ArrayList<>();
        for (Long id : ids) {
            Tuple tuple = queryFactory.select(qFile.id, qFile.status, qFile.path).from(qFile).where(qFile.id.eq(id)).fetchFirst();
            if (tuple == null) {
                throw new BadRequestException("文件发生更改，请重新上传文件");
            }
            tuples.add(tuple);
        }
        tuples.forEach(tuple -> {
            if (tuple.get(qFile.status)) {
                minioUtils.copyObject(MinioService.bucketFormal, MinioService.bucketTemp, tuple.get(qFile.path));
                queryFactory.update(qFile).set(qFile.status, false).set(qFile.bucket, MinioService.bucketFormal)
                        .where(qFile.id.eq(tuple.get(qFile.id))).execute();
            }
        });
    }

    /**
     * 更新用户有效时间上传次数的缓存
     */
    public void updateDownload() {
        String filePre = "file:" + SecurityUtils.getCurrentUsername();
        Object count = redisUtils.get(filePre);
        if (Objects.isNull(count)) {
            redisUtils.setIfAbsent(filePre, 1, 600L);
        } else {
            redisUtils.increment(filePre);
        }
    }

    /**
     * 文件加减一
     *
     * @param filesId             文件id
     * @param primaryId           引用文件表的其他数据唯一值
     * @param addTrueOrMinusFalse 加 true 减 false
     * @param aClass              类文件
     */
    @Transactional
    public void quoteCountAddOrMinus(Long filesId, Object primaryId, Boolean addTrueOrMinusFalse, Class<?> aClass) {
        String name = aClass.getName();
        String quoteSingleRecord = name + "-" + primaryId;
        Tuple tuple = queryFactory.select(qFile.count, qFile.countRecord).from(qFile)
                .where(qFile.id.eq(filesId)).fetchFirst();
        Set<String> keySet = tuple.get(qFile.countRecord);
        Integer count = tuple.get(qFile.count);
        if (keySet == null) {
            keySet = new HashSet<>();
        }
        if (count == null) {
            count = 0;
        }
        boolean update = false;
        if (addTrueOrMinusFalse && !keySet.contains(quoteSingleRecord)) {
            // 加一
            count++;
            keySet.add(quoteSingleRecord);
            update = true;
        } else if (!addTrueOrMinusFalse && keySet.contains(quoteSingleRecord)) {
            // 减一
            count--;
            keySet.remove(quoteSingleRecord);
            update = true;
        }
        if (update) {
            queryFactory.update(qFile).set(qFile.count, count).set(qFile.countRecord, keySet)
                    .where(qFile.id.eq(filesId)).execute();
        }
    }

}
