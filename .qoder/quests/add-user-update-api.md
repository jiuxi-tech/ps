# 用户信息修改接口 - API管理配置设计

## 1. 需求概述

在API管理系统的第三方开放接口清单中，新增用户信息修改接口、密码修改接口和SSO同步接口的定义记录，使得这些接口可被第三方应用调用。

### 1.1 背景说明

系统已具备API管理能力，包含第三方应用管理、API定义管理、权限配置和调用日志记录等完整功能。目前仅开放了用户查询类接口（查询单个用户、用户列表查询、搜索用户），需要补充用户信息修改、密码修改和SSO同步能力。

### 1.2 功能目标

- 在API定义表中添加用户信息修改接口、密码修改接口和SSO同步接口的元数据
- 使这些接口可在第三方应用的权限配置页面中被选择和授权
- 确保接口符合开放API的安全标准（API Key验证、数据脱敏、调用日志等）
- 密码修改接口需要支持Keycloak多凭据密码同步
- SSO同步接口需要支持Keycloak多凭据（USERNAME、PHONE、IDCARD）同步

## 2. 接口定义

本次需要添加三个开放API接口：
1. 用户信息修改接口：修改用户基本信息（姓名、手机号、邮箱等）
2. 密码修改接口：重置用户登录密码
3. SSO同步接口：同步用户账号到Keycloak（多凭据）

### 2.1 用户信息修改接口

#### 2.1.1 基本信息

| 属性 | 取值 |
|------|------|
| API编码 | api_user_update |
| API名称 | 用户信息修改 |
| API路径 | /open-api/v1/users/{personId} |
| HTTP方法 | PUT |
| API分类 | 用户管理 |
| 描述 | 通过人员ID修改用户基本信息，支持修改姓名、手机号、邮箱、固定电话等字段 |

#### 2.1.2 接口特性

| 特性 | 取值 | 说明 |
|------|------|------|
| 是否敏感接口 | 是 (1) | 涉及用户信息变更，属于敏感操作 |
| 是否需要签名验证 | 否 (0) | 暂不启用签名机制，依赖API Key验证 |
| 状态 | 启用 (1) | 接口可用 |
| 排序序号 | 4 | 排列在现有3个用户查询接口之后 |

#### 2.1.3 请求参数

#### 路径参数
- personId: 人员ID（必填，19位字符串）

#### 请求头参数
- Authorization: 第三方应用的API密钥（必填，由拦截器验证）
- Content-Type: application/json

#### 请求体参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| personName | String | 否 | 姓名 |
| phone | String | 否 | 手机号 |
| email | String | 否 | 邮箱地址 |
| tel | String | 否 | 固定电话 |

#### 2.1.4 响应说明

#### 成功响应示例
```
{
  "success": true,
  "code": 0,
  "message": "操作成功",
  "data": "更新成功"
}
```

#### 失败响应示例
```
{
  "success": false,
  "code": -1,
  "message": "用户更新失败: 用户不存在"
}
```

#### HTTP状态码
- 200: 操作成功
- 401: API Key无效或缺失
- 403: 无权限访问该接口（应用未被授权或IP不在白名单）
- 404: 用户不存在
- 500: 服务器内部错误

### 2.2 密码修改接口

#### 2.2.1 基本信息

| 属性 | 取值 |
|------|------|
| API编码 | api_user_password_reset |
| API名称 | 用户密码重置 |
| API路径 | /open-api/v1/users/{personId}/reset-password |
| HTTP方法 | PUT |
| API分类 | 用户管理 |
| 描述 | 通过人员ID重置用户登录密码，支持Keycloak多凭据（用户名、手机号、身份证号）密码同步 |

#### 2.2.2 接口特性

| 特性 | 取值 | 说明 |
|------|------|------|
| 是否敏感接口 | 是 (1) | 涉及密码修改，属于高度敏感操作 |
| 是否需要签名验证 | 否 (0) | 暂不启用签名机制，依赖API Key验证 |
| 状态 | 启用 (1) | 接口可用 |
| 排序序号 | 5 | 排列在用户信息修改接口之后 |

