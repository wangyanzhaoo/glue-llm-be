package com.rune.llm.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description 渠道表 view
 */
@Getter
@Setter
public class ChannelView {

    private Long id;

    private String key;

    private String name;

    private String group;

    private Long type;

    private Integer status;

    private Long responseTime;

    private String usedLimit;

    private String remainLimit;

    private Long remainLimitUpdateTime;

    private String models;

    private Integer priority;

    private Integer weight;

    private Integer autoBan;

    private String createBy;

    private Timestamp createTime;

}
