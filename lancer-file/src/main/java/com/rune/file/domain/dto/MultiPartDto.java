package com.rune.file.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(title = "分片DTO")
@Setter
@Getter
public class MultiPartDto extends MinioSmallDto{

    //分片数量
    private Integer part;

    //分片Id
    private String uploadId;

    //上传md5
    private String md5;
}