#### 2.2.3 请求参数

##### 路径参数
- personId: 人员ID（必填，19位字符串）

##### 请求头参数
- Authorization: 第三方应用的API密钥（必填，由拦截器验证）
- Content-Type: application/json

##### 请求体参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newPassword | String | 是 | 新密码，需符合密码强度要求 |

密码强度要求：
- 长度至少8位
- 包含大写字母、小写字母、数字
- 可包含特殊字符

#### 2.2.4 响应说明

##### 成功响应示例
```
{
  "success": true,
  "code": 0,
  "message": "操作成功",
  "data": "密码重置成功"
}
```

##### 失败响应示例
```
{
  "success": false,
  "code": -1,
  "message": "密码重置失败: 用户不存在"
}
```

##### HTTP状态码
- 200: 操作成功
- 401: API Key无效或缺失
- 403: 无权限访问该接口（应用未被授权或IP不在白名单）
- 404: 用户不存在
- 500: 服务器内部错误

##### 特殊说明

密码修改会同步到Keycloak的多个凭据用户：
- USERNAME凭据用户：使用用户名登录的Keycloak账号
- PHONE凭据用户：使用手机号登录的Keycloak账号
- IDCARD凭据用户：使用身份证号登录的Keycloak账号

同步策略：
- 使用updatePasswordForAllCredentials方法进行密码同步
- 如果某个凭据对应的Keycloak用户不存在，不影响其他凭据的密码更新
- 所有成功的密码同步会记录在响应结果中

### 2.3 SSO同步接口

#### 2.3.1 基本信息

| 属性 | 取值 |
|------|------|
| API编码 | api_user_sso_sync |
| API名称 | 用户SSO同步 |
| API路径 | /open-api/v1/users/{personId}/sync-sso |
| HTTP方法 | POST |
| API分类 | 用户管理 |
| 描述 | 同步用户账号到Keycloak SSO系统，支持多凭据（USERNAME、PHONE、IDCARD）同步，等同于账号管理的“同步SSO”功能 |

#### 2.3.2 接口特性

| 特性 | 取值 | 说明 |
|------|------|------|
| 是否敏感接口 | 是 (1) | 涉及Keycloak用户创建/更新，属于敏感操作 |
| 是否需要签名验证 | 否 (0) | 暂不启用签名机制，依赖API Key验证 |
| 状态 | 启用 (1) | 接口可用 |
| 排序序号 | 6 | 排列在密码修改接口之后 |

#### 2.3.3 请求参数

##### 路径参数
- personId: 人员ID（必填，19位字符串）

##### 请求头参数
- Authorization: 第三方应用的API密钥（必填，由拦截器验证）
- Content-Type: application/json

##### 请求体参数

无需请求体参数，仅需路径参数personId。

#### 2.3.4 响应说明

##### 成功响应示例
```
{
  "success": true,
  "code": 0,
  "message": "操作成功",
  "data": {
    "syncStatus": "success",
    "totalCredentials": 3,
    "successCount": 3,
    "failureCount": 0,
    "details": [
      {
        "credentialType": "USERNAME",
        "credential": "test***",
        "success": true,
        "keycloakUserId": "kc-user-id-123",
        "message": "用户创建成功"
      },
      {
        "credentialType": "PHONE",
        "credential": "138****5678",
        "success": true,
        "keycloakUserId": "kc-user-id-456",
        "message": "用户已存在，更新成功"
      },
      {
        "credentialType": "IDCARD",
        "credential": "1101***********014",
        "success": true,
        "keycloakUserId": "kc-user-id-789",
        "message": "用户创建成功"
      }
    ]
  }
}
```

##### 失败响应示例
```
{
  "success": false,
  "code": -1,
  "message": "SSO同步失败: 用户不存在"
}
```

