package com.rune.llm.repository;

import com.rune.annotation.PrimaryRepositoryMarker;
import com.rune.llm.domain.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Set;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description 渠道表接口
 */
@PrimaryRepositoryMarker
public interface ChannelRepo extends JpaRepository<Channel, Long>, QuerydslPredicateExecutor<Channel> {

    /**
     * 根据 ids 删除
     *
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);
}
