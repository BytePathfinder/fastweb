# Spring Boot 企业级开发规范

基于 Spring Boot 3.2.x + JDK 17 + MyBatis-Plus 3.5.7+ 的企业级开发规范

---

## 一、技术选型标准

### 1.1 基础技术栈

- **JDK 17** - LTS版本，平衡稳定性和新特性
- **Spring Boot 3.2.x** - 最新稳定版，支持Java 17
- **Maven 3.9.x** - 多模块聚合构建工具

### 1.2 数据层技术

- **MySQL 8.0.x** - 主数据库
- **HikariCP 5.x** - 高性能连接池
- **MyBatis-Plus 3.5.7+** - ORM框架，提供强大的CRUD能力
- **Flyway 10.x** - 数据库版本管理
- **Redis 7.x** - 缓存/会话/分布式锁

### 1.3 中间件与服务

- **MinIO** - S3兼容对象存储
- **Quartz** - 定时任务调度
- **Spring Boot Starter Mail** - 邮件服务
- **Sa-Token 1.37.x** - 认证、授权、权限缓存

### 1.4 开发效率工具

- **Lombok 1.18.x** - 减少样板代码
- **MapStruct 1.5.x** - 对象映射转换
- **Knife4j 4.x** - OpenAPI 3文档与在线调试

### 1.5 监控与测试

- **Logback + MDC traceId** - 链路日志
- **Spring Boot Actuator** - 应用监控
- **Micrometer Registry Prometheus** - 指标收集
- **JUnit 5.10.x** - 单元测试框架
- **Testcontainers** - 集成测试容器

---

## 二、项目架构设计

### 2.1 顶层模块结构

```
fastweb/
├── fastweb-app/                    # 应用启动层
├── fastweb-biz/                    # 业务逻辑层
│   ├── biz-user/                 # 用户业务模块
│   ├── biz-auth/                 # 认证业务模块
│   └── ...
└── fastweb-core/                   # 框架核心层
    ├── core-common               # 语言级基础能力
    📋 需求分析阶段
    ├── 🌍 core-i18n          # 国际化支持（多语言需求）
    ├── 📏 core-rule          # 规则引擎（业务规则定义）
    ├── 📋 core-doc           # 文档生成（需求文档化）
    └── 🔍 core-search        # 搜索引擎（需求检索）

    ⚙️  开发集成阶段
    ├── 📊 core-data          # 数据访问（MyBatis-Plus生态）
    ├── ⚡ core-cache          # 缓存管理（Redis/Caffeine）
    ├── 📡 core-message       # 消息通信（Kafka/RabbitMQ）
    ├── 💾 core-storage       # 文件存储（MinIO/OSS适配）
    ├── 🔄 core-workflow      # 工作流引擎（业务流程编排）
    └── 🧪 core-testing       # 测试支持（单元/集成/端到端）

    🔐 安全合规阶段
    ├── 🔒 core-security      # 安全认证（Sa-Token深度集成）
    ├── ⚠️ core-exception     # 异常处理（统一异常体系）
    ├── 🕵️ core-audit         # 审计日志（合规追溯）
    └── 🏢 core-tenant        # 多租户（SaaS化支持）

    🚀 交付运维阶段
    ├── ⚙️ core-config        # 配置管理（Nacos/Apollo适配）
    ├── ⏰ core-task          # 任务调度（Quartz分布式）
    ├── 📦 core-batch         # 批处理（Spring Batch）
    ├── 🚀 core-deploy        # 部署运维（Docker/K8s）
    └── 👁️ core-monitor       # 监控观测（Prometheus/Grafana）

    🤖 智能优化阶段
    └── 🧠 core-ai            # AI智能服务（大模型集成）
```

### 2.2 业务模块内部分层

以业务模块为核心划分，在每个业务模块内再进行技术分层：

```
com.company.project.biz.{模块名}.{功能名}
├── controller         # 控制层
│   ├── admin            # 管理后台接口（接收 form，返回 vo）
│   └── client           # 客户端/小程序/APP接口（接收 form，返回 vo）
├── service            # 业务逻辑层（接收 dto，返回 dto；不直接使用 form/vo）
│   └── impl             # 服务实现类
├── mapper             # 数据访问层（接收简单类型/po/qo，返回 po/ro）
├── model              # 跨层数据载体（form/dto/vo/qo/po/ro，无业务逻辑）
│   ├── form             # 前端表单对象（需校验）
│   ├── vo               # 响应视图对象（禁止向下传递）
│   ├── dto              # 业务层传输对象（中间转换层）
│   ├── qo               # 持久层查询参数对象
│   ├── po               # 持久层表结构映射对象（同数据库）
│   └── ro               # 持久层聚合查询结果对象
└── converter          # 对象转换器（form→dto、dto→po、po→vo 等）
```

---

## 三、数据对象规范

### 3.1 六类对象定义

| 对象类型     | 命名规范          | 使用场景        | 示例               |
|----------|---------------|-------------|------------------|
| **form** | XxxCreateForm | 前端表单对象（需校验） | UserCreateForm   |
| **dto**  | XxxDTO        | 业务层传输对象     | UserDTO          |
| **vo**   | XxxVO         | 响应视图对象      | UserDetailVO     |
| **qo**   | XxxQO         | 查询参数对象      | UserQueryQO      |
| **po**   | XxxPO         | 持久化映射对象     | UserPO           |
| **ro**   | XxxRO         | 聚合查询结果对象    | UserStatisticsRO |

### 3.2 对象流转规则

| 层级         | 输入类型       | 输出类型            | 说明                            |
|------------|------------|-----------------|-------------------------------|
| Controller | form（表单）   | vo（视图对象）        | 接收前端参数，返回渲染数据；禁止感知 po/qo      |
| Service    | dto（业务对象）  | dto（业务对象）       | 处理核心业务逻辑，协调领域对象；不直接使用 form/vo |
| Mapper     | 简单类型/po/qo | po（实体）/ro（聚合结果） | 数据持久化操作；简单查询返回 po，复杂聚合返回 ro   |

### 3.3 跨层依赖约束

- Service 层可调用其他 Service，但禁止跳过 Service 直接访问其他模块 Mapper
- Controller 禁止感知 po/qo，Mapper 禁止返回 vo
- 依赖方向：Controller → Service → Mapper（单向依赖）

---

## 四、代码实现规范

### 4.1 PO（持久化对象）规范

```java

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user")
public class UserPO {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    private String email;

    @TableLogic(value = "0", delval = "null")
    private Integer isDeleted;

    @Version
    private Integer version;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

**规范要点：**

- 类名与表名保持一致，如不一致用 `@TableName` 注解
- 使用 Lombok：`@Data @Builder @NoArgsConstructor @AllArgsConstructor`
- 主键：`@TableId(type = IdType.AUTO)` 或 `ASSIGN_ID`（雪花算法）
- 逻辑删除：`@TableLogic(value = "0", delval = "null")`
- 乐观锁：`@Version`
- 字段验证在 form 层完成，PO 只做 ORM 映射

### 4.2 Mapper（数据访问层）规范

```java

@Mapper
public interface UserMapper extends BaseMapper<UserPO> {

    // 简单查询使用注解
    @Select("SELECT * FROM t_user WHERE username = #{username}")
    UserPO findByUsername(@Param("username") String username);

    // 复杂查询使用XML
    List<UserStatisticsRO> getUserStatistics(@Param("qo") UserQueryQO qo);