##### HTTP状态码
- 200: 操作成功
- 401: API Key无效或缺失
- 403: 无权限访问该接口（应用未被授权或IP不在白名单）
- 404: 用户不存在
- 500: 服务器内部错误

##### 特殊说明

SSO同步会创建或更新Keycloak中的多个凭据用户：
- USERNAME凭据用户：使用用户名登录的Keycloak账号
- PHONE凭据用户：使用手机号登录的Keycloak账号
- IDCARD凭据用户：使用身份证号登录的Keycloak账号

同步策略：
- 使用syncMultipleCredentials方法进行同步
- 如果凭据对应的Keycloak用户已存在，则更新用户信息
- 如果凭据对应的Keycloak用户不存在，则创建新用户
- 同步是异步操作，接口立即返回，实际同步在后台执行
- 每个凭据的同步结果会在details中详细记录

## 3. 数据库变更

### 3.1 tp_api_definition表新增记录

需要在API定义表中插入三条记录，分别对应用户信息修改接口、密码修改接口和SSO同步接口。

#### 3.1.1 用户信息修改接口记录

| 字段 | 值 |
|------|-----|
| api_id | 6 |
| api_code | api_user_update |
| api_name | 用户信息修改 |
| api_path | /open-api/v1/users/{personId} |
| http_method | PUT |
| category | 用户管理 |
| description | 通过人员ID修改用户基本信息，支持修改姓名、手机号、邮箱、固定电话等字段 |
| is_sensitive | 1 |
| require_secret | 0 |
| status | 1 |
| order_index | 4.0 |
| actived | 1 |
| creator | system |
| create_time | 当前时间（格式：yyyyMMddHHmmss） |
| updator | NULL |
| update_time | NULL |

#### 3.1.2 密码修改接口记录

| 字段 | 值 |
|------|-----|
| api_id | 7 |
| api_code | api_user_password_reset |
| api_name | 用户密码重置 |
| api_path | /open-api/v1/users/{personId}/reset-password |
| http_method | PUT |
| category | 用户管理 |
| description | 通过人员ID重置用户登录密码，支持Keycloak多凭据（用户名、手机号、身份证号）密码同步 |
| is_sensitive | 1 |
| require_secret | 0 |
| status | 1 |
| order_index | 5.0 |
| actived | 1 |
| creator | system |
| create_time | 当前时间（格式：yyyyMMddHHmmss） |
| updator | NULL |
| update_time | NULL |

#### 3.1.3 SSO同步接口记录

| 字段 | 值 |
|------|-----|
| api_id | 8 |
| api_code | api_user_sso_sync |
| api_name | 用户SSO同步 |
| api_path | /open-api/v1/users/{personId}/sync-sso |
| http_method | POST |
| category | 用户管理 |
| description | 同步用户账号到Keycloak SSO系统，支持多凭据（USERNAME、PHONE、IDCARD）同步，等同于账号管理的“同步SSO”功能 |
| is_sensitive | 1 |
| require_secret | 0 |
| status | 1 |
| order_index | 6.0 |
| actived | 1 |
| creator | system |
| create_time | 当前时间（格式：yyyyMMddHHmmss） |
| updator | NULL |
| update_time | NULL |

### 3.2 数据库脚本位置

建议在以下路径创建数据库更新脚本：
- `ps-be/sql/update/add_api_user_update_definition.sql`

脚本内容需包含：
- 插入用户信息修改接口定义记录的INSERT语句
- 插入密码修改接口定义记录的INSERT语句
- 插入SSO同步接口定义记录的INSERT语句
- 查询验证语句，确认插入成功

## 4. 后端实现

### 4.1 用户信息修改接口实现

#### 4.1.1 Controller实现

需要在OpenAPI控制器包中创建或扩展用户修改接口。

##### 实现位置
- 包路径：`com.jiuxi.admin.core.controller.openapi`
- 类名：`OpenApiUserController`（已存在，需扩展）

