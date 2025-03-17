package com.rune.new_api.repository;

import com.rune.annotation.SecondaryRepositoryMarker;
import com.rune.new_api.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description new-api 用户表接口
 */
@SecondaryRepositoryMarker
public interface UsersRepo extends JpaRepository<Users, Long>, QuerydslPredicateExecutor<Users> {

}
