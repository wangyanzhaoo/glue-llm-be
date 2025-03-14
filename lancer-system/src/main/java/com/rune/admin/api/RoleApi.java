package com.rune.admin.api;

import com.rune.admin.domain.dto.RoleQuery;
import com.rune.admin.domain.entity.Role;
import com.rune.admin.service.RoleService;
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
 * @date 22/3/31 10:39
 * @description /
 */
@Tag(name = "角色管理 Api")
@RequiredArgsConstructor
@RequestMapping("/admin/role")
@RestController
public class RoleApi {

    private final RoleService roleService;

    @Operation(summary = "查询")
    @PreAuthorize("hasPermission('role','list')")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(RoleQuery query) {
        return ApiResponse.page(roleService.queryAll(query));
    }

    @Operation(summary = "新增")
    @CreateLog(msg = "新增角色")
    @PreAuthorize("hasPermission('role','add')")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody Role resources) {
        roleService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @UpdateLog(msg = "修改角色")
    @PreAuthorize("hasPermission('role','update')")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody Role resources) {
        roleService.update(resources);
        return ApiResponse.ok();
    }

    @Operation(summary = "删除")
    @DeleteLog(msg = "删除角色")
    @PreAuthorize("hasPermission('role','delete')")
    @DeleteMapping("/{ids}")
    public ResponseEntity<ApiOk> delete(@PathVariable("ids") Set<Long> ids) {
        roleService.delete(ids);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "返回所有角色")
    @PreAuthorize("hasPermission('usre','list')")
    @GetMapping("/select")
    public ResponseEntity<ApiOk> select() {
        return ApiResponse.ok(roleService.select());
    }
}
