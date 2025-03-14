package com.rune.admin.api;


import com.rune.admin.domain.entity.Organization;
import com.rune.admin.service.OrganizationService;
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
 * @author 大方的脑壳
 * @date 2023/4/19 17:11
 * @description /
 */
@Tag(name = "组织架构 Api")
@RestController
@RequestMapping("admin/organization")
@RequiredArgsConstructor
public class OrganizationApi {

    private final OrganizationService organizationService;

    @Operation(summary = "查询")
    @PreAuthorize("hasPermission('organization','list')")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll() {
        return ApiResponse.ok(organizationService.queryAll());
    }

    @Operation(summary = "新增")
    @CreateLog(msg = "新增组织架构")
    @PreAuthorize("hasPermission('organization','add')")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody Organization resources) {
        organizationService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @UpdateLog(msg = "修改组织架构")
    @PreAuthorize("hasPermission('organization','update')")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody Organization resources) {
        organizationService.update(resources);
        return ApiResponse.ok();
    }

    @Operation(summary = "删除")
    @DeleteLog(msg = "删除组织架构")
    @PreAuthorize("hasPermission('organization','delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiOk> delete(@PathVariable("id") Long id) {
        organizationService.delete(id);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }
}
