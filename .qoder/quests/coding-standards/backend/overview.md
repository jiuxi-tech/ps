# 后端编码规范概览

## 文档说明

本文档是PS-BMP后端开发的总体规范概览，定义了Controller、Service、Mapper三层架构的编码标准。

---

## 1. 项目架构

### 1.1 技术栈

- **框架**: Spring Boot 2.x
- **ORM**: MyBatis-Plus
- **构建工具**: Maven
- **JDK版本**: Java 8+
- **数据库**: MySQL 5.7+

### 1.2 分层架构

```
src/main/java/com/jiuxi/
├── controller/          # 控制层（接口层）
│   └── admin/          # 管理后台接口
│       └── sys/        # 系统管理模块
├── service/            # 业务逻辑层
│   ├── interface/      # Service接口定义
│   └── impl/           # Service实现类
├── mapper/             # 数据访问层
│   └── admin/          # Mapper接口
├── domain/             # 领域对象
│   ├── entity/         # 实体类（数据库映射）
│   ├── dto/            # 数据传输对象
│   └── vo/             # 视图对象
└── common/             # 通用组件
    ├── result/         # 统一响应
    ├── exception/      # 异常定义
    └── util/           # 工具类
```

---

## 2. Controller层规范

### 2.1 路径前缀规范

⚠️ **重要**: 系统管理类接口必须包含 `/sys` 前缀

```java
@RestController
@RequestMapping("/sys/third-party-app")  // ✅ 正确：包含/sys前缀
public class ThirdPartyAppController {
    // ...
}

@RestController
@RequestMapping("/third-party-app")  // ❌ 错误：缺少/sys前缀
public class ThirdPartyAppController {
    // ...
}
```

### 2.2 请求方式和注解

#### GET请求 - 查询和删除

```java
/**
 * 查询列表
 */
@GetMapping("/list")
public Result list(
    @RequestParam(required = false) String appName,
    @RequestParam(required = false) Integer status,
    @RequestParam(defaultValue = "0") Integer logDelete,
    @RequestParam(defaultValue = "1") Integer page,
    @RequestParam(defaultValue = "10") Integer size
) {
    // 处理逻辑
    return Result.success("查询成功", data);
}

/**
 * 查询详情
 */
@GetMapping("/view")
public Result view(
    @RequestParam String appId,
    @RequestParam String passKey
) {
    // 处理逻辑
    return Result.success("查询成功", data);
}

/**
 * 删除
 */
@GetMapping("/delete")
public Result delete(
    @RequestParam String appId,
    @RequestParam String passKey
) {
    // 校验passKey
    // 逻辑删除
    return Result.success("删除成功");
}
```

#### POST请求 - 表单格式

**前端Content-Type**: `application/x-www-form-urlencoded`

⚠️ **注意**: **不要**使用`@RequestBody`注解

```java
/**
 * 新增（表单格式）
 */
@PostMapping("/add")
public Result add(ThirdPartyAppDTO dto) {  // ✅ 直接对象绑定
    // 参数校验
    if (StringUtils.isBlank(dto.getAppName())) {
        return Result.error(400, "应用名称不能为空");
    }
    
    // 业务处理
    service.add(dto);
    
    return Result.success("新增成功");
}

/**
 * 修改（表单格式）
 */
@PostMapping("/update")
public Result update(ThirdPartyAppDTO dto) {  // ✅ 直接对象绑定
    // 校验passKey
    if (!service.validatePassKey(dto.getAppId(), dto.getPassKey())) {
        return Result.error(403, "数据校验失败");
    }
    
    // 更新操作
    service.update(dto);
    
    return Result.success("修改成功");
}
```

❌ **常见错误**:
```java
@PostMapping("/add")
public Result add(@RequestBody ThirdPartyAppDTO dto) {  // ❌ 表单请求不能用@RequestBody
    // 会导致参数绑定失败
}
```

#### POST请求 - JSON格式

