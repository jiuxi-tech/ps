# ps-bmp项目DDD架构目录结构优化分析

## 1. 现状分析

### 1.1 当前目录结构概览

```
com.jiuxi/
├── Application.java           # 应用启动类
├── admin/                      # 传统管理模块（需重构）
│   ├── autoconfig/            # 自动配置
│   ├── bean/                  # 数据对象
│   ├── constant/              # 常量定义
│   ├── core/                  # 核心业务逻辑
│   │   ├── controller/        # 控制器层
│   │   ├── service/           # 服务层
│   │   ├── mapper/            # 数据访问层
│   │   ├── bean/              # 实体对象
│   │   └── ...
│   └── domain/                # 领域配置（新增）
├── app/               # 应用层（已创建框架）
│   ├── assembler/             # 数据转换器
│   ├── command/               # 命令对象
│   ├── dto/                   # 数据传输对象
│   ├── query/                 # 查询对象
│   └── service/               # 应用服务
├── common/                    # 通用组件（需迁移到shared）
│   ├── bean/                  # 通用数据对象
│   ├── config/                # 通用配置
│   ├── constant/              # 通用常量
│   ├── exception/             # 异常处理
│   └── util/                  # 工具类
├── core/                      # 核心配置（需重构）
├── module/                    # 业务模块（DDD架构）
│   ├── auth/                  # 认证授权领域
│   ├── org/                   # 组织架构领域
│   ├── role/                  # 角色权限领域
│   ├── sys/                   # 系统管理领域
│   └── user/                  # 用户管理领域
├── shared/                    # 共享内核（已优化）
│   ├── common/                # 通用组件
│   ├── config/                # 配置管理
│   ├── infrastructure/        # 基础设施
│   └── security/              # 安全组件
├── monitor/                   # 监控模块（需重构）
├── mybatis/                   # MyBatis配置（需迁移）
├── platform/                  # 平台功能（需重构）
└── security/                  # 安全模块（需迁移）
```

### 1.2 存在的问题

#### 1.2.1 架构层面问题
1. **职责混乱**：admin、core、platform等模块职责重叠
2. **依赖复杂**：传统分层架构导致循环依赖
3. **技术导向**：以技术组件划分而非业务领域
4. **耦合度高**：业务逻辑与技术实现紧耦合

#### 1.2.2 包结构问题
1. **命名不一致**：interfaces vs intf，不同模块命名规范不统一
2. **层次不清**：application层框架已建立但内容为空
3. **重复定义**：common和shared功能重叠
4. **分散管理**：配置文件分散在多个包中

#### 1.2.3 DDD实施问题
1. **领域边界模糊**：admin包中混合了多个领域的业务逻辑
2. **聚合设计不完整**：缺少明确的聚合根和值对象设计
3. **事件驱动不彻底**：事件机制仅在部分模块中实现
4. **CQRS分离不清晰**：命令和查询混合在同一服务中

## 2. 优化目标架构

### 2.1 设计原则

- **六边形架构**：应用核心与外部依赖解耦
- **DDD分层**：领域驱动的清洁架构
- **CQRS分离**：命令查询职责分离
- **事件驱动**：通过领域事件实现模块解耦
- **依赖倒置**：高层模块不依赖低层模块

### 2.2 优化后的完整目录结构

