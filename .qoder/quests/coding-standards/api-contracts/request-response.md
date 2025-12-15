# API请求响应规范

## 文档说明

本文档定义了PS-BMP系统前后端通信的标准规范，包括请求格式、响应格式、数据类型映射等。所有API开发必须严格遵循本规范。

---

## 1. 请求规范

### 1.1 请求路径规范

#### 路径前缀规则

| 模块类型 | 路径前缀 | 示例 |
|---------|---------|------|
| 系统管理类 | `/sys/` | `/sys/third-party-app/list` |
| 组织人员类 | `/sys/` | `/sys/person/org/list` |
| 认证授权类 | `/auth/` | `/auth/login` |
| 业务功能类 | `/biz/` | `/biz/order/list` |

⚠️ **重要**: 系统管理相关接口必须包含 `/sys` 前缀，避免404错误

**正确示例**:
```
GET  /sys/third-party-app/list
POST /sys/third-party-app/add
POST /sys/third-party-app/update
GET  /sys/third-party-app/delete
```

**错误示例**:
```
GET  /third-party-app/list        ❌ 缺少/sys前缀
POST /api/third-party-app/add     ❌ 使用了错误的前缀
```

#### 路径命名规范

- **使用kebab-case**: `third-party-app`（不是`thirdPartyApp`）
- **RESTful风格**: 使用名词复数或功能描述
- **操作动词**: list/add/update/delete/view

### 1.2 请求方式

#### GET请求

**适用场景**:
- 查询操作（列表、详情）
- 删除操作（简单参数）
- 下载操作

**参数传递**: Query String

**示例**:
```javascript
// 前端
app.service.get('/sys/third-party-app/list', {
  params: {
    appName: '移动应用',
    status: 1,
    page: 1,
    size: 10
  }
})
```

```java
// 后端Controller
@GetMapping("/list")
public Result list(
    @RequestParam(required = false) String appName,
    @RequestParam(required = false) Integer status,
    @RequestParam(defaultValue = "1") Integer page,
    @RequestParam(defaultValue = "10") Integer size
) {
    // 处理逻辑
}
```

#### POST请求（表单格式）

**适用场景**:
- 新增操作
- 修改操作
- 复杂参数的查询

**Content-Type**: `application/x-www-form-urlencoded`

**前端配置**:
```javascript
app.service.request({
  url: '/sys/third-party-app/add',
  method: 'post',
  transformRequest: [
    function (data) {
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' +
          encodeURIComponent(data[it]) + '&'
      }
      return ret.substring(0, ret.lastIndexOf('&'))
    },
  ],
  data: formData,
  headers: {'Content-Type': 'application/x-www-form-urlencoded'},
  responseType: 'json',
  timeout: 5000,
})
```

**后端接收**:
```java
// 方式1: 直接参数绑定（推荐）
@PostMapping("/add")
public Result add(
    @RequestParam String appName,
    @RequestParam Integer status,
    @RequestParam(required = false) String description
) {
    // 处理逻辑
}

// 方式2: 对象自动封装
@PostMapping("/add")
public Result add(ThirdPartyAppDTO dto) {
    // dto会自动封装表单参数
}
```

⚠️ **注意**: 表单格式请求**不要**使用`@RequestBody`注解

#### POST请求（JSON格式）

**适用场景**:
- 复杂对象提交
- 数组参数提交
- 嵌套对象提交

**Content-Type**: `application/json`

**前端配置**:
```javascript
app.service.request({
  url: '/sys/third-party-app/config-permissions',
  method: 'post',
  data: {
    appId: 'APP001',
    apiIds: ['API001', 'API002', 'API003']  // 数组参数
  },
  headers: {'Content-Type': 'application/json'},
  responseType: 'json',
  timeout: 5000,
})
```

**后端接收**:
```java
@PostMapping("/config-permissions")
public Result configPermissions(@RequestBody ApiPermissionDTO dto) {
    // dto.getAppId()
    // dto.getApiIds() -> List<String>
}
```

✅ **注意**: JSON格式请求**必须**使用`@RequestBody`注解

#### POST请求（文件上传）

**Content-Type**: `multipart/form-data`

**前端配置**:
```javascript
const formData = new FormData()
formData.append('file', file)
formData.append('deptId', 'D001')
formData.append('ascnId', 'A001')

app.service.request({
  url: '/sys/person/import-excel',
  method: 'post',
  data: formData,
  headers: { 'Content-Type': 'multipart/form-data' },
  timeout: 60000,
})
```