    // 分页查询
    Page<UserPO> selectUserPage(Page<UserPO> page, @Param("qo") UserQueryQO qo);
}
```

**规范要点：**

- 继承 `BaseMapper<PO>`，获得基础 CRUD 操作
- 简单 SQL 使用注解：`@Select/@Update/@Insert/@Delete`
- 复杂 SQL 使用 XML 文件：`resources/mapper/表名Mapper.xml`
- 多表联查返回自定义 RO，XML 中定义 ResultMap 映射
- 禁止在 Mapper 中写业务逻辑，仅做数据访问
- 分页查询：Mapper 方法入参 `Page<T> page`，Service 层 `new Page<>(current,size)`

### 4.3 Service（业务逻辑层）规范

```java
public interface UserService extends IService<UserPO> {
    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long id);

    Page<UserDTO> getUserPage(UserQueryQO qo);
}

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {

    private final UserConverter userConverter;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        UserPO userPO = userConverter.toPO(userDTO);
        this.save(userPO);
        return userConverter.toDTO(userPO);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        UserPO userPO = this.getById(id);
        if (userPO == null) {
            throw new BizException("用户不存在: " + id);
        }
        return userConverter.toDTO(userPO);
    }
}
```

**规范要点：**

- 结构：接口 `UserService` + 实现 `UserServiceImpl`
- 实现类继承 `ServiceImpl<UserMapper, UserPO>` 并实现 `UserService`
- 事务：在 Service 方法或类上标注 `@Transactional(rollbackFor = Exception.class)`
- 校验存在性：使用 MP 提供的 `getById`、`lambdaQuery()...oneOpt().orElseThrow(...)`
- 禁止在 Service 手写 SQL，只能调用 Mapper 方法或链式条件构造器

### 4.4 Controller（控制层）规范

```java

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    @PostMapping
    public ResponseEntity<ApiResult<UserVO>> createUser(@Valid @RequestBody UserCreateForm form) {
        UserDTO dto = userConverter.toDTO(form);
        UserDTO result = userService.createUser(dto);
        return ResponseEntity.ok(ApiResult.success(userConverter.toVO(result)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<UserVO>> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(ApiResult.success(userConverter.toVO(userDTO)));
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResult<Page<UserVO>>> getUserPage(@Valid UserQueryQO qo) {
        Page<UserDTO> page = userService.getUserPage(qo);
        Page<UserVO> voPage = page.convert(userConverter::toVO);
        return ResponseEntity.ok(ApiResult.success(voPage));
    }
}
```

**规范要点：**

- 注解：`@RestController` + `@RequestMapping("/api/客户端类型（admin|client）/资源名")`
- 方法映射：GET（查询）、POST（创建）、PUT（更新）、DELETE（删除）
- 返回值：统一使用 `ResponseEntity<ApiResult<T>>`
- 参数校验：form 前加 `@Valid` 或 `@Validated`
- 全局异常：所有业务异常抛 `BizException` → `GlobalExceptionHandler` 统一处理

### 4.5 Form（表单对象）规范

```java
public record UserCreateForm(
        @NotBlank(message = "用户名不能为空")
        @Length(min = 3, max = 20, message = "用户名长度必须在3-20之间")
        String username,

        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        String email,

        @NotBlank(message = "密码不能为空")
        @Length(min = 6, max = 20, message = "密码长度必须在6-20之间")
        String password
) {
    // 可以添加 compact 构造器进行额外校验
    public UserCreateForm {
        if (username != null) {
            username = username.trim();
        }
    }
}
```

**规范要点：**

- 统一使用 Java record 定义
- 添加 Bean Validation 注解进行校验
- 可写 compact 构造器进行额外处理
- 命名：`XxxCreateForm`、`XxxUpdateForm`

### 4.6 Converter（对象转换器）规范

```java

@Mapper(componentModel = "spring")
public interface UserConverter {

    UserDTO toDTO(UserCreateForm form);

    UserDTO toDTO(UserPO po);

    UserPO toPO(UserDTO dto);

    UserVO toVO(UserDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    UserPO toCreatePO(UserDTO dto);
}
```

**规范要点：**

- 使用 MapStruct 自动生成转换代码
- 所有对象转换统一使用 converter 完成
- 转换规则：form→dto、dto→po、po→vo、ro→vo
- 禁止在 Service 中手写对象映射

---

## 五、统一响应与异常处理

### 5.1 统一响应格式

```java

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<T> {
    private String code;
    private String message;
    private T data;
    private String traceId;

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>("SUCCESS", "操作成功", data, MDC.get("traceId"));
    }

    public static <T> ApiResult<T> error(String code, String message) {
        return new ApiResult<>(code, message, null, MDC.get("traceId"));
    }
}
```

### 5.2 业务异常定义

```java
public class BizException extends RuntimeException {
    private final String code;

    public BizException(String message) {
        super(message);
        this.code = "BIZ_ERROR";
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.code = "BIZ_ERROR";
    }

    public String getCode() {
        return code;
    }
}
```

### 5.3 全局异常处理

```java

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ApiResult<Object>> handleBusiness(BizException ex) {
        log.warn("Business error: code={}, msg={}, traceId={}",
                ex.getCode(), ex.getMessage(), MDC.get("traceId"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error("VALIDATION_ERROR", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Object>> handleOther(Exception ex) {
        String traceId = MDC.get("traceId");
        log.error("Unexpected error, traceId={}", traceId, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResult.error("SYSTEM_ERROR", "系统异常，请稍后重试。 traceId=" + traceId));
    }
}
```

---

## 六、配置管理规范

### 6.1 配置文件组织

- **主配置文件**：`application.yml` 包含通用配置
- **环境配置**：`application-{profile}.yml` 按环境分离
- **配置优先级**：命令行参数 > 环境变量 > 配置文件
- **敏感信息**：使用环境变量或配置中心，禁止硬编码

### 6.2 MyBatis-Plus 配置示例

```yaml
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # dev环境
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: null
      logic-not-delete-value: 0
  mapper-locations: classpath*:/mapper/**/*.xml

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db?useSSL=false&serverTimezone=UTC
    username: root
    password: ${DB_PASSWORD:123456}
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

---

## 七、命名与代码风格规范

### 7.1 命名规范

- **包名**：全部小写，按三层架构分层：`com.company.project.{core|biz|app}.module`
- **类名**：首字母大写，使用名词或名词短语：`UserController`、`OrderService`
- **方法名**：动词开头，驼峰法：`createOrder()`、`findById()`
- **常量**：全大写下划线：`MAX_RETRY_COUNT`
- **变量**：驼峰，避免单字母：`userId`、`orderNo`
- **布尔变量**：用 `is`/`has` 前缀：`isActive`、`hasPermission`

### 7.2 文件命名规范

| 文件类型   | 命名规则              | 示例                     |
|--------|-------------------|------------------------|
| Java 类 | PascalCase（首字母大写） | `UserController.java`  |
| Java 包 | 小写连写              | `user`                 |
| 配置文件   | kebab-case（短横线分隔） | `application-dev.yml`  |
| 测试类    | 主类名 + `Test`      | `UserServiceTest.java` |
| 常量类    | 名称 + `Constants`  | `UserConstants.java`   |

### 7.3 代码风格

- 使用团队统一的 formatter（IDE 格式化配置）
- 行长建议 120，尽量保持 100 以内
- 使用 4 空格缩进，UTF-8 编码，Unix 换行
- 使用 `@Slf4j`（Lombok），禁止 `System.out.print`
- 避免魔法数字/字符串，使用命名常量或配置

---

## 八、安全与性能规范

### 8.1 安全规范

- **输入校验**：所有外部输入必须验证（长度、格式、范围等）
- **SQL 注入防护**：使用参数化查询，禁止字符串拼接 SQL
- **XSS 防护**：输出编码和 CSP 策略
- **敏感数据保护**：密码、密钥等敏感信息不得入版本控制系统
- **权限控制**：使用 `@PreAuthorize` 和 `@PostAuthorize` 进行方法级权限控制
- **审计日志**：关键操作记录审计日志，包含操作者、时间、内容

### 8.2 性能优化

- **连接池配置**：数据库、Redis、HTTP 客户端连接池合理配置
- **查询优化**：避免 N+1 查询，使用分页查询，建立合适索引
- **缓存策略**：本地缓存（Caffeine）+ 分布式缓存（Redis）
- **事务管理**：只读事务使用 `readOnly = true`，合理设置事务传播和超时
- **异步处理**：耗时操作使用异步处理，配置合理的线程池

---

## 九、测试规范

### 9.1 测试分层

- **单元测试**：测试单个类或方法，使用 Mock，目标覆盖率 ≥ 80%
- **集成测试**：测试组件间交互，使用 TestContainers
- **端到端测试**：完整业务流程测试

### 9.2 测试示例

```java

@SpringBootTest
@Testcontainers
class UserServiceTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("应该成功创建用户")
    void shouldCreateUserSuccessfully() {
        // Given
        UserDTO userDTO = UserDTO.builder()
                .username("testuser")
                .email("test@example.com")
                .build();

        // When
        UserDTO result = userService.createUser(userDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
    }
}
```

---

## 十、日志与监控规范

### 10.1 日志规范

- **日志级别**：
    - ERROR：系统错误，需要立即处理
    - WARN：警告信息，需要关注
    - INFO：重要业务信息，正常流程记录
    - DEBUG：调试信息，开发环境使用

- **结构化日志**：使用 JSON 格式，便于解析
- **上下文信息**：包含用户 ID、请求 ID、业务标识
- **敏感信息**：避免记录密码、身份证等敏感数据
- **MDC/traceId**：在入口生成 `traceId` 并放入 MDC

### 10.2 监控配置

- **应用指标**：JVM、业务指标、自定义指标
- **健康检查**：Actuator 端点监控应用状态
- **告警机制**：阈值监控、异常告警
- **性能分析**：慢查询、热点方法识别

---

## 十一、Git 版本控制规范

### 11.1 提交规范

提交信息格式：`<type>(<scope>): <subject>`

类型枚举：

- `feat`: 新增功能
- `fix`: 修复 bug
- `docs`: 文档注释
- `style`: 代码格式
- `refactor`: 重构、优化
- `perf`: 性能优化
- `test`: 增加测试
- `chore`: 构建过程或辅助工具的变动

### 11.2 分支管理

- `main/master`: 主分支，保持稳定可发布状态
- `develop`: 开发分支，包含最新开发特性
- `feature/*`: 功能分支，用于开发新功能
- `bugfix/*`: 修复分支，用于修复bug
- `hotfix/*`: 热修复分支，用于修复紧急bug

---

## 十二、开发注意事项

### 12.1 强制要求

- **分层架构**：Controller 只处理请求/权限/校验，业务逻辑放 Service，数据访问放 Mapper
- **异常处理**：禁止空 catch 块；异常必须记录日志并按业务层次适当处理或向上抛出
- **数据隔离**：禁止在实体中直接暴露给前端；使用 DTO/VO 进行数据传输
- **外部调用防护**：所有外部请求必须设置超时、重试策略和熔断机制
- **线程池规范**：线程池必须统一配置并监控；禁止直接使用 `new Thread()` 创建线程

### 12.2 禁止事项

- 禁止在 Controller 直接调用 Mapper
- 禁止在 Mapper 层写业务逻辑
- 禁止用 Map 作为返回类型（统一用 DTO/VO）
- 禁止在 SQL 中写 `*`，必须显式写字段
- 禁止把密码、密钥等敏感信息打印到日志
- 禁止把异常堆栈直接返回给前端

### 12.3 开发流程

1. 分析需求时需要结合项目上下文，并提示用户需要提供哪些信息
2. 给出方案时尽量简洁易懂
3. 结合项目给出具体代码
4. 实施当前需求代码时避免破坏其它原有逻辑
5. 在遇到需要破坏原有逻辑时提醒用户
6. 实施代码完成后，执行检查：
    - 检查代码实施是否符合预期方案
    - 检查语法、事务、锁、并发问题
    - 检查逻辑
    - 适当优化
7. 方法与复杂逻辑给予简要注释
8. 处理问题时既要语法正确也要逻辑正确

---

## 十三、配置示例与最佳实践

### 13.1 完整的 application.yml 配置示例

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  profiles:
    active: dev
  application:
    name: fast-web

  # 数据源配置
  datasource:
    url: jdbc:mysql://localhost:3306/fast_web?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: ${DB_PASSWORD:123456}
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      leak-detection-threshold: 60000

  # Redis 配置
  redis:
    host: localhost
    port: 6379
    password: ${REDIS_PASSWORD:}
    database: 0
    timeout: 3000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: -1ms

  # Jackson 配置
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
    default-property-inclusion: non_null

# MyBatis-Plus 配置
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
    call-setters-on-nulls: true
    jdbc-type-for-null: null
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: null
      logic-not-delete-value: 0
      insert-strategy: not_null
      update-strategy: not_null
      select-strategy: not_null
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.company.project.**.model.po

# 日志配置
logging:
  level:
    com.company.project: debug
    org.springframework.security: debug
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{traceId}] %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{traceId}] %logger{36} - %msg%n"

# Actuator 监控配置
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: when_authorized
  metrics:
    export:
      prometheus:
        enabled: true

# Knife4j 文档配置
knife4j:
  enable: true
  setting:
    language: zh_cn
    enable-version: true
    enable-reload-cache-parameter: true

# Sa-Token 配置
sa-token:
  token-name: Authorization
  timeout: 2592000
  activity-timeout: -1
  is-concurrent: true
  is-share: true
  token-style: uuid
  is-log: false

# Flyway 数据库迁移配置
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true
    baseline-version: 1
    encoding: UTF-8
```

### 13.2 环境配置分离

**application-dev.yml（开发环境）**

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fast_web_dev?useSSL=false&serverTimezone=UTC
    hikari:
      maximum-pool-size: 5

logging:
  level:
    root: info
    com.company.project: debug

knife4j:
  enable: true
```

**application-prod.yml（生产环境）**

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 50
      minimum-idle: 10

logging:
  level:
    root: warn
    com.company.project: info
  file:
    name: /var/log/fast-web/application.log

knife4j:
  enable: false
```

### 13.3 Maven 依赖管理示例

```xml

<properties>
    <java.version>17</java.version>
    <spring-boot.version>3.2.5</spring-boot.version>
    <mybatis-plus.version>3.5.7</mybatis-plus.version>
    <knife4j.version>4.5.0</knife4j.version>
    <mapstruct.version>1.5.5.Final</mapstruct.version>
    <sa-token.version>1.37.0</sa-token.version>
</properties>

<dependencies>
<!-- Spring Boot Starters -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- MyBatis-Plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>${mybatis-plus.version}</version>
</dependency>

<!-- MySQL Driver -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Knife4j API 文档 -->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
    <version>${knife4j.version}</version>
</dependency>

<!-- MapStruct 对象映射 -->
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>${mapstruct.version}</version>
</dependency>
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct-processor</artifactId>
    <version>${mapstruct.version}</version>
    <scope>provided</scope>
</dependency>

<!-- Sa-Token 权限认证 -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-spring-boot3-starter</artifactId>
    <version>${sa-token.version}</version>
</dependency>
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-redis-jackson</artifactId>
    <version>${sa-token.version}</version>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- 测试依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mysql</artifactId>
    <scope>test</scope>
</dependency>
</dependencies>
```

---

## 十四、质量保证与工具链

### 14.1 代码质量检查

**使用 ArchUnit 进行架构约束检查：**

```java

@AnalyzeClasses(packages = "com.company.project")
public class ArchitectureTest {

    @ArchTest
    static final ArchRule controllers_should_only_be_accessed_by_controllers =
            classes().that().resideInAPackage("..controller..")
                    .should().onlyBeAccessed().byClassesThat()
                    .resideInAnyPackage("..controller..", "..config..");

    @ArchTest
    static final ArchRule services_should_not_access_controllers =
            noClasses().that().resideInAPackage("..service..")
                    .should().accessClassesThat().resideInAPackage("..controller..");

    @ArchTest
    static final ArchRule mappers_should_only_be_accessed_by_services =
            classes().that().resideInAPackage("..mapper..")
                    .should().onlyBeAccessed().byClassesThat()
                    .resideInAnyPackage("..service..", "..config..");
}
```

### 14.2 代码格式化配置

**使用 Spotless 插件统一代码格式：**

```xml

<plugin>
    <groupId>com.diffplug.spotless</groupId>
    <artifactId>spotless-maven-plugin</artifactId>
    <version>2.43.0</version>
    <configuration>
        <java>
            <googleJavaFormat>
                <version>1.17.0</version>
                <style>GOOGLE</style>
            </googleJavaFormat>
            <removeUnusedImports/>
            <trimTrailingWhitespace/>
            <endWithNewline/>
        </java>
    </configuration>
</plugin>
```

### 14.3 MapStruct 严格配置

```java

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN
)
public interface UserConverter {
    // 转换方法定义
}
```

---

## 十五、部署与运维规范

### 15.1 Docker 容器化

**Dockerfile 示例：**

```dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app

COPY target/fast-web-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

**docker-compose.yml 示例：**

```yaml
version: '3.8'
services:
  app:
    build: ../.rules
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_URL=jdbc:mysql://mysql:3306/fast_web
      - DB_USERNAME=root
      - DB_PASSWORD=password
    depends_on:
      - mysql
      - redis

  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: fast_web
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:7-alpine
    volumes:
      - redis_data:/data

volumes:
  mysql_data:
  redis_data:
```

### 15.2 健康检查配置

```java

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // 检查数据库连接、Redis连接等
        try {
            // 执行健康检查逻辑
            return Health.up()
                    .withDetail("database", "available")
                    .withDetail("redis", "available")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
```

---

## 十六、数据库设计规范

### 16.1 数据库命名约定

#### 16.1.1 库名规范

- **命名格式**：`{项目名}_{环境}`，如 `fast_web_dev`、`fast_web_prod`
- **字符要求**：小写字母、数字、下划线，不超过32字符
- **环境标识**：dev（开发）、test（测试）、prod（生产）

#### 16.1.2 表名规范

- **命名格式**：`t_{业务模块}_{实体名}`，如 `t_user_info`、`t_order_detail`
- **字符要求**：小写字母、数字、下划线，不超过64字符
- **业务分组**：按业务模块分组，便于管理和维护
- **关联表**：多对多关系表命名 `t_{模块}_{实体1}_{实体2}_rel`

#### 16.1.3 字段名规范

- **命名格式**：小写字母、数字、下划线，使用有意义的英文单词
- **主键字段**：统一使用 `id`
- **外键字段**：`{关联表实体名}_id`，如 `user_id`、`order_id`
- **时间字段**：`create_time`、`update_time`、`delete_time`
- **状态字段**：`status`、`is_active`、`is_deleted`
- **版本字段**：`version`（乐观锁）

#### 16.1.4 索引命名规范

- **主键索引**：`pk_{表名}`
- **唯一索引**：`uk_{表名}_{字段名}`
- **普通索引**：`idx_{表名}_{字段名}`
- **复合索引**：`idx_{表名}_{字段1}_{字段2}`
- **外键索引**：`fk_{表名}_{关联表名}`

### 16.2 表结构设计原则

#### 16.2.1 基础字段规范

```sql
-- 标准表结构模板
CREATE TABLE t_user_info
(
    id          BIGINT AUTO_INCREMENT COMMENT '主键ID',
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    email       VARCHAR(100) NOT NULL COMMENT '邮箱',
    phone       VARCHAR(20) COMMENT '手机号',
    status      TINYINT  DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    is_deleted  TINYINT  DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    version     INT      DEFAULT 0 COMMENT '版本号（乐观锁）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by   BIGINT COMMENT '创建人ID',
    update_by   BIGINT COMMENT '更新人ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_info_username (username),
    UNIQUE KEY uk_user_info_email (email),
    KEY idx_user_info_status (status),
    KEY idx_user_info_create_time (create_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户信息表';
```

#### 16.2.2 字段类型选择规范

| 数据类型         | 使用场景      | 规范要求                 | 示例                             |
|--------------|-----------|----------------------|--------------------------------|
| **BIGINT**   | 主键、外键、大数值 | 自增主键统一使用             | `id BIGINT AUTO_INCREMENT`     |
| **INT**      | 普通整数、状态码  | 范围足够时优先使用            | `status INT DEFAULT 1`         |
| **TINYINT**  | 布尔值、小范围枚举 | 0/1表示布尔，1-127表示枚举    | `is_deleted TINYINT DEFAULT 0` |
| **VARCHAR**  | 变长字符串     | 根据实际需要设置长度           | `username VARCHAR(50)`         |
| **TEXT**     | 长文本内容     | 避免过度使用，影响性能          | `content TEXT`                 |
| **DECIMAL**  | 精确数值（金额）  | 金额统一使用 DECIMAL(10,2) | `amount DECIMAL(10,2)`         |
| **DATETIME** | 时间戳       | 统一使用 DATETIME        | `create_time DATETIME`         |
| **JSON**     | 结构化数据     | MySQL 5.7+ 支持，谨慎使用   | `extra_info JSON`              |

#### 16.2.3 表设计最佳实践

**1. 三范式设计原则**

- **第一范式**：字段原子性，不可再分
- **第二范式**：消除部分依赖，非主键字段完全依赖主键
- **第三范式**：消除传递依赖，非主键字段不依赖其他非主键字段

**2. 反范式设计场景**

- 查询性能优先的场景可适当冗余
- 统计类字段可以冗余存储
- 历史数据快照可以冗余保存

**3. 分表分库策略**

```sql
-- 按时间分表示例
CREATE TABLE t_order_202401 LIKE t_order;
CREATE TABLE t_order_202402 LIKE t_order;

-- 按用户ID分表示例  
CREATE TABLE t_user_info_0 LIKE t_user_info;
CREATE TABLE t_user_info_1 LIKE t_user_info;
```

### 16.3 索引使用规范

#### 16.3.1 索引设计原则

- **主键索引**：每张表必须有主键，推荐使用自增BIGINT
- **唯一索引**：业务唯一字段必须建立唯一索引
- **复合索引**：遵循最左前缀原则，高选择性字段在前
- **覆盖索引**：查询字段都在索引中，避免回表查询

#### 16.3.2 索引创建规范

```sql
-- 单字段索引
CREATE INDEX idx_user_info_email ON t_user_info (email);

-- 复合索引（注意字段顺序）
CREATE INDEX idx_order_user_status_time ON t_order_info (user_id, status, create_time);

-- 唯一索引
CREATE UNIQUE INDEX uk_user_info_phone ON t_user_info (phone);

-- 部分索引（MySQL 8.0+）
CREATE INDEX idx_user_active ON t_user_info (username) WHERE is_deleted = 0;
```

#### 16.3.3 索引优化指导

- **避免过多索引**：每张表索引数量控制在5个以内
- **定期分析索引使用情况**：删除未使用的索引
- **监控慢查询**：针对慢查询优化索引
- **索引长度限制**：VARCHAR字段索引长度不超过255

### 16.4 SQL编写指南

#### 16.4.1 查询语句规范

```sql
-- 标准查询格式
SELECT u.id,
       u.username,
       u.email,
       u.create_time
FROM t_user_info u
WHERE u.is_deleted = 0
  AND u.status = 1
  AND u.create_time >= '2024-01-01'
ORDER BY u.create_time DESC
LIMIT 20;

-- 关联查询规范
SELECT u.username,
       p.title as profile_title
FROM t_user_info u
         INNER JOIN t_user_profile p ON u.id = p.user_id
WHERE u.is_deleted = 0
  AND p.is_deleted = 0;
```

#### 16.4.2 SQL编写最佳实践

**1. 查询优化**

- 避免使用 `SELECT *`，明确指定需要的字段
- WHERE条件中避免函数操作，影响索引使用
- 使用LIMIT限制返回结果集大小
- 复杂查询考虑分步执行或使用临时表

**2. 事务处理**

```sql
-- 事务示例
START TRANSACTION;

UPDATE t_account
SET balance = balance - 100
WHERE user_id = 1;
UPDATE t_account
SET balance = balance + 100
WHERE user_id = 2;

-- 检查余额是否足够
SELECT balance
FROM t_account
WHERE user_id = 1 FOR
UPDATE;

COMMIT;
```

**3. 批量操作**

```sql
-- 批量插入
INSERT INTO t_user_info (username, email, create_time)
VALUES ('user1', 'user1@example.com', NOW()),
       ('user2', 'user2@example.com', NOW()),
       ('user3', 'user3@example.com', NOW());

-- 批量更新
UPDATE t_user_info
SET status = CASE
                 WHEN id IN (1, 2, 3) THEN 1
                 WHEN id IN (4, 5, 6) THEN 0
                 ELSE status
    END
WHERE id IN (1, 2, 3, 4, 5, 6);
```

#### 16.4.3 性能优化建议

- **分页查询优化**：使用游标分页替代OFFSET
- **子查询优化**：能用JOIN的不用子查询
- **临时表使用**：复杂查询可以使用临时表分步处理
- **执行计划分析**：使用EXPLAIN分析查询性能

### 16.5 逻辑删除与唯一约束冲突处理

#### 16.5.1 问题描述

在使用逻辑删除时，如果表中存在唯一约束字段（如用户名、邮箱），会出现以下问题：

- 删除用户后，该用户名/邮箱无法被新用户使用
- 同一用户多次删除和创建会产生唯一约束冲突

#### 16.5.2 解决方案

**方案一：联合唯一索引（推荐）**

```sql
-- 表结构设计
CREATE TABLE t_user_info
(
    id          BIGINT AUTO_INCREMENT COMMENT '主键ID',
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    email       VARCHAR(100) NOT NULL COMMENT '邮箱',
    is_deleted  TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，null-已删除',
    delete_time DATETIME COMMENT '删除时间',
    PRIMARY KEY (id),
    -- 联合唯一索引：用户名 + 逻辑删除标记
    UNIQUE KEY uk_user_username_deleted (username, is_deleted),
    -- 联合唯一索引：邮箱 + 逻辑删除标记  
    UNIQUE KEY uk_user_email_deleted (email, is_deleted)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户信息表';
```

**工作原理：**

- 未删除记录：`(username='john', is_deleted=0)` - 唯一
- 已删除记录：`(username='john', is_deleted=null)` - 可以有多条
- 同一用户名可以有1条未删除记录 + N条已删除记录

```java
// Service层正常使用MyBatis-Plus逻辑删除
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {

    @Override
    public void deleteUser(Long userId) {
        // 直接使用MyBatis-Plus的逻辑删除，无需特殊处理
        this.removeById(userId);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        // 检查用户名是否已存在（只检查未删除的记录）
        boolean exists = this.lambdaQuery()
                .eq(UserPO::getUsername, userDTO.getUsername())
                .eq(UserPO::getIsDeleted, 0)  // MyBatis-Plus会自动添加此条件
                .exists();

        if (exists) {
            throw new BizException("用户名已存在");
        }

        UserPO userPO = userConverter.toPO(userDTO);
        this.save(userPO);
        return userConverter.toDTO(userPO);
    }
}
```

**方案二：删除时修改唯一字段值**

```sql
-- 表结构设计
CREATE TABLE t_user_info
(
    id          BIGINT AUTO_INCREMENT COMMENT '主键ID',
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    email       VARCHAR(100) NOT NULL COMMENT '邮箱',
    is_deleted  TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，null-已删除',
    delete_time DATETIME COMMENT '删除时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_username (username),
    UNIQUE KEY uk_user_email (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户信息表';
```

```java
// Service层删除逻辑
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {

    @Override
    public void deleteUser(Long userId) {
        UserPO user = this.getById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }

        // 修改唯一字段值，避免冲突
        String timestamp = String.valueOf(System.currentTimeMillis());
        user.setUsername(user.getUsername() + "_deleted_" + timestamp);
        user.setEmail(user.getEmail() + "_deleted_" + timestamp);
        user.setIsDeleted(null);
        user.setDeleteTime(LocalDateTime.now());

        this.updateById(user);
    }

    @Override
    public void restoreUser(Long userId) {
        UserPO user = this.getById(userId);
        if (user == null || user.getIsDeleted() == null) {
            throw new BizException("用户不存在或未被删除");
        }

        // 恢复原始字段值
        String originalUsername = user.getUsername().replaceAll("_deleted_\\d+$", "");
        String originalEmail = user.getEmail().replaceAll("_deleted_\\d+$", "");

        // 检查原始值是否已被占用
        if (this.lambdaQuery()
                .eq(UserPO::getUsername, originalUsername)
                .eq(UserPO::getIsDeleted, 0)
                .exists()) {
            throw new BizException("用户名已被占用，无法恢复");
        }

        user.setUsername(originalUsername);
        user.setEmail(originalEmail);
        user.setIsDeleted(0);
        user.setDeleteTime(null);

        this.updateById(user);
    }
}
```

**方案二：使用函数索引（MySQL 8.0+）**

```sql
-- 创建函数索引，只对未删除记录建立唯一约束
CREATE UNIQUE INDEX uk_user_username_active
    ON t_user_info (username) WHERE is_deleted = 0;

CREATE UNIQUE INDEX uk_user_email_active
    ON t_user_info (email) WHERE is_deleted = 0;
```

#### 16.5.3 最佳实践建议

**推荐使用联合唯一索引方案的原因：**

1. **数据库层面保证**：通过数据库约束确保数据一致性
2. **性能最优**：无需修改字段值，查询和删除性能最佳
3. **代码简洁**：直接使用MyBatis-Plus逻辑删除，无需额外处理
4. **语义清晰**：未删除记录唯一，已删除记录可重复
5. **维护简单**：不需要复杂的字段值修改逻辑

**核心原理：**

- `NULL` 值在MySQL中不参与唯一性检查
- `(username='john', is_deleted=0)` 只能存在一条记录
- `(username='john', is_deleted=null)` 可以存在多条记录
- 完美解决了逻辑删除与唯一约束的冲突问题

**注意事项：**

```java
// 查询时MyBatis-Plus会自动添加逻辑删除条件
List<UserPO> users = userMapper.selectList(
                new LambdaQueryWrapper<UserPO>()
                        .eq(UserPO::getUsername, "john")
                // 自动添加: AND is_deleted = 0
        );

// 如需查询包含已删除的记录，需要禁用逻辑删除
@Select("SELECT * FROM t_user_info WHERE username = #{username}")
List<UserPO> selectAllByUsername(@Param("username") String username);
```

**实现要点：**

```java
// 自定义删除方法，覆盖MyBatis-Plus默认行为
@Override
public boolean removeById(Serializable id) {
    UserPO user = this.getById(id);
    if (user == null) {
        return false;
    }

    // 自定义删除逻辑
    String timestamp = String.valueOf(System.currentTimeMillis());
    return this.lambdaUpdate()
            .eq(UserPO::getId, id)
            .set(UserPO::getUsername, user.getUsername() + "_deleted_" + timestamp)
            .set(UserPO::getEmail, user.getEmail() + "_deleted_" + timestamp)
            .set(UserPO::getIsDeleted, null)
            .set(UserPO::getDeleteTime, LocalDateTime.now())
            .update();
}

// 批量删除
@Override
public boolean removeByIds(Collection<? extends Serializable> idList) {
    return idList.stream()
            .allMatch(this::removeById);
}
```

#### 16.5.4 数据清理策略

**定期清理已删除数据：**

```java

@Component
public class DeletedDataCleanupTask {

    @Autowired
    private UserMapper userMapper;

    // 每月清理一次超过6个月的已删除数据
    @Scheduled(cron = "0 0 2 1 * ?")
    public void cleanupDeletedData() {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);

        // 物理删除超过6个月的已删除数据
        userMapper.delete(new LambdaQueryWrapper<UserPO>()
                .isNull(UserPO::getIsDeleted)
                .lt(UserPO::getDeleteTime, sixMonthsAgo));

        log.info("清理了超过6个月的已删除用户数据");
    }
}
```

**归档策略：**

```java
// 将已删除数据迁移到归档表
@Service
public class DataArchiveService {

    public void archiveDeletedUsers() {
        // 1. 查询需要归档的数据
        List<UserPO> deletedUsers = userMapper.selectList(
                new LambdaQueryWrapper<UserPO>()
                        .isNull(UserPO::getIsDeleted)
                        .lt(UserPO::getDeleteTime, LocalDateTime.now().minusMonths(3))
        );

        // 2. 插入到归档表
        deletedUsers.forEach(user -> {
            UserArchivePO archive = new UserArchivePO();
            BeanUtils.copyProperties(user, archive);
            userArchiveMapper.insert(archive);
        });

        // 3. 物理删除原表数据
        List<Long> userIds = deletedUsers.stream()
                .map(UserPO::getId)
                .collect(Collectors.toList());
        userMapper.deleteBatchIds(userIds);
    }
}
```

---

## 十七、API设计与文档规范

### 17.1 RESTful API设计原则 - Admin/Client分离架构

#### 17.1.1 URL设计规范 - 多端分离

**基础路径规范：**

```
# 管理端API路径
/api/admin/v{版本号}/{资源名}

# 客户端API路径  
/api/client/v{版本号}/{资源名}

# 其他端API路径（可选）
/api/mini/v{版本号}/{资源名}     # 小程序端
/api/app/v{版本号}/{资源名}      # APP端
/api/public/v{版本号}/{资源名}   # 公共接口
```

**具体API设计示例：**

**管理端API（功能完整，权限严格）：**

```
# 用户管理
GET    /api/admin/v1/users                    # 获取用户列表（包含敏感信息）
GET    /api/admin/v1/users/{id}               # 获取用户详情（完整信息）
POST   /api/admin/v1/users                    # 创建用户（可设置任意角色）
PUT    /api/admin/v1/users/{id}               # 更新用户（全量更新）
DELETE /api/admin/v1/users/{id}               # 删除用户（管理员删除）
POST   /api/admin/v1/users/batch-import       # 批量导入用户
GET    /api/admin/v1/users/statistics         # 用户统计数据
PUT    /api/admin/v1/users/{id}/reset-password # 重置用户密码

# 订单管理
GET    /api/admin/v1/orders                   # 获取所有订单
GET    /api/admin/v1/orders/{id}              # 获取订单详情
PUT    /api/admin/v1/orders/{id}/status       # 修改订单状态
GET    /api/admin/v1/orders/statistics        # 订单统计报表

# 系统管理
GET    /api/admin/v1/system/config            # 系统配置
PUT    /api/admin/v1/system/config            # 更新系统配置
GET    /api/admin/v1/system/logs              # 系统日志
```

**客户端API（功能受限，安全过滤）：**

```
# 用户相关
POST   /api/client/v1/users/register          # 用户注册
GET    /api/client/v1/users/profile           # 获取个人信息
PUT    /api/client/v1/users/profile           # 更新个人信息
PUT    /api/client/v1/users/password          # 修改密码
DELETE /api/client/v1/users/account           # 注销账户
GET    /api/client/v1/users/{id}/public       # 获取用户公开信息

# 订单相关
GET    /api/client/v1/orders                  # 获取个人订单列表
GET    /api/client/v1/orders/{id}             # 获取订单详情（仅本人）
POST   /api/client/v1/orders                  # 创建订单
PUT    /api/client/v1/orders/{id}/cancel      # 取消订单（仅本人）

# 商品相关
GET    /api/client/v1/products                # 获取商品列表
GET    /api/client/v1/products/{id}           # 获取商品详情
```

**其他端API示例：**

```
# 小程序端（轻量化）
GET    /api/mini/v1/users/profile             # 获取个人信息（精简版）
POST   /api/mini/v1/orders                    # 创建订单（小程序专用）
GET    /api/mini/v1/products/recommend        # 推荐商品

# 公共接口（无需认证）
GET    /api/public/v1/captcha                 # 获取验证码
POST   /api/public/v1/sms/send                # 发送短信验证码
GET    /api/public/v1/regions                 # 获取地区信息
```

**查询参数规范：**

```
# 管理端查询（功能强大）
GET /api/admin/v1/users?page=1&size=20&sort=createTime,desc&status=all&includeDeleted=true&keyword=john

# 客户端查询（功能受限）
GET /api/client/v1/orders?page=1&size=10&status=active&startTime=2024-01-01

# 小程序端查询（轻量化）
GET /api/mini/v1/products?page=1&size=5&category=electronics
```

#### 17.1.2 HTTP状态码规范

| 状态码     | 含义                    | 使用场景       |
|---------|-----------------------|------------|
| **200** | OK                    | 请求成功       |
| **201** | Created               | 资源创建成功     |
| **204** | No Content            | 删除成功，无返回内容 |
| **400** | Bad Request           | 请求参数错误     |
| **401** | Unauthorized          | 未认证        |
| **403** | Forbidden             | 无权限        |
| **404** | Not Found             | 资源不存在      |
| **409** | Conflict              | 资源冲突       |
| **422** | Unprocessable Entity  | 参数验证失败     |
| **500** | Internal Server Error | 服务器内部错误    |

#### 17.1.3 请求响应格式规范

**统一响应格式：**

```json
{
  "code": "SUCCESS",
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "john",
    "email": "john@example.com"
  },
  "traceId": "abc123def456",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**分页响应格式：**

```json
{
  "code": "SUCCESS",
  "message": "查询成功",
  "data": {
    "records": [
      {
        "id": 1,
        "username": "john"
      }
    ],
    "total": 100,
    "current": 1,
    "size": 20,
    "pages": 5
  },
  "traceId": "abc123def456"
}
```

**错误响应格式：**

```json
{
  "code": "VALIDATION_ERROR",
  "message": "参数验证失败",
  "data": null,
  "errors": [
    {
      "field": "email",
      "message": "邮箱格式不正确"
    }
  ],
  "traceId": "abc123def456"
}
```

### 17.2 API版本控制策略

#### 17.2.1 版本控制方式

```java
// 1. URL路径版本控制（推荐）
@RequestMapping("/api/v1/users")
@RequestMapping("/api/v2/users")

// 2. 请求头版本控制
@RequestMapping(value = "/api/users", headers = "API-Version=1")

// 3. 参数版本控制
@RequestMapping(value = "/api/users", params = "version=1")
```

#### 17.2.2 版本兼容性策略

```java

@RestController
@RequestMapping("/api")
public class UserController {

    // V1版本接口
    @GetMapping("/v1/users/{id}")
    public ResponseEntity<ApiResult<UserV1VO>> getUserV1(@PathVariable Long id) {
        // V1版本实现
    }

    // V2版本接口（向后兼容）
    @GetMapping("/v2/users/{id}")
    public ResponseEntity<ApiResult<UserV2VO>> getUserV2(@PathVariable Long id) {
        // V2版本实现，包含更多字段
    }

    // 默认版本（指向最新版本）
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResult<UserV2VO>> getUser(@PathVariable Long id) {
        return getUserV2(id);
    }
}
```

### 17.3 Controller层实现规范 - Admin/Client分离

#### 17.3.1 管理端Controller实现

```java

@RestController
@RequestMapping("/api/admin/v1/users")
@RequiredArgsConstructor
@Tag(name = "管理端-用户管理", description = "管理后台用户相关接口")
@PreAuthorize("hasRole('ADMIN')")  // 类级别权限控制
public class AdminUserController {

    private final UserService userService;
    private final AdminUserConverter adminUserConverter;

    @Operation(summary = "创建用户", description = "管理员创建用户账户")
    @PostMapping
    @PreAuthorize("hasPermission('user', 'create')")
    public ResponseEntity<ApiResult<AdminUserVO>> createUser(
            @Valid @RequestBody AdminUserCreateForm form,
            Authentication authentication) {

        // 管理端可以设置用户角色和状态
        UserDTO dto = adminUserConverter.toDTO(form);
        dto.setCreateBy(getCurrentUserId(authentication));

        UserDTO result = userService.createUser(dto);
        AdminUserVO vo = adminUserConverter.toVO(result);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(vo));
    }

    @Operation(summary = "获取用户详情", description = "获取用户完整信息（包含敏感数据）")
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('user', 'read')")
    public ResponseEntity<ApiResult<AdminUserDetailVO>> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        // 管理端返回完整信息，包含敏感数据
        AdminUserDetailVO vo = adminUserConverter.toDetailVO(userDTO);
        return ResponseEntity.ok(ApiResult.success(vo));
    }

    @Operation(summary = "用户列表查询", description = "分页查询用户列表，支持多条件筛选")
    @GetMapping
    @PreAuthorize("hasPermission('user', 'read')")
    public ResponseEntity<ApiResult<Page<AdminUserVO>>> getUserPage(
            @Valid @ParameterObject AdminUserQueryQO qo) {

        // 管理端可以查询所有用户，包括已删除用户
        Page<UserDTO> page = userService.getAdminUserPage(qo);
        Page<AdminUserVO> voPage = page.convert(adminUserConverter::toVO);

        return ResponseEntity.ok(ApiResult.success(voPage));
    }

    @Operation(summary = "批量导入用户", description = "批量导入用户数据")
    @PostMapping("/batch-import")
    @PreAuthorize("hasPermission('user', 'import')")
    public ResponseEntity<ApiResult<BatchImportResultVO>> batchImportUsers(
            @Valid @RequestBody List<AdminUserImportForm> forms,
            Authentication authentication) {

        List<UserDTO> dtos = forms.stream()
                .map(adminUserConverter::toDTO)
                .peek(dto -> dto.setCreateBy(getCurrentUserId(authentication)))
                .collect(Collectors.toList());

        BatchImportResultDTO result = userService.batchImportUsers(dtos);
        return ResponseEntity.ok(ApiResult.success(
                adminUserConverter.toBatchImportResultVO(result)));
    }

    @Operation(summary = "强制删除用户", description = "管理员强制删除用户（逻辑删除）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('user', 'delete')")
    public ResponseEntity<ApiResult<Void>> deleteUser(
            @PathVariable Long id,
            Authentication authentication) {

        userService.deleteUserByAdmin(id, getCurrentUserId(authentication));
        return ResponseEntity.ok(ApiResult.success(null));
    }

    private Long getCurrentUserId(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getId();
    }
}
```

#### 17.3.2 客户端Controller实现

```java

