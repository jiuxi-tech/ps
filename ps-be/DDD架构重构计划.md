# ps-bmp项目DDD架构重构计划

## 1. 项目概述

### 1.1 重构目标

本次重构旨在将ps-bmp项目从传统的分层架构转换为基于领域驱动设计(DDD)的清洁架构，提高代码的可维护性、可扩展性和业务表达力。

### 1.2 设计原则

- **领域驱动设计(DDD)**：以业务领域为核心，构建清晰的领域模型
- **六边形架构**：应用核心与外部依赖解耦
- **CQRS模式**：命令查询职责分离
- **事件驱动**：通过领域事件实现模块间解耦
- **依赖倒置**：高层模块不依赖低层模块

## 2. 目标架构设计

### 2.1 新的包结构

```
com.jiuxi/
├── app/                    # 应用层（应用服务、配置等）
│   ├── service/           # 应用服务
│   ├── assembler/         # 数据转换器
│   ├── dto/              # 数据传输对象
│   ├── command/          # 命令对象
│   ├── query/            # 查询对象
│   └── config/           # 应用配置
├── domain/                # 领域层（核心业务逻辑）
│   ├── auth/             # 认证授权领域
│   ├── org/              # 组织架构领域
│   ├── user/             # 用户管理领域
│   ├── sys/              # 系统管理领域
│   └── role/             # 角色权限领域
├── infra/                 # 基础设施层（持久化、外部服务等）
│   ├── persistence/      # 数据持久化
│   ├── messaging/        # 消息传递
│   ├── cache/            # 缓存实现
│   ├── external/         # 外部服务集成
│   └── config/           # 基础设施配置
├── intf/                  # 接口适配器层（Web、RPC等）
│   ├── web/              # Web接口
│   ├── rpc/              # RPC接口
│   ├── mq/               # 消息队列接口
│   └── scheduler/        # 定时任务接口
├── shared/                # 共享内核（通用工具、公共组件等）
│   ├── common/           # 通用组件
│   ├── util/             # 工具类
│   ├── exception/        # 异常定义
│   ├── constant/         # 常量定义
│   └── security/         # 安全组件
└── Bootstrap.java         # 应用启动类
```

### 2.2 各层职责说明

#### 2.2.1 应用层 (app)
- **职责**：协调领域对象完成业务用例，不包含业务逻辑
- **组件**：
  - `service/`：应用服务，编排领域服务完成业务用例
  - `assembler/`：DTO与领域对象之间的转换
  - `dto/`：数据传输对象，用于层间数据传递
  - `command/`：命令对象，封装写操作请求
  - `query/`：查询对象，封装读操作请求
  - `config/`：应用层配置

#### 2.2.2 领域层 (module)
- **职责**：核心业务逻辑，领域模型的实现
- **组件**：
  - `entity/`：领域实体
  - `valueobject/`：值对象
  - `service/`：领域服务
  - `repository/`：仓储接口
  - `event/`：领域事件
  - `factory/`：领域工厂

#### 2.2.3 基础设施层 (infra)
- **职责**：技术实现，为其他层提供技术支持
- **组件**：
  - `persistence/`：数据持久化实现
  - `messaging/`：消息传递实现
  - `cache/`：缓存实现
  - `external/`：外部服务集成
  - `config/`：基础设施配置

#### 2.2.4 接口适配器层 (intf)
- **职责**：外部接口适配，协议转换
- **组件**：
  - `web/`：Web控制器
  - `rpc/`：RPC服务实现
  - `mq/`：消息队列监听器
  - `scheduler/`：定时任务

#### 2.2.5 共享内核 (shared)
- **职责**：跨领域的通用组件和工具
- **组件**：
  - `common/`：通用组件
  - `util/`：工具类
  - `exception/`：异常定义
  - `constant/`：常量定义
  - `security/`：安全组件

## 3. 现状分析

### 3.1 当前包结构

