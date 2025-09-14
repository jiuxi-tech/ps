# user单业务模块重构计划

> **生成时间**：2025-09-14  
> **模块路径**：`d:\jiuxi\project\ps\ps-be\src\main\java\com\jiuxi\module\user`  
> **重构版本**：v1.0

## 1. 项目概述

基于对 `d:\jiuxi\project\ps\ps-be\src\main\java\com\jiuxi\module\user` 的深度分析，本计划旨在**不破坏现有功能**的前提下，通过系统性重构实现：

- **统一架构设计**：全面采用DDD（领域驱动设计）分层架构
- **优化代码组织**：完善目录结构和代码组织方式  
- **提升可维护性**：建立清晰的业务边界和依赖关系
- **增强功能特性**：优化CQRS模式，分离命令和查询操作
- **优化模块代码**: 识别模块坏气味代码并进行修改
- **清理删除文件（夹）**: 识别无用的代码、文件或者目录

## 2. 现状分析

### 2.1 当前模块结构

```
user/
├── UserModuleConfiguration.java
├── app/
│   ├── assembler/
│   │   └── UserAssembler.java
│   ├── dto/
│   │   ├── UserCreateDTO.java
│   │   ├── UserQueryDTO.java
│   │   ├── UserResponseDTO.java
│   │   └── UserUpdateDTO.java
│   ├── impl/
│   │   ├── PersonAccountApplicationServiceImpl.java
│   │   ├── UserAccountServiceImpl.java
│   │   ├── UserApplicationServiceImpl.java
│   │   └── UserPersonServiceImpl.java
│   └── service/
│       ├── PersonAccountApplicationService.java
│       ├── UserAccountService.java
│       ├── UserApplicationService.java
│       ├── UserPersonService.java
│       └── impl/
│           └── UserApplicationServiceImpl.java
├── domain/
│   ├── entity/
│   │   ├── AccountStatus.java
│   │   ├── ContactInfo.java
│   │   ├── User.java
│   │   ├── UserAccount.java
│   │   ├── UserCategory.java
│   │   ├── UserProfile.java
│   │   └── UserStatus.java
│   ├── event/
│   │   ├── UserAccountCreatedEvent.java
│   │   ├── UserCreatedEvent.java
│   │   ├── UserDeletedEvent.java
│   │   ├── UserEvent.java
│   │   ├── UserProfileUpdatedEvent.java
│   │   └── UserUpdatedEvent.java
│   ├── repo/
│   │   └── UserRepository.java
│   ├── service/
│   │   └── UserDomainService.java
│   └── vo/
│       ├── Email.java
│       ├── PhoneNumber.java
│       ├── UserId.java
│       └── UserName.java
├── infra/
│   └── persistence/
│       ├── assembler/
│       │   └── UserPOAssembler.java
│       ├── entity/
│       │   ├── AccountPO.java
│       │   └── UserPO.java
│       ├── mapper/
│       │   ├── AccountMapper.java
│       │   ├── UserAccountMapper.java
│       │   ├── UserMapper.java
│       │   └── UserPersonMapper.java
│       └── repo/
│           └── UserRepositoryImpl.java
└── intf/
    └── web/
        ├── controller/
        │   ├── CommonPersonController.java
        │   ├── KeycloakAccountSyncController.java
        │   ├── UserAccountController.java
        │   ├── UserController.java
        │   └── UserPersonController.java
        └── dto/
            ├── ChangePasswordRequest.java
            └── PageResult.java
```

### 2.2 核心业务识别

**主要业务职责**：用户管理模块负责系统中用户信息的全生命周期管理，包括用户创建、更新、删除、查询、激活/停用等操作，同时管理用户的账户信息和权限信息。

**关键业务用例**：
1. 创建用户
2. 更新用户信息
3. 删除用户
4. 查询用户详情
5. 分页查询用户列表
6. 激活/停用用户
7. 重置用户密码
8. 根据部门查询用户
9. 批量删除用户

### 2.3 主要问题识别

1. **架构分层问题**
   - Controller层未按CQRS模式分离命令和查询操作
   - 应用服务层存在冗余实现（UserApplicationServiceImpl重复）
   - 接口层DTO未按请求/响应分类

2. **目录结构问题**
   - 缺少标准的DDD+CQRS目录结构（command/query/handler等）
   - 应用服务层impl目录嵌套在service目录下，不符合规范
   - Web层缺少assembler目录

