# auth模块数据模型分析文档

> **生成时间**：2025-09-19  
> **模块名称**：auth权限管理模块  
> **分析版本**：v1.0

## 1. 分析概述

本文档对auth模块的数据模型进行深入分析，包括实体关系设计、PO对象与领域对象的映射关系、数据库表结构、数据访问模式和查询复杂度评估，为重构工作提供数据层面的设计指导。

## 2. 领域实体模型分析

### 2.1 核心实体识别

**主要聚合根**：
- **Menu（菜单）**：系统菜单管理的聚合根
- **Permission（权限）**：权限控制的聚合根
- **Role（角色）**：角色管理的聚合根

**值对象**：
- **MenuType**：菜单类型枚举（目录、菜单、按钮）
- **MenuStatus**：菜单状态枚举（激活、非激活）
- **PermissionType**：权限类型枚举
- **PermissionStatus**：权限状态枚举
- **RoleType**：角色类型枚举
- **RoleStatus**：角色状态枚举

### 2.2 实体关系模型

#### 2.2.1 菜单实体关系

```
Menu (聚合根)
├── MenuType (值对象) - 枚举关系
├── MenuStatus (值对象) - 枚举关系
└── children: List<Menu> - 自引用关系（一对多）
    └── parentMenuId: String - 父子关系外键
```

**关系特征**：
- **自引用层级关系**：菜单支持无限层级嵌套
- **树形结构**：通过parentMenuId构建父子关系
- **层级深度控制**：menuLevel字段记录层级深度
- **排序支持**：orderIndex字段支持同级菜单排序

#### 2.2.2 权限实体关系

```
Permission (聚合根)
├── PermissionType (值对象) - 枚举关系
├── PermissionStatus (值对象) - 枚举关系
├── ResourcePath (值对象) - 组合关系
└── DataScope (值对象) - 组合关系
```

**关系特征**：
- **多类型权限**：支持API、菜单、数据等多种权限类型
- **资源路径抽象**：通过ResourcePath值对象封装资源访问路径
- **数据范围控制**：支持基于数据范围的权限控制

#### 2.2.3 角色实体关系

```
Role (聚合根)
├── RoleType (值对象) - 枚举关系
├── RoleStatus (值对象) - 枚举关系
├── permissions: List<Permission> - 多对多关系
└── menus: List<Menu> - 多对多关系
```

**关系特征**：
- **角色权限关联**：角色可以关联多个权限
- **角色菜单关联**：角色可以关联多个菜单
- **灵活的权限组合**：支持细粒度的权限组合配置

### 2.3 实体生命周期管理

#### 2.3.1 公共生命周期属性

**审计字段**：
```java
// 创建信息
private String creator;      // 创建人
private LocalDateTime createTime;  // 创建时间

// 更新信息  
private String updator;      // 更新人
private LocalDateTime updateTime;  // 更新时间

// 多租户
private String tenantId;     // 租户ID
```

**生命周期管理特征**：
- **创建审计**：记录创建人和创建时间
- **修改审计**：记录最后修改人和修改时间
- **多租户隔离**：通过tenantId实现数据隔离
- **软删除支持**：通过deleted字段实现逻辑删除

## 3. 数据库表结构分析

### 3.1 菜单表（sys_menu）