```
com.jiuxi/
├── Bootstrap.java                    # 应用启动类（重命名）
│
├── application/                      # 应用层（Application Layer）
│   ├── command/                      # 命令处理
│   │   ├── handler/                  # 命令处理器
│   │   │   ├── auth/                 # 认证命令处理器
│   │   │   ├── org/                  # 组织命令处理器
│   │   │   ├── role/                 # 角色命令处理器
│   │   │   ├── sys/                  # 系统命令处理器
│   │   │   └── user/                 # 用户命令处理器
│   │   └── dto/                      # 命令DTO
│   │       ├── auth/
│   │       ├── org/
│   │       ├── role/
│   │       ├── sys/
│   │       └── user/
│   ├── query/                        # 查询处理
│   │   ├── handler/                  # 查询处理器
│   │   │   ├── auth/
│   │   │   ├── org/
│   │   │   ├── role/
│   │   │   ├── sys/
│   │   │   └── user/
│   │   ├── dto/                      # 查询DTO
│   │   │   ├── auth/
│   │   │   ├── org/
│   │   │   ├── role/
│   │   │   ├── sys/
│   │   │   └── user/
│   │   └── projection/               # 查询投影
│   │       ├── auth/
│   │       ├── org/
│   │       ├── role/
│   │       ├── sys/
│   │       └── user/
│   ├── service/                      # 应用服务
│   │   ├── auth/                     # 认证应用服务
│   │   ├── org/                      # 组织应用服务
│   │   ├── role/                     # 角色应用服务
│   │   ├── sys/                      # 系统应用服务
│   │   └── user/                     # 用户应用服务
│   ├── assembler/                    # 数据转换器
│   │   ├── auth/
│   │   ├── org/
│   │   ├── role/
│   │   ├── sys/
│   │   └── user/
│   ├── orchestrator/                 # 业务编排器
│   │   ├── UserRegistrationOrchestrator.java
│   │   ├── OrgStructureOrchestrator.java
│   │   └── RoleAssignmentOrchestrator.java
│   └── config/                       # 应用层配置
│       ├── ApplicationConfig.java
│       ├── CommandConfig.java
│       ├── QueryConfig.java
│       └── EventConfig.java
│
├── domain/                           # 领域层（Domain Layer）
│   ├── auth/                         # 认证授权领域
│   │   ├── aggregate/                # 聚合根
│   │   │   ├── AuthSession.java
│   │   │   ├── AuthToken.java
│   │   │   └── AuthClient.java
│   │   ├── entity/                   # 实体
│   │   │   ├── LoginRecord.java
│   │   │   └── PermissionGrant.java
│   │   ├── valueobject/              # 值对象
│   │   │   ├── TokenValue.java
│   │   │   ├── SessionId.java
│   │   │   └── Credentials.java
│   │   ├── service/                  # 领域服务
│   │   │   ├── AuthenticationService.java
│   │   │   ├── AuthorizationService.java
│   │   │   └── TokenService.java
│   │   ├── repository/               # 仓储接口
│   │   │   ├── AuthSessionRepository.java
│   │   │   ├── AuthTokenRepository.java
│   │   │   └── AuthClientRepository.java
│   │   ├── event/                    # 领域事件
│   │   │   ├── UserLoginEvent.java
│   │   │   ├── UserLogoutEvent.java
│   │   │   ├── TokenExpiredEvent.java
│   │   │   └── PermissionChangedEvent.java
│   │   ├── factory/                  # 领域工厂
│   │   │   ├── AuthSessionFactory.java
│   │   │   └── AuthTokenFactory.java
│   │   └── policy/                   # 领域策略
│   │       ├── PasswordPolicy.java
│   │       ├── SessionPolicy.java
│   │       └── TokenPolicy.java
│   ├── org/                          # 组织架构领域
│   │   ├── aggregate/
│   │   │   ├── Organization.java
│   │   │   ├── Department.java
│   │   │   └── Position.java
│   │   ├── entity/
│   │   │   ├── OrgUnit.java
│   │   │   └── OrgRelation.java
│   │   ├── valueobject/
│   │   │   ├── OrgCode.java
│   │   │   ├── OrgLevel.java
│   │   │   └── OrgPath.java
│   │   ├── service/
│   │   │   ├── OrganizationService.java
│   │   │   ├── DepartmentService.java
│   │   │   └── OrgStructureService.java
│   │   ├── repository/
│   │   │   ├── OrganizationRepository.java
│   │   │   ├── DepartmentRepository.java
│   │   │   └── PositionRepository.java
│   │   ├── event/
│   │   │   ├── OrgCreatedEvent.java
│   │   │   ├── OrgUpdatedEvent.java
│   │   │   ├── DeptCreatedEvent.java
│   │   │   └── DeptDeletedEvent.java
│   │   ├── factory/
│   │   │   ├── OrganizationFactory.java
│   │   │   └── DepartmentFactory.java
│   │   └── policy/
│   │       ├── OrgStructurePolicy.java
│   │       └── DeptHierarchyPolicy.java
│   ├── role/                         # 角色权限领域
│   │   ├── aggregate/
│   │   │   ├── Role.java
│   │   │   ├── Permission.java
│   │   │   └── RolePermission.java
│   │   ├── entity/
│   │   │   ├── RoleAssignment.java
│   │   │   └── PermissionGrant.java
│   │   ├── valueobject/
│   │   │   ├── RoleCode.java
│   │   │   ├── PermissionCode.java
│   │   │   └── RoleType.java
│   │   ├── service/
│   │   │   ├── RoleService.java
│   │   │   ├── PermissionService.java
│   │   │   └── RoleAssignmentService.java
│   │   ├── repository/
│   │   │   ├── RoleRepository.java
│   │   │   ├── PermissionRepository.java
│   │   │   └── RolePermissionRepository.java
│   │   ├── event/
│   │   │   ├── RoleCreatedEvent.java
│   │   │   ├── RoleAssignedEvent.java
│   │   │   ├── PermissionGrantedEvent.java
│   │   │   └── PermissionRevokedEvent.java
│   │   ├── factory/
│   │   │   ├── RoleFactory.java
│   │   │   └── PermissionFactory.java
│   │   └── policy/
│   │       ├── RoleAssignmentPolicy.java
│   │       └── PermissionPolicy.java
│   ├── sys/                          # 系统管理领域
│   │   ├── aggregate/
│   │   │   ├── SystemConfig.java
│   │   │   ├── SystemLog.java
│   │   │   └── Dictionary.java
│   │   ├── entity/
│   │   │   ├── ConfigItem.java
│   │   │   ├── LogEntry.java
│   │   │   └── DictItem.java
│   │   ├── valueobject/
│   │   │   ├── ConfigKey.java
│   │   │   ├── LogLevel.java
│   │   │   └── DictCode.java
│   │   ├── service/
│   │   │   ├── SystemConfigService.java
│   │   │   ├── SystemLogService.java
│   │   │   └── DictionaryService.java
│   │   ├── repository/
│   │   │   ├── SystemConfigRepository.java
│   │   │   ├── SystemLogRepository.java
│   │   │   └── DictionaryRepository.java
│   │   ├── event/
│   │   │   ├── ConfigChangedEvent.java
│   │   │   ├── SystemErrorEvent.java
│   │   │   └── DictUpdatedEvent.java
│   │   ├── factory/
│   │   │   ├── SystemConfigFactory.java
│   │   │   └── DictionaryFactory.java
│   │   └── policy/
│   │       ├── ConfigValidationPolicy.java
│   │       └── LogRetentionPolicy.java
│   └── user/                         # 用户管理领域
│       ├── aggregate/
│       │   ├── User.java
│       │   ├── UserProfile.java
│       │   └── UserAccount.java
│       ├── entity/
│       │   ├── PersonInfo.java
│       │   ├── ContactInfo.java
│       │   └── UserPreference.java
│       ├── valueobject/
│       │   ├── UserId.java
│       │   ├── Email.java
│       │   ├── PhoneNumber.java
│       │   └── UserStatus.java
│       ├── service/
│       │   ├── UserService.java
│       │   ├── UserProfileService.java
│       │   └── UserValidationService.java
│       ├── repository/
│       │   ├── UserRepository.java
│       │   ├── UserProfileRepository.java
│       │   └── UserAccountRepository.java
│       ├── event/
│       │   ├── UserRegisteredEvent.java
│       │   ├── UserActivatedEvent.java
│       │   ├── UserDeactivatedEvent.java
│       │   └── ProfileUpdatedEvent.java
│       ├── factory/
│       │   ├── UserFactory.java
│       │   └── UserAccountFactory.java
│       └── policy/
│           ├── UserRegistrationPolicy.java
│           ├── PasswordPolicy.java
│           └── UserValidationPolicy.java
│
├── infrastructure/                   # 基础设施层（Infrastructure Layer）
│   ├── persistence/                  # 数据持久化
│   │   ├── config/                   # 持久化配置
│   │   │   ├── DataSourceConfig.java
│   │   │   ├── MybatisPlusConfig.java
│   │   │   ├── TransactionConfig.java
│   │   │   └── DatabaseInitializer.java
│   │   ├── mapper/                   # MyBatis映射器
│   │   │   ├── auth/
│   │   │   │   ├── AuthSessionMapper.java
│   │   │   │   ├── AuthTokenMapper.java
│   │   │   │   └── AuthClientMapper.java
│   │   │   ├── org/
│   │   │   │   ├── OrganizationMapper.java
│   │   │   │   ├── DepartmentMapper.java
│   │   │   │   └── PositionMapper.java
│   │   │   ├── role/
│   │   │   │   ├── RoleMapper.java
│   │   │   │   ├── PermissionMapper.java
│   │   │   │   └── RolePermissionMapper.java
│   │   │   ├── sys/
│   │   │   │   ├── SystemConfigMapper.java
│   │   │   │   ├── SystemLogMapper.java
│   │   │   │   └── DictionaryMapper.java
│   │   │   └── user/
│   │   │       ├── UserMapper.java
│   │   │       ├── UserProfileMapper.java
│   │   │       └── UserAccountMapper.java
│   │   ├── entity/                   # 持久化实体（PO）
│   │   │   ├── auth/
│   │   │   ├── org/
│   │   │   ├── role/
│   │   │   ├── sys/
│   │   │   └── user/
│   │   ├── repository/               # 仓储实现
│   │   │   ├── auth/
│   │   │   │   ├── AuthSessionRepositoryImpl.java
│   │   │   │   ├── AuthTokenRepositoryImpl.java
│   │   │   │   └── AuthClientRepositoryImpl.java
│   │   │   ├── org/
│   │   │   │   ├── OrganizationRepositoryImpl.java
│   │   │   │   ├── DepartmentRepositoryImpl.java
│   │   │   │   └── PositionRepositoryImpl.java
│   │   │   ├── role/
│   │   │   │   ├── RoleRepositoryImpl.java
│   │   │   │   ├── PermissionRepositoryImpl.java
│   │   │   │   └── RolePermissionRepositoryImpl.java
│   │   │   ├── sys/
│   │   │   │   ├── SystemConfigRepositoryImpl.java
│   │   │   │   ├── SystemLogRepositoryImpl.java
│   │   │   │   └── DictionaryRepositoryImpl.java
│   │   │   └── user/
│   │   │       ├── UserRepositoryImpl.java
│   │   │       ├── UserProfileRepositoryImpl.java
│   │   │       └── UserAccountRepositoryImpl.java
│   │   └── assembler/                # 持久化转换器
│   │       ├── auth/
│   │       ├── org/
│   │       ├── role/
│   │       ├── sys/
│   │       └── user/
│   ├── cache/                        # 缓存实现
│   │   ├── config/
│   │   │   ├── RedisConfig.java
│   │   │   ├── CacheConfig.java
│   │   │   └── CacheKeyGenerator.java
│   │   ├── service/
│   │   │   ├── RedisCacheService.java
│   │   │   ├── LocalCacheService.java
│   │   │   └── DistributedCacheService.java
│   │   └── impl/
│   │       ├── auth/
│   │       ├── org/
│   │       ├── role/
│   │       ├── sys/
│   │       └── user/
│   ├── messaging/                    # 消息传递
│   │   ├── config/
│   │   │   ├── EventConfig.java
│   │   │   ├── MessageQueueConfig.java
│   │   │   └── EventPublisherConfig.java
│   │   ├── publisher/
│   │   │   ├── DomainEventPublisher.java
│   │   │   ├── IntegrationEventPublisher.java
│   │   │   └── AsyncEventPublisher.java
│   │   ├── handler/
│   │   │   ├── auth/
│   │   │   ├── org/
│   │   │   ├── role/
│   │   │   ├── sys/
│   │   │   └── user/
│   │   └── event/
│   │       ├── DomainEventStore.java
│   │       ├── EventSerializer.java
│   │       └── EventDispatcher.java
│   ├── external/                     # 外部服务集成
│   │   ├── keycloak/
│   │   │   ├── KeycloakClient.java
│   │   │   ├── KeycloakUserService.java
│   │   │   └── KeycloakSyncService.java
│   │   ├── sms/
│   │   │   ├── SmsService.java
│   │   │   └── SmsProvider.java
│   │   ├── email/
│   │   │   ├── EmailService.java
│   │   │   └── EmailProvider.java
│   │   ├── file/
│   │   │   ├── FileStorageService.java
│   │   │   └── FileUploadService.java
│   │   └── config/
│   │       ├── ExternalServiceConfig.java
│   │       ├── HttpClientConfig.java
│   │       └── CircuitBreakerConfig.java
│   ├── monitoring/                   # 监控实现
│   │   ├── metrics/
│   │   │   ├── BusinessMetrics.java
│   │   │   ├── SystemMetrics.java
│   │   │   └── PerformanceMetrics.java
│   │   ├── logging/
│   │   │   ├── StructuredLogger.java
│   │   │   ├── AuditLogger.java
│   │   │   └── SecurityLogger.java
│   │   ├── tracing/
│   │   │   ├── TracingConfig.java
│   │   │   └── TraceContextHolder.java
│   │   └── health/
│   │       ├── HealthIndicator.java
│   │       ├── DatabaseHealthCheck.java
│   │       └── ExternalServiceHealthCheck.java
│   └── config/                       # 基础设施配置
│       ├── InfrastructureConfig.java
│       ├── AsyncConfig.java
│       ├── SchedulingConfig.java
│       └── SecurityInfraConfig.java
│
├── interfaces/                       # 接口适配器层（Interface Layer）
│   ├── web/                          # Web接口
│   │   ├── controller/               # REST控制器
│   │   │   ├── auth/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── LoginController.java
│   │   │   │   └── TokenController.java
│   │   │   ├── org/
│   │   │   │   ├── OrganizationController.java
│   │   │   │   ├── DepartmentController.java
│   │   │   │   └── PositionController.java
│   │   │   ├── role/
│   │   │   │   ├── RoleController.java
│   │   │   │   ├── PermissionController.java
│   │   │   │   └── RoleAssignmentController.java
│   │   │   ├── sys/
│   │   │   │   ├── SystemConfigController.java
│   │   │   │   ├── SystemLogController.java
│   │   │   │   └── DictionaryController.java
│   │   │   └── user/
│   │   │       ├── UserController.java
│   │   │       ├── UserProfileController.java
│   │   │       └── UserAccountController.java
│   │   ├── dto/                      # Web传输对象
│   │   │   ├── request/              # 请求DTO
│   │   │   │   ├── auth/
│   │   │   │   ├── org/
│   │   │   │   ├── role/
│   │   │   │   ├── sys/
│   │   │   │   └── user/
│   │   │   └── response/             # 响应DTO
│   │   │       ├── auth/
│   │   │       ├── org/
│   │   │       ├── role/
│   │   │       ├── sys/
│   │   │       └── user/
│   │   ├── assembler/                # Web转换器
│   │   │   ├── auth/
│   │   │   ├── org/
│   │   │   ├── role/
│   │   │   ├── sys/
│   │   │   └── user/
│   │   ├── filter/                   # Web过滤器
│   │   │   ├── AuthenticationFilter.java
│   │   │   ├── AuthorizationFilter.java
│   │   │   ├── CorsFilter.java
│   │   │   └── RequestLoggingFilter.java
│   │   ├── interceptor/              # Web拦截器
│   │   │   ├── SecurityInterceptor.java
│   │   │   ├── LoggingInterceptor.java
│   │   │   └── RateLimitInterceptor.java
│   │   ├── handler/                  # 异常处理器
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── ValidationExceptionHandler.java
│   │   │   └── SecurityExceptionHandler.java
│   │   └── config/                   # Web配置
│   │       ├── WebMvcConfig.java
│   │       ├── SwaggerConfig.java
│   │       └── CorsConfig.java
│   ├── rpc/                          # RPC接口
│   │   ├── service/                  # RPC服务实现
│   │   │   ├── auth/
│   │   │   ├── org/
│   │   │   ├── role/
│   │   │   ├── sys/
│   │   │   └── user/
│   │   ├── dto/                      # RPC传输对象
│   │   └── config/                   # RPC配置
│   ├── mq/                           # 消息队列接口
│   │   ├── listener/                 # 消息监听器
│   │   │   ├── auth/
│   │   │   ├── org/
│   │   │   ├── role/
│   │   │   ├── sys/
│   │   │   └── user/
│   │   ├── producer/                 # 消息生产者
│   │   └── config/                   # 消息队列配置
│   ├── scheduler/                    # 定时任务接口
│   │   ├── job/                      # 定时任务
│   │   │   ├── auth/
│   │   │   ├── org/
│   │   │   ├── role/
│   │   │   ├── sys/
│   │   │   └── user/
│   │   └── config/                   # 调度配置
│   └── graphql/                      # GraphQL接口（可选）
│       ├── resolver/
│       ├── schema/
│       └── config/
│
└── shared/                           # 共享内核（Shared Kernel）
    ├── kernel/                       # 核心共享组件
    │   ├── base/                     # 基础抽象
    │   │   ├── entity/               # 基础实体
    │   │   │   ├── BaseEntity.java
    │   │   │   ├── AggregateRoot.java
    │   │   │   └── ValueObject.java
    │   │   ├── repository/           # 基础仓储
    │   │   │   ├── BaseRepository.java
    │   │   │   └── Repository.java
    │   │   ├── service/              # 基础服务
    │   │   │   ├── DomainService.java
    │   │   │   └── ApplicationService.java
    │   │   ├── event/                # 基础事件
    │   │   │   ├── DomainEvent.java
    │   │   │   ├── IntegrationEvent.java
    │   │   │   └── EventHandler.java
    │   │   └── specification/        # 规约模式
    │   │       ├── Specification.java
    │   │       └── CompositeSpecification.java
    │   ├── common/                   # 通用组件
    │   │   ├── annotation/           # 注解定义
    │   │   ├── constants/            # 常量定义
    │   │   ├── enums/                # 枚举定义
    │   │   ├── exception/            # 异常定义
    │   │   ├── utils/                # 工具类
    │   │   ├── validation/           # 验证框架
    │   │   └── serializer/           # 序列化器
    │   ├── security/                 # 安全组件
    │   │   ├── authentication/      # 认证组件
    │   │   ├── authorization/        # 授权组件
    │   │   ├── encryption/           # 加密组件
    │   │   ├── audit/                # 审计组件
    │   │   └── config/               # 安全配置
    │   └── monitoring/               # 监控组件
    │       ├── metrics/              # 指标收集
    │       ├── logging/              # 日志组件
    │       ├── tracing/              # 链路追踪
    │       └── health/               # 健康检查
    └── config/                       # 全局配置
        ├── GlobalConfig.java
        ├── ProfileConfig.java
        ├── EnvironmentConfig.java
        └── FeatureToggleConfig.java
```

