package com.rune.log.domain.entity;

import com.rune.annotation.PrimaryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * @author avalon
 * @date 22/8/5 18:05
 * @description /
 */
@Schema(title = "登录日志实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "log_login")
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "操作信息")
    private String msg;

    @Schema(description = "操作类型(字典 loglType")
    private Integer type;

    @Schema(description = "操作人")
    private String username;

    @Schema(description = "IP")
    private String ip;

    @Schema(description = "用户代理")
    private String userAgent;

    @Schema(description = "创建时间")
    @CreationTimestamp
    private Timestamp createTime;
}
