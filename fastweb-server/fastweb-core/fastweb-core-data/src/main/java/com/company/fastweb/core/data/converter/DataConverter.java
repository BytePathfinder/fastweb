package com.company.fastweb.core.data.converter;

import com.company.fastweb.core.data.model.form.DataSourceConfigForm;
import com.company.fastweb.core.data.model.form.DataSourceSwitchForm;
import com.company.fastweb.core.data.model.form.SqlExecuteForm;
import com.company.fastweb.core.data.model.vo.DataSourceInfoVO;
import com.company.fastweb.core.data.model.vo.DatabaseInfoVO;
import com.company.fastweb.core.data.model.vo.SqlExecuteResultVO;
import com.company.fastweb.core.data.model.vo.TableInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

/**
 * 数据访问模块转换器
 *
 * @author FastWeb
 */
@Mapper(componentModel = "spring")
public interface DataConverter {

    /**
     * 数据源配置表单转换为配置Map
     *
     * @param form 数据源配置表单
     * @return 配置Map
     */
    @Mapping(target = "url", source = "url")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "driverClassName", source = "driverClassName")
    @Mapping(target = "maximumPoolSize", source = "maximumPoolSize")
    @Mapping(target = "minimumIdle", source = "minimumIdle")
    @Mapping(target = "connectionTimeout", source = "connectionTimeout")
    @Mapping(target = "idleTimeout", source = "idleTimeout")
    @Mapping(target = "maxLifetime", source = "maxLifetime")
    @Mapping(target = "connectionTestQuery", source = "connectionTestQuery")
    @Mapping(target = "autoCommit", source = "autoCommit")
    @Mapping(target = "readOnly", source = "readOnly")
    Map<String, Object> formToConfigMap(DataSourceConfigForm form);

    /**
     * 格式化数据库大小
     *
     * @param bytes 字节数
     * @return 格式化后的大小字符串
     */
    default String formatBytes(Long bytes) {
        if (bytes == null || bytes == 0) {
            return "0 B";
        }
        
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

    /**
     * 格式化连接池状态
     *
     * @param activeConnections 活跃连接数
     * @param totalConnections 总连接数
     * @return 状态描述
     */
    default String formatConnectionStatus(Integer activeConnections, Integer totalConnections) {
        if (activeConnections == null || totalConnections == null) {
            return "未知";
        }
        
        if (totalConnections == 0) {
            return "无连接";
        }
        
        double utilization = (double) activeConnections / totalConnections;
        if (utilization < 0.5) {
            return "正常";
        } else if (utilization < 0.8) {
            return "繁忙";
        } else {
            return "高负载";
        }
    }

    /**
     * 格式化执行时间
     *
     * @param milliseconds 毫秒数
     * @return 格式化后的时间字符串
     */
    default String formatExecutionTime(Long milliseconds) {
        if (milliseconds == null) {
            return "未知";
        }
        
        if (milliseconds < 1000) {
            return milliseconds + " ms";
        } else if (milliseconds < 60000) {
            return String.format("%.2f s", milliseconds / 1000.0);
        } else {
            long minutes = milliseconds / 60000;
            long seconds = (milliseconds % 60000) / 1000;
            return String.format("%d min %d s", minutes, seconds);
        }
    }

    /**
     * 获取SQL类型描述
     *
     * @param sql SQL语句
     * @return SQL类型
     */
    default String getSqlType(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return "UNKNOWN";
        }
        
        String upperSql = sql.trim().toUpperCase();
        if (upperSql.startsWith("SELECT")) {
            return "SELECT";
        } else if (upperSql.startsWith("INSERT")) {
            return "INSERT";
        } else if (upperSql.startsWith("UPDATE")) {
            return "UPDATE";
        } else if (upperSql.startsWith("DELETE")) {
            return "DELETE";
        } else if (upperSql.startsWith("CREATE")) {
            return "CREATE";
        } else if (upperSql.startsWith("DROP")) {
            return "DROP";
        } else if (upperSql.startsWith("ALTER")) {
            return "ALTER";
        } else if (upperSql.startsWith("TRUNCATE")) {
            return "TRUNCATE";
        } else {
            return "OTHER";
        }
    }

    /**
     * 获取数据库类型
     *
     * @param url 数据库连接URL
     * @return 数据库类型
     */
    default String getDatabaseType(String url) {
        if (url == null) {
            return "未知";
        }
        
        String lowerUrl = url.toLowerCase();
        if (lowerUrl.contains("mysql")) {
            return "MySQL";
        } else if (lowerUrl.contains("postgresql")) {
            return "PostgreSQL";
        } else if (lowerUrl.contains("oracle")) {
            return "Oracle";
        } else if (lowerUrl.contains("sqlserver")) {
            return "SQL Server";
        } else if (lowerUrl.contains("h2")) {
            return "H2";
        } else if (lowerUrl.contains("sqlite")) {
            return "SQLite";
        } else {
            return "其他";
        }
    }

    /**
     * 获取健康状态描述
     *
     * @param isHealthy 是否健康
     * @return 状态描述
     */
    default String getHealthStatusDescription(Boolean isHealthy) {
        if (isHealthy == null) {
            return "未知";
        }
        return isHealthy ? "健康" : "异常";
    }

    /**
     * 脱敏数据库连接URL
     *
     * @param url 原始URL
     * @return 脱敏后的URL
     */
    default String maskDatabaseUrl(String url) {
        if (url == null) {
            return null;
        }
        
        // 简单脱敏：隐藏密码部分
        return url.replaceAll("password=[^&]*", "password=***")
                 .replaceAll("pwd=[^&]*", "pwd=***");
    }

    /**
     * 脱敏用户名
     *
     * @param username 原始用户名
     * @return 脱敏后的用户名
     */
    default String maskUsername(String username) {
        if (username == null || username.length() <= 2) {
            return username;
        }
        
        if (username.length() <= 4) {
            return username.charAt(0) + "***";
        }
        
        return username.substring(0, 2) + "***" + username.substring(username.length() - 1);
    }
}