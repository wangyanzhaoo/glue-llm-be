package com.rune.new_api.domain.entity;

import com.rune.annotation.SecondEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description new-api Api令牌表实体
 */
@Schema(title = "new-api Api令牌表实体")
@Getter
@Setter
@Entity
@SecondEntity
@Table(name = "tokens")
//@Where(clause = "is_delete = false")
public class Tokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "")
    private Long userId;

    @Schema(description = "")
    @Column(name = "`key`")
    private String key;

    @Schema(description = "")
    private Long status;

    @Schema(description = "")
    private String name;

    @Schema(description = "")
    private Long createdTime;

    @Schema(description = "")
    private Long accessedTime;

    @Schema(description = "")
    private Long expiredTime;

    @Schema(description = "")
    private Long remainQuota;

    @Schema(description = "")
    private Integer unlimitedQuota;

    @Schema(description = "")
    private Integer modelLimitsEnabled;

    @Schema(description = "")
    private String modelLimits;

    @Schema(description = "")
    private String allowIps;

    @Schema(description = "")
    private Long usedQuota;

    @Schema(description = "")
    @Column(name = "`group`")
    private String group;

    @Schema(description = "")
    private LocalDateTime deletedAt;


}
