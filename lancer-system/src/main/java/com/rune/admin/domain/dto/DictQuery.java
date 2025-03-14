package com.rune.admin.domain.dto;

import com.rune.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author avalon
 * @date 22/8/2 14:27
 * @description /
 */
@Schema(title = "字典表格 搜索DTO")
@Getter
@Setter
public class DictQuery extends BaseQuery {

    private String name;
}
