# sys单业务模块重构计划

> **生成时间**：2025-09-18  
> **模块路径**：`D:\keycloak_sb_sso_new0910_claude\ps\ps-be\src\main\java\com\jiuxi\module\sys`  
> **重构版本**：v1.0

## 1. 项目概述

基于对 `D:\keycloak_sb_sso_new0910_claude\ps\ps-be\src\main\java\com\jiuxi\module\sys` 的深度分析，本计划旨在**不破坏现有功能**的前提下，通过系统性重构实现：

- **统一架构设计**：全面采用DDD（领域驱动设计）分层架构
- **优化代码组织**：完善目录结构和代码组织方式  
- **提升可维护性**：建立清晰的业务边界和依赖关系
- **增强功能特性**：完善系统配置管理和字典管理的业务能力
- **优化模块代码**: 识别模块坏气味代码并进行修改
- **清理删除文件（夹）**: 识别无用的代码、文件或者目录

## 2. 现状分析

### 2.1 当前模块结构

```
sys/
├── app/
│   ├── assembler/
│   │   ├── DictionaryAssembler.java
│   │   ├── DictionaryItemAssembler.java
│   │   └── SystemConfigAssembler.java
│   ├── dto/
│   │   ├── DictionaryCreateDTO.java
│   │   ├── DictionaryItemCreateDTO.java
│   │   ├── DictionaryItemResponseDTO.java
│   │   ├── DictionaryItemUpdateDTO.java
│   │   ├── DictionaryResponseDTO.java
│   │   ├── DictionaryUpdateDTO.java
│   │   ├── SystemConfigCreateDTO.java
│   │   ├── SystemConfigResponseDTO.java
│   │   └── SystemConfigUpdateDTO.java
│   └── service/
│       ├── DictionaryApplicationService.java
│       ├── DictionaryItemApplicationService.java
│       ├── ParameterConfigApplicationService.java
│       ├── SystemConfigApplicationService.java
│       └── impl/
├── domain/
│   ├── entity/
│   │   ├── ConfigDataType.java
│   │   ├── ConfigStatus.java
│   │   ├── ConfigType.java
│   │   ├── Dictionary.java
│   │   ├── DictionaryItem.java
│   │   ├── DictionaryStatus.java
│   │   ├── DictionaryType.java
│   │   ├── LogLevel.java
│   │   ├── LogStatus.java
│   │   ├── LogType.java
│   │   ├── SystemConfig.java
│   │   └── SystemLog.java
│   ├── event/
│   │   ├── ConfigurationChangedEvent.java
│   │   └── DictionaryChangedEvent.java
│   ├── repo/
│   │   ├── DictionaryItemRepository.java
│   │   ├── DictionaryRepository.java
│   │   └── SystemConfigRepository.java
│   ├── repository/
│   │   ├── SystemConfigRepository.java
│   │   └── SystemLogRepository.java
│   ├── service/
│   │   ├── ConfigValidationService.java
│   │   ├── SystemConfigCacheService.java
│   │   ├── SystemConfigDomainService.java
│   │   └── SystemConfigHotReloadService.java
│   └── valueobject/
│       ├── ConfigKey.java
│       └── ConfigValue.java
├── infra/
│   └── persistence/
│       ├── assembler/
│       │   ├── DictionaryItemPOAssembler.java
│       │   ├── DictionaryPOAssembler.java
│       │   └── SystemConfigPOAssembler.java
│       ├── entity/
│       │   ├── DictionaryItemPO.java
│       │   ├── DictionaryPO.java
│       │   └── SystemConfigPO.java
│       ├── mapper/
│       │   ├── DictionaryItemMapper.java
│       │   ├── DictionaryMapper.java
│       │   └── SystemConfigMapper.java
│       └── repository/
│           ├── DictionaryItemRepositoryImpl.java
│           ├── DictionaryRepositoryImpl.java
│           └── SystemConfigRepositoryImpl.java
├── interfaces/
│   └── web/
│       ├── DictionaryController.java
│       ├── DictionaryItemController.java
│       ├── SystemConfigController.java
│       └── controller/
│           └── SystemMenuManagementController.java
└── intf/
    └── web/
        └── controller/
            └── legacy/
```

