package com.rune.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;

/**
 * @author 大方的脑壳
 * @date 2022/8/25 11:06
 * @description QueryDsl配置类
 */
@Configuration
public class QueryDslConfig {

    /**
     * 为 primary 数据源创建 JPAQueryFactory
     *
     * @param primaryEntityManager /
     * @return JPAQueryFactoryPrimary
     */
    @Bean
    @Qualifier("primaryJpaQueryFactory")
    public JPAQueryFactoryPrimary primaryJpaQueryFactory(@Qualifier("primaryEntityManagerFactory") EntityManager primaryEntityManager) {
        return new JPAQueryFactoryPrimary(primaryEntityManager);
    }

    /**
     * 为 secondary 数据源创建 JPAQueryFactory
     *
     * @param secondaryEntityManager /
     * @return JPAQueryFactorySecond
     */
    @Bean
    @Qualifier("secondaryJpaQueryFactory")
    public JPAQueryFactorySecond secondaryJpaQueryFactory(@Qualifier("secondaryEntityManagerFactory") EntityManager secondaryEntityManager) {
        return new JPAQueryFactorySecond(secondaryEntityManager);
    }
}