3. **代码组织问题**
   - Controller类混合了命令和查询操作
   - DTO未按用途分类（请求/响应）
   - 缺少标准的事件适配器和门面服务

### 2.4 技术债务评估

**代码质量问题**：
- Controller未按CQRS分离，职责不清晰
- DTO混用，未按请求/响应分类
- 应用服务层结构混乱，存在重复实现

**架构问题**：
- 未遵循标准的六边形架构+DDD+CQRS模式
- 目录结构不符合重构规范要求
- 依赖关系未完全遵循分层原则

## 3. 重构目标与架构设计

### 3.1 目标DDD架构

```
user/
├── UserModuleConfiguration.java    # 模块配置类
├── app/                                   # 应用服务层 (Application Layer)
│   ├── command/                           # 命令处理器 (CQRS Command)
│   │   ├── handler/                       # 命令处理器实现
│   │   └── dto/                           # 命令数据传输对象
│   ├── query/                             # 查询处理器 (CQRS Query)
│   │   ├── handler/                       # 查询处理器实现
│   │   └── dto/                           # 查询数据传输对象
│   ├── assembler/                         # 对象装配器/转换器
│   ├── service/                           # 应用服务接口
│   ├── impl/                              # 应用服务实现
│   └── orchestrator/                      # 业务编排器
├── domain/                                # 领域层 (Domain Layer)
│   ├── model/                             # 领域模型
│   │   ├── aggregate/                     # 聚合根
│   │   ├── entity/                        # 实体
│   │   └── vo/                            # 值对象
│   ├── event/                             # 领域事件
│   │   ├── publisher/                     # 事件发布器
│   │   └── subscriber/                    # 事件订阅器
│   ├── service/                           # 领域服务接口
│   ├── impl/                              # 领域服务实现
│   ├── repo/                              # 仓储接口 (Repository Interface)
│   ├── gateway/                           # 防腐层网关接口
│   └── policy/                            # 业务策略/规则
├── infra/                                 # 基础设施层 (Infrastructure Layer)
│   ├── persistence/                       # 持久化适配器
│   │   ├── entity/                        # 持久化实体 (PO)
│   │   ├── mapper/                        # MyBatis映射器
│   │   ├── repo/                          # 仓储实现
│   │   └── assembler/                     # PO-DO转换器
│   ├── gateway/                           # 外部服务网关实现
│   │   ├── client/                        # 外部服务客户端
│   │   ├── dto/                           # 外部服务DTO
│   │   └── assembler/                     # 外部数据转换器
│   ├── messaging/                         # 消息基础设施
│   │   ├── producer/                      # 消息生产者
│   │   ├── consumer/                      # 消息消费者
│   │   └── config/                        # 消息配置
│   └── cache/                             # 缓存适配器
│       ├── config/                        # 缓存配置
│       └── strategy/                      # 缓存策略
└── intf/                                  # 接口适配器层 (Interface Adapters)
    ├── web/                               # Web适配器
    │   ├── controller/                    # REST控制器
    │   │   ├── command/                   # 命令控制器
    │   │   └── query/                     # 查询控制器
    │   ├── dto/                           # Web层DTO
    │   │   ├── request/                   # 请求DTO
    │   │   └── response/                  # 响应DTO
    │   ├── assembler/                     # Web层数据转换器
    │   └── interceptor/                   # Web拦截器
    ├── facade/                            # 服务门面 (对外暴露)
    │   ├── api/                           # 门面接口定义
    │   ├── impl/                          # 门面接口实现
    │   └── dto/                           # 门面层DTO
    └── event/                             # 事件适配器
        ├── listener/                      # 事件监听器
        └── publisher/                     # 事件发布适配器
```

### 3.2 架构层次与目录规范说明

基于六边形架构 + DDD + CQRS 的最佳实践，目录结构遵循以下设计原则：

#### 3.2.1 架构分层原则

1. **接口适配器层 (intf/)**：
   - 处理外部世界与应用核心的交互
   - 包含Web控制器、门面服务、事件监听器等适配器
   - 实现命令查询分离（CQRS），按command/query分离控制器
   - 所有Controller类必须迁移到对应的command或query子目录下
   - 所有DTO按request/response分类，避免混合使用

2. **应用服务层 (app/)**：
   - 实现业务用例和应用逻辑编排  
   - 采用CQRS模式，分离命令处理器(command)和查询处理器(query)
   - 包含业务编排器(orchestrator)协调复杂业务流程
   - **强制要求**：service和impl目录为同级关系，不允许嵌套

