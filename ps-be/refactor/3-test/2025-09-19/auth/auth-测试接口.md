# auth模块接口测试文档

> **生成时间**：2025-09-19  
> **模块名称**：auth权限管理模块  
> **测试版本**：v1.0

## 1. 测试环境准备

### 1.1 前置条件
- 系统已正常启动
- 数据库连接正常
- 测试账号具有管理员权限
- 准备测试租户ID：`test-tenant-001`
- 准备测试操作员ID：`test-user-001`

### 1.2 测试数据准备
```json
{
  "testTenantId": "test-tenant-001",
  "testUserId": "test-user-001",
  "testMenuCode": "TEST_MENU_001",
  "testPermissionCode": "TEST_PERMISSION_001",
  "testRoleCode": "TEST_ROLE_001"
}
```

## 2. 菜单管理接口测试

### 2.1 创建菜单测试

**测试用例**: TC_MENU_001 - 创建菜单正常流程

**测试步骤**:
1. 发送POST请求到 `/api/v1/menus`
2. 设置请求头：
   - `X-User-Person-Id: test-user-001`
   - `X-Tenant-Id: test-tenant-001`
   - `Content-Type: application/json`
3. 发送请求体：
```json
{
  "menuCode": "TEST_MENU_001",
  "menuName": "测试菜单",
  "menuTitle": "测试菜单标题",
  "parentMenuId": null
}
```

**预期结果**:
- HTTP状态码：200
- 响应体包含成功标志和新创建的菜单ID
- 菜单记录成功保存到数据库

**测试用例**: TC_MENU_002 - 创建菜单异常测试（重复菜单编码）

**测试步骤**:
1. 重复执行TC_MENU_001的步骤
2. 使用相同的menuCode再次创建菜单

**预期结果**:
- HTTP状态码：200
- 响应体包含失败标志和错误信息
- 提示菜单编码已存在

### 2.2 更新菜单测试

**测试用例**: TC_MENU_003 - 更新菜单正常流程

**测试步骤**:
1. 先执行TC_MENU_001创建菜单，获取menuId
2. 发送PUT请求到 `/api/v1/menus/{menuId}`
3. 设置请求头：
   - `X-User-Person-Id: test-user-001`
   - `Content-Type: application/json`
4. 发送请求体：
```json
{
  "menuName": "更新后的菜单名称",
  "menuTitle": "更新后的菜单标题"
}
```

**预期结果**:
- HTTP状态码：200
- 响应体包含"更新成功"消息
- 数据库中菜单信息已更新

### 2.3 删除菜单测试

**测试用例**: TC_MENU_004 - 删除菜单正常流程

**测试步骤**:
1. 使用TC_MENU_001创建的菜单ID
2. 发送DELETE请求到 `/api/v1/menus/{menuId}`

**预期结果**:
- HTTP状态码：200
- 响应体包含"删除成功"消息
- 数据库中菜单记录已删除

### 2.4 移动菜单测试

**测试用例**: TC_MENU_005 - 移动菜单正常流程

**测试步骤**:
1. 创建两个菜单：父菜单和子菜单
2. 发送PUT请求到 `/api/v1/menus/{childMenuId}/move?newParentId={parentMenuId}`
3. 设置请求头：`X-User-Person-Id: test-user-001`

**预期结果**:
- HTTP状态码：200
- 响应体包含"移动成功"消息
- 菜单层级关系更新正确

### 2.5 菜单状态切换测试

**测试用例**: TC_MENU_006 - 启用菜单

**测试步骤**:
1. 使用已创建的菜单ID
2. 发送PUT请求到 `/api/v1/menus/{menuId}/enable`
3. 设置请求头：`X-User-Person-Id: test-user-001`

**预期结果**:
- HTTP状态码：200
- 响应体包含"启用成功"消息

**测试用例**: TC_MENU_007 - 停用菜单

**测试步骤**:
1. 发送PUT请求到 `/api/v1/menus/{menuId}/disable`
2. 设置请求头：`X-User-Person-Id: test-user-001`

**预期结果**:
- HTTP状态码：200
- 响应体包含"停用成功"消息

## 3. 权限管理接口测试

