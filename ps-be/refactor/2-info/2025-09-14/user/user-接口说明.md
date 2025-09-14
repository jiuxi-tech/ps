# User模块接口业务说明文档

> **文档生成时间**：2025-09-14  
> **模块路径**：`D:\keycloak_sb_sso_new0910_claude\ps\ps-be\src\main\java\com\jiuxi\module\user`  
> **文档版本**：v1.0

## 接口概述

User模块提供完整的用户生命周期管理功能，包括用户创建、更新、查询、删除，以及账户管理、权限控制等功能。该模块包含三个主要的Controller类，提供了现代RESTful风格和传统风格的API接口。

## 接口分类

### 1. 现代RESTful用户管理接口 (UserController)

**控制器路径**：`com.jiuxi.module.user.interfaces.web.UserController`  
**基础路径**：`/api/v1/users`

#### 1.1 获取当前用户信息
- **接口路径**：`GET /api/v1/users/me`
- **接口作用**：登录成功后获取当前用户信息
- **权限要求**：无需权限验证（@IgnoreAuthorization）
- **入参**：
  - `X-User-Dept-Id`（Header）：用户部门ID（可选）
  - `X-User-Person-Id`（Header）：用户人员ID（可选）
  - `X-User-Role-Ids`（Header）：用户角色IDs（可选）
  - `X-User-City-Code`（Header）：用户城市代码（可选）
- **返回类型**：`JsonResponse<PersonVO>`
- **业务逻辑**：通过PersonService获取用户信息并设置额外属性

#### 1.2 创建用户
- **接口路径**：`POST /api/v1/users`
- **接口作用**：创建新用户
- **权限要求**：需要权限验证
- **入参**：
  - `UserCreateDTO`（RequestBody）：用户创建信息
  - `X-User-Person-Id`（Header）：操作者ID
  - `X-Tenant-Id`（Header）：租户ID
- **返回类型**：`JsonResponse<String>`（返回用户ID）
- **业务逻辑**：调用UserApplicationService创建用户，包含数据验证和异常处理

#### 1.3 更新用户信息
- **接口路径**：`PUT /api/v1/users/{personId}`
- **接口作用**：更新指定用户的信息
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `personId`（PathVariable）：用户人员ID
  - `UserUpdateDTO`（RequestBody）：用户更新信息
  - `X-User-Person-Id`（Header）：操作者ID
  - `X-Tenant-Id`（Header）：租户ID
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：调用UserApplicationService更新用户信息

#### 1.4 查看用户详情
- **接口路径**：`GET /api/v1/users/{personId}`
- **接口作用**：查询指定用户的详细信息
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `personId`（PathVariable）：用户人员ID
- **返回类型**：`JsonResponse<UserResponseDTO>`
- **业务逻辑**：通过UserApplicationService获取用户详情

#### 1.5 根据用户名查询用户
- **接口路径**：`GET /api/v1/users/username/{username}`
- **接口作用**：通过用户名查询用户信息
- **权限要求**：需要权限验证
- **入参**：
  - `username`（PathVariable）：用户名
  - `X-Tenant-Id`（Header）：租户ID
- **返回类型**：`JsonResponse<UserResponseDTO>`
- **业务逻辑**：通过用户名和租户ID查询用户

#### 1.6 根据部门查询用户列表
- **接口路径**：`GET /api/v1/users/departments/{deptId}`
- **接口作用**：查询指定部门下的用户列表
- **权限要求**：需要权限验证
- **入参**：
  - `deptId`（PathVariable）：部门ID
- **返回类型**：`JsonResponse<List<UserResponseDTO>>`
- **业务逻辑**：查询部门下所有用户信息

#### 1.7 删除用户
- **接口路径**：`DELETE /api/v1/users/{personId}`
- **接口作用**：删除指定用户
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `personId`（PathVariable）：用户人员ID
  - `X-User-Person-Id`（Header）：操作者ID
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：调用UserApplicationService删除用户

