package com.rune.admin.domain.entity;

import com.rune.annotation.PrimaryEntity;
import com.rune.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

/**
 * @author 大方的脑壳
 * @date 2023/4/19 17:11
 * @description /
 */
@Schema(title = "组织架构实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "sys_organization")
public class Organization extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Schema(description = "组织名称")
    @NotBlank(message = "组织名称不能为空")
    private String name;

    @Schema(description = "父级ID")
    private Long pid;

    @Schema(description = "层级信息，从根节点到当前节点的最短路径，使用-分割节点ID")
    private String hierarchy = "0";

    @Schema(description = "当前节点深度")
    private Integer depth = 1;

    @Schema(description = "排序字段，由小到大")
    @NotNull(message = "排序值不能为空")
    private Integer sort;

    @Schema(description = "备注")
    private String remarks;

}
