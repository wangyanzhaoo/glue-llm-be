package com.rune.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author avalon
 * @date 22/7/14 14:48
 * @description /
 */
@Schema(title = "菜单 页面VO")
@Getter
@Setter
public class MenuView {

    private Long id;

    private Long pid;

    private Integer type;

    private String path;

    private String name;

    private String icon;

    private String component;

    private Integer sort;

    private String auth;

    private String createBy;

    private Timestamp createTime;
}
