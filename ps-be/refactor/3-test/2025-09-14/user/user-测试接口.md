# User模块接口测试文档

> **文档生成时间**：2025-09-14  
> **模块路径**：`D:\keycloak_sb_sso_new0910_claude\ps\ps-be\src\main\java\com\jiuxi\module\user`  
> **测试版本**：v1.0

## 测试环境准备

### 环境要求
- **测试环境**：开发/测试环境
- **数据库**：MySQL/MariaDB（含用户基础数据）
- **认证系统**：Spring Security + JWT
- **外部依赖**：Keycloak身份认证系统（可选）

### 测试前置条件
1. 系统已正常启动
2. 数据库已初始化基础数据
3. 已获得有效的JWT Token
4. 测试用户账号已创建
5. 测试部门和角色已配置

### 测试数据准备
```json
{
  "testTenantId": "TEST_TENANT_001",
  "testDeptId": "DEPT_001",
  "testPersonId": "PERSON_001",
  "testUsername": "testuser",
  "testPassword": "Test123456!",
  "testEmail": "test@jiuxi.com",
  "testPhone": "13800138000",
  "adminPersonId": "ADMIN_001"
}
```

## 现代RESTful接口测试用例

### 1. 用户信息获取接口测试

#### 测试用例 1.1：获取当前用户信息
**接口**：`GET /api/v1/users/me`

**测试步骤**：
1. 准备测试数据：设置有效的JWT Header参数
2. 发送GET请求到 `/api/v1/users/me`
3. 验证响应状态码为200
4. 验证返回的用户信息完整性
5. 验证角色和权限信息正确性

**测试用例**：
```http
GET /api/v1/users/me
Headers:
  X-User-Dept-Id: DEPT_001
  X-User-Person-Id: PERSON_001
  X-User-Role-Ids: ROLE_001,ROLE_002
  X-User-City-Code: 320100
  Authorization: Bearer <JWT_TOKEN>
```

**预期结果**：
- 状态码：200
- 响应包含用户基本信息
- 角色信息正确设置
- 城市代码和名称正确

### 2. 用户创建接口测试

#### 测试用例 2.1：正常创建用户
**接口**：`POST /api/v1/users`

**测试步骤**：
1. 准备有效的用户创建数据
2. 设置有效的操作者和租户信息
3. 发送POST请求创建用户
4. 验证响应状态码为200
5. 验证返回的用户ID不为空
6. 查询数据库验证用户已创建

**测试用例**：
```http
POST /api/v1/users
Headers:
  X-User-Person-Id: ADMIN_001
  X-Tenant-Id: TEST_TENANT_001
  Content-Type: application/json
  Authorization: Bearer <JWT_TOKEN>

Body:
{
  "username": "newuser001",
  "password": "NewUser123!",
  "personName": "新用户001",
  "phone": "13900139001",
  "email": "newuser001@jiuxi.com",
  "deptId": "DEPT_001",
  "idCard": "320101199001011111"
}
```

**预期结果**：
- 状态码：200
- 返回用户ID
- 数据库中用户记录已创建
- 用户账户信息已创建

#### 测试用例 2.2：用户名重复创建失败
**测试步骤**：
1. 使用已存在的用户名创建用户
2. 验证返回失败响应
3. 验证错误信息提示用户名已存在

**预期结果**：
- 状态码：200（业务失败）
- 响应消息提示用户名已存在
- 数据库无重复记录

#### 测试用例 2.3：参数校验失败
**测试步骤**：
1. 发送缺少必填字段的请求
2. 发送格式错误的数据
3. 验证参数校验错误响应

**预期结果**：
- 状态码：400或200（业务失败）
- 错误信息明确指出参数问题

### 3. 用户更新接口测试

#### 测试用例 3.1：正常更新用户信息
**接口**：`PUT /api/v1/users/{personId}`

**测试步骤**：
1. 准备要更新的用户数据
2. 发送PUT请求更新用户信息
3. 验证更新成功
4. 查询用户信息验证更新内容

**测试用例**：
```http
PUT /api/v1/users/PERSON_001
Headers:
  X-User-Person-Id: ADMIN_001
  X-Tenant-Id: TEST_TENANT_001
  Content-Type: application/json
  Authorization: Bearer <JWT_TOKEN>

Body:
{
  "personId": "PERSON_001",
  "personName": "更新后的用户名",
  "phone": "13900139002",
  "email": "updated@jiuxi.com"
}
```