#### 1.8 批量删除用户
- **接口路径**：`DELETE /api/v1/users/batch`
- **接口作用**：批量删除多个用户
- **权限要求**：需要权限验证
- **入参**：
  - `List<String>`（RequestBody）：用户ID列表
  - `X-User-Person-Id`（Header）：操作者ID
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：批量调用删除用户操作

#### 1.9 激活用户
- **接口路径**：`PUT /api/v1/users/{personId}/activate`
- **接口作用**：激活指定用户账户
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `personId`（PathVariable）：用户人员ID
  - `X-User-Person-Id`（Header）：操作者ID
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：激活用户账户状态

#### 1.10 停用用户
- **接口路径**：`PUT /api/v1/users/{personId}/deactivate`
- **接口作用**：停用指定用户账户
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `personId`（PathVariable）：用户人员ID
  - `X-User-Person-Id`（Header）：操作者ID
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：停用用户账户状态

#### 1.11 分页查询用户列表
- **接口路径**：`POST /api/v1/users/search`
- **接口作用**：根据查询条件分页获取用户列表
- **权限要求**：需要权限验证
- **入参**：
  - `UserQueryDTO`（RequestBody）：查询条件
  - `X-Tenant-Id`（Header）：租户ID
- **返回类型**：`JsonResponse<PageResult<UserResponseDTO>>`
- **业务逻辑**：分页查询用户列表并构建分页结果对象

### 2. 用户账户管理接口 (UserAccountController)

**控制器路径**：`com.jiuxi.module.user.interfaces.web.UserAccountController`  
**基础路径**：`/api/v1/user-accounts`

#### 2.1 重置用户密码
- **接口路径**：`PUT /api/v1/user-accounts/{personId}/reset-password`
- **接口作用**：管理员重置指定用户的密码
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `personId`（PathVariable）：用户人员ID
  - `newPassword`（RequestParam）：新密码
  - `X-User-Person-Id`（Header）：操作者ID
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：调用UserApplicationService重置用户密码

#### 2.2 修改用户密码
- **接口路径**：`PUT /api/v1/user-accounts/change-password`
- **接口作用**：当前登录用户修改自己的密码
- **权限要求**：无需权限验证（@IgnoreAuthorization）
- **入参**：
  - `ChangePasswordRequest`（RequestBody）：密码修改请求
  - `X-User-Person-Id`（Header）：用户人员ID
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：验证新密码一致性后调用重置密码功能

#### 2.3 锁定/解锁账户
- **接口路径**：`PUT /api/v1/user-accounts/{personId}/lock`
- **接口作用**：锁定或解锁用户账户
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `personId`（PathVariable）：用户人员ID
  - `locked`（RequestParam）：锁定状态（true锁定，false解锁）
  - `X-User-Person-Id`（Header）：操作者ID
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：根据locked参数调用激活或停用用户功能

#### 2.4 启用/禁用账户
- **接口路径**：`PUT /api/v1/user-accounts/{personId}/enable`
- **接口作用**：启用或禁用用户账户
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `personId`（PathVariable）：用户人员ID
  - `enabled`（RequestParam）：启用状态（true启用，false禁用）
  - `X-User-Person-Id`（Header）：操作者ID
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：根据enabled参数调用激活或停用用户功能

### 3. 传统风格用户管理接口 (UserPersonController)

**控制器路径**：`com.jiuxi.module.user.interfaces.web.controller.UserPersonController`  
**基础路径**：`/sys/person`

#### 3.1 获取用户信息（传统）
- **接口路径**：`POST /sys/person/getUserInfo`
- **接口作用**：登录后获取用户信息（传统版本）
- **权限要求**：无需权限验证（@IgnoreAuthorization）
- **入参**：
  - `jwtdid`（RequestParam）：部门ID
  - `jwtpid`（RequestParam）：人员ID
  - `jwtrids`（RequestParam）：角色IDs
  - `jwtCityCode`（RequestParam）：城市代码
