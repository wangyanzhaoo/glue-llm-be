package com.rune.llm.domain.dto;

import com.rune.domain.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yangkf
 * @date 2025-03-21
 * @description 会话表 dto
 */
@Getter
@Setter
public class SessionQuery extends BaseQuery {

    private String title;

    private String createBy;

}
