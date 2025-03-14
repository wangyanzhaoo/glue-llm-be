package com.rune.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class JPAQueryFactoryPrimary extends JPAQueryFactory {
    public JPAQueryFactoryPrimary(EntityManager entityManager) {
        super(entityManager);
    }
}
