package com.rune.admin.repository;

import com.rune.admin.domain.entity.User;
import com.rune.annotation.PrimaryRepositoryMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author avalon
 * @date 22/3/30 19:38
 * @description 用户数据接口
 */
@PrimaryRepositoryMarker
public interface UserRepo extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username /
     * @return /
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据数据规则查询用户
     *
     * @param dataRuleId /
     * @return /
     */
    Integer countByDataRuleId(Long dataRuleId);

    /**
     * 根据数据规则查询用户
     *
     * @param organizationId /
     * @return /
     */
    Integer countByOrganizationId(Long organizationId);

    /**
     * 根据用户数据规则id查询用户
     *
     * @param userName /
     * @return /
     */
    List<User> findByUsernameIn(Set<String> userName);
}
