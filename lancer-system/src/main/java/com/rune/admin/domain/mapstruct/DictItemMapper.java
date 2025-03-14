package com.rune.admin.domain.mapstruct;

import com.rune.admin.domain.entity.DictItem;
import com.rune.admin.domain.vo.DictItemSmallView;
import com.rune.admin.domain.vo.DictItemView;
import com.rune.domain.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

/**
 * @author avalon
 * @date 22/8/2 14:27
 * @description 字典项 Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictItemMapper extends BaseMapper<DictItem, DictItemView> {

    Set<DictItemSmallView> toItemSmallViews(Set<DictItem> dictItems);

}
