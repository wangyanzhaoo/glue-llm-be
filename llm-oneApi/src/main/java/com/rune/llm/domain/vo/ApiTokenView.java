package com.rune.llm.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description api令牌表 view
 */
@Getter
@Setter
public class ApiTokenView {

    private Long id;

    private Long userId;

    private String key;

    private String name;

    private Long status;

    private String usedLimit;

    private String remainLimit;

    private Long expireDate;

    private Boolean unlimit;

    private Boolean modelLimit;

    private String modelLimitValue;

    private String allowIps;

    private String group;

    private String createBy;

    private Timestamp createTime;

}
