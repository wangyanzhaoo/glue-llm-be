package com.rune.log.api;

import com.rune.annotation.DeleteLog;
import com.rune.annotation.OperationLogging;
import com.rune.domain.ApiOk;
import com.rune.enums.LogTypeEnum;
import com.rune.log.domain.dto.LoginLogQuery;
import com.rune.log.service.LoginLogService;
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
 * @date 22/8/5 17:04
 * @description /
 */
@Tag(name = "登录日志 Api")
@RequiredArgsConstructor
@RequestMapping("/log/login")
@RestController
public class LoginLogApi {

    private final LoginLogService logService;

    @Operation(summary = "查询")
    @PreAuthorize("hasPermission('logl','list')")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(LoginLogQuery query) {
        return ApiResponse.page(logService.queryAll(query));
    }

    @Operation(summary = "删除")
    @DeleteLog(msg = "删除登录日志")
    @PreAuthorize("hasPermission('logl','delete')")
    @DeleteMapping("/{ids}")
    public ResponseEntity<ApiOk> delete(@PathVariable("ids") Set<Long> ids) {
        logService.delete(ids);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "清除登录日志")
    @DeleteLog(msg = "清除登录日志")
    @PreAuthorize("hasPermission('logl','remove')")
    @DeleteMapping("/clear/{type}")
    public ResponseEntity<ApiOk> clear(@PathVariable("type") Integer type) {
        logService.deleteByCreateTimeRange(type);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "导出登录日志")
    @OperationLogging(msg = "导出登录日志", type = LogTypeEnum.EXPORT)
    @PreAuthorize("hasPermission('logl','export')")
    @GetMapping("/export")
    public ResponseEntity<ApiOk> export(HttpServletResponse response, @RequestParam("beginTime") LocalDate beginTime, @RequestParam("endTime") LocalDate endTime) throws Exception {
        logService.exportExcel(response, beginTime, endTime);
        return ApiResponse.ok();
    }
}
