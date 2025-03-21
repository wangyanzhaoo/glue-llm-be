package com.rune.llm.domain.entity;

import com.rune.annotation.PrimaryEntity;
import com.rune.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

/**
 * @author yangkf
 * @date 2025-03-21
 * @description 对话记录表实体
 */
@Schema(title = "对话记录表实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "llm_chat")
@Where(clause = "is_delete = false")
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "是否被删除")
    private Boolean isDelete = false;

    @Schema(description = "")
    private Long userId;

    @Schema(description = "")
    private Long sessionId;

    @Schema(description = "提示词")
    private String prompt;

    @Schema(description = "回答")
    private String content;

    @Schema(description = "类型")
    private String type;


}
