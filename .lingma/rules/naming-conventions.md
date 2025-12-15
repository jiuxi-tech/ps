---
trigger: always
description: 命名规范，包括文件命名、变量命名、函数命名、类命名等
---

# 命名规范

## 一、文件命名规范

### 1. 前端文件命名

| 文件类型 | 命名规范 | 示例 |
|---------|---------|------|
| **页面文件** | 小写+连字符 | `user-list.vue`, `role-add.vue` |
| **组件文件** | 小写+连字符 | `user-dialog.vue`, `data-table.vue` |
| **Service 文件** | 小写+连字符 | `user-service.js`, `dict-service.js` |
| **工具类文件** | 小写+连字符 | `date-util.js`, `file-util.js` |
| **常量文件** | 小写+连字符 | `api-config.js`, `dict-config.js` |

### 2. 后端文件命名

| 文件类型 | 命名规范 | 示例 |
|---------|---------|------|
| **Entity** | 大驼峰+Entity | `UserEntity.java`, `DeptEntity.java` |
| **VO** | 大驼峰+VO | `UserVO.java`, `DeptVO.java` |
| **DTO** | 大驼峰+DTO | `UserDTO.java`, `DeptDTO.java` |
| **Controller** | 大驼峰+Controller | `UserController.java` |
| **Service 接口** | I+大驼峰+Service | `IUserService.java` |
| **Service 实现** | 大驼峰+ServiceImpl | `UserServiceImpl.java` |
| **Mapper** | 大驼峰+Mapper | `UserMapper.java` |
| **工具类** | 大驼峰+Util(s) | `DateUtil.java`, `FileUtils.java` |

## 二、Java 命名规范

### 1. 类命名

```java
/**
 * 规则：大驼峰命名法（PascalCase）
 * 格式：每个单词首字母大写
 */

// ✅ 正确示例
public class UserEntity { }
public class UserController { }
public class IUserService { }
public class UserServiceImpl { }
public class UserMapper { }
public class DateUtil { }
public class FileUtils { }

// ❌ 错误示例
public class userEntity { }
public class User_Controller { }
public class userservice { }
```

### 2. 接口命名

```java
/**
 * 规则：I + 大驼峰命名
 * Service 接口必须以 I 开头
 */

// ✅ 正确示例
public interface IUserService { }
public interface IDeptService { }
public interface IRoleService { }

// ❌ 错误示例
public interface UserService { }
public interface UserServiceInterface { }
```

### 3. 方法命名

```java
/**
 * 规则：小驼峰命名法（camelCase）
 * 格式：第一个单词首字母小写，其余单词首字母大写
 * 动词开头：get/set/add/edit/delete/query/save/update/remove/is/has
 */

// ✅ 正确示例
public void addUser() { }
public void editUser() { }
public void deleteUser() { }
public UserVO queryById(String id) { }
public List<UserVO> queryList() { }
public IPage<UserVO> queryPage() { }
public boolean isAdmin() { }
public boolean hasPermission() { }

// ❌ 错误示例
public void AddUser() { }
public void user_add() { }
public void add_user() { }
```

### 4. 变量命名

```java
/**
 * 规则：小驼峰命名法
 */

// ✅ 正确示例
private String userName;
private Integer userId;
private Date createTime;
private List<UserVO> userList;

// ❌ 错误示例
private String UserName;
private String user_name;
private String username;  // 单词分隔不清晰
```

### 5. 常量命名

```java
/**
 * 规则：全大写，单词间用下划线分隔
 */

// ✅ 正确示例
public static final String DEFAULT_PASSWORD = "123456";
public static final Integer MAX_PAGE_SIZE = 1000;
public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

// ❌ 错误示例
public static final String defaultPassword = "123456";
public static final String DEFAULT_password = "123456";
```

### 6. 枚举命名

```java
/**
 * 枚举类：大驼峰命名
 * 枚举值：全大写，下划线分隔
 */

// ✅ 正确示例
public enum UserStatus {
    NORMAL(1, "正常"),
    DISABLED(0, "禁用");
    
    private Integer code;
    private String desc;
}

public enum OrderType {
    ONLINE_ORDER(1, "线上订单"),
    OFFLINE_ORDER(2, "线下订单");
}

// ❌ 错误示例
public enum userStatus { }
public enum UserStatus {
    normal, disabled  // 应该全大写
}
```

## 三、前端命名规范

### 1. Vue 组件命名

