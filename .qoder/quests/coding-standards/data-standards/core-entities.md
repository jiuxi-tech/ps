# 核心实体定义规范

## 文档说明

本文档定义了PS-BMP系统的核心数据实体，包括前端、后端和数据库层面的字段定义。开发时必须严格遵循这些定义，避免字段命名错误导致的数据绑定失败。

---

## 1. 人员（Person）实体

### 1.1 数据库表结构

**主表名称**: `tp_person_basicinfo` ⚠️ **不是** `tp_person`

**重要说明**: 
- 人员基础信息的主表是 `tp_person_basicinfo`，所有涉及人员基础信息查询的SQL都应使用此表名
- `tp_person` 表可能存在但不是主表，避免混淆

**核心字段**:

| 数据库字段 | 类型 | 说明 | 示例 |
|-----------|------|------|------|
| person_id | VARCHAR(32) | 人员ID（主键） | P001 |
| person_name | VARCHAR(50) | 人员姓名 | 张三 |
| person_no | VARCHAR(50) | 人员编号 | E2024001 |
| sex | INT | 性别（1男 2女） | 1 |
| phone | VARCHAR(20) | 手机号码 | 13800138000 |
| email | VARCHAR(100) | 电子邮箱 | zhangsan@example.com |
| dept_id | VARCHAR(32) | 部门ID | D001 |
| ascn_id | VARCHAR(32) | 单位ID | A001 |
| pass_key | VARCHAR(50) | 数据校验密钥 | xxx |
| log_delete | INT | 逻辑删除标记（0未删除 1已删除） | 0 |
| create_time | VARCHAR(14) | 创建时间（YYYYMMDDHHmmss） | 20241201120000 |
| update_time | VARCHAR(14) | 更新时间（YYYYMMDDHHmmss） | 20241201120000 |
| create_person_id | VARCHAR(32) | 创建人ID | P002 |
| update_person_id | VARCHAR(32) | 更新人ID | P002 |

### 1.2 后端实体类

**实体类名**: `PersonBasicInfo` 或 `Person`

**Java字段定义**:

```java
public class PersonBasicInfo {
    /**
     * 人员ID（主键）
     */
    private String personId;
    
    /**
     * 人员姓名
     */
    private String personName;
    
    /**
     * 人员编号
     */
    private String personNo;
    
    /**
     * 性别（1男 2女）
     */
    private Integer sex;
    
    /**
     * 性别名称（冗余字段，用于显示）
     */
    private String sexName;
    
    /**
     * 手机号码
     */
    private String phone;
    
    /**
     * 电子邮箱
     */
    private String email;
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 部门名称（关联查询）
     */
    private String deptName;
    
    /**
     * 部门简称（关联查询）
     */
    private String deptSimpleName;
    
    /**
     * 单位ID
     */
    private String ascnId;
    
    /**
     * 单位名称（关联查询）
     */
    private String ascnName;
    
    /**
     * 是否主部门（1主部门 0兼职部门）
     */
    private Integer defaultDept;
    
    /**
     * 主部门名称（用于显示）
     */
    private String defaultDeptName;
    
    /**
     * 数据校验密钥
     */
    private String passKey;
    
    /**
     * 逻辑删除标记（0未删除 1已删除）
     */
    private Integer logDelete;
    
    /**
     * 创建时间（YYYYMMDDHHmmss格式字符串）
     */
    private String createTime;
    
    /**
     * 更新时间（YYYYMMDDHHmmss格式字符串）
     */
    private String updateTime;
    
    /**
     * 创建人ID
     */
    private String createPersonId;
    
    /**
     * 创建人姓名（关联查询）
     */
    private String createPersonName;
    
    /**
     * 更新人ID
     */
    private String updatePersonId;
    
    /**
     * 更新人姓名（关联查询）
     */
    private String updatePersonName;
}
```

### 1.3 前端数据结构

**JavaScript/Vue对象**:

