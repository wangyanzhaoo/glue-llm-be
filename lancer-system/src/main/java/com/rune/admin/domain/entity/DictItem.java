package com.rune.admin.domain.entity;

import com.rune.annotation.PrimaryEntity;
import com.rune.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * @author avalon
 * @date 22/8/2 11:08
 * @description /
 */
@Schema(title = "字典项实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "sys_dict_item")
public class DictItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "父ID")
    @NotNull(message = "字典父Id不能为空")
    private Long pid;

    @Schema(description = "字典详情父ID")
    @NotNull(message = "字典详情父id不能为空")
    private Long rid;

    @Schema(description = "深度级别")
    @NotNull(message = "深度级别不能为空")
    private Integer level;

    @Schema(description = "字典项文本")
    @NotBlank(message = "字典项文本不能为空")
    @Length(max = 50)
    private String text;

    @Schema(description = "字典项值")
    @NotBlank(message = "字典项值不能为空")
    @Length(max = 50)
    private String value;

    @Schema(description = "排序")
    @NotNull(message = "排序不能为空")
    @Max(99)
    private Integer sort;

    @Schema(description = "备注")
    @Length(max = 100)
    private String remark;
}
