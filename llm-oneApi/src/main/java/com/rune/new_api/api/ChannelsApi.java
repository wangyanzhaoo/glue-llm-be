package com.rune.new_api.api;

import com.rune.domain.ApiOk;
import com.rune.new_api.domain.dto.ChannelsQuery;
import com.rune.new_api.domain.entity.Channels;
import com.rune.new_api.service.ChannelsService;
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
 * @description new-api 渠道表 Api
 */
@Tag(name = "new-api 渠道表 Api")
@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelsApi {

    private final ChannelsService channelsService;

    @Operation(summary = "查询")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(ChannelsQuery query) {
        return ApiResponse.page(channelsService.queryAll(query));
    }

    @Operation(summary = "新增")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody Channels resources) {
        channelsService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody Channels resources) {
        channelsService.update(resources);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{ids}")
    public ResponseEntity<ApiOk> delete(@PathVariable("ids") Set<Long> ids) {
        channelsService.delete(ids);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }
}
