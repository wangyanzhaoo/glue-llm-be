package com.rune.admin.domain.entity;

import com.rune.annotation.PrimaryEntity;
import com.rune.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

/**
 * @author 大方的脑壳
 * @date 2023/4/20 16:19
 * @description /
 */
@Schema(title = "数据规则实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "sys_data_rule")
public class DataRule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "规则名称")
    @NotBlank(message = "规则名称不能为空")
    @Length(max = 50)
    private String name;

    @Schema(description = "规则")
    @NotBlank(message = "规则不能为空")
    @Length(max = 50)
    private String rule;

}