##### 接口方法定义

方法签名概要：
- 访问路径：PUT /open-api/v1/users/{personId}
- 参数接收：路径参数personId、请求体参数（包含personName、phone、email、tel等可选字段）
- 返回类型：JsonResponse
- 日志记录：记录接口调用、参数和执行结果

##### 关键实现要点

1. **参数验证**
   - 校验personId非空且格式正确
   - 校验至少提供一个待更新的字段
   - 校验手机号、邮箱格式（如提供）

2. **业务调用**
   - 委托给OpenApiUserService处理业务逻辑
   - 不直接调用内部用户模块的接口，保持API层隔离

3. **异常处理**
   - 捕获业务异常，返回友好的错误提示
   - 用户不存在时返回404状态提示

4. **拦截器自动处理**
   - API Key验证由ApiKeyInterceptor统一处理
   - 调用日志由拦截器自动记录，无需手动操作

#### 4.1.2 Service实现

需要在OpenAPI服务层实现用户更新业务逻辑。

##### 实现位置
- 接口：`com.jiuxi.admin.core.service.OpenApiUserService`
- 实现类：`com.jiuxi.admin.core.service.impl.OpenApiUserServiceImpl`

##### 业务方法定义

方法职责：
- 调用内部用户模块的更新命令处理器
- 处理租户ID、操作人ID等上下文信息的传递
- 异常转换与统一处理

##### 关键实现要点

1. **依赖内部服务**
   - 注入UserCommandHandler，调用handleUpdateUser方法
   - 构造UserUpdateCommand对象，设置必要参数

2. **上下文处理**
   - 从请求属性中获取appId作为操作来源标识
   - tenantId可从拦截器存储的应用信息中获取，或设置默认值
   - operator设置为"api_" + appId，标识来自第三方API调用

3. **数据转换**
   - 将请求参数转换为内部Command对象
   - 仅传递非空字段，避免覆盖未修改的数据

4. **异常处理**
   - 捕获用户不存在异常，抛出清晰的业务异常
   - 捕获Keycloak同步失败等底层异常，包装后抛出

#### 4.1.3 VO对象定义

如需接收请求体，可定义OpenApiUserUpdateVO或直接使用Map。

建议结构：
- personName: String
- phone: String
- email: String
- tel: String

### 4.2 密码修改接口实现

#### 4.2.1 Controller实现

##### 实现位置
- 包路径：`com.jiuxi.admin.core.controller.openapi`
- 类名：`OpenApiUserController`（已存在，需扩展）

##### 接口方法定义

方法签名概要：
- 访问路径：PUT /open-api/v1/users/{personId}/reset-password
- 参数接收：路径参数personId、请求体参数（包含newPassword）
- 返回类型：JsonResponse
- 日志记录：记录密码修改操作、执行结果和操作来源

##### 关键实现要点

1. **参数验证**
   - 校验personId非空且格式正确
   - 校验newPassword非空
   - 校验密码强度（长度、复杂度等）

2. **业务调用**
   - 委托给OpenApiUserService处理密码修改逻辑
   - 不直接调用内部用户模块的接口，保持API层隔离

3. **异常处理**
   - 捕获业务异常，返回友好的错误提示
   - 用户不存在时返回404状态提示
   - Keycloak同步失败时记录详细错误信息

4. **拦截器自动处理**
   - API Key验证由ApiKeyInterceptor统一处理
   - 调用日志由拦截器自动记录

#### 4.2.2 Service实现

##### 实现位置
- 接口：`com.jiuxi.admin.core.service.OpenApiUserService`
- 实现类：`com.jiuxi.admin.core.service.impl.OpenApiUserServiceImpl`

##### 业务方法定义

方法职责：
- 调用内部用户模块的密码修改服务
- 确保使用updatePasswordForAllCredentials方法进行Keycloak多凭据密码同步
- 处理同步结果和异常转换

##### 关键实现要点