```vue
<!-- 文件名：user-list.vue -->
<script>
/**
 * 规则：
 * - 文件名：小写+连字符
 * - 组件名：大驼峰
 */

// ✅ 正确示例
export default {
  name: "UserList",
  components: {
    UserDialog,
    DataTable
  }
}

// ❌ 错误示例
export default {
  name: "user-list",  // 组件名应该用大驼峰
  components: {
    'user-dialog': UserDialog  // 组件引用应该用大驼峰
  }
}
</script>
```

### 2. JavaScript 变量命名

```javascript
/**
 * 规则：小驼峰命名法
 */

// ✅ 正确示例
const userName = "张三"
const userId = "123"
const createTime = "2024-12-01"
const userList = []
const formData = {}

// ❌ 错误示例
const UserName = "张三"
const user_name = "张三"
const username = "张三"  // 单词分隔不清晰
```

### 3. JavaScript 函数命名

```javascript
/**
 * 规则：小驼峰命名法
 * 动词开头：handle/get/set/init/load/fetch/update/delete/add/edit/query
 */

// ✅ 正确示例
function handleAdd() { }
function handleEdit() { }
function handleDelete() { }
function handleQuery() { }
function getUserInfo() { }
function setUserInfo() { }
function initForm() { }
function loadData() { }
function fetchUserList() { }

// ❌ 错误示例
function HandleAdd() { }
function handle_add() { }
function add() { }  // 缺少动词前缀
```

### 4. JavaScript 常量命名

```javascript
/**
 * 规则：全大写，单词间用下划线分隔
 */

// ✅ 正确示例
const API_BASE_URL = "http://localhost:8080"
const MAX_FILE_SIZE = 10 * 1024 * 1024
const DATE_FORMAT = "YYYY-MM-DD HH:mm:ss"
const DEFAULT_PAGE_SIZE = 10

// ❌ 错误示例
const apiBaseUrl = "http://localhost:8080"
const MaxFileSize = 10 * 1024 * 1024
```

### 5. Vue 事件命名

```vue
<template>
  <!-- 规则：小写+连字符 -->
  
  <!-- ✅ 正确示例 -->
  <fb-button @click="handleAdd">新增</fb-button>
  <fb-button @click="handleEdit">编辑</fb-button>
  <fb-button @click="handleDelete">删除</fb-button>
  
  <!-- 自定义事件 -->
  <user-dialog @close="handleClose" />
  <data-table @row-click="handleRowClick" />
  
  <!-- ❌ 错误示例 -->
  <fb-button @click="HandleAdd">新增</fb-button>
  <fb-button @click="handle_add">新增</fb-button>
</template>
```

### 6. Vue Props 命名

```javascript
/**
 * 规则：
 * - 定义时：小驼峰
 * - 使用时：小写+连字符
 */

// ✅ 正确示例
export default {
  props: {
    userName: String,
    userId: String,
    maxLength: Number,
    isDisabled: Boolean
  }
}

// 使用时
<user-dialog
  :user-name="userName"
  :user-id="userId"
  :max-length="100"
  :is-disabled="false"
/>
```

### 7. data 属性命名

```javascript
/**
 * 规则：小驼峰命名法
 */

// ✅ 正确示例
data() {
  return {
    formData: {},
    queryForm: {},
    tableData: [],
    loading: false,
    visible: false,
    currentPage: 1,
    pageSize: 10,
    totalCount: 0
  }
}

// ❌ 错误示例
data() {
  return {
    FormData: {},  // 首字母不应大写
    query_form: {},  // 不应使用下划线
    data: [],  // 名称过于简单
    flag: false  // 名称不明确
  }
}
```

## 四、数据库命名规范

### 1. 表名命名

```sql
/**
 * 规则：
 * - 全小写
 * - 单词间用下划线分隔
 * - 前缀：sys_（系统表）、biz_（业务表）
 */

-- ✅ 正确示例
CREATE TABLE sys_user (...);
CREATE TABLE sys_dept (...);
CREATE TABLE sys_role (...);
CREATE TABLE biz_order (...);
CREATE TABLE biz_product (...);

-- ❌ 错误示例
CREATE TABLE SysUser (...);
CREATE TABLE sys_User (...);
CREATE TABLE user (...);  -- 缺少前缀
```

### 2. 字段名命名

```sql
/**
 * 规则：
 * - 全小写
 * - 单词间用下划线分隔
 */

-- ✅ 正确示例
CREATE TABLE sys_user (
  id varchar(32),
  user_name varchar(100),
  real_name varchar(100),
  create_time datetime,
  update_time datetime,
  create_by varchar(32),
  update_by varchar(32)
);

-- ❌ 错误示例
CREATE TABLE sys_user (
  Id varchar(32),
  UserName varchar(100),
  realName varchar(100),
  createTime datetime
);
```

### 3. 索引命名

