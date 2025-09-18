# auth模块接口业务说明文档

> **生成时间**：2025-09-19  
> **模块名称**：auth权限管理模块  
> **版本**：v1.0

## 1. 模块概述

auth模块是系统权限管理的核心模块，负责菜单、权限、角色的全生命周期管理，以及权限分配和授权控制。模块采用RBAC（基于角色的访问控制）模型，支持多租户架构。

## 2. 菜单管理接口

### 2.1 创建菜单

**接口URL**: `POST /api/v1/menus`

**接口作用**: 创建新的系统菜单项

**入参说明**:
- **RequestBody**: MenuCreateDTO
  - `menuCode` (String): 菜单编码，唯一标识
  - `menuName` (String): 菜单名称
  - `menuTitle` (String): 菜单标题
  - `parentMenuId` (String): 父菜单ID，支持层级结构
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID
  - `X-Tenant-Id` (String): 租户ID

**返回类型**: JsonResponse<String>
- 成功: 返回新创建的菜单ID
- 失败: 返回错误信息

### 2.2 更新菜单信息

**接口URL**: `PUT /api/v1/menus/{menuId}`

**接口作用**: 更新已存在菜单的基本信息

**入参说明**:
- **PathVariable**: 
  - `menuId` (String): 菜单ID
- **RequestBody**: MenuUpdateDTO
  - `menuName` (String): 菜单名称
  - `menuTitle` (String): 菜单标题
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID

**返回类型**: JsonResponse<String>
- 成功: 返回"更新成功"
- 失败: 返回错误信息

### 2.3 删除菜单

**接口URL**: `DELETE /api/v1/menus/{menuId}`

**接口作用**: 删除指定的菜单项

**入参说明**:
- **PathVariable**: 
  - `menuId` (String): 菜单ID

**返回类型**: JsonResponse<String>
- 成功: 返回"删除成功"
- 失败: 返回错误信息

### 2.4 移动菜单

**接口URL**: `PUT /api/v1/menus/{menuId}/move`

**接口作用**: 调整菜单层级结构，移动菜单到新的父级下

**入参说明**:
- **PathVariable**: 
  - `menuId` (String): 要移动的菜单ID
- **RequestParam**: 
  - `newParentId` (String): 新的父菜单ID
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID

**返回类型**: JsonResponse<String>
- 成功: 返回"移动成功"
- 失败: 返回错误信息

### 2.5 启用菜单

**接口URL**: `PUT /api/v1/menus/{menuId}/enable`

**接口作用**: 启用指定菜单，使其在系统中可见

**入参说明**:
- **PathVariable**: 
  - `menuId` (String): 菜单ID
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID

**返回类型**: JsonResponse<String>
- 成功: 返回"启用成功"
- 失败: 返回错误信息

### 2.6 停用菜单

**接口URL**: `PUT /api/v1/menus/{menuId}/disable`

**接口作用**: 停用指定菜单，使其在系统中不可见

**入参说明**:
- **PathVariable**: 
  - `menuId` (String): 菜单ID
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID

**返回类型**: JsonResponse<String>
- 成功: 返回"停用成功"
- 失败: 返回错误信息

## 3. 权限管理接口

### 3.1 创建权限

**接口URL**: `POST /api/v1/permissions`

**接口作用**: 创建新的系统权限

**入参说明**:
- **RequestBody**: PermissionCreateDTO
  - `permissionCode` (String): 权限编码，唯一标识
  - `permissionName` (String): 权限名称
  - `permissionDesc` (String): 权限描述
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID
  - `X-Tenant-Id` (String): 租户ID

**返回类型**: JsonResponse<String>
- 成功: 返回新创建的权限ID
- 失败: 返回错误信息

### 3.2 更新权限信息

**接口URL**: `PUT /api/v1/permissions/{permissionId}`

**接口作用**: 更新已存在权限的基本信息

**入参说明**:
- **PathVariable**: 
  - `permissionId` (String): 权限ID
- **RequestBody**: PermissionUpdateDTO
  - `permissionName` (String): 权限名称
  - `permissionDesc` (String): 权限描述
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID

**返回类型**: JsonResponse<String>
- 成功: 返回"更新成功"
- 失败: 返回错误信息

### 3.3 删除权限

**接口URL**: `DELETE /api/v1/permissions/{permissionId}`

**接口作用**: 删除指定的权限

**入参说明**:
- **PathVariable**: 
  - `permissionId` (String): 权限ID

**返回类型**: JsonResponse<String>
- 成功: 返回"删除成功"
- 失败: 返回错误信息

### 3.4 启用权限

**接口URL**: `PUT /api/v1/permissions/{permissionId}/enable`

