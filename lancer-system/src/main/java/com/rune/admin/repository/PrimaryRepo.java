package com.rune.admin.repository;

import com.rune.admin.domain.entity.Primary;
import com.rune.annotation.PrimaryRepositoryMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

@PrimaryRepositoryMarker
public interface PrimaryRepo extends JpaRepository<Primary, Long>, QuerydslPredicateExecutor<Primary> {
}
