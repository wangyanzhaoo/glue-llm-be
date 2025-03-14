package com.rune.admin.repository;

import com.rune.admin.domain.entity.Menu;
import com.rune.annotation.PrimaryRepositoryMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author avalon
 * @date 22/7/14 14:48
 * @description 菜单数据接口
 */
@PrimaryRepositoryMarker
public interface MenuRepo extends JpaRepository<Menu, Long>, QuerydslPredicateExecutor<Menu> {

    /**
     * 根据父id查询是否存在子项数据
     *
     * @param pid 父id
     */
    int countByPid(Long pid);

    /**
     * 根据菜单id查询角色菜单表
     *
     * @param id /
     */
    @Query(value = "select count(1) from sys_role_menu where menu_id = ?1", nativeQuery = true)
    int countByMenuId(Long id);
}
