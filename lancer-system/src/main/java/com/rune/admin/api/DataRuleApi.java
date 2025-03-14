package com.rune.admin.api;


import com.rune.admin.domain.dto.DataRuleQuery;
import com.rune.admin.domain.entity.DataRule;
import com.rune.admin.service.DataRuleService;
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

/**
 * @author 大方的脑壳
 * @date 2023/4/20 16:19
 * @description /
 */
@Tag(name = "数据规则 Api")
@RestController
@RequestMapping("/admin/data/rule")
@RequiredArgsConstructor
public class DataRuleApi {

    private final DataRuleService dataRuleService;

    @Operation(summary = "查询")
    @PreAuthorize("hasPermission('dataRule','list')")
    @GetMapping
    public ResponseEntity<ApiOk> queryAll(DataRuleQuery query) {
        return ApiResponse.page(dataRuleService.queryAll(query));
    }

    @Operation(summary = "新增")
    @CreateLog(msg = "新增数据规则")
    @PreAuthorize("hasPermission('dataRule','add')")
    @PostMapping
    public ResponseEntity<ApiOk> create(@Validated @RequestBody DataRule resources) {
        dataRuleService.create(resources);
        return ApiResponse.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "修改")
    @UpdateLog(msg = "修改数据规则")
    @PreAuthorize("hasPermission('dataRule','update')")
    @PutMapping
    public ResponseEntity<ApiOk> update(@Validated @RequestBody DataRule resources) {
        dataRuleService.update(resources);
        return ApiResponse.ok();
    }

    @Operation(summary = "删除")
    @DeleteLog(msg = "删除数据规则")
    @PreAuthorize("hasPermission('dataRule','delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiOk> delete(@PathVariable("id") Long id) {
        dataRuleService.delete(id);
        return ApiResponse.ok(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "返回所有数据规则")
    @PreAuthorize("hasPermission('user','list')")
    @GetMapping("/select")
    public ResponseEntity<ApiOk> select() {
        return ApiResponse.ok(dataRuleService.select());
    }
}
