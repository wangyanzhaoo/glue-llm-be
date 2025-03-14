package com.rune.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 大方的脑壳
 * @date 2023/4/19 17:11
 * @description /
 */
@Schema(title = "组织架构 页面VO")
@Getter
@Setter
public class OrganizationView {

    private Integer id;

    private String name;

    private Integer pid;

    private String hierarchy;

    private Integer depth;

    private Integer sort;

    private String remarks;
}