@RestController
@RequestMapping("/api/client/v1/users")
@RequiredArgsConstructor
@Tag(name = "客户端-用户管理", description = "客户端用户相关接口")
public class ClientUserController {

    private final UserService userService;
    private final ClientUserConverter clientUserConverter;

    @Operation(summary = "用户注册", description = "用户自主注册账户")
    @PostMapping("/register")
    @Anonymous  // 允许匿名访问
    public ResponseEntity<ApiResult<ClientUserVO>> register(
            @Valid @RequestBody ClientUserRegisterForm form,
            HttpServletRequest request) {

        // 客户端注册，默认为普通用户角色
        UserDTO dto = clientUserConverter.toDTO(form);
        dto.setRole(UserRole.USER);  // 强制设置为普通用户
        dto.setRegisterIp(getClientIp(request));

        UserDTO result = userService.registerUser(dto);
        ClientUserVO vo = clientUserConverter.toVO(result);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(vo));
    }

    @Operation(summary = "获取个人信息", description = "获取当前登录用户信息")
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<ClientUserProfileVO>> getProfile(
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);
        UserDTO userDTO = userService.getUserById(currentUserId);

        // 客户端只返回非敏感信息
        ClientUserProfileVO vo = clientUserConverter.toProfileVO(userDTO);
        return ResponseEntity.ok(ApiResult.success(vo));
    }

    @Operation(summary = "更新个人信息", description = "用户更新自己的个人信息")
    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<ClientUserProfileVO>> updateProfile(
            @Valid @RequestBody ClientUserProfileForm form,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);
        UserDTO dto = clientUserConverter.toDTO(form);
        dto.setId(currentUserId);  // 确保只能更新自己的信息

        UserDTO result = userService.updateUserProfile(dto);
        ClientUserProfileVO vo = clientUserConverter.toProfileVO(result);

        return ResponseEntity.ok(ApiResult.success(vo));
    }

    @Operation(summary = "修改密码", description = "用户修改登录密码")
    @PutMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<Void>> changePassword(
            @Valid @RequestBody ClientPasswordChangeForm form,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);
        userService.changePassword(currentUserId, form.oldPassword(), form.newPassword());

        return ResponseEntity.ok(ApiResult.success(null));
    }

    @Operation(summary = "注销账户", description = "用户主动注销账户")
    @DeleteMapping("/account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<Void>> deleteAccount(
            @Valid @RequestBody ClientAccountDeleteForm form,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);
        // 客户端删除需要验证密码
        userService.deleteUserAccount(currentUserId, form.password());

        return ResponseEntity.ok(ApiResult.success(null));
    }

    private Long getCurrentUserId(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getId();
    }

    private String getClientIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
