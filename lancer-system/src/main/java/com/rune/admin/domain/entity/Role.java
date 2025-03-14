package com.rune.admin.domain.entity;

import com.rune.annotation.PrimaryEntity;
import com.rune.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author avalon
 * @date 22/3/31 10:30
 * @description /
 */
@Schema(title = "角色实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "sys_role")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "角色名")
    @NotBlank(message = "角色名不能为空")
    @Length(max = 10)
    private String name;

    @Schema(description = "备注")
    @Length(max = 100)
    private String remark;

    @Schema(description = "描述")
    @ManyToMany
    @JoinTable(name = "sys_role_menu",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
    private Set<Menu> menus;

}