```sql
/**
 * 规则：
 * - 普通索引：idx_表名_字段名
 * - 唯一索引：uk_表名_字段名
 * - 主键索引：pk_表名
 */

-- ✅ 正确示例
ALTER TABLE sys_user ADD INDEX idx_user_name (user_name);
ALTER TABLE sys_user ADD UNIQUE INDEX uk_user_username (username);
ALTER TABLE sys_user ADD PRIMARY KEY pk_user (id);

-- ❌ 错误示例
ALTER TABLE sys_user ADD INDEX user_name_index (user_name);
ALTER TABLE sys_user ADD INDEX index_1 (user_name);
```

## 五、URL 路径命名规范

### 1. 后端接口路径

```java
/**
 * 规则：
 * - 全小写
 * - 单词间用连字符分隔
 * - 使用复数形式（可选）
 */

// ✅ 正确示例
@RequestMapping("/sys/user")
@GetMapping("/list")  // /sys/user/list
@GetMapping("/query-page")  // /sys/user/query-page
@PostMapping("/add")  // /sys/user/add
@PutMapping("/edit")  // /sys/user/edit
@DeleteMapping("/delete")  // /sys/user/delete

// ❌ 错误示例
@RequestMapping("/sys/User")
@GetMapping("/List")
@GetMapping("/query_page")
@GetMapping("/queryPage")
```

### 2. 前端路由路径

```javascript
/**
 * 规则：
 * - 全小写
 * - 单词间用连字符分隔
 */

// ✅ 正确示例
{
  path: '/system/user-list',
  name: 'UserList',
  component: () => import('./user-list.vue')
}

{
  path: '/system/role-management',
  name: 'RoleManagement',
  component: () => import('./role-management.vue')
}

// ❌ 错误示例
{
  path: '/system/UserList',
  path: '/system/user_list',
  path: '/system/userList'
}
```

## 六、特殊命名规范

### 1. 布尔类型命名

```java
// Java
private Boolean isAdmin;
private Boolean hasPermission;
private Boolean canEdit;
private Boolean enabled;

// JavaScript
const isVisible = true
const hasData = false
const canDelete = true
const disabled = false
```

### 2. 集合类型命名

```java
// Java - 使用复数或 List 后缀
private List<UserVO> userList;
private List<String> idList;
private List<RoleVO> roles;

// JavaScript - 使用复数或 List 后缀
const userList = []
const idList = []
const roles = []
```

### 3. 临时变量命名

```java
// 循环变量
for (int i = 0; i < list.size(); i++) { }
for (UserVO user : userList) { }

// 临时变量
UserVO temp = userList.get(0);
String result = processData(data);
```

## 七、必须遵守的规则

1. **类名使用大驼峰**：`UserEntity`, `UserController`
2. **方法/变量使用小驼峰**：`getUserInfo`, `userName`
3. **常量全大写**：`DEFAULT_PASSWORD`, `MAX_PAGE_SIZE`
4. **文件名使用小写+连字符**：`user-list.vue`, `user-service.js`
5. **数据库表名/字段名全小写+下划线**：`sys_user`, `create_time`
6. **URL 路径全小写+连字符**：`/sys/user-list`, `/query-page`
7. **布尔类型使用 is/has/can 前缀**：`isAdmin`, `hasPermission`
8. **集合类型使用复数或 List 后缀**：`userList`, `roles`
9. **Service 接口使用 I 前缀**：`IUserService`
10. **枚举值全大写+下划线**：`ONLINE_ORDER`, `OFFLINE_ORDER`

## 八、常见错误和解决方案

### 1. ❌ 错误：类名首字母小写

```java
// ❌ 错误
public class userEntity { }

// ✅ 正确
public class UserEntity { }
```

### 2. ❌ 错误：方法名首字母大写

```java
// ❌ 错误
public void AddUser() { }

// ✅ 正确
public void addUser() { }
```

### 3. ❌ 错误：变量名使用下划线

```java
// ❌ 错误
private String user_name;

// ✅ 正确
private String userName;
```

### 4. ❌ 错误：常量使用小驼峰

```java
// ❌ 错误
public static final String defaultPassword = "123456";

// ✅ 正确
public static final String DEFAULT_PASSWORD = "123456";
```

### 5. ❌ 错误：文件名使用大驼峰

```javascript
// ❌ 错误
// UserList.vue
// UserService.js

// ✅ 正确
// user-list.vue
// user-service.js
```

### 6. ❌ 错误：数据库字段使用驼峰

```sql
-- ❌ 错误
CREATE TABLE sys_user (
  createTime datetime
);

-- ✅ 正确
CREATE TABLE sys_user (
  create_time datetime
);
```
