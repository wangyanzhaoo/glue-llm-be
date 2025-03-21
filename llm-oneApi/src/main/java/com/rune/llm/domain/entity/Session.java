package com.rune.llm.domain.entity;

import com.rune.annotation.PrimaryEntity;
import com.rune.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yangkf
 * @date 2025-03-21
 * @description 会话表实体
 */
@Schema(title = "会话表实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "llm_session")
//@Where(clause = "is_delete = false")
public class Session extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "是否被删除")
    private Boolean isDelete = false;

    @Schema(description = "")
    private Long userId;

    @Schema(description = "会话名")
    private String title;

    @Schema(description = "对话次数")
    private Integer count;


}