```
com.jiuxi/
├── admin/              # 系统管理相关模块
├── app/                # 应用配置模块
├── common/             # 通用工具、常量、异常等
├── core/               # 核心配置模块
├── module/             # 业务模块（用户、组织、系统等）
├── monitor/            # 监控相关模块
├── mybatis/            # MyBatis配置模块
├── platform/           # 平台级功能模块（验证码、监控等）
├── security/           # 安全相关模块
└── shared/             # 共享资源模块
```

### 3.2 存在的问题

1. **架构混乱**：缺乏清晰的分层架构，业务逻辑分散
2. **职责不清**：admin、core、platform等模块职责重叠
3. **耦合度高**：模块间依赖关系复杂，难以维护
4. **命名不规范**：包名不符合DDD架构规范
5. **技术导向**：以技术组件为划分依据，而非业务领域

## 4. 重构实施计划

### 4.1 第一阶段：基础架构搭建（1-2周）

#### 4.1.1 创建新的包结构
- [ ] 创建app包及其子包
- [ ] 重构module包结构
- [ ] 创建infra包及其子包
- [ ] 创建intf包及其子包
- [ ] 整理shared包结构

#### 4.1.2 定义架构规范
- [ ] 制定包命名规范
- [ ] 定义层间依赖规则
- [ ] 建立代码规范文档
- [ ] 配置架构检查工具

### 4.2 第二阶段：共享内核重构（1周）

#### 4.2.1 整理共享组件
- [ ] 迁移common包内容到shared
- [ ] 重构异常处理机制
- [ ] 统一工具类组织
- [ ] 整理常量定义

#### 4.2.2 安全组件重构
- [ ] 迁移security包到shared/security
- [ ] 重构认证授权组件
- [ ] 统一安全配置

### 4.3 第三阶段：领域模块重构（3-4周）

#### 4.3.1 组织架构领域 (module/org)
- [ ] 提取组织架构领域模型
- [ ] 重构部门管理业务逻辑
- [ ] 实现组织架构仓储接口
- [ ] 定义组织架构领域事件

#### 4.3.2 用户管理领域 (module/user)
- [ ] 重构用户实体模型
- [ ] 实现用户领域服务
- [ ] 重构用户仓储实现
- [ ] 定义用户相关事件

#### 4.3.3 角色权限领域 (module/role)
- [ ] 提取角色权限模型
- [ ] 实现权限检查服务
- [ ] 重构角色分配逻辑

#### 4.3.4 系统管理领域 (module/sys)
- [ ] 重构系统配置管理
- [ ] 实现日志管理服务
- [ ] 重构字典管理功能

#### 4.3.5 认证授权领域 (module/auth)
- [ ] 从admin包迁移认证逻辑
- [ ] 实现SSO集成服务
- [ ] 重构会话管理

### 4.4 第四阶段：基础设施层重构（2-3周）

#### 4.4.1 数据持久化 (infra/persistence)
- [ ] 迁移MyBatis配置到infra
- [ ] 重构数据访问层
- [ ] 实现仓储模式
- [ ] 优化数据库连接配置

#### 4.4.2 缓存实现 (infra/cache)
- [ ] 统一缓存配置
- [ ] 实现分布式缓存
- [ ] 优化缓存策略

#### 4.4.3 消息传递 (infra/messaging)
- [ ] 实现事件发布机制
- [ ] 配置消息队列
- [ ] 实现异步处理

#### 4.4.4 外部服务集成 (infra/external)
- [ ] 重构第三方服务调用
- [ ] 实现服务熔断机制
- [ ] 统一外部接口管理

### 4.5 第五阶段：应用层重构（2周）

#### 4.5.1 应用服务 (app/service)
- [ ] 重构业务用例实现
- [ ] 实现CQRS模式
- [ ] 优化事务管理

#### 4.5.2 数据转换 (app/assembler)
- [ ] 实现DTO转换器
- [ ] 统一数据映射规则
- [ ] 优化性能

#### 4.5.3 命令查询对象 (app/command & app/query)
- [ ] 定义命令对象
- [ ] 实现查询对象
- [ ] 添加验证规则

### 4.6 第六阶段：接口适配层重构（2周）

