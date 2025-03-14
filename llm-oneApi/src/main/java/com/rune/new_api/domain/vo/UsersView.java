package com.rune.new_api.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description new-api 用户表 view
 */
@Getter
@Setter
public class UsersView {

    private Long id;
    private String username;
    private String password;
    private String displayName;
    private Long role;
    private Long status;
    private String email;
    private String githubId;
    private String oidcId;
    private String wechatId;
    private String telegramId;
    private String accessToken;
    private Long quota;
    private Long usedQuota;
    private Long requestCount;
    private String group;
    private String affCode;
    private Long affCount;
    private Long affQuota;
    private Long affHistory;
    private Long inviterId;
    private LocalDateTime deletedAt;
    private String linuxDoId;
    private String setting;
    private Timestamp createTime;

}
