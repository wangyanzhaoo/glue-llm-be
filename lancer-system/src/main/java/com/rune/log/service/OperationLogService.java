package com.rune.log.service;

import com.alibaba.excel.EasyExcel;
import com.querydsl.core.BooleanBuilder;
import com.rune.exception.BadRequestException;
import com.rune.log.domain.dto.OperationLogQuery;
import com.rune.log.domain.entity.OperationLog;
import com.rune.log.domain.entity.QOperationLog;
import com.rune.log.domain.mapstruct.OperationLogMapper;
import com.rune.log.domain.vo.OperationLogView;
import com.rune.log.repository.OperationLogRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author avalon
 * @date 22/8/4 17:04
 * @description 操作日志接口实现
 */
@RequiredArgsConstructor
@Service
public class OperationLogService {

    private final OperationLogRepo operationLogRepo;

    private final OperationLogMapper operationLogMapper;

    public Page<OperationLogView> queryAll(OperationLogQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        QOperationLog qOperationLog = QOperationLog.operationLog;
        if (StringUtils.isNotBlank(query.getMsg())) builder.and(qOperationLog.msg.contains(query.getMsg()));
        if (Objects.nonNull(query.getType())) builder.and(qOperationLog.type.eq(query.getType()));
        if (StringUtils.isNotBlank(query.getUsername())) builder.and(qOperationLog.username.contains(query.getUsername()));
        if (StringUtils.isNotBlank(query.getIp())) builder.and(qOperationLog.ip.contains(query.getIp()));
        if (StringUtils.isNotBlank(query.getMethod())) builder.and(qOperationLog.method.contains(query.getMethod()));

        Page<OperationLog> page = operationLogRepo.findAll(builder, PageRequest.of(query.getCurrent(), query.getPageSize(),Sort.by("createTime").descending()));
        return page.map(operationLogMapper::toVo);
    }

    @Async
    public void create(OperationLog resources) {
        operationLogRepo.save(resources);
    }

    public void delete(Set<Long> ids) {
        operationLogRepo.deleteAllByIdInBatch(ids);
    }

    @Transactional
    public void deleteByCreateTimeRange(Integer type) {
        // XXX优化为字典
        int range = 0;
        switch (type) {
            case 1 -> range = 1;
            case 2 -> range = 7;
            case 3 -> range = 30;
            case 4 -> operationLogRepo.deleteAllInBatch();
        }
        //删除所有数据后无需进行二次删除
        if (type != 4) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -range);
            operationLogRepo.deleteByCreateTimeBetween(new Date(calendar.getTimeInMillis()), new Date(System.currentTimeMillis()));
        }
    }

    public void exportExcel(HttpServletResponse response, LocalDate beginTime, LocalDate endTime) throws Exception {
        endTime = endTime.plusDays(1);
        long betweenDay = ChronoUnit.DAYS.between(beginTime, endTime);
        if (betweenDay <= 60) {
            List<OperationLogView> operationLogViewList = operationLogMapper.toVo(operationLogRepo.findAllByCreateTimeBetween(Date.valueOf(beginTime), Date.valueOf(endTime)));
            EasyExcel.write(response.getOutputStream(), OperationLogView.class).sheet("操作日志").doWrite(operationLogViewList);
        } else {
            throw new BadRequestException("时间范围超过60天");
        }
    }
}