```javascript
// 人员列表项
{
  personId: 'P001',           // 人员ID
  personName: '张三',         // 人员姓名
  personNo: 'E2024001',       // 人员编号
  sex: 1,                     // 性别（1男 2女）
  sexName: '男',              // 性别名称
  phone: '13800138000',       // 手机号码
  email: 'zhangsan@example.com', // 电子邮箱
  deptId: 'D001',             // 部门ID
  deptName: '技术部',         // 部门名称
  deptSimpleName: '技术部',   // 部门简称
  ascnId: 'A001',             // 单位ID
  ascnName: '总公司',         // 单位名称
  defaultDept: 1,             // 是否主部门
  defaultDeptName: '主部门',  // 主部门标识
  passKey: 'xxx',             // 校验密钥
  logDelete: 0,               // 逻辑删除标记
  createTime: '20241201120000', // 创建时间
  updateTime: '20241201120000', // 更新时间
  createPersonName: '管理员', // 创建人姓名
  updatePersonName: '管理员'  // 更新人姓名
}
```

### 1.4 关键字段说明

#### passKey（数据校验密钥）
- **用途**: 数据修改和删除时的安全校验
- **传递规则**: 
  - 查询时后端返回
  - 修改/删除时前端必须回传
  - 不可为空

#### logDelete（逻辑删除标记）
- **值定义**: 
  - `0`: 未删除（正常数据）
  - `1`: 已删除（软删除）
- **查询规则**: 
  - 默认查询条件：`logDelete = 0`
  - 避免查询到已删除数据

#### defaultDept（主部门标识）
- **值定义**:
  - `1`: 主部门
  - `0`: 兼职部门
- **业务规则**:
  - 每个人员有且仅有一个主部门
  - 可以有多个兼职部门
  - 只有主部门人员可以修改基本信息

---

## 2. 部门（Department）实体

### 2.1 数据库表结构

**主表名称**: `tp_dept`

**核心字段**:

| 数据库字段 | 类型 | 说明 | 示例 |
|-----------|------|------|------|
| dept_id | VARCHAR(32) | 部门ID（主键） | D001 |
| dept_name | VARCHAR(100) | 部门名称 | 技术研发部 |
| dept_simple_name | VARCHAR(50) | 部门简称 | 技术部 |
| dept_no | VARCHAR(50) | 部门编号 | DEPT001 |
| dept_levelcode | VARCHAR(200) | 部门层级编码 | 01.01.01 |
| parent_dept_id | VARCHAR(32) | 父部门ID | D000 |
| ascn_id | VARCHAR(32) | 所属单位ID | A001 |
| sort_no | INT | 排序号 | 1 |
| dept_type | INT | 部门类型 | 1 |
| pass_key | VARCHAR(50) | 数据校验密钥 | xxx |
| log_delete | INT | 逻辑删除标记 | 0 |
| create_time | VARCHAR(14) | 创建时间 | 20241201120000 |

### 2.2 后端实体类

```java
public class Department {
    /**
     * 部门ID（主键）
     */
    private String deptId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 部门简称
     */
    private String deptSimpleName;
    
    /**
     * 部门编号
     */
    private String deptNo;
    
    /**
     * 部门层级编码（用于树形结构）
     */
    private String deptLevelcode;
    
    /**
     * 父部门ID
     */
    private String parentDeptId;
    
    /**
     * 父部门名称（关联查询）
     */
    private String parentDeptName;
    
    /**
     * 所属单位ID
     */
    private String ascnId;
    
    /**
     * 所属单位名称（关联查询）
     */
    private String ascnName;
    
    /**
     * 排序号
     */
    private Integer sortNo;
    
    /**
     * 部门类型
     */
    private Integer deptType;
    
    /**
     * 数据校验密钥
     */
    private String passKey;
    
    /**
     * 逻辑删除标记
     */
    private Integer logDelete;
}
```

### 2.3 前端数据结构

#### 列表项数据
```javascript
{
  deptId: 'D001',              // 部门ID
  deptName: '技术研发部',      // 部门名称
  deptSimpleName: '技术部',    // 部门简称
  deptNo: 'DEPT001',           // 部门编号
  deptLevelcode: '01.01.01',   // 层级编码
  parentDeptId: 'D000',        // 父部门ID
  parentDeptName: '总部',      // 父部门名称
  ascnId: 'A001',              // 单位ID
  ascnName: '总公司',          // 单位名称
  sortNo: 1,                   // 排序号
  passKey: 'xxx'               // 校验密钥
}
```

