package com.rune.log.domain.mapstruct;

import com.rune.domain.BaseMapper;
import com.rune.log.domain.entity.LoginLog;
import com.rune.log.domain.vo.LoginLogView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
 * @author avalon
 * @date 22/8/5 17:04
 * @description 登录日志 Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoginLogMapper extends BaseMapper<LoginLog, LoginLogView> {
}
