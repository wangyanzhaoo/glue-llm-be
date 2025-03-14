package com.rune.admin.domain.dto;

import com.rune.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author avalon
 * @date 22/7/14 15:06
 * @description /
 */
@Schema(title = "角色表格 搜索DTO")
@Getter
@Setter
public class RoleQuery extends BaseQuery {

    private String name;
}
