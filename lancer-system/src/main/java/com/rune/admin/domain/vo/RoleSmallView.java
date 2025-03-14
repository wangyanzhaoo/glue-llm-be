package com.rune.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author avalon
 * @date 22/7/14 14:48
 * @description /
 */
@Schema(title = "角色精简 页面VO")
@Getter
@Setter
public class RoleSmallView {

    private Long id;

    private String name;
}
