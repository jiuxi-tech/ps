---
trigger: model_decision
description: 后端 Mapper 层开发规范，包括 MyBatis-Plus 使用、XML 编写、SQL 优化等
---

# 后端 Mapper 层开发规范

## 一、Mapper 基本结构

### 1. Mapper 接口定义

```java
/**
 * XXX Mapper 接口
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@Mapper
public interface XxxMapper extends BaseMapper<XxxEntity> {
    
    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param dto 查询条件
     * @return 分页结果
     */
    IPage<XxxVO> queryPage(Page<XxxVO> page, @Param("dto") XxxDTO dto);
    
    /**
     * 查询列表
     *
     * @param dto 查询条件
     * @return 列表数据
     */
    List<XxxVO> queryList(@Param("dto") XxxDTO dto);
    
    /**
     * 根据ID查询详情
     *
     * @param id 主键ID
     * @return 详情数据
     */
    XxxVO queryById(@Param("id") String id);
    
}
```

### 2. 必须遵守的规则

1. **必须继承 BaseMapper**：`extends BaseMapper<Entity>`
2. **必须使用 @Mapper 注解**：标识为 MyBatis Mapper
3. **必须使用 @Param 注解**：方法参数超过1个或为对象时必须使用
4. **返回值使用 VO**：自定义查询方法返回 VO 对象，不返回 Entity
5. **XML 文件位置**：与 Mapper 接口同包或在 `resources/mapper` 目录下

## 二、MyBatis-Plus BaseMapper 方法

### 1. 常用查询方法

```java
// 根据ID查询
XxxEntity entity = xxxMapper.selectById(id);

// 根据条件查询单条记录
XxxEntity entity = xxxMapper.selectOne(
    new LambdaQueryWrapper<XxxEntity>()
        .eq(XxxEntity::getName, "xxx")
);

// 根据条件查询列表
List<XxxEntity> list = xxxMapper.selectList(
    new LambdaQueryWrapper<XxxEntity>()
        .eq(XxxEntity::getType, 1)
        .orderByDesc(XxxEntity::getCreateTime)
);

// 根据条件查询数量
Long count = xxxMapper.selectCount(
    new LambdaQueryWrapper<XxxEntity>()
        .eq(XxxEntity::getType, 1)
);

// 分页查询
Page<XxxEntity> page = new Page<>(1, 10);
IPage<XxxEntity> pageResult = xxxMapper.selectPage(page,
    new LambdaQueryWrapper<XxxEntity>()
        .eq(XxxEntity::getType, 1)
);
```

### 2. 常用新增方法

```java
// 插入一条记录（null字段也会插入）
xxxMapper.insert(entity);

// 批量插入（需要在 Service 中使用）
xxxService.saveBatch(entityList);
```

### 3. 常用修改方法

```java
// 根据ID更新（null字段不更新）
xxxMapper.updateById(entity);

// 根据条件更新
xxxMapper.update(entity,
    new LambdaUpdateWrapper<XxxEntity>()
        .eq(XxxEntity::getType, 1)
);
```

### 4. 常用删除方法

```java
// 根据ID删除
xxxMapper.deleteById(id);

// 根据ID批量删除
xxxMapper.deleteBatchIds(Arrays.asList("id1", "id2", "id3"));

// 根据条件删除
xxxMapper.delete(
    new LambdaQueryWrapper<XxxEntity>()
        .eq(XxxEntity::getType, 1)
);
```

## 三、LambdaQueryWrapper 使用

### 1. 等值查询

```java
new LambdaQueryWrapper<XxxEntity>()
    .eq(XxxEntity::getName, "xxx")
    .eq(XxxEntity::getType, 1)
```

### 2. 模糊查询

```java
new LambdaQueryWrapper<XxxEntity>()
    .like(XxxEntity::getName, "xxx")      // LIKE '%xxx%'
    .likeLeft(XxxEntity::getName, "xxx")  // LIKE '%xxx'
    .likeRight(XxxEntity::getName, "xxx") // LIKE 'xxx%'
```

