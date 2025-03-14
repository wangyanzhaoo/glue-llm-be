package com.rune.admin.domain.entity;

import com.rune.annotation.PrimaryEntity;
import com.rune.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

/**
 * @author avalon
 * @date 22/3/30 19:38
 * @description /
 */
@Schema(title = "用户实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "sys_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "昵称")
    @NotBlank(message = "昵称不能为空")
    @Length(max = 10)
    private String nickName;

    @Schema(description = "用户名")
    @Column(updatable = false)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "用户名只能为4-10位字母或数字")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入正确的手机号")
    private String phone;

    @Schema(description = "邮箱")
    @Email(message = "请输入正确的邮箱")
    private String email;

    @Schema(description = "是否禁用(字典 userEnabled")
    @NotNull(message = "是否禁用标识不能为空")
    private Boolean enabled;

    @Schema(description = "组织id")
    private Long organizationId;

    @Schema(description = "数据规则")
    @ManyToOne
    @JoinColumn(name = "data_rule_id")
    private DataRule dataRule;

    @Schema(description = "用户角色")
    @ManyToMany
    @JoinTable(name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles;
}
