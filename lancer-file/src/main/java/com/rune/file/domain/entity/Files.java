package com.rune.file.domain.entity;


import com.rune.annotation.PrimaryEntity;
import com.rune.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 大方的脑壳
 * @date 2022/8/23 16:56
 * @description /
 */
@Schema(title = "文件实体")
@Getter
@Setter
@Entity
@PrimaryEntity
@Table(name = "lancer_files")
public class Files extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "md5密钥")
    private String md5;

    // 文件路径需要拼接bucket桶名
    @Schema(description = "文件path")
    private String path;

    @Schema(description = "所属文件夹")
    private String bucket;

    @Schema(description = "文件大小")
    private Long size;

    @Schema(description = "文件类型")
    private String type;

    @Schema(description = "是否为临时文件")
    private Boolean status = true;

    @Schema(description = "引用次数")
    private Integer count = 0;

    @Schema(description = "引用次数记录")
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<String> countRecord = new HashSet<>();

}