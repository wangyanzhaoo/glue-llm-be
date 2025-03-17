package com.rune.new_api.domain.entity;

import com.rune.annotation.SecondEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description new-api 渠道表实体
 */
@Schema(title = "new-api 渠道表实体")
@Getter
@Setter
@Entity
@SecondEntity
@Table(name = "channels")
//@Where(clause = "is_delete = false")
public class Channels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "")
    private Long type;

    @Schema(description = "")
    @Column(name = "`key`")
    private String key;

    @Schema(description = "")
    private String openAiOrganization;

    @Schema(description = "")
    private String testModel;

    @Schema(description = "")
    private Long status;

    @Schema(description = "")
    private String name;

    @Schema(description = "")
    private Long weight;

    @Schema(description = "")
    private Long createdTime;

    @Schema(description = "")
    private Long testTime;

    @Schema(description = "")
    private Long responseTime;

    @Schema(description = "")
    private String baseUrl;

    @Schema(description = "")
    private String other;

    @Schema(description = "")
    private Double balance;

    @Schema(description = "")
    private Long balanceUpdatedTime;

    @Schema(description = "")
    private String models;

    @Schema(description = "")
    @Column(name = "`group`")
    private String group;

    @Schema(description = "")
    private Long usedQuota;

    @Schema(description = "")
    private String modelMapping;

    @Schema(description = "")
    private String statusCodeMapping;

    @Schema(description = "")
    private Long priority;

    @Schema(description = "")
    private Long autoBan;

    @Schema(description = "")
    private String otherInfo;

    @Schema(description = "")
    private String tag;

    @Schema(description = "")
    private String setting;


}
