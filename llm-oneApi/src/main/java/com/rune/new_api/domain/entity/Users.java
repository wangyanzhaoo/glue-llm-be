package com.rune.new_api.domain.entity;

import com.rune.annotation.SecondEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description new-api 用户表实体
 */
@Schema(title = "new-api 用户表实体")
@Getter
@Setter
@Entity
@SecondEntity
@Table(name = "users")
//@Where(clause = "is_delete = false")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "")
    private String username;

    @Schema(description = "")
    private String password;

    @Schema(description = "")
    private String displayName;

    @Schema(description = "")
    private Long role;

    @Schema(description = "")
    private Long status;

    @Schema(description = "")
    private String email;

    @Schema(description = "")
    private String githubId;

    @Schema(description = "")
    private String oidcId;

    @Schema(description = "")
    private String wechatId;

    @Schema(description = "")
    private String telegramId;

    @Schema(description = "")
    private String accessToken;

    @Schema(description = "")
    private Long quota;

    @Schema(description = "")
    private Long usedQuota;

    @Schema(description = "")
    private Long requestCount;

    @Schema(description = "")
    @Column(name = "`group`")
    private String group;

    @Schema(description = "")
    private String affCode;

    @Schema(description = "")
    private Long affCount;

    @Schema(description = "")
    private Long affQuota;

    @Schema(description = "")
    private Long affHistory;

    @Schema(description = "")
    private Long inviterId;

    @Schema(description = "")
    private LocalDateTime deletedAt;

    @Schema(description = "")
    private String linuxDoId;

    @Schema(description = "")
    private String setting;


}
