# common模块设计规范

> 版本: 1.0.0  
> 最后更新: 2025-08-13

## 核心设计原则

### 1. 三"无"原则
- 无业务语义：不包含任何业务逻辑、业务规则或业务状态
- 无框架依赖：不依赖Spring、MyBatis、Redis等特定框架API
- 无运行状态：仅提供静态工具、常量、枚举等无状态功能

### 2. 语言级抽象原则
只提供Java语言层面的基础能力扩展：

- ✅ 标准注解定义（如@ApiLog、@DistributedLock）
- ✅ 基础数据结构（如KeyValue、TreeNode）
- ✅ 通用工具类（如DateUtils、JsonUtils）
- ✅ 标准异常体系（如BizException）
- ✅ 常量与枚举定义

### 3. 零侵入性原则

```java
// ✅ 符合规范 - 纯JDK依赖
public @interface ApiLog {
    String value() default "";
}

// ❌ 违反规范 - 框架侵入
public @interface ApiLog {
    String value() default "";
    Class<?> handler() default SpringLogHandler.class; // 依赖Spring
}
```

### 4. 可复用性最大化
- 跨项目复用：可在任何Java项目中使用，不限于Spring Boot
- 跨层级复用：可被controller、service、mapper各层共享
- 技术栈无关：不绑定特定技术实现

### 5. 职责单一化

| 模块 | 职责边界 | 示例 |
| --- | --- | --- |
| common | 语言级通用能力 | 日期格式化、JSON转换 |
| infra | 框架级基础设施 | RedisTemplate封装、Spring Security配置 |
| adapter | 第三方服务适配 | 支付宝SDK封装、AWS S3适配 |

### 6. 依赖最小化

```xml
<!-- ✅ common模块允许的依赖 -->
<dependencies>
    <!-- 标准库 -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
    
    <!-- 编译期工具 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>provided</scope>
    </dependency>
    
    <!-- 标准规范API -->
    <dependency>
        <groupId>jakarta.validation</groupId>
        <artifactId>jakarta.validation-api</artifactId>
    </dependency>
</dependencies>
```

### 7. 包结构规范化

```
core/common/
├── constant/          # 业务无关常量
├── enums/            # 通用枚举
├── exception/        # 标准异常体系
├── utils/            # 工具类
├── model/            # 基础数据结构
├── annotation/       # 通用注解
└── base/             # 基础抽象类（仅语言级）
```

### 8. 设计契约遵守
- 禁止包含：数据库实体、持久化注解、框架配置
- 禁止依赖：Spring Context、MyBatis、Redis客户端
- 禁止状态：实例变量、缓存数据、配置信息

这些原则确保common模块成为项目的语言级基石，为所有业务模块提供纯粹、稳定、可复用的基础能力。