**表结构设计**：
```sql
CREATE TABLE sys_menu (
    id VARCHAR(32) PRIMARY KEY,           -- 主键ID
    menu_code VARCHAR(50) UNIQUE NOT NULL, -- 菜单编码
    menu_name VARCHAR(50) NOT NULL,       -- 菜单名称
    menu_title VARCHAR(100),              -- 菜单标题
    parent_menu_id VARCHAR(32),           -- 父菜单ID
    menu_path VARCHAR(200),               -- 菜单路径
    menu_level INT DEFAULT 1,             -- 菜单层级
    menu_type VARCHAR(20) DEFAULT 'MENU', -- 菜单类型
    menu_uri VARCHAR(200),                -- 菜单URI
    menu_icon VARCHAR(50),                -- 菜单图标
    component VARCHAR(200),               -- 组件路径
    status VARCHAR(20) DEFAULT 'ACTIVE',  -- 菜单状态
    visible BOOLEAN DEFAULT TRUE,         -- 是否可见
    keep_alive BOOLEAN DEFAULT FALSE,     -- 是否缓存
    external BOOLEAN DEFAULT FALSE,       -- 是否外链
    leaf BOOLEAN DEFAULT FALSE,           -- 是否叶子节点
    order_index INT DEFAULT 0,            -- 排序序号
    creator VARCHAR(32),                  -- 创建人
    create_time DATETIME,                 -- 创建时间
    updator VARCHAR(32),                  -- 更新人
    update_time DATETIME,                 -- 更新时间
    tenant_id VARCHAR(32) NOT NULL,       -- 租户ID
    deleted INT DEFAULT 0                 -- 逻辑删除标识
);
```

**索引设计**：
```sql
-- 主键索引
PRIMARY KEY (id)

-- 唯一索引
UNIQUE KEY uk_menu_code_tenant (menu_code, tenant_id, deleted)

-- 普通索引
KEY idx_parent_menu_id (parent_menu_id)
KEY idx_tenant_id (tenant_id)
KEY idx_status (status)
KEY idx_menu_level (menu_level)
KEY idx_order_index (order_index)
```

### 3.2 权限表（sys_permission）

**表结构设计**：
```sql
CREATE TABLE sys_permission (
    id VARCHAR(32) PRIMARY KEY,              -- 主键ID
    permission_code VARCHAR(50) UNIQUE NOT NULL, -- 权限编码
    permission_name VARCHAR(50) NOT NULL,    -- 权限名称
    permission_desc VARCHAR(200),            -- 权限描述
    permission_type VARCHAR(20) DEFAULT 'API', -- 权限类型
    status VARCHAR(20) DEFAULT 'ACTIVE',     -- 权限状态
    permission_uri VARCHAR(200),             -- 权限URI
    permission_method VARCHAR(10),           -- 权限方法
    component_id VARCHAR(100),               -- 组件标识
    data_scope VARCHAR(50),                  -- 数据范围
    order_index INT DEFAULT 0,               -- 排序序号
    creator VARCHAR(32),                     -- 创建人
    create_time DATETIME,                    -- 创建时间
    updator VARCHAR(32),                     -- 更新人
    update_time DATETIME,                    -- 更新时间
    tenant_id VARCHAR(32) NOT NULL,          -- 租户ID
    deleted INT DEFAULT 0                    -- 逻辑删除标识
);
```

**索引设计**：
```sql
-- 主键索引
PRIMARY KEY (id)

-- 唯一索引
UNIQUE KEY uk_permission_code_tenant (permission_code, tenant_id, deleted)

-- 普通索引
KEY idx_permission_type (permission_type)
KEY idx_tenant_id (tenant_id)
KEY idx_status (status)
KEY idx_permission_uri (permission_uri)
```

### 3.3 角色表（sys_role）

**表结构设计**：
```sql
CREATE TABLE sys_role (
    id VARCHAR(32) PRIMARY KEY,           -- 主键ID
    role_code VARCHAR(50) UNIQUE NOT NULL, -- 角色编码
    role_name VARCHAR(50) NOT NULL,       -- 角色名称
    role_desc VARCHAR(200),               -- 角色描述
    role_type VARCHAR(20) DEFAULT 'CUSTOM', -- 角色类型
    status VARCHAR(20) DEFAULT 'ACTIVE',  -- 角色状态
    data_scope VARCHAR(50),               -- 数据权限范围
    order_index INT DEFAULT 0,            -- 排序序号
    creator VARCHAR(32),                  -- 创建人
    create_time DATETIME,                 -- 创建时间
    updator VARCHAR(32),                  -- 更新人
    update_time DATETIME,                 -- 更新时间
    tenant_id VARCHAR(32) NOT NULL,       -- 租户ID
    deleted INT DEFAULT 0                 -- 逻辑删除标识
);
```

### 3.4 关联表设计