## 3. 迁移映射表

### 3.1 包级别迁移映射

| 现有包路径 | 新包路径 | 迁移内容 | 优先级 | 备注 |
|-----------|----------|----------|--------|------|
| `com.jiuxi.admin.core.controller` | `com.jiuxi.interfaces.web.controller` | Web控制器 | 高 | 按领域拆分 |
| `com.jiuxi.admin.core.service` | `com.jiuxi.app.service` | 应用服务 | 高 | 实现CQRS分离 |
| `com.jiuxi.admin.core.mapper` | `com.jiuxi.infrastructure.persistence.mapper` | 数据访问 | 高 | 按领域分包 |
| `com.jiuxi.admin.core.bean.entity` | `com.jiuxi.domain.*/aggregate` | 领域实体 | 高 | 重构为聚合根 |
| `com.jiuxi.admin.core.bean.vo` | `com.jiuxi.interfaces.web.dto` | 数据传输对象 | 中 | 区分请求/响应 |
| `com.jiuxi.common.*` | `com.jiuxi.shared.kernel.common.*` | 通用组件 | 中 | 整合到共享内核 |
| `com.jiuxi.security.*` | `com.jiuxi.shared.kernel.security.*` | 安全组件 | 中 | 统一安全框架 |
| `com.jiuxi.mybatis.*` | `com.jiuxi.infrastructure.persistence.config` | 持久化配置 | 低 | 配置统一管理 |
| `com.jiuxi.monitor.*` | `com.jiuxi.shared.kernel.monitoring.*` | 监控组件 | 低 | 监控能力共享 |
| `com.jiuxi.platform.*` | `com.jiuxi.infrastructure.*` | 平台组件 | 低 | 按功能重新分类 |

