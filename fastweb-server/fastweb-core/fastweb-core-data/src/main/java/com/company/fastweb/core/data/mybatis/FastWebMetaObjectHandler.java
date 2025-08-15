package com.company.fastweb.core.data.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 元对象处理器
 * 用于自动填充创建时间、更新时间等字段
 */
public class FastWebMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        // 更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        
        // 创建人，可以从上下文中获取当前用户
        // this.strictInsertFill(metaObject, "createBy", String.class, getCurrentUsername());
    }

    /**
     * 更新操作自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        
        // 更新人，可以从上下文中获取当前用户
        // this.strictUpdateFill(metaObject, "updateBy", String.class, getCurrentUsername());
    }
    
    /**
     * 获取当前用户名
     * 这里可以根据实际的用户认证方式获取
     */
    private String getCurrentUsername() {
        // 实际项目中可以从 SecurityContextHolder 或自定义的用户上下文中获取
        return "system";
    }
}