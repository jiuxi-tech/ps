# role模块接口说明文档

> **文档生成时间**：2025-09-15  
> **模块名称**：role（角色权限管理模块）  
> **控制器类**：AuthorizationRoleController

## 接口概述

role模块提供完整的角色权限管理功能，包括角色的增删改查、权限配置、人员授权等核心业务操作。所有接口均基于RESTful设计原则，支持标准的HTTP请求方式。

## 接口详细说明

### 1. 角色授权列表查询接口

**接口URL**: `/sys/role/roleAuthList`  
**请求方式**: POST/GET  
**接口作用**: 查询当前登录用户拥有的角色授权列表

**入参说明**:
- `query` (TpRoleAuthQuery): 角色授权查询条件对象
- `jwtpid` (String): JWT中的人员ID
- `jwtdid` (String): JWT中的部门ID  
- `jwtrids` (String): JWT中的角色ID集合
- `jwtaid` (String): JWT中的应用ID

**返回类型**: `JsonResponse<LinkedHashSet<TpRoleVO>>`
- 成功返回角色列表集合
- 使用LinkedHashSet保证顺序和唯一性

### 2. 角色授权列表查询接口（兼容版本）

**接口URL**: `/sys/role/role-auth-list`  
**请求方式**: POST/GET  
**接口作用**: 人员授权时的待选角色列表，查询自己创建的角色以及拥有的角色（向后兼容接口）

**入参说明**:
- `query` (TpRoleAuthQuery): 角色授权查询条件对象
- `jwtpid` (String): JWT中的人员ID
- `jwtdid` (String): JWT中的部门ID
- `jwtrids` (String): JWT中的角色ID集合  
- `jwtaid` (String): JWT中的应用ID

**返回类型**: `JsonResponse<LinkedHashSet<TpRoleVO>>`
**特殊说明**: 该接口使用@IgnoreAuthorization注解，跳过权限验证

### 3. 角色分页列表查询接口

**接口URL**: `/sys/role/list`  
**请求方式**: POST/GET  
**接口作用**: 分页查询角色列表，支持多种筛选条件

**入参说明**:
- `query` (TpRoleQuery): 角色查询条件对象，包含分页参数
- `jwtpid` (String): JWT中的人员ID
- `jwtrids` (String): JWT中的角色ID集合
- `jwtaid` (String): JWT中的应用ID

**返回类型**: `JsonResponse<IPage<TpRoleVO>>`
- 返回分页结果对象，包含总数、当前页数据等信息

### 4. 政府角色列表查询接口

**接口URL**: `/sys/role/org/list`  
**请求方式**: POST/GET  
**接口作用**: 查询政府类别的角色列表，自动设置category=0筛选政府角色

**入参说明**:
- `query` (TpRoleQuery): 角色查询条件对象
- `jwtpid` (String): JWT中的人员ID
- `jwtrids` (String): JWT中的角色ID集合
- `jwtaid` (String): JWT中的应用ID

**返回类型**: `JsonResponse<IPage<TpRoleVO>>`
**特殊说明**: 自动设置query.category=0，专门查询政府类别角色

### 5. 角色列表查询接口（不分页）

**接口URL**: `/sys/role/getList`  
**请求方式**: POST/GET  
**接口作用**: 获取完整的角色列表，不进行分页处理

**入参说明**:
- `query` (TpRoleQuery): 角色查询条件对象
- `jwtaid` (String): JWT中的应用ID

**返回类型**: `JsonResponse<List<TpRoleVO>>`
- 返回完整的角色列表

### 6. 角色新增接口

**接口URL**: `/sys/role/add`  
**请求方式**: POST  
**接口作用**: 新增普通角色

**入参说明**:
- `vo` (TpRoleVO): 角色数据对象，使用@Validated(AddGroup.class)进行参数校验
- `jwtpid` (String): JWT中的人员ID
- `jwtaid` (String): JWT中的应用ID

**返回类型**: `JsonResponse<Integer>`
- 返回操作影响的行数

### 7. 政府角色新增接口

**接口URL**: `/sys/role/org/add`  
**请求方式**: POST  
**接口作用**: 新增政府类别角色

**入参说明**:
- `vo` (TpRoleVO): 角色数据对象，使用@Validated(AddGroup.class)进行参数校验
- `jwtpid` (String): JWT中的人员ID
- `jwtaid` (String): JWT中的应用ID

**返回类型**: `JsonResponse<Integer>`
**特殊说明**: 自动设置category=0，创建政府类别角色

### 8. 用户授权接口

**接口URL**: `/sys/role/auth-add`  
**请求方式**: POST  
**接口作用**: 为指定用户添加角色权限，转发到UserPersonController处理

**入参说明**:
- `personId` (String): 人员ID，必填参数
- `deptId` (String): 部门ID，必填参数  
- `roleIds` (String): 角色ID列表，逗号分隔

