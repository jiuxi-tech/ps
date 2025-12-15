---
trigger: model_decision
description: 后端 Service 层开发规范，包括业务逻辑处理、事务管理、异常处理等
---

# 后端 Service 层开发规范

## 一、Service 基本结构

### 1. 接口定义

```java
/**
 * XXX Service 接口
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
public interface IXxxService extends IService<XxxEntity> {
    
    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param dto 查询条件
     * @return 分页结果
     */
    IPage<XxxVO> queryPage(Page<XxxVO> page, XxxDTO dto);
    
    /**
     * 查询列表
     *
     * @param dto 查询条件
     * @return 列表数据
     */
    List<XxxVO> queryList(XxxDTO dto);
    
    /**
     * 根据ID查询详情
     *
     * @param id 主键ID
     * @return 详情数据
     */
    XxxVO queryById(String id);
    
    /**
     * 新增
     *
     * @param dto 新增参数
     */
    void add(XxxDTO dto);
    
    /**
     * 修改
     *
     * @param dto 修改参数
     */
    void edit(XxxDTO dto);
    
}
```

### 2. 实现类定义

```java
/**
 * XXX Service 实现类
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@Service
public class XxxServiceImpl extends ServiceImpl<XxxMapper, XxxEntity> implements IXxxService {
    
    @Autowired
    private XxxMapper xxxMapper;
    
}
```

### 3. 必须遵守的规则

1. **必须继承 IService**：接口必须继承 `IService<Entity>`
2. **必须继承 ServiceImpl**：实现类必须继承 `ServiceImpl<Mapper, Entity>`
3. **必须使用 @Service 注解**：标识为 Spring 服务组件
4. **必须注入 Mapper**：使用 `@Autowired` 注入对应的 Mapper
5. **禁止直接操作数据库**：不允许在 Service 中编写 SQL，必须通过 Mapper
6. **必须处理业务逻辑**：Service 层是业务逻辑的核心

## 二、分页查询实现

### 1. 标准分页查询

```java
@Override
public IPage<XxxVO> queryPage(Page<XxxVO> page, XxxDTO dto) {
    return xxxMapper.queryPage(page, dto);
}
```

### 2. Mapper 中的分页查询

```java
/**
 * 分页查询
 *
 * @param page 分页对象
 * @param dto 查询条件
 * @return 分页结果
 */
IPage<XxxVO> queryPage(Page<XxxVO> page, @Param("dto") XxxDTO dto);
```

### 3. XML 中的分页查询

```xml
<select id="queryPage" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name,
        t.type,
        t.create_time AS createTime
    FROM xxx_table t
    <where>
        <if test="dto.name != null and dto.name != ''">
            AND t.name LIKE CONCAT('%', #{dto.name}, '%')
        </if>
        <if test="dto.type != null">
            AND t.type = #{dto.type}
        </if>
    </where>
    ORDER BY t.create_time DESC
</select>
```

## 三、列表查询实现

```java
@Override
public List<XxxVO> queryList(XxxDTO dto) {
    return xxxMapper.queryList(dto);
}
```

## 四、详情查询实现

```java
@Override
public XxxVO queryById(String id) {
    if (StringUtils.isBlank(id)) {
        throw new BusinessException("ID不能为空");
    }
    return xxxMapper.queryById(id);
}
```

## 五、新增操作实现

### 1. 基本新增

```java
@Override
public void add(XxxDTO dto) {
    // 1. 业务校验
    validateForAdd(dto);
    
    // 2. 构建实体对象
    XxxEntity entity = new XxxEntity();
    BeanUtils.copyProperties(dto, entity);
    
    // 3. 设置默认值
    entity.setId(UUID.randomUUID().toString().replace("-", ""));
    entity.setCreateTime(new Date());
    entity.setCreateBy(getCurrentUsername());
    
    // 4. 保存到数据库
    this.save(entity);
}

/**
 * 新增业务校验
 */
private void validateForAdd(XxxDTO dto) {
    // 校验名称是否重复
    XxxEntity exist = this.getOne(
        new LambdaQueryWrapper<XxxEntity>()
            .eq(XxxEntity::getName, dto.getName())
    );
    if (exist != null) {
        throw new BusinessException("名称已存在");
    }
}
```

### 2. 带事务的新增

```java
@Override
@Transactional(rollbackFor = Exception.class)
public void add(XxxDTO dto) {
    // 1. 保存主表
    XxxEntity entity = new XxxEntity();
    BeanUtils.copyProperties(dto, entity);
    entity.setId(UUID.randomUUID().toString().replace("-", ""));
    entity.setCreateTime(new Date());
    this.save(entity);
    
    // 2. 保存子表
    if (CollectionUtils.isNotEmpty(dto.getDetailList())) {
        List<XxxDetailEntity> detailList = dto.getDetailList().stream()
            .map(detail -> {
                XxxDetailEntity detailEntity = new XxxDetailEntity();
                BeanUtils.copyProperties(detail, detailEntity);
                detailEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                detailEntity.setMainId(entity.getId());
                return detailEntity;
            })
            .collect(Collectors.toList());
        xxxDetailService.saveBatch(detailList);
    }
}
```

