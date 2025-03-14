package com.rune.admin.domain.mapstruct;

import com.rune.admin.domain.entity.DataRule;
import com.rune.admin.domain.vo.DataRuleView;
import com.rune.domain.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author 大方的脑壳
 * @date 2023/4/20 16:19
 * @description 数据规则 Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataRuleMapper extends BaseMapper<DataRule, DataRuleView> {


}