#### 4.6.1 Web接口 (intf/web)
- [ ] 迁移现有Controller
- [ ] 统一接口规范
- [ ] 实现接口版本管理
- [ ] 优化异常处理

#### 4.6.2 其他接口适配
- [ ] 实现RPC接口
- [ ] 配置消息队列监听
- [ ] 重构定时任务

### 4.7 第七阶段：监控和平台功能重构（1-2周）

#### 4.7.1 监控功能重构
- [ ] 迁移monitor包功能
- [ ] 实现分布式监控
- [ ] 优化性能指标收集

#### 4.7.2 平台功能重构
- [ ] 迁移platform包功能
- [ ] 重构验证码服务
- [ ] 统一平台级配置

## 5. 迁移策略

### 5.1 渐进式迁移
- 采用渐进式迁移策略，避免大规模代码变更
- 保持系统运行稳定性
- 分模块逐步迁移，降低风险

### 5.2 向后兼容
- 在迁移过程中保持API向后兼容
- 使用适配器模式处理接口变更
- 逐步废弃旧接口

### 5.3 数据迁移
- 数据库结构保持不变
- 仅调整代码层面的组织结构
- 确保数据一致性

## 6. 风险控制

### 6.1 技术风险
- **风险**：大规模重构可能引入新的Bug
- **控制措施**：
  - 完善单元测试覆盖
  - 分阶段回归测试
  - 代码审查机制

### 6.2 进度风险
- **风险**：重构周期可能超出预期
- **控制措施**：
  - 制定详细的里程碑计划
  - 定期进度评估
  - 灵活调整优先级

### 6.3 业务风险
- **风险**：重构期间可能影响业务功能
- **控制措施**：
  - 保持功能完整性
  - 充分的测试验证
  - 快速回滚机制

## 7. 质量保证

### 7.1 代码质量
- 建立代码规范检查
- 实施代码审查流程
- 提高测试覆盖率
- 使用静态代码分析工具

### 7.2 架构质量
- 定义架构决策记录(ADR)
- 实施架构合规性检查
- 定期架构评审

### 7.3 文档质量
- 更新技术文档
- 编写迁移指南
- 维护API文档

## 8. 详细实施步骤

### 8.1 包结构创建脚本

```bash
# 创建新的包结构目录
mkdir -p src/main/java/com/jiuxi/app/{service,assembler,dto,command,query,config}
mkdir -p src/main/java/com/jiuxi/module/{auth,org,user,sys,role}/{entity,valueobject,service,repository,event,factory}
mkdir -p src/main/java/com/jiuxi/infra/{persistence,messaging,cache,external,config}
mkdir -p src/main/java/com/jiuxi/intf/{web,rpc,mq,scheduler}
mkdir -p src/main/java/com/jiuxi/shared/{common,util,exception,constant,security}
```

### 8.2 具体迁移映射表

#### 8.2.1 现有包 -> 新包映射

| 现有包路径 | 新包路径 | 迁移内容 | 优先级 |
|-----------|----------|----------|--------|
| `com.jiuxi.admin.core.controller` | `com.jiuxi.intf.web.controller` | Web控制器 | 高 |
| `com.jiuxi.admin.core.service` | `com.jiuxi.app.service` | 应用服务 | 高 |
| `com.jiuxi.admin.core.mapper` | `com.jiuxi.infra.persistence.mapper` | 数据访问 | 高 |
| `com.jiuxi.admin.core.bean.entity` | `com.jiuxi.module.*/entity` | 领域实体 | 高 |
| `com.jiuxi.admin.core.bean.vo` | `com.jiuxi.app.dto` | 数据传输对象 | 中 |
| `com.jiuxi.common.util` | `com.jiuxi.shared.util` | 工具类 | 中 |
| `com.jiuxi.common.exception` | `com.jiuxi.shared.exception` | 异常定义 | 中 |
| `com.jiuxi.common.constant` | `com.jiuxi.shared.constant` | 常量定义 | 中 |
| `com.jiuxi.security` | `com.jiuxi.shared.security` | 安全组件 | 中 |
| `com.jiuxi.mybatis` | `com.jiuxi.infra.persistence.config` | 持久化配置 | 低 |
| `com.jiuxi.monitor` | `com.jiuxi.infra.monitoring` | 监控组件 | 低 |
| `com.jiuxi.platform` | `com.jiuxi.infra.platform` | 平台组件 | 低 |

