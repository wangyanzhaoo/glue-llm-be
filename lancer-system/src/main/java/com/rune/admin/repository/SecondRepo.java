package com.rune.admin.repository;

import com.rune.admin.domain.entity.Second;
import com.rune.annotation.SecondaryRepositoryMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

@SecondaryRepositoryMarker
public interface SecondRepo extends JpaRepository<Second, Long>, QuerydslPredicateExecutor<Second> {
}