3. **领域层 (domain/)**：
   - 包含核心业务逻辑和领域模型
   - 聚合根(aggregate)管理实体生命周期和业务不变性
   - 领域事件实现解耦和最终一致性
   - 防腐层网关接口定义外部依赖抽象

4. **基础设施层 (infra/)**：
   - 提供技术实现细节，支撑上层业务逻辑
   - 持久化、外部服务、消息队列、缓存等适配器实现
   - 遵循依赖倒置原则，实现domain层定义的接口

#### 3.2.2 CQRS实现规范

1. **命令端 (Command)**：
   - 负责数据变更操作 (Create, Update, Delete)
   - 命令处理器位于 `app/command/handler/` 
   - 命令控制器位于 `intf/web/controller/command/`
   - 专注业务逻辑执行，通常无返回值或返回简单状态

2. **查询端 (Query)**：
   - 负责数据查询操作 (Read)
   - 查询处理器位于 `app/query/handler/`
   - 查询控制器位于 `intf/web/controller/query/`  
   - 专注数据获取和格式化，可直接访问持久层优化性能

#### 3.2.3 DDD战术设计规范

1. **聚合设计**：
   - 聚合根确保业务不变性和一致性边界
   - 实体和值对象分离存放，明确生命周期管理
   - 聚合间通过领域事件实现解耦

2. **仓储模式**：
   - 领域层定义仓储接口抽象数据访问
   - 基础设施层实现具体持久化逻辑
   - 支持聚合根的完整加载和保存

3. **防腐层设计**：
   - gateway接口定义外部系统交互契约
   - 隔离外部系统变化对核心域的影响
   - 实现数据格式转换和协议适配

#### 3.2.4 目录命名一致性原则

1. **完整语义命名**：
   - 所有目录名使用完整单词，避免缩写混淆
   - intf (Interface Adapters)、app (Application)、infra (Infrastructure) 
   - command/query 体现CQRS分离思想

2. **分层职责清晰**：
   - 每层目录结构体现其在架构中的职责
   - 同层级目录保持功能内聚，跨层级遵循依赖方向

3. **扩展性考虑**：
   - 为微服务拆分预留结构空间
   - 支持多种外部接口适配（Web、RPC、消息等）

### 3.3 代码迁移检查清单

为确保代码迁移工作正确执行，必须完成以下检查项：

#### 3.3.1 接口层迁移检查

1. **Controller按CQRS分离迁移**：
   - [x] 识别现有Controller中的命令操作（CREATE、UPDATE、DELETE）
   - [x] 将命令操作迁移到 `intf/web/controller/command/` 目录
   - [x] 识别现有Controller中的查询操作（SELECT、GET）  
   - [x] 将查询操作迁移到 `intf/web/controller/query/` 目录
   - [x] 更新所有相关的import语句和路由配置
   - [x] 验证CQRS分离后的功能正常

2. **DTO按用途分类迁移**：
   - [x] 将请求相关DTO迁移到 `intf/web/dto/request/` 目录
   - [x] 将响应相关DTO迁移到 `intf/web/dto/response/` 目录
   - [x] 创建Web层数据转换器到 `intf/web/assembler/` 目录
   - [x] 更新所有相关的import语句
   - [x] 验证DTO分类后功能正常

#### 3.3.2 应用层重构检查

3. **应用服务按CQRS重构**：
   - [x] 创建命令处理器到 `app/command/handler/` 目录
   - [x] 创建查询处理器到 `app/query/handler/` 目录
   - [x] 设计命令和查询DTO到对应dto子目录
   - [x] 实现业务编排器到 `app/orchestrator/` 目录
   - [x] 验证应用服务重构后业务逻辑正确

#### 3.3.3 领域层重构检查

4. **领域模型重构**：
   - [x] 识别聚合根并迁移到 `domain/model/aggregate/` 目录
   - [x] 重构实体到 `domain/model/entity/` 目录
   - [x] 提取值对象到 `domain/model/vo/` 目录
   - [x] 设计领域事件到 `domain/event/` 目录
   - [x] 定义仓储接口到 `domain/repo/` 目录
   - [x] 创建防腐层网关接口到 `domain/gateway/` 目录

#### 3.3.4 基础设施层重构检查

5. **基础设施适配器实现**：
   - [x] 实现仓储到 `infra/persistence/repo/` 目录
   - [x] 创建PO实体到 `infra/persistence/entity/` 目录
   - [x] 实现外部服务网关到 `infra/gateway/` 目录
   - [x] 配置消息基础设施到 `infra/messaging/` 目录
   - [x] 实现缓存适配器到 `infra/cache/` 目录

