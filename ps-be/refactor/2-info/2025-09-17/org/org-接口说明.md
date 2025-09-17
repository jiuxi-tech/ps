# org模块接口业务说明文档

> **生成时间**：2025-09-17  
> **模块路径**：`D:\keycloak_sb_sso_new0910_claude\ps\ps-be\src\main\java\com\jiuxi\module\org`  
> **文档版本**：v1.0

## 📋 接口概览

org模块提供组织架构管理相关的接口服务，主要包括部门管理、企业管理、组织架构管理等功能。模块共包含4个控制器，提供50余个接口。

## 🏢 1. 部门管理接口 (DepartmentController)

**基础路径**：`/api/v1/departments`

### 1.1 基础CRUD操作

#### 1.1.1 创建部门
- **接口路径**：`POST /api/v1/departments`
- **接口作用**：创建新的部门
- **入参类型**：
  - `@RequestBody DepartmentCreateDTO` - 部门创建信息
  - `@RequestHeader("X-User-Person-Id") String operator` - 操作者ID
  - `@RequestHeader("X-Tenant-Id") String tenantId` - 租户ID
- **返回类型**：`JsonResponse<DepartmentResponseDTO>` - 创建成功的部门信息
- **业务说明**：支持部门层级管理，自动计算部门路径和层级，支持多租户隔离

#### 1.1.2 更新部门信息
- **接口路径**：`PUT /api/v1/departments/{deptId}`
- **接口作用**：更新指定部门的信息
- **入参类型**：
  - `@PathVariable String deptId` - 部门ID
  - `@RequestBody DepartmentUpdateDTO` - 部门更新信息
  - `@RequestHeader("X-User-Person-Id") String operator` - 操作者ID
  - `@RequestHeader("X-Tenant-Id") String tenantId` - 租户ID
- **返回类型**：`JsonResponse<DepartmentResponseDTO>` - 更新后的部门信息
- **业务说明**：支持部门移动操作，会重新计算层级和路径信息

#### 1.1.3 查看部门详情
- **接口路径**：`GET /api/v1/departments/{deptId}`
- **接口作用**：根据部门ID查询部门详细信息
- **入参类型**：
  - `@PathVariable String deptId` - 部门ID
- **返回类型**：`JsonResponse<DepartmentResponseDTO>` - 部门详细信息
- **业务说明**：返回完整的部门信息，包括基本信息、层级信息、状态信息等

#### 1.1.4 删除部门
- **接口路径**：`DELETE /api/v1/departments/{deptId}`
- **接口作用**：删除指定的部门
- **入参类型**：
  - `@PathVariable String deptId` - 部门ID
  - `@RequestHeader("X-User-Person-Id") String operator` - 操作者ID
  - `@RequestHeader("X-Tenant-Id") String tenantId` - 租户ID
- **返回类型**：`JsonResponse<String>` - 删除操作结果
- **业务说明**：删除前会进行业务规则验证，确保不破坏组织架构完整性

### 1.2 部门树形查询

#### 1.2.1 获取部门树形结构
- **接口路径**：`GET /api/v1/departments/tree`
- **接口作用**：获取完整的部门树形结构
- **入参类型**：
  - `@RequestHeader("X-Tenant-Id") String tenantId` - 租户ID
- **返回类型**：`JsonResponse<List<DepartmentResponseDTO>>` - 部门树形数据
- **业务说明**：返回层级化的部门结构，支持前端树形展示

#### 1.2.2 根据父部门查询子部门
- **接口路径**：`GET /api/v1/departments/parent/{parentDeptId}`
- **接口作用**：查询指定父部门下的直接子部门
- **入参类型**：
  - `@PathVariable String parentDeptId` - 父部门ID
- **返回类型**：`JsonResponse<List<DepartmentResponseDTO>>` - 子部门列表
- **业务说明**：只返回直接子部门，不包含更深层级的后代部门

#### 1.2.3 获取根部门列表
- **接口路径**：`GET /api/v1/departments/root`
- **接口作用**：获取租户下的所有根部门
- **入参类型**：
  - `@RequestHeader("X-Tenant-Id") String tenantId` - 租户ID
- **返回类型**：`JsonResponse<List<DepartmentResponseDTO>>` - 根部门列表
- **业务说明**：返回没有父部门的顶级部门列表

### 1.3 部门状态管理

#### 1.3.1 启用部门
- **接口路径**：`PUT /api/v1/departments/{deptId}/enable`
- **接口作用**：启用指定的部门
- **入参类型**：
  - `@PathVariable String deptId` - 部门ID
  - `@RequestHeader("X-User-Person-Id") String operator` - 操作者ID