**前端Content-Type**: `application/json`

✅ **必须**使用`@RequestBody`注解

```java
/**
 * 配置权限（JSON格式）
 */
@PostMapping("/config-permissions")
public Result configPermissions(@RequestBody ApiPermissionDTO dto) {  // ✅ 必须使用@RequestBody
    // dto.getAppId()
    // dto.getApiIds() -> List<String>
    
    service.configPermissions(dto);
    
    return Result.success("配置成功");
}
```

#### POST请求 - 文件上传

```java
/**
 * 导入Excel
 */
@PostMapping("/import-excel")
public Result importExcel(
    @RequestParam("file") MultipartFile file,  // 文件参数
    @RequestParam String deptId,               // 普通参数
    @RequestParam String ascnId
) {
    try {
        ImportResult result = service.importExcel(file, deptId, ascnId);
        return Result.success("导入成功", result);
    } catch (Exception e) {
        return Result.error("导入失败: " + e.getMessage());
    }
}
```

### 2.3 统一响应格式

```java
/**
 * 统一响应结果
 */
public class Result<T> {
    private Integer code;    // 状态码：1成功，其他失败
    private String message;  // 响应消息
    private T data;         // 业务数据
    
    // 成功响应
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(1, message, data);
    }
    
    public static Result success(String message) {
        return new Result<>(1, message, null);
    }
    
    // 失败响应
    public static Result error(String message) {
        return new Result<>(500, message, null);
    }
    
    public static Result error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
```

---

## 3. Service层规范

### 3.1 接口定义

```java
/**
 * 第三方应用Service接口
 */
public interface ThirdPartyAppService {
    
    /**
     * 分页查询列表
     */
    PageInfo<ThirdPartyApp> list(String appName, Integer status, Integer logDelete, Integer page, Integer size);
    
    /**
     * 根据ID查询详情
     */
    ThirdPartyApp getById(String appId, String passKey);
    
    /**
     * 新增
     */
    void add(ThirdPartyAppDTO dto);
    
    /**
     * 修改
     */
    void update(ThirdPartyAppDTO dto);
    
    /**
     * 删除（逻辑删除）
     */
    void delete(String appId, String passKey);
    
    /**
     * 校验passKey
     */
    boolean validatePassKey(String appId, String passKey);
}
```

### 3.2 实现类

```java
/**
 * 第三方应用Service实现类
 */
@Service
public class ThirdPartyAppServiceImpl implements ThirdPartyAppService {
    
    @Autowired
    private ThirdPartyAppMapper mapper;
    
    @Override
    public PageInfo<ThirdPartyApp> list(String appName, Integer status, Integer logDelete, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<ThirdPartyApp> list = mapper.selectList(appName, status, logDelete);
        return new PageInfo<>(list);
    }
    
    @Override
    public ThirdPartyApp getById(String appId, String passKey) {
        // 校验passKey
        if (!validatePassKey(appId, passKey)) {
            throw new BusinessException("数据校验失败");
        }
        
        return mapper.selectById(appId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ThirdPartyAppDTO dto) {
        // 参数校验
        if (StringUtils.isBlank(dto.getAppName())) {
            throw new BusinessException("应用名称不能为空");
        }
        
        // 业务逻辑
        ThirdPartyApp entity = new ThirdPartyApp();
        BeanUtils.copyProperties(dto, entity);
        
        // 生成主键和密钥
        entity.setAppId(IdUtil.simpleUUID());
        entity.setApiKey(generateApiKey());
        entity.setPassKey(IdUtil.simpleUUID());
        entity.setLogDelete(0);
        
        // 插入数据
        mapper.insert(entity);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ThirdPartyAppDTO dto) {
        // 校验passKey
        if (!validatePassKey(dto.getAppId(), dto.getPassKey())) {
            throw new BusinessException("数据校验失败");
        }
        
        // 更新数据
        ThirdPartyApp entity = new ThirdPartyApp();
        BeanUtils.copyProperties(dto, entity);
        mapper.updateById(entity);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String appId, String passKey) {
        // 校验passKey
        if (!validatePassKey(appId, passKey)) {
            throw new BusinessException("数据校验失败");
        }
        
        // 逻辑删除
        ThirdPartyApp entity = new ThirdPartyApp();
        entity.setAppId(appId);
        entity.setLogDelete(1);
        mapper.updateById(entity);
    }
    
    @Override
    public boolean validatePassKey(String appId, String passKey) {
        ThirdPartyApp entity = mapper.selectById(appId);
        return entity != null && entity.getPassKey().equals(passKey);
    }
    
    private String generateApiKey() {
        return "ak_" + IdUtil.simpleUUID();
    }
}
```