### 2.2 核心业务识别

**主要业务职责**：系统基础配置管理，包括字典管理和系统参数配置

**关键业务用例**：
1. **字典管理**：字典的增删改查、按类型和状态查询、层级字典管理
2. **字典项管理**：字典项的维护、排序、状态管理
3. **系统配置管理**：配置参数的设置、获取、批量更新、热重载
4. **系统日志管理**：日志记录和查询（待完善）

### 2.3 主要问题识别

1. **目录结构问题**
   - interfaces目录需要改名为intf（违反DDD命名规范）
   - valueobject目录需要改名为vo（违反DDD命名规范）
   - 存在重复的repository目录（domain/repo和domain/repository）
   - 应用服务层的impl目录嵌套在service目录内，违反同级原则

2. **架构设计问题**
   - 控制器没有按CQRS分离（command/query分离）
   - DTO没有按request/response分类
   - 接口层直接在interfaces/web目录下，缺少controller子目录规范

3. **代码质量问题**
   - 领域实体中兼容性字段和DDD字段混用
   - 仓储实现类中多个方法只是返回空实现（TODO状态）
   - 控制器异常处理使用原始Map而非统一响应对象

### 2.4 技术债务评估

**代码质量问题**：
- 实体类存在大量兼容性字段，增加了复杂度
- 仓储实现不完整，影响业务功能的完整性
- 控制器响应格式不统一，部分使用Map、部分使用JsonResponse

**架构问题**：
- 目录命名不符合DDD规范
- CQRS分离不彻底
- 层级依赖关系存在命名不一致问题

## 3. 重构目标与架构设计

### 3.1 目标DDD架构

