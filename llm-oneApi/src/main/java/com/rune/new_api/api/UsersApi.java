package com.rune.new_api.api;

import com.rune.domain.ApiOk;
import com.rune.new_api.domain.dto.UsersQuery;
import com.rune.new_api.domain.entity.Users;
import com.rune.new_api.service.UsersService;
import com.rune.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description new-api 用户表 Api
 */
@Tag(name = "new-api 用户表 Api")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersApi {

    private final UsersService usersService;

    @Operation(summary = "查询")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(UsersQuery query) {
        return ApiResponse.page(usersService.queryAll(query));
    }

    @Operation(summary = "新增")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody Users resources) {
        usersService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody Users resources) {
        usersService.update(resources);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{ids}")
    public ResponseEntity<ApiOk> delete(@PathVariable("ids") Set<Long> ids) {
        usersService.delete(ids);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }
}
