package com.rune.log.repository;

import com.rune.annotation.PrimaryRepositoryMarker;
import com.rune.log.domain.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.sql.Date;
import java.util.List;

/**
 * @author avalon
 * @date 22/8/5 17:04
 * @description 登录日志数据接口
 */
@PrimaryRepositoryMarker
public interface LoginLogRepo extends JpaRepository<LoginLog, Long>, QuerydslPredicateExecutor<LoginLog> {

    /**
     * 根据时间删除
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     */
    void deleteByCreateTimeBetween(Date beginTime, Date endTime);

    /**
     * 根据时间范围查询
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return /
     */
    List<LoginLog> findAllByCreateTimeBetween(Date beginTime, Date endTime);
}
