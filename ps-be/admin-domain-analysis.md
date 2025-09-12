# Admin模块业务域分析报告

## 结构概览
- **总文件数**: 348个Java文件
- **主要包结构**: autoconfig, bean, constant, core, domain
- **核心层结构**: bean(entity/query/vo), controller, service, mapper, listener, config

## 业务域分类方案

### 1. 用户管理域 (User Domain)
**责任范围**: 用户账号、个人信息、认证授权

**迁移映射**:
```
Controllers:
- TpAccountController.java                     → module/user/interface/web/
- TpPersonBasicinfoController.java            → module/user/interface/web/
- CommonPersonBasicinfoController.java        → module/user/interface/web/
- KeycloakAccountSyncTestController.java      → module/user/interface/web/

Services:
- TpAccountService.java                        → module/user/application/service/
- TpPersonBasicinfoService.java               → module/user/application/service/
- PersonAccountService.java                   → module/user/application/service/
- KeycloakSyncService.java                    → module/user/application/service/

Entities & VOs:
- TpAccount.java                              → module/user/domain/entity/
- TpPersonBasicinfo.java                      → module/user/domain/entity/
- TpAccountExinfo.java                        → module/user/domain/entity/
- TpAccountThird.java                         → module/user/domain/entity/
- TpKeycloakAccount.java                      → module/user/domain/entity/

Mappers:
- TpAccountMapper.java                        → module/user/infrastructure/persistence/
- TpPersonBasicinfoMapper.java               → module/user/infrastructure/persistence/
- TpKeycloakAccountMapper.java               → module/user/infrastructure/persistence/
```

### 2. 组织架构域 (Organization Domain)
**责任范围**: 部门管理、企业信息、人员部门关系

**迁移映射**:
```
Controllers:
- TpDeptBasicinfoController.java              → module/organization/interface/web/
- TpEntBasicinfoController.java               → module/organization/interface/web/
- OrgTreeChangeHistoryController.java         → module/organization/interface/web/

Services:
- TpDeptBasicinfoService.java                 → module/organization/application/service/
- TpEntBasicinfoService.java                  → module/organization/application/service/
- OrgTreeChangeHistoryService.java           → module/organization/application/service/

Entities & VOs:
- TpDeptBasicinfo.java                        → module/organization/domain/entity/
- TpDeptExinfo.java                           → module/organization/domain/entity/
- TpEntBasicinfo.java                         → module/organization/domain/entity/
- TpPersonDept.java                           → module/organization/domain/entity/

Mappers:
- TpDeptBasicinfoMapper.java                  → module/organization/infrastructure/persistence/
- TpEntBasicinfoMapper.java                   → module/organization/infrastructure/persistence/
```

### 3. 系统管理域 (System Domain)
**责任范围**: 系统配置、参数管理、操作日志、字典管理

**迁移映射**:
```
Controllers:
- TpSystemConfigController.java               → module/system/interface/web/
- TpParameterConfigController.java            → module/system/interface/web/
- TpOperateLogController.java                 → module/system/interface/web/
- TpDictionaryController.java                 → module/system/interface/web/
- LoggerLevelController.java                  → module/system/interface/web/

Services:
- TpSystemConfigService.java                  → module/system/application/service/
- TpParameterConfigService.java               → module/system/application/service/
- TpOperateLogService.java                    → module/system/application/service/
- TpDictionaryService.java                    → module/system/application/service/

Entities & VOs:
- TpSystemConfig.java                         → module/system/domain/entity/
- TpParameterConfig.java                      → module/system/domain/entity/
- TpOperateLog.java                           → module/system/domain/entity/
- TpDictionary.java                           → module/system/domain/entity/
```

### 4. 权限管理域 (Authorization Domain)
**责任范围**: 角色管理、菜单权限、数据权限

**迁移映射**:
```
Controllers:
- TpRoleController.java                       → module/authorization/interface/web/
- TpMenuController.java                       → module/authorization/interface/web/
- TpDataPermissionsController.java            → module/authorization/interface/web/

Services:
- TpRoleService.java                          → module/authorization/application/service/
- TpMenuService.java                          → module/authorization/application/service/
- TpDataPermissionsService.java               → module/authorization/application/service/

Entities & VOs:
- TpRole.java                                 → module/authorization/domain/entity/
- TpMenu.java                                 → module/authorization/domain/entity/
- TpRoleMenu.java                             → module/authorization/domain/entity/
- TpPersonRole.java                           → module/authorization/domain/entity/
- TpDataPermissions.java                      → module/authorization/domain/entity/
```

