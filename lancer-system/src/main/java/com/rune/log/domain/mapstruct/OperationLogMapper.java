package com.rune.log.domain.mapstruct;

import com.rune.domain.BaseMapper;
import com.rune.log.domain.entity.OperationLog;
import com.rune.log.domain.vo.OperationLogView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
 * @author avalon
 * @date 22/8/4 17:04
 * @description 操作日志 Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OperationLogMapper extends BaseMapper<OperationLog, OperationLogView> {
}
