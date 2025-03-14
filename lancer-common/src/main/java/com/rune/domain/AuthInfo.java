package com.rune.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author sedate
 * @date 2023/4/21 14:52
 * @description /
 */
@Schema(title = "缓存权限信息")
@Getter
@Setter
public class AuthInfo {

    // 当前所属规则
    private String rule;

    //当前所属组织id
    private Long organizationId;

    // 是否可以查看全部
    private Boolean lookAll = false;

    // 允许的组织id
    private Set<Long> allowOrganization;
}
