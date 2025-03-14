package com.rune.config.repository;

import com.rune.annotation.PrimaryRepositoryMarker;
import com.rune.config.domain.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

/**
 * @author wangyz
 * @date 2023/11/30 09:20
 * @description 配置项接口
 */
@PrimaryRepositoryMarker
public interface ConfigRepo extends JpaRepository<Config, Long>, QuerydslPredicateExecutor<Config> {

    /**
     * 根据配置项的configKey获取配置项信息
     * @param key /
     * @return /
     */
    Optional<Config> findByCode(String key);
}
