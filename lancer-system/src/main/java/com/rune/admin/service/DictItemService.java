package com.rune.admin.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rune.admin.domain.dto.DictItemQuery;
import com.rune.admin.domain.entity.Dict;
import com.rune.admin.domain.entity.DictItem;
import com.rune.admin.domain.entity.QDict;
import com.rune.admin.domain.entity.QDictItem;
import com.rune.admin.domain.mapstruct.DictItemMapper;
import com.rune.admin.domain.vo.DictItemSmallView;
import com.rune.admin.domain.vo.DictItemView;
import com.rune.admin.repository.DictItemRepo;
import com.rune.exception.BadRequestException;
import com.rune.querydsl.JPAQueryFactoryPrimary;
import com.rune.utils.RedisUtils;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author avalon
 * @date 22/8/2 14:46
 * @description 字典项接口实现
 */
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "dict")
public class DictItemService {

    private final DictItemRepo dictItemRepo;

    private final DictItemMapper dictItemMapper;

    private final DictService dictService;

    private final JPAQueryFactoryPrimary queryFactory;

    private final RedisUtils redisUtils;

    public Page<DictItemView> queryAll(DictItemQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        QDictItem qDictItem = QDictItem.dictItem;
        builder.and(qDictItem.pid.eq(query.getPid()));
        if (StringUtils.isNotBlank(query.getText())) builder.and(qDictItem.text.contains(query.getText()));

        Page<DictItem> page = dictItemRepo.findAll(builder, PageRequest.of(query.getCurrent(), query.getPageSize(), Sort.Direction.ASC, "sort"));
        return page.map(dictItemMapper::toVo);
    }

    /**
     * 设置字典
     *
     * @param pid
     */
    public void queryByPidCode(Long pid) {
        QDict qDict = QDict.dict;
        Dict dict = queryFactory.select(qDict).from(qDict).where(qDict.id.eq(pid)).fetchFirst();
        Set<DictItem> dictItems = dictService.queryOne(dict.getCode());
        Set<DictItemSmallView> itemSmallViews = null;
        if (dict.getIsTree()) {
            itemSmallViews = composeTree(dictItems);
        } else {
            itemSmallViews = dictItemMapper.toItemSmallViews(dictItems);
        }
        redisUtils.set(dictService.dictPrefix + dict.getCode(), itemSmallViews);
    }

    /**
     * 将字典项组成树
     * @param dictItemsAll /
     */
    private HashSet<DictItemSmallView> composeTree(Set<DictItem> dictItemsAll) {
        Map<Long, List<DictItem>> map = dictItemsAll.stream().collect(Collectors.groupingBy(DictItem::getRid));
        List<DictItem> dictItems = map.get(0L);
        HashSet<DictItemSmallView> itemSmallViews = new HashSet<>();
        for (DictItem dictItem : dictItems) {
            DictItemSmallView dictItemSmallView = new DictItemSmallView();
            dictItemSmallView.setText(dictItem.getText());
            dictItemSmallView.setValue(dictItem.getValue());
            dictService.composeDictTree(dictItem, map, dictItemSmallView);
            itemSmallViews.add(dictItemSmallView);
        }
        return itemSmallViews;
    }

    public void create(DictItem resources) {
        QDictItem qDictItem = QDictItem.dictItem;
        dictItemRepo.findByValueAndPidAndRid(resources.getValue(), resources.getPid(), resources.getRid()).ifPresent(d -> {
            throw new BadRequestException("字典项值: " + resources.getValue() + " 已经存在");
        });
        Long count = queryFactory.select(qDictItem.count()).from(qDictItem).where(
                qDictItem.pid.eq(resources.getPid()).and(qDictItem.rid.eq(resources.getRid())).and(qDictItem.sort.eq(resources.getSort()))
        ).fetchFirst();
        if (count != 0) {
            reformSort(qDictItem, resources);
        }
        dictItemRepo.save(resources);
        queryByPidCode(resources.getPid());
    }

    public void update(DictItem resources) {
        QDictItem qDictItem = QDictItem.dictItem;
        DictItem dictItem = dictItemRepo.findById(resources.getId()).orElseThrow(
                () -> new BadRequestException("字典项不存在")
        );
        resources.setValue(null);
        resources.setPid(null);
        resources.setRid(null);
        UpdateUtil.copyNullProperties(dictItem, resources);
        Long count = queryFactory.select(qDictItem.count()).from(qDictItem).where(
                qDictItem.pid.eq(resources.getPid()).and(qDictItem.rid.eq(resources.getRid()))
                        .and(qDictItem.sort.eq(resources.getSort())).and(qDictItem.id.ne(resources.getId()))
        ).fetchFirst();
        if (count != 0) {
            reformSort(qDictItem, resources);
        }
        dictItemRepo.save(resources);
        queryByPidCode(resources.getPid());
    }

    /**
     * 调整字典项目的排序顺序。
     *
     * @param qDictItem DictItem 实体的查询类型
     * @param dictItem  要调整排序顺序的字典项目
     */
    private void reformSort(QDictItem qDictItem, DictItem dictItem) {
        List<DictItem> fetch = queryFactory.selectFrom(qDictItem).where(
                qDictItem.pid.eq(dictItem.getPid()).and(qDictItem.rid.eq(dictItem.getRid())).and(qDictItem.sort.goe(dictItem.getSort()))).fetch();
        Integer sort = dictItem.getSort();
        for (DictItem item : fetch) {
            sort++;
            item.setSort(sort);
            dictItemRepo.save(item);
        }
    }


    public void delete(Set<Long> ids) {
        // TODO 删除选中父节点包含所有子节点也会报错
        QDictItem qDictItem = QDictItem.dictItem;
        Long pid = null;
        for (Long id : ids) {
            pid = queryFactory.select(qDictItem.pid).from(qDictItem).where(qDictItem.id.eq(id)).fetchFirst();
            break;
        }
        ids.forEach(id -> {
            if (dictItemRepo.countByRid(id) > 0)
                throw new BadRequestException("字典项存在子节点，请先删除所有子节点");
        });
        dictItemRepo.deleteAllByIdInBatch(ids);
        queryByPidCode(pid);
    }
}
