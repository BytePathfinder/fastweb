# FastWeb Core模块架构设计规范

## 概述

本文档定义了FastWeb项目中core模块的标准化架构设计，参考ruoyi-vue-plus的功能架构和分层设计模式 <mcreference link="https://gitee.com/zhijiantianya/ruoyi-vue-pro" index="4">4</mcreference>，确保各模块结构清晰、职责分明，提升代码的可维护性和开发效率。

## 设计原则

### 1. 模块化设计
- 每个core模块按功能职责独立设计
- 模块间通过接口进行交互，降低耦合度
- 支持模块的独立开发、测试和部署

### 2. 分层架构
- 采用经典的三层架构：Controller层、Service层、Mapper层
- 每层职责明确，遵循单一职责原则
- 数据对象按用途分类：Form、DTO、VO、PO、QO、RO

### 3. 统一规范
- 包命名遵循统一规范
- 类命名和方法命名保持一致性
- 代码结构标准化，便于团队协作

## 标准包结构

### 基础包结构模板

```
com.company.fastweb.core.{模块名}
├── config/                    # 配置类
│   ├── {模块名}AutoConfiguration.java
│   └── {模块名}Properties.java
├── controller/                # 控制层（如需要）
│   ├── admin/                 # 管理端接口
│   └── client/                # 客户端接口
├── service/                   # 业务逻辑层
│   ├── {功能}Service.java
│   └── impl/
│       └── {功能}ServiceImpl.java
├── mapper/                    # 数据访问层（如需要）
│   └── {实体}Mapper.java
├── model/                     # 数据模型
│   ├── form/                  # 表单对象
│   ├── dto/                   # 数据传输对象
│   ├── vo/                    # 视图对象
│   ├── po/                    # 持久化对象
│   ├── qo/                    # 查询对象
│   └── ro/                    # 结果对象
├── converter/                 # 对象转换器
│   └── {实体}Converter.java
├── enums/                     # 枚举类
├── exception/                 # 异常类
├── annotation/                # 注解类
├── aspect/                    # 切面类
├── util/                      # 工具类
└── constant/                  # 常量类
```

### 数据对象分类说明

| 对象类型 | 命名规范 | 使用场景 | 示例 |
|---------|----------|----------|------|
| **Form** | XxxCreateForm | 前端表单对象（需校验） | StorageCreateForm |
| **DTO** | XxxDTO | 业务层传输对象 | StorageDTO |
| **VO** | XxxVO | 响应视图对象 | StorageDetailVO |
| **QO** | XxxQO | 查询参数对象 | StorageQueryQO |
| **PO** | XxxPO | 持久化映射对象 | StoragePO |
| **RO** | XxxRO | 聚合查询结果对象 | StorageStatisticsRO |

## 各模块具体设计

### 1. core-storage（存储模块）

```
com.company.fastweb.core.storage
├── config/
│   ├── StorageAutoConfiguration.java
│   └── StorageProperties.java
├── controller/
│   ├── admin/
│   │   └── StorageAdminController.java
│   └── client/
│       └── LocalStorageController.java
├── service/
│   ├── StorageService.java
│   ├── BucketService.java
│   └── impl/
│       ├── LocalStorageServiceImpl.java
│       ├── MinioStorageServiceImpl.java
│       └── BucketServiceImpl.java
├── model/
│   ├── form/
│   │   ├── StorageUploadForm.java
│   │   └── BucketCreateForm.java
│   ├── dto/
│   │   ├── StorageDTO.java
│   │   └── BucketDTO.java
│   ├── vo/
│   │   ├── StorageVO.java
│   │   └── BucketVO.java
│   └── po/
│       └── StorageObject.java
├── converter/
│   ├── StorageConverter.java
│   └── BucketConverter.java
├── enums/
│   └── StorageTypeEnum.java
├── exception/
│   └── StorageException.java
└── util/
    └── StorageRetryUtil.java
```

### 2. core-cache（缓存模块）

```
com.company.fastweb.core.cache
├── config/
│   ├── CacheAutoConfiguration.java
│   └── FastWebCacheProperties.java
├── service/
│   ├── CacheService.java
│   └── impl/
│       └── CacheServiceImpl.java
├── model/
│   ├── dto/
│   │   └── CacheInfoDTO.java
│   └── vo/
│       └── CacheInfoVO.java
├── converter/
│   └── CacheConverter.java
├── annotation/
│   ├── Cacheable.java
│   └── CacheEvict.java
├── lock/
│   └── DistributedLockClient.java
└── util/
    ├── CacheUtils.java
    └── RedisKeyBuilder.java
```

### 3. core-security（安全模块）

```
com.company.fastweb.core.security
├── config/
│   └── SecurityAutoConfiguration.java
├── service/
│   ├── AuthService.java
│   ├── PermissionService.java
│   └── impl/
│       ├── AuthServiceImpl.java
│       └── PermissionServiceImpl.java
├── model/
│   ├── dto/
│   │   ├── LoginUserDTO.java
│   │   └── PermissionDTO.java
│   ├── vo/
│   │   └── LoginUserVO.java
│   └── po/
│       └── LoginUser.java
├── converter/
│   └── SecurityConverter.java
├── annotation/
│   └── PreAuthorize.java
├── aspect/
│   └── PreAuthorizeAspect.java
├── expression/
│   └── PermissionExpressionEngine.java
└── util/
    └── SecurityUtils.java
```

