package com.rune.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class JPAQueryFactorySecond extends JPAQueryFactory {
    public JPAQueryFactorySecond(EntityManager entityManager) {
        super(entityManager);
    }
}