package com.rune.file.domain.dto;

import com.rune.domain.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 大方的脑壳
 * @date 2022/8/24 14:28
 * @description /
 */
@Schema(title = "文件管理表格 查询DTO")
@Getter
@Setter
public class FileQuery extends BaseQuery {

    private String name;

    private String path;

    private String type;

    private Boolean status = false;

}
