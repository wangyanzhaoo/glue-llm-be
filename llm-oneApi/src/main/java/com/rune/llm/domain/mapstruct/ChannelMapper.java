package com.rune.llm.domain.mapstruct;

import com.rune.domain.BaseMapper;
import com.rune.llm.domain.entity.Channel;
import com.rune.llm.domain.vo.ChannelView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description Channel Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChannelMapper extends BaseMapper<Channel, ChannelView> {


}
