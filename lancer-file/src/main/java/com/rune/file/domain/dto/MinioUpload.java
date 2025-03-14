package com.rune.file.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(title = "获取上传预签名")
@Setter
@Getter
public class MinioUpload extends MinioSmallDto{

    // 文件唯一值
    private String md5;

    // 文件大小
    private Integer size;
}
