package com.rune.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author avalon
 * @date 22/3/30 19:38
 * @description /
 */
@Schema(title = "用户 页面VO")
@Getter
@Setter
public class UserView {

    private Long id;

    private String nickName;

    private String username;

    private String phone;

    private String email;

    private Boolean enabled;

    private Integer organizationId;

    private DataRuleSmallView dataRule;

    private Set<RoleSmallView> roles;

    private String createBy;

    private Timestamp createTime;

    private String group;

    private String usedLimit;

    private String remainLimit;

    private Long requestCount;

}