**接口作用**: 启用指定权限，使其可以被分配和使用

**入参说明**:
- **PathVariable**: 
  - `permissionId` (String): 权限ID
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID

**返回类型**: JsonResponse<String>
- 成功: 返回"启用成功"
- 失败: 返回错误信息

### 3.5 停用权限

**接口URL**: `PUT /api/v1/permissions/{permissionId}/disable`

**接口作用**: 停用指定权限，使其不可被分配和使用

**入参说明**:
- **PathVariable**: 
  - `permissionId` (String): 权限ID
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID

**返回类型**: JsonResponse<String>
- 成功: 返回"停用成功"
- 失败: 返回错误信息

## 4. 角色管理接口

### 4.1 创建角色

**接口URL**: `POST /api/v1/roles`

**接口作用**: 创建新的系统角色

**入参说明**:
- **RequestBody**: RoleCreateDTO
  - `roleCode` (String): 角色编码，唯一标识
  - `roleName` (String): 角色名称
  - `roleDesc` (String): 角色描述
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID
  - `X-Tenant-Id` (String): 租户ID

**返回类型**: JsonResponse<String>
- 成功: 返回新创建的角色ID
- 失败: 返回错误信息

### 4.2 更新角色信息

**接口URL**: `PUT /api/v1/roles/{roleId}`

**接口作用**: 更新已存在角色的基本信息

**入参说明**:
- **PathVariable**: 
  - `roleId` (String): 角色ID
- **RequestBody**: RoleUpdateDTO
  - `roleName` (String): 角色名称
  - `roleDesc` (String): 角色描述
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID

**返回类型**: JsonResponse<String>
- 成功: 返回"更新成功"
- 失败: 返回错误信息

### 4.3 删除角色

**接口URL**: `DELETE /api/v1/roles/{roleId}`

**接口作用**: 删除指定的角色

**入参说明**:
- **PathVariable**: 
  - `roleId` (String): 角色ID

**返回类型**: JsonResponse<String>
- 成功: 返回"删除成功"
- 失败: 返回错误信息

### 4.4 启用角色

**接口URL**: `PUT /api/v1/roles/{roleId}/enable`

**接口作用**: 启用指定角色，使其可以被分配给用户

**入参说明**:
- **PathVariable**: 
  - `roleId` (String): 角色ID
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID

**返回类型**: JsonResponse<String>
- 成功: 返回"启用成功"
- 失败: 返回错误信息

### 4.5 停用角色

**接口URL**: `PUT /api/v1/roles/{roleId}/disable`

**接口作用**: 停用指定角色，使其不可被分配给用户

**入参说明**:
- **PathVariable**: 
  - `roleId` (String): 角色ID
- **RequestHeader**: 
  - `X-User-Person-Id` (String): 操作人员ID

**返回类型**: JsonResponse<String>
- 成功: 返回"停用成功"
- 失败: 返回错误信息

### 4.6 为角色分配权限

**接口URL**: `POST /api/v1/roles/{roleId}/permissions`

**接口作用**: 为指定角色分配权限，建立角色-权限关联关系

**入参说明**:
- **PathVariable**: 
  - `roleId` (String): 角色ID
- **RequestBody**: List<String>
  - 权限ID列表，支持批量分配

**返回类型**: JsonResponse<String>
- 成功: 返回"权限分配成功"
- 失败: 返回错误信息

### 4.7 为角色分配菜单

**接口URL**: `POST /api/v1/roles/{roleId}/menus`

**接口作用**: 为指定角色分配菜单，建立角色-菜单关联关系

**入参说明**:
- **PathVariable**: 
  - `roleId` (String): 角色ID
- **RequestBody**: List<String>
  - 菜单ID列表，支持批量分配

**返回类型**: JsonResponse<String>
- 成功: 返回"菜单分配成功"
- 失败: 返回错误信息

## 5. 通用规范

### 5.1 认证与授权

所有接口都使用 `@Authorization` 注解进行权限控制，需要携带有效的认证信息。

### 5.2 多租户支持

创建操作需要传递 `X-Tenant-Id` 头部信息，用于多租户数据隔离。

### 5.3 审计日志

所有修改操作都需要传递 `X-User-Person-Id` 头部信息，用于操作审计和日志记录。

### 5.4 异常处理

所有接口均采用统一的异常处理机制，返回标准的JsonResponse格式。

### 5.5 业务约束

- 菜单支持层级结构，删除父菜单时需要先删除子菜单
- 权限被角色使用时不能直接删除
- 角色被用户使用时不能直接删除
- 停用的权限和菜单不会在权限分配时显示