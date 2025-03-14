package com.rune.llm.domain.entity;

import com.rune.annotation.PrimaryEntity;
import com.rune.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description api令牌表实体
 */
@Schema(title = "api令牌表实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "llm_api_token")
//@Where(clause = "is_delete = false")
public class ApiToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "是否被删除")
    private Boolean isDelete = false;

    @Schema(description = "用户")
    private Long userId;

    @Schema(description = "key")
    @Column(name = "`key`")
    private String key;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "状态")
    private Long status;

    @Schema(description = "已用额度")
    private String usedLimit;

    @Schema(description = "剩余额度")
    private String remainLimit;

    @Schema(description = "过期时间")
    private Long expireDate;

    @Schema(description = "是否不限制额度")
    private Boolean unlimit;

    @Schema(description = "是否限制模型选择")
    private Boolean modelLimit;

    @Schema(description = "限制模型内容")
    private String modelLimitValue;

    @Schema(description = "白名单")
    private String allowIps;

    @Schema(description = "分组")
    @Column(name = "`group`")
    private String group;

}