#### 树形结构数据
```javascript
{
  id: 'D001',                  // 节点ID（映射自deptId）
  text: '技术研发部',          // 节点文本（映射自deptName）
  value: 'D001',               // 节点值（映射自deptId）
  parentId: 'D000',            // 父节点ID
  levelcode: '01.01.01',       // 层级编码
  extend01: 'A001',            // 扩展字段1：单位ID
  children: []                 // 子节点数组
}
```

### 2.4 树形结构配置

**fb-tree组件配置**:
```javascript
<fb-tree
  ref="deptTree"
  :data="deptData"
  :reader="{value: 'id', label: 'text'}"
  @on-select-change="handleSelectChange">
</fb-tree>
```

**数据映射说明**:
- `id`: 节点唯一标识，对应 `deptId`
- `text`: 节点显示文本，对应 `deptName`
- `value`: 节点值，对应 `deptId`
- `extend01`: 扩展字段，通常存储 `ascnId`（单位ID）

---

## 3. 单位/组织（Organization）实体

### 3.1 数据库表结构

**主表名称**: `tp_ascription`

**核心字段**:

| 数据库字段 | 类型 | 说明 | 示例 |
|-----------|------|------|------|
| ascn_id | VARCHAR(32) | 单位ID（主键） | A001 |
| ascn_name | VARCHAR(100) | 单位名称 | XX科技有限公司 |
| ascn_no | VARCHAR(50) | 单位编号 | ORG001 |
| parent_ascn_id | VARCHAR(32) | 父单位ID | A000 |
| ascn_type | INT | 单位类型 | 1 |
| sort_no | INT | 排序号 | 1 |
| pass_key | VARCHAR(50) | 数据校验密钥 | xxx |
| log_delete | INT | 逻辑删除标记 | 0 |

### 3.2 后端实体类

```java
public class Ascription {
    /**
     * 单位ID（主键）
     */
    private String ascnId;
    
    /**
     * 单位名称
     */
    private String ascnName;
    
    /**
     * 单位编号
     */
    private String ascnNo;
    
    /**
     * 父单位ID
     */
    private String parentAscnId;
    
    /**
     * 单位类型
     */
    private Integer ascnType;
    
    /**
     * 排序号
     */
    private Integer sortNo;
    
    /**
     * 数据校验密钥
     */
    private String passKey;
    
    /**
     * 逻辑删除标记
     */
    private Integer logDelete;
}
```

### 3.3 前端数据结构

```javascript
{
  ascnId: 'A001',              // 单位ID
  ascnName: 'XX科技有限公司',  // 单位名称
  ascnNo: 'ORG001',            // 单位编号
  parentAscnId: 'A000',        // 父单位ID
  ascnType: 1,                 // 单位类型
  sortNo: 1,                   // 排序号
  passKey: 'xxx'               // 校验密钥
}
```

---

## 4. 角色（Role）实体

### 4.1 数据库表结构

**主表名称**: `tp_role`

**核心字段**:

| 数据库字段 | 类型 | 说明 | 示例 |
|-----------|------|------|------|
| role_id | VARCHAR(32) | 角色ID（主键） | R001 |
| role_name | VARCHAR(50) | 角色名称 | 系统管理员 |
| role_no | VARCHAR(50) | 角色编号 | ADMIN |
| role_type | INT | 角色类型 | 1 |
| role_desc | VARCHAR(200) | 角色描述 | 系统管理员角色 |
| status | INT | 状态（1启用 0禁用） | 1 |
| pass_key | VARCHAR(50) | 数据校验密钥 | xxx |
| log_delete | INT | 逻辑删除标记 | 0 |

### 4.2 后端实体类

```java
public class Role {
    /**
     * 角色ID（主键）
     */
    private String roleId;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色编号
     */
    private String roleNo;
    
    /**
     * 角色类型
     */
    private Integer roleType;
    
    /**
     * 角色描述
     */
    private String roleDesc;
    
    /**
     * 状态（1启用 0禁用）
     */
    private Integer status;
    
    /**
     * 数据校验密钥
     */
    private String passKey;
    
    /**
     * 逻辑删除标记
     */
    private Integer logDelete;
}
```

### 4.3 前端数据结构

```javascript
{
  roleId: 'R001',              // 角色ID
  roleName: '系统管理员',      // 角色名称
  roleNo: 'ADMIN',             // 角色编号
  roleType: 1,                 // 角色类型
  roleDesc: '系统管理员角色',  // 角色描述
  status: 1,                   // 状态
  passKey: 'xxx'               // 校验密钥
}
```

