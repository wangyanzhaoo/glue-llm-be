package com.rune.new_api.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description new-api Api令牌表 view
 */
@Getter
@Setter
public class TokensView {

    private Long id;
    private Long userId;
    private String key;
    private Long status;
    private String name;
    private Long createdTime;
    private Long accessedTime;
    private Long expiredTime;
    private Long remainQuota;
    private Integer unlimitedQuota;
    private Integer modelLimitsEnabled;
    private String modelLimits;
    private String allowIps;
    private Long usedQuota;
    private String group;
    private LocalDateTime deletedAt;
    private Timestamp createTime;

}
