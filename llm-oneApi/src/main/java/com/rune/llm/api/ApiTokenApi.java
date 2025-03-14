package com.rune.llm.api;

import com.rune.domain.ApiOk;
import com.rune.llm.domain.dto.ApiTokenQuery;
import com.rune.llm.domain.entity.ApiToken;
import com.rune.llm.service.ApiTokenService;
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
 * @description api令牌表 Api
 */
@Tag(name = "api令牌表 Api")
@RestController
@RequestMapping("/apiToken")
@RequiredArgsConstructor
public class ApiTokenApi {

    private final ApiTokenService apiTokenService;

    @Operation(summary = "查询")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(ApiTokenQuery query) {
        return ApiResponse.page(apiTokenService.queryAll(query));
    }

    @Operation(summary = "新增")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody ApiToken resources) {
        apiTokenService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody ApiToken resources) {
        apiTokenService.update(resources);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{ids}")
    public ResponseEntity<ApiOk> delete(@PathVariable("ids") Set<Long> ids) {
        apiTokenService.delete(ids);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }
}