- **返回类型**：`JsonResponse<PersonVO>`
- **业务逻辑**：获取用户信息并设置城市名称

#### 3.2 政府人员列表
- **接口路径**：`POST /sys/person/org/list`
- **接口作用**：查询政府类型人员列表
- **权限要求**：需要权限验证
- **入参**：
  - `TpPersonBasicQuery`：查询条件
  - `jwtpid`：操作者人员ID
- **返回类型**：`JsonResponse<IPage<TpPersonBasicinfoVO>>`
- **业务逻辑**：设置人员类型为政府并分页查询

#### 3.3 企业人员列表
- **接口路径**：`POST /sys/person/ent/list`
- **接口作用**：查询企业类型人员列表
- **权限要求**：需要权限验证
- **入参**：
  - `TpPersonBasicQuery`：查询条件
  - `jwtpid`：操作者人员ID
- **返回类型**：`JsonResponse<IPage<TpPersonBasicinfoVO>>`
- **业务逻辑**：验证部门ID后设置人员类型为企业并分页查询

#### 3.4 添加政府用户
- **接口路径**：`POST /sys/person/org/add`
- **接口作用**：添加政府类型用户
- **权限要求**：需要权限验证
- **入参**：
  - `TpPersonBasicinfoVO`（RequestBody）：用户基本信息
  - `jwtpid`：操作者人员ID
- **返回类型**：`JsonResponse<TpPersonBasicinfoVO>`
- **业务逻辑**：创建政府类型用户

#### 3.5 添加企业用户
- **接口路径**：`POST /sys/person/ent/add`
- **接口作用**：添加企业类型用户
- **权限要求**：需要权限验证
- **入参**：
  - `TpPersonBasicinfoVO`（RequestBody）：用户基本信息
  - `jwtpid`：操作者人员ID
- **返回类型**：`JsonResponse<TpPersonBasicinfoVO>`
- **业务逻辑**：创建企业类型用户

#### 3.6 账号管理
- **接口路径**：`POST /sys/person/account-manage`
- **接口作用**：新增或修改用户账号信息
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `TpAccountVO`（RequestBody）：账号信息
- **返回类型**：`JsonResponse<Integer>`
- **业务逻辑**：如果传了账号ID则修改，否则新增账号

#### 3.7 查看用户信息
- **接口路径**：`POST /sys/person/view`
- **接口作用**：查看用户基本信息
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `personId`：用户人员ID
  - `deptId`：部门ID
- **返回类型**：`JsonResponse<TpPersonBasicinfoVO>`
- **业务逻辑**：查询用户基本信息

#### 3.8 查看用户账号信息
- **接口路径**：`POST /sys/person/account-view`
- **接口作用**：查看用户账号信息
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `personId`：用户人员ID
- **返回类型**：`JsonResponse<TpAccountVO>`
- **业务逻辑**：查询用户账号信息（包含测试代码）

#### 3.9 修改用户基本信息
- **接口路径**：`POST /sys/person/update`
- **接口作用**：修改用户基本信息
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `TpPersonBasicinfoVO`（RequestBody）：用户信息
  - `jwtpid`：操作者人员ID
- **返回类型**：`JsonResponse<Integer>`
- **业务逻辑**：更新用户基本信息

#### 3.10 当前用户修改密码
- **接口路径**：`POST /sys/person/update-pwd`
- **接口作用**：当前登录用户修改密码
- **权限要求**：无需权限验证（@IgnoreAuthorization）
- **入参**：
  - `oldUserpwd`：原密码
  - `userpwd`：新密码
  - `jwtpid`：用户人员ID
- **返回类型**：`JsonResponse<Integer>`
- **业务逻辑**：验证原密码后更新新密码

#### 3.11 重置密码
- **接口路径**：`POST /sys/person/account-resetpwd`
- **接口作用**：管理员重置用户密码
- **权限要求**：需要权限验证
- **入参**：
  - `accountId`：账号ID
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：生成随机密码并重置