#### 8.2.2 领域模块详细划分

**组织架构领域 (module/org)**
- 实体：Department, Organization, Position
- 值对象：DepartmentCode, OrganizationLevel
- 服务：OrganizationDomainService, DepartmentDomainService
- 仓储：DepartmentRepository, OrganizationRepository
- 事件：DepartmentCreatedEvent, OrganizationUpdatedEvent

**用户管理领域 (module/user)**
- 实体：User, UserProfile, UserAccount
- 值对象：UserId, Email, PhoneNumber
- 服务：UserDomainService, UserValidationService
- 仓储：UserRepository, UserProfileRepository
- 事件：UserRegisteredEvent, UserActivatedEvent

**角色权限领域 (module/role)**
- 实体：Role, Permission, RolePermission
- 值对象：RoleCode, PermissionCode
- 服务：RoleDomainService, PermissionDomainService
- 仓储：RoleRepository, PermissionRepository
- 事件：RoleAssignedEvent, PermissionGrantedEvent

**系统管理领域 (module/sys)**
- 实体：SystemConfig, SystemLog, Dictionary
- 值对象：ConfigKey, LogLevel
- 服务：SystemConfigService, LoggingService
- 仓储：SystemConfigRepository, SystemLogRepository
- 事件：ConfigChangedEvent, SystemErrorEvent

**认证授权领域 (module/auth)**
- 实体：AuthSession, AuthToken, AuthClient
- 值对象：TokenValue, SessionId
- 服务：AuthenticationService, AuthorizationService
- 仓储：AuthSessionRepository, AuthTokenRepository
- 事件：UserLoginEvent, TokenExpiredEvent

### 8.3 重构检查清单

#### 8.3.1 架构合规性检查
- [ ] 依赖方向正确（外层依赖内层）
- [ ] 领域层不依赖基础设施层
- [ ] 接口适配层不包含业务逻辑
- [ ] 应用层不包含技术实现细节

#### 8.3.2 代码质量检查
- [ ] 单元测试覆盖率 > 80%
- [ ] 集成测试覆盖核心业务流程
- [ ] 代码规范检查通过
- [ ] 性能测试通过

#### 8.3.3 功能完整性检查
- [ ] 所有现有功能正常工作
- [ ] API接口保持兼容
- [ ] 数据完整性验证通过
- [ ] 安全功能正常

## 9. 配置文件调整

### 9.1 Spring Boot配置调整

```yaml
# application.yml 包扫描配置调整
spring:
  main:
    banner-mode: console
  application:
    name: ps-bmp

# 组件扫描配置
mybatis-plus:
  mapper-locations: classpath*:com/jiuxi/infra/persistence/mapper/**/*.xml
  type-aliases-package: com.jiuxi.module.*.entity

# 日志配置调整
logging:
  level:
    com.jiuxi.app: INFO
    com.jiuxi.module: INFO
    com.jiuxi.infra: DEBUG
    com.jiuxi.intf: INFO
```

### 9.2 Maven配置调整

```xml
<!-- pom.xml 构建配置调整 -->
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>com.jiuxi.Bootstrap</mainClass>
            </configuration>
        </plugin>
        
        <!-- 架构检查插件 -->
        <plugin>
            <groupId>com.tngtech.archunit</groupId>
            <artifactId>archunit-maven-plugin</artifactId>
            <version>0.23.1</version>
            <configuration>
                <rules>
                    <rule>com.jiuxi.shared.architecture.ArchitectureRules</rule>
                </rules>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 10. 测试策略

### 10.1 单元测试
- 每个领域服务必须有对应的单元测试
- 应用服务的业务逻辑测试
- 工具类和共享组件测试

### 10.2 集成测试
- 数据库集成测试
- Web接口集成测试
- 外部服务集成测试

### 10.3 架构测试
```java
// 架构规则测试示例
@ArchTest
static final ArchRule domain_should_not_depend_on_infrastructure =
    noClasses().that().resideInAPackage("..module..")
    .should().dependOnClassesThat().resideInAPackage("..infra..");

