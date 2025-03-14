package com.rune.admin.repository;

import com.rune.admin.domain.entity.Role;
import com.rune.annotation.PrimaryRepositoryMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.Set;

/**
 * @author avalon
 * @date 22/7/14 14:48
 * @description 角色数据接口
 */
@PrimaryRepositoryMarker
public interface RoleRepo extends JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {

    /**
     * 根据角色名查询
     *
     * @param name 角色名
     * @return /
     */
    Optional<Role> findByName(String name);

    /**
     * 根据角色id查询用户角色表
     */
    @Query(value = "select role_id from sys_user_role where 1 = 1", nativeQuery = true)
    Set<Long> countByRoleId();

}
