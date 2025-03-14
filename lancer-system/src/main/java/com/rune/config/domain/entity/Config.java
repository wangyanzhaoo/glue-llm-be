package com.rune.config.domain.entity;

import com.rune.annotation.PrimaryEntity;
import com.rune.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;


/**
 * @author wangyz
 * @date 2023/11/30 09:20
 * @description /
 */
@Schema(title = "配置项实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "sys_config")
@SQLRestriction("is_delete = false")
public class Config extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "配置名称")
    @Length(max = 50)
    private String name;

    @Schema(description = "配置标识")
    @NotBlank(message = "配置标识不能为空")
    @Length(max = 50)
    private String code;

    @Schema(description = "配置值")
    @NotBlank(message = "配置值不能为空")
    @Length(max = 200)
    private String value;

    @Schema(description = "是否删除")
    private Boolean isDelete = false;

}