@ArchTest
static final ArchRule application_should_not_depend_on_interface =
    noClasses().that().resideInAPackage("..app..")
    .should().dependOnClassesThat().resideInAPackage("..intf..");
```

## 11. 性能优化建议

### 11.1 数据访问优化
- 实现读写分离
- 优化查询性能
- 合理使用缓存

### 11.2 应用性能优化
- 异步处理非关键业务
- 实现事件驱动架构
- 优化内存使用

### 11.3 监控和告警
- 添加业务指标监控
- 实现性能告警
- 建立运维仪表板

## 12. 后续演进规划

### 12.1 微服务拆分准备
- 领域边界清晰化
- 服务接口标准化
- 数据库拆分准备

### 12.2 云原生改造
- 容器化部署
- 服务网格集成
- 配置中心集成

### 12.3 技术栈升级
- Spring Boot版本升级
- JDK版本升级
- 数据库版本升级

## 13. 总结

本重构计划基于DDD架构原则，将传统的分层架构转换为清洁架构，通过明确的职责划分和依赖关系，提高系统的可维护性和可扩展性。重构过程采用渐进式策略，确保系统稳定性的同时逐步完成架构升级。

重构完成后，系统将具备以下优势：
1. **清晰的架构边界**：各层职责明确，依赖关系清晰
2. **高度的可测试性**：领域逻辑与技术实现分离
3. **良好的可扩展性**：为未来的微服务拆分奠定基础
4. **统一的代码规范**：提高团队开发效率
5. **完善的监控体系**：便于系统运维和问题排查

---

**文档版本**: v1.0  
**创建日期**: 2024年1月  
**最后更新**: 2024年1月  
**负责人**: 系统架构师  
**审核人**: 技术总监







          
## 基础设施层 vs 接口层的区别

在DDD（领域驱动设计）的清洁架构中，基础设施层和接口层有着明确的职责分工：

### 基础设施层 (infra/)
**职责**：提供技术实现和外部依赖的具体实现

**主要内容**：
- **数据持久化**：数据库访问、Mapper、Repository实现
- **外部服务集成**：第三方API调用、消息队列、缓存
- **技术配置**：数据库配置、Redis配置、消息中间件配置
- **基础组件**：文件存储、邮件发送、短信服务等

**特点**：
- 包含具体的技术实现细节
- 依赖外部系统和框架
- 为上层提供技术能力支撑
- 可以被替换而不影响业务逻辑

### 接口层 (intf/)
**职责**：处理外部请求和对外提供服务接口

**主要内容**：
- **Web接口**：REST API、Controller
- **RPC接口**：远程过程调用接口
- **消息队列**：消息监听器、事件处理
- **定时任务**：调度器、批处理任务

**特点**：
- 负责协议转换和数据格式转换
- 处理外部请求的入口点
- 调用应用层服务完成业务处理
- 不包含业务逻辑，只做适配和转换

### 关键区别

| 维度 | 基础设施层 | 接口层 |
|------|------------|--------|
| **方向** | 向内提供技术能力 | 向外暴露业务能力 |
| **依赖** | 被应用层和领域层依赖 | 依赖应用层 |
| **职责** | 技术实现和外部集成 | 协议适配和请求处理 |
| **示例** | 数据库访问、缓存、文件存储 | HTTP接口、消息监听、定时任务 |

### 实际例子

**用户注册场景**：
- **接口层**：`UserController.register()` 接收HTTP请求，验证参数格式
- **应用层**：`UserApplicationService.registerUser()` 编排业务流程
- **领域层**：`User` 实体验证业务规则
- **基础设施层**：`UserRepositoryImpl` 将用户数据保存到数据库

这样的分层设计确保了：
1. **业务逻辑与技术实现分离**
2. **外部接口与内部实现解耦**
3. **系统的可测试性和可维护性**
4. **技术栈的可替换性**
        