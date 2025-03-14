package com.rune.config.domain.mapstruct;

import com.rune.config.domain.entity.Config;
import com.rune.config.domain.vo.ConfigView;
import com.rune.domain.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author wangyz
 * @date 2023/11/30 09:20
 * @description 配置项 Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfigMapper extends BaseMapper<Config, ConfigView> {
}
