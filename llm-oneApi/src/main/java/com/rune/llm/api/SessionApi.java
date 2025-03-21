package com.rune.llm.api;

import com.rune.domain.ApiOk;
import com.rune.llm.domain.dto.SessionQuery;
import com.rune.llm.domain.entity.Session;
import com.rune.llm.service.SessionService;
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
 * @date 2025-03-21
 * @description 会话表 Api
 */
@Tag(name = "会话表 Api")
@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionApi {

    private final SessionService sessionService;

    @Operation(summary = "查询")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(SessionQuery query) {
        return ApiResponse.page(sessionService.queryAll(query));
    }

    @Operation(summary = "新增")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody Session resources) {
        sessionService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody Session resources) {
        sessionService.update(resources);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "修改：对话名称")
    @PutMapping("updateTitle")
    public ResponseEntity<ApiOk> updateTitle(Long session, String title) {
        sessionService.updateTitle(session, title);
        return ApiResponse.ok();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{ids}")
    public ResponseEntity<ApiOk> delete(@PathVariable("ids") Set<Long> ids) {
        sessionService.delete(ids);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }
}