#### 3.4.1 角色权限关联表（sys_role_permission）

```sql
CREATE TABLE sys_role_permission (
    id VARCHAR(32) PRIMARY KEY,           -- 主键ID
    role_id VARCHAR(32) NOT NULL,         -- 角色ID
    permission_id VARCHAR(32) NOT NULL,   -- 权限ID
    creator VARCHAR(32),                  -- 创建人
    create_time DATETIME,                 -- 创建时间
    tenant_id VARCHAR(32) NOT NULL,       -- 租户ID
    
    UNIQUE KEY uk_role_permission (role_id, permission_id, tenant_id),
    KEY idx_role_id (role_id),
    KEY idx_permission_id (permission_id),
    KEY idx_tenant_id (tenant_id)
);
```

#### 3.4.2 角色菜单关联表（sys_role_menu）

```sql
CREATE TABLE sys_role_menu (
    id VARCHAR(32) PRIMARY KEY,           -- 主键ID
    role_id VARCHAR(32) NOT NULL,         -- 角色ID
    menu_id VARCHAR(32) NOT NULL,         -- 菜单ID
    creator VARCHAR(32),                  -- 创建人
    create_time DATETIME,                 -- 创建时间
    tenant_id VARCHAR(32) NOT NULL,       -- 租户ID
    
    UNIQUE KEY uk_role_menu (role_id, menu_id, tenant_id),
    KEY idx_role_id (role_id),
    KEY idx_menu_id (menu_id),
    KEY idx_tenant_id (tenant_id)
);
```

## 4. PO对象与领域对象映射分析

### 4.1 MenuPO ↔ Menu实体映射

**映射关系矩阵**：

| MenuPO字段 | Menu实体字段 | 映射类型 | 说明 |
|-----------|-------------|----------|------|
| id | menuId | 直接映射 | 主键标识 |
| menuCode | menuCode | 直接映射 | 菜单编码 |
| menuName | menuName | 直接映射 | 菜单名称 |
| menuTitle | menuTitle | 直接映射 | 菜单标题 |
| parentMenuId | parentMenuId | 直接映射 | 父菜单ID |
| menuType | menuType | 枚举转换 | String ↔ MenuType |
| status | status | 枚举转换 | String ↔ MenuStatus |
| visible | visible | 直接映射 | 可见性标识 |
| keepAlive | keepAlive | 直接映射 | 缓存标识 |
| children | children | 复杂映射 | 需要额外查询构建 |

**映射复杂度分析**：
- **简单映射**：15个字段，直接赋值
- **枚举转换**：2个字段，需要类型转换
- **关联映射**：1个字段（children），需要递归查询
- **计算字段**：1个字段（leaf），基于children列表计算

### 4.2 PermissionPO ↔ Permission实体映射

**映射特征**：
- **多类型权限支持**：通过permissionType字段区分不同类型权限
- **灵活的资源定义**：支持URI、方法、组件等多种资源形式
- **数据权限扩展**：通过dataScope字段支持数据级权限控制

### 4.3 映射层性能优化

**批量映射优化**：
```java
// 优化前：N+1查询问题
List<Menu> menus = menuRepository.findAll();
for (Menu menu : menus) {
    List<Menu> children = menuRepository.getChildMenus(menu.getMenuId());
    menu.setChildren(children);
}

// 优化后：批量查询
List<Menu> allMenus = menuRepository.findAll();
Map<String, List<Menu>> childrenMap = buildChildrenMap(allMenus);
allMenus.forEach(menu -> {
    menu.setChildren(childrenMap.getOrDefault(menu.getMenuId(), new ArrayList<>()));
});
```

## 5. 数据访问模式分析

### 5.1 查询模式分类

#### 5.1.1 单实体查询模式

**按ID查询**：
```java
// 主键查询 - O(1)复杂度
Optional<Menu> findById(String menuId);

// 业务键查询 - O(1)复杂度（有索引）
Optional<Menu> findByMenuCode(String menuCode, String tenantId);
```

**查询复杂度**：低 - 基于主键和唯一索引

