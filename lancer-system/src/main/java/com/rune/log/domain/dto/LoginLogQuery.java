package com.rune.log.domain.dto;

import com.rune.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author avalon
 * @date 22/8/5 17:04
 * @description /
 */
@Schema(title = "登录日志表格 搜索DTO")
@Getter
@Setter
public class LoginLogQuery extends BaseQuery {

    private String msg;

    private Integer type;

    private String username;

    private String ip;
}
