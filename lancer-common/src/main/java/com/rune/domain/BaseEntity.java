package com.rune.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

/**
 * @author avalon
 * @date 22/4/28 16:05
 * @description /
 */
@Schema(title = "基本实体")
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Schema(description = "创建人")
    @CreatedBy
    @Column(updatable = false)
    private String createBy;

    @Schema(description = "创建时间")
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createTime;

    @Schema(description = "更新人")
    @LastModifiedBy
    @Column(insertable = false)
    private String updateBy;

    @Schema(description = "更新时间")
    @UpdateTimestamp
    @Column(insertable = false)
    private Timestamp updateTime;
}
