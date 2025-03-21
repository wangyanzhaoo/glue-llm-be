package com.rune.llm.domain.mapstruct;

import com.rune.domain.BaseMapper;
import com.rune.llm.domain.entity.Session;
import com.rune.llm.domain.vo.SessionView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yangkf
 * @date 2025-03-21
 * @description Session Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionMapper extends BaseMapper<Session, SessionView> {


}
