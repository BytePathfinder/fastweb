package com.company.fastweb.core.data.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus自动填充处理器
 *
 * @author FastWeb
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";
    private static final String CREATE_BY = "createBy";
    private static final String UPDATE_BY = "updateBy";
    private static final String IS_DELETED = "isDeleted";
    private static final String VERSION = "version";
    private static final String TENANT_ID = "tenantId";
    private static final String STATUS = "status";
    private static final String SORT_ORDER = "sortOrder";

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入填充字段");
        LocalDateTime now = LocalDateTime.now();
        String currentUser = getCurrentUser();
        String currentTenantId = getCurrentTenantId();
        
        this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime.class, now);
        this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime.class, now);
        this.strictInsertFill(metaObject, CREATE_BY, String.class, currentUser);
        this.strictInsertFill(metaObject, UPDATE_BY, String.class, currentUser);
        this.strictInsertFill(metaObject, IS_DELETED, Integer.class, 0);
        this.strictInsertFill(metaObject, VERSION, Integer.class, 0);
        this.strictInsertFill(metaObject, TENANT_ID, String.class, currentTenantId);
        this.strictInsertFill(metaObject, STATUS, Integer.class, 0);
        this.strictInsertFill(metaObject, SORT_ORDER, Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始更新填充字段");
        LocalDateTime now = LocalDateTime.now();
        String currentUser = getCurrentUser();
        
        this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime.class, now);
        this.strictUpdateFill(metaObject, UPDATE_BY, String.class, currentUser);
    }

    /**
     * 获取当前用户
     */
    private String getCurrentUser() {
        // TODO: 从Spring Security或ThreadLocal中获取当前用户
        return "system";
    }

    /**
     * 获取当前租户ID
     */
    private String getCurrentTenantId() {
        // TODO: 从ThreadLocal中获取当前租户ID
        return "000000";
    }
}