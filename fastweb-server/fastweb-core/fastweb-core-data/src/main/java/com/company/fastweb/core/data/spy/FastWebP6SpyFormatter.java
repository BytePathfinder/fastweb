package com.company.fastweb.core.data.spy;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * 自定义 P6Spy SQL 日志格式化器
 */
public class FastWebP6SpyFormatter implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (sql == null || sql.trim().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(256);
        sb.append("[SQL] ")
          .append(sql.replaceAll("\n|\r", " ").trim())
          .append(" | elapsed=").append(elapsed).append("ms")
          .append(" | category=").append(category)
          .append(" | connection=").append(connectionId);
        if (url != null) {
            sb.append(" | url=").append(url);
        }
        return sb.toString();
    }
}