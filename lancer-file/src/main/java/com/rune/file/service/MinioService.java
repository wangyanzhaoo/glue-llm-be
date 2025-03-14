package com.rune.file.service;

import com.rune.exception.BadRequestException;
import com.rune.file.domain.dto.MinioSmallDto;
import com.rune.file.domain.dto.MinioUpload;
import com.rune.file.domain.dto.MultiPartDto;
import com.rune.file.domain.entity.Files;
import com.rune.file.repository.FileResp;
import com.rune.utils.Configiutils;
import com.rune.utils.RedisUtils;
import com.rune.utils.SecurityUtils;
import com.rune.utils.minio.MinioUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MinioService {
    /* 设置 minio 临时和正式桶名称 */
    // 正式文件桶名称
    public static String bucketFormal;
    // 临时文件桶名称
    public static String bucketTemp;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${minio.bucketFormal}")
    public void setBucketFormal(String bucketFormal) {
        MinioService.bucketFormal = bucketFormal;
    }
    /* 设置 minio 临时和正式桶名称 */

    private final MinioUtils minIoUtils;

    private final FileResp fileResp;

    private final RedisUtils redisUtils;

    @Resource(name = "configService")
    private Configiutils configiutils;

    @Value("${minio.bucketTemp}")
    public void setBucketTemp(String bucketTemp) {
        MinioService.bucketTemp = bucketTemp;
    }

    public HashMap<String, Object> getUploadObjectUrl(MinioUpload resource) {
        HashMap<String, Object> map = isNotMd5(resource.getMd5());
        if (map != null) {
            return map;
        }
        // 验证判断条件
        verifyFile(resource.getFileName(), resource.getSize());
        // 默认上传临时桶
        resource.setBucketName(MinioService.bucketTemp);
        map = new HashMap<>();
        // 需要验证 minio 桶内是否含有相同文件名称
        String fileName = resource.getFileName();
        // 获取上传路径和文件名称
        String minioFullFileName = getMinioFullFileName(resource.getBucketName(), fileName);
        map.put("uploadUrl", minIoUtils.getObjectUrl(resource.getBucketName(), minioFullFileName, "PUT"));
        map.put("fileFullName", "/" + minioFullFileName);
        map.put("minioFileName", minioFullFileName.substring(minioFullFileName.lastIndexOf("/") + 1));
        return map;
    }

    /**
     * 返回文件minio全路径 xxx/xxx/name.xxx 最前面没有 /
     *
     * @param bucket   桶名
     * @param fileName 文件名
     * @return /
     */
    public String getMinioFullFileName(String bucket, String fileName) {
        // 文件名称（不包含后缀名）
        String fileNameNoType = fileName.substring(0, fileName.lastIndexOf("."));
        // 文件类型
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        // 桶内路径前缀（当前日期）
        String primaryPath = timeFormatter.format(LocalDate.now()) + "/";
        // 查询桶内-文件夹内-有没有此文件名的文件（不包含文件类型）
        List<String> formalFiles = minIoUtils.listObjects(MinioService.bucketFormal, fileNameNoType, primaryPath);
        Set<String> strings = new HashSet<>(formalFiles);
        if (MinioService.bucketTemp.equals(bucket)) {
            // 临时桶和正式桶可能存在相同名称的文件 所以都需要检查
            List<String> tempFiles = minIoUtils.listObjects(MinioService.bucketTemp, fileNameNoType, primaryPath);
            strings.addAll(tempFiles);
        }
        String alreadyFileName;
        if (strings.isEmpty() || !strings.contains(primaryPath + fileName)) {
            // minio 返回的文件全名称为 (文件夹/文件名) 需要拼接文件夹名称和传递的文件名来进行比对
            // 没有此文件名或者文件类型都不匹配 直接上传
            alreadyFileName = primaryPath + fileName;
        } else {
            // 正则表达式只匹配 (x) 格式 x 必须为数字
            Pattern compile = Pattern.compile("\\(\\d+\\)");
            int max = 0;
            alreadyFileName = primaryPath + fileNameNoType;
            ArrayList<Integer> integers = new ArrayList<>();
            for (String string : strings) {
                String bucketFileNameNoType = string.substring(0, string.lastIndexOf("."));
                if (!bucketFileNameNoType.startsWith(alreadyFileName)) {
                    continue;
                }
                // 获取格式为 (x) 的字符串
                String theNum = bucketFileNameNoType.replace(alreadyFileName, "");
                if (!compile.matcher(theNum).matches()) {
                    continue;
                }
                String currentTimes = theNum.replace("(", "").replace(")", "").trim();
                integers.add(Integer.parseInt(currentTimes));
            }
            if (!integers.isEmpty()) {
                max = integers.stream().max(Integer::compareTo).get();
            }
            // max 为当前后缀 0 代表还未添加后缀 最后处理都需要在当前的 max值+1
            alreadyFileName = alreadyFileName + "(" + (max + 1) + ")" + fileType;
        }
        return alreadyFileName;
    }

    public List<String> getDownloadObjectUrl(List<MinioSmallDto> minioDto) {
        return minioDto.stream()
                .map(minioSmallDto -> minIoUtils.getObjectUrl(minioSmallDto.getBucketName(), minioSmallDto.getFileFullName(), "GET"))
                .collect(Collectors.toList());
    }

    public Map<String, Object> initMultiPartUpload(MultiPartDto resource) {
        HashMap<String, Object> map = isNotMd5(resource.getMd5());
        if (map != null) {
            return map;
        }
        String minioFullFileName = getMinioFullFileName(resource.getBucketName(), resource.getFileName());
        return minIoUtils.initMultiPartUpload(resource.getBucketName(), minioFullFileName, resource.getPart());
    }

    public Map<String, String> mergeMultipartUpload(MultiPartDto minioDto) {
        HashMap<String, String> map = new HashMap<>();
        String fileFullName = minioDto.getFileFullName();
        minIoUtils.mergeMultipartUpload(minioDto.getBucketName(), fileFullName, minioDto.getUploadId(), minioDto.getPart());
        map.put("fileFullName", "/" + fileFullName);
        map.put("minioFileName", fileFullName.substring(fileFullName.lastIndexOf("/") + 1));
        return map;
    }

    /**
     * 文件合法性校验
     *
     * @param name 文件名称
     * @param size 文件大小
     */
    public void verifyFile(String name, Integer size) {
        String filePre = "file:" + SecurityUtils.getCurrentUsername();
        //验证上传次数
        Object count = redisUtils.get(filePre);
        if (Objects.nonNull(count) && Integer.parseInt(count.toString()) >= 5) {
            throw new BadRequestException("上传频繁,请稍后再试");
        }
        //验证文件类型
        Map<String, String> map = configiutils.data();
        int i = name.lastIndexOf(".");
        String substring = name.substring(i);
        String filetype = substring.replaceAll("\\.", "");
        if (!map.get("fileType").contains(filetype))
            throw new BadRequestException("不允许上传" + filetype + "类型文件");
        //验证文件大小
        if (Objects.isNull(size) || size == 0) {
            throw new BadRequestException("上传文件不能为空!");
        } else if (size > Long.parseLong(map.get("fileSize")) * 1024 * 1024) {
            throw new BadRequestException("文件超过限定大小，最大为" + Integer.parseInt(map.get("fileSize")) + "M");
        }
    }

    /**
     * 判断是否上传过
     *
     * @param md5 文件唯一标识
     * @return
     */
    private HashMap<String, Object> isNotMd5(String md5) {
        // 判断是否上传过
        if (StringUtils.isNotBlank(md5)) {
            Files files = fileResp.findByMd5(md5);
            if (Objects.nonNull(files)) {
                return new HashMap<>() {{
                    put("fileId", files.getId());
                    put("filePath", files.getPath());
                }};
            }
        }
        return null;
    }
}
