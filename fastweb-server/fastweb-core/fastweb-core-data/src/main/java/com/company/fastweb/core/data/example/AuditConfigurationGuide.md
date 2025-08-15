# FastWeb 审计字段配置指南

## 概述

FastWeb 数据模块支持可配置的审计字段名称，允许用户根据项目需求自定义创建人、创建时间、最后修改人、最后修改时间的字段名。

## 配置属性

### 基础配置

在 `application.yml` 中配置审计字段：

```yaml
fastweb:
  data:
    audit:
      enabled: true  # 是否启用审计功能
      create-by-field: create_by      # 创建人字段名
      create-time-field: create_time  # 创建时间字段名
      update-by-field: update_by      # 最后修改人字段名
      update-time-field: update_time  # 最后修改时间字段名
```

### 自定义字段名示例

如果数据库字段名与默认值不同，可以这样配置：

```yaml
fastweb:
  data:
    audit:
      create-by-field: creator
      create-time-field: gmt_create
      update-by-field: modifier
      update-time-field: gmt_modified
```

## 实体类使用

### 基础审计实体

创建一个包含审计字段的基础实体类：

```java
@Data
public class BaseAuditEntity {
    
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

### 业务实体继承

业务实体类继承基础审计实体：

```java
@Data
@TableName("t_user")
public class UserPO extends BaseAuditEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    private String email;
    
    // 其他业务字段...
}
```

## 数据库表设计

### 标准审计字段表结构

```sql
CREATE TABLE t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '最后修改人',
    update_time DATETIME COMMENT '最后修改时间',
    INDEX idx_create_time (create_time),
    INDEX idx_update_time (update_time)
);
```

### 自定义字段名表结构

如果配置了自定义字段名：

```sql
CREATE TABLE t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    creator VARCHAR(50) COMMENT '创建人',
    gmt_create DATETIME COMMENT '创建时间',
    modifier VARCHAR(50) COMMENT '最后修改人',
    gmt_modified DATETIME COMMENT '最后修改时间',
    INDEX idx_gmt_create (gmt_create),
    INDEX idx_gmt_modified (gmt_modified)
);
```

## 注意事项

1. **字段名一致性**：实体类中的字段名必须与配置中的字段名保持一致
2. **@TableField注解**：确保使用 `fill = FieldFill.INSERT` 或 `fill = FieldFill.INSERT_UPDATE`
3. **类型匹配**：字段类型必须与审计处理器期望的类型匹配
   - 时间字段：java.time.LocalDateTime
   - 用户字段：java.lang.String
4. **数据库兼容性**：确保数据库字段类型与Java类型匹配

## 验证配置

### 测试审计功能

创建一个测试类验证审计字段是否正确填充：

```java
@SpringBootTest
public class AuditTest {
    
    @Autowired
    private UserMapper userMapper;
    
    @Test
    public void testAuditFields() {
        UserPO user = new UserPO();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        
        userMapper.insert(user);
        
        assertNotNull(user.getCreateTime());
        assertNotNull(user.getUpdateTime());
        // createBy 和 updateBy 需要集成用户认证后才能获取
    }
}
```

### 配置验证

在应用启动时检查配置：

```java
@Component
public class AuditConfigValidator implements ApplicationRunner {
    
    @Autowired
    private FastWebDataProperties properties;
    
    @Override
    public void run(ApplicationArguments args) {
        log.info("Audit configuration:");
        log.info("Create by field: {}", properties.getAudit().getCreateByField());
        log.info("Create time field: {}", properties.getAudit().getCreateTimeField());
        log.info("Update by field: {}", properties.getAudit().getUpdateByField());
        log.info("Update time field: {}", properties.getAudit().getUpdateTimeField());
    }
}
```