```

### 17.4 Swagger/OpenAPI文档规范 - 多端分组

#### 17.4.1 Knife4j配置 - Admin/Client分离

```java

@Configuration
@EnableKnife4j
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fast Web API")
                        .version("1.0.0")
                        .description("企业级SpringBoot应用API文档")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@company.com")
                                .url("https://company.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("开发环境"),
                        new Server().url("https://api.company.com").description("生产环境")
                ));
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("管理端API")
                .pathsToMatch("/api/admin/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info()
                            .title("管理端API")
                            .description("管理后台专用接口，需要管理员权限")
                            .version("1.0.0"));
                })
                .build();
    }

    @Bean
    public GroupedOpenApi clientApi() {
        return GroupedOpenApi.builder()
                .group("客户端API")
                .pathsToMatch("/api/client/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info()
                            .title("客户端API")
                            .description("用户端接口，面向普通用户")
                            .version("1.0.0"));
                })
                .build();
    }

    @Bean
    public GroupedOpenApi miniApi() {
        return GroupedOpenApi.builder()
                .group("小程序API")
                .pathsToMatch("/api/mini/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info()
                            .title("小程序API")
                            .description("微信小程序专用接口")
                            .version("1.0.0"));
                })
                .build();
    }

    @Bean
    public GroupedOpenApi appApi() {
        return GroupedOpenApi.builder()
                .group("APP端API")
                .pathsToMatch("/api/app/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info()
                            .title("APP端API")
                            .description("移动APP专用接口")
                            .version("1.0.0"));
                })
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("公共API")
                .pathsToMatch("/api/public/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.info(new Info()
                            .title("公共API")
                            .description("无需认证的公共接口")
                            .version("1.0.0"));
                })
                .build();
    }
}
```

#### 17.3.2 API文档注解规范

```java

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户管理", description = "用户相关的API接口")
public class UserController {

