package com.rune.new_api.api;

import com.rune.domain.ApiOk;
import com.rune.new_api.domain.dto.TokensQuery;
import com.rune.new_api.domain.entity.Tokens;
import com.rune.new_api.service.TokensService;
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
 * @description new-api Api令牌表 Api
 */
@Tag(name = "new-api Api令牌表 Api")
@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor
public class TokensApi {

    private final TokensService tokensService;

    @Operation(summary = "查询")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(TokensQuery query) {
        return ApiResponse.page(tokensService.queryAll(query));
    }

    @Operation(summary = "新增")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody Tokens resources) {
        tokensService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody Tokens resources) {
        tokensService.update(resources);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{ids}")
    public ResponseEntity<ApiOk> delete(@PathVariable("ids") Set<Long> ids) {
        tokensService.delete(ids);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }
}