1. **依赖内部服务**
   - 注入UserApplicationService，调用resetPassword方法
   - 确保密码修改会同步到Keycloak的所有凭据用户

2. **上下文处理**
   - 从请求属性中获取appId作为操作来源标识
   - operator设置为"api_" + appId，标识来自第三方API调用

3. **密码验证**
   - 验证密码格式和强度
   - 确保密码符合安全要求

4. **多凭据同步**
   - 系统会自动调用updatePasswordForAllCredentials方法
   - 该方法会同步更新USERNAME、PHONE、IDCARD三种凭据对应的Keycloak用户密码
   - 即使某个凭据用户不存在，也不影响其他凭据的密码更新

5. **异常处理**
   - 捕获用户不存在异常，抛出清晰的业务异常
   - 捕获Keycloak同步失败异常，记录详细错误信息
   - 返回密码修改结果，包括成功和失败的凭据信息

#### 4.2.3 VO对象定义

请求VO结构：
- newPassword: String（必填）

建议定义OpenApiPasswordResetVO或直接使用Map接收。

### 4.3 SSO同步接口实现

#### 4.3.1 Controller实现

##### 实现位置
- 包路径：`com.jiuxi.admin.core.controller.openapi`
- 类名：`OpenApiUserController`（已存在，需扩展）

##### 接口方法定义

方法签名概要：
- 访问路径：POST /open-api/v1/users/{personId}/sync-sso
- 参数接收：路径参数personId
- 返回类型：JsonResponse
- 日志记录：记录SSO同步操作、执行结果和操作来源

##### 关键实现要点

1. **参数验证**
   - 校验personId非空且格式正确

2. **业务调用**
   - 委托给OpenApiUserService处理SSO同步逻辑
   - 不直接调用内部用户模块的接口，保持API层隔离

3. **异步处理**
   - SSO同步是异步操作，接口立即返回
   - 返回同步任务提交成功的消息

4. **异常处理**
   - 捕获业务异常，返回友好的错误提示
   - 用户不存在时返回404状态提示
   - 账号不存在时返回明确的错误信息

5. **拦截器自动处理**
   - API Key验证由ApiKeyInterceptor统一处理
   - 调用日志由拦截器自动记录

#### 4.3.2 Service实现

##### 实现位置
- 接口：`com.jiuxi.admin.core.service.OpenApiUserService`
- 实现类：`com.jiuxi.admin.core.service.impl.OpenApiUserServiceImpl`

##### 业务方法定义

方法职责：
- 调用内部用户模块的syncAccountToKeycloak方法
- 该方法会发布Keycloak同步事件，异步执行多凭据同步
- 处理异常和结果转换

##### 关键实现要点

1. **依赖内部服务**
   - 注入UserAccountService，调用syncAccountToKeycloak方法
   - 该方法会根据personId获取accountId

2. **上下文处理**
   - 从请求属性中获取appId作为操作来源标识
   - 记录来自第三方API调用的同步操作

3. **异步同步**
   - syncAccountToKeycloak方法会发布KeycloakSyncEvent事件
   - 事件监听器会异步调用KeycloakSyncService.syncMultipleCredentials方法
   - 同步过程包括创建或更新USERNAME、PHONE、IDCARD三种凭据对应的Keycloak用户

4. **响应处理**
   - 由于是异步操作，接口返回提交成功的消息
   - 实际同步结果需要通过日志或单独的查询接口获取
   - 可以返回一个同步任务ID，用于后续查询同步状态

5. **异常处理**
   - 捕获用户/账号不存在异常，抛出清晰的业务异常
   - 捕获事件发布失败异常，记录详细错误信息

6. **多凭据同步机制**
   - 异步事件处理器会调用syncMultipleCredentials方法
   - 该方法会收集账号的所有凭据（username、phone、idcard）
   - 为每个凭据创建或更新Keycloak用户
   - 每个凭据用户在attributes中包含credentialType标识

## 5. 安全与权限