---

## 4. Mapper层规范

### 4.1 Mapper接口

```java
/**
 * 第三方应用Mapper接口
 */
@Mapper
public interface ThirdPartyAppMapper extends BaseMapper<ThirdPartyApp> {
    
    /**
     * 查询列表
     */
    List<ThirdPartyApp> selectList(
        @Param("appName") String appName,
        @Param("status") Integer status,
        @Param("logDelete") Integer logDelete
    );
    
    /**
     * 根据ID查询
     */
    ThirdPartyApp selectById(@Param("appId") String appId);
    
    /**
     * 插入
     */
    int insert(ThirdPartyApp entity);
    
    /**
     * 更新
     */
    int updateById(ThirdPartyApp entity);
}
```

### 4.2 Mapper XML

**文件位置**: `src/main/resources/mapper/admin/ThirdPartyAppMapper.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.jiuxi.mapper.admin.ThirdPartyAppMapper">
    
    <!-- 结果映射 -->
    <resultMap id="BaseResultMap" type="com.jiuxi.domain.entity.ThirdPartyApp">
        <id column="app_id" property="appId"/>
        <result column="app_name" property="appName"/>
        <result column="api_key" property="apiKey"/>
        <result column="status" property="status"/>
        <result column="expire_time" property="expireTime"/>
        <result column="description" property="description"/>
        <result column="pass_key" property="passKey"/>
        <result column="log_delete" property="logDelete"/>
        <result column="create_time" property="createTime"/>
        <result column="create_person_id" property="createPersonId"/>
    </resultMap>
    
    <!-- 查询列表 -->
    <select id="selectList" resultMap="BaseResultMap">
        SELECT 
            app_id, app_name, api_key, status, expire_time, 
            description, pass_key, log_delete, create_time, create_person_id
        FROM tp_third_party_app
        WHERE 1=1
        <if test="appName != null and appName != ''">
            AND app_name LIKE CONCAT('%', #{appName}, '%')
        </if>
        <if test="status != null">
            AND status = #{status}
        </if>
        <if test="logDelete != null">
            AND log_delete = #{logDelete}
        </if>
        ORDER BY create_time DESC
    </select>
    
    <!-- 根据ID查询 -->
    <select id="selectById" resultMap="BaseResultMap">
        SELECT 
            app_id, app_name, api_key, status, expire_time, 
            description, pass_key, log_delete, create_time, create_person_id
        FROM tp_third_party_app
        WHERE app_id = #{appId}
    </select>
    
    <!-- 插入 -->
    <insert id="insert">
        INSERT INTO tp_third_party_app (
            app_id, app_name, api_key, status, expire_time, 
            description, pass_key, log_delete, create_time, create_person_id
        ) VALUES (
            #{appId}, #{appName}, #{apiKey}, #{status}, #{expireTime}, 
            #{description}, #{passKey}, #{logDelete}, #{createTime}, #{createPersonId}
        )
    </insert>
    
    <!-- 更新 -->
    <update id="updateById">
        UPDATE tp_third_party_app
        <set>
            <if test="appName != null">app_name = #{appName},</if>
            <if test="status != null">status = #{status},</if>
            <if test="expireTime != null">expire_time = #{expireTime},</if>
            <if test="description != null">description = #{description},</if>
            <if test="logDelete != null">log_delete = #{logDelete},</if>
        </set>
        WHERE app_id = #{appId}
    </update>
    
</mapper>
```