    @Operation(summary = "创建用户", description = "创建新的用户账户")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "用户创建成功",
                    content = @Content(schema = @Schema(implementation = UserVO.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "409", description = "用户名或邮箱已存在")
    })
    @PostMapping
    public ResponseEntity<ApiResult<UserVO>> createUser(
            @Valid @RequestBody
            @Parameter(description = "用户创建表单", required = true)
            UserCreateForm form) {
        // 实现逻辑
    }

    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<UserVO>> getUserById(
            @PathVariable
            @Parameter(description = "用户ID", example = "1")
            Long id) {
        // 实现逻辑
    }

    @Operation(summary = "用户列表查询", description = "分页查询用户列表，支持关键字搜索")
    @GetMapping
    public ResponseEntity<ApiResult<Page<UserVO>>> getUserPage(
            @Valid @ParameterObject UserQueryQO qo) {
        // 实现逻辑
    }
}
```

#### 17.3.3 数据模型文档规范

```java

@Schema(description = "用户创建表单")
public record UserCreateForm(
        @Schema(description = "用户名", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "用户名不能为空")
        String username,

        @Schema(description = "邮箱地址", example = "john@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        String email,

        @Schema(description = "密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "密码不能为空")
        @Length(min = 6, max = 20, message = "密码长度必须在6-20之间")
        String password
) {
}

@Schema(description = "用户视图对象")
@Data
public class UserVO {
    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "john_doe")
    private String username;

    @Schema(description = "邮箱地址", example = "john@example.com")
    private String email;

    @Schema(description = "用户状态", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "LOCKED"})
    private String status;

    @Schema(description = "创建时间", example = "2024-01-15T10:30:00Z")
    private LocalDateTime createTime;
}
```

### 17.5 API安全规范 - 分层权限控制

#### 17.5.1 管理端权限控制（严格模式）

```java

