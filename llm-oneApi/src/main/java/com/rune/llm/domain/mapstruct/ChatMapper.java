package com.rune.llm.domain.mapstruct;

import com.rune.domain.BaseMapper;
import com.rune.llm.domain.entity.Chat;
import com.rune.llm.domain.vo.ChatView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yangkf
 * @date 2025-03-21
 * @description Chat Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper extends BaseMapper<Chat, ChatView> {


}