**后端接收**:
```java
@PostMapping("/import-excel")
public Result importExcel(
    @RequestParam("file") MultipartFile file,
    @RequestParam String deptId,
    @RequestParam String ascnId
) {
    // 处理文件上传
}
```

### 1.3 请求参数规范

#### 分页参数

**标准分页参数**:
```javascript
{
  page: 1,      // 页码（从1开始）
  size: 10,     // 每页条数
  // 或使用
  pageNum: 1,   // 页码
  pageSize: 10  // 每页大小
}
```

#### 排序参数

```javascript
{
  sortField: 'createTime',  // 排序字段
  sortOrder: 'desc'         // 排序方式：asc/desc
}
```

#### 查询参数

```javascript
{
  // 精确查询
  status: 1,
  deptId: 'D001',
  
  // 模糊查询（后端使用LIKE）
  personName: '张',
  phone: '138',
  
  // 范围查询
  startTime: '20241201000000',
  endTime: '20241231235959',
  
  // 逻辑删除过滤
  logDelete: 0  // 默认查询未删除数据
}
```

---

## 2. 响应规范

### 2.1 统一响应结构

**标准响应格式**:
```json
{
  "code": 1,
  "message": "操作成功",
  "data": {
    // 具体业务数据
  }
}
```

**字段说明**:

| 字段 | 类型 | 说明 | 必需 |
|------|------|------|------|
| code | Integer | 响应状态码（1成功，其他失败） | ✅ |
| message | String | 响应消息 | ✅ |
| data | Object/Array | 业务数据 | ❌ |

### 2.2 成功响应示例

#### 单条数据响应

```json
{
  "code": 1,
  "message": "查询成功",
  "data": {
    "personId": "P001",
    "personName": "张三",
    "phone": "13800138000",
    "deptId": "D001",
    "deptName": "技术部"
  }
}
```

#### 列表数据响应

```json
{
  "code": 1,
  "message": "查询成功",
  "data": {
    "total": 100,
    "records": [
      {
        "personId": "P001",
        "personName": "张三"
      },
      {
        "personId": "P002",
        "personName": "李四"
      }
    ]
  }
}
```

**分页响应字段**:

| 字段 | 类型 | 说明 |
|------|------|------|
| total | Integer | 总记录数 |
| records | Array | 数据列表 |
| page | Integer | 当前页码（可选） |
| size | Integer | 每页条数（可选） |

#### 简单操作响应

```json
{
  "code": 1,
  "message": "操作成功",
  "data": null
}
```

或

```json
{
  "code": 1,
  "message": "新增成功",
  "data": {
    "id": "P001"  // 返回新增记录的ID
  }
}
```

### 2.3 失败响应示例

#### 参数错误

```json
{
  "code": 400,
  "message": "参数错误：应用名称不能为空",
  "data": null
}
```

#### 业务错误

```json
{
  "code": 500,
  "message": "操作失败：该应用已存在",
  "data": null
}
```

#### 权限错误

```json
{
  "code": 403,
  "message": "无权限访问",
  "data": null
}
```

#### 未登录

```json
{
  "code": 401,
  "message": "未登录或登录已过期",
  "data": null
}
```

### 2.4 响应状态码

| 状态码 | 说明 | 使用场景 |
|--------|------|---------|
| 1 | 成功 | 操作成功 |
| 400 | 参数错误 | 请求参数校验失败 |
| 401 | 未认证 | 未登录或Token过期 |
| 403 | 无权限 | 没有访问权限 |
| 404 | 资源不存在 | 数据不存在 |
| 500 | 服务器错误 | 业务逻辑错误 |

---

## 3. 数据类型映射

### 3.1 基础类型映射

| Java类型 | JavaScript类型 | JSON类型 | 说明 |
|---------|---------------|---------|------|
| String | string | "string" | 字符串 |
| Integer | number | 123 | 整数 |
| Long | number | 123 | 长整数 |
| Double | number | 123.45 | 浮点数 |
| Boolean | boolean | true/false | 布尔值 |
| Date | string | "20241201120000" | 日期（特殊格式） |
| List\<T\> | Array | [] | 数组 |
| Map<K,V> | Object | {} | 对象 |

### 3.2 特殊类型处理

#### 日期时间

**后端存储**: `String` 格式 `YYYYMMDDHHmmss`

