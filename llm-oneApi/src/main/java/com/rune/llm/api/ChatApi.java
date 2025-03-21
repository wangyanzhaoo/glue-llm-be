package com.rune.llm.api;

import com.rune.domain.ApiOk;
import com.rune.llm.domain.dto.ChatQuery;
import com.rune.llm.domain.entity.Chat;
import com.rune.llm.service.ChatService;
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
 * @description 对话记录表 Api
 */
@Tag(name = "对话记录表 Api")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatApi {

    private final ChatService chatService;

    @Operation(summary = "查询")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(ChatQuery query) {
        return ApiResponse.page(chatService.queryAll(query));
    }

    @Operation(summary = "新增")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody Chat resources) {
        chatService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody Chat resources) {
        chatService.update(resources);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{ids}")
    public ResponseEntity<ApiOk> delete(@PathVariable("ids") Set<Long> ids) {
        chatService.delete(ids);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }
}