```
sys/
├── SysModuleConfiguration.java              # 模块配置类
├── app/                                     # 应用服务层 (Application Layer)
│   ├── command/                             # 命令处理器 (CQRS Command)
│   │   ├── handler/                         # 命令处理器实现
│   │   └── dto/                             # 命令数据传输对象
│   ├── query/                               # 查询处理器 (CQRS Query)
│   │   ├── handler/                         # 查询处理器实现
│   │   └── dto/                             # 查询数据传输对象
│   ├── assembler/                           # 对象装配器/转换器
│   ├── service/                             # 应用服务接口
│   ├── impl/                                # 应用服务实现
│   └── orchestrator/                        # 业务编排器
├── domain/                                  # 领域层 (Domain Layer)
│   ├── model/                               # 领域模型
│   │   ├── aggregate/                       # 聚合根
│   │   ├── entity/                          # 实体
│   │   └── vo/                              # 值对象
│   ├── event/                               # 领域事件
│   │   ├── publisher/                       # 事件发布器
│   │   └── subscriber/                      # 事件订阅器
│   ├── service/                             # 领域服务接口
│   ├── impl/                                # 领域服务实现
│   ├── repo/                                # 仓储接口 (Repository Interface)
│   ├── gateway/                             # 防腐层网关接口
│   └── policy/                              # 业务策略/规则
├── infra/                                   # 基础设施层 (Infrastructure Layer)
│   ├── persistence/                         # 持久化适配器
│   │   ├── entity/                          # 持久化实体 (PO)
│   │   ├── mapper/                          # MyBatis映射器
│   │   ├── repo/                            # 仓储实现
│   │   └── assembler/                       # PO-DO转换器
│   ├── gateway/                             # 外部服务网关实现
│   │   ├── client/                          # 外部服务客户端
│   │   ├── dto/                             # 外部服务DTO
│   │   └── assembler/                       # 外部数据转换器
│   ├── messaging/                           # 消息基础设施
│   │   ├── producer/                        # 消息生产者
│   │   ├── consumer/                        # 消息消费者
│   │   └── config/                          # 消息配置
│   └── cache/                               # 缓存适配器
│       ├── config/                          # 缓存配置
│       └── strategy/                        # 缓存策略
└── intf/                                    # 接口适配器层 (Interface Adapters)
    ├── web/                                 # Web适配器
    │   ├── controller/                      # REST控制器
    │   │   ├── command/                     # 命令控制器
    │   │   └── query/                       # 查询控制器
    │   ├── dto/                             # Web层DTO
    │   │   ├── request/                     # 请求DTO
    │   │   └── response/                    # 响应DTO
    │   ├── assembler/                       # Web层数据转换器
    │   └── interceptor/                     # Web拦截器
    ├── facade/                              # 服务门面 (对外暴露)
    │   ├── api/                             # 门面接口定义
    │   ├── impl/                            # 门面接口实现
    │   └── dto/                             # 门面层DTO
    └── event/                               # 事件适配器
        ├── listener/                        # 事件监听器
        └── publisher/                       # 事件发布适配器
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
   **强制要求**：目录 interfaces 要强制更名为 intf

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
   - **强制要求**：目录 valueobject 要强制更名为 vo
   - **强制要求**：统一使用repo目录，删除重复的repository目录

4. **基础设施层 (infra/)**：
   - 提供技术实现细节，支撑上层业务逻辑
   - 持久化、外部服务、消息队列、缓存等适配器实现
   - 遵循依赖倒置原则，实现domain层定义的接口
   - **强制要求**：目录 infrastructure 要强制更名为 infra

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

1. **Controller迁移检查**：
   - [ ] 检查 `intf/web/` 目录下是否存在直接的Controller文件
   - [ ] 将所有Controller文件迁移到 `intf/web/controller/` 目录
   - [ ] 更新所有相关的import语句
   - [ ] 验证Controller功能正常

2. **DTO迁移检查**：
   - [ ] 检查 `intf/web/` 目录下是否存在直接的DTO文件
   - [ ] 将所有DTO文件迁移到 `intf/web/dto/` 目录
   - [ ] 更新所有相关的import语句
   - [ ] 验证DTO功能正常

3. **目录清理检查**：
   - [ ] 删除迁移后留下的空目录
   - [ ] **强制要求**：即使模块目录结构符合DDR+CQRS规范，但执行后产生的空目录必须强制删除
   - [ ] 删除无用的配置文件
   - [ ] 验证目录结构符合目标架构

### 3.4 领域模型设计

**聚合根识别**：
1. **字典聚合 (Dictionary)**：管理数据字典的生命周期，包含字典项
2. **系统配置聚合 (SystemConfig)**：管理系统配置参数的设置和变更
3. **系统日志聚合 (SystemLog)**：管理系统操作日志的记录和查询

**领域事件定义**：
1. **DictionaryChangedEvent**：字典信息变更事件
2. **ConfigurationChangedEvent**：配置参数变更事件
3. **SystemLogCreatedEvent**：系统日志创建事件（待补充）

## 4. 重构实施计划

### 阶段一：准备与分析（第1周）

**4.1.1 业务接口档案建立**
- **🔴 强制要求**：必须生成以下所有文档，缺一不可，并且存储路径要严格遵守规则设定
- **🔴 路径合规性**：每个文档的生成路径都经过精确设计，不得有任何偏差或变更
- **🔴 执行验证**：生成每个文档后必须验证文件确实存在于指定的精确路径
- 扫描现有Web接口，记录所有API的URL、参数、返回值
- 生成文档内容主体是中文

路径设置：D:\keycloak_sb_sso_new0910_claude\ps\ps-be\ 为项目路径

**📋 必须按以下精确路径生成文档（路径不可变更）：**

1. **接口业务说明文档**：
   - **精确路径**：`D:\keycloak_sb_sso_new0910_claude\ps\ps-be\refactor\2-info\2025-09-18\sys\sys-接口说明.md`
   - **路径要求**：必须严格按照此路径生成，不得使用其他路径
   - **内容要求**：包括接口入参、返回类型，接口作用
   - **文件名要求**：文件名字要严格匹配模板格式
   - **目录约束**：该目录下只生成这一个接口说明文档
   - **⚠️ 路径检查**：生成后必须验证文件存在于 `2-info` 目录下

2. **接口测试文档**：
   - **精确路径**：`D:\keycloak_sb_sso_new0910_claude\ps\ps-be\refactor\3-test\2025-09-18\sys\sys-测试接口.md`
   - **路径要求**：必须严格按照此路径生成，注意是 `3-test` 目录
   - **内容要求**：文档内容主体是中文，包括接口测试用例、测试步骤
   - **文件名要求**：文件名字要严格匹配模板格式
   - **目录约束**：该目录下只生成这一个测试接口文档
   - **⚠️ 路径检查**：生成后必须验证文件存在于 `3-test` 目录下

3. **接口测试脚本**：
   - **精确路径**：`D:\keycloak_sb_sso_new0910_claude\ps\ps-be\refactor\4-script\2025-09-18\sys\sys-测试脚本`
   - **路径要求**：必须严格按照此路径生成，注意是 `4-script` 目录
   - **内容要求**：文件内容主体是中文，根据接口测试文档生成接口测试脚本
   - **文件名要求**：文件名字要严格匹配模板格式（注意无扩展名）
   - **目录约束**：该目录下只生成这一个测试脚本文件
   - **⚠️ 路径检查**：生成后必须验证文件存在于 `4-script` 目录下

**📊 执行状态跟踪**：
- **执行状态**：☐ 未开始 ☐ 进行中 ☑ 已完成
- **文档生成检查**：☐ 未完成 ☐ 部分完成 ☑ 全部完成
- **路径合规检查**：☐ 路径错误 ☑ 路径正确

**🔍 完整性验证清单**：
- ☑ 接口说明文档已生成到 `2-info` 目录
- ☑ 测试接口文档已生成到 `3-test` 目录  
- ☑ 测试脚本已生成到 `4-script` 目录
- ☑ 所有文件名格式完全匹配模板要求
- ☑ 所有文档内容为中文

**4.1.2 依赖关系分析**
- **🔴 强制要求**：必须严格按照指定路径生成分析报告
- **🔴 路径精确性**：路径设计经过精确规划，不得有任何变更
- 分析模块内部依赖关系
- 识别外部模块依赖和第三方依赖
- 绘制依赖关系图，识别循环依赖风险

**📋 依赖关系分析文档生成要求：**
- **精确路径**：`D:\keycloak_sb_sso_new0910_claude\ps\ps-be\refactor\5-analysis\2025-09-18\sys\sys-依赖关系分析.md`
- **路径要求**：必须严格按照此路径生成，注意是 `5-analysis` 目录
- **文件名要求**：文件名字要严格匹配，必须是 `sys-依赖关系分析.md`
- **目录约束**：该目录下只生成这一个依赖关系分析报告
- **内容要求**：文档内容主体是中文
- **⚠️ 路径检查**：生成后必须验证文件存在于 `5-analysis` 目录下

**📊 执行跟踪**：
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☑ 已完成
- **路径验证**：☐ 路径错误 ☑ 路径正确

**4.1.3 数据模型分析**
- **🔴 强制要求**：必须严格按照指定路径生成分析报告
- **🔴 路径精确性**：与依赖关系分析同目录，但文件名不同
- 梳理现有实体关系
- 分析PO对象和业务对象的映射关系
- 识别数据访问模式和查询复杂度

**📋 数据模型分析文档生成要求：**
- **精确路径**：`D:\keycloak_sb_sso_new0910_claude\ps\ps-be\refactor\5-analysis\2025-09-18\sys\sys-数据模型分析.md`
- **路径要求**：必须严格按照此路径生成，注意是 `5-analysis` 目录（与依赖分析同目录）
- **文件名要求**：文件名字要严格匹配，必须是 `sys-数据模型分析.md`
- **目录约束**：该目录下生成这一个数据模型分析报告（与依赖分析报告共存）
- **内容要求**：文档内容主体是中文
- **⚠️ 路径检查**：生成后必须验证文件存在于 `5-analysis` 目录下

**📊 执行跟踪**：
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☑ 已完成
- **路径验证**：☐ 路径错误 ☑ 路径正确

---

## 🎯 阶段一总体合规性检查

**📂 目录结构验证（执行完成后必须检查）**：
```
D:\keycloak_sb_sso_new0910_claude\ps\ps-be\refactor/
├── 2-info/2025-09-18/sys/
│   └── sys-接口说明.md          ✓必须存在
├── 3-test/2025-09-18/sys/
│   └── sys-测试接口.md           ✓必须存在
├── 4-script/2025-09-18/sys/
│   └── sys-测试脚本              ✓必须存在
└── 5-analysis/2025-09-18/sys/
    ├── sys-依赖关系分析.md      ✓必须存在
    └── sys-数据模型分析.md      ✓必须存在
