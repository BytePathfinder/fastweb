package com.company.fastweb.core.data.controller;

import com.company.fastweb.core.common.model.ApiResult;
import com.company.fastweb.core.data.model.vo.DataSourceInfoVO;
import com.company.fastweb.core.data.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据源管理控制器
 *
 * @author FastWeb
 */
@RestController
@RequestMapping("/api/admin/datasource")
@RequiredArgsConstructor
public class DataSourceController {

    private final DataSourceService dataSourceService;

    /**
     * 获取所有数据源信息
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<DataSourceInfoVO>>> getDataSources() {
        List<DataSourceInfoVO> dataSources = dataSourceService.getAllDataSources();
        return ResponseEntity.ok(ApiResult.success(dataSources));
    }

    /**
     * 获取当前数据源信息
     */
    @GetMapping("/current")
    public ResponseEntity<ApiResult<DataSourceInfoVO>> getCurrentDataSource() {
        DataSourceInfoVO currentDataSource = dataSourceService.getCurrentDataSource();
        return ResponseEntity.ok(ApiResult.success(currentDataSource));
    }

    /**
     * 切换数据源
     */
    @PostMapping("/switch/{dataSourceKey}")
    public ResponseEntity<ApiResult<Void>> switchDataSource(@PathVariable String dataSourceKey) {
        dataSourceService.switchDataSource(dataSourceKey);
        return ResponseEntity.ok(ApiResult.success(null));
    }

    /**
     * 测试数据源连接
     */
    @PostMapping("/test/{dataSourceKey}")
    public ResponseEntity<ApiResult<Boolean>> testDataSourceConnection(@PathVariable String dataSourceKey) {
        boolean isConnected = dataSourceService.testConnection(dataSourceKey);
        return ResponseEntity.ok(ApiResult.success(isConnected));
    }

    /**
     * 获取数据源健康状态
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResult<Map<String, Object>>> getDataSourceHealth() {
        Map<String, Object> healthInfo = dataSourceService.getHealthInfo();
        return ResponseEntity.ok(ApiResult.success(healthInfo));
    }

    /**
     * 获取数据源统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResult<Map<String, Object>>> getDataSourceStatistics() {
        Map<String, Object> statistics = dataSourceService.getStatistics();
        return ResponseEntity.ok(ApiResult.success(statistics));
    }

    /**
     * 清理数据源连接池
     */
    @PostMapping("/cleanup/{dataSourceKey}")
    public ResponseEntity<ApiResult<Void>> cleanupDataSource(@PathVariable String dataSourceKey) {
        dataSourceService.cleanupDataSource(dataSourceKey);
        return ResponseEntity.ok(ApiResult.success(null));
    }
}