package com.rune.llm.domain.mapstruct;

import com.rune.domain.BaseMapper;
import com.rune.llm.domain.entity.ApiToken;
import com.rune.llm.domain.vo.ApiTokenView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description ApiToken Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApiTokenMapper extends BaseMapper<ApiToken, ApiTokenView> {


}
