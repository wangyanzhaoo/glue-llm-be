package com.rune.llm.repository;

import com.rune.annotation.PrimaryRepositoryMarker;
import com.rune.llm.domain.entity.ApiToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Set;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description api令牌表接口
 */
@PrimaryRepositoryMarker
public interface ApiTokenRepo extends JpaRepository<ApiToken, Long>, QuerydslPredicateExecutor<ApiToken> {

    /**
     * 根据 ids 删除
     *
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);
}
