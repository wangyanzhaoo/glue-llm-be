package com.rune.admin.domain.entity;

import com.rune.annotation.PrimaryEntity;
import com.rune.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;


/**
 * @author avalon
 * @date 22/8/2 11:08
 * @description /
 */
@Schema(title = "字典实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "sys_dict")
public class Dict extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "是否为树状字典")
    private Boolean isTree;

    @Schema(description = "字典名")
    @NotBlank(message = "字典名不能为空")
    @Length(max = 50)
    private String name;

    @Schema(description = "字典编码")
    @NotBlank(message = "字典编码不能为空")
    @Length(max = 20)
    private String code;

    @Schema(description = "备注")
    @Length(max = 100)
    private String remark;

    @Schema(description = "字典项")
    @OneToMany(mappedBy = "pid", orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("sort asc")
    private Set<DictItem> items;
}
