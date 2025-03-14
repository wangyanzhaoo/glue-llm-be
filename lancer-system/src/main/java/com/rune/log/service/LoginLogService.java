package com.rune.log.service;

import com.alibaba.excel.EasyExcel;
import com.querydsl.core.BooleanBuilder;
import com.rune.exception.BadRequestException;
import com.rune.log.domain.dto.LoginLogQuery;
import com.rune.log.domain.entity.LoginLog;
import com.rune.log.domain.entity.QLoginLog;
import com.rune.log.domain.mapstruct.LoginLogMapper;
import com.rune.log.domain.vo.LoginLogView;
import com.rune.log.repository.LoginLogRepo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author avalon
 * @date 22/8/5 17:04
 * @description 登录日志接口实现
 */
@RequiredArgsConstructor
@Service
public class LoginLogService {

    private final LoginLogRepo loginLogRepo;

    private final LoginLogMapper loginLogMapper;

    public Page<LoginLogView> queryAll(LoginLogQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        QLoginLog qLoginLog = QLoginLog.loginLog;
        if (StringUtils.isNotBlank(query.getMsg())) builder.and(qLoginLog.msg.contains(query.getMsg()));
        if (Objects.nonNull(query.getType())) builder.and(qLoginLog.type.eq(query.getType()));
        if (StringUtils.isNotBlank(query.getUsername())) builder.and(qLoginLog.username.contains(query.getUsername()));
        if (StringUtils.isNotBlank(query.getIp())) builder.and(qLoginLog.ip.contains(query.getIp()));

        Page<LoginLog> page = loginLogRepo.findAll(builder, PageRequest.of(query.getCurrent(), query.getPageSize(), Sort.by("createTime").descending()));
        return page.map(loginLogMapper::toVo);
    }

    @Async
    public void create(LoginLog resources) {
        loginLogRepo.save(resources);
    }

    public void delete(Set<Long> ids) {
        loginLogRepo.deleteAllByIdInBatch(ids);
    }

    @Transactional
    public void deleteByCreateTimeRange(Integer type) {
        // XXX优化为字典
        int range = 0;
        switch (type) {
            case 1 -> range = 1;
            case 2 -> range = 7;
            case 3 -> range = 30;
            case 4 -> loginLogRepo.deleteAllInBatch();
        }
        //删除所有数据后无需进行二次删除
        if (type != 4) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -range);
            loginLogRepo.deleteByCreateTimeBetween(new Date(calendar.getTimeInMillis()), new Date(System.currentTimeMillis()));
        }
    }

    public void exportExcel(HttpServletResponse response, LocalDate beginTime, LocalDate endTime) throws Exception {
        endTime = endTime.plusDays(1);
        long betweenDay = ChronoUnit.DAYS.between(beginTime, endTime);
        if (betweenDay <= 60) {
            List<LoginLogView> loginLogViews = loginLogMapper.toVo(loginLogRepo.findAllByCreateTimeBetween(Date.valueOf(beginTime),Date.valueOf(endTime)));
            EasyExcel.write(response.getOutputStream(), LoginLogView.class).sheet("登录日志").doWrite(loginLogViews);
        } else {
            throw new BadRequestException("时间范围超过60天");
        }
    }
}