### 3.1 创建权限测试

**测试用例**: TC_PERMISSION_001 - 创建权限正常流程

**测试步骤**:
1. 发送POST请求到 `/api/v1/permissions`
2. 设置请求头：
   - `X-User-Person-Id: test-user-001`
   - `X-Tenant-Id: test-tenant-001`
   - `Content-Type: application/json`
3. 发送请求体：
```json
{
  "permissionCode": "TEST_PERMISSION_001",
  "permissionName": "测试权限",
  "permissionDesc": "这是一个测试权限"
}
```

**预期结果**:
- HTTP状态码：200
- 响应体包含成功标志和新创建的权限ID
- 权限记录成功保存到数据库

### 3.2 更新权限测试

**测试用例**: TC_PERMISSION_002 - 更新权限正常流程

**测试步骤**:
1. 先执行TC_PERMISSION_001创建权限，获取permissionId
2. 发送PUT请求到 `/api/v1/permissions/{permissionId}`
3. 设置请求头：
   - `X-User-Person-Id: test-user-001`
   - `Content-Type: application/json`
4. 发送请求体：
```json
{
  "permissionName": "更新后的权限名称",
  "permissionDesc": "更新后的权限描述"
}
```

**预期结果**:
- HTTP状态码：200
- 响应体包含"更新成功"消息

### 3.3 删除权限测试

**测试用例**: TC_PERMISSION_003 - 删除权限正常流程

**测试步骤**:
1. 使用TC_PERMISSION_001创建的权限ID
2. 发送DELETE请求到 `/api/v1/permissions/{permissionId}`

**预期结果**:
- HTTP状态码：200
- 响应体包含"删除成功"消息

### 3.4 权限状态切换测试

**测试用例**: TC_PERMISSION_004 - 启用权限

**测试步骤**:
1. 发送PUT请求到 `/api/v1/permissions/{permissionId}/enable`
2. 设置请求头：`X-User-Person-Id: test-user-001`

**预期结果**:
- HTTP状态码：200
- 响应体包含"启用成功"消息

**测试用例**: TC_PERMISSION_005 - 停用权限

**测试步骤**:
1. 发送PUT请求到 `/api/v1/permissions/{permissionId}/disable`
2. 设置请求头：`X-User-Person-Id: test-user-001`

**预期结果**:
- HTTP状态码：200
- 响应体包含"停用成功"消息

## 4. 角色管理接口测试

### 4.1 创建角色测试

**测试用例**: TC_ROLE_001 - 创建角色正常流程

**测试步骤**:
1. 发送POST请求到 `/api/v1/roles`
2. 设置请求头：
   - `X-User-Person-Id: test-user-001`
   - `X-Tenant-Id: test-tenant-001`
   - `Content-Type: application/json`
3. 发送请求体：
```json
{
  "roleCode": "TEST_ROLE_001",
  "roleName": "测试角色",
  "roleDesc": "这是一个测试角色"
}
```

**预期结果**:
- HTTP状态码：200
- 响应体包含成功标志和新创建的角色ID
- 角色记录成功保存到数据库

### 4.2 更新角色测试

**测试用例**: TC_ROLE_002 - 更新角色正常流程

**测试步骤**:
1. 先执行TC_ROLE_001创建角色，获取roleId
2. 发送PUT请求到 `/api/v1/roles/{roleId}`
3. 设置请求头：
   - `X-User-Person-Id: test-user-001`
   - `Content-Type: application/json`
4. 发送请求体：
```json
{
  "roleName": "更新后的角色名称",
  "roleDesc": "更新后的角色描述"
}
```

**预期结果**:
- HTTP状态码：200
- 响应体包含"更新成功"消息

### 4.3 删除角色测试

**测试用例**: TC_ROLE_003 - 删除角色正常流程

**测试步骤**:
1. 使用TC_ROLE_001创建的角色ID
2. 发送DELETE请求到 `/api/v1/roles/{roleId}`

**预期结果**:
- HTTP状态码：200
- 响应体包含"删除成功"消息

### 4.4 角色权限分配测试

**测试用例**: TC_ROLE_004 - 为角色分配权限