**数据库存储**: `VARCHAR(14)` 或 `BIGINT`

**前端接收**: `String`

**前端显示**: 格式化为 `YYYY-MM-DD HH:mm:ss`

**示例**:
```javascript
// 后端返回
{
  createTime: "20241201153045"
}

// 前端格式化显示
formatTime(val) {
  if (!val) return '-';
  return dayjs(val, 'YYYYMMDDHHmmss').format('YYYY-MM-DD HH:mm:ss');
}
// 显示: 2024-12-01 15:30:45
```

#### 枚举类型

**后端定义**:
```java
// 状态枚举
public enum StatusEnum {
    ENABLED(1, "启用"),
    DISABLED(0, "禁用");
    
    private Integer code;
    private String desc;
}
```

**接口返回**: 返回枚举的`code`值
```json
{
  "status": 1  // 不是"ENABLED"
}
```

**前端映射**:
```javascript
const statusMap = {
  1: '启用',
  0: '禁用'
}

// 或使用formatter
formatters: {
  status(val) {
    return val === 1 ? '启用' : '禁用';
  }
}
```

#### 布尔类型

**后端**: `Boolean` 或 `Integer`（0/1）

**前端**: `boolean` 或 `number`（0/1）

**建议**: 统一使用Integer（0/1）避免歧义

```java
// 后端
private Integer status;  // 1启用 0禁用

// 前端
status: 1  // 不是 true
```

#### 数组类型

**后端**: `List<String>` 或 `List<Object>`

**前端**: `Array`

**示例**:
```javascript
// 提交
{
  roleIds: ['R001', 'R002', 'R003']
}

// 返回
{
  roles: [
    { roleId: 'R001', roleName: '管理员' },
    { roleId: 'R002', roleName: '操作员' }
  ]
}
```

---

## 4. 错误处理规范

### 4.1 前端错误处理

#### 统一错误处理

```javascript
this.service.add(formData).then((result) => {
  if (result.code == 1) {
    // 成功处理
    this.$message.success(result.message || '操作成功');
    // 执行后续逻辑
  } else {
    // 失败处理
    this.$message.error(result.message || '操作失败');
  }
}).catch((error) => {
  // 网络异常或其他错误
  console.error('请求失败:', error);
  this.$message.error('请求失败，请稍后重试');
})
```

#### 特定错误处理

```javascript
this.service.add(formData).then((result) => {
  if (result.code == 1) {
    this.$message.success('新增成功');
    this.closeTpDialog({ success: true, action: 'add' });
  } else if (result.code == 400) {
    // 参数错误
    this.$message.warn(result.message);
  } else if (result.code == 500) {
    // 业务错误
    this.$message.error(result.message);
  } else {
    // 其他错误
    this.$message.error('操作失败');
  }
})
```

### 4.2 后端错误处理

#### 统一响应构造

```java
// 成功响应
return Result.success("操作成功", data);
return Result.success("操作成功");  // 无数据

// 失败响应
return Result.error("操作失败");
return Result.error(500, "业务处理失败");

// 参数校验失败
return Result.error(400, "参数错误：应用名称不能为空");
```

#### 异常处理

```java
@ExceptionHandler(BusinessException.class)
public Result handleBusinessException(BusinessException e) {
    return Result.error(e.getCode(), e.getMessage());
}

@ExceptionHandler(Exception.class)
public Result handleException(Exception e) {
    log.error("系统异常", e);
    return Result.error("系统异常，请联系管理员");
}
```

---

## 5. 常见通信场景

### 5.1 查询列表

**前端请求**:
```javascript
handleQuery() {
  this.$refs.table.doSearch()
}

// table配置
table: {
  service: app.$svc.sys.thirdPartyApp,  // 自动调用service.list()
  primaryKey: "appId",
  columns: [...]
}

// 查询参数
formData: {
  appName: '移动',
  status: 1,
  logDelete: 0
}
```

**后端接口**:
```java
@GetMapping("/list")
public Result list(
    @RequestParam(required = false) String appName,
    @RequestParam(required = false) Integer status,
    @RequestParam(defaultValue = "0") Integer logDelete,
    @RequestParam(defaultValue = "1") Integer page,
    @RequestParam(defaultValue = "10") Integer size
) {
    PageInfo<ThirdPartyApp> pageInfo = service.list(appName, status, logDelete, page, size);
    return Result.success("查询成功", pageInfo);
}
```

