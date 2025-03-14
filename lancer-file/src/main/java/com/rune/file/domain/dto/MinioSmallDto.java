package com.rune.file.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(title = "Minio基础DTO")
@Setter
@Getter
public class MinioSmallDto {

    //文件名
    private String fileName;

    //minio 桶内全路径
    private String fileFullName;

    // 桶名称
    private String bucketName;
}
