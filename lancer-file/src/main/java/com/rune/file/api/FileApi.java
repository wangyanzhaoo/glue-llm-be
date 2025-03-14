package com.rune.file.api;

import com.rune.annotation.CreateLog;
import com.rune.annotation.UpdateLog;
import com.rune.domain.ApiOk;
import com.rune.file.domain.dto.FileQuery;
import com.rune.file.domain.entity.Files;
import com.rune.file.service.FileService;
import com.rune.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author 大方的脑壳
 * @date 2022/8/24 14:28
 * @description 文件管理控制器
 */
@Tag(name = "文件管理 Api")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileApi {

    private final FileService fileService;

    @Operation(summary = "查询文件信息列表")
    @PreAuthorize("hasPermission('file','list')")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(FileQuery query) {
        return ApiResponse.page(fileService.queryAll(query));
    }

    @Operation(summary = "新增")
    @CreateLog(msg = "新增文件信息")
    @PreAuthorize("hasPermission('file','create')")
    @PostMapping
    public ResponseEntity<ApiOk> create(@RequestBody Files fileInfo) {
        return ApiResponse.ok(HttpStatus.CREATED, fileService.create(fileInfo));
    }

    @Operation(summary = "更新文件状态")
    @UpdateLog(msg = "更新文件状态")
    @PreAuthorize("hasPermission('file','update')")
    @PutMapping("/{ids}")
    public ResponseEntity<ApiOk> updateStatus(@PathVariable("ids") Set<Long> ids) {
        fileService.updateStatus(ids);
        return ApiResponse.ok();
    }

    @Operation(summary = "文件删除")
    @UpdateLog(msg = "文件删除")
    @PreAuthorize("hasPermission('file','delete')")
    @DeleteMapping("/{ids}")
    public ResponseEntity<ApiOk> delete(@PathVariable("ids") Set<Long> ids) {
        fileService.delete(ids);
        return ApiResponse.ok();
    }

}
