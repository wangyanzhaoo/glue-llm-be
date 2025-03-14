package com.rune.admin.api;

import com.rune.admin.domain.entity.Menu;
import com.rune.admin.service.MenuService;
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

/**
 * @author avalon
 * @date 22/3/31 10:39
 * @description /
 */
@Tag(name = "菜单权限管理 Api")
@RequiredArgsConstructor
@RequestMapping("/admin/menu")
@RestController
public class MenuApi {

    private final MenuService menuService;

    @Operation(summary = "查询")
    @PreAuthorize("hasPermission('menu','list')")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll() {
        return ApiResponse.ok(menuService.queryAll());
    }

    @Operation(summary = "新增")
    @CreateLog(msg = "新增菜单")
    @PreAuthorize("hasPermission('menu','add')")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody Menu resources) {
        menuService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @UpdateLog(msg = "修改菜单")
    @PreAuthorize("hasPermission('menu','update')")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody Menu resources) {
        menuService.update(resources);
        return ApiResponse.ok();
    }

    @Operation(summary = "删除")
    @DeleteLog(msg = "删除菜单")
    @PreAuthorize("hasPermission('menu','delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiOk> delete(@PathVariable("id") Long id) {
        menuService.delete(id);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "返回菜单树")
    @PreAuthorize("hasPermission('role','list')")
    @GetMapping("/tree")
    public ResponseEntity<ApiOk> tree() {
        return ApiResponse.ok(menuService.tree());
    }
}