- **返回类型**：`JsonResponse<String>` - 启用操作结果
- **业务说明**：将部门状态设置为激活状态

#### 1.3.2 停用部门
- **接口路径**：`PUT /api/v1/departments/{deptId}/disable`
- **接口作用**：停用指定的部门
- **入参类型**：
  - `@PathVariable String deptId` - 部门ID
  - `@RequestHeader("X-User-Person-Id") String operator` - 操作者ID
- **返回类型**：`JsonResponse<String>` - 停用操作结果
- **业务说明**：将部门状态设置为非激活状态，但不删除数据

### 1.4 高级查询功能

#### 1.4.1 查询部门祖先链
- **接口路径**：`GET /api/v1/departments/{deptId}/ancestors`
- **接口作用**：获取指定部门的所有祖先部门链
- **入参类型**：
  - `@PathVariable String deptId` - 部门ID
- **返回类型**：`JsonResponse<List<DepartmentResponseDTO>>` - 祖先部门列表
- **业务说明**：从根部门到直接父部门的完整路径

#### 1.4.2 查询部门完整子树
- **接口路径**：`GET /api/v1/departments/{deptId}/descendants`
- **接口作用**：获取指定部门的所有后代部门
- **入参类型**：
  - `@PathVariable String deptId` - 部门ID
  - `@RequestParam(defaultValue = "false") Boolean includeInactive` - 是否包含停用部门
- **返回类型**：`JsonResponse<List<DepartmentResponseDTO>>` - 后代部门列表
- **业务说明**：返回所有层级的后代部门，可选择是否包含停用部门

#### 1.4.3 按层级查询部门
- **接口路径**：`GET /api/v1/departments/level/{level}`
- **接口作用**：查询指定层级的所有部门
- **入参类型**：
  - `@PathVariable Integer level` - 部门层级
  - `@RequestHeader("X-Tenant-Id") String tenantId` - 租户ID
- **返回类型**：`JsonResponse<List<DepartmentResponseDTO>>` - 指定层级的部门列表
- **业务说明**：支持按部门层级进行水平查询

### 1.5 部门统计分析

#### 1.5.1 获取部门详细统计
- **接口路径**：`GET /api/v1/departments/{deptId}/statistics`
- **接口作用**：获取指定部门的详细统计信息
- **入参类型**：
  - `@PathVariable String deptId` - 部门ID
- **返回类型**：`JsonResponse<DepartmentStatisticsDTO>` - 部门统计信息
- **业务说明**：包括子部门数量、用户数量、层级信息等统计数据

#### 1.5.2 获取租户部门概览
- **接口路径**：`GET /api/v1/departments/overview`
- **接口作用**：获取租户下所有部门的概览统计
- **入参类型**：
  - `@RequestHeader("X-Tenant-Id") String tenantId` - 租户ID
- **返回类型**：`JsonResponse<TenantDepartmentOverviewDTO>` - 租户部门概览
- **业务说明**：包括总数、激活数、层级分布等概览信息

#### 1.5.3 获取部门层级分布
- **接口路径**：`GET /api/v1/departments/level-distribution`
- **接口作用**：获取租户下部门的层级分布统计
- **入参类型**：
  - `@RequestHeader("X-Tenant-Id") String tenantId` - 租户ID
- **返回类型**：`JsonResponse<Map<Integer, Long>>` - 层级分布数据
- **业务说明**：返回每个层级的部门数量统计

### 1.6 批量操作

#### 1.6.1 批量删除部门
- **接口路径**：`DELETE /api/v1/departments/batch`
- **接口作用**：批量删除多个部门
- **入参类型**：
  - `@RequestBody List<String> deptIds` - 部门ID列表
  - `@RequestHeader("X-User-Person-Id") String operator` - 操作者ID
  - `@RequestHeader("X-Tenant-Id") String tenantId` - 租户ID
- **返回类型**：`JsonResponse<String>` - 批量删除结果
- **业务说明**：支持一次性删除多个部门，会进行批量验证

#### 1.6.2 批量查询部门及子部门
- **接口路径**：`POST /api/v1/departments/batch-with-children`
- **接口作用**：批量查询部门及其子部门信息
- **入参类型**：
  - `@RequestBody List<String> deptIds` - 部门ID列表
  - `@RequestParam(defaultValue = "false") Boolean includeDescendants` - 是否包含所有后代
- **返回类型**：`JsonResponse<List<DepartmentResponseDTO>>` - 部门列表
- **业务说明**：支持批量查询，可选择包含直接子部门或所有后代部门

