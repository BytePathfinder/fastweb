package com.company.fastweb.core.tenant.datasource;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.company.fastweb.core.data.properties.FastWebDataProperties;
import com.company.fastweb.core.tenant.context.TenantContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 租户数据源路由
 * 根据租户ID和租户模式进行数据源切换
 */
@Component
public class TenantDataSourceRouter {
    
    private final FastWebDataProperties properties;
    
    public TenantDataSourceRouter(FastWebDataProperties properties) {
        this.properties = properties;
    }
    
    /**
     * 路由到租户数据源
     * 如果是数据库模式，则切换到对应的租户数据源
     */
    public void routeToTenantDataSource() {
        String tenantId = TenantContextHolder.getTenantId();
        if (StringUtils.hasText(tenantId) && 
                properties.getMybatisPlus().getTenant().getTenantMode() == FastWebDataProperties.TenantMode.DATABASE) {
            String dataSourceName = "tenant_" + tenantId;
            DynamicDataSourceContextHolder.push(dataSourceName);
        }
    }
    
    /**
     * 路由到指定租户数据源
     *
     * @param tenantId 租户ID
     */
    public void routeToTenantDataSource(String tenantId) {
        if (StringUtils.hasText(tenantId) && 
                properties.getMybatisPlus().getTenant().getTenantMode() == FastWebDataProperties.TenantMode.DATABASE) {
            String dataSourceName = "tenant_" + tenantId;
            DynamicDataSourceContextHolder.push(dataSourceName);
        }
    }
    
    /**
     * 路由到主数据源
     */
    public void routeToMasterDataSource() {
        DynamicDataSourceContextHolder.push(properties.getPrimary());
    }
    
    /**
     * 清除数据源路由
     */
    public void clearDataSourceRoute() {
        DynamicDataSourceContextHolder.clear();
    }
}