### 3. 范围查询

```java
new LambdaQueryWrapper<XxxEntity>()
    .gt(XxxEntity::getAge, 18)           // > 18
    .ge(XxxEntity::getAge, 18)           // >= 18
    .lt(XxxEntity::getAge, 60)           // < 60
    .le(XxxEntity::getAge, 60)           // <= 60
    .between(XxxEntity::getAge, 18, 60)  // BETWEEN 18 AND 60
```

### 4. IN 查询

```java
new LambdaQueryWrapper<XxxEntity>()
    .in(XxxEntity::getType, Arrays.asList(1, 2, 3))
```

### 5. NULL 查询

```java
new LambdaQueryWrapper<XxxEntity>()
    .isNull(XxxEntity::getDeleteTime)     // IS NULL
    .isNotNull(XxxEntity::getDeleteTime)  // IS NOT NULL
```

### 6. 排序

```java
new LambdaQueryWrapper<XxxEntity>()
    .orderByAsc(XxxEntity::getSort)
    .orderByDesc(XxxEntity::getCreateTime)
```

### 7. 条件拼接

```java
new LambdaQueryWrapper<XxxEntity>()
    .eq(StringUtils.isNotBlank(dto.getName()), XxxEntity::getName, dto.getName())
    .eq(dto.getType() != null, XxxEntity::getType, dto.getType())
    .ge(dto.getStartTime() != null, XxxEntity::getCreateTime, dto.getStartTime())
    .le(dto.getEndTime() != null, XxxEntity::getCreateTime, dto.getEndTime())
```

## 四、XML 映射文件

### 1. XML 文件头部

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.xxx.mapper.XxxMapper">

