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
@Schema(title = "字典 页面VO")
@Getter
@Setter
public class DictView {

    private Long id;

    private Boolean isTree;

    private String name;

    private String code;

    private String remark;

    private String createBy;

    private Timestamp createTime;
}