@RestController
@RequestMapping("/api/admin/v1/users")
@PreAuthorize("hasRole('ADMIN')")  // 类级别：必须是管理员
public class AdminUserController {

    @GetMapping
    @PreAuthorize("hasPermission('user', 'read')")
    public ResponseEntity<ApiResult<Page<AdminUserVO>>> getUserPage() {
        // 管理端：需要用户读取权限
    }

    @PostMapping
    @PreAuthorize("hasPermission('user', 'create')")
    public ResponseEntity<ApiResult<AdminUserVO>> createUser() {
        // 管理端：需要用户创建权限
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('user', 'delete')")
    public ResponseEntity<ApiResult<Void>> deleteUser(@PathVariable Long id) {
        // 管理端：需要用户删除权限
    }

    @PostMapping("/batch-import")
    @PreAuthorize("hasRole('SUPER_ADMIN')")  // 超级管理员才能批量导入
    public ResponseEntity<ApiResult<BatchImportResultVO>> batchImportUsers() {
        // 高危操作：需要超级管理员权限
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasPermission('user', 'statistics')")
    public ResponseEntity<ApiResult<UserStatisticsVO>> getUserStatistics() {
        // 统计数据：需要统计权限
    }
}
```

#### 17.5.2 客户端权限控制（宽松模式）

```java

@RestController
@RequestMapping("/api/client/v1/users")
public class ClientUserController {

    @PostMapping("/register")
    @Anonymous  // 允许匿名访问
    public ResponseEntity<ApiResult<ClientUserVO>> register() {
        // 用户注册：无需认证
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")  // 只需要登录
    public ResponseEntity<ApiResult<ClientUserProfileVO>> getProfile() {
        // 获取个人信息：只需要登录
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<ClientUserProfileVO>> updateProfile() {
        // 更新个人信息：只需要登录
    }

    @GetMapping("/{id}/public")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<ClientUserPublicVO>> getUserPublicInfo(@PathVariable Long id) {
        // 查看他人公开信息：只需要登录
    }

    @DeleteMapping("/account")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<Void>> deleteAccount() {
        // 注销账户：只需要登录（业务层验证密码）
    }
}
```

#### 17.5.3 数据权限控制策略

```java

@Service
public class UserServiceImpl implements UserService {

    // 管理端：可以访问所有用户数据
    @Override
    public Page<UserDTO> getAdminUserPage(AdminUserQueryQO qo) {
        // 管理员可以查询所有用户，包括已删除用户
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<UserPO>()
                .like(StringUtils.hasText(qo.getKeyword()), UserPO::getUsername, qo.getKeyword())
                .eq(qo.getStatus() != null, UserPO::getStatus, qo.getStatus());

        // 如果需要查询已删除用户
        if (Boolean.TRUE.equals(qo.getIncludeDeleted())) {
            // 使用原生查询绕过逻辑删除
            return this.baseMapper.selectAdminUserPage(new Page<>(qo.getCurrent(), qo.getSize()), qo)
                    .convert(userConverter::toDTO);
        }

        return this.page(new Page<>(qo.getCurrent(), qo.getSize()), wrapper)
                .convert(userConverter::toDTO);
    }

    // 客户端：只能访问自己的数据或公开数据
    @Override
    public UserDTO updateUserProfile(UserDTO userDTO) {
        // 获取当前登录用户ID
        Long currentUserId = SecurityUtils.getCurrentUserId();

        // 确保只能更新自己的信息
        if (!Objects.equals(userDTO.getId(), currentUserId)) {
            throw new BizException("PERMISSION_DENIED", "只能修改自己的信息");
        }

        // 客户端只能更新允许的字段
        UserPO updateUser = new UserPO();
        updateUser.setId(userDTO.getId());
        updateUser.setNickname(userDTO.getNickname());
        updateUser.setAvatar(userDTO.getAvatar());
        updateUser.setPhone(userDTO.getPhone());
        // 不允许修改角色、状态等敏感字段

        this.updateById(updateUser);
        return getUserById(userDTO.getId());
    }
}
```

#### 17.5.4 接口限流和防护

```java

@RestController
@RequestMapping("/api/client/v1/users")
public class ClientUserController {

    @PostMapping("/register")
    @RateLimiter(key = "register", rate = 5, rateInterval = 60) // 每分钟最多5次注册
    @Anonymous
    public ResponseEntity<ApiResult<ClientUserVO>> register(
            @Valid @RequestBody ClientUserRegisterForm form,
            HttpServletRequest request) {

        // IP限流检查
        String clientIp = getClientIp(request);
        if (!rateLimitService.tryAcquire("register:ip:" + clientIp, 3, 3600)) {
            throw new BizException("RATE_LIMIT", "注册过于频繁，请稍后再试");
        }

        // 邮箱限流检查
        if (!rateLimitService.tryAcquire("register:email:" + form.email(), 1, 86400)) {
            throw new BizException("EMAIL_LIMIT", "该邮箱今日注册次数已达上限");
        }

        return userService.registerUser(form);
    }

    @PutMapping("/password")
    @RateLimiter(key = "changePassword", rate = 3, rateInterval = 300) // 5分钟最多3次
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResult<Void>> changePassword(
            @Valid @RequestBody ClientPasswordChangeForm form,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);

        // 密码修改限流
        if (!rateLimitService.tryAcquire("password:change:" + currentUserId, 3, 300)) {
            throw new BizException("RATE_LIMIT", "密码修改过于频繁");
        }

        userService.changePassword(currentUserId, form.oldPassword(), form.newPassword());
        return ResponseEntity.ok(ApiResult.success(null));
    }
}
```

#### 17.5.5 敏感数据过滤策略

```java
// 管理端转换器 - 包含敏感信息
@Component
public class AdminUserConverter {

    public AdminUserDetailVO toDetailVO(UserDTO userDTO) {
        return AdminUserDetailVO.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())           // 敏感信息：邮箱
                .phone(userDTO.getPhone())           // 敏感信息：手机号
                .realName(userDTO.getRealName())     // 敏感信息：真实姓名
                .idCard(userDTO.getIdCard())         // 敏感信息：身份证
                .registerIp(userDTO.getRegisterIp()) // 敏感信息：注册IP
                .lastLoginIp(userDTO.getLastLoginIp()) // 敏感信息：最后登录IP
                .status(userDTO.getStatus())
                .role(userDTO.getRole())
                .createTime(userDTO.getCreateTime())
                .updateTime(userDTO.getUpdateTime())
                .build();
    }
}

// 客户端转换器 - 过滤敏感信息
@Component
public class ClientUserConverter {

    public ClientUserVO toVO(UserDTO userDTO) {
        return ClientUserVO.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .nickname(userDTO.getNickname())
                .avatar(userDTO.getAvatar())
                .gender(userDTO.getGender())
                .birthday(userDTO.getBirthday())
                // 不包含敏感信息：邮箱、手机号、真实姓名、身份证、IP等
                .createTime(userDTO.getCreateTime())
                .build();
    }

    public ClientUserPublicVO toPublicVO(UserDTO userDTO) {
        return ClientUserPublicVO.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .nickname(userDTO.getNickname())
                .avatar(userDTO.getAvatar())
                // 只包含公开信息，更少的字段
                .build();
    }
}
```

#### 17.5.6 API安全最佳实践

**1. 权限分层原则：**

```java
// 管理端：严格权限控制
@PreAuthorize("hasRole('ADMIN') and hasPermission('user', 'delete')")

// 客户端：相对宽松，但有业务限制
@PreAuthorize("isAuthenticated()")  // 在业务层检查是否只能操作自己的数据
```

**2. 数据过滤原则：**

```java
// 管理端：返回完整数据
AdminUserDetailVO adminVO = adminUserConverter.toDetailVO(userDTO);

// 客户端：过滤敏感数据
ClientUserVO clientVO = clientUserConverter.toVO(userDTO);
```

**3. 接口限流原则：**

```java
// 高频接口：严格限流
@RateLimiter(key = "register", rate = 5, rateInterval = 60)

// 敏感操作：更严格限流
@RateLimiter(key = "changePassword", rate = 3, rateInterval = 300)
```

**4. 参数验证原则：**

```java
// 管理端：可以设置更多参数
public ResponseEntity<ApiResult<AdminUserVO>> createUser(
        @Valid @RequestBody AdminUserCreateForm form) {
    // form中可以包含role、status等敏感字段
}

// 客户端：限制参数范围
public ResponseEntity<ApiResult<ClientUserVO>> register(
        @Valid @RequestBody ClientUserRegisterForm form) {
    // form中不包含role、status等敏感字段，在业务层强制设置
}
```

#### 17.4.2 参数验证与安全

```java

@RestController
@Validated
public class UserController {

    @PostMapping("/users")
    public ResponseEntity<ApiResult<UserVO>> createUser(
            @Valid @RequestBody UserCreateForm form,
            HttpServletRequest request) {

        // 1. 参数验证（通过@Valid自动验证）

        // 2. 业务规则验证
        if (userService.existsByUsername(form.username())) {
            throw new BizException("USER_EXISTS", "用户名已存在");
        }

        // 3. 安全检查
        String clientIp = getClientIp(request);
        if (isBlacklisted(clientIp)) {
            throw new BizException("ACCESS_DENIED", "访问被拒绝");
        }

        // 4. 限流检查
        if (!rateLimiter.tryAcquire()) {
            throw new BizException("RATE_LIMIT", "请求过于频繁");
        }

        return ResponseEntity.ok(ApiResult.success(userService.createUser(form)));
    }
}
```

---

## 十八、测试策略与规范

### 18.1 测试分层策略

#### 18.1.1 测试金字塔

```
    /\
   /  \     E2E Tests (端到端测试)
  /____\    Integration Tests (集成测试)  
 /______\   Unit Tests (单元测试)
```

**测试比例建议：**

- **单元测试**：70% - 快速、稳定、低成本
- **集成测试**：20% - 验证组件协作
- **端到端测试**：10% - 验证完整业务流程

#### 18.1.2 测试分类与职责

| 测试类型      | 测试范围   | 测试工具                              | 覆盖率要求    |
|-----------|--------|-----------------------------------|----------|
| **单元测试**  | 单个类/方法 | JUnit 5 + Mockito                 | ≥ 80%    |
| **集成测试**  | 多个组件协作 | Spring Boot Test + TestContainers | ≥ 60%    |
| **端到端测试** | 完整业务流程 | RestAssured + TestContainers      | 核心流程100% |
| **性能测试**  | 系统性能指标 | JMeter + Gatling                  | 关键接口     |

### 18.2 单元测试规范

#### 18.2.1 测试类结构规范

```java

@ExtendWith(MockitoExtension.class)
@DisplayName("用户服务单元测试")
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("创建用户测试")
    class CreateUserTest {

        @Test
        @DisplayName("应该成功创建用户")
        void shouldCreateUserSuccessfully() {
            // Given - 准备测试数据
            UserDTO userDTO = UserDTO.builder()
                    .username("testuser")
                    .email("test@example.com")
                    .build();

            UserPO userPO = UserPO.builder()
                    .id(1L)
                    .username("testuser")
                    .email("test@example.com")
                    .build();

            when(userConverter.toPO(userDTO)).thenReturn(userPO);
            when(userMapper.insert(userPO)).thenReturn(1);
            when(userConverter.toDTO(userPO)).thenReturn(userDTO);

            // When - 执行测试方法
            UserDTO result = userService.createUser(userDTO);

            // Then - 验证结果
            assertThat(result).isNotNull();
            assertThat(result.getUsername()).isEqualTo("testuser");
            assertThat(result.getEmail()).isEqualTo("test@example.com");

            // 验证交互
            verify(userMapper).insert(userPO);
            verify(userConverter).toPO(userDTO);
            verify(userConverter).toDTO(userPO);
        }

        @Test
        @DisplayName("当用户名已存在时应该抛出异常")
        void shouldThrowExceptionWhenUsernameExists() {
            // Given
            UserDTO userDTO = UserDTO.builder()
                    .username("existinguser")
                    .build();

            when(userMapper.selectOne(any())).thenReturn(new UserPO());

            // When & Then
            assertThatThrownBy(() -> userService.createUser(userDTO))
                    .isInstanceOf(BizException.class)
                    .hasMessage("用户名已存在");
        }
    }

    @Nested
    @DisplayName("查询用户测试")
    class GetUserTest {

        @Test
        @DisplayName("应该成功获取用户")
        void shouldGetUserSuccessfully() {
            // 测试实现
        }

        @Test
        @DisplayName("当用户不存在时应该抛出异常")
        void shouldThrowExceptionWhenUserNotFound() {
            // 测试实现
        }
    }
}
```

#### 18.2.2 测试数据构建器模式

```java
public class UserTestDataBuilder {

