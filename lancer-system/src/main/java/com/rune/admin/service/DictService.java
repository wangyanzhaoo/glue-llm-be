package com.rune.admin.service;

import com.querydsl.core.BooleanBuilder;
import com.rune.admin.domain.dto.DictQuery;
import com.rune.admin.domain.entity.Dict;
import com.rune.admin.domain.entity.DictItem;
import com.rune.admin.domain.entity.QDict;
import com.rune.admin.domain.mapstruct.DictMapper;
import com.rune.admin.domain.vo.DictItemSmallView;
import com.rune.admin.domain.vo.DictSmallView;
import com.rune.admin.domain.vo.DictView;
import com.rune.admin.repository.DictItemRepo;
import com.rune.admin.repository.DictRepo;
import com.rune.exception.BadRequestException;
import com.rune.utils.RedisUtils;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author avalon
 * @date 22/8/2 14:46
 * @description 字典接口实现
 */
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "dict")
public class DictService {

    private final DictRepo dictRepo;

    private final DictItemRepo dictItemRepo;

    private final DictMapper dictMapper;

    private final RedisUtils redisUtils;

    public final String dictPrefix = "dict::";

    public List<DictSmallView> data() {
        List<Dict> all = dictRepo.findAll();
        ArrayList<DictSmallView> dictSmallViews = new ArrayList<>();
        for (Dict dict : all) {
            if (dict.getIsTree() && dict.getItems() != null) {
                dictSmallViews.add(composeDictSmallView(dict));
            } else {
                dictSmallViews.add(dictMapper.toSmallVo(dict));
            }
        }
        for (DictSmallView dictSmallView : dictSmallViews) {
            redisUtils.set(dictPrefix + dictSmallView.getCode(), dictSmallView.getItems());
        }
        return dictSmallViews;
    }

    /**
     * 将一个 Dict 对象组装成一个 DictSmallView 对象。
     *
     * @param dict 要组装的 Dict 对象
     * @return 组装后的 DictSmallView 对象
     */
    private DictSmallView composeDictSmallView(Dict dict) {
        DictSmallView dictSmallView = new DictSmallView();
        dictSmallView.setCode(dict.getCode());
        HashSet<DictItemSmallView> itemSmallViews = new HashSet<>();
        Map<Long, List<DictItem>> map = dict.getItems().stream().collect(Collectors.groupingBy(DictItem::getRid));
        // 取出所有顶层父节点
        List<DictItem> dictItems = map.get(0L);
        if (dictItems == null) {
            dictSmallView.setItems(itemSmallViews);
            return dictSmallView;
        }
        for (DictItem dictItem : dictItems) {
            DictItemSmallView dictItemSmallView = new DictItemSmallView();
            dictItemSmallView.setText(dictItem.getText());
            dictItemSmallView.setValue(dictItem.getValue());
            composeDictTree(dictItem, map, dictItemSmallView);
            itemSmallViews.add(dictItemSmallView);
        }
        dictSmallView.setItems(itemSmallViews);
        return dictSmallView;
    }

    /**
     * 递归组装 DictItemSmallView 树形结构
     *
     * @param dictItem          /
     * @param map               根据 rid 分组后的 map
     * @param dictItemSmallView /
     */
    public void composeDictTree(DictItem dictItem, Map<Long, List<DictItem>> map, DictItemSmallView dictItemSmallView) {
        List<DictItem> dictItems = map.get(dictItem.getId());
        List<DictItemSmallView> itemSmallViews = new ArrayList<>();
        if (dictItems == null) {
            dictItemSmallView.setChildren(itemSmallViews);
            return;
        }
        for (DictItem item : dictItems) {
            DictItemSmallView view = new DictItemSmallView();
            view.setText(item.getText());
            view.setValue(item.getValue());
            List<DictItem> items = map.get(item.getId());
            if (items != null && !items.isEmpty()) {
                composeDictTree(item, map, view);
            }
            itemSmallViews.add(view);
        }
        dictItemSmallView.setChildren(itemSmallViews);
    }

    public Page<DictView> queryAll(DictQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        QDict qDict = QDict.dict;
        if (StringUtils.isNotBlank(query.getName())) builder.and(qDict.name.contains(query.getName()));

        Page<Dict> page = dictRepo.findAll(builder, PageRequest.of(query.getCurrent(), query.getPageSize()));
        return page.map(dictMapper::toVo);
    }

    public Set<DictItem> queryOne(String code) {
        Dict dict = dictRepo.findByCode(code).orElseThrow(() -> new BadRequestException("字典不存在"));
        return dict.getItems();
    }

    public void create(Dict resources) {
        dictRepo.findByCode(resources.getCode()).ifPresent(d -> {
            throw new BadRequestException("字典编码: " + resources.getCode() + " 已经存在");
        });
        dictRepo.save(resources);
        redisUtils.set(dictPrefix + resources.getCode(), new HashSet<>());
    }

    public void update(Dict resources) {
        Dict dict = dictRepo.findById(resources.getId()).orElseThrow(
                () -> new BadRequestException("字典不存在")
        );
        resources.setCode(null);
        UpdateUtil.copyNullProperties(dict, resources);
        dictRepo.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (dictItemRepo.countByPid(id) > 0)
            throw new BadRequestException("字典项存在子节点，请先删除所有子节点");
        Dict dict = dictRepo.findById(id).orElseThrow(
                () -> new BadRequestException("字典不存在")
        );
        String code = dict.getCode();
        redisUtils.delete(dictPrefix + code);
        dictRepo.deleteById(id);
    }
}
