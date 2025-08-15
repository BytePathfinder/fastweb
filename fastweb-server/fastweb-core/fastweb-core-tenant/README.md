# FastWeb 多租户模块

## 概述

FastWeb多租户模块提供了完整的多租户解决方案，支持多种租户隔离模式，包括字段模式、数据库模式和Schema模式。

## 功能特性

- 🏢 **多租户隔离**: 支持字段、数据库、Schema三种隔离模式
- 🔄 **动态数据源**: 支持运行时动态切换数据源
- 🎯 **租户识别**: 从请求头、参数、Cookie中自动识别租户
- 💾 **租户缓存**: 租户感知的缓存Key生成器
- 🛡️ **SQL拦截**: 自动在SQL中添加租户条件
- ⚙️ **配置驱动**: 通过配置文件控制所有功能

## 快速开始

### 1. 添加依赖

在您的项目中添加以下依赖：

```xml
<dependency>
    <groupId>com.company</groupId>
    <artifactId>fastweb-core-tenant</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置文件

在`application.yml`中添加多租户配置：

```yaml
fastweb:
  data:
    enabled: true
    primary: master
    datasource:
      master:
        url: jdbc:mysql://localhost:3306/fastweb_master
        username: root
        password: password
        driver-class-name: com.mysql.cj.jdbc.Driver
        hikari:
          maximum-pool-size: 20
          minimum-idle: 5
      tenant_1001:
        url: jdbc:mysql://localhost:3306/fastweb_tenant_1001
        username: tenant1
        password: password
        driver-class-name: com.mysql.cj.jdbc.Driver
      tenant_1002:
        url: jdbc:mysql://localhost:3306/fastweb_tenant_1002
        username: tenant2
        password: password
        driver-class-name: com.mysql.cj.jdbc.Driver
    
    mybatis-plus:
      mapper-locations: classpath*:/mapper/**/*.xml
      type-aliases-package: com.company.fastweb.**.entity
      pagination:
        enabled: true
        max-limit: 1000
      tenant:
        enabled: true
        tenant-id-column: tenant_id
        tenant-mode: COLUMN  # COLUMN, DATABASE, SCHEMA
        ignore-tables:
          - sys_tenant
          - sys_config
          - sys_dict
          - sys_menu
        ignore-sql-types: []
```

### 3. 实体类设计

确保您的实体类包含租户ID字段：

```java
@Data
@TableName("sys_user")
public class User extends BaseEntity {
    
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    
    private String username;
    
    private String email;
    
    /**
     * 租户ID - 多租户字段
     */
    @TableField("tenant_id")
    private String tenantId;
}
```

## 租户模式

### 1. 字段模式 (COLUMN)

通过在表中添加租户ID字段来隔离数据，所有租户共享同一个数据库和表结构。

**优点**:
- 部署简单，只需一个数据库
- 数据备份和维护相对简单
- 成本较低

**缺点**:
- 数据隔离性相对较弱
- 单个数据库性能瓶颈
- 数据量大时查询性能下降

**适用场景**: 租户数量较少，数据量不大的场景

### 2. 数据库模式 (DATABASE)

每个租户使用独立的数据库，通过动态数据源切换实现隔离。

**优点**:
- 数据隔离性强
- 性能隔离，互不影响
- 可以为不同租户定制数据库配置

**缺点**:
- 部署和维护复杂
- 成本较高
- 数据库连接数增加

**适用场景**: 租户数量适中，对数据隔离性要求高的场景

### 3. Schema模式 (SCHEMA)

每个租户使用独立的Schema，在同一数据库实例中实现逻辑隔离。

**优点**:
- 数据隔离性较好
- 相比数据库模式成本更低
- 便于统一管理

**缺点**:
- 数据库实例仍可能成为瓶颈
- Schema管理复杂度增加

**适用场景**: 需要较好隔离性但成本敏感的场景

## 使用示例

### 1. 控制器层

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 查询用户列表 - 自动应用租户过滤
     */
    @GetMapping
    public List<User> listUsers() {
        return userService.list();
    }
    
    /**
     * 创建用户 - 自动设置租户ID
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }
}
```

### 2. 服务层

```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    /**
     * 查询用户列表 - 自动应用租户过滤
     */
    @Override
    public List<User> list() {
        return baseMapper.selectList(null);
    }
    
    /**
     * 使用租户感知缓存
     */
    @Override
    @Cacheable(value = "userCache", keyGenerator = "tenantAwareCacheKeyGenerator")
    public User getById(Long id) {
        return baseMapper.selectById(id);
    }
    
    /**
     * 忽略租户过滤的查询（需要自定义注解）
     */
    @Override
    @TenantIgnore
    public List<User> listAllTenants() {
        return baseMapper.selectList(null);
    }
}
```

### 3. 租户切换

```java
@Service
public class TenantSwitchService {
    
    @Autowired
    private TenantDataSourceRouter dataSourceRouter;
    
    /**
     * 切换到指定租户
     */
    public void switchToTenant(String tenantId) {
        TenantContextHolder.setTenantId(tenantId);
        dataSourceRouter.routeToTenantDataSource(tenantId);
    }
    
    /**
     * 切换到主数据源
     */
    public void switchToMaster() {
        TenantContextHolder.clear();
        dataSourceRouter.routeToMasterDataSource();
    }
}
```

## 租户识别

系统支持多种方式识别租户：

### 1. 请求头方式

```bash
curl -H "X-Tenant-ID: 1001" http://localhost:8080/api/users
```

### 2. 请求参数方式

```bash
curl http://localhost:8080/api/users?tenantId=1001
```

### 3. Cookie方式

```javascript
document.cookie = "tenant_id=1001; path=/";
```

## 自定义租户解析器

如果默认的租户解析逻辑不满足需求，可以自定义租户解析器：

```java
@Component
public class CustomTenantResolver implements TenantResolver {
    
    @Override
    public String resolveTenantId(HttpServletRequest request) {
        // 从JWT Token中解析租户ID
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token)) {
            // 解析JWT获取租户ID
            return parseTokenForTenantId(token);
        }
        return null;
    }
    
    private String parseTokenForTenantId(String token) {
        // JWT解析逻辑
        return "1001";
    }
}
```

## 注意事项

1. **数据库设计**: 确保所有业务表都包含租户ID字段
2. **索引优化**: 为租户ID字段添加索引以提升查询性能
3. **数据迁移**: 现有数据需要添加租户ID字段并填充数据
4. **权限控制**: 确保用户只能访问自己租户的数据
5. **缓存隔离**: 使用租户感知的缓存Key生成器避免数据串扰

## 故障排除

### 1. 租户ID为空

检查租户解析器是否正确配置，确认请求中包含租户标识。

### 2. SQL未添加租户条件

检查表名是否在忽略列表中，确认多租户功能已启用。

### 3. 数据源切换失败

检查数据源配置是否正确，确认动态数据源功能已启用。

## 更多信息

- [FastWeb官方文档](https://fastweb.example.com)
- [多租户最佳实践](https://fastweb.example.com/tenant-best-practices)
- [性能优化指南](https://fastweb.example.com/performance-guide)