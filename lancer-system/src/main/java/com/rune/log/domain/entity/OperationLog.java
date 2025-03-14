package com.rune.log.domain.entity;

import com.rune.annotation.PrimaryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 * @author avalon
 * @date 22/8/4 17:04
 * @description /
 */
@Schema(title = "操作日志实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "log_operation")
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "操作信息")
    private String msg;

    @Schema(description = "操作类型(字典 operationType")
    private Integer type;

    @Schema(description = "操作人")
    private String username;

    @Schema(description = "请求IP")
    private String ip;

    @Schema(description = "请求类型")
    private String method;

    @Schema(description = "请求URL")
    private String url;

    @Schema(description = "状态(0 失败，1 成功")
    private Boolean status;

    @Schema(description = "耗时")
    private Long time;

    @Schema(title = "用户代理")
    private String userAgent;

    @Schema(description = "请求参数")
    private String params;

    @Schema(description = "返回参数")
    private String result;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "创建时间")
    @CreationTimestamp
    private Timestamp createTime;
}