**响应数据**:
```json
{
  "code": 1,
  "message": "查询成功",
  "data": {
    "total": 50,
    "records": [...]
  }
}
```

### 5.2 查询详情

**前端请求**:
```javascript
view(appId, passKey) {
  this.service.view({"appId": appId, "passKey": passKey}).then((result) => {
    if (result.code == 1) {
      this.formData = result.data;
      // 日期格式转换
      if (this.formData.expireTime) {
        this.formData.expireTime = dayjs(this.formData.expireTime, 'YYYYMMDDHHmmss').toDate();
      }
    }
  })
}
```

**后端接口**:
```java
@GetMapping("/view")
public Result view(
    @RequestParam String appId,
    @RequestParam String passKey
) {
    ThirdPartyApp app = service.getById(appId, passKey);
    return Result.success("查询成功", app);
}
```

### 5.3 新增数据

**前端请求**:
```javascript
save() {
  this.$refs.fbform.validate((result) => {
    if (result === true) {
      const submitData = { ...this.formData };
      
      // 日期格式转换
      if (submitData.expireTime) {
        submitData.expireTime = dayjs(submitData.expireTime).format('YYYYMMDDHHmmss');
      } else {
        submitData.expireTime = ''
      }
      
      this.service.add(submitData).then((result) => {
        if (result.code == 1) {
          this.$message.success('新增成功');
          this.closeTpDialog({ success: true, action: 'add' });
        } else {
          this.$message.error('新增失败: ' + result.message)
        }
      })
    }
  })
}
```

**后端接口**:
```java
@PostMapping("/add")
public Result add(ThirdPartyAppDTO dto) {
    // 参数校验
    if (StringUtils.isBlank(dto.getAppName())) {
        return Result.error(400, "应用名称不能为空");
    }
    
    // 业务处理
    service.add(dto);
    
    return Result.success("新增成功");
}
```

### 5.4 修改数据

**前端请求**:
```javascript
save() {
  this.$refs.fbform.validate((result) => {
    if (result === true) {
      const submitData = { ...this.formData };
      
      // 必须传递passKey
      submitData.passKey = this.param.passKey;
      
      // 日期格式转换
      if (submitData.expireTime) {
        submitData.expireTime = dayjs(submitData.expireTime).format('YYYYMMDDHHmmss');
      }
      
      this.service.update(submitData).then((result) => {
        if (result.code == 1) {
          this.$message.success('修改成功');
          this.closeTpDialog({ success: true, action: 'edit' });
        } else {
          this.$message.error('修改失败:' + result.message)
        }
      })
    }
  })
}
```

**后端接口**:
```java
@PostMapping("/update")
public Result update(ThirdPartyAppDTO dto) {
    // 校验passKey
    if (!service.validatePassKey(dto.getAppId(), dto.getPassKey())) {
        return Result.error(403, "数据校验失败");
    }
    
    // 更新操作
    service.update(dto);
    
    return Result.success("修改成功");
}
```

### 5.5 删除数据

**前端请求**:
```javascript
handleDel(row) {
  this.$confirm('确定要删除该应用吗？删除后将无法恢复！', () => {
    this.delete(row.appId, row.passKey);
  })
}

delete(appId, passKey) {
  app.service.request('/sys/third-party-app/delete', {
    method: 'get',
    params: {"appId": appId, "passKey": passKey},
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    responseType: 'json',
    timeout: 5000,
  }).then((result) => {
    if (result.code == 1) {
      this.$message.success('删除成功');
      this.$refs.table.doReload();
    } else {
      this.$message.error('删除失败: ' + result.message)
    }
  })
}
```

**后端接口**:
```java
@GetMapping("/delete")
public Result delete(
    @RequestParam String appId,
    @RequestParam String passKey
) {
    // 校验passKey
    if (!service.validatePassKey(appId, passKey)) {
        return Result.error(403, "数据校验失败");
    }
    
    // 逻辑删除
    service.delete(appId);
    
    return Result.success("删除成功");
}
```

---

## 6. 文件上传下载

### 6.1 文件上传

**前端**:
```javascript
handleImportExcel() {
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = '.xlsx,.xls'
  
  input.onchange = (event) => {
    const file = event.target.files[0]
    if (!file) return
    
    const formData = new FormData()
    formData.append('file', file)
    formData.append('deptId', this.selectNode.deptId)
    
    app.$svc.sys.person.importExcel(formData).then((response) => {
      if (response.code === 1) {
        this.$message.success('导入成功')
        this.handleQuery()
      } else {
        this.$message.error('导入失败: ' + response.message)
      }
    })
  }
  
  document.body.appendChild(input)
  input.click()
}
```

