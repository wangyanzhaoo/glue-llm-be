package com.rune.admin.domain.entity;

import com.rune.admin.domain.privider.MenuGroupSequenceProvider;
import com.rune.annotation.PrimaryEntity;
import com.rune.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.group.GroupSequenceProvider;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * @author avalon
 * @date 22/3/31 10:32
 * @description /
 */
@Schema(title = "菜单权限实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "sys_menu")
@GroupSequenceProvider(MenuGroupSequenceProvider.class)
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "父ID")
    @NotNull(message = "父ID不能为空")
    private Long pid;

    @Schema(description = "路由类型(0 菜单，1 页面，2 按钮")
    @NotNull(message = "路由类型不能为空")
    private Integer type;

    @Schema(description = "路由地址")
    @NotBlank(groups = {menuGroup.class, pathGroup.class}, message = "非按钮类型路由地址不能为空")
    @Null(groups = buttonGroup.class, message = "按钮类型路由地址必须为空")
    @Length(max = 50)
    private String path;

    @Schema(description = "路由名")
    @NotBlank(message = "路由名不能为空")
    @Length(max = 10)
    private String name;

    @Schema(description = "图标")
    @Null(groups = buttonGroup.class, message = "按钮类型图标必须为空")
    @Length(max = 20)
    private String icon;

    @Schema(description = "组件路径")
    @Null(groups = {buttonGroup.class, menuGroup.class}, message = "非页面类型组件路径必须为空")
    @NotBlank(groups = pathGroup.class, message = "页面类型组件路径不能为空")
    @Length(max = 100)
    private String component;

    @Schema(description = "排序值")
    @NotNull(message = "排序不能为空")
    @Max(99)
    private Integer sort;

    @Schema(description = "权限")
    @NotBlank(message = "按钮类型权限标识符不能为空", groups = buttonGroup.class)
    @Null(message = "非按钮类型权限标识符必须为空", groups = {menuGroup.class, pathGroup.class})
    @Length(max = 30)
    private String auth;

    //菜单组
    public @interface menuGroup {
    }

    //页面组
    public @interface pathGroup {
    }

    //按钮组
    public @interface buttonGroup {
    }

}
