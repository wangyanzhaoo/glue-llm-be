package com.rune.admin.api;

import com.rune.admin.domain.dto.DictItemQuery;
import com.rune.admin.domain.dto.DictQuery;
import com.rune.admin.domain.entity.Dict;
import com.rune.admin.domain.entity.DictItem;
import com.rune.admin.service.DictItemService;
import com.rune.admin.service.DictService;
import com.rune.annotation.CreateLog;
import com.rune.annotation.DeleteLog;
import com.rune.annotation.UpdateLog;
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

import java.util.Set;

/**
 * @author avalon
 * @date 22/8/2 14:54
 * @description /
 */
@Tag(name = "字典管理 Api")
@RequiredArgsConstructor
@RequestMapping("/admin/dict")
@RestController
public class DictApi {

    private final DictService dictService;

    private final DictItemService dictItemService;

    @Operation(summary = "字典数据")
    @GetMapping("/data")
    public ResponseEntity<ApiOk> data() {
        return ApiResponse.ok(dictService.data());
    }

    @Operation(summary = "查询")
    @PreAuthorize("hasPermission('dict','list')")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(DictQuery query) {
        return ApiResponse.page(dictService.queryAll(query));
    }

    @Operation(summary = "新增")
    @CreateLog(msg = "新增字典")
    @PreAuthorize("hasPermission('dict','add')")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody Dict resources) {
        dictService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @UpdateLog(msg = "修改字典")
    @PreAuthorize("hasPermission('dict','update')")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody Dict resources) {
        dictService.update(resources);
        return ApiResponse.ok();
    }

    @Operation(summary = "删除")
    @DeleteLog(msg = "删除字典")
    @PreAuthorize("hasPermission('dict','delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiOk> delete(@PathVariable("id") Long id) {
        dictService.delete(id);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "item查询")
    @PreAuthorize("hasPermission('dict','list')")
    @GetMapping("/item")
    public ResponseEntity<ApiOk> itemQueryAll(DictItemQuery query) {
        return ApiResponse.page(dictItemService.queryAll(query));
    }

    @Operation(summary = "item新增")
    @CreateLog(msg = "新增字典项")
    @PreAuthorize("hasPermission('dict','add')")
    @PostMapping("/item")
    public ResponseEntity<ApiOk> itemCreate(@Validated @RequestBody DictItem resources) {
        dictItemService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "item修改")
    @UpdateLog(msg = "修改字典项")
    @PreAuthorize("hasPermission('dict','update')")
    @PutMapping("/item")
    public ResponseEntity<ApiOk> itemUpdate(@Validated @RequestBody DictItem resources) {
        dictItemService.update(resources);
        return ApiResponse.ok();
    }

    @Operation(summary = "item删除")
    @DeleteLog(msg = "删除字典项")
    @PreAuthorize("hasPermission('dict','delete')")
    @DeleteMapping("/item/{ids}")
    public ResponseEntity<ApiOk> itemDelete(@PathVariable("ids") Set<Long> ids) {
        dictItemService.delete(ids);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }
}
