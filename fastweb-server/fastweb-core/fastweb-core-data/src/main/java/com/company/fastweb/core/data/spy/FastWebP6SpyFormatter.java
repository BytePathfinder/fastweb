package com.company.fastweb.core.data.spy;

import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * P6Spy SQL格式化器
 * 提供美观的SQL输出格式，包含执行时间、连接信息等
 */
public class FastWebP6SpyFormatter implements MessageFormattingStrategy {
    
    private static final Logger log = LoggerFactory.getLogger(FastWebP6SpyFormatter.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, 
                               String category, String prepared, String sql, String url) {
        
        StringBuilder sb = new StringBuilder();
        
        // 时间戳
        sb.append("\n┌─[SQL执行日志] ").append(DATE_FORMAT.format(new Date())).append(" ─┐");
        
        // 连接信息
        sb.append("\n├─ 连接ID: ").append(connectionId);
        sb.append("\n├─ 数据源: ").append(extractDatabaseName(url));
        
        // 执行时间
        sb.append("\n├─ 执行时间: ").append(elapsed).append("ms");
        if (elapsed > 1000) {
            sb.append(" ⚠️ 慢查询");
        } else if (elapsed > 100) {
            sb.append(" ⚡ 较快");
        } else {
            sb.append(" ✅ 正常");
        }
        
        // SQL类型
        sb.append("\n├─ SQL类型: ").append(getSqlType(sql));
        
        // 完整SQL
        sb.append("\n├─ 完整SQL:");
        sb.append("\n├─ ").append(formatSql(sql));
        
        sb.append("\n└─").append("-".repeat(80));
        
        return sb.toString();
    }
    
    private String extractDatabaseName(String url) {
        if (url == null) return "unknown";
        try {
            // jdbc:mysql://localhost:3306/fastweb?...
            String[] parts = url.split("/");
            if (parts.length >= 4) {
                String dbPart = parts[3];
                return dbPart.split("\\?")[0];
            }
        } catch (Exception e) {
            log.warn("解析数据库名称失败: {}", url, e);
        }
        return "unknown";
    }
    
    private String getSqlType(String sql) {
        if (sql == null) return "UNKNOWN";
        String upperSql = sql.trim().toUpperCase();
        if (upperSql.startsWith("SELECT")) return "SELECT";
        if (upperSql.startsWith("INSERT")) return "INSERT";
        if (upperSql.startsWith("UPDATE")) return "UPDATE";
        if (upperSql.startsWith("DELETE")) return "DELETE";
        return "OTHER";
    }
    
    private String formatSql(String sql) {
        if (sql == null) return "";
        
        // 美化SQL格式
        String formatted = sql.trim()
                .replaceAll("\\s+", " ") // 合并多余空格
                .replaceAll("\\s*,\\s*", ", ") // 规范逗号格式
                .replaceAll("\\s*\\(\\s*", " (") // 规范括号格式
                .replaceAll("\\s*\\)\\s*", ") ") // 规范括号格式
                .replaceAll("\\s*;\\s*", ";"); // 规范分号格式
        
        // 长SQL换行处理
        if (formatted.length() > 100) {
            formatted = formatted.replaceAll("\\s+(FROM|WHERE|ORDER BY|GROUP BY|LEFT JOIN|INNER JOIN|RIGHT JOIN)", 
                    "\\n$1");
        }
        
        return formatted;
    }
}