### 3.2 领域模块详细映射

#### 3.2.1 认证授权领域 (auth)

| 现有类 | 新位置 | 类型 | 说明 |
|--------|--------|------|------|
| `TpAccount*` | `domain.auth.aggregate.AuthAccount` | 聚合根 | 账户聚合 |
| `TpKeycloakAccount*` | `domain.auth.entity.KeycloakAccount` | 实体 | Keycloak账户 |
| `LoginController` | `interfaces.web.controller.auth.AuthController` | 控制器 | 认证接口 |
| `PersonAccountService` | `app.service.auth.AuthApplicationService` | 应用服务 | 认证应用服务 |
| `KeycloakSyncService` | `infrastructure.external.keycloak.KeycloakSyncService` | 外部服务 | Keycloak集成 |

#### 3.2.2 组织架构领域 (org)

| 现有类 | 新位置 | 类型 | 说明 |
|--------|--------|------|------|
| `TpEntBasicinfo*` | `domain.org.aggregate.Organization` | 聚合根 | 企业组织 |
| `TpDeptBasicinfo*` | `domain.org.aggregate.Department` | 聚合根 | 部门聚合 |
| `TpCity*` | `domain.org.entity.City` | 实体 | 城市实体 |
| `OrgController` | `interfaces.web.controller.org.OrganizationController` | 控制器 | 组织接口 |
| `TpEntBasicinfoService` | `app.service.org.OrganizationApplicationService` | 应用服务 | 组织应用服务 |
| `TpDeptBasicinfoService` | `app.service.org.DepartmentApplicationService` | 应用服务 | 部门应用服务 |

