package com.rune.config.api;

import com.rune.annotation.CreateLog;
import com.rune.annotation.DeleteLog;
import com.rune.annotation.UpdateLog;
import com.rune.config.domain.dto.ConfigQuery;
import com.rune.config.domain.entity.Config;
import com.rune.config.service.ConfigService;
import com.rune.domain.ApiOk;
import com.rune.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author wyz
 * @date 2023/12/25 20:46
 * @description 配置项管理APi
 */
@Tag(name = "配置项管理 Api")
@RestController
@RequestMapping("/admin/config")
@RequiredArgsConstructor
public class ConfigApi {

    private final ConfigService configService;

    @Operation(summary = "查询")
    @GetMapping
    @PreAuthorize("hasPermission('config','list')")
    public ResponseEntity<ApiOk> queryAll(ConfigQuery query) {
        return ApiResponse.page(configService.queryAll(query));
    }

    @Operation(summary = "新增")
    @CreateLog(msg = "新增配置项")
    @PreAuthorize("hasPermission('config','create')")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody Config resources) {
        configService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @UpdateLog(msg = "修改配置")
    @PreAuthorize("hasPermission('config','update')")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody Config resources) {
        configService.update(resources);
        return ApiResponse.ok();
    }

    @Operation(summary = "删除")
    @DeleteLog(msg = "删除配置")
    @PreAuthorize("hasPermission('config','delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiOk> delete(@PathVariable("id") Long id) {
        configService.delete(id);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }
}
