package com.rune.llm.api;

import com.rune.domain.ApiOk;
import com.rune.llm.domain.dto.ChannelQuery;
import com.rune.llm.domain.entity.Channel;
import com.rune.llm.service.ChannelService;
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
 * @description 渠道表 Api
 */
@Tag(name = "渠道表 Api")
@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelApi {

    private final ChannelService channelService;

    @Operation(summary = "查询")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(ChannelQuery query) {
        return ApiResponse.page(channelService.queryAll(query));
    }

    @Operation(summary = "新增")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody Channel resources) {
        channelService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody Channel resources) {
        channelService.update(resources);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{ids}")
    public ResponseEntity<ApiOk> delete(@PathVariable("ids") Set<Long> ids) {
        channelService.delete(ids);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }
}
