package com.rune.file.api;

import com.rune.domain.ApiOk;
import com.rune.file.domain.dto.MinioSmallDto;
import com.rune.file.domain.dto.MinioUpload;
import com.rune.file.domain.dto.MultiPartDto;
import com.rune.file.service.MinioService;
import com.rune.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author yyk-sun
 * @date 2024/03/22 14:08
 * @description TODO
 **/
@Tag(name = "文件管理 Api")
@RestController
@RequestMapping("/minio")
@RequiredArgsConstructor
public class MinioApi {

    private final MinioService minioService;

    @Operation(summary = "上传获取预签名")
    @PreAuthorize("hasPermission('minio','upload')")
    @PostMapping
    public ResponseEntity<ApiOk> getUploadObjectUrl(@RequestBody MinioUpload resource) {
        return ApiResponse.ok(minioService.getUploadObjectUrl(resource));
    }

    @Operation(summary = "下载获取预签名")
    @PreAuthorize("hasPermission('minio','download')")
    @PostMapping("DownloadObjectUrl")
    public ResponseEntity<ApiOk> getDownloadObjectUrl(@RequestBody List<MinioSmallDto> resource) {
        List<String> objectUrl = minioService.getDownloadObjectUrl(resource);
        return ApiResponse.ok(objectUrl.size() == 1 ? objectUrl.get(0) : objectUrl);
    }

    @Operation(summary = "初始化分片")
    @PreAuthorize("hasPermission('minio','initpart')")
    @PostMapping("initMultiPart")
    public ResponseEntity<ApiOk> initMultiPartUpload(@RequestBody MultiPartDto resource)throws Exception {
        return ApiResponse.ok(minioService.initMultiPartUpload(resource));
    }

    @Operation(summary = "合并分片")
    @PreAuthorize("hasPermission('minio','mergerpart')")
    @PostMapping("mergeMultipart")
    public ResponseEntity<ApiOk> mergeMultipartUpload(@RequestBody MultiPartDto resource) {
        return ApiResponse.ok(minioService.mergeMultipartUpload(resource));
    }

}
