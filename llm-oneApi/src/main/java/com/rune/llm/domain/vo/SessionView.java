package com.rune.llm.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author yangkf
 * @date 2025-03-21
 * @description 会话表 view
 */
@Getter
@Setter
public class SessionView {

    private Long id;
    private Long userId;
    private String title;
    private Integer count;
    private String createBy;
    private Timestamp createTime;

}
