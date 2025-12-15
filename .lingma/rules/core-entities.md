---
trigger: always
description: 核心实体定义和字段映射规范，包括用户、部门、字典等核心实体
---

# 核心实体定义规范

## 一、用户实体

### 1. sys_user 表结构

| 字段名 | 类型 | 说明 | Java属性名 |
|--------|------|------|-----------|
| id | varchar(32) | 主键ID | id |
| username | varchar(100) | 账号 | username |
| realname | varchar(100) | 真实姓名 | realname |
| password | varchar(255) | 密码 | password |
| avatar | varchar(255) | 头像 | avatar |
| birthday | datetime | 生日 | birthday |
| sex | int | 性别（1-男，2-女） | sex |
| email | varchar(45) | 电子邮件 | email |
| phone | varchar(45) | 电话 | phone |
| org_code | varchar(64) | 机构编码 | orgCode |
| status | int | 状态（1-正常，2-冻结） | status |
| del_flag | int | 删除状态（0-正常，1-已删除） | delFlag |
| work_no | varchar(100) | 工号 | workNo |
| post | varchar(100) | 职务 | post |
| telephone | varchar(45) | 座机号 | telephone |
| create_by | varchar(32) | 创建人 | createBy |
| create_time | datetime | 创建时间 | createTime |
| update_by | varchar(32) | 更新人 | updateBy |
| update_time | datetime | 更新时间 | updateTime |

### 2. 用户实体类

```java
/**
 * 用户实体
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@Data
@TableName("sys_user")
public class SysUser {
    
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    private String username;
    private String realname;
    private String password;
    private String avatar;
    private Date birthday;
    private Integer sex;
    private String email;
    private String phone;
    private String orgCode;
    private Integer status;
    private Integer delFlag;
    private String workNo;
    private String post;
    private String telephone;
    
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;
    
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    
}
```

## 二、部门实体

### 1. sys_depart 表结构

| 字段名 | 类型 | 说明 | Java属性名 |
|--------|------|------|-----------|
| id | varchar(32) | 主键ID | id |
| parent_id | varchar(32) | 父级ID | parentId |
| depart_name | varchar(100) | 部门名称 | departName |
| depart_name_en | varchar(500) | 英文名称 | departNameEn |
| depart_name_abbr | varchar(500) | 缩写 | departNameAbbr |
| depart_order | int | 排序 | departOrder |
| description | text | 描述 | description |
| org_code | varchar(64) | 机构编码 | orgCode |
| org_category | varchar(10) | 机构类别（1-公司，2-部门） | orgCategory |
| org_type | varchar(10) | 机构类型 | orgType |
| mobile | varchar(32) | 手机号 | mobile |
| fax | varchar(32) | 传真 | fax |
| address | varchar(100) | 地址 | address |
| memo | varchar(500) | 备注 | memo |
| status | varchar(1) | 状态（1-正常，0-禁用） | status |
| del_flag | varchar(1) | 删除状态（0-正常，1-已删除） | delFlag |
| create_by | varchar(32) | 创建人 | createBy |
| create_time | datetime | 创建时间 | createTime |
| update_by | varchar(32) | 更新人 | updateBy |
| update_time | datetime | 更新时间 | updateTime |

### 2. 部门实体类

```java
/**
 * 部门实体
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@Data
@TableName("sys_depart")
public class SysDepart {
    
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    private String parentId;
    private String departName;
    private String departNameEn;
    private String departNameAbbr;
    private Integer departOrder;
    private String description;
    private String orgCode;
    private String orgCategory;
    private String orgType;
    private String mobile;
    private String fax;
    private String address;
    private String memo;
    private String status;
    private String delFlag;
    
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;
    
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    
}
```

## 三、字典实体

### 1. sys_dict 表结构（字典主表）

| 字段名 | 类型 | 说明 | Java属性名 |
|--------|------|------|-----------|
| id | varchar(32) | 主键ID | id |
| dict_name | varchar(100) | 字典名称 | dictName |
| dict_code | varchar(100) | 字典编码 | dictCode |
| description | varchar(255) | 描述 | description |
| del_flag | int | 删除状态（0-正常，1-已删除） | delFlag |
| create_by | varchar(32) | 创建人 | createBy |
| create_time | datetime | 创建时间 | createTime |
| update_by | varchar(32) | 更新人 | updateBy |
| update_time | datetime | 更新时间 | updateTime |
| type | int | 字典类型（0-字符串，1-数字） | type |

### 2. sys_dict_item 表结构（字典项表）

| 字段名 | 类型 | 说明 | Java属性名 |
|--------|------|------|-----------|
| id | varchar(32) | 主键ID | id |
| dict_id | varchar(32) | 字典ID | dictId |
| item_text | varchar(100) | 字典项文本 | itemText |
| item_value | varchar(100) | 字典项值 | itemValue |
| description | varchar(255) | 描述 | description |
| sort_order | int | 排序 | sortOrder |
| status | int | 状态（0-禁用，1-启用） | status |
| create_by | varchar(32) | 创建人 | createBy |
| create_time | datetime | 创建时间 | createTime |
| update_by | varchar(32) | 更新人 | updateBy |
| update_time | datetime | 更新时间 | updateTime |

### 3. 字典实体类