---

## 5. 实体和DTO规范

### 5.1 实体类（Entity）

```java
/**
 * 第三方应用实体
 * 对应数据库表：tp_third_party_app
 */
@Data
@TableName("tp_third_party_app")
public class ThirdPartyApp {
    
    /**
     * 应用ID（主键）
     */
    @TableId(value = "app_id", type = IdType.INPUT)
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
     * API秘钥
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
     * 逻辑删除标记（0未删除 1已删除）
     */
    private Integer logDelete;
    
    /**
     * 创建时间（YYYYMMDDHHmmss）
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    
    /**
     * 创建人ID
     */
    private String createPersonId;
    
    /**
     * 更新时间（YYYYMMDDHHmmss）
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateTime;
    
    /**
     * 更新人ID
     */
    private String updatePersonId;
}
```

### 5.2 DTO（数据传输对象）

```java
/**
 * 第三方应用DTO
 * 用于前后端数据传输
 */
@Data
public class ThirdPartyAppDTO {
    
    /**
     * 应用ID（修改时必填）
     */
    private String appId;
    
    /**
     * 应用名称（必填）
     */
    @NotBlank(message = "应用名称不能为空")
    private String appName;
    
    /**
     * 状态（1启用 0禁用）
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
    
    /**
     * 过期时间（可选）
     */
    private String expireTime;
    
    /**
     * 应用描述
     */
    private String description;
    
    /**
     * 数据校验密钥（修改/删除时必填）
     */
    private String passKey;
}
```

---

## 6. 异常处理

### 6.1 自定义业务异常

```java
/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {
    
    private Integer code;
    
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    
    public Integer getCode() {
        return code;
    }
}
```

### 6.2 全局异常处理

```java
/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = bindingResult.getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining("; "));
        return Result.error(400, "参数校验失败：" + message);
    }
    
    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("系统异常，请联系管理员");
    }
}
```

---

## 7. 开发检查清单

### 7.1 Controller层
- [ ] 路径包含正确前缀（/sys）
- [ ] 选择正确的请求方式（GET/POST）
- [ ] 表单请求不使用@RequestBody
- [ ] JSON请求使用@RequestBody
- [ ] 返回统一Result格式
- [ ] 添加参数校验
- [ ] 捕获和处理异常

### 7.2 Service层
- [ ] 定义Service接口
- [ ] 实现ServiceImpl
- [ ] 添加事务注解（@Transactional）
- [ ] 校验passKey（修改/删除操作）
- [ ] 过滤logDelete（查询操作）
- [ ] 抛出业务异常而非返回null

### 7.3 Mapper层
- [ ] Mapper接口继承BaseMapper
- [ ] XML文件存放在mapper/admin目录
- [ ] 定义resultMap映射
- [ ] 使用参数化查询防止SQL注入
- [ ] 模糊查询使用CONCAT函数

### 7.4 实体类
- [ ] 字段使用camelCase命名
- [ ] 添加字段注释
- [ ] 时间字段使用String类型
- [ ] 配置自动填充（createTime/updateTime）
- [ ] 主键使用@TableId注解

---

## 8. 相关文档

- **[Controller层详细规范](controller-layer.md)**
- **[Service层详细规范](service-layer.md)**
- **[Mapper层详细规范](mapper-layer.md)**
- **[DTO/VO设计规范](dto-vo-standards.md)**
- **[异常处理规范](exception-handling.md)**

---

## 版本信息

- **版本号**: v1.0.0
- **创建日期**: 2025年12月1日
- **最后更新**: 2025年12月1日

---

**重要提醒**: 后端开发必须严格遵循三层架构规范，确保代码结构清晰、职责分明！