    public static UserDTO.UserDTOBuilder aUserDTO() {
        return UserDTO.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .status(UserStatus.ACTIVE)
                .createTime(LocalDateTime.now());
    }

    public static UserPO.UserPOBuilder aUserPO() {
        return UserPO.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .status(1)
                .isDeleted(0)
                .createTime(LocalDateTime.now());
    }

    public static UserCreateForm aUserCreateForm() {
        return new UserCreateForm(
                "testuser",
                "test@example.com",
                "password123"
        );
    }
}

// 使用示例
@Test
void shouldCreateUser() {
    UserDTO userDTO = aUserDTO()
            .username("customuser")
            .email("custom@example.com")
            .build();

    // 测试逻辑
}
```

### 18.3 集成测试规范

#### 18.3.1 数据库集成测试

```java

@SpringBootTest
@Testcontainers
@Transactional
@Rollback
@DisplayName("用户服务集成测试")
class UserServiceIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("sql/init-test-data.sql");

    @Container
    static RedisContainer redis = new RedisContainer("redis:7-alpine");

    @Autowired
    private UserService userService;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("应该成功创建用户并保存到数据库")
    void shouldCreateUserAndSaveToDatabase() {
        // Given
        UserDTO userDTO = aUserDTO()
                .id(null) // 新建用户ID为空
                .build();

        // When
        UserDTO result = userService.createUser(userDTO);
        entityManager.flush(); // 强制刷新到数据库

        // Then
        assertThat(result.getId()).isNotNull();

        // 验证数据库中的数据
        UserPO savedUser = entityManager.find(UserPO.class, result.getId());
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(userDTO.getUsername());
    }