#### 5.1.2 列表查询模式

**分页查询**：
```java
// 分页查询 - O(log n)复杂度
Page<Menu> findByTenantId(String tenantId, Pageable pageable);

// 条件查询 - O(log n)到O(n)复杂度
List<Menu> findByStatusAndTenantId(MenuStatus status, String tenantId);
```

**查询复杂度**：中等 - 依赖索引效果

#### 5.1.3 树形查询模式

**菜单树查询**：
```java
// 递归查询 - O(n)复杂度
List<Menu> getMenuTree(String tenantId);

// 子菜单查询 - O(log n)复杂度
List<Menu> getChildMenus(String parentMenuId, String tenantId);
```

**查询复杂度**：高 - 涉及递归和多次查询

### 5.2 数据访问性能分析

#### 5.2.1 热点数据访问

**用户权限查询场景**：
```java
// 高频查询：用户登录时的权限获取
// 查询路径：用户 → 角色 → 权限 → 菜单
List<Permission> getUserPermissions(String userId, String tenantId);
List<Menu> getUserMenus(String userId, String tenantId);
```

**性能影响**：
- **查询频率**：极高（每次用户请求）
- **查询复杂度**：涉及多表关联
- **缓存需求**：强烈需要缓存优化

#### 5.2.2 管理后台访问

**权限管理场景**：
```java
// 中频查询：权限配置管理
List<Role> getAllRoles(String tenantId);
List<Permission> getRolePermissions(String roleId);
List<Menu> getRoleMenus(String roleId);
```

**性能特征**：
- **查询频率**：中等（管理操作）
- **数据量**：相对较小
- **实时性要求**：高（配置即时生效）

### 5.3 数据一致性管理

#### 5.3.1 事务边界设计

**聚合内事务**：
```java
@Transactional
public void updateMenu(String menuId, MenuUpdateDTO updateDTO) {
    // 单聚合事务，强一致性
}
```

**跨聚合事务**：
```java
@Transactional
public void assignPermissionsToRole(String roleId, List<String> permissionIds) {
    // 跨聚合事务，需要考虑事务范围
}
```

#### 5.3.2 缓存一致性策略

**写穿透策略**：
- 更新数据库的同时更新缓存
- 保证数据的强一致性
- 适用于权限核心数据

**失效策略**：
- 更新数据库后删除相关缓存
- 下次访问时重新加载
- 适用于低频修改的数据

## 6. 查询复杂度评估

### 6.1 复杂度分级

| 查询类型 | 复杂度等级 | 时间复杂度 | 说明 |
|---------|-----------|-----------|------|
| 主键查询 | 低 | O(1) | 基于主键索引 |
| 唯一键查询 | 低 | O(1) | 基于唯一索引 |
| 简单条件查询 | 中 | O(log n) | 基于普通索引 |
| 范围查询 | 中 | O(log n + k) | 索引范围扫描 |
| 菜单树查询 | 高 | O(n) | 递归遍历 |
| 多表关联查询 | 高 | O(n*m) | 关联表查询 |

### 6.2 性能瓶颈识别

#### 6.2.1 菜单树构建

**问题分析**：
- 递归查询导致多次数据库访问
- N+1查询问题显著
- 深层级菜单性能下降明显

**优化方案**：
```java
// 方案1：一次查询 + 内存构建
List<MenuPO> allMenus = menuMapper.selectByTenantId(tenantId);
return buildMenuTree(allMenus);

// 方案2：CTE递归查询（MySQL 8.0+）
WITH RECURSIVE menu_tree AS (
    SELECT * FROM sys_menu WHERE parent_menu_id IS NULL AND tenant_id = ?
    UNION ALL
    SELECT m.* FROM sys_menu m 
    JOIN menu_tree mt ON m.parent_menu_id = mt.id
)
SELECT * FROM menu_tree ORDER BY menu_level, order_index;
```

#### 6.2.2 权限验证查询

**问题分析**：
- 每次请求都需要验证权限
- 涉及用户-角色-权限的多级关联
- 实时性要求高，缓存失效影响大