## 🏢 2. 企业管理接口 (EnterpriseController)

**基础路径**：`/sys/ent`

### 2.1 企业基础管理

#### 2.1.1 分页查询企业列表
- **接口路径**：`POST /sys/ent/list`
- **接口作用**：分页查询企业基本信息列表
- **入参类型**：
  - `TpEntBasicQuery query` - 查询条件
  - `String jwtpid` - JWT人员ID
  - `String jwtaid` - JWT单位ID
- **返回类型**：`JsonResponse<IPage<TpEntBasicinfoVO>>` - 分页企业列表
- **业务说明**：支持条件查询和分页，返回企业基本信息

#### 2.1.2 查看企业基本信息
- **接口路径**：`POST /sys/ent/view`
- **接口作用**：根据企业ID查询企业详细信息
- **入参类型**：
  - `String entId` - 企业ID
- **返回类型**：`JsonResponse<TpEntBasicinfoVO>` - 企业详细信息
- **业务说明**：返回企业的完整基本信息

#### 2.1.3 新增企业基本信息
- **接口路径**：`POST /sys/ent/add`
- **接口作用**：新增企业基本信息
- **入参类型**：
  - `@Validated(AddGroup.class) TpEntBasicinfoVO vo` - 企业信息
  - `String jwtpid` - JWT人员ID
- **返回类型**：`JsonResponse<String>` - 新增的企业ID
- **业务说明**：创建新企业记录，自动设置租户信息

#### 2.1.4 修改企业基本信息
- **接口路径**：`POST /sys/ent/update`
- **接口作用**：修改企业基本信息
- **入参类型**：
  - `@Validated(UpdateGroup.class) TpEntBasicinfoVO vo` - 企业信息
  - `String jwtpid` - JWT人员ID
- **返回类型**：`JsonResponse` - 修改结果
- **业务说明**：更新企业基本信息，支持部分字段更新

#### 2.1.5 删除企业信息
- **接口路径**：`POST /sys/ent/delete`
- **接口作用**：删除指定企业信息
- **入参类型**：
  - `String entId` - 企业ID
  - `String jwtpid` - JWT人员ID
- **返回类型**：`JsonResponse` - 删除结果
- **业务说明**：逻辑删除企业记录

### 2.2 企业账号管理

#### 2.2.1 获取企业管理员列表
- **接口路径**：`POST /sys/ent/ent-admin-list`
- **接口作用**：分页查询企业管理员账号列表
- **入参类型**：
  - `TpEntAccountQuery query` - 查询条件
  - `String jwtpid` - JWT人员ID
- **返回类型**：`JsonResponse<IPage<TpEntAccountVO>>` - 分页管理员列表
- **业务说明**：查询企业的管理员账号信息

#### 2.2.2 查询企业用户账号（已过期）
- **接口路径**：`POST /sys/ent/ent-account-list`
- **接口作用**：分页查询企业下的所有用户账号
- **入参类型**：
  - `TpEntAccountQuery query` - 查询条件
  - `String jwtpid` - JWT人员ID
  - `String jwtaid` - JWT单位ID
- **返回类型**：`JsonResponse<IPage<TpEntAccountVO>>` - 分页账号列表
- **业务说明**：**已过期接口**，建议使用ent-admin-list接口

## 🏢 3. 组织部门管理接口 (OrganizationDepartmentController)

**基础路径**：`/sys/dept`

### 3.1 组织机构树查询

#### 3.1.1 机构树查询
- **接口路径**：`POST /sys/dept/org/tree`
- **接口作用**：获取政府组织机构树形结构
- **入参类型**：
  - `String deptId` - 部门ID（可选）
  - `String jwtdid` - JWT部门ID
  - `@RequestParam(defaultValue = "1") int sync` - 同步标识
- **返回类型**：`JsonResponse<List<TreeNode>>` - 机构树数据
- **业务说明**：根据权限返回可见的政府组织机构树

#### 3.1.2 完整机构树查询
- **接口路径**：`POST /sys/dept/org/all-tree`
- **接口作用**：获取完整的政府组织机构树
- **入参类型**：
  - `@RequestParam(defaultValue = "1") int sync` - 同步标识
  - `String deptId` - 起始部门ID
- **返回类型**：`JsonResponse<List<TreeNode>>` - 完整机构树
- **业务说明**：返回从指定节点开始的完整机构树

