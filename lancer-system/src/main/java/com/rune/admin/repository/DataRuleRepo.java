package com.rune.admin.repository;

import com.rune.admin.domain.entity.DataRule;
import com.rune.annotation.PrimaryRepositoryMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author 大方的脑壳
 * @date 2023/4/20 16:19
 * @description 数据规则接口
 */
@PrimaryRepositoryMarker
public interface DataRuleRepo extends JpaRepository<DataRule, Long>, QuerydslPredicateExecutor<DataRule> {

    /**
     * 根据 id 删除
     *
     * @param id /
     */
    void deleteById(Long id);
}