</mapper>
```

### 2. 分页查询 XML

```xml
<select id="queryPage" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name,
        t.type,
        t.status,
        DATE_FORMAT(t.create_time, '%Y-%m-%d %H:%i:%s') AS createTime,
        u.realname AS createByName
    FROM xxx_table t
    LEFT JOIN sys_user u ON t.create_by = u.username
    <where>
        <if test="dto.name != null and dto.name != ''">
            AND t.name LIKE CONCAT('%', #{dto.name}, '%')
        </if>
        <if test="dto.type != null">
            AND t.type = #{dto.type}
        </if>
        <if test="dto.status != null">
            AND t.status = #{dto.status}
        </if>
        <if test="dto.startTime != null and dto.startTime != ''">
            AND t.create_time &gt;= #{dto.startTime}
        </if>
        <if test="dto.endTime != null and dto.endTime != ''">
            AND t.create_time &lt;= #{dto.endTime}
        </if>
    </where>
    ORDER BY t.create_time DESC
</select>
```

### 3. 列表查询 XML

```xml
<select id="queryList" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name,
        t.type
    FROM xxx_table t
    <where>
        <if test="dto.type != null">
            AND t.type = #{dto.type}
        </if>
    </where>
    ORDER BY t.sort ASC, t.create_time DESC
</select>
```

### 4. 详情查询 XML

```xml
<select id="queryById" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name,
        t.type,
        t.status,
        t.remark,
        DATE_FORMAT(t.create_time, '%Y-%m-%d %H:%i:%s') AS createTime,
        u1.realname AS createByName,
        DATE_FORMAT(t.update_time, '%Y-%m-%d %H:%i:%s') AS updateTime,
        u2.realname AS updateByName
    FROM xxx_table t
    LEFT JOIN sys_user u1 ON t.create_by = u1.username
    LEFT JOIN sys_user u2 ON t.update_by = u2.username
    WHERE t.id = #{id}
</select>
```

### 5. 统计查询 XML

```xml
<select id="countByType" resultType="java.lang.Long">
    SELECT COUNT(*)
    FROM xxx_table
    WHERE type = #{type}
</select>
```

## 五、动态 SQL 使用

### 1. if 标签

```xml
<if test="dto.name != null and dto.name != ''">
    AND t.name LIKE CONCAT('%', #{dto.name}, '%')
</if>
```

### 2. where 标签

```xml
<where>
    <if test="dto.name != null and dto.name != ''">
        AND t.name LIKE CONCAT('%', #{dto.name}, '%')
    </if>
    <if test="dto.type != null">
        AND t.type = #{dto.type}
    </if>
</where>
```

### 3. choose/when/otherwise 标签

```xml
<choose>
    <when test="dto.orderBy == 'name'">
        ORDER BY t.name ASC
    </when>
    <when test="dto.orderBy == 'time'">
        ORDER BY t.create_time DESC
    </when>
    <otherwise>
        ORDER BY t.id DESC
    </otherwise>
</choose>
```

### 4. foreach 标签

```xml
<select id="queryByIds" resultType="com.xxx.vo.XxxVO">
    SELECT * FROM xxx_table
    WHERE id IN
    <foreach collection="idList" item="id" open="(" separator="," close=")">
        #{id}
    </foreach>
</select>
```

### 5. trim 标签

```xml
<trim prefix="WHERE" prefixOverrides="AND |OR ">
    <if test="dto.name != null and dto.name != ''">
        AND t.name = #{dto.name}
    </if>
    <if test="dto.type != null">
        AND t.type = #{dto.type}
    </if>
</trim>
```

## 六、SQL 编写规范

### 1. 字段别名规范

```xml
<!-- ✅ 正确：使用 AS 定义别名，驼峰命名 -->
<select id="queryPage" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name,
        t.create_time AS createTime,
        t.update_time AS updateTime,
        u.realname AS createByName
    FROM xxx_table t
    LEFT JOIN sys_user u ON t.create_by = u.username
</select>
```

### 2. 日期格式化

```xml
<!-- ✅ 使用 DATE_FORMAT 格式化日期 -->
DATE_FORMAT(t.create_time, '%Y-%m-%d %H:%i:%s') AS createTime
```

### 3. 关联查询

```xml
<!-- ✅ 使用 LEFT JOIN 关联查询 -->
<select id="queryPage" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name,
        u.realname AS createByName,
        d.name AS deptName
    FROM xxx_table t
    LEFT JOIN sys_user u ON t.create_by = u.username
    LEFT JOIN sys_dept d ON u.dept_id = d.id
    WHERE t.id = #{id}
</select>
```

### 4. LIKE 查询

```xml
<!-- ✅ 使用 CONCAT 拼接 -->
<if test="dto.name != null and dto.name != ''">
    AND t.name LIKE CONCAT('%', #{dto.name}, '%')
</if>
```

### 5. 大于小于符号

```xml
<!-- ✅ 使用转义字符 -->
<if test="dto.startTime != null">
    AND t.create_time &gt;= #{dto.startTime}  <!-- >= -->
</if>
<if test="dto.endTime != null">
    AND t.create_time &lt;= #{dto.endTime}    <!-- <= -->
</if>
```

## 七、结果映射

### 1. resultType

```xml
<!-- 简单映射：字段名与属性名一致（驼峰自动转换） -->
<select id="queryList" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name,
        t.create_time AS createTime
    FROM xxx_table t
</select>
```

### 2. resultMap

```xml
<!-- 复杂映射：一对一、一对多 -->
<resultMap id="XxxResultMap" type="com.xxx.vo.XxxVO">
    <id column="id" property="id"/>
    <result column="name" property="name"/>
    <result column="create_time" property="createTime"/>
    <association property="createUser" javaType="com.xxx.vo.UserVO">
        <id column="user_id" property="id"/>
        <result column="username" property="username"/>
        <result column="realname" property="realname"/>
    </association>
    <collection property="detailList" ofType="com.xxx.vo.XxxDetailVO">
        <id column="detail_id" property="id"/>
        <result column="detail_name" property="name"/>
    </collection>
</resultMap>

<select id="queryById" resultMap="XxxResultMap">
    SELECT
        t.id,
        t.name,
        t.create_time,
        u.id AS user_id,
        u.username,
        u.realname,
        d.id AS detail_id,
        d.name AS detail_name
    FROM xxx_table t
    LEFT JOIN sys_user u ON t.create_by = u.username
    LEFT JOIN xxx_detail d ON t.id = d.main_id
    WHERE t.id = #{id}
</select>
```

## 八、SQL 优化建议

### 1. 避免 SELECT *

```xml
<!-- ❌ 错误示例 -->
<select id="queryList" resultType="com.xxx.vo.XxxVO">
    SELECT * FROM xxx_table
</select>

<!-- ✅ 正确示例 -->
<select id="queryList" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name,
        t.type
    FROM xxx_table t
</select>
```

### 2. 合理使用索引

```xml
<!-- ✅ WHERE 条件使用索引字段 -->
<select id="queryList" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name
    FROM xxx_table t
    WHERE t.type = #{type}  -- type 字段有索引
</select>
```

### 3. 避免函数操作索引字段

```xml
<!-- ❌ 错误示例：索引失效 -->
WHERE DATE_FORMAT(t.create_time, '%Y-%m-%d') = '2024-12-01'

<!-- ✅ 正确示例：索引有效 -->
WHERE t.create_time &gt;= '2024-12-01 00:00:00'
  AND t.create_time &lt;= '2024-12-01 23:59:59'
```

### 4. 分页查询优化

```xml
<!-- ✅ 使用 MyBatis-Plus 分页插件 -->
<select id="queryPage" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name
    FROM xxx_table t
    WHERE t.type = #{dto.type}
    ORDER BY t.create_time DESC
    -- 不需要写 LIMIT，分页插件会自动添加
</select>
```

## 九、常见错误和解决方案

### 1. ❌ 错误：未使用 @Param 注解

```java
// ❌ 错误示例
IPage<XxxVO> queryPage(Page<XxxVO> page, XxxDTO dto);
```

```java
// ✅ 正确示例
IPage<XxxVO> queryPage(Page<XxxVO> page, @Param("dto") XxxDTO dto);
```

### 2. ❌ 错误：返回 Entity 而不是 VO

```java
// ❌ 错误示例
List<XxxEntity> queryList(@Param("dto") XxxDTO dto);
```

```java
// ✅ 正确示例
List<XxxVO> queryList(@Param("dto") XxxDTO dto);
```

### 3. ❌ 错误：XML 中直接使用 < > 符号

```xml
<!-- ❌ 错误示例 -->
<if test="dto.age != null">
    AND t.age >= #{dto.age}
</if>
```

```xml
<!-- ✅ 正确示例 -->
<if test="dto.age != null">
    AND t.age &gt;= #{dto.age}
</if>
```

### 4. ❌ 错误：日期字段未格式化

```xml
<!-- ❌ 错误示例 -->
<select id="queryList" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.create_time AS createTime
    FROM xxx_table t
</select>
<!-- 返回的时间格式：2024-12-01T10:30:45.000+00:00 -->
```

```xml
<!-- ✅ 正确示例 -->
<select id="queryList" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        DATE_FORMAT(t.create_time, '%Y-%m-%d %H:%i:%s') AS createTime
    FROM xxx_table t
</select>
<!-- 返回的时间格式：2024-12-01 10:30:45 -->
```

## 十、注释规范

### 1. Mapper 接口注释

```java
/**
 * 分页查询XXX列表
 *
 * @param page 分页对象
 * @param dto 查询条件
 * @return 分页结果
 */
IPage<XxxVO> queryPage(Page<XxxVO> page, @Param("dto") XxxDTO dto);
```

### 2. XML 注释

```xml
<!-- 分页查询XXX列表 -->
<select id="queryPage" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name
    FROM xxx_table t
</select>
```
