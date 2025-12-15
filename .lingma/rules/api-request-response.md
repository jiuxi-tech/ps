---
trigger: always
description: 前后端 API 请求响应规范，包括统一返回格式、错误处理、HTTP 状态码等
---

# API 请求响应规范

## 一、统一返回格式

### 1. Result 对象结构

所有后端接口必须返回统一的 `Result` 对象：

```java
{
  "success": true,         // 是否成功
  "message": "操作成功",    // 提示消息
  "code": 200,             // 状态码
  "result": {},            // 返回数据
  "timestamp": 1701417045000  // 时间戳
}
```

### 2. Result 类定义

```java
/**
 * 统一返回对象
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@Data
public class Result<T> {
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 提示消息
     */
    private String message;
    
    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 返回数据
     */
    private T result;
    
    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();
    
}
```

### 3. Result 工具方法

```java
/**
 * 成功，无返回数据
 */
public static<T> Result<T> OK() {
    Result<T> r = new Result<>();
    r.setSuccess(true);
    r.setCode(200);
    r.setMessage("操作成功");
    return r;
}

/**
 * 成功，有返回数据
 */
public static<T> Result<T> OK(T data) {
    Result<T> r = new Result<>();
    r.setSuccess(true);
    r.setCode(200);
    r.setResult(data);
    return r;
}

/**
 * 成功，自定义消息
 */
public static<T> Result<T> OK(String message) {
    Result<T> r = new Result<>();
    r.setSuccess(true);
    r.setCode(200);
    r.setMessage(message);
    return r;
}

/**
 * 失败，错误消息
 */
public static<T> Result<T> error(String message) {
    Result<T> r = new Result<>();
    r.setSuccess(false);
    r.setCode(500);
    r.setMessage(message);
    return r;
}

/**
 * 失败，自定义状态码和消息
 */
public static<T> Result<T> error(int code, String message) {
    Result<T> r = new Result<>();
    r.setSuccess(false);
    r.setCode(code);
    r.setMessage(message);
    return r;
}
```

## 二、Controller 返回规范

### 1. 查询接口

```java
/**
 * 分页查询
 */
@GetMapping("/queryPage")
public Result<?> queryPage(XxxDTO dto,
                           @RequestParam(defaultValue = "1") Integer pageNo,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
    Page<XxxVO> page = new Page<>(pageNo, pageSize);
    IPage<XxxVO> pageList = xxxService.queryPage(page, dto);
    return Result.OK(pageList);
}

/**
 * 列表查询
 */
@GetMapping("/list")
public Result<?> queryList(XxxDTO dto) {
    List<XxxVO> list = xxxService.queryList(dto);
    return Result.OK(list);
}

/**
 * 详情查询
 */
@GetMapping("/queryById")
public Result<?> queryById(@RequestParam String id) {
    XxxVO vo = xxxService.queryById(id);
    return Result.OK(vo);
}
```

### 2. 新增接口

```java
/**
 * 新增
 */
@PostMapping("/add")
public Result<?> add(@RequestBody XxxDTO dto) {
    xxxService.add(dto);
    return Result.OK("添加成功");
}
```

### 3. 修改接口

```java
/**
 * 修改
 */
@PutMapping("/edit")
public Result<?> edit(@RequestBody XxxDTO dto) {
    xxxService.edit(dto);
    return Result.OK("修改成功");
}
```

### 4. 删除接口

```java
/**
 * 删除
 */
@DeleteMapping("/delete")
public Result<?> delete(@RequestParam String id) {
    xxxService.removeById(id);
    return Result.OK("删除成功");
}

/**
 * 批量删除
 */
@DeleteMapping("/deleteBatch")
public Result<?> deleteBatch(@RequestParam String ids) {
    List<String> idList = Arrays.asList(ids.split(","));
    xxxService.removeByIds(idList);
    return Result.OK("批量删除成功");
}
```

## 三、分页返回格式

### 1. 分页对象结构

```java
{
  "success": true,
  "message": "操作成功",
  "code": 200,
  "result": {
    "records": [
      {
        "id": "1",
        "name": "测试数据"
      }
    ],
    "total": 100,        // 总记录数
    "size": 10,          // 每页条数
    "current": 1,        // 当前页码
    "pages": 10          // 总页数
  },
  "timestamp": 1701417045000
}
```

### 2. 前端分页参数

```javascript
const params = {
  pageNo: 1,      // 页码，从1开始
  pageSize: 10,   // 每页条数
  // 其他查询条件
  name: "xxx",
  type: 1
}
```