**优化方案**：
```java
// 预计算权限视图
CREATE VIEW user_permission_view AS
SELECT u.id as user_id, p.permission_code, p.permission_uri
FROM sys_user u
JOIN sys_user_role ur ON u.id = ur.user_id
JOIN sys_role r ON ur.role_id = r.id
JOIN sys_role_permission rp ON r.id = rp.role_id
JOIN sys_permission p ON rp.permission_id = p.id
WHERE u.deleted = 0 AND r.deleted = 0 AND p.deleted = 0;
```

## 7. 数据模型优化建议

### 7.1 立即优化项

#### 7.1.1 索引优化

**添加复合索引**：
```sql
-- 菜单表复合索引
ALTER TABLE sys_menu ADD INDEX idx_tenant_parent_level (tenant_id, parent_menu_id, menu_level);

-- 权限表复合索引
ALTER TABLE sys_permission ADD INDEX idx_tenant_type_status (tenant_id, permission_type, status);

-- 角色关联表复合索引
ALTER TABLE sys_role_permission ADD INDEX idx_tenant_role (tenant_id, role_id);
```

#### 7.1.2 数据类型优化

**字段长度调整**：
```sql
-- 权限URI字段扩展
ALTER TABLE sys_permission MODIFY COLUMN permission_uri VARCHAR(500);

-- 菜单路径字段扩展
ALTER TABLE sys_menu MODIFY COLUMN menu_path VARCHAR(500);
```

### 7.2 架构改进建议

#### 7.2.1 读写分离优化

**查询端优化**：
- 为高频查询建立专门的查询视图
- 使用读库承载查询压力
- 通过异步同步保证数据一致性

**命令端优化**：
- 保持事务的原子性和一致性
- 优化批量操作的性能
- 减少不必要的关联查询

#### 7.2.2 缓存策略设计

**多级缓存架构**：
```java
// L1缓存：应用内存缓存
@Cacheable(value = "permissions", key = "#userId + ':' + #tenantId")
public List<Permission> getUserPermissions(String userId, String tenantId);

// L2缓存：Redis分布式缓存
@Cacheable(value = "menuTree", key = "#tenantId")
public List<Menu> getMenuTree(String tenantId);
```

### 7.3 长期发展规划

#### 7.3.1 数据分片准备

**分片策略**：
- 按租户ID进行水平分片
- 保持同租户数据在同一分片
- 支持跨分片的聚合查询

#### 7.3.2 CQRS数据模型

**命令端模型**：
- 保持聚合完整性
- 支持复杂的业务规则验证
- 优化事务处理性能

**查询端模型**：
- 扁平化的查询结构
- 预计算的数据视图
- 优化的索引设计

## 8. 结论与行动计划

### 8.1 数据模型评估总结

**优势**：
- DDD实体设计清晰，聚合边界明确
- 多租户支持完备，数据隔离有效
- 审计功能完整，生命周期管理规范
- 逻辑删除设计，数据安全可追溯

**待改进**：
- 树形查询性能有优化空间
- 缓存策略需要完善
- 索引设计需要进一步优化
- 跨聚合查询效率有待提升

### 8.2 优先级改进计划

| 改进项目 | 优先级 | 预期收益 | 实施难度 |
|---------|-------|---------|----------|
| 添加复合索引 | 高 | 查询性能提升50% | 低 |
| 菜单树查询优化 | 高 | 响应时间减少70% | 中 |
| 权限缓存策略 | 中 | 并发性能提升3倍 | 中 |
| CQRS数据模型 | 低 | 架构清晰度提升 | 高 |

### 8.3 技术风险控制

**数据迁移风险**：
- 制定详细的数据迁移方案
- 建立回滚机制和数据校验
- 分阶段执行，降低影响范围

**性能回归风险**：
- 建立性能基准测试
- 监控关键指标变化
- 及时调整优化策略

通过系统性的数据模型分析，为auth模块重构提供了坚实的数据层面支撑，确保重构后的系统具备更好的性能、可维护性和扩展性。