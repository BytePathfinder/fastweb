package com.company.fastweb.core.data.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 多租户处理器实现
 *
 * @author FastWeb
 */
@Component
public class TenantLineHandlerImpl implements TenantLineHandler {

    /**
     * 租户字段名
     */
    private static final String TENANT_ID_COLUMN = "tenant_id";

    /**
     * 忽略多租户的表
     */
    private static final List<String> IGNORE_TABLES = Arrays.asList(
            "t_sys_config",
            "t_sys_dict",
            "t_sys_dict_data",
            "t_sys_tenant"
    );

    /**
     * 获取租户ID值表达式，只支持单个ID值
     */
    @Override
    public Expression getTenantId() {
        // TODO: 从ThreadLocal或SecurityContext中获取当前租户ID
        return new StringValue("000000");
    }

    /**
     * 获取租户字段名
     */
    @Override
    public String getTenantIdColumn() {
        return TENANT_ID_COLUMN;
    }

    /**
     * 根据表名判断是否忽略拼接多租户条件
     */
    @Override
    public boolean ignoreTable(String tableName) {
        return tableName != null && IGNORE_TABLES.stream()
                .anyMatch(table -> table.equalsIgnoreCase(tableName));
    }
}