#### 3.2.3 用户管理领域 (user)

| 现有类 | 新位置 | 类型 | 说明 |
|--------|--------|------|------|
| `TpPersonBasicinfo*` | `domain.user.aggregate.User` | 聚合根 | 用户聚合 |
| `TpPersonExinfo*` | `domain.user.entity.UserProfile` | 实体 | 用户档案 |
| `TpPersonDept*` | `domain.user.entity.UserDepartment` | 实体 | 用户部门关系 |
| `UserController` | `interfaces.web.controller.user.UserController` | 控制器 | 用户接口 |
| `TpPersonBasicinfoService` | `app.service.user.UserApplicationService` | 应用服务 | 用户应用服务 |

#### 3.2.4 角色权限领域 (role)

| 现有类 | 新位置 | 类型 | 说明 |
|--------|--------|------|------|
| `TpRole*` | `domain.role.aggregate.Role` | 聚合根 | 角色聚合 |
| `TpMenu*` | `domain.role.aggregate.Permission` | 聚合根 | 权限聚合 |
| `TpRoleMenu*` | `domain.role.entity.RolePermission` | 实体 | 角色权限关系 |
| `TpPersonRole*` | `domain.role.entity.UserRole` | 实体 | 用户角色关系 |
| `RoleController` | `interfaces.web.controller.role.RoleController` | 控制器 | 角色接口 |
| `TpRoleService` | `app.service.role.RoleApplicationService` | 应用服务 | 角色应用服务 |

