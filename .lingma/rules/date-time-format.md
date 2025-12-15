---
trigger: always
description: 日期时间格式转换规范，包括前后端传输、数据库存储、显示格式等
---

# 日期时间格式规范

## 一、标准格式定义

### 1. 标准日期时间格式

| 格式类型 | 格式字符串 | 示例 | 使用场景 |
|---------|-----------|------|----------|
| **标准日期时间** | `yyyy-MM-dd HH:mm:ss` | 2024-12-01 14:30:45 | 数据库存储、前后端传输 |
| **日期** | `yyyy-MM-dd` | 2024-12-01 | 日期选择器、日期查询 |
| **时间** | `HH:mm:ss` | 14:30:45 | 时间选择器 |
| **年月** | `yyyy-MM` | 2024-12 | 月份选择器 |
| **年** | `yyyy` | 2024 | 年份选择器 |

### 2. 时区规范

- **统一使用东八区（GMT+8）**
- **数据库时间统一存储为北京时间**
- **前端显示时间统一为北京时间**

## 二、后端日期处理

### 1. Entity 中的日期字段

```java
/**
 * XXX实体
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@Data
@TableName("xxx_table")
public class XxxEntity {
    
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    
    /**
     * 生效时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectTime;
    
}
```

### 2. 注解说明

```java
/**
 * @JsonFormat：JSON 序列化时的日期格式
 * - timezone: 时区，统一使用 GMT+8
 * - pattern: 格式，统一使用 yyyy-MM-dd HH:mm:ss
 */
@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")

/**
 * @DateTimeFormat：接收前端传递的日期参数时的格式
 * - pattern: 格式，统一使用 yyyy-MM-dd HH:mm:ss
 */
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

/**
 * @TableField：MyBatis-Plus 字段填充策略
 * - fill: FieldFill.INSERT - 插入时自动填充
 * - fill: FieldFill.UPDATE - 更新时自动填充
 */
@TableField(fill = FieldFill.INSERT)
```

### 3. VO 中的日期字段

```java
/**
 * XXX VO
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@Data
public class XxxVO {
    
    /**
     * 创建时间（字符串类型，已格式化）
     */
    private String createTime;
    
    /**
     * 更新时间（字符串类型，已格式化）
     */
    private String updateTime;
    
}
```

### 4. Mapper XML 中的日期格式化

```xml
<select id="queryPage" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name,
        DATE_FORMAT(t.create_time, '%Y-%m-%d %H:%i:%s') AS createTime,
        DATE_FORMAT(t.update_time, '%Y-%m-%d %H:%i:%s') AS updateTime
    FROM xxx_table t
    ORDER BY t.create_time DESC
</select>
```

### 5. DATE_FORMAT 格式符

| 格式符 | 说明 | 示例 |
|-------|------|------|
| `%Y` | 四位年份 | 2024 |
| `%m` | 两位月份（01-12） | 12 |
| `%d` | 两位日期（01-31） | 01 |
| `%H` | 24小时制小时（00-23） | 14 |
| `%i` | 分钟（00-59） | 30 |
| `%s` | 秒（00-59） | 45 |
| `%Y-%m-%d` | 日期格式 | 2024-12-01 |
| `%H:%i:%s` | 时间格式 | 14:30:45 |
| `%Y-%m-%d %H:%i:%s` | 完整日期时间 | 2024-12-01 14:30:45 |

## 三、前端日期处理

### 1. dayjs 引入

```javascript
import dayjs from "dayjs"
```

**所有页面必须引入 dayjs**，用于日期格式化和转换。

### 2. 日期格式化

```javascript
// 格式化为标准日期时间
dayjs(new Date()).format("YYYY-MM-DD HH:mm:ss")
// 输出：2024-12-01 14:30:45

// 格式化为日期
dayjs(new Date()).format("YYYY-MM-DD")
// 输出：2024-12-01

// 格式化为时间
dayjs(new Date()).format("HH:mm:ss")
// 输出：14:30:45

// 格式化为年月
dayjs(new Date()).format("YYYY-MM")
// 输出：2024-12
```

### 3. 日期选择器值转换

#### fb-date-picker 日期选择器

```vue
<template>
  <fb-date-picker
    v-model="formData.effectTime"
    :value-format="'YYYY-MM-DD HH:mm:ss'"
    :time-picker-options="{ format: 'HH:mm:ss' }"
    placeholder="请选择生效时间"
  />
</template>

<script>
export default {
  data() {
    return {
      formData: {
        effectTime: ""  // 绑定值会自动格式化为 YYYY-MM-DD HH:mm:ss
      }
    }
  }
}
</script>
```