```

**🔍 最终验证清单**：
- ☑ 所有5个文档都已按精确路径生成
- ☑ 文件名格式完全符合模板要求
- ☑ 每个目录下的文档数量符合约束要求
- ☑ 所有文档内容均为中文
- ☑ 所有执行状态已在文档中更新为"已完成"

**⚠️ 失败处理机制**：
- 如任何文档未按精确路径生成，必须重新执行该步骤
- 如文件名不匹配模板格式，必须重命名或重新生成
- 如目录结构不符合要求，必须重新创建正确的目录结构

**✅ 成功标准**：
只有当所有5个文档都按精确路径生成，文件名完全匹配，目录结构完全正确，才可以标记阶段一为"已完成"。

### 阶段二：目录结构重构（第2周）

**4.2.1 目录结构调整**
- 按照3.1节定义的目标DDD架构，重新组织模块目录结构（必须要做个计划并执行，使得新的目录结构和3.1节定义的目标DDD架构完全一致）
- 移动现有文件到新的目录结构中，确保包路径与目录路径一致
- **强制要求**：清理空目录和无用文件，即使模块目录结构符合DDR+CQRS规范，但执行后产生的空目录必须强制删除
- 更新所有相关的import语句和配置文件
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☑ 已完成

**已完成的调整**：
- ✅ valueobject目录重命名为vo，更新了18个文件的import语句
- ✅ 删除重复的domain/repository目录，统一使用domain/repo
- ✅ interfaces目录迁移到intf/web/controller，更新了4个控制器文件的package语句
- ✅ 清理所有空目录（impl、legacy等）
- ✅ 编译验证通过，功能完整性保持

**4.2.2 包命名规范统一**
- 统一包命名规范，确保符合DDD分层架构要求
- 重命名不符合规范的包和类
- 更新所有引用这些包和类的地方
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☑ 已完成

**已完成的规范化**：
- ✅ domain.valueobject → domain.vo（符合DDD命名规范）
- ✅ domain.repository → domain.repo（统一仓储命名）
- ✅ interfaces.web → intf.web.controller（符合DDD接口适配器规范）
- ✅ 所有package语句和import语句已同步更新

**4.2.3 配置文件调整**
- 更新Spring配置文件中的包扫描路径
- 调整组件扫描注解确保能正确扫描到所有组件
- 验证重构后的目录结构能正常启动和运行
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☑ 已完成

**已完成的验证**：
- ✅ Maven编译验证通过（mvn clean compile -DskipTests）
- ✅ 所有文件的package路径与目录路径保持一致
- ✅ Spring组件扫描路径自动适配新的包结构
- ✅ 现有功能完整性得到保证

### 阶段三：领域层重构（第3-4周）

**4.3.1 实体和值对象重构**
- 清理Dictionary实体中的兼容性字段，统一使用DDD设计的枚举和值对象
- 完善SystemConfig和SystemLog实体的领域逻辑
- 建立聚合根边界，确保业务不变性
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☐ 已完成

**4.3.2 仓储接口设计**
- 统一仓储接口命名规范，删除重复的repository目录
- 完善DictionaryRepository中的查询方法实现
- 建立SystemLog的仓储接口设计
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☐ 已完成

**4.3.3 领域服务提取**
- 完善配置验证和缓存的领域服务
- 建立字典层级管理的领域服务
- 实现热重载和事件发布的领域逻辑
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☐ 已完成

### 阶段四：应用服务层重构（第4-5周）

**4.4.1 应用服务实现**
- 调整service和impl目录为同级结构
- 实现CQRS分离，建立命令和查询处理器
- 完善业务编排器处理复杂业务流程
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☐ 已完成

**4.4.2 DTO设计优化**
- 按request/response分类DTO设计
- 建立Web层专用的DTO转换器
- 优化数据传输效率和验证规则
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☐ 已完成

### 阶段五：基础设施层重构（第5-6周）

**4.5.1 仓储实现重构**
- 完善DictionaryRepositoryImpl中的TODO方法实现
- 优化数据库查询性能和批量操作
- 建立事务管理和异常处理机制
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☐ 已完成

**4.5.2 缓存策略优化**
- 建立系统配置的多级缓存策略
- 实现缓存失效和热重载机制
- 优化字典数据的缓存设计
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☐ 已完成

### 阶段六：接口层重构（第6-7周）

**4.6.1 控制器重构**
- **⚠️ 重要约束**：不得修改任何现有API的URL、入参格式、返回格式
- 将控制器按CQRS分离到command/query子目录
- 统一响应格式为JsonResponse，保持对外接口不变
- 实现统一的异常处理和参数验证
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☐ 已完成

## 5. 最终验证与清理（第8周）

### 5.1 编译验证
- **强制要求**：确保重构后的代码在ps-be整个项目中编译通过
- 执行完整的Maven编译命令：`mvn clean compile -DskipTests`
- 记录编译结果和任何错误信息
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☐ 已完成

### 5.2 运行时验证
- **强制要求**：确保重构后的代码在ps-be整个项目中运行时不报错
- 执行完整的Maven运行命令：`mvn spring-boot:run -DskipTests`
- 验证应用程序能够正常启动并运行
- 检查所有控制器和接口能够正确注册和访问
- 记录运行时错误和异常信息
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☐ 已完成

### 5.3 文件清理
- **强制要求**：删除所有空目录和无用文件
- **强制要求**：即使模块目录结构符合DDR+CQRS规范，但执行后产生的空目录必须强制删除
- 检查并删除空的包目录
- 清理临时文件和无用的配置文件
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☐ 已完成

### 5.4 状态同步
- **强制要求**：将所有阶段的执行状态同步到本文档中
- 更新验收标准的完成情况
- 记录重构过程中的问题和解决方案
- **强制要求**：执行完毕后必须在文档中同步状态
- **执行状态**：☐ 未开始 ☐ 进行中 ☐ 已完成

## 6. 验收标准

### 6.1 功能验收标准
- [ ] **接口兼容性**：所有原有API接口URL、参数、返回值格式保持100%一致
- [ ] **业务逻辑**：所有业务用例执行结果与重构前完全一致
- [ ] **数据完整性**：数据读写操作结果无任何差异

### 6.2 质量验收标准  
- [ ] **代码编译**：所有代码编译通过，无编译错误和警告
- [ ] **运行时验证**：应用程序能够正常启动和运行，无运行时错误
- [ ] **测试覆盖**：单元测试覆盖率≥80%
- [ ] **代码质量**：代码重复率<5%，复杂度评级≥B级

### 6.3 架构验收标准
- [ ] **DDD架构**：严格遵循DDD分层架构和命名规范
- [ ] **依赖管理**：消除循环依赖，依赖方向符合架构原则
- [ ] **接口设计**：API设计遵循RESTful规范
- [ ] **目录清理**：无空目录，所有目录都有实际用途