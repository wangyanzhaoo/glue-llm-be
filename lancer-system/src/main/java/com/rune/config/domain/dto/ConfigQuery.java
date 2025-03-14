package com.rune.config.domain.dto;

import com.rune.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangyz
 * @date 2023/11/30 09:20
 * @description /
 */
@Schema(title = "配置项表格 搜索DTO")
@Setter
@Getter
public class ConfigQuery extends BaseQuery{

    private String name;

    private String code;

    private String value;

}