### 5. 消息通知域 (Message Domain)
**责任范围**: 消息管理、通知、短信服务

**迁移映射**:
```
Controllers:
- TpMessageController.java                    → module/message/interface/web/
- SmsControllerApp.java                       → module/message/interface/web/

Services:
- TpMessageService.java                       → module/message/application/service/
- TpMessageReadService.java                   → module/message/application/service/
- TpSmsSendService.java                       → module/message/application/service/
- EmailService.java                           → module/message/application/service/

Entities & VOs:
- TpMessage.java                              → module/message/domain/entity/
- TpMessageRead.java                          → module/message/domain/entity/
```

### 6. 文件管理域 (File Domain)
**责任范围**: 文件上传、下载、附件管理

**迁移映射**:
```
Controllers:
- FileController.java                         → module/file/interface/web/
- FileControllerApp.java                     → module/file/interface/web/
- FileThirdController.java                   → module/file/interface/web/

Services:
- FileService.java                            → module/file/application/service/
- FileExpService.java                         → module/file/application/service/
- TpAttachinfoService.java                    → module/file/application/service/

Entities & VOs:
- TpAttachinfo.java                           → module/file/domain/entity/
```

### 7. 集成服务域 (Integration Domain)
**责任范围**: 第三方集成、Keycloak集成、测试控制器

**迁移映射**:
```
Controllers:
- KeycloakTestController.java                 → module/integration/interface/web/
- MigrationController.java                    → module/integration/interface/web/
- SqlExecutorController.java                  → module/integration/interface/web/
- TestEmailController.java                    → module/integration/interface/web/

Services:
- QrCodeService.java                          → module/integration/application/service/
- TpKeycloakAccountService.java               → module/integration/application/service/
```

## 共享组件分类

### 1. 通用实体 → shared/common/base/entity/
```
- TestCity.java
- TpCity.java  
- TpTrace.java
- TpTenant.java
- TpAgent.java
- TpAgentDeal.java
```

### 2. 通用VO → shared/common/base/vo/
```
- CJsonVO.java
- TpCityVO.java
- TpTraceVO.java
- TpTenantVO.java
```

### 3. 枚举常量 → shared/common/enums/
```
- MenuTypeEnum.java
- OpertionTypeEnum.java
- OSEnum.java
```

### 4. 配置组件 → shared/config/
```
- RestTemplateConfig.java
- DatabaseInitializer.java
```

### 5. 基础设施组件 → shared/infrastructure/
```
Events & Listeners:
- 所有Event和Listener类 → shared/infrastructure/messaging/
- 事件服务类 → shared/infrastructure/messaging/service/

Utilities:
- CommonDataPermissionsUtil.java → shared/infrastructure/util/
- IpAccessControlUtil.java → shared/infrastructure/util/

Interceptors:
- IpAccessControlInterceptor.java → shared/infrastructure/web/interceptor/
```

## 依赖关系分析

### 核心依赖关系
1. **用户域** ↔ **组织域**: 人员部门关系
2. **权限域** → **用户域**: 用户角色分配
3. **权限域** → **组织域**: 部门权限控制
4. **系统域** → 所有域: 配置参数服务
5. **消息域** → **用户域**: 消息推送目标用户
6. **文件域** → **用户域**: 用户文件关联

### 迁移风险评估
- **低风险**: System, Message, File域 (相对独立)
- **中风险**: Authorization域 (多域交互)
- **高风险**: User, Organization域 (核心业务，强耦合)

## 建议实施顺序
1. **阶段1**: File, Message域 (独立性强)
2. **阶段2**: System域 (基础配置)
3. **阶段3**: Authorization域 (权限控制)
4. **阶段4**: Organization域 (组织架构)
5. **阶段5**: User域 (用户管理)
6. **阶段6**: Integration域 (集成服务)
7. **阶段7**: 共享组件迁移和清理

## 预估工作量
- **总迁移组件**: 348个文件
- **预计耗时**: 15-20工作日
- **每域平均**: 2-3工作日
- **风险缓冲**: 20%工作量用于问题修复