---

## 5. 第三方应用（ThirdPartyApp）实体

### 5.1 数据库表结构

**主表名称**: `tp_third_party_app`

**核心字段**:

| 数据库字段 | 类型 | 说明 | 示例 |
|-----------|------|------|------|
| app_id | VARCHAR(32) | 应用ID（主键） | APP001 |
| app_name | VARCHAR(100) | 应用名称 | 移动端应用 |
| api_key | VARCHAR(100) | API密钥 | ak_xxxxxxxxxxxx |
| api_secret | VARCHAR(200) | API秘钥 | as_xxxxxxxxxxxx |
| status | INT | 状态（1启用 0禁用） | 1 |
| expire_time | VARCHAR(14) | 过期时间 | 20251231235959 |
| description | VARCHAR(500) | 应用描述 | 用于移动端访问 |
| pass_key | VARCHAR(50) | 数据校验密钥 | xxx |
| log_delete | INT | 逻辑删除标记 | 0 |
| create_time | VARCHAR(14) | 创建时间 | 20241201120000 |
| create_person_id | VARCHAR(32) | 创建人ID | P001 |

### 5.2 后端实体类

```java
public class ThirdPartyApp {
    /**
     * 应用ID（主键）
     */
    private String appId;
    
    /**
     * 应用名称
     */
    private String appName;
    
    /**
     * API密钥
     */
    private String apiKey;
    
    /**
     * API秘钥（敏感信息，不返回前端）
     */
    private String apiSecret;
    
    /**
     * 状态（1启用 0禁用）
     */
    private Integer status;
    
    /**
     * 过期时间（YYYYMMDDHHmmss）
     */
    private String expireTime;
    
    /**
     * 应用描述
     */
    private String description;
    
    /**
     * 数据校验密钥
     */
    private String passKey;
    
    /**
     * 逻辑删除标记
     */
    private Integer logDelete;
    
    /**
     * 创建时间
     */
    private String createTime;
    
    /**
     * 创建人ID
     */
    private String createPersonId;
    
    /**
     * 创建人姓名（关联查询）
     */
    private String createPersonName;
}
```

### 5.3 前端数据结构

```javascript
{
  appId: 'APP001',                      // 应用ID
  appName: '移动端应用',                // 应用名称
  apiKey: 'ak_xxxxxxxxxxxx',            // API密钥
  // apiSecret不返回前端
  status: 1,                            // 状态
  expireTime: '20251231235959',         // 过期时间
  description: '用于移动端访问',        // 应用描述
  passKey: 'xxx',                       // 校验密钥
  createTime: '20241201120000',         // 创建时间
  createPersonName: '管理员'            // 创建人姓名
}
```

---

## 6. 通用字段说明

### 6.1 标准字段

所有实体通常包含以下标准字段：

| 字段名 | 类型 | 说明 | 必需 |
|--------|------|------|------|
| {entity}_id | String | 主键ID | ✅ |
| pass_key | String | 数据校验密钥 | ✅ |
| log_delete | Integer | 逻辑删除标记（0未删除 1已删除） | ✅ |
| create_time | String | 创建时间（YYYYMMDDHHmmss） | ✅ |
| update_time | String | 更新时间（YYYYMMDDHHmmss） | ❌ |
| create_person_id | String | 创建人ID | ✅ |
| update_person_id | String | 更新人ID | ❌ |

### 6.2 状态字段

状态字段统一定义：

| 字段名 | 值 | 说明 |
|--------|---|------|
| status | 1 | 启用/正常 |
| status | 0 | 禁用/停用 |
| log_delete | 0 | 未删除 |
| log_delete | 1 | 已删除 |

### 6.3 性别字段

```javascript
sex: {
  1: '男',
  2: '女'
}
```

---

## 7. 字段命名映射规则

### 7.1 前后端字段映射

**规则**: 前后端字段名称保持一致，使用**camelCase**命名

| 数据库（snake_case） | 后端Java（camelCase） | 前端JS（camelCase） |
|---------------------|---------------------|-------------------|
| person_id | personId | personId |
| person_name | personName | personName |
| dept_simple_name | deptSimpleName | deptSimpleName |
| create_time | createTime | createTime |
| log_delete | logDelete | logDelete |