### 5.1 API Key验证

- 由ApiKeyInterceptor统一拦截/open-api/**路径
- 验证请求头中的Authorization是否有效
- 验证应用是否启用且未过期
- 验证IP是否在白名单内（如配置）

### 5.2 接口授权

- 第三方应用需要在权限配置页面勾选"用户信息修改"接口
- 通过tp_app_api_permission表关联应用与API的授权关系
- 拦截器根据授权关系判断应用是否有权访问该接口

### 5.3 调用日志

- 每次调用自动记录到tp_api_call_log表
- 记录内容包括：应用ID、API路径、HTTP方法、请求参数、响应状态、响应时间、IP地址、调用时间等
- 管理员可在后台查询调用日志和统计数据

## 6. 前端变更

### 6.1 权限配置页面

无需前端代码修改。

- 权限配置页面通过接口查询tp_api_definition表，自动展示所有启用的API
- 新增的"用户信息修改"和"用户密码重置"接口会自动出现在"用户管理"分类下
- 管理员可勾选这些接口，为第三方应用授权

### 6.2 API清单展示

无需前端代码修改。

- 在第三方应用详情页面，查看已授权的API列表时，新接口会自动显示
- 展示格式：API名称、API路径、HTTP方法、分类
- 两个新接口均属于"用户管理"分类

## 7. 测试验证

### 7.1 数据库验证

验证API定义记录是否成功插入：
```
SELECT api_id, api_code, api_name, api_path, http_method, category 
FROM tp_api_definition 
WHERE api_code IN ('api_user_update', 'api_user_password_reset', 'api_user_sso_sync');
```

预期结果：返回三条记录，各字段值与设计一致。

### 7.2 权限配置验证

操作步骤：
1. 登录管理后台
2. 导航至"系统管理 > API管理 > 第三方应用"
3. 选择一个测试应用，点击"权限配置"
4. 在"用户管理"分类下，查看是否出现"用户信息修改"、"用户密码重置"和"用户SSO同步"接口
5. 勾选这些接口并保存

预期结果：
- 界面正常显示三个新接口
- 保存成功，tp_app_api_permission表中新增对应授权记录

### 7.3 用户信息修改接口调用验证

使用Postman或curl工具测试：

#### 测试用例1：正常更新用户信息
请求示例：
```
PUT /open-api/v1/users/{实际人员ID}
Headers:
  Authorization: {实际API密钥}
  Content-Type: application/json
Body:
{
  "personName": "测试用户",
  "phone": "13800138000"
}
```

预期响应：
- HTTP状态码：200
- success: true
- message: "操作成功"

#### 测试用例2：缺少API Key
请求示例：
```
PUT /open-api/v1/users/{实际人员ID}
Headers:
  Content-Type: application/json
Body:
{
  "personName": "测试用户"
}
```

预期响应：
- HTTP状态码：401
- 错误提示：缺少API Key，请在请求头中添加 Authorization

#### 测试用例3：应用未授权该接口
请求示例：
```
PUT /open-api/v1/users/{实际人员ID}
Headers:
  Authorization: {未授权应用的API密钥}
  Content-Type: application/json
Body:
{
  "personName": "测试用户"
}
```

预期响应：
- HTTP状态码：403
- 错误提示：无权限访问该接口

#### 测试用例4：用户不存在
请求示例：
```
PUT /open-api/v1/users/9999999999999999999
Headers:
  Authorization: {实际API密钥}
  Content-Type: application/json
Body:
{
  "personName": "测试用户"
}
```

预期响应：
- HTTP状态码：200或404
- success: false
- 错误提示：用户不存在

### 7.4 密码修改接口调用验证

使用Postman或curl工具测试：

#### 测试用例1：正常重置密码
请求示例：
```
PUT /open-api/v1/users/{实际人员ID}/reset-password
Headers:
  Authorization: {实际API密钥}
  Content-Type: application/json
Body:
{
  "newPassword": "NewPassword@123"
}
```

预期响应：
- HTTP状态码：200
- success: true
- message: "操作成功"
- data: "密码重置成功"

验证点：
- 用户可以使用新密码通过用户名、手机号、身份证号三种方式登录
- Keycloak中对应的三个凭据用户密码均已更新

#### 测试用例2：密码强度不符合要求
请求示例：
```
PUT /open-api/v1/users/{实际人员ID}/reset-password
Headers:
  Authorization: {实际API密钥}
  Content-Type: application/json
Body:
{
  "newPassword": "123"
}
```

预期响应：
- HTTP状态码：200或400
- success: false
- 错误提示：密码不符合强度要求

#### 测试用例3：缺少newPassword参数
请求示例：
```
PUT /open-api/v1/users/{实际人员ID}/reset-password
Headers:
  Authorization: {实际API密钥}
  Content-Type: application/json
Body:
{}
```

预期响应：
- HTTP状态码：200或400
- success: false
- 错误提示：新密码不能为空

#### 测试用例4：重置不存在用户的密码
请求示例：
```
PUT /open-api/v1/users/9999999999999999999/reset-password
Headers:
  Authorization: {实际API密钥}
  Content-Type: application/json
Body:
{
  "newPassword": "NewPassword@123"
}
```

预期响应：
- HTTP状态码：200或404
- success: false
- 错误提示：用户不存在

### 7.5 SSO同步接口调用验证

使用Postman或curl工具测试：

#### 测试用例1：正常同步用户到SSO
请求示例：
```
POST /open-api/v1/users/{实际人员ID}/sync-sso
Headers:
  Authorization: {实际API密钥}
  Content-Type: application/json
```

预期响应：
- HTTP状态码：200
- success: true
- message: "操作成功"
- data: 包含同步状态和详细信息

验证点：
- 查看Keycloak中是否创建了三个凭据用户（USERNAME、PHONE、IDCARD）
- 每个用户的attributes中包含credentialType标识
- 用户可以使用三种凭据中的任意一种登录

#### 测试用例2：同步不存在的用户
请求示例：
```
POST /open-api/v1/users/9999999999999999999/sync-sso
Headers:
  Authorization: {实际API密钥}
  Content-Type: application/json
```

预期响应：
- HTTP状态码：200或404
- success: false
- 错误提示：用户不存在或账号不存在

#### 测试用例3：重复同步（更新场景）
请求示例：
```
POST /open-api/v1/users/{已同步过的人员ID}/sync-sso
Headers:
  Authorization: {实际API密钥}
  Content-Type: application/json
```

预期响应：
- HTTP状态码：200
- success: true
- data中显示部分或全部凭据为"用户已存在，更新成功"

验证点：
- 已存在的Keycloak用户信息被更新
- 不会创建重复的用户

#### 测试用例4：部分凭据同步失败

模拟场景：用户有用户名和手机号，但没有身份证号。

预期响应：
- HTTP状态码：200
- success: true
- data中显示：
  - USERNAME凭据：同步成功
  - PHONE凭据：同步成功
  - IDCARD凭据：跳过（因为没有身份证号）

### 7.6 日志验证

验证调用日志是否正确记录：
```
SELECT log_id, app_name, api_path, http_method, request_ip, response_status, response_time, call_time
FROM tp_api_call_log
WHERE api_path LIKE '%/open-api/v1/users/%' AND http_method IN ('PUT', 'POST')
ORDER BY call_time DESC
LIMIT 20;
```

预期结果：
- 每次调用都有对应日志记录
- 记录包含请求IP、响应状态、响应时间等关键信息
- 可以区分用户信息修改、密码修改和SSO同步三种操作（通过api_path和http_method字段）

## 8. 部署说明

### 8.1 部署顺序

1. 执行数据库更新脚本，插入API定义记录
2. 部署后端代码（Controller和Service实现）
3. 重启应用服务
4. 验证接口是否正常工作

### 8.2 数据库脚本执行

在生产环境执行前需确认：
- api_id值（6、7、8）不与现有记录冲突
- 脚本具有幂等性，避免重复执行导致数据重复

建议脚本包含检查逻辑：
- 在插入前检查api_code是否已存在
- 如已存在则跳过或更新

### 8.3 回滚方案

如需回滚：
1. 删除tp_api_definition表中api_code为'api_user_update'、'api_user_password_reset'和'api_user_sso_sync'的记录
2. 删除tp_app_api_permission表中关联这三个API的授权记录
3. 回滚后端代码至上一版本

## 9. 风险与注意事项

### 9.1 数据安全

- 虽然是开放API，但已通过API Key、IP白名单、权限配置等多层机制保护
- 修改接口（特别是密码修改和SSO同步）属于高度敏感操作，需确保只授权给可信的第三方应用
- 建议定期审查授权情况和调用日志
- 密码修改操作应记录详细的审计日志，包括操作来源、时间、结果等
- SSO同步操作会创建或更新Keycloak用户，需确保同步逻辑的安全性

### 9.2 数据一致性

- 用户信息修改会同步到Keycloak，需确保同步逻辑正常
- 密码修改必须使用updatePasswordForAllCredentials方法，确保所有凭据用户的密码同步更新
- SSO同步使用syncMultipleCredentials方法，确保所有凭据用户被创建或更新
- 如Keycloak同步失败，需明确异常处理策略（回滚或记录日志）
- 密码修改失败时，应保持原密码不变，避免用户无法登录
- SSO同步是异步操作，需要通过日志或单独查询接口获取实际同步结果

### 9.3 并发控制

- 如多个第三方应用同时修改同一用户，可能产生覆盖问题
- 建议在业务层实现版本号或时间戳机制，避免脏写

### 9.4 接口文档

- 需要在开放API接口文档中补充用户信息修改、密码修改和SSO同步接口的说明
- 文档位置：ps-be/docs/开放API接口文档.md
- 内容包括：接口路径、请求参数、响应示例、错误码说明
- 密码修改接口需要特别说明多凭据同步机制
- SSO同步接口需要说明异步执行特性和多凭据创建/更新机制

## 10. 后续扩展

### 10.1 批量修改

如有需求，可扩展批量修改接口：
- API路径：PUT /open-api/v1/users/batch
- 请求体：用户ID和修改信息的数组

### 10.2 签名验证

如需增强安全性，可启用API签名机制：
- 设置require_secret字段为1
- 在拦截器中验证请求签名的有效性

### 10.3 操作审计

可扩展审计日志功能，记录每次用户信息修改、密码修改和SSO同步的详细变更内容：
- 记录修改前后的字段值对比（用户信息修改）
- 记录密码修改操作（不记录密码明文，仅记录操作事实）
- 记录SSO同步操作和每个凭据的同步结果
- 关联操作来源（应用ID、IP地址）
- 支持审计查询和数据追溯
- 密码修改记录应包含修改时间、操作来源、成功/失败状态、同步的凭据类型等
- SSO同步记录应包含同步时间、操作来源、每个凭据的同步状态、Keycloak用户ID等

### 10.4 密码策略增强

未来可扩展更严格的密码策略：
- 密码复杂度规则配置化
- 密码历史记录，防止重复使用旧密码
- 密码过期策略
- 密码尝试次数限制
- 强制定期修改密码

### 10.5 SSO同步状态查询

由于SSO同步是异步操作，可扩展同步状态查询接口：
- API路径：GET /open-api/v1/users/{personId}/sync-status
- 返回同步任务的执行状态和结果
- 包含每个凭据的同步情况
- 支持查询历史同步记录

### 10.6 批量同步

可扩展批量同步接口：
- API路径：POST /open-api/v1/users/batch-sync-sso
- 请求体：用户ID数组
- 批量提交同步任务
- 返回批量任务ID，用于后续查询
