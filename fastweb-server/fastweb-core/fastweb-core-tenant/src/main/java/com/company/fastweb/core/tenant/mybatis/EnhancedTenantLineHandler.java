package com.company.fastweb.core.tenant.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.company.fastweb.core.data.properties.FastWebDataProperties;
import com.company.fastweb.core.tenant.context.TenantContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 增强版多租户处理器
 * 用于MyBatis-Plus的多租户支持
 */
public class EnhancedTenantLineHandler implements TenantLineHandler {

    private final FastWebDataProperties properties;
    private final List<String> ignoreTables;

    public EnhancedTenantLineHandler(FastWebDataProperties properties) {
        this.properties = properties;
        this.ignoreTables = properties.getMybatisPlus().getTenant().getIgnoreTables();
    }

    @Override
    public Expression getTenantId() {
        String tenantId = TenantContextHolder.getTenantId();
        if (StringUtils.hasText(tenantId)) {
            return new StringValue(tenantId);
        }
        return null;
    }

    @Override
    public String getTenantIdColumn() {
        return properties.getMybatisPlus().getTenant().getTenantIdColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return ignoreTables.contains(tableName);
    }
}