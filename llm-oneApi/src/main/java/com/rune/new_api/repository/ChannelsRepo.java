package com.rune.new_api.repository;

import com.rune.annotation.SecondaryRepositoryMarker;
import com.rune.new_api.domain.entity.Channels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Set;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description new-api 渠道表接口
 */
@SecondaryRepositoryMarker
public interface ChannelsRepo extends JpaRepository<Channels, Long>, QuerydslPredicateExecutor<Channels> {

    /**
     * 根据 ids 删除
     *
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);
}
