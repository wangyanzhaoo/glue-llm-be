package com.rune.admin.domain.dto;

import com.rune.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author avalon
 * @date 22/3/31 15:24
 * @description /
 */
@Schema(title = "用户表格 搜索DTO")
@Getter
@Setter
public class UserQuery extends BaseQuery {

    private String username;

    private String nickName;

    private String phone;

    private String email;

    private Long organizationId;

    private Boolean enabled;
}