### 4. core-data（数据访问模块）

```
com.company.fastweb.core.data
├── config/
│   ├── DataAutoConfiguration.java
│   └── MyBatisPlusConfig.java
├── model/
│   ├── po/
│   │   └── BaseEntity.java
│   └── dto/
│       └── PageDTO.java
├── enums/
│   └── StatusEnum.java
├── handler/
│   ├── JsonMapTypeHandler.java
│   └── MyMetaObjectHandler.java
├── datasource/
│   ├── DataSourceContextHolder.java
│   └── annotation/
│       └── DS.java
├── properties/
│   └── FastWebDataProperties.java
├── spy/
│   └── FastWebP6SpyFormatter.java
└── util/
    └── MyBatisPlusUtils.java
```

### 5. core-monitor（监控模块）

```
com.company.fastweb.core.monitor
├── config/
│   └── MonitorAutoConfiguration.java
├── controller/
│   └── admin/
│       └── MonitorAdminController.java
├── service/
│   ├── MonitorService.java
│   ├── MetricsService.java
│   └── impl/
│       ├── DefaultMonitorServiceImpl.java
│       └── MetricsServiceImpl.java
├── model/
│   ├── dto/
│   │   ├── SystemInfoDTO.java
│   │   └── MetricsDTO.java
│   └── vo/
│       ├── SystemInfoVO.java
│       └── MetricsVO.java
├── converter/
│   └── MonitorConverter.java
└── collector/
    ├── SystemInfoCollector.java
    └── MetricsCollector.java
```

## 分层职责说明

### Controller层
- **职责**：接收HTTP请求，参数校验，调用Service层，返回响应
- **输入**：Form对象（表单数据）
- **输出**：VO对象（视图数据）
- **规范**：
  - 使用`@RestController`注解
  - 统一返回`ApiResult<T>`格式
  - 参数校验使用`@Valid`注解
  - 异常统一由全局异常处理器处理

### Service层
- **职责**：业务逻辑处理，事务管理，调用Mapper层
- **输入**：DTO对象（业务数据）
- **输出**：DTO对象（业务数据）
- **规范**：
  - 接口与实现分离
  - 使用`@Service`注解
  - 事务注解`@Transactional`
  - 不直接使用Form/VO对象

### Mapper层
- **职责**：数据持久化操作，SQL执行
- **输入**：PO对象、QO对象、简单类型
- **输出**：PO对象、RO对象
- **规范**：
  - 继承`BaseMapper<PO>`
  - 使用`@Mapper`注解
  - 复杂查询使用XML文件
  - 简单查询使用注解

## 对象转换规范

### 转换器设计
- 使用MapStruct自动生成转换代码
- 每个业务实体对应一个Converter
- 转换方法命名规范：`toDTO()`, `toPO()`, `toVO()`

### 转换流程
```
Controller: Form → DTO → Service → DTO → Mapper: PO/QO
Mapper: PO/RO → Service: DTO → Controller: VO
```

## 配置规范

### AutoConfiguration
- 每个模块提供自动配置类
- 使用`@ConditionalOnProperty`控制启用条件
- 提供默认配置和自定义配置支持

### Properties
- 配置属性类使用`@ConfigurationProperties`
- 提供配置校验和默认值
- 配置前缀统一为`fastweb.{模块名}`

## 异常处理规范

### 异常分类
- **业务异常**：继承`BizException`
- **系统异常**：继承`SystemException`
- **参数异常**：使用Bean Validation

### 异常处理
- 全局异常处理器统一处理
- 异常信息国际化支持
- 异常日志记录和监控

## 测试规范

### 单元测试
- Service层必须编写单元测试
- 使用Mock框架模拟依赖
- 测试覆盖率要求80%以上

### 集成测试
- 关键功能编写集成测试
- 使用TestContainers进行数据库测试
- 测试环境隔离

## 文档规范

### 代码文档
- 类和方法必须添加JavaDoc注释
- 复杂业务逻辑添加详细注释
- 接口文档使用Swagger/OpenAPI

### 架构文档
- 模块设计文档
- API接口文档
- 部署运维文档

## 总结

通过统一的架构设计和规范约束，确保FastWeb项目的core模块具有：

1. **清晰的结构**：标准化的包结构和分层架构
2. **明确的职责**：每层职责单一，边界清晰
3. **统一的规范**：命名、编码、测试等统一标准
4. **良好的扩展性**：模块化设计，易于扩展和维护
5. **高效的开发**：减少重复工作，提升开发效率

参考ruoyi-vue-plus的成功实践 <mcreference link="https://doc.ruoyi.vip/ruoyi-vue/" index="1">1</mcreference> <mcreference link="http://chenxutan.com/d/346.html" index="5">5</mcreference>，结合FastWeb项目的实际需求，形成了这套完整的架构设计规范。