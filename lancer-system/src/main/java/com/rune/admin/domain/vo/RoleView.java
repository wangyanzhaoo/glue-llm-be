package com.rune.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author avalon
 * @date 22/7/14 14:48
 * @description /
 */
@Schema(title = "角色 页面VO")
@Getter
@Setter
public class RoleView {

    private Long id;

    private String name;

    private Set<MenuSmallView> menus;

    private String remark;

    private String createBy;

    private Timestamp createTime;
}