**预期结果**：
- 状态码：200
- 更新成功消息
- 数据库记录已更新

### 4. 用户查询接口测试

#### 测试用例 4.1：根据用户ID查询
**接口**：`GET /api/v1/users/{personId}`

**测试步骤**：
1. 使用存在的用户ID查询
2. 验证返回完整用户信息
3. 使用不存在的用户ID查询
4. 验证返回用户不存在错误

#### 测试用例 4.2：根据用户名查询
**接口**：`GET /api/v1/users/username/{username}`

**测试步骤**：
1. 使用存在的用户名和租户ID查询
2. 验证返回正确用户信息
3. 使用不存在的用户名查询
4. 验证返回用户不存在错误

#### 测试用例 4.3：分页查询用户列表
**接口**：`POST /api/v1/users/search`

**测试步骤**：
1. 发送分页查询请求
2. 验证返回分页结果格式
3. 验证分页参数计算正确
4. 测试多条件组合查询

**测试用例**：
```http
POST /api/v1/users/search
Headers:
  X-Tenant-Id: TEST_TENANT_001
  Content-Type: application/json
  Authorization: Bearer <JWT_TOKEN>

Body:
{
  "tenantId": "TEST_TENANT_001",
  "deptId": "DEPT_001",
  "personName": "测试",
  "phone": "139",
  "current": 1,
  "size": 10
}
```

### 5. 用户删除接口测试

#### 测试用例 5.1：单个用户删除
**接口**：`DELETE /api/v1/users/{personId}`

**测试步骤**：
1. 创建测试用户
2. 删除该用户
3. 验证删除成功
4. 验证用户已不存在

#### 测试用例 5.2：批量用户删除
**接口**：`DELETE /api/v1/users/batch`

**测试步骤**：
1. 准备多个测试用户
2. 批量删除用户
3. 验证所有用户已删除

### 6. 用户状态管理测试

#### 测试用例 6.1：激活用户
**接口**：`PUT /api/v1/users/{personId}/activate`

**测试步骤**：
1. 准备停用状态的用户
2. 调用激活接口
3. 验证用户状态变为激活
4. 验证用户可正常登录

#### 测试用例 6.2：停用用户
**接口**：`PUT /api/v1/users/{personId}/deactivate`

**测试步骤**：
1. 准备激活状态的用户
2. 调用停用接口
3. 验证用户状态变为停用
4. 验证用户无法登录

## 账户管理接口测试用例

### 7. 密码管理测试

#### 测试用例 7.1：管理员重置密码
**接口**：`PUT /api/v1/user-accounts/{personId}/reset-password`

**测试步骤**：
1. 使用管理员权限重置用户密码
2. 验证重置成功
3. 使用新密码登录验证
4. 验证原密码失效

**测试用例**：
```http
PUT /api/v1/user-accounts/PERSON_001/reset-password
Headers:
  X-User-Person-Id: ADMIN_001
  Authorization: Bearer <ADMIN_JWT_TOKEN>

Params:
  newPassword=NewPassword123!
```

#### 测试用例 7.2：用户自助修改密码
**接口**：`PUT /api/v1/user-accounts/change-password`

**测试步骤**：
1. 用户使用当前密码修改密码
2. 验证新密码确认一致性
3. 验证密码强度要求
4. 使用新密码登录验证

**测试用例**：
```http
PUT /api/v1/user-accounts/change-password
Headers:
  X-User-Person-Id: PERSON_001
  Content-Type: application/json
  Authorization: Bearer <JWT_TOKEN>

Body:
{
  "newPassword": "NewUserPass123!",
  "confirmPassword": "NewUserPass123!"
}
```

### 8. 账户状态管理测试

#### 测试用例 8.1：账户锁定/解锁
**接口**：`PUT /api/v1/user-accounts/{personId}/lock`

**测试步骤**：
1. 锁定用户账户（locked=true）
2. 验证用户无法登录
3. 解锁用户账户（locked=false）
4. 验证用户可正常登录

#### 测试用例 8.2：账户启用/禁用
**接口**：`PUT /api/v1/user-accounts/{personId}/enable`