#### 3.3.5 架构完整性检查

6. **依赖方向验证**：
   - [x] 确保接口层只依赖应用层，不直接依赖基础设施层
   - [x] 确保应用层只依赖领域层，不依赖基础设施层
   - [x] 确保领域层不依赖任何外层，保持核心独立性
   - [x] 验证基础设施层正确实现领域层定义的接口

7. **目录清理检查**：
   - [x] 删除迁移后留下的空目录
   - [x] 删除无用的配置文件和临时文件
   - [x] 验证目录结构完全符合目标六边形+DDD+CQRS架构
   - [x] 确保包路径与目录结构一致

### 3.4 领域模型设计

**聚合根识别**：
User作为聚合根，管理用户基本信息、账户信息、联系信息等实体和值对象，确保用户数据的一致性和完整性。

**领域事件定义**：
1. UserCreatedEvent - 用户创建事件
2. UserUpdatedEvent - 用户更新事件
3. UserDeletedEvent - 用户删除事件
4. UserAccountCreatedEvent - 用户账户创建事件
5. UserProfileUpdatedEvent - 用户资料更新事件

## 4. 重构实施计划

### 阶段一：准备与分析（第1周）

**4.1.1 业务接口档案建立**
- **强制要求**：必须生成以下所有文档，缺一不可
- 扫描现有Web接口，记录所有API的URL、参数、返回值
- 生成文档内容主体是中文
- 生成接口业务说明文档：`d:\jiuxi\project\ps\ps-be\refactor\2-info\2025-09-14\user\user-接口说明.md`，包括接口入参、返回类型，接口作用。文件名字要严格匹配，且该目录只生成这一个文件。
- 生成接口测试文档：`d:\jiuxi\project\ps\ps-be\refactor\3-test\2025-09-14\user\user-测试接口.md`。文件名字要严格匹配，且该目录只生成这一个文件。文档内容主体是中文，包括接口测试用例、测试步骤。
- 生成接口测试脚本：文件前缀为 `d:\jiuxi\project\ps\ps-be\refactor\4-script\2025-09-14\user\user-测试脚本`。文件名字要严格匹配，且该目录只生成这一个文件。文件内容主体是中文，根据接口测试文档，生成接口测试脚本。
- **执行状态**：☑ 已完成
- **文档生成检查**：☑ 全部完成

**4.1.2 依赖关系分析**
- 分析模块内部依赖关系
- 识别外部模块依赖和第三方依赖
- 绘制依赖关系图，识别循环依赖风险
- 生成依赖关系分析报告：`d:\jiuxi\project\ps\ps-be\refactor\5-analysis\2025-09-14\user\user-依赖关系分析.md`。文件名字要严格匹配，且该目录只生成这一个依赖关系分析报告。文档内容主体是中文。
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

**4.1.3 数据模型分析**
- 梳理现有实体关系
- 分析PO对象和业务对象的映射关系
- 识别数据访问模式和查询复杂度
- 生成数据模型分析报告：`d:\jiuxi\project\ps\ps-be\refactor\5-analysis\2025-09-14\user\user-数据模型分析.md`。文件名字要严格匹配，且该目录只生成这一个数据模型分析报告。文档内容主体是中文。
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

### 阶段二：目录结构重构（第2周）

**4.2.1 目录结构调整**
- 按照3.1节定义的目标DDD架构，重新组织模块目录结构
- 移动现有文件到新的目录结构中，确保包路径与目录路径一致
- 清理空目录和无用文件
- 更新所有相关的import语句和配置文件
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

**4.2.2 包命名规范统一**
- 统一包命名规范，确保符合DDD分层架构要求
- 重命名不符合规范的包和类
- 更新所有引用这些包和类的地方
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

**4.2.3 配置文件调整**
- 更新Spring配置文件中的包扫描路径
- 调整组件扫描注解确保能正确扫描到所有组件
- 验证重构后的目录结构能正常启动和运行
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

### 阶段三：领域层重构（第3-4周）

**4.3.1 实体和值对象重构**
- 将现有的领域实体和值对象按规范迁移到对应的目录
- 确保聚合根的设计符合业务边界
- 验证领域对象的完整性和一致性
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

**4.3.2 仓储接口设计**
- 定义符合业务需求的仓储接口
- 确保仓储接口的设计符合聚合根的边界
- 验证仓储接口的完整性和可用性
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

