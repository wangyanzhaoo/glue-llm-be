package com.rune.llm.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author yangkf
 * @date 2025-03-21
 * @description 对话记录表 view
 */
@Getter
@Setter
public class ChatView {

    private Long id;
    private Long userId;
    private Long sessionId;
    private String prompt;
    private String content;
    private String type;
    private String createBy;
    private Timestamp createTime;

}