#### fb-date-picker 参数说明

```javascript
/**
 * value-format: 绑定值的格式
 * - 'YYYY-MM-DD HH:mm:ss' - 完整日期时间
 * - 'YYYY-MM-DD' - 仅日期
 * - 'YYYY-MM' - 年月
 * - 'YYYY' - 年份
 */
:value-format="'YYYY-MM-DD HH:mm:ss'"

/**
 * time-picker-options: 时间选择器配置
 * - format: 时间格式，默认 'HH:mm:ss'
 */
:time-picker-options="{ format: 'HH:mm:ss' }"
```

### 4. tp-datepicker 日期选择器

```vue
<template>
  <tp-datepicker
    v-model="formData.effectTime"
    :value-format="'YYYY-MM-DD HH:mm:ss'"
    :time-picker-options="{ format: 'HH:mm:ss' }"
    placeholder="请选择生效时间"
  />
</template>

<script>
export default {
  data() {
    return {
      formData: {
        effectTime: ""  // tp-datepicker 会自动转换为 YYYY-MM-DD HH:mm:ss
      }
    }
  }
}
</script>
```

**tp-datepicker 特性**：
- 自动将 Date 对象转换为字符串
- 自动将 timestamp 转换为字符串
- 自动应用 value-format 格式

### 5. 表格中的日期显示

```vue
<fb-table-column
  label="创建时间"
  prop="createTime"
  width="180"
/>
```

后端返回的 `createTime` 已经是格式化后的字符串（`yyyy-MM-dd HH:mm:ss`），直接显示即可。

### 6. 查询条件日期范围

```vue
<template>
  <fb-form :model="queryForm" ref="fbform">
    <fb-form-item label="创建时间" prop="createTime">
      <fb-date-picker
        v-model="queryForm.createTime"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :value-format="'YYYY-MM-DD HH:mm:ss'"
      />
    </fb-form-item>
  </fb-form>
</template>

<script>
export default {
  data() {
    return {
      queryForm: {
        createTime: []  // [开始时间, 结束时间]
      }
    }
  },
  methods: {
    handleQuery() {
      // 将日期范围转换为查询参数
      const params = {
        ...this.queryForm,
        startTime: this.queryForm.createTime[0] || "",
        endTime: this.queryForm.createTime[1] || ""
      }
      delete params.createTime
      
      this.$refs.table.doSearch(params)
    }
  }
}
</script>
```

## 四、Service 层日期处理

### 1. 前端传递日期到后端

```javascript
// 前端
const formData = {
  name: "测试",
  effectTime: "2024-12-01 14:30:45"  // 字符串格式
}

app.service.request({
  url: '/sys/xxx/add',
  method: 'post',
  data: formData,
  headers: {'Content-Type': 'application/json'}
})
```

```java
// 后端 DTO
@Data
public class XxxDTO {
    
    private String name;
    
    /**
     * 生效时间
     * 接收字符串，Spring 会自动转换为 Date
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectTime;
    
}
```

### 2. 后端返回日期到前端

```java
// 后端 VO
@Data
public class XxxVO {
    
    private String id;
    private String name;
    
    /**
     * 创建时间（字符串类型，已由 SQL 格式化）
     */
    private String createTime;
    
}
```

```xml
<!-- Mapper XML -->
<select id="queryPage" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name,
        DATE_FORMAT(t.create_time, '%Y-%m-%d %H:%i:%s') AS createTime
    FROM xxx_table t
</select>
```

```javascript
// 前端接收
const result = await xxxService.queryPage(params)
console.log(result.data.records[0].createTime)
// 输出：2024-12-01 14:30:45
```

## 五、常见日期处理场景

### 1. 获取当前时间

```javascript
// 前端
const now = dayjs().format("YYYY-MM-DD HH:mm:ss")
// 2024-12-01 14:30:45
```

```java
// 后端
Date now = new Date();
// 自动填充字段无需手动设置
```

### 2. 日期加减

```javascript
// 前端 - 加7天
const after7Days = dayjs().add(7, 'day').format("YYYY-MM-DD HH:mm:ss")

// 前端 - 减30天
const before30Days = dayjs().subtract(30, 'day').format("YYYY-MM-DD HH:mm:ss")

// 前端 - 本月第一天
const firstDayOfMonth = dayjs().startOf('month').format("YYYY-MM-DD HH:mm:ss")

// 前端 - 本月最后一天
const lastDayOfMonth = dayjs().endOf('month').format("YYYY-MM-DD HH:mm:ss")
```

