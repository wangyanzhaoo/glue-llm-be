package com.rune.admin.api;

import com.rune.admin.domain.dto.PasswordRequest;
import com.rune.admin.domain.dto.UserQuery;
import com.rune.admin.domain.entity.User;
import com.rune.admin.service.UserService;
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
@Tag(name = "用户管理 Api")
@RequiredArgsConstructor
@RequestMapping("/admin/user")
@RestController
public class UserApi {

    private final UserService userService;

    @Operation(summary = "查询")
    @PreAuthorize("hasPermission('user','list')")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(UserQuery query) throws Exception {
        return ApiResponse.page(userService.queryAll(query));
    }

    @Operation(summary = "新增")
    @CreateLog(msg = "新增用户")
    @PreAuthorize("hasPermission('user','add')")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody User resources) {
        return ApiResponse.ok(HttpStatus.CREATED, userService.create(resources));
    }

    @Operation(summary = "修改")
    @UpdateLog(msg = "修改用户")
    @PreAuthorize("hasPermission('user','update')")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody User resources) {
        userService.update(resources);
        return ApiResponse.ok();
    }

    @Operation(summary = "删除")
    @DeleteLog(msg = "删除用户")
    @PreAuthorize("hasPermission('user','delete')")
    @DeleteMapping("/{ids}")
    public ResponseEntity<ApiOk> delete(@PathVariable("ids") Set<Long> ids) {
        userService.delete(ids);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "个人修改密码")
    @UpdateLog(msg = "更新密码")
    @PutMapping("/updatePassword")
    public ResponseEntity<ApiOk> updatePassword(@Validated @RequestBody PasswordRequest passwordRequest) {
        userService.updatePassword(passwordRequest);
        return ApiResponse.ok();
    }

    @Operation(summary = "管理员重置用户密码")
    @UpdateLog(msg = "重置密码")
    @PreAuthorize("hasPermission('user','reset')")
    @PutMapping("/resetPassword/{id}")
    public ResponseEntity<ApiOk> resetPassword(@PathVariable("id") Long id) {
        return ApiResponse.ok(HttpStatus.OK, userService.resetPassword(id));
    }

}