#### 3.12 手机号找回密码
- **接口路径**：`POST /sys/person/account-findpwd-by-phone`
- **接口作用**：通过手机号找回密码
- **权限要求**：无需权限验证（@IgnoreAuthorization）
- **入参**：
  - `phone`（RequestParam）：手机号
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：目前返回"短信服务不可用"

#### 3.13 邮箱找回密码
- **接口路径**：`POST /sys/person/account-findpwd-by-email`
- **接口作用**：通过邮箱找回密码
- **权限要求**：无需权限验证（@IgnoreAuthorization）
- **入参**：
  - `email`（RequestParam）：邮箱地址
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：发送邮箱验证码

#### 3.14 验证码校验
- **接口路径**：`POST /sys/person/check-vcode`
- **接口作用**：验证码校验并修改密码
- **权限要求**：无需权限验证（@IgnoreAuthorization）
- **入参**：
  - `phone`（RequestParam，可选）：手机号
  - `email`（RequestParam，可选）：邮箱
  - `vcode`：验证码
  - `userpwd`：新密码
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：根据手机或邮箱验证码修改密码

#### 3.15 账号冻结/解冻
- **接口路径**：`POST /sys/person/account-locked`
- **接口作用**：冻结或解冻用户账号
- **权限要求**：需要权限验证
- **入参**：
  - `accountId`：账号ID
  - `locked`：冻结状态（0解冻，1冻结）
- **返回类型**：`JsonResponse<Integer>`
- **业务逻辑**：更新账号冻结状态

#### 3.16 账号启用/禁用
- **接口路径**：`POST /sys/person/account-enabled`
- **接口作用**：启用或禁用用户账号
- **权限要求**：需要权限验证
- **入参**：
  - `accountId`：账号ID
  - `enabled`：启用状态
- **返回类型**：`JsonResponse<Integer>`
- **业务逻辑**：更新账号启用状态

#### 3.17 批量删除用户
- **接口路径**：`POST /sys/person/delete`
- **接口作用**：批量删除用户信息
- **权限要求**：需要权限验证
- **入参**：
  - `deptIds`：部门ID列表
  - `personIds`：人员ID列表
  - `jwtpid`：操作者人员ID
- **返回类型**：`JsonResponse`
- **业务逻辑**：批量删除用户信息

#### 3.18 同步账号到Keycloak
- **接口路径**：`POST /sys/person/sync-account-to-keycloak`
- **接口作用**：同步账号信息到Keycloak系统
- **权限要求**：需要权限验证（businessKey = "personId"）
- **入参**：
  - `personId`：用户人员ID
  - `accountId`：账号ID
- **返回类型**：`JsonResponse<String>`
- **业务逻辑**：将本地账号同步到Keycloak身份认证系统

## 数据传输对象（DTO）说明

### UserCreateDTO
用户创建请求对象，包含用户基本信息、部门信息、账号信息等必要字段。

### UserUpdateDTO  
用户更新请求对象，包含可更新的用户信息字段。

### UserQueryDTO
用户查询请求对象，支持分页查询和多条件筛选。

### UserResponseDTO
用户响应对象，包含完整的用户信息用于前端展示。

### ChangePasswordRequest
密码修改请求对象，包含新密码和确认密码字段。

### PageResult<T>
分页结果封装对象，包含数据列表、总数、当前页、页大小等分页信息。

## 安全说明

1. **权限控制**：大部分接口需要通过@Authorization注解进行权限验证
2. **业务键验证**：涉及用户操作的接口通过businessKey="personId"进行业务权限验证
3. **租户隔离**：通过X-Tenant-Id请求头实现多租户数据隔离
4. **密码安全**：支持密码加密传输和强密码策略验证
5. **审计日志**：所有用户操作都记录操作者信息用于审计

## 接口兼容性

该模块同时提供现代RESTful风格接口和传统风格接口，确保系统升级过程中的向后兼容性。建议新功能优先使用RESTful风格接口，传统接口主要用于兼容现有系统。