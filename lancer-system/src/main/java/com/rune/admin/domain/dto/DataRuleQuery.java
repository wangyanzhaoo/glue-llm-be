package com.rune.admin.domain.dto;

import com.rune.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 大方的脑壳
 * @date 2023/4/20 16:19
 * @description /
 */
@Schema(title = "数据规则表格 搜索DTO")
@Getter
@Setter
public class DataRuleQuery extends BaseQuery {

    private String name;

}