**测试步骤**：
1. 禁用用户账户（enabled=false）
2. 验证用户无法登录
3. 启用用户账户（enabled=true）
4. 验证用户可正常登录

## 传统接口兼容性测试

### 9. 传统用户管理接口测试

#### 测试用例 9.1：政府人员列表查询
**接口**：`POST /sys/person/org/list`

**测试步骤**：
1. 查询政府类型人员列表
2. 验证返回数据格式兼容性
3. 验证分页功能正常

#### 测试用例 9.2：企业人员列表查询
**接口**：`POST /sys/person/ent/list`

**测试步骤**：
1. 提供有效部门ID查询企业人员
2. 不提供部门ID验证错误处理
3. 验证分页和筛选功能

#### 测试用例 9.3：传统密码修改
**接口**：`POST /sys/person/update-pwd`

**测试步骤**：
1. 提供正确的原密码和新密码
2. 验证密码修改成功
3. 测试原密码错误的情况
4. 验证密码强度要求

### 10. Keycloak集成测试

#### 测试用例 10.1：账号同步到Keycloak
**接口**：`POST /sys/person/sync-account-to-keycloak`

**测试步骤**：
1. 同步本地账号到Keycloak
2. 验证同步成功状态
3. 在Keycloak中验证账号创建
4. 测试重复同步的处理

## 异常场景测试

### 11. 权限验证测试

#### 测试用例 11.1：无权限访问测试
**测试步骤**：
1. 使用无效JWT Token访问受保护接口
2. 验证返回401未授权
3. 使用过期Token访问
4. 验证返回相应错误

#### 测试用例 11.2：业务权限测试
**测试步骤**：
1. 用户A尝试操作用户B的数据
2. 验证业务权限检查生效
3. 验证返回权限不足错误

### 12. 数据一致性测试

#### 测试用例 12.1：并发操作测试
**测试步骤**：
1. 同时创建用户名相同的用户
2. 验证只有一个创建成功
3. 并发更新同一用户信息
4. 验证数据一致性

#### 测试用例 12.2：事务回滚测试
**测试步骤**：
1. 模拟创建用户过程中异常
2. 验证用户信息和账户信息回滚
3. 确保数据库状态一致

### 13. 性能测试

#### 测试用例 13.1：批量操作性能测试
**测试步骤**：
1. 批量创建100个用户
2. 记录响应时间和成功率
3. 批量查询大量用户数据
4. 验证分页性能

#### 测试用例 13.2：并发访问性能测试
**测试步骤**：
1. 模拟50个并发用户查询
2. 记录平均响应时间
3. 验证系统稳定性

## 测试执行计划

### 阶段一：基础功能测试（2天）
- 用户创建、更新、查询、删除功能
- 账户状态管理功能
- 基本权限验证

### 阶段二：账户管理测试（2天）  
- 密码管理功能
- 账户锁定/启用功能
- Keycloak集成功能

### 阶段三：兼容性测试（2天）
- 传统接口兼容性验证
- 新旧接口数据一致性验证
- API响应格式兼容性

### 阶段四：异常和性能测试（2天）
- 异常场景处理
- 权限边界测试  
- 性能和并发测试

## 测试环境配置

### 数据库配置
```sql
-- 创建测试数据
INSERT INTO tp_dept (dept_id, dept_name) VALUES ('DEPT_001', '测试部门');
INSERT INTO tp_person_basicinfo (person_id, person_name, dept_id) VALUES ('PERSON_001', '测试用户', 'DEPT_001');
INSERT INTO tp_account (account_id, username, person_id) VALUES ('ACCOUNT_001', 'testuser', 'PERSON_001');
```

### 应用配置
```yaml
# application-test.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test_db
  security:
    password-encryption: false  # 测试环境关闭密码加密
logging:
  level:
    com.jiuxi.module.user: DEBUG
```

## 测试报告要求

### 测试结果记录
- 测试用例执行状态
- 发现的问题清单
- 性能指标记录
- 兼容性验证结果

### 问题分类
- **严重问题**：功能无法使用
- **一般问题**：功能异常但有绕过方案
- **建议优化**：功能正常但体验可优化

### 测试通过标准
- 所有核心功能测试用例通过率 ≥ 95%
- 所有接口响应时间 ≤ 3秒
- 兼容性测试100%通过
- 无严重级别问题