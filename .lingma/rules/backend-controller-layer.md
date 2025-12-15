---
trigger: model_decision
description: 后端 Controller 层开发规范，包括统一返回封装、参数校验、异常处理等
---

# 后端 Controller 层开发规范

## 一、Controller 基本结构

### 1. 类定义规范

```java
/**
 * XXX管理 Controller
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@RestController
@RequestMapping("/sys/xxx")
public class XxxController {
    
    @Autowired
    private IXxxService xxxService;
    
}
```

### 2. 必须遵守的规则

1. **必须使用 @RestController**：除非需要返回视图，否则统一使用 `@RestController`
2. **必须定义 @RequestMapping**：类级别必须定义统一的请求路径前缀
3. **请求路径规范**：
   - 列表查询：`/list`
   - 分页查询：`/queryPage`
   - 详情查询：`/queryById`
   - 新增：`/add`
   - 修改：`/edit`
   - 删除：`/delete`
   - 批量删除：`/deleteBatch`
4. **必须使用统一返回对象**：所有接口返回值必须包装在 `Result` 对象中
5. **必须进行参数校验**：使用 `@Valid` 或手动校验

## 二、统一返回封装

### 1. Result 对象使用

```java
/**
 * 查询列表
 */
@GetMapping("/list")
public Result<?> queryList(XxxDTO dto) {
    List<XxxVO> list = xxxService.queryList(dto);
    return Result.OK(list);
}

/**
 * 分页查询
 */
@GetMapping("/queryPage")
public Result<?> queryPage(XxxDTO dto, @RequestParam(defaultValue = "1") Integer pageNo,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
    Page<XxxVO> page = new Page<>(pageNo, pageSize);
    IPage<XxxVO> pageList = xxxService.queryPage(page, dto);
    return Result.OK(pageList);
}

/**
 * 新增
 */
@PostMapping("/add")
public Result<?> add(@Valid @RequestBody XxxDTO dto) {
    xxxService.add(dto);
    return Result.OK("添加成功");
}

/**
 * 修改
 */
@PutMapping("/edit")
public Result<?> edit(@Valid @RequestBody XxxDTO dto) {
    xxxService.edit(dto);
    return Result.OK("修改成功");
}

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

### 2. Result 对象方法

| 方法 | 说明 | 使用场景 |
|------|------|----------|
| `Result.OK()` | 成功，无返回数据 | 删除、修改等操作 |
| `Result.OK(data)` | 成功，有返回数据 | 查询、新增返回ID等 |
| `Result.OK(message, data)` | 成功，自定义消息和数据 | 特殊提示场景 |
| `Result.error(message)` | 失败，返回错误消息 | 业务校验失败 |
| `Result.error(code, message)` | 失败，返回错误码和消息 | 需要前端特殊处理的错误 |

## 三、参数校验

### 1. 使用 @Valid 注解

```java
/**
 * 新增
 *
 * @param dto 新增参数
 */
@PostMapping("/add")
public Result<?> add(@Valid @RequestBody XxxDTO dto) {
    xxxService.add(dto);
    return Result.OK("添加成功");
}
```

### 2. DTO 中的校验注解

```java
public class XxxDTO {
    
    @NotBlank(message = "名称不能为空")
    private String name;
    
    @NotNull(message = "类型不能为空")
    private Integer type;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
}
```

### 3. 手动参数校验

```java
@GetMapping("/queryById")
public Result<?> queryById(@RequestParam String id) {
    if (StringUtils.isBlank(id)) {
        return Result.error("ID不能为空");
    }
    XxxVO vo = xxxService.queryById(id);
    return Result.OK(vo);
}
```

## 四、异常处理

### 1. 业务异常抛出

```java
@PostMapping("/add")
public Result<?> add(@Valid @RequestBody XxxDTO dto) {
    // 业务校验
    XxxEntity exist = xxxService.getOne(
        new LambdaQueryWrapper<XxxEntity>()
            .eq(XxxEntity::getName, dto.getName())
    );
    if (exist != null) {
        throw new BusinessException("名称已存在");
    }
    
    xxxService.add(dto);
    return Result.OK("添加成功");
}
```

### 2. 系统异常由全局异常处理器统一处理

Controller 层不需要捕获异常，由全局异常处理器统一处理：
- `BusinessException`：业务异常，返回友好提示
- `RuntimeException`：系统异常，返回系统错误提示

## 五、请求方法规范

| HTTP 方法 | 使用场景 | 参数接收方式 |
|-----------|----------|-------------|
| **GET** | 查询操作 | `@RequestParam` 或对象接收 |
| **POST** | 新增操作 | `@RequestBody` 接收 JSON |
| **PUT** | 修改操作 | `@RequestBody` 接收 JSON |
| **DELETE** | 删除操作 | `@RequestParam` 接收 |

### 示例

```java
// GET 请求 - 对象接收参数
@GetMapping("/list")
public Result<?> queryList(XxxDTO dto) {
    // dto 会自动绑定 URL 参数
}