## 六、修改操作实现

### 1. 基本修改

```java
@Override
public void edit(XxxDTO dto) {
    // 1. 校验ID
    if (StringUtils.isBlank(dto.getId())) {
        throw new BusinessException("ID不能为空");
    }
    
    // 2. 业务校验
    validateForEdit(dto);
    
    // 3. 查询原记录
    XxxEntity entity = this.getById(dto.getId());
    if (entity == null) {
        throw new BusinessException("记录不存在");
    }
    
    // 4. 更新字段
    BeanUtils.copyProperties(dto, entity);
    entity.setUpdateTime(new Date());
    entity.setUpdateBy(getCurrentUsername());
    
    // 5. 更新到数据库
    this.updateById(entity);
}

/**
 * 修改业务校验
 */
private void validateForEdit(XxxDTO dto) {
    // 校验名称是否重复（排除自己）
    XxxEntity exist = this.getOne(
        new LambdaQueryWrapper<XxxEntity>()
            .eq(XxxEntity::getName, dto.getName())
            .ne(XxxEntity::getId, dto.getId())
    );
    if (exist != null) {
        throw new BusinessException("名称已存在");
    }
}
```

### 2. 带事务的修改

```java
@Override
@Transactional(rollbackFor = Exception.class)
public void edit(XxxDTO dto) {
    // 1. 更新主表
    XxxEntity entity = this.getById(dto.getId());
    if (entity == null) {
        throw new BusinessException("记录不存在");
    }
    BeanUtils.copyProperties(dto, entity);
    entity.setUpdateTime(new Date());
    this.updateById(entity);
    
    // 2. 删除原有子表数据
    xxxDetailService.remove(
        new LambdaQueryWrapper<XxxDetailEntity>()
            .eq(XxxDetailEntity::getMainId, entity.getId())
    );
    
    // 3. 保存新的子表数据
    if (CollectionUtils.isNotEmpty(dto.getDetailList())) {
        List<XxxDetailEntity> detailList = dto.getDetailList().stream()
            .map(detail -> {
                XxxDetailEntity detailEntity = new XxxDetailEntity();
                BeanUtils.copyProperties(detail, detailEntity);
                detailEntity.setId(UUID.randomUUID().toString().replace("-", ""));
                detailEntity.setMainId(entity.getId());
                return detailEntity;
            })
            .collect(Collectors.toList());
        xxxDetailService.saveBatch(detailList);
    }
}
```

## 七、删除操作实现

### 1. 单个删除

```java
@Override
public void deleteById(String id) {
    if (StringUtils.isBlank(id)) {
        throw new BusinessException("ID不能为空");
    }
    
    // 业务校验
    validateForDelete(id);
    
    // 执行删除
    this.removeById(id);
}

/**
 * 删除业务校验
 */
private void validateForDelete(String id) {
    // 校验是否被引用
    Long count = xxxDetailService.count(
        new LambdaQueryWrapper<XxxDetailEntity>()
            .eq(XxxDetailEntity::getMainId, id)
    );
    if (count > 0) {
        throw new BusinessException("该记录已被引用，无法删除");
    }
}
```

### 2. 批量删除

```java
@Override
public void deleteBatch(List<String> idList) {
    if (CollectionUtils.isEmpty(idList)) {
        throw new BusinessException("ID列表不能为空");
    }
    
    // 批量校验
    for (String id : idList) {
        validateForDelete(id);
    }
    
    // 批量删除
    this.removeByIds(idList);
}
```

### 3. 级联删除

```java
@Override
@Transactional(rollbackFor = Exception.class)
public void deleteById(String id) {
    if (StringUtils.isBlank(id)) {
        throw new BusinessException("ID不能为空");
    }
    
    // 1. 删除子表数据
    xxxDetailService.remove(
        new LambdaQueryWrapper<XxxDetailEntity>()
            .eq(XxxDetailEntity::getMainId, id)
    );
    
    // 2. 删除主表数据
    this.removeById(id);
}
```

## 八、事务管理

### 1. @Transactional 注解使用

```java
@Override
@Transactional(rollbackFor = Exception.class)
public void add(XxxDTO dto) {
    // 事务方法内的所有数据库操作要么全部成功，要么全部回滚
}
```

### 2. 事务注解参数

- `rollbackFor`：指定回滚的异常类，建议使用 `Exception.class`
- `propagation`：事务传播行为，默认 `REQUIRED`
- `isolation`：事务隔离级别，默认数据库默认级别
- `timeout`：超时时间，单位秒

### 3. 事务使用规范

