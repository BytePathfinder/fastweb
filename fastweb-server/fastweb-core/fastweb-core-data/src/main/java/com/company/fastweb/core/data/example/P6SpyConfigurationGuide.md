# P6Spy SQL监控配置指南

## 概述

FastWeb已集成P6Spy框架，提供完整的SQL监控能力，包括：
- 完整的SQL语句输出（含参数值）
- 精确的执行时间统计
- 慢查询自动标记
- 美观的格式化输出
- 多数据源支持

## 快速开始

### 1. 启用P6Spy监控

在`application.yml`中添加配置：

```yaml
fastweb:
  data:
    p6spy:
      enabled: true  # 启用SQL监控
      slow-query-threshold: 1000  # 慢查询阈值（毫秒）
      log-parameters: true  # 是否记录SQL参数
      log-to-file: false  # 是否输出到文件
      log-file: logs/p6spy.log  # 日志文件路径
```

### 2. 多环境配置

#### 开发环境（完整SQL输出）
```yaml
spring:
  profiles:
    active: dev

fastweb:
  data:
    p6spy:
      enabled: true
      slow-query-threshold: 500  # 开发环境更敏感的慢查询检测
```

#### 测试环境（仅慢查询）
```yaml
spring:
  profiles:
    active: test

fastweb:
  data:
    p6spy:
      enabled: true
      slow-query-threshold: 1000  # 仅记录超过1秒的查询
```

#### 生产环境（关闭监控）
```yaml
spring:
  profiles:
    active: prod

fastweb:
  data:
    p6spy:
      enabled: false  # 生产环境关闭监控
```

## 输出格式示例

### 正常查询
```
┌─[SQL执行日志] 2024-01-15 14:30:45.123 ─┐
├─ 连接ID: 1
├─ 数据源: fastweb
├─ 执行时间: 15ms ✅ 正常
├─ SQL类型: SELECT
├─ 完整SQL:
├─ SELECT id, username, email, create_time, update_time 
FROM t_user 
WHERE is_deleted = 0 
ORDER BY create_time DESC 
LIMIT 10
└────────────────────────────────────────────────────────────────────────────────
```

### 慢查询
```
┌─[SQL执行日志] 2024-01-15 14:31:02.456 ─┐
├─ 连接ID: 2
├─ 数据源: fastweb
├─ 执行时间: 2150ms ⚠️ 慢查询
├─ SQL类型: SELECT
├─ 完整SQL:
├─ SELECT u.id, u.username, r.name as role_name, COUNT(DISTINCT o.id) as order_count
FROM t_user u
LEFT JOIN t_user_role ur ON u.id = ur.user_id
LEFT JOIN t_role r ON ur.role_id = r.id
LEFT JOIN t_order o ON u.id = o.user_id
WHERE u.create_time >= '2024-01-01'
GROUP BY u.id, u.username, r.name
ORDER BY order_count DESC
└────────────────────────────────────────────────────────────────────────────────
```

## 高级配置

### 1. 自定义慢查询阈值

针对不同数据源设置不同阈值：

```yaml
fastweb:
  data:
    p6spy:
      enabled: true
    datasource:
      master:
        url: jdbc:mysql://localhost:3306/fastweb
        # 主库阈值：500ms
        slow-query-threshold: 500
      slave:
        url: jdbc:mysql://localhost:3306/fastweb_slave
        # 从库阈值：1000ms（允许更长的查询时间）
        slow-query-threshold: 1000
```

### 2. 日志文件输出

如需将SQL日志输出到独立文件：

```yaml
fastweb:
  data:
    p6spy:
      enabled: true
      log-to-file: true
      log-file: logs/sql-monitor.log

logging:
  level:
    p6spy: info
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
```

### 3. 排除特定SQL

在`spy.properties`中添加排除规则：

```properties
# 排除健康检查查询
exclude=SELECT 1

# 排除特定表
exclude=select * from t_config
exclude=select * from t_schedule_log
```

## 性能影响

- **开发环境**：影响极小，可忽略不计
- **测试环境**：建议仅记录慢查询
- **生产环境**：默认关闭，可通过动态配置开启

## 集成测试

### 1. 验证配置生效

启动应用后，控制台应显示：
```
FastWeb P6Spy SQL监控已启用
```

### 2. 执行查询测试

执行任意数据库操作，观察控制台输出是否包含格式化SQL。

### 3. 慢查询测试

执行一个复杂查询，验证慢查询标记：

```java
@Test
public void testSlowQuery() {
    // 执行复杂查询
    List<Map<String, Object>> result = jdbcTemplate.queryForList(
        "SELECT * FROM t_user u " +
        "LEFT JOIN t_order o ON u.id = o.user_id " +
        "LEFT JOIN t_order_item i ON o.id = i.order_id " +
        "WHERE u.create_time >= '2024-01-01' " +
        "ORDER BY o.create_time DESC"
    );
    
    // 验证控制台输出包含慢查询标记
}
```

## 故障排查

### 问题1：无SQL输出

**现象**：启用后无SQL日志输出

**解决方案**：
1. 检查`fastweb.data.p6spy.enabled=true`配置
2. 确认应用有数据库操作
3. 检查日志级别设置

### 问题2：格式混乱

**现象**：SQL输出格式不美观

**解决方案**：
1. 确认使用FastWebP6SpyFormatter
2. 检查控制台编码（建议使用UTF-8）

### 问题3：性能下降

**现象**：应用响应变慢

**解决方案**：
1. 生产环境关闭P6Spy
2. 调整慢查询阈值，减少日志量
3. 使用异步日志输出

## 最佳实践

### 1. 开发阶段
- 启用完整SQL监控
- 设置较低的慢查询阈值（200-500ms）
- 关注SQL执行计划

### 2. 测试阶段
- 启用慢查询监控
- 分析慢查询原因
- 优化索引和查询语句

### 3. 生产阶段
- 默认关闭监控
- 紧急情况下可通过配置中心动态开启
- 定期分析慢查询日志

## 扩展功能

如需更高级的SQL分析功能，可考虑集成：
- **Druid**：阿里巴巴开源的数据库连接池，提供SQL防火墙功能
- **MyBatis-Plus SQL分析器**：分析SQL执行计划
- **APM工具**：SkyWalking、Pinpoint等分布式追踪系统