### 3. 日期比较

```javascript
// 前端
const date1 = dayjs("2024-12-01")
const date2 = dayjs("2024-12-15")

// 是否在之前
date1.isBefore(date2)  // true

// 是否在之后
date1.isAfter(date2)  // false

// 是否相同
date1.isSame(date2)  // false
```

### 4. 日期范围默认值

```javascript
// 查询条件默认最近7天
data() {
  return {
    queryForm: {
      createTime: [
        dayjs().subtract(6, 'day').format("YYYY-MM-DD 00:00:00"),
        dayjs().format("YYYY-MM-DD 23:59:59")
      ]
    }
  }
}
```

## 六、数据库日期字段类型

### 1. MySQL 日期类型

| 类型 | 格式 | 范围 | 使用场景 |
|------|------|------|----------|
| **datetime** | YYYY-MM-DD HH:mm:ss | 1000-01-01 00:00:00 到 9999-12-31 23:59:59 | 业务时间字段（推荐） |
| date | YYYY-MM-DD | 1000-01-01 到 9999-12-31 | 仅需要日期的字段 |
| timestamp | YYYY-MM-DD HH:mm:ss | 1970-01-01 00:00:01 到 2038-01-19 03:14:07 | 系统时间戳 |

### 2. 字段定义

```sql
-- 创建时间
create_time datetime DEFAULT NULL COMMENT '创建时间',

-- 更新时间
update_time datetime DEFAULT NULL COMMENT '更新时间',

-- 生效时间
effect_time datetime DEFAULT NULL COMMENT '生效时间',

-- 失效时间
expire_time datetime DEFAULT NULL COMMENT '失效时间'
```

## 七、必须遵守的规则

1. **后端 Entity 日期字段必须添加注解**：
   ```java
   @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   ```

2. **后端 VO 日期字段使用 String 类型**：
   ```java
   private String createTime;  // 已由 SQL 格式化
   ```

3. **Mapper XML 必须格式化日期**：
   ```xml
   DATE_FORMAT(t.create_time, '%Y-%m-%d %H:%i:%s') AS createTime
   ```

4. **前端必须引入 dayjs**：
   ```javascript
   import dayjs from "dayjs"
   ```

5. **日期选择器必须设置 value-format**：
   ```vue
   :value-format="'YYYY-MM-DD HH:mm:ss'"
   ```

6. **统一使用东八区时间**：
   ```java
   timezone = "GMT+8"
   ```

7. **数据库日期字段统一使用 datetime 类型**

8. **所有日期时间格式统一为**：`yyyy-MM-dd HH:mm:ss`

## 八、常见错误和解决方案

### 1. ❌ 错误：Entity 未添加 @JsonFormat

```java
// ❌ 错误
private Date createTime;
// 返回前端：2024-12-01T14:30:45.000+08:00
```

```java
// ✅ 正确
@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
private Date createTime;
// 返回前端：2024-12-01 14:30:45
```

### 2. ❌ 错误：XML 未格式化日期

```xml
<!-- ❌ 错误 -->
<select id="queryList" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.create_time AS createTime
    FROM xxx_table t
</select>
<!-- 返回格式不统一 -->
```

```xml
<!-- ✅ 正确 -->
<select id="queryList" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        DATE_FORMAT(t.create_time, '%Y-%m-%d %H:%i:%s') AS createTime
    FROM xxx_table t
</select>
<!-- 返回：2024-12-01 14:30:45 -->
```

### 3. ❌ 错误：前端未引入 dayjs

```javascript
// ❌ 错误
const now = new Date().toISOString()
// 格式不统一
```

```javascript
// ✅ 正确
import dayjs from "dayjs"
const now = dayjs().format("YYYY-MM-DD HH:mm:ss")
// 2024-12-01 14:30:45
```

### 4. ❌ 错误：日期选择器未设置 value-format

```vue
<!-- ❌ 错误 -->
<fb-date-picker v-model="formData.effectTime" />
<!-- 绑定值可能是 Date 对象 -->
```

```vue
<!-- ✅ 正确 -->
<fb-date-picker
  v-model="formData.effectTime"
  :value-format="'YYYY-MM-DD HH:mm:ss'"
/>
<!-- 绑定值：2024-12-01 14:30:45 -->
```