```java
/**
 * 字典主表
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@Data
@TableName("sys_dict")
public class SysDict {
    
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    private String dictName;
    private String dictCode;
    private String description;
    private Integer delFlag;
    private Integer type;
    
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;
    
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    
}

/**
 * 字典项表
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@Data
@TableName("sys_dict_item")
public class SysDictItem {
    
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    private String dictId;
    private String itemText;
    private String itemValue;
    private String description;
    private Integer sortOrder;
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;
    
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    
}
```

## 四、公共字段

### 1. 基础实体类

所有业务实体都应包含以下公共字段：

```java
/**
 * 基础实体类
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@Data
public class BaseEntity {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
}
```

### 2. 业务实体继承基础类

```java
/**
 * XXX实体
 *
 * @author PS-BMP Development Team
 * @since 2024-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("xxx_table")
public class XxxEntity extends BaseEntity {
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 类型
     */
    private Integer type;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
    
}
```

## 五、字段命名规范

### 1. 数据库字段命名

- **规则**：全小写，单词之间用下划线分隔
- **示例**：
  - `create_time`
  - `update_by`
  - `org_code`
  - `depart_name`

### 2. Java 属性命名

- **规则**：驼峰命名法
- **示例**：
  - `createTime`
  - `updateBy`
  - `orgCode`
  - `departName`

### 3. 自动映射

MyBatis-Plus 会自动将驼峰命名转换为下划线命名：

```java
// Java 属性
private Date createTime;

// 自动映射到数据库字段
// create_time
```

## 六、字段类型映射

### 1. Java 类型与数据库类型映射

| Java 类型 | 数据库类型 | 说明 |
|-----------|-----------|------|
| String | varchar | 字符串 |
| Integer | int | 整数 |
| Long | bigint | 长整数 |
| Double | double | 双精度浮点数 |
| BigDecimal | decimal | 精确小数 |
| Date | datetime | 日期时间 |
| Boolean | tinyint | 布尔值（0/1） |

### 2. 特殊类型处理

```java
/**
 * 日期类型
 */
@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private Date createTime;

/**
 * 金额类型
 */
private BigDecimal amount;

/**
 * 枚举类型
 */
private Integer status;  // 使用整数存储
```

## 七、常用字段说明

### 1. 状态字段

```java
/**
 * 状态（1-正常，0-禁用）
 */
private Integer status;
```

### 2. 删除标志

```java
/**
 * 删除状态（0-正常，1-已删除）
 */
private Integer delFlag;
```

### 3. 排序字段

```java
/**
 * 排序号
 */
private Integer sortOrder;
```

### 4. 备注字段

```java
/**
 * 备注
 */
private String remark;
```

## 八、字段填充策略

### 1. 自动填充配置

```java
/**
 * 创建人（插入时自动填充）
 */
@TableField(fill = FieldFill.INSERT)
private String createBy;

/**
 * 创建时间（插入时自动填充）
 */
@TableField(fill = FieldFill.INSERT)
private Date createTime;

/**
 * 更新人（更新时自动填充）
 */
@TableField(fill = FieldFill.UPDATE)
private String updateBy;

/**
 * 更新时间（更新时自动填充）
 */
@TableField(fill = FieldFill.UPDATE)
private Date updateTime;
```

### 2. 填充处理器

自动填充由 `MyMetaObjectHandler` 统一处理，无需手动设置这些字段。

## 九、常用字典编码

### 1. 性别字典

- 字典编码：`sex`
- 字典项：
  - `1` - 男
  - `2` - 女

### 2. 状态字典

- 字典编码：`status`
- 字典项：
  - `1` - 正常
  - `0` - 禁用

### 3. 删除标志字典

- 字典编码：`del_flag`
- 字典项：
  - `0` - 正常
  - `1` - 已删除

## 十、关联查询字段映射

### 1. VO 中的关联字段

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
     * 主键ID
     */
    private String id;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 创建时间
     */
    private String createTime;
    
    /**
     * 创建人
     */
    private String createBy;
    
    /**
     * 创建人姓名（关联查询）
     */
    private String createByName;
    
    /**
     * 部门名称（关联查询）
     */
    private String deptName;
    
}
```

### 2. Mapper XML 中的关联查询

```xml
<select id="queryPage" resultType="com.xxx.vo.XxxVO">
    SELECT
        t.id,
        t.name,
        DATE_FORMAT(t.create_time, '%Y-%m-%d %H:%i:%s') AS createTime,
        t.create_by AS createBy,
        u.realname AS createByName,
        d.depart_name AS deptName
    FROM xxx_table t
    LEFT JOIN sys_user u ON t.create_by = u.username
    LEFT JOIN sys_depart d ON u.dept_id = d.id
    ORDER BY t.create_time DESC
</select>
```

## 十一、必须遵守的规则

1. **所有实体必须继承 BaseEntity**：包含公共字段
2. **主键统一使用 String 类型**：自动生成32位UUID
3. **日期字段统一使用 Date 类型**：并添加 `@JsonFormat` 注解
4. **金额字段统一使用 BigDecimal**：保证精度
5. **枚举值使用 Integer 存储**：便于扩展和维护
6. **公共字段使用自动填充**：不需要手动设置
7. **删除使用逻辑删除**：设置 `delFlag=1`，不物理删除
8. **字段必须添加注释**：说明字段含义
