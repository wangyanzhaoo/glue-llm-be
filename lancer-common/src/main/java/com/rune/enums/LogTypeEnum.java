package com.rune.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author avalon
 * @date 22/8/5 14:12
 * @description /
 */
@Schema(title = "操作日志类型枚举")
@Getter
@RequiredArgsConstructor
public enum LogTypeEnum {

    // 查看操作
    QUERY(0),

    // 新建操作
    CREATE(1),

    // 修改操作
    UPDATE(2),

    // 删除操作
    DELETE(3),

    // 导入操作
    IMPORT(4),

    // 导出操作
    EXPORT(5),

    // 其他操作
    OTHER(6);

    private final Integer value;

}
