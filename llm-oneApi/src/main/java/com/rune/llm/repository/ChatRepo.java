package com.rune.llm.repository;

import com.rune.annotation.PrimaryRepositoryMarker;
import com.rune.llm.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Set;

/**
 * @author yangkf
 * @date 2025-03-21
 * @description 对话记录表接口
 */
@PrimaryRepositoryMarker
public interface ChatRepo extends JpaRepository<Chat, Long>, QuerydslPredicateExecutor<Chat> {

    /**
     * 根据 ids 删除
     *
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);
}