1. **必须指定 rollbackFor**：`@Transactional(rollbackFor = Exception.class)`
2. **方法必须是 public**：Spring 事务只对 public 方法生效
3. **避免自调用**：同一个类内部调用事务方法，事务不生效
4. **异常不能被捕获**：事务方法中的异常必须抛出，不能被 try-catch 吞掉

## 九、异常处理

### 1. 业务异常

```java
@Override
public void add(XxxDTO dto) {
    // 业务校验失败，抛出业务异常
    if (StringUtils.isBlank(dto.getName())) {
        throw new BusinessException("名称不能为空");
    }
    
    XxxEntity exist = this.getOne(
        new LambdaQueryWrapper<XxxEntity>()
            .eq(XxxEntity::getName, dto.getName())
    );
    if (exist != null) {
        throw new BusinessException("名称已存在");
    }
    
    // 正常业务逻辑
    XxxEntity entity = new XxxEntity();
    BeanUtils.copyProperties(dto, entity);
    this.save(entity);
}
```

### 2. 异常传播

Service 层抛出的异常会被全局异常处理器捕获：
- `BusinessException`：业务异常，返回友好提示
- `RuntimeException`：系统异常，返回系统错误提示

## 十、工具方法

### 1. 获取当前用户

```java
/**
 * 获取当前登录用户名
 */
private String getCurrentUsername() {
    LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
    return loginUser.getUsername();
}
```

### 2. 对象拷贝

```java
// 使用 Spring 的 BeanUtils
XxxEntity entity = new XxxEntity();
BeanUtils.copyProperties(dto, entity);
```

### 3. 集合判空

```java
// 使用 Apache Commons Collections
if (CollectionUtils.isNotEmpty(list)) {
    // 处理非空集合
}
```

### 4. 字符串判空

```java
// 使用 Apache Commons Lang
if (StringUtils.isNotBlank(str)) {
    // 处理非空字符串
}
```

## 十一、常见错误和解决方案

### 1. ❌ 错误：在 Service 中编写 SQL

```java
// ❌ 错误示例
@Override
public List<XxxVO> queryList(XxxDTO dto) {
    String sql = "SELECT * FROM xxx_table WHERE name = '" + dto.getName() + "'";
    // 执行 SQL...
}
```

```java
// ✅ 正确示例
@Override
public List<XxxVO> queryList(XxxDTO dto) {
    return xxxMapper.queryList(dto);
}
```

### 2. ❌ 错误：未使用事务

```java
// ❌ 错误示例
@Override
public void add(XxxDTO dto) {
    // 保存主表
    this.save(mainEntity);
    
    // 保存子表
    xxxDetailService.saveBatch(detailList);
}
```

```java
// ✅ 正确示例
@Override
@Transactional(rollbackFor = Exception.class)
public void add(XxxDTO dto) {
    // 保存主表
    this.save(mainEntity);
    
    // 保存子表
    xxxDetailService.saveBatch(detailList);
}
```

### 3. ❌ 错误：捕获异常不抛出

```java
// ❌ 错误示例
@Override
@Transactional(rollbackFor = Exception.class)
public void add(XxxDTO dto) {
    try {
        this.save(entity);
        xxxDetailService.saveBatch(detailList);
    } catch (Exception e) {
        e.printStackTrace();  // 事务不会回滚
    }
}
```

```java
// ✅ 正确示例
@Override
@Transactional(rollbackFor = Exception.class)
public void add(XxxDTO dto) {
    this.save(entity);
    xxxDetailService.saveBatch(detailList);
    // 异常自动抛出，事务会回滚
}
```

### 4. ❌ 错误：未设置必要字段

```java
// ❌ 错误示例
@Override
public void add(XxxDTO dto) {
    XxxEntity entity = new XxxEntity();
    BeanUtils.copyProperties(dto, entity);
    this.save(entity);  // 缺少 ID、创建时间等
}
```

```java
// ✅ 正确示例
@Override
public void add(XxxDTO dto) {
    XxxEntity entity = new XxxEntity();
    BeanUtils.copyProperties(dto, entity);
    entity.setId(UUID.randomUUID().toString().replace("-", ""));
    entity.setCreateTime(new Date());
    entity.setCreateBy(getCurrentUsername());
    this.save(entity);
}
```

## 十二、注释规范

### 1. 接口方法注释

```java
/**
 * 分页查询XXX列表
 *
 * @param page 分页对象
 * @param dto 查询条件
 * @return 分页结果
 */
IPage<XxxVO> queryPage(Page<XxxVO> page, XxxDTO dto);
```

### 2. 实现方法注释

```java
@Override
public IPage<XxxVO> queryPage(Page<XxxVO> page, XxxDTO dto) {
    return xxxMapper.queryPage(page, dto);
}
// 实现方法通常不需要重复注释，除非有特殊说明
```

### 3. 私有方法注释

```java
/**
 * 新增业务校验
 */
private void validateForAdd(XxxDTO dto) {
    // ...
}
```
