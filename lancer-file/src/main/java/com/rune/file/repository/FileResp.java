package com.rune.file.repository;

import com.rune.annotation.PrimaryRepositoryMarker;
import com.rune.file.domain.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 大方的脑壳
 * @date 2022/8/24 14:28
 * @description 文件管理接口
 */
@Repository
@PrimaryRepositoryMarker
public interface FileResp extends JpaRepository<Files, Long>, QuerydslPredicateExecutor<Files> {

    /**
     * 查找文件信息
     *
     * @param b 是否为临时文件
     */
    List<Files> findByStatus(Boolean b);

    /**
     * 查找文件信息
     *
     * @param md5 文件唯一值
     */
    Integer countByMd5(String md5);

    /**
     * 查找文件信息
     *
     * @param md5 文件唯一值
     */
    Files findByMd5(String md5);

}
