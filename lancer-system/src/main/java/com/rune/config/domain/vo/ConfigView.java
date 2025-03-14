package com.rune.config.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author wangyz
 * @date 2023/11/30 09:20
 * @description /
 */
@Schema(title = "配置项 页面VO")
@Setter
@Getter
public class ConfigView {

    private String id;

    private Boolean isDelete;

    private String name;

    private String code;

    private String value;

    private String createBy;

    private Timestamp createTime;
}
