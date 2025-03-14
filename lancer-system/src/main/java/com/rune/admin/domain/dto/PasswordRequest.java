package com.rune.admin.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author 大方的脑壳
 * @date 2022-08-03 17:11
 * @description /
 */
@Schema(title = "修改密码 请求DTO")
@Getter
@Setter
public class PasswordRequest {

    @NotBlank(message = "旧密码不能为空")
    @Size(min = 6, max = 15, message = "密码长度最小为6，最大为15")
    String oldPassWord;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 15, message = "密码长度最小为6，最大为15")
    String newPassWord;

    @NotBlank(message = "验证密码不能为空")
    @Size(min = 6, max = 15, message = "密码长度最小为6，最大为15")
    String confirmPassWord;
}
