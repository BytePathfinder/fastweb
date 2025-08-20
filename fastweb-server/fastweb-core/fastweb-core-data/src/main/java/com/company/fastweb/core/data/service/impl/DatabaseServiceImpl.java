package com.company.fastweb.core.data.service.impl;

import com.company.fastweb.core.data.model.vo.DatabaseInfoVO;
import com.company.fastweb.core.data.model.vo.SqlExecuteResultVO;
import com.company.fastweb.core.data.model.vo.TableInfoVO;
import com.company.fastweb.core.data.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据库服务实现类
 *
 * @author FastWeb
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseServiceImpl implements DatabaseService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public DatabaseInfoVO getDatabaseInfo() {
        DatabaseInfoVO info = new DatabaseInfoVO();
        
        try {
            jdbcTemplate.execute(connection -> {
                DatabaseMetaData metaData = connection.getMetaData();
                info.setDatabaseProductName(metaData.getDatabaseProductName());
                info.setDatabaseProductVersion(metaData.getDatabaseProductVersion());
                info.setDriverName(metaData.getDriverName());
                info.setDriverVersion(metaData.getDriverVersion());
                info.setUrl(metaData.getURL());
                info.setUserName(metaData.getUserName());
                info.setCatalogName(connection.getCatalog());
                info.setSchemaName(connection.getSchema());
                return null;
            });
            
            // 获取数据库大小等统计信息
            Map<String, Object> stats = getPerformanceStats();
            info.setDatabaseSize((String) stats.get("databaseSize"));
            info.setTableCount((Integer) stats.get("tableCount"));
            
        } catch (Exception e) {
            log.error("获取数据库信息失败", e);
        }
        
        return info;
    }

    @Override
    public List<TableInfoVO> getAllTables() {
        List<TableInfoVO> tables = new ArrayList<>();
        
        try {
            String sql = "SELECT TABLE_NAME, TABLE_COMMENT, TABLE_ROWS, DATA_LENGTH, INDEX_LENGTH, " +
                        "CREATE_TIME, UPDATE_TIME FROM information_schema.TABLES " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_TYPE = 'BASE TABLE'";
            
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
            
            for (Map<String, Object> row : rows) {
                TableInfoVO table = new TableInfoVO();
                table.setTableName((String) row.get("TABLE_NAME"));
                table.setTableComment((String) row.get("TABLE_COMMENT"));
                table.setTableRows(getLongValue(row.get("TABLE_ROWS")));
                table.setDataLength(getLongValue(row.get("DATA_LENGTH")));
                table.setIndexLength(getLongValue(row.get("INDEX_LENGTH")));
                table.setCreateTime((Date) row.get("CREATE_TIME"));
                table.setUpdateTime((Date) row.get("UPDATE_TIME"));
                
                // 计算表大小（数据 + 索引）
                long totalSize = table.getDataLength() + table.getIndexLength();
                table.setTableSize(formatBytes(totalSize));
                
                tables.add(table);
            }
            
        } catch (Exception e) {
            log.error("获取表信息失败", e);
        }
        
        return tables;
    }

    @Override
    public TableInfoVO getTableInfo(String tableName) {
        try {
            String sql = "SELECT TABLE_NAME, TABLE_COMMENT, TABLE_ROWS, DATA_LENGTH, INDEX_LENGTH, " +
                        "CREATE_TIME, UPDATE_TIME FROM information_schema.TABLES " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?";
            
            Map<String, Object> row = jdbcTemplate.queryForMap(sql, tableName);
            
            TableInfoVO table = new TableInfoVO();
            table.setTableName((String) row.get("TABLE_NAME"));
            table.setTableComment((String) row.get("TABLE_COMMENT"));
            table.setTableRows(getLongValue(row.get("TABLE_ROWS")));
            table.setDataLength(getLongValue(row.get("DATA_LENGTH")));
            table.setIndexLength(getLongValue(row.get("INDEX_LENGTH")));
            table.setCreateTime((Date) row.get("CREATE_TIME"));
            table.setUpdateTime((Date) row.get("UPDATE_TIME"));
            
            // 计算表大小
            long totalSize = table.getDataLength() + table.getIndexLength();
            table.setTableSize(formatBytes(totalSize));
            
            // 获取字段信息
            table.setColumns(getTableColumns(tableName));
            
            // 获取索引信息
            table.setIndexes(getTableIndexes(tableName));
            
            return table;
            
        } catch (Exception e) {
            log.error("获取表信息失败: {}", tableName, e);
            return null;
        }
    }

    @Override
    public SqlExecuteResultVO executeSql(String sql, Map<String, Object> parameters) {
        SqlExecuteResultVO result = new SqlExecuteResultVO();
        long startTime = System.currentTimeMillis();
        
        try {
            sql = sql.trim();
            String sqlType = sql.split("\\s+")[0].toUpperCase();
            
            switch (sqlType) {
                case "SELECT":
                    List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
                    result.setData(rows);
                    result.setRowCount(rows.size());
                    result.setSuccess(true);
                    break;
                    
                case "INSERT":
                case "UPDATE":
                case "DELETE":
                    int affectedRows = jdbcTemplate.update(sql);
                    result.setRowCount(affectedRows);
                    result.setSuccess(true);
                    result.setMessage(String.format("影响行数: %d", affectedRows));
                    break;
                    
                default:
                    jdbcTemplate.execute(sql);
                    result.setSuccess(true);
                    result.setMessage("SQL执行成功");
            }
            
        } catch (DataAccessException e) {
            result.setSuccess(false);
            result.setMessage("SQL执行失败: " + e.getMessage());
            log.error("SQL执行失败: {}", sql, e);
        }
        
        long endTime = System.currentTimeMillis();
        result.setExecutionTime(endTime - startTime);
        
        return result;
    }

    @Override
    public Map<String, Object> getPerformanceStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // 数据库大小
            String sizeQuery = "SELECT ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS db_size_mb " +
                              "FROM information_schema.tables WHERE table_schema = DATABASE()";
            Double dbSize = jdbcTemplate.queryForObject(sizeQuery, Double.class);
            stats.put("databaseSize", dbSize + " MB");
            
            // 表数量
            String tableCountQuery = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE()";
            Integer tableCount = jdbcTemplate.queryForObject(tableCountQuery, Integer.class);
            stats.put("tableCount", tableCount);
            
            // 连接数
            String connectionQuery = "SHOW STATUS LIKE 'Threads_connected'";
            Map<String, Object> connectionResult = jdbcTemplate.queryForMap(connectionQuery);
            stats.put("activeConnections", connectionResult.get("Value"));
            
            // 查询缓存命中率
            String cacheHitQuery = "SHOW STATUS LIKE 'Qcache_hits'";
            Map<String, Object> cacheHitResult = jdbcTemplate.queryForMap(cacheHitQuery);
            stats.put("queryCacheHits", cacheHitResult.get("Value"));
            
        } catch (Exception e) {
            log.error("获取性能统计失败", e);
        }
        
        return stats;
    }

    @Override
    public List<Map<String, Object>> getSlowQueries(int limit) {
        try {
            String sql = "SELECT sql_text, exec_count, avg_timer_wait/1000000000 as avg_time_sec, " +
                        "sum_timer_wait/1000000000 as total_time_sec " +
                        "FROM performance_schema.events_statements_summary_by_digest " +
                        "WHERE schema_name = DATABASE() " +
                        "ORDER BY avg_timer_wait DESC LIMIT ?";
            
            return jdbcTemplate.queryForList(sql, limit);
            
        } catch (Exception e) {
            log.error("获取慢查询失败", e);
            return Collections.emptyList();
        }
    }

    @Override
    public void optimizeTable(String tableName) {
        try {
            String sql = "OPTIMIZE TABLE " + tableName;
            jdbcTemplate.execute(sql);
            log.info("表优化完成: {}", tableName);
        } catch (Exception e) {
            log.error("表优化失败: {}", tableName, e);
            throw new RuntimeException("表优化失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> analyzeTable(String tableName) {
        try {
            String sql = "ANALYZE TABLE " + tableName;
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
            log.info("表分析完成: {}", tableName);
            return result.isEmpty() ? Collections.emptyMap() : result.get(0);
        } catch (Exception e) {
            log.error("表分析失败: {}", tableName, e);
            throw new RuntimeException("表分析失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> checkTable(String tableName) {
        try {
            String sql = "CHECK TABLE " + tableName;
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
            log.info("表检查完成: {}", tableName);
            return result.isEmpty() ? Collections.emptyMap() : result.get(0);
        } catch (Exception e) {
            log.error("表检查失败: {}", tableName, e);
            throw new RuntimeException("表检查失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getTableSize(String tableName) {
        try {
            String sql = "SELECT table_name, table_rows, data_length, index_length, " +
                        "(data_length + index_length) as total_length " +
                        "FROM information_schema.tables " +
                        "WHERE table_schema = DATABASE() AND table_name = ?";
            
            return jdbcTemplate.queryForMap(sql, tableName);
            
        } catch (Exception e) {
            log.error("获取表大小失败: {}", tableName, e);
            return Collections.emptyMap();
        }
    }

    @Override
    public List<Map<String, Object>> getTableIndexes(String tableName) {
        try {
            String sql = "SHOW INDEX FROM " + tableName;
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            log.error("获取表索引失败: {}", tableName, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Map<String, Object>> getTableColumns(String tableName) {
        try {
            String sql = "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT, " +
                        "COLUMN_COMMENT, COLUMN_KEY, EXTRA " +
                        "FROM information_schema.COLUMNS " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? " +
                        "ORDER BY ORDINAL_POSITION";
            
            return jdbcTemplate.queryForList(sql, tableName);
        } catch (Exception e) {
            log.error("获取表字段失败: {}", tableName, e);
            return Collections.emptyList();
        }
    }

    @Override
    public void backupTable(String tableName, String backupName) {
        try {
            String sql = String.format("CREATE TABLE %s AS SELECT * FROM %s", backupName, tableName);
            jdbcTemplate.execute(sql);
            log.info("表备份完成: {} -> {}", tableName, backupName);
        } catch (Exception e) {
            log.error("表备份失败: {} -> {}", tableName, backupName, e);
            throw new RuntimeException("表备份失败: " + e.getMessage());
        }
    }

    @Override
    public void restoreTable(String tableName, String backupName) {
        try {
            // 先删除原表
            jdbcTemplate.execute("DROP TABLE IF EXISTS " + tableName);
            // 从备份表恢复
            String sql = String.format("CREATE TABLE %s AS SELECT * FROM %s", tableName, backupName);
            jdbcTemplate.execute(sql);
            log.info("表恢复完成: {} <- {}", tableName, backupName);
        } catch (Exception e) {
            log.error("表恢复失败: {} <- {}", tableName, backupName, e);
            throw new RuntimeException("表恢复失败: " + e.getMessage());
        }
    }

    /**
     * 获取Long值
     */
    private Long getLongValue(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return 0L;
    }

    /**
     * 格式化字节大小
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        }
        if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        }
        return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
}