### 7.2 常见错误示例

❌ **错误**:
```javascript
// 前端使用了错误的字段名
formData.personid = 'P001'  // 应该是personId
formData.person_name = '张三' // 应该是personName
```

✅ **正确**:
```javascript
formData.personId = 'P001'
formData.personName = '张三'
```

---

## 8. 关联查询字段

### 8.1 人员关联字段

查询人员时通常需要关联查询的字段：

```javascript
{
  personId: 'P001',
  personName: '张三',
  deptId: 'D001',
  deptName: '技术部',           // 关联tp_dept查询
  deptSimpleName: '技术部',      // 关联tp_dept查询
  ascnId: 'A001',
  ascnName: '总公司',            // 关联tp_ascription查询
  createPersonName: '管理员'     // 关联tp_person_basicinfo查询
}
```

### 8.2 部门关联字段

查询部门时通常需要关联查询的字段：

```javascript
{
  deptId: 'D001',
  deptName: '技术部',
  parentDeptId: 'D000',
  parentDeptName: '总部',        // 关联父部门查询
  ascnId: 'A001',
  ascnName: '总公司'              // 关联tp_ascription查询
}
```

---

## 9. 使用示例

### 9.1 前端查询参数构造

```javascript
// 查询人员列表
const queryParams = {
  deptId: 'D001',        // 部门ID
  personName: '张',      // 人员姓名（模糊查询）
  sex: 1,                // 性别
  phone: '138',          // 手机号（模糊查询）
  logDelete: 0,          // 只查询未删除的数据
  page: 1,               // 页码
  size: 10               // 每页条数
}
```

### 9.2 前端表单数据构造

```javascript
// 新增/修改人员
const formData = {
  personId: '',          // 新增时为空，修改时必填
  personName: '张三',
  personNo: 'E2024001',
  sex: 1,
  phone: '13800138000',
  email: 'zhangsan@example.com',
  deptId: 'D001',
  ascnId: 'A001',
  passKey: 'xxx',        // 修改时必须回传
  logDelete: 0
}
```

### 9.3 后端响应数据示例

```java
// 列表响应
{
  "code": 1,
  "message": "查询成功",
  "data": {
    "total": 100,
    "records": [
      {
        "personId": "P001",
        "personName": "张三",
        "personNo": "E2024001",
        "sex": 1,
        "sexName": "男",
        "phone": "13800138000",
        "deptId": "D001",
        "deptName": "技术部",
        "ascnId": "A001",
        "ascnName": "总公司",
        "passKey": "xxx"
      }
    ]
  }
}
```

---

## 10. 注意事项

### 10.1 必须遵守的规则

1. ✅ **表名使用规范**
   - 人员表必须使用 `tp_person_basicinfo`
   - 不要使用 `tp_person`

2. ✅ **字段命名一致性**
   - 前后端字段名必须完全一致（camelCase）
   - 数据库字段使用snake_case

3. ✅ **passKey传递**
   - 查询时后端必须返回
   - 修改/删除时前端必须回传

4. ✅ **logDelete过滤**
   - 查询时默认添加 `logDelete = 0` 条件

5. ✅ **时间格式统一**
   - 后端存储：`YYYYMMDDHHmmss`
   - 前端显示：`YYYY-MM-DD HH:mm:ss`

### 10.2 常见错误

❌ **错误1**: 表名错误
```sql
SELECT * FROM tp_person  -- 错误！应该是tp_person_basicinfo
```

❌ **错误2**: 字段名不一致
```javascript
// 前端
formData.person_id = 'P001'  // 错误！应该是personId

// 后端
String person_id;  // 错误！应该是personId
```

❌ **错误3**: 忘记传递passKey
```javascript
// 修改时缺少passKey
this.service.update({
  personId: 'P001',
  personName: '李四'
  // 缺少passKey！
})
```

---

## 版本信息

- **版本号**: v1.0.0
- **创建日期**: 2025年12月1日
- **最后更新**: 2025年12月1日
- **维护状态**: 持续更新

---

**重要提醒**: 开发时请严格遵循本文档定义的字段名称和数据类型，避免因命名错误导致的数据绑定失败！
