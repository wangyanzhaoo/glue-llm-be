package com.rune.admin.repository;

import com.rune.admin.domain.entity.DictItem;
import com.rune.annotation.PrimaryRepositoryMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author avalon
 * @date 22/8/2 14:27
 * @description 字典项数据接口
 */
@PrimaryRepositoryMarker
public interface DictItemRepo extends JpaRepository<DictItem, Long>, QuerydslPredicateExecutor<DictItem> {

    /**
     * 根据字典项值查询
     *
     * @param value /
     * @param pid   /
     * @param rid   /
     * @return /
     */
    Optional<DictItem> findByValueAndPidAndRid(String value, Long pid, Long rid);

    /**
     * 根据Rid查询字典项
     *
     * @param rid /
     * @return /
     */
    Integer countByRid(Long rid);

    /**
     * 根据pid查询字典项
     *
     * @param pid /
     * @return  /
     */
    Integer countByPid(Long pid);
}
