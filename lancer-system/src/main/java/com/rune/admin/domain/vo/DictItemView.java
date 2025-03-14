package com.rune.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author avalon
 * @date 22/8/2 11:08
 * @description /
 */
@Schema(title = "字典项 页面VO")
@Getter
@Setter
public class DictItemView {

    private Long id;

    private Long pid;

    private Long rid;

    private Integer level;

    private String text;

    private String value;

    private Integer sort;

    private String remark;

    private String createBy;

    private Timestamp createTime;
}
