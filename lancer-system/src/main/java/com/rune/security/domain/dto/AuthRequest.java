package com.rune.security.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

/**
 * @author avalon
 * @date 22/3/30 19:38
 * @description /
 */
@Schema(title = "登录认证 请求DTO")
@Getter
@Setter
public class AuthRequest {

    @Schema(description = "用户名")
    @NotBlank
    private String username;

    @Schema(description = "密码")
    @NotBlank
    private String password;

    @Schema(description = "验证码")
    @NotBlank
    private String captcha;

    @Schema(description = "保持登录")
    private Boolean autoLogin;

    @Schema(description = "uuid")
    @NotBlank
    private String uuid;

    @Schema(description = "登录类型")
    private String type;
}
