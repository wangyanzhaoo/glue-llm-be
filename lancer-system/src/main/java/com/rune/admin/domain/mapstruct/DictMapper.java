package com.rune.admin.domain.mapstruct;

import com.rune.admin.domain.entity.Dict;
import com.rune.admin.domain.vo.DictSmallView;
import com.rune.admin.domain.vo.DictView;
import com.rune.domain.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author avalon
 * @date 22/8/2 14:27
 * @description 字典 Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictMapper extends BaseMapper<Dict, DictView> {

    DictSmallView toSmallVo(Dict dict);
}
