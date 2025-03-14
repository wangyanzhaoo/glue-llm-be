package com.rune.admin.repository;

import com.rune.admin.domain.entity.Organization;
import com.rune.annotation.PrimaryRepositoryMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Set;

/**
 * @author 大方的脑壳
 * @date 2023/4/19 17:11
 * @description 组织架构接口
 */
@PrimaryRepositoryMarker
public interface OrganizationRepo extends JpaRepository<Organization, Long>, QuerydslPredicateExecutor<Organization> {

    /**
     *  根据pid查询组织架构
     *
     * @param pid /
     * @return /
     */
    int countByPid(Long pid);
}
