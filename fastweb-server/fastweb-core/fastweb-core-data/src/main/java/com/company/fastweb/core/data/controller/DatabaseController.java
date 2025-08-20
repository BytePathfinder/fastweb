package com.company.fastweb.core.data.controller;

import com.company.fastweb.core.common.model.ApiResult;
import com.company.fastweb.core.data.model.form.SqlExecuteForm;
import com.company.fastweb.core.data.model.vo.DatabaseInfoVO;
import com.company.fastweb.core.data.model.vo.SqlExecuteResultVO;
import com.company.fastweb.core.data.model.vo.TableInfoVO;
import com.company.fastweb.core.data.service.DatabaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据库管理控制器
 *
 * @author FastWeb
 */
@RestController
@RequestMapping("/api/admin/database")
@RequiredArgsConstructor
public class DatabaseController {

    private final DatabaseService databaseService;

    /**
     * 获取数据库信息
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResult<DatabaseInfoVO>> getDatabaseInfo() {
        DatabaseInfoVO databaseInfo = databaseService.getDatabaseInfo();
        return ResponseEntity.ok(ApiResult.success(databaseInfo));
    }

    /**
     * 获取所有表信息
     */
    @GetMapping("/tables")
    public ResponseEntity<ApiResult<List<TableInfoVO>>> getTables() {
        List<TableInfoVO> tables = databaseService.getAllTables();
        return ResponseEntity.ok(ApiResult.success(tables));
    }

    /**
     * 获取指定表信息
     */
    @GetMapping("/tables/{tableName}")
    public ResponseEntity<ApiResult<TableInfoVO>> getTableInfo(@PathVariable String tableName) {
        TableInfoVO tableInfo = databaseService.getTableInfo(tableName);
        return ResponseEntity.ok(ApiResult.success(tableInfo));
    }

    /**
     * 执行SQL查询
     */
    @PostMapping("/execute")
    public ResponseEntity<ApiResult<SqlExecuteResultVO>> executeSql(@Valid @RequestBody SqlExecuteForm form) {
        SqlExecuteResultVO result = databaseService.executeSql(form.getSql(), form.getParameters());
        return ResponseEntity.ok(ApiResult.success(result));
    }

    /**
     * 获取数据库性能统计
     */
    @GetMapping("/performance")
    public ResponseEntity<ApiResult<Map<String, Object>>> getPerformanceStats() {
        Map<String, Object> stats = databaseService.getPerformanceStats();
        return ResponseEntity.ok(ApiResult.success(stats));
    }

    /**
     * 获取慢查询日志
     */
    @GetMapping("/slow-queries")
    public ResponseEntity<ApiResult<List<Map<String, Object>>>> getSlowQueries(
            @RequestParam(defaultValue = "100") int limit) {
        List<Map<String, Object>> slowQueries = databaseService.getSlowQueries(limit);
        return ResponseEntity.ok(ApiResult.success(slowQueries));
    }

    /**
     * 优化表
     */
    @PostMapping("/optimize/{tableName}")
    public ResponseEntity<ApiResult<Void>> optimizeTable(@PathVariable String tableName) {
        databaseService.optimizeTable(tableName);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 分析表
     */
    @PostMapping("/analyze/{tableName}")
    public ResponseEntity<ApiResult<Map<String, Object>>> analyzeTable(@PathVariable String tableName) {
        Map<String, Object> result = databaseService.analyzeTable(tableName);
        return ResponseEntity.ok(ApiResult.success(result));
    }

    /**
     * 检查表
     */
    @PostMapping("/check/{tableName}")
    public ResponseEntity<ApiResult<Map<String, Object>>> checkTable(@PathVariable String tableName) {
        Map<String, Object> result = databaseService.checkTable(tableName);
        return ResponseEntity.ok(ApiResult.success(result));
    }
}