#### 3.2.5 系统管理领域 (sys)

| 现有类 | 新位置 | 类型 | 说明 |
|--------|--------|------|------|
| `TpSystemConfig*` | `domain.sys.aggregate.SystemConfig` | 聚合根 | 系统配置 |
| `TpOperateLog*` | `domain.sys.aggregate.SystemLog` | 聚合根 | 操作日志 |
| `TpDictionary*` | `domain.sys.aggregate.Dictionary` | 聚合根 | 数据字典 |
| `SystemController` | `interfaces.web.controller.sys.SystemController` | 控制器 | 系统接口 |
| `TpSystemConfigService` | `app.service.sys.SystemConfigApplicationService` | 应用服务 | 配置应用服务 |

## 4. 实施策略

### 4.1 分阶段实施计划

#### 阶段1：基础架构搭建（2-3周）
1. **创建新包结构**
   - 创建完整的目录结构
   - 建立包级别的依赖规则
   - 配置ArchUnit架构检查

2. **共享内核重构**
   - 迁移common包到shared.kernel.common
   - 重构异常处理体系
   - 统一工具类和常量定义

3. **基础设施层搭建**
   - 重构持久化配置
   - 建立缓存框架
   - 配置消息传递机制

#### 阶段2：领域模型重构（4-5周）
1. **用户领域重构**（1周）
   - 提取User聚合根
   - 重构用户相关服务
   - 实现用户仓储模式

