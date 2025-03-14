package com.rune.admin.domain.dto;

import com.rune.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

/**
 * @author avalon
 * @date 22/8/2 14:27
 * @description /
 */
@Schema(title = "字典项表格 搜索DTO")
@Getter
@Setter
public class DictItemQuery extends BaseQuery {

    @NotBlank
    private Long pid;

    private String text;
}
