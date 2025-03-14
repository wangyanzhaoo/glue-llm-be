package com.rune.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Avalon
 * @date 2023/10/17 16:19
 * @description /
 */
@Schema(title = "数据规则精简 页面VO")
@Getter
@Setter
public class DataRuleSmallView {

    private Integer id;

    private String name;

    private String rule;
}
