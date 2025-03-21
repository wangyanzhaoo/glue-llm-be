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
 * @date 2025-03-14
 * @description 渠道表实体
 */
@Schema(title = "渠道表实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "llm_channel")
@Where(clause = "is_delete = false")
public class Channel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "是否被删除")
    private Boolean isDelete = false;

    @Schema(description = "key")
    @Column(name = "`key`")
    private String key;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "分组")
    @Column(name = "`group`")
    private String group;

    @Schema(description = "类型")
    private Long type;

    @Schema(description = "状态")
    private Long status;

    @Schema(description = "响应时间")
    private Long responseTime;

    @Schema(description = "已用额度")
    private String usedLimit;

    @Schema(description = "剩余额度")
    private String remainLimit;

    @Schema(description = "剩余额度修改时间")
    private Long remainLimitUpdateTime;

    @Schema(description = "选择模型")
    private String models;

    @Schema(description = "优先级")
    private Long priority;

    @Schema(description = "权重")
    private Long weight;

    @Schema(description = "自动禁用")
    private Long autoBan;


}