**测试步骤**:
1. 先创建角色和权限（使用TC_ROLE_001和TC_PERMISSION_001）
2. 发送POST请求到 `/api/v1/roles/{roleId}/permissions`
3. 设置请求头：`Content-Type: application/json`
4. 发送请求体：
```json
["permission-id-1", "permission-id-2"]
```

**预期结果**:
- HTTP状态码：200
- 响应体包含"权限分配成功"消息
- 角色权限关联关系创建成功

### 4.5 角色菜单分配测试

**测试用例**: TC_ROLE_005 - 为角色分配菜单

**测试步骤**:
1. 先创建角色和菜单（使用TC_ROLE_001和TC_MENU_001）
2. 发送POST请求到 `/api/v1/roles/{roleId}/menus`
3. 设置请求头：`Content-Type: application/json`
4. 发送请求体：
```json
["menu-id-1", "menu-id-2"]
```

**预期结果**:
- HTTP状态码：200
- 响应体包含"菜单分配成功"消息
- 角色菜单关联关系创建成功

## 5. 异常场景测试

### 5.1 参数验证测试

**测试用例**: TC_ERROR_001 - 缺少必填参数

**测试步骤**:
1. 发送创建菜单请求，但omit menuCode字段
2. 观察系统响应

**预期结果**:
- HTTP状态码：400或返回业务错误
- 响应体包含参数验证错误信息

### 5.2 权限验证测试

**测试用例**: TC_ERROR_002 - 无权限访问

**测试步骤**:
1. 使用无权限的token访问接口
2. 观察系统响应

**预期结果**:
- HTTP状态码：401或403
- 响应体包含权限不足错误信息

### 5.3 资源不存在测试

**测试用例**: TC_ERROR_003 - 访问不存在的资源

**测试步骤**:
1. 使用不存在的ID访问更新、删除接口
2. 观察系统响应

**预期结果**:
- HTTP状态码：404或返回业务错误
- 响应体包含资源不存在错误信息

## 6. 性能测试

### 6.1 并发创建测试

**测试用例**: TC_PERFORMANCE_001 - 并发创建角色

**测试步骤**:
1. 模拟10个并发请求同时创建角色
2. 每个请求使用不同的roleCode
3. 观察响应时间和成功率

**预期结果**:
- 所有请求都应该成功
- 平均响应时间<500ms
- 无数据一致性问题

### 6.2 批量查询测试

**测试用例**: TC_PERFORMANCE_002 - 批量查询测试

**测试步骤**:
1. 先创建100个菜单、权限、角色
2. 模拟用户登录时的权限查询场景
3. 测量响应时间

**预期结果**:
- 查询响应时间<200ms
- 内存使用稳定
- 缓存命中率>80%

## 7. 测试数据清理

### 7.1 清理步骤

**执行顺序**:
1. 删除测试角色的权限和菜单关联
2. 删除测试角色
3. 删除测试权限
4. 删除测试菜单
5. 清理测试数据库记录

### 7.2 清理验证

**验证内容**:
- 确认所有测试数据已从数据库删除
- 确认缓存已清理
- 确认系统状态恢复正常

## 8. 测试报告模板

### 8.1 测试执行记录

| 测试用例编号 | 测试用例名称 | 执行状态 | 执行时间 | 备注 |
|-------------|-------------|----------|----------|------|
| TC_MENU_001 | 创建菜单正常流程 | 通过 | 2025-09-19 10:00 | 无 |
| TC_MENU_002 | 创建菜单异常测试 | 通过 | 2025-09-19 10:05 | 无 |
| ... | ... | ... | ... | ... |

### 8.2 缺陷记录

| 缺陷编号 | 缺陷描述 | 严重级别 | 发现时间 | 修复状态 |
|---------|---------|----------|----------|----------|
| BUG_001 | 权限分配接口返回码不正确 | 中等 | 2025-09-19 | 待修复 |
| ... | ... | ... | ... | ... |

### 8.3 测试结论

测试完成后，需要评估：
- 功能完整性：是否所有接口都正常工作
- 性能指标：是否满足性能要求
- 安全性：权限控制是否有效
- 稳定性：长时间运行是否稳定
- 用户体验：接口响应是否符合预期