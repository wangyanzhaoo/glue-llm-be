package com.rune.log.api;

import com.rune.annotation.DeleteLog;
import com.rune.annotation.OperationLogging;
import com.rune.domain.ApiOk;
import com.rune.enums.LogTypeEnum;
import com.rune.log.domain.dto.OperationLogQuery;
import com.rune.log.service.OperationLogService;
import com.rune.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author avalon
 * @date 22/8/4 17:04
 * @description /
 */
@Tag(name = "操作日志 Api")
@RequiredArgsConstructor
@RequestMapping("/log/operation")
@RestController
public class OperationLogApi {

    private final OperationLogService logService;

    @Operation(summary = "查询")
    @PreAuthorize("hasPermission('logo','list')")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(OperationLogQuery query) {
        return ApiResponse.page(logService.queryAll(query));
    }

    @Operation(summary = "删除")
    @DeleteLog(msg = "删除日志")
    @PreAuthorize("hasPermission('logo','delete')")
    @DeleteMapping("/{ids}")
    public ResponseEntity<ApiOk> delete(@PathVariable("ids") Set<Long> ids) {
        logService.delete(ids);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "清除操作日志")
    @DeleteLog(msg = "清除操作日志")
    @PreAuthorize("hasPermission('logo','remove')")
    @DeleteMapping("/clear/{type}")
    public ResponseEntity<ApiOk> clear(@PathVariable("type") Integer type) {
        logService.deleteByCreateTimeRange(type);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "导出操作日志")
    @OperationLogging(msg = "导出操作日志", type = LogTypeEnum.EXPORT)
    @PreAuthorize("hasPermission('logo','export')")
    @GetMapping("/export")
    public ResponseEntity<ApiOk> export(HttpServletResponse response, @RequestParam("beginTime") LocalDate beginTime, @RequestParam("endTime") LocalDate endTime) throws Exception {
        logService.exportExcel(response, beginTime, endTime);
        return ApiResponse.ok(HttpStatus.CREATED);
    }
}
