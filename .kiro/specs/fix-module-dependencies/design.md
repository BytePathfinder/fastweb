# 模块依赖修复设计文档

## 概述

本设计文档描述了如何修复 FastWeb 项目中的模块依赖问题，确保所有模块能够正常编译和运行。

## 架构

### 当前问题分析

1. **编译错误**：`fastweb-app` 模块无法找到 `CacheService` 类
2. **依赖缺失**：新增的核心模块未被正确引用
3. **自动配置问题**：部分模块的自动配置可能无法正常工作

### 依赖关系图

```
fastweb-app
├── fastweb-core-common
├── fastweb-core-exception  
├── fastweb-core-data
├── fastweb-core-security
├── fastweb-core-cache      ← 缺失
├── fastweb-core-config     ← 缺失
├── fastweb-core-monitor    ← 缺失
└── fastweb-core-storage    ← 缺失
```

## 组件和接口

### 1. 依赖配置修复

#### fastweb-app 模块依赖
需要添加以下依赖：
- `fastweb-core-cache`：缓存服务
- `fastweb-core-config`：配置管理
- `fastweb-core-monitor`：监控服务
- `fastweb-core-storage`：文件存储

#### 条件依赖处理
某些依赖应该设置为 `optional=true`，避免强制依赖：
- MinIO 相关依赖
- Quartz 相关依赖
- Prometheus 相关依赖

### 2. 自动配置验证

#### 配置类检查
确保所有自动配置类都能正确加载：
- `CacheAutoConfiguration`
- `ConfigAutoConfiguration`
- `MonitorAutoConfiguration`
- `StorageAutoConfiguration`

#### 条件注解验证
检查条件注解是否正确：
- `@ConditionalOnClass`
- `@ConditionalOnMissingBean`
- `@ConditionalOnProperty`

### 3. 测试接口增强

#### 新增测试端点
为新模块添加测试端点：
- `/api/test/config` - 配置管理测试
- `/api/test/monitor` - 监控数据测试
- `/api/test/storage` - 文件存储测试

## 数据模型

### 配置数据模型
```java
public class ConfigTestRequest {
    private String key;
    private String value;
    private String description;
}

public class ConfigTestResponse {
    private String key;
    private String value;
    private boolean success;
}
```

### 监控数据模型
```java
public class MonitorTestResponse {
    private Map<String, Object> systemInfo;
    private Map<String, Object> jvmInfo;
    private Map<String, Object> healthStatus;
}
```

### 存储数据模型
```java
public class StorageTestResponse {
    private String fileName;
    private String fileUrl;
    private long fileSize;
    private boolean success;
}
```

## 错误处理

### 依赖缺失处理
- 使用条件注解避免强制依赖
- 提供友好的错误提示
- 支持功能降级

### 自动配置失败处理
- 记录详细的错误日志
- 提供配置建议
- 支持手动配置覆盖

## 测试策略

### 编译测试
1. 执行 `mvn clean compile` 验证编译成功
2. 检查所有模块的依赖解析
3. 验证自动配置类加载

### 功能测试
1. 启动应用验证自动配置
2. 调用测试接口验证功能
3. 检查日志输出确认模块加载

### 集成测试
1. 测试模块间的协作
2. 验证缓存、配置、监控等功能
3. 测试错误场景的处理

## 实施步骤

### 阶段 1：修复编译问题
1. 更新 `fastweb-app` 的 pom.xml
2. 添加缺失的模块依赖
3. 验证编译成功

### 阶段 2：完善自动配置
1. 检查自动配置类
2. 修复条件注解问题
3. 验证自动配置加载

### 阶段 3：增强测试接口
1. 添加新模块的测试端点
2. 完善错误处理
3. 验证功能正常

### 阶段 4：集成验证
1. 端到端功能测试
2. 性能和稳定性测试
3. 文档更新