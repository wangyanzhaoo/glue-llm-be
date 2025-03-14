package com.rune.log.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.rune.utils.easyExcel.ConverterDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author avalon
 * @date 22/8/5 17:04
 * @description /
 */
@Schema(title = "登录日志 页面VO")
@Getter
@Setter
public class LoginLogView {

    @ExcelProperty(value = "id", index = 0)
    private Long id;

    @ExcelProperty(value = "操作信息", index = 1)
    private String msg;

    @ExcelProperty(value = "操作类型", index = 2)
    private Integer type;

    @ExcelProperty(value = "操作人", index = 3)
    private String username;

    @ExcelProperty(value = "IP", index = 4)
    private String ip;

    @ExcelProperty(value = "用户代理", index = 5)
    private String userAgent;

    @ExcelProperty(value = "创建时间", index = 6, converter = ConverterDate.class)
    private Timestamp createTime;
}