// GET 请求 - @RequestParam 接收
@GetMapping("/queryById")
public Result<?> queryById(@RequestParam String id) {
    // 接收单个参数
}

// POST 请求 - @RequestBody 接收 JSON
@PostMapping("/add")
public Result<?> add(@RequestBody XxxDTO dto) {
    // 接收 JSON 格式的请求体
}

// DELETE 请求 - @RequestParam 接收
@DeleteMapping("/delete")
public Result<?> delete(@RequestParam String id) {
    // 接收单个参数
}
```

## 六、分页查询规范

### 1. 标准分页查询

```java
/**
 * 分页查询
 *
 * @param dto 查询条件
 * @param pageNo 页码，默认第1页
 * @param pageSize 每页条数，默认10条
 */
@GetMapping("/queryPage")
public Result<?> queryPage(XxxDTO dto,
                           @RequestParam(defaultValue = "1") Integer pageNo,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
    Page<XxxVO> page = new Page<>(pageNo, pageSize);
    IPage<XxxVO> pageList = xxxService.queryPage(page, dto);
    return Result.OK(pageList);
}
```

### 2. 分页参数说明

- `pageNo`：页码，从1开始，默认值为1
- `pageSize`：每页条数，默认值为10
- 返回对象：`IPage<T>`，包含 `records`（数据列表）、`total`（总条数）、`pages`（总页数）等

## 七、文件上传下载

### 1. 文件上传

```java
/**
 * 文件上传
 *
 * @param file 上传的文件
 */
@PostMapping("/upload")
public Result<?> upload(@RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
        return Result.error("文件不能为空");
    }
    
    String fileUrl = xxxService.uploadFile(file);
    return Result.OK("上传成功", fileUrl);
}
```

### 2. 文件下载

```java
/**
 * 文件下载
 *
 * @param filePath 文件路径
 * @param response 响应对象
 */
@GetMapping("/download")
public void download(@RequestParam String filePath, HttpServletResponse response) {
    xxxService.downloadFile(filePath, response);
}
```

## 八、导出 Excel

```java
/**
 * 导出 Excel
 *
 * @param dto 查询条件
 * @param response 响应对象
 */
@GetMapping("/export")
public void export(XxxDTO dto, HttpServletResponse response) {
    List<XxxVO> list = xxxService.queryList(dto);
    xxxService.exportExcel(list, response);
}
```

## 九、常见错误和解决方案

### 1. ❌ 错误：没有使用统一返回对象

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

### 2. ❌ 错误：POST 请求未使用 @RequestBody

```java
// ❌ 错误示例
@PostMapping("/add")
public Result<?> add(XxxDTO dto) {
    xxxService.add(dto);
    return Result.OK("添加成功");
}
```

```java
// ✅ 正确示例
@PostMapping("/add")
public Result<?> add(@RequestBody XxxDTO dto) {
    xxxService.add(dto);
    return Result.OK("添加成功");
}
```

### 3. ❌ 错误：在 Controller 中处理业务逻辑

```java
// ❌ 错误示例
@PostMapping("/add")
public Result<?> add(@RequestBody XxxDTO dto) {
    XxxEntity entity = new XxxEntity();
    BeanUtils.copyProperties(dto, entity);
    entity.setId(UUID.randomUUID().toString());
    entity.setCreateTime(new Date());
    xxxMapper.insert(entity);
    return Result.OK("添加成功");
}
```

```java
// ✅ 正确示例
@PostMapping("/add")
public Result<?> add(@RequestBody XxxDTO dto) {
    xxxService.add(dto);
    return Result.OK("添加成功");
}
```

### 4. ❌ 错误：手动捕获所有异常

```java
// ❌ 错误示例
@PostMapping("/add")
public Result<?> add(@RequestBody XxxDTO dto) {
    try {
        xxxService.add(dto);
        return Result.OK("添加成功");
    } catch (Exception e) {
        return Result.error("添加失败：" + e.getMessage());
    }
}
```

```java
// ✅ 正确示例
@PostMapping("/add")
public Result<?> add(@RequestBody XxxDTO dto) {
    xxxService.add(dto);
    return Result.OK("添加成功");
}
// 异常由全局异常处理器统一处理
```

## 十、注释规范

### 1. 类注释

```java
/**
 * XXX管理 Controller
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
```

### 2. 方法注释

```java
/**
 * 分页查询XXX列表
 *
 * @param dto 查询条件
 * @param pageNo 页码
 * @param pageSize 每页条数
 * @return 分页结果
 */
@GetMapping("/queryPage")
public Result<?> queryPage(XxxDTO dto,
                           @RequestParam(defaultValue = "1") Integer pageNo,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
    // ...
}
```