### 3. 后端分页处理

```java
@GetMapping("/queryPage")
public Result<?> queryPage(XxxDTO dto,
                           @RequestParam(defaultValue = "1") Integer pageNo,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
    Page<XxxVO> page = new Page<>(pageNo, pageSize);
    IPage<XxxVO> pageList = xxxService.queryPage(page, dto);
    return Result.OK(pageList);
}
```

## 四、前端 Service 层规范

### 1. GET 请求

```javascript
/**
 * 分页查询
 */
queryPage(params) {
  return app.service.request({
    url: '/sys/xxx/queryPage',
    method: 'get',
    params: params,
    responseType: 'json',
    timeout: 5000,
  })
}

/**
 * 详情查询
 */
queryById(id) {
  return app.service.request({
    url: '/sys/xxx/queryById',
    method: 'get',
    params: { id },
    responseType: 'json',
    timeout: 5000,
  })
}
```

### 2. POST 请求（JSON）

```javascript
/**
 * 新增（JSON 格式）
 */
add(formData) {
  return app.service.request({
    url: '/sys/xxx/add',
    method: 'post',
    data: formData,
    headers: {'Content-Type': 'application/json'},
    responseType: 'json',
    timeout: 5000,
  })
}
```

### 3. POST 请求（表单）

```javascript
/**
 * 新增（表单格式）
 */
add(formData) {
  return app.service.request({
    url: '/sys/xxx/add',
    method: 'post',
    transformRequest: [
      function (data) {
        let ret = ''
        for (let it in data) {
          ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
        }
        return ret.substring(0, ret.lastIndexOf('&'))
      },
    ],
    data: formData,
    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
    responseType: 'json',
    timeout: 5000,
  })
}
```

### 4. PUT 请求

```javascript
/**
 * 修改
 */
edit(formData) {
  return app.service.request({
    url: '/sys/xxx/edit',
    method: 'put',
    data: formData,
    headers: {'Content-Type': 'application/json'},
    responseType: 'json',
    timeout: 5000,
  })
}
```

### 5. DELETE 请求

```javascript
/**
 * 删除
 */
delete(id) {
  return app.service.request({
    url: '/sys/xxx/delete',
    method: 'delete',
    params: { id },
    responseType: 'json',
    timeout: 5000,
  })
}

/**
 * 批量删除
 */
deleteBatch(ids) {
  return app.service.request({
    url: '/sys/xxx/deleteBatch',
    method: 'delete',
    params: { ids: ids.join(',') },
    responseType: 'json',
    timeout: 5000,
  })
}
```

## 五、前端响应处理

### 1. 成功响应

```javascript
const result = await xxxService.add(formData)

if (result.success) {
  this.$message.success(result.message || '操作成功')
  // 执行后续逻辑
} else {
  this.$message.error(result.message || '操作失败')
}
```

### 2. 分页数据处理

```javascript
const result = await xxxService.queryPage(params)

if (result.success) {
  this.tableData = result.result.records
  this.total = result.result.total
}
```

### 3. 详情数据处理

```javascript
const result = await xxxService.queryById(id)

if (result.success) {
  this.formData = result.result
}
```

## 六、错误处理

### 1. 业务异常

```java
// 后端抛出业务异常
throw new BusinessException("名称已存在");
```

```java
// 全局异常处理器捕获
{
  "success": false,
  "message": "名称已存在",
  "code": 500,
  "result": null,
  "timestamp": 1701417045000
}
```

### 2. 参数校验异常

```java
// 后端 DTO 校验
@NotBlank(message = "名称不能为空")
private String name;
```

```java
// 校验失败返回
{
  "success": false,
  "message": "名称不能为空",
  "code": 500,
  "result": null,
  "timestamp": 1701417045000
}
```

### 3. 系统异常

```java
// 全局异常处理器捕获系统异常
{
  "success": false,
  "message": "系统错误，请联系管理员",
  "code": 500,
  "result": null,
  "timestamp": 1701417045000
}
```

### 4. 前端错误处理

```javascript
try {
  const result = await xxxService.add(formData)
  
  if (result.success) {
    this.$message.success(result.message || '操作成功')
  } else {
    this.$message.error(result.message || '操作失败')
  }
} catch (error) {
  console.error(error)
  this.$message.error('网络错误，请稍后重试')
}
```

## 七、HTTP 状态码规范

### 1. 常用状态码

