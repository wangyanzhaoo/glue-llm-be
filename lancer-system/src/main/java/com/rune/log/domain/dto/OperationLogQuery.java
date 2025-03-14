package com.rune.log.domain.dto;

import com.rune.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author avalon
 * @date 22/8/4 17:04
 * @description /
 */
@Schema(title = "操作日志表格搜索 搜索DTO")
@Getter
@Setter
public class OperationLogQuery extends BaseQuery {

    private String msg;

    private Integer type;

    private String username;

    private String ip;

    private String method;
}