2. **组织架构领域重构**（1.5周）
   - 提取Organization和Department聚合
   - 重构组织架构服务
   - 实现组织树查询优化

3. **角色权限领域重构**（1周）
   - 提取Role和Permission聚合
   - 重构权限检查逻辑
   - 实现角色分配服务

4. **认证授权领域重构**（1周）
   - 提取AuthSession聚合
   - 重构认证流程
   - 集成Keycloak服务

5. **系统管理领域重构**（0.5周）
   - 提取SystemConfig聚合
   - 重构配置管理
   - 实现日志服务

#### 阶段3：应用层重构（2-3周）
1. **CQRS实现**
   - 分离命令和查询处理
   - 实现命令处理器
   - 建立查询投影

2. **应用服务重构**
   - 重构现有应用服务
   - 实现业务编排器
   - 优化事务管理

#### 阶段4：接口层重构（2周）
1. **Web接口重构**
   - 迁移现有Controller
   - 统一接口规范
   - 实现接口版本管理

2. **其他接口适配**
   - 实现RPC接口
   - 配置消息队列监听
   - 重构定时任务

#### 阶段5：监控和优化（1-2周）
1. **监控体系建立**
   - 实现业务指标监控
   - 建立性能监控
   - 配置告警机制

2. **性能优化**
   - 查询性能优化
   - 缓存策略优化
   - 异步处理优化