#### 3.1.3 企业部门树查询
- **接口路径**：`POST /sys/dept/ent/tree`
- **接口作用**：获取企业部门树形结构
- **入参类型**：
  - `@RequestParam(defaultValue = "1") int sync` - 同步标识
  - `@RequestParam(required = false) String deptId` - 部门ID
  - `String jwtdid` - JWT部门ID
- **返回类型**：`JsonResponse<List<TreeNode>>` - 企业部门树
- **业务说明**：返回企业的部门树形结构，管理员账号返回空列表

### 3.2 层级查询

#### 3.2.1 查询下级部门
- **接口路径**：`POST /sys/dept/children`
- **接口作用**：查询指定部门的下一级单位或部门
- **入参类型**：
  - `String parentId` - 父部门ID
  - `String deptType` - 部门类型（SYS0501:单位, SYS0502:部门, null:全部）
  - `String filterCommAscn` - 过滤委员会（1:过滤）
  - `Integer category` - 分类
- **返回类型**：`JsonResponse<List<TreeNode>>` - 下级部门列表
- **业务说明**：支持按类型过滤查询下级组织

### 3.3 特殊树形查询

#### 3.3.1 行政区划树查询
- **接口路径**：`POST /sys/dept/org/dept-city-tree`
- **接口作用**：获取当前登录人所在单位的行政区划树
- **入参类型**：
  - `String jwtaid` - JWT单位ID
  - `@RequestParam(defaultValue = "1") int sync` - 同步标识
  - `@RequestParam(defaultValue = "false") boolean expand` - 展开标识
  - `@RequestParam(defaultValue = "off") String showTop` - 显示顶级节点
- **返回类型**：`JsonResponse<List<TreeNode>>` - 行政区划树
- **业务说明**：本级及下级的行政区划树形结构

#### 3.3.2 兼职部门树查询
- **接口路径**：`POST /sys/dept/org/parttimejob-tree`
- **接口作用**：获取人员兼职机构树
- **入参类型**：
  - `String personId` - 人员ID
  - `String jwtdid` - JWT部门ID
- **返回类型**：`JsonResponse<List<TreeNode>>` - 兼职机构树
- **业务说明**：用于人员兼职部门设置的机构树查询

### 3.4 部门信息管理

#### 3.4.1 新增政府组织
- **接口路径**：`POST /sys/dept/org/add`
- **接口作用**：添加政府组织根节点或树节点
- **入参类型**：
  - `@Validated(AddGroup.class) TpDeptBasicinfoVO vo` - 部门信息
  - `String jwtpid` - JWT人员ID
- **返回类型**：`JsonResponse<TpDeptBasicinfoVO>` - 创建的部门信息
- **业务说明**：创建政府类型的组织机构节点

#### 3.4.2 新增企业部门
- **接口路径**：`POST /sys/dept/ent/add`
- **接口作用**：添加企业部门根节点或树节点
- **入参类型**：
  - `@Validated(AddGroup.class) TpDeptBasicinfoVO vo` - 部门信息
  - `String jwtpid` - JWT人员ID
- **返回类型**：`JsonResponse<TpDeptBasicinfoVO>` - 创建的部门信息
- **业务说明**：创建企业类型的部门节点

#### 3.4.3 查询部门信息
- **接口路径**：`POST /sys/dept/view`
- **接口作用**：根据部门ID查询部门详细信息
- **入参类型**：
  - `String deptId` - 部门ID
  - `String jwtpid` - JWT人员ID
  - `String jwtaid` - JWT单位ID
- **返回类型**：`JsonResponse<TpDeptBasicinfoVO>` - 部门详细信息
- **业务说明**：返回部门的基本信息

#### 3.4.4 修改部门信息
- **接口路径**：`POST /sys/dept/update`
- **接口作用**：修改部门基本信息
- **入参类型**：
  - `@Validated(UpdateGroup.class) TpDeptBasicinfoVO vo` - 部门信息
  - `String jwtpid` - JWT人员ID
- **返回类型**：`JsonResponse<TpDeptBasicinfoVO>` - 修改后的部门信息
- **业务说明**：更新部门基本信息

#### 3.4.5 删除部门
- **接口路径**：`POST /sys/dept/delete`
- **接口作用**：根据ID删除部门信息
- **入参类型**：
  - `String deptId` - 部门ID
  - `String jwtpid` - JWT人员ID
- **返回类型**：`JsonResponse` - 删除结果
- **业务说明**：删除指定的部门记录

### 3.5 部门扩展信息管理

#### 3.5.1 添加部门扩展信息
- **接口路径**：`POST /sys/dept/exp-add`
- **接口作用**：添加部门扩展信息
- **入参类型**：
  - `@Validated(AddGroup.class) TpDeptExinfoVO vo` - 扩展信息
