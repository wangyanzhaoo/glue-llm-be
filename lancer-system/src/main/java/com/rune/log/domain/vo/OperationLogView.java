package com.rune.log.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.rune.utils.easyExcel.ConverterDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author avalon
 * @date 22/8/4 17:04
 * @description /
 */
@Schema(title = "操作日志 页面VO")
@Getter
@Setter
public class OperationLogView {

    @ExcelProperty(value = "id", index = 0)
    private Long id;

    @ExcelProperty(value = "操作信息", index = 1)
    private String msg;

    @ExcelProperty(value = "操作类型", index = 2)
    private Integer type;

    @ExcelProperty(value = "操作人", index = 3)
    private String username;

    @ExcelProperty(value = "请求IP", index = 4)
    private String ip;

    @ExcelProperty(value = "请求类型", index = 5)
    private String method;

    @ExcelProperty(value = "请求URL", index = 6)
    private String url;

    @ExcelProperty(value = "状态", index = 7)
    private Boolean status;

    @ExcelProperty(value = "耗时", index = 8)
    private Long time;

    @ExcelProperty(value = "用户代理", index = 9)
    private String userAgent;

    @ExcelProperty(value = "请求参数", index = 10)
    private String params;

    @ExcelProperty(value = "返回参数", index = 11)
    private String result;

    @ExcelProperty(value = "错误信息", index = 12)
    private String errorMsg;

    @ExcelProperty(value = "创建时间", index = 13, converter = ConverterDate.class)
    private Timestamp createTime;
}