**后端**:
```java
@PostMapping("/import-excel")
public Result importExcel(
    @RequestParam("file") MultipartFile file,
    @RequestParam String deptId
) {
    try {
        ImportResult result = service.importExcel(file, deptId);
        return Result.success("导入成功", result);
    } catch (Exception e) {
        return Result.error("导入失败: " + e.getMessage());
    }
}
```

### 6.2 文件下载

**前端**:
```javascript
handleExportExcel() {
  this.exportLoading = true

  app.$svc.sys.person.exportExcel(this.formData).then((response) => {
    const blob = new Blob([response.data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    })

    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '人员信息.xlsx'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)

    this.$message.success('导出成功')
  }).finally(() => {
    this.exportLoading = false
  })
}
```

**Service配置**:
```javascript
exportExcel(formData) {
  return app.service.request({
    url: '/sys/person/export-excel',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'application/json' },
    responseType: 'blob',  // 重要！
    timeout: 300000,       // 5分钟
  })
}
```

**后端**:
```java
@PostMapping("/export-excel")
public void exportExcel(
    @RequestBody PersonQueryDTO dto,
    HttpServletResponse response
) {
    try {
        List<Person> persons = service.queryList(dto);
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=persons.xlsx");
        
        service.exportToExcel(persons, response.getOutputStream());
    } catch (Exception e) {
        log.error("导出失败", e);
    }
}
```

---

## 7. 常见错误与解决方案

### 7.1 404错误

❌ **错误原因**: 缺少路径前缀

```javascript
// 错误
app.service.get('/third-party-app/list')
```

✅ **解决方案**: 添加/sys前缀

```javascript
// 正确
app.service.get('/sys/third-party-app/list')
```

### 7.2 参数绑定失败

❌ **错误原因**: 表单请求使用了@RequestBody

```java
// 错误
@PostMapping("/add")
public Result add(@RequestBody ThirdPartyAppDTO dto) {
    // Content-Type是application/x-www-form-urlencoded时无法绑定
}
```

✅ **解决方案**: 移除@RequestBody

```java
// 正确
@PostMapping("/add")
public Result add(ThirdPartyAppDTO dto) {
    // 自动绑定表单参数
}
```

### 7.3 JSON绑定失败

❌ **错误原因**: JSON请求未使用@RequestBody

```java
// 错误
@PostMapping("/config-permissions")
public Result config(ApiPermissionDTO dto) {
    // Content-Type是application/json时无法绑定
}
```

✅ **解决方案**: 添加@RequestBody

```java
// 正确
@PostMapping("/config-permissions")
public Result config(@RequestBody ApiPermissionDTO dto) {
    // 正确绑定JSON数据
}
```

### 7.4 字段名不匹配

❌ **错误**: 前后端字段名不一致

```javascript
// 前端
formData.person_id = 'P001'

// 后端
private String personId;
```

✅ **解决方案**: 统一使用camelCase

```javascript
// 前端
formData.personId = 'P001'

// 后端
private String personId;
```

---

## 8. 检查清单

### 8.1 API开发检查清单

**后端开发**:
- [ ] 路径包含正确前缀（/sys）
- [ ] 选择正确的请求方式（GET/POST）
- [ ] 正确使用@RequestBody注解
  - 表单请求：不使用
  - JSON请求：使用
  - 文件上传：不使用
- [ ] 返回统一响应格式（code/message/data）
- [ ] 添加参数校验
- [ ] 处理业务异常
- [ ] 校验passKey（修改/删除操作）
- [ ] 过滤logDelete（查询操作）

**前端开发**:
- [ ] 使用正确的API路径（含前缀）
- [ ] 设置正确的Content-Type
- [ ] 表单请求配置transformRequest
- [ ] 设置合理的timeout
- [ ] 处理成功和失败响应
- [ ] 捕获网络异常
- [ ] 日期格式正确转换
- [ ] 字段名与后端一致

---

## 版本信息

- **版本号**: v1.0.0
- **创建日期**: 2025年12月1日
- **最后更新**: 2025年12月1日

---

**重要提醒**: 严格遵循本规范可避免90%以上的前后端通信问题！