### 4.2 风险控制措施

#### 4.2.1 技术风险
- **渐进式迁移**：每次只迁移一个小模块
- **双轨并行**：新旧代码并存，逐步切换
- **自动化测试**：确保重构不破坏现有功能
- **回滚机制**：每个步骤都有明确的回滚方案

#### 4.2.2 业务风险
- **功能完整性验证**：确保所有业务功能正常
- **数据一致性检查**：保证数据迁移的准确性
- **性能基准测试**：确保重构后性能不下降
- **用户体验验证**：保证接口兼容性

### 4.3 质量保证

#### 4.3.1 代码质量
- **代码规范检查**：使用Checkstyle、PMD等工具
- **架构合规性检查**：使用ArchUnit进行架构约束检查
- **单元测试覆盖率**：要求覆盖率达到80%以上
- **集成测试**：确保模块间协作正常

#### 4.3.2 架构质量
- **依赖关系检查**：确保依赖方向正确
- **循环依赖检测**：避免模块间循环依赖
- **接口设计审查**：确保接口设计合理
- **性能测试**：验证架构性能表现

## 5. 预期收益

### 5.1 架构收益
- **清晰的职责边界**：各层职责明确，便于维护
- **高度的可测试性**：领域逻辑与技术实现分离
- **良好的可扩展性**：为未来微服务拆分奠定基础
- **统一的代码规范**：提高团队开发效率

### 5.2 业务收益
- **更快的功能交付**：清晰的架构减少开发时间
- **更低的维护成本**：模块化设计降低维护复杂度
- **更好的系统稳定性**：分层架构提高系统健壮性
- **更强的业务表达力**：DDD模型更贴近业务

### 5.3 技术收益
- **现代化的技术栈**：采用最新的架构模式
- **完善的监控体系**：便于系统运维和问题排查
- **标准化的开发流程**：提高团队协作效率
- **可持续的架构演进**：为未来技术升级做好准备

## 6. 总结

本优化方案基于DDD架构原则，结合六边形架构和CQRS模式，将传统的分层架构转换为清洁架构。通过明确的职责划分和依赖关系，提高系统的可维护性、可扩展性和业务表达力。

重构完成后，系统将具备：
1. **清晰的架构边界**：各层职责明确，依赖关系清晰
2. **高度的可测试性**：领域逻辑与技术实现分离
3. **良好的可扩展性**：为未来的微服务拆分奠定基础
4. **统一的代码规范**：提高团队开发效率
5. **完善的监控体系**：便于系统运维和问题排查

通过渐进式的迁移策略，确保系统稳定性的同时逐步完成架构升级，最终实现从传统架构向现代DDD架构的成功转型。

---

**文档版本**: v1.0  
**创建日期**: 2024年1月  
**负责人**: 系统架构师  
**审核人**: 技术总监