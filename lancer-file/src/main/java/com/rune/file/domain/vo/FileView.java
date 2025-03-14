package com.rune.file.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


/**
 * @author 大方的脑壳
 * @date 2022/8/22 17:26
 * @description /
 */
@Schema(title = "文件管理 页面VO")
@Getter
@Setter
public class FileView {

    private Long id;

    private String name;

    private String md5;

    private String path;

    private String bucket;

    private Long size;

    private String type;

    private Boolean status;

    private Timestamp createTime;

}