| 状态码 | 说明 | 使用场景 |
|-------|------|----------|
| **200** | 成功 | 请求成功 |
| **400** | 请求错误 | 参数错误、业务校验失败 |
| **401** | 未授权 | 未登录或 Token 失效 |
| **403** | 禁止访问 | 无权限访问 |
| **404** | 未找到 | 资源不存在 |
| **500** | 服务器错误 | 系统异常 |

### 2. Result 中的 code 字段

虽然 HTTP 状态码统一返回 200，但 Result 对象中的 `code` 字段用于区分业务状态：

```java
// 成功
Result.OK()  // code = 200

// 失败
Result.error("操作失败")  // code = 500

// 自定义状态码
Result.error(4001, "参数错误")  // code = 4001
```

## 八、超时时间配置

### 1. 前端超时配置

| 操作类型 | 超时时间 | 说明 |
|---------|---------|------|
| **查询** | 5000ms | 普通查询 |
| **新增** | 5000ms | 新增操作 |
| **修改** | 5000ms | 修改操作 |
| **删除** | 5000ms | 删除操作 |
| **上传** | 30000ms | 文件上传 |
| **导出** | 30000ms | Excel 导出 |
| **导入** | 60000ms | Excel 导入 |

### 2. Service 超时配置示例

```javascript
/**
 * 普通查询（5秒）
 */
queryList(params) {
  return app.service.request({
    url: '/sys/xxx/list',
    method: 'get',
    params: params,
    timeout: 5000,
  })
}

/**
 * 文件上传（30秒）
 */
upload(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  return app.service.request({
    url: '/sys/xxx/upload',
    method: 'post',
    data: formData,
    headers: {'Content-Type': 'multipart/form-data'},
    timeout: 30000,
  })
}

/**
 * Excel 导入（60秒）
 */
importExcel(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  return app.service.request({
    url: '/sys/xxx/import',
    method: 'post',
    data: formData,
    headers: {'Content-Type': 'multipart/form-data'},
    timeout: 60000,
  })
}
```

## 九、必须遵守的规则

1. **所有接口必须返回 Result 对象**
2. **成功时 success = true，失败时 success = false**
3. **分页参数统一为 pageNo 和 pageSize**
4. **GET 请求使用 params 传参**
5. **POST/PUT 请求使用 data 传参，并设置 Content-Type**
6. **DELETE 请求使用 params 传参**
7. **所有请求必须设置 timeout**
8. **返回数据字段使用驼峰命名**
9. **日期时间统一格式：yyyy-MM-dd HH:mm:ss**
10. **金额字段使用 BigDecimal，返回前端时转为 String**

## 十、常见错误和解决方案

### 1. ❌ 错误：未使用统一返回对象

```java
// ❌ 错误示例
@GetMapping("/list")
public List<XxxVO> queryList(XxxDTO dto) {
    return xxxService.queryList(dto);
}
```

```java
// ✅ 正确示例
@GetMapping("/list")
public Result<?> queryList(XxxDTO dto) {
    List<XxxVO> list = xxxService.queryList(dto);
    return Result.OK(list);
}
```

### 2. ❌ 错误：POST 请求未设置 Content-Type

```javascript
// ❌ 错误示例
add(formData) {
  return app.service.request({
    url: '/sys/xxx/add',
    method: 'post',
    data: formData,
    timeout: 5000,
  })
}
```

```javascript
// ✅ 正确示例
add(formData) {
  return app.service.request({
    url: '/sys/xxx/add',
    method: 'post',
    data: formData,
    headers: {'Content-Type': 'application/json'},
    timeout: 5000,
  })
}
```

### 3. ❌ 错误：未设置超时时间

```javascript
// ❌ 错误示例
queryList(params) {
  return app.service.request({
    url: '/sys/xxx/list',
    method: 'get',
    params: params,
  })
}
```

```javascript
// ✅ 正确示例
queryList(params) {
  return app.service.request({
    url: '/sys/xxx/list',
    method: 'get',
    params: params,
    timeout: 5000,
  })
}
```

### 4. ❌ 错误：未处理响应结果

```javascript
// ❌ 错误示例
async handleAdd() {
  await xxxService.add(this.formData)
  this.$message.success('添加成功')
}
```

```javascript
// ✅ 正确示例
async handleAdd() {
  const result = await xxxService.add(this.formData)
  if (result.success) {
    this.$message.success(result.message || '添加成功')
  } else {
    this.$message.error(result.message || '添加失败')
  }
}
```