- **返回类型**：`JsonResponse<TpDeptExinfoVO>` - 扩展信息
- **业务说明**：为部门添加额外的扩展属性信息

#### 3.5.2 查询部门扩展信息
- **接口路径**：`POST /sys/dept/exp-view`
- **接口作用**：查询部门扩展信息
- **入参类型**：
  - `String deptId` - 部门ID
- **返回类型**：`JsonResponse<TpDeptExinfoVO>` - 扩展信息
- **业务说明**：获取部门的扩展属性信息

### 3.6 删除前验证

#### 3.6.1 查询部门绑定人员
- **接口路径**：`POST /sys/dept/select/deptid`
- **接口作用**：删除部门前查询绑定的人员数量
- **入参类型**：
  - `String deptId` - 部门ID
- **返回类型**：`JsonResponse<Integer>` - 绑定人员数量
- **业务说明**：删除前检查，如果有绑定人员需要确认删除

## 📱 4. 组织部门APP接口 (OrganizationDepartmentAppController)

**基础路径**：`/app/sys/dept`

### 4.1 移动端机构树查询

#### 4.1.1 移动端机构树
- **接口路径**：`POST /app/sys/dept/org/tree`
- **接口作用**：移动端获取政府组织机构树
- **入参类型**：
  - `@RequestParam(defaultValue = "1") int sync` - 同步标识
  - `@RequestParam(required = false) String deptId` - 部门ID
  - `String jwtdid` - JWT部门ID
- **返回类型**：`JsonResponse<List<TreeNode>>` - 机构树数据
- **业务说明**：移动端专用的机构树查询接口，无需权限验证

#### 4.1.2 移动端完整机构树
- **接口路径**：`POST /app/sys/dept/org/all-tree`
- **接口作用**：移动端获取完整机构树
- **入参类型**：
  - `@RequestParam(defaultValue = "1") int sync` - 同步标识
  - `String deptId` - 部门ID
- **返回类型**：`JsonResponse<List<TreeNode>>` - 完整机构树
- **业务说明**：移动端获取完整的机构树形结构

#### 4.1.3 移动端行政区划树
- **接口路径**：`POST /app/sys/dept/org/dept-city-tree`
- **接口作用**：移动端获取行政区划树
- **入参类型**：
  - `String jwtaid` - JWT单位ID
  - `int sync` - 同步标识
  - `@RequestParam(defaultValue = "false") boolean expand` - 展开标识
  - `@RequestParam(defaultValue = "off") String showTop` - 显示顶级节点
- **返回类型**：`JsonResponse<List<TreeNode>>` - 行政区划树
- **业务说明**：移动端专用的行政区划树查询

#### 4.1.4 移动端企业部门树
- **接口路径**：`POST /app/sys/dept/ent/tree`
- **接口作用**：移动端获取企业部门树
- **入参类型**：
  - `@RequestParam(defaultValue = "1") int sync` - 同步标识
  - `@RequestParam(required = false) String deptId` - 部门ID
  - `String jwtdid` - JWT部门ID
- **返回类型**：`JsonResponse<List<TreeNode>>` - 企业部门树
- **业务说明**：移动端专用的企业部门树查询，管理员返回空列表

## 📊 接口统计汇总

| 控制器 | 接口数量 | 主要功能 |
|--------|----------|----------|
| DepartmentController | 25 | 部门管理、树形查询、统计分析 |
| EnterpriseController | 7 | 企业管理、账号管理 |
| OrganizationDepartmentController | 17 | 组织架构管理、树形查询 |
| OrganizationDepartmentAppController | 4 | 移动端组织架构查询 |
| **总计** | **53** | **组织架构完整管理** |

## 🔒 权限说明

- **@Authorization**：需要权限验证的接口
- **@IgnoreAuthorization**：忽略权限验证的接口
- **businessKey**：业务权限标识，用于细粒度权限控制
- **passKey**：数据传递标识，用于上下文数据传递

## 📋 数据格式说明

### 通用响应格式
```json
{
  "code": "操作代码",
  "message": "操作消息",
  "data": "响应数据",
  "success": "操作是否成功"
}
```

### 分页响应格式
```json
{
  "records": "数据列表",
  "total": "总记录数",
  "size": "每页大小", 
  "current": "当前页码",
  "pages": "总页数"
}
```

### 树形节点格式
```json
{
  "id": "节点ID",
  "text": "节点名称",
  "children": "子节点列表",
  "checked": "选中状态",
  "expand": "展开状态"
}
```