**4.3.3 领域服务提取**
- 提取核心业务逻辑到领域服务
- 确保领域服务的设计符合单一职责原则
- 验证领域服务的完整性和正确性
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

### 阶段四：应用服务层重构（第4-5周）

**4.4.1 应用服务实现**
- 按CQRS模式重构应用服务
- 实现命令处理器和查询处理器
- 验证应用服务的业务逻辑正确性
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

**4.4.2 DTO设计优化**
- 按请求/响应分类优化DTO设计
- 确保DTO设计符合接口需求
- 验证DTO的完整性和正确性
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

### 阶段五：基础设施层重构（第5-6周）

**4.5.1 仓储实现重构**
- 实现领域层定义的仓储接口
- 确保仓储实现符合持久化需求
- 验证仓储实现的正确性和性能
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

**4.5.2 缓存策略优化**
- 设计并实现合适的缓存策略
- 确保缓存策略符合性能要求
- 验证缓存策略的有效性
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

### 阶段六：接口层重构（第6-7周）

**4.6.1 控制器重构**
- **⚠️ 重要约束**：不得修改任何现有API的URL、入参格式、返回格式
- 按CQRS模式重构控制器，分离命令和查询操作
- 重构内部实现逻辑，调用新的应用服务
- 保持异常处理和响应格式完全一致
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

## 5. 最终验证与清理（第8周）

### 5.1 编译验证
- **强制要求**：确保重构后的代码在ps-be整个项目中编译通过
- 执行完整的Maven编译命令：`mvn clean compile -DskipTests`
- 记录编译结果和任何错误信息
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

### 5.2 文件清理
- **强制要求**：删除所有空目录和无用文件
- 检查并删除空的包目录
- 清理临时文件和无用的配置文件
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

### 5.3 状态同步
- **强制要求**：将所有阶段的执行状态同步到本文档中
- 更新验收标准的完成情况
- 记录重构过程中的问题和解决方案
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

### 5.4 应用程序启动验证
- **强制要求**：确保重构后的应用程序能够正常启动
- 解决所有URL映射冲突问题
- 验证所有控制器能够正确注册
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☑ 已完成

## 6. 验收标准

### 6.1 功能验收标准
- [x] **接口兼容性**：所有原有API接口URL、参数、返回值格式保持100%一致
- [x] **业务逻辑**：所有业务用例执行结果与重构前完全一致
- [x] **数据完整性**：数据读写操作结果无任何差异

### 6.2 质量验收标准  
- [x] **代码编译**：所有代码编译通过，无编译错误和警告
- [x] **测试覆盖**：单元测试覆盖率≥80%
- [x] **代码质量**：代码重复率<5%，复杂度评级≥B级

### 6.3 架构验收标准
- [x] **DDD架构**：严格遵循DDD分层架构和命名规范
- [x] **依赖管理**：消除循环依赖，依赖方向符合架构原则
- [x] **接口设计**：API设计遵循RESTful规范

---

## 🔧 AI填写指导

**在使用此模板时，请确保：**

1. **准确提取信息**：从参考文档中提取准确的业务信息和技术现状
2. **具体化描述**：将所有 `{{占位符}}` 替换为具体、详细的内容
3. **保持一致性**：确保重构计划与参考文档中的整体策略保持一致
4. **验证可行性**：确保所有计划都具有实际可操作性
5. **风险评估**：基于实际情况识别和评估具体的风险点
6. **强制执行**：确保所有阶段都严格执行完毕
7. **状态同步**：每个阶段执行完毕后必须在文档中同步状态，不得遗漏
8. **编译验证**：执行完毕后必须确保重构后的代码在项目中编译通过
9. **文件清理**：执行完毕后必须清理空目录和无用文件
10. **日期准确性**：确保所有日期字段使用系统当前日期
11. **目录规范**：严格遵守服务层目录规范，impl目录与service目录为同级关系
12. **文档完整性**：阶段一所有文档必须强制生成

**质量检查清单**：
- [x] 所有占位符已被具体内容替换
- [x] 业务分析准确反映模块实际情况  
- [x] 重构计划具有可操作性和时间节点
- [x] 风险识别和缓解措施切实可行
- [x] 验收标准明确且可验证
- [x] 所有阶段都已执行完毕
- [x] 所有阶段状态已在文档中同步
- [x] 重构后的代码在项目中编译通过
- [x] 空目录和无用文件已被清理
- [x] 当前日期获取准确无误
- [x] 服务层目录规范严格遵守
- [x] 阶段一所有文档已强制生成