**返回类型**: `JsonResponse`
**特殊说明**: 
- 包含参数验证逻辑，确保personId和deptId不为空
- 自动处理roleIds开头逗号的格式问题
- 转发请求到UserPersonController.authAdd方法

### 9. 角色更新接口

**接口URL**: `/sys/role/update`  
**请求方式**: POST  
**接口作用**: 更新现有角色信息

**入参说明**:
- `vo` (TpRoleVO): 角色数据对象，使用@Validated(UpdateGroup.class)进行参数校验
- `jwtpid` (String): JWT中的人员ID

**返回类型**: `JsonResponse<Integer>`
- 返回操作影响的行数

### 10. 角色详情查看接口

**接口URL**: `/sys/role/view`  
**请求方式**: GET  
**接口作用**: 根据角色ID查询角色详细信息

**入参说明**:
- `roleId` (String): 角色ID

**返回类型**: `JsonResponse<TpRoleVO>`
- 返回角色详细信息对象

### 11. 角色人员关系查询接口

**接口URL**: `/sys/role/selectByRoleId`  
**请求方式**: GET  
**接口作用**: 根据角色ID查询该角色下的所有人员关系

**入参说明**:
- `roleId` (String): 角色ID

**返回类型**: `JsonResponse<List<TpPersonRoleVO>>`
- 返回角色人员关系列表

### 12. 角色人员关系查询接口（兼容版本）

**接口URL**: `/sys/role/select-by-roleid`  
**请求方式**: GET  
**接口作用**: 根据角色ID查询该角色下的所有人员关系（小写连字符形式，兼容前端调用）

**入参说明**:
- `roleId` (String): 角色ID

**返回类型**: `JsonResponse<List<TpPersonRoleVO>>`
**特殊说明**: 与selectByRoleId功能完全相同，提供不同的URL格式支持

### 13. 角色删除接口

**接口URL**: `/sys/role/delete`  
**请求方式**: POST  
**接口作用**: 删除指定角色

**入参说明**:
- `roleId` (String): 要删除的角色ID
- `creator` (String): 创建者ID
- `jwtpid` (String): JWT中的人员ID

**返回类型**: `JsonResponse<Integer>`
- 返回操作影响的行数

### 14. 权限树获取接口

**接口URL**: `/sys/role/authTree` 或 `/sys/role/auth-tree`  
**请求方式**: GET  
**接口作用**: 获取角色的权限树结构，用于权限配置界面

**入参说明**:
- `roleId` (String): 角色ID
- `jwtrids` (String): JWT中的角色ID集合
- `jwtpid` (String): JWT中的人员ID

**返回类型**: `JsonResponse<List<TreeNode>>`
**特殊说明**: 
- 支持两种URL格式：驼峰式和连字符式
- 使用@IgnoreAuthorization注解，跳过权限验证

### 15. 角色菜单权限配置接口

**接口URL**: `/sys/role/roleMenus`  
**请求方式**: POST  
**接口作用**: 为指定角色配置菜单权限

**入参说明**:
- `roleId` (String): 角色ID
- `menuIds` (String): 菜单ID列表，逗号分隔

**返回类型**: `JsonResponse<Integer>`
- 返回操作影响的行数

## 接口分类总结

### 查询类接口（Query）
1. `/sys/role/roleAuthList` - 角色授权列表查询
2. `/sys/role/role-auth-list` - 角色授权列表查询（兼容版本）
3. `/sys/role/list` - 角色分页列表查询
4. `/sys/role/org/list` - 政府角色列表查询
5. `/sys/role/getList` - 角色列表查询（不分页）
6. `/sys/role/view` - 角色详情查看
7. `/sys/role/selectByRoleId` - 角色人员关系查询
8. `/sys/role/select-by-roleid` - 角色人员关系查询（兼容版本）
9. `/sys/role/authTree` - 权限树获取
10. `/sys/role/auth-tree` - 权限树获取（兼容版本）

### 命令类接口（Command）
1. `/sys/role/add` - 角色新增
2. `/sys/role/org/add` - 政府角色新增  
3. `/sys/role/auth-add` - 用户授权
4. `/sys/role/update` - 角色更新
5. `/sys/role/delete` - 角色删除
6. `/sys/role/roleMenus` - 角色菜单权限配置

## 安全机制

1. **权限控制**: 控制器级别使用@Authorization注解进行权限验证
2. **参数验证**: 使用@Validated注解进行输入参数校验
3. **JWT认证**: 通过JWT参数获取用户身份信息
4. **例外处理**: 部分查询接口使用@IgnoreAuthorization跳过权限验证

## 响应格式

所有接口统一使用JsonResponse包装响应结果：
```json
{
  "success": true/false,
  "message": "操作结果消息",
  "data": "具体返回数据"
}
```