package com.rune.new_api.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description new-api 渠道表 view
 */
@Getter
@Setter
public class ChannelsView {

    private Long id;
    private Long type;
    private String key;
    private String openAiOrganization;
    private String testModel;
    private Long status;
    private String name;
    private Long weight;
    private Long createdTime;
    private Long testTime;
    private Long responseTime;
    private String baseUrl;
    private String other;
    private Double balance;
    private Long balanceUpdatedTime;
    private String models;
    private String group;
    private Long usedQuota;
    private String modelMapping;
    private String statusCodeMapping;
    private Long priority;
    private Long autoBan;
    private String otherInfo;
    private String tag;
    private String setting;
    private Timestamp createTime;

}
