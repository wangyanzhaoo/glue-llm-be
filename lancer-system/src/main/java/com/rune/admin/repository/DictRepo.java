package com.rune.admin.repository;

import com.rune.admin.domain.entity.Dict;
import com.rune.annotation.PrimaryRepositoryMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author avalon
 * @date 22/8/2 14:27
 * @description 字典数据接口
 */
@PrimaryRepositoryMarker
public interface DictRepo extends JpaRepository<Dict, Long>, QuerydslPredicateExecutor<Dict> {

    /**
     * 根据字典编码查询
     *
     * @param code 字典编码
     * @return /
     */
    Optional<Dict> findByCode(String code);
}