    @Test
    @DisplayName("应该支持事务回滚")
    void shouldSupportTransactionRollback() {
        // Given
        UserDTO userDTO = aUserDTO().build();

        // When & Then
        assertThatThrownBy(() -> {
            userService.createUser(userDTO);
            throw new RuntimeException("模拟异常");
        }).isInstanceOf(RuntimeException.class);

        // 验证事务回滚
        List<UserPO> users = entityManager.getEntityManager()
                .createQuery("SELECT u FROM UserPO u WHERE u.username = :username", UserPO.class)
                .setParameter("username", userDTO.getUsername())
                .getResultList();
        assertThat(users).isEmpty();
    }
}
```

#### 18.3.2 Web层集成测试

```java

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DisplayName("用户控制器集成测试")
class UserControllerIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("应该成功创建用户")
    void shouldCreateUserSuccessfully() {
        // Given
        UserCreateForm form = new UserCreateForm(
                "testuser",
                "test@example.com",
                "password123"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserCreateForm> request = new HttpEntity<>(form, headers);

        // When
        ResponseEntity<ApiResult<UserVO>> response = restTemplate.postForEntity(
                "/api/v1/users",
                request,
                new ParameterizedTypeReference<ApiResult<UserVO>>() {
                }
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("SUCCESS");
        assertThat(response.getBody().getData().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("当参数验证失败时应该返回400错误")
    void shouldReturn400WhenValidationFails() {
        // Given
        UserCreateForm invalidForm = new UserCreateForm(
                "", // 空用户名
                "invalid-email", // 无效邮箱
                "123" // 密码太短
        );

        HttpEntity<UserCreateForm> request = new HttpEntity<>(invalidForm);

        // When
        ResponseEntity<ApiResult<Object>> response = restTemplate.postForEntity(
                "/api/v1/users",
                request,
                new ParameterizedTypeReference<ApiResult<Object>>() {
                }
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getCode()).isEqualTo("VALIDATION_ERROR");
    }
}
```

### 18.4 测试覆盖率要求

#### 18.4.1 覆盖率指标

```xml
<!-- Maven Jacoco 插件配置 -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>BUNDLE</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.80</minimum>
                            </limit>
                            <limit>
                                <counter>BRANCH</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.70</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

#### 18.4.2 覆盖率分层要求

| 层级              | 行覆盖率  | 分支覆盖率 | 说明            |
|-----------------|-------|-------|---------------|
| **Service层**    | ≥ 90% | ≥ 85% | 核心业务逻辑，要求最高   |
| **Controller层** | ≥ 80% | ≥ 70% | 接口层，重点测试参数验证  |
| **Mapper层**     | ≥ 70% | ≥ 60% | 数据访问层，重点测试SQL |
| **Util工具类**     | ≥ 95% | ≥ 90% | 工具类，逻辑相对简单    |
| **整体项目**        | ≥ 80% | ≥ 70% | 项目整体覆盖率要求     |

### 18.5 自动化测试实施指南

#### 18.5.1 CI/CD集成

```yaml
# GitHub Actions 示例
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest

    services:
      mysql:
        image: mysql:8.0
        env:
          MYSQL_ROOT_PASSWORD: root
          MYSQL_DATABASE: test_db
        ports:
          - 3306:3306
        options: --health-cmd="mysqladmin ping" --health-interval=10s --health-timeout=5s --health-retries=3

      redis:
        image: redis:7-alpine
        ports:
          - 6379:6379
        options: --health-cmd="redis-cli ping" --health-interval=10s --health-timeout=5s --health-retries=3

    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Cache Maven dependencies
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}

      - name: Run tests
        run: mvn clean test

      - name: Generate test report
        run: mvn jacoco:report

      - name: Upload coverage to Codecov
        uses: codecov/codecov-action@v3
        with:
          file: ./target/site/jacoco/jacoco.xml

      - name: Quality Gate
        run: mvn sonar:sonar -Dsonar.projectKey=fast-web
```

#### 18.5.2 测试数据管理

```java

@TestConfiguration
public class TestDataConfig {

    @Bean
    @Primary
    public Clock testClock() {
        return Clock.fixed(
                Instant.parse("2024-01-15T10:30:00Z"),
                ZoneOffset.UTC
        );
    }

    @EventListener
    public void handleTestExecutionStarted(TestExecutionStartedEvent event) {
        // 测试开始前的数据准备
        setupTestData();
    }

    @EventListener
    public void handleTestExecutionFinished(TestExecutionFinishedEvent event) {
        // 测试结束后的数据清理
        cleanupTestData();
    }

    private void setupTestData() {
        // 初始化测试数据
    }

    private void cleanupTestData() {
        // 清理测试数据
    }
}
```

#### 18.5.3 测试环境隔离

```java

@TestProfile("test")
@Configuration
public class TestEnvironmentConfig {

    @Bean
    @Primary
    public RedisTemplate<String, Object> testRedisTemplate() {
        // 测试环境Redis配置
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public TestContainers testContainers() {
        return TestContainers.builder()
                .mysql("mysql:8.0")
                .redis("redis:7-alpine")
                .build();
    }
}
```

### 18.6 性能测试规范

#### 18.6.1 JMeter测试脚本

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2">
    <hashTree>
        <TestPlan guiclass="TestPlanGui" testclass="TestPlan" testname="用户API性能测试">
            <elementProp name="TestPlan.arguments" elementType="Arguments" guiclass="ArgumentsPanel">
                <collectionProp name="Arguments.arguments"/>
            </elementProp>
            <stringProp name="TestPlan.user_define_classpath"></stringProp>
            <boolProp name="TestPlan.serialize_threadgroups">false</boolProp>
        </TestPlan>

        <hashTree>
            <ThreadGroup guiclass="ThreadGroupGui" testclass="ThreadGroup" testname="用户创建测试">
                <stringProp name="ThreadGroup.on_sample_error">continue</stringProp>
                <elementProp name="ThreadGroup.main_controller" elementType="LoopController">
                    <boolProp name="LoopController.continue_forever">false</boolProp>
                    <stringProp name="LoopController.loops">100</stringProp>
                </elementProp>
                <stringProp name="ThreadGroup.num_threads">10</stringProp>
                <stringProp name="ThreadGroup.ramp_time">60</stringProp>
            </ThreadGroup>

            <hashTree>
                <HTTPSamplerProxy guiclass="HttpTestSampleGui" testclass="HTTPSamplerProxy" testname="创建用户请求">
                    <elementProp name="HTTPsampler.Arguments" elementType="Arguments">
                        <collectionProp name="Arguments.arguments">
                            <elementProp name="" elementType="HTTPArgument">
                                <boolProp name="HTTPArgument.always_encode">false</boolProp>
                                <stringProp name="Argument.value">
                                    {"username":"test_${__Random(1,10000)}","email":"test${__Random(1,10000)}@example.com","password":"password123"}
                                </stringProp>
                                <stringProp name="Argument.metadata">=</stringProp>
                            </elementProp>
                        </collectionProp>
                    </elementProp>
                    <stringProp name="HTTPSampler.domain">localhost</stringProp>
                    <stringProp name="HTTPSampler.port">8080</stringProp>
                    <stringProp name="HTTPSampler.path">/api/v1/users</stringProp>
                    <stringProp name="HTTPSampler.method">POST</stringProp>
                </HTTPSamplerProxy>
            </hashTree>
        </hashTree>
    </hashTree>
</jmeterTestPlan>
```

#### 18.6.2 性能测试指标

```java

@Component
public class PerformanceTestMetrics {

    private final MeterRegistry meterRegistry;

    public PerformanceTestMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @EventListener
    public void handlePerformanceTest(PerformanceTestEvent event) {
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            // 执行性能测试
            executePerformanceTest(event);
        } finally {
            sample.stop(Timer.builder("performance.test")
                    .tag("test.name", event.getTestName())
                    .register(meterRegistry));
        }
    }

    private void executePerformanceTest(PerformanceTestEvent event) {
        // 性能测试逻辑

        // 记录关键指标
        meterRegistry.counter("performance.test.requests").increment();
        meterRegistry.gauge("performance.test.response.time", event.getResponseTime());
        meterRegistry.gauge("performance.test.throughput", event.getThroughput());
    }
}
```

---

## 十九、总结

本规范基于 Spring Boot 3.2.x + JDK 17 + MyBatis-Plus 3.5.7+ 技术栈，建立了完整的企业级开发标准。核心要点包括：

### 19.1 架构设计

- **三层模块架构**：core（基础）/biz（业务）/app（启动）
- **技术分层**：Controller/Service/Mapper 单向依赖
- **六类对象模型**：form/dto/vo/qo/po/ro 职责明确

### 19.2 数据库设计

- **命名规范**：统一的库表字段命名约定
- **表结构设计**：标准化的表结构和字段类型
- **索引优化**：合理的索引设计和使用规范
- **SQL规范**：高效的SQL编写和优化指南

### 19.3 API设计

- **RESTful规范**：标准的URL设计和HTTP状态码使用
- **版本控制**：灵活的API版本管理策略
- **文档规范**：完整的Swagger/OpenAPI文档标准
- **安全防护**：全面的API安全和参数验证机制

### 19.4 测试策略

- **分层测试**：单元测试、集成测试、端到端测试
- **覆盖率要求**：明确的测试覆盖率指标和要求
- **自动化测试**：CI/CD集成和测试环境管理
- **性能测试**：系统性能测试和监控指标

### 19.5 质量保证

- **代码规范**：命名、格式、注释统一标准
- **安全防护**：输入校验、SQL注入防护、权限控制
- **性能优化**：连接池、缓存、异步处理、监控告警
- **开发流程**：Git规范、配置管理、部署运维

遵循本规范可以确保项目的**可维护性**、**可扩展性**、**安全性**和**性能**，为企业级应用开发提供坚实的技术基础。

---

*本规范将随着技术发展和项目实践持续更新完善。*