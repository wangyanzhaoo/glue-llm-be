package com.rune.llm.repository;

import com.rune.annotation.PrimaryRepositoryMarker;
import com.rune.llm.domain.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Set;

/**
 * @author yangkf
 * @date 2025-03-21
 * @description 会话表接口
 */
@PrimaryRepositoryMarker
public interface SessionRepo extends JpaRepository<Session, Long>, QuerydslPredicateExecutor<Session> {

    /**
     * 根据 ids 删除
     *
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);
}
