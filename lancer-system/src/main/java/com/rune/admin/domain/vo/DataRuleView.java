package com.rune.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author 大方的脑壳
 * @date 2023/4/20 16:19
 * @description /
 */
@Schema(title = "数据规则 页面VO")
@Getter
@Setter
public class DataRuleView {

    private Integer id;

    private String name;

    private String rule;

    private String createBy;

    private Timestamp createTime;
}
