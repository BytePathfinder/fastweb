package com.company.fastweb.core.data.service;

import com.company.fastweb.core.data.model.vo.DatabaseInfoVO;
import com.company.fastweb.core.data.model.vo.SqlExecuteResultVO;
import com.company.fastweb.core.data.model.vo.TableInfoVO;

import java.util.List;
import java.util.Map;

/**
 * 数据库服务接口
 *
 * @author FastWeb
 */
public interface DatabaseService {

    /**
     * 获取数据库信息
     *
     * @return 数据库信息
     */
    DatabaseInfoVO getDatabaseInfo();

    /**
     * 获取所有表信息
     *
     * @return 表信息列表
     */
    List<TableInfoVO> getAllTables();

    /**
     * 获取指定表信息
     *
     * @param tableName 表名
     * @return 表信息
     */
    TableInfoVO getTableInfo(String tableName);

    /**
     * 执行SQL语句
     *
     * @param sql SQL语句
     * @param parameters 参数
     * @return 执行结果
     */
    SqlExecuteResultVO executeSql(String sql, Map<String, Object> parameters);

    /**
     * 获取数据库性能统计
     *
     * @return 性能统计信息
     */
    Map<String, Object> getPerformanceStats();

    /**
     * 获取慢查询日志
     *
     * @param limit 限制条数
     * @return 慢查询列表
     */
    List<Map<String, Object>> getSlowQueries(int limit);

    /**
     * 优化表
     *
     * @param tableName 表名
     */
    void optimizeTable(String tableName);

    /**
     * 分析表
     *
     * @param tableName 表名
     * @return 分析结果
     */
    Map<String, Object> analyzeTable(String tableName);

    /**
     * 检查表
     *
     * @param tableName 表名
     * @return 检查结果
     */
    Map<String, Object> checkTable(String tableName);

    /**
     * 获取表大小信息
     *
     * @param tableName 表名
     * @return 表大小信息
     */
    Map<String, Object> getTableSize(String tableName);

    /**
     * 获取表索引信息
     *
     * @param tableName 表名
     * @return 索引信息列表
     */
    List<Map<String, Object>> getTableIndexes(String tableName);

    /**
     * 获取表字段信息
     *
     * @param tableName 表名
     * @return 字段信息列表
     */
    List<Map<String, Object>> getTableColumns(String tableName);

    /**
     * 备份表
     *
     * @param tableName 表名
     * @param backupName 备份名称
     */
    void backupTable(String tableName, String backupName);

    /**
     * 恢复表
     *
     * @param tableName 表名
     * @param backupName 备份名称
     */
    void restoreTable(String tableName, String backupName);
}