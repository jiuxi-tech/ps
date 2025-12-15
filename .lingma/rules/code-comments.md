---
trigger: always
description: 代码注释规范
---

# 代码注释规范

## 注释原则

1. **精确性**：注释必须准确描述代码的功能、逻辑和意图
2. **专业性**：使用专业术语，避免口语化表达
3. **简洁性**：言简意赅，避免冗余和重复
4. **及时性**：代码修改时同步更新注释

## Java 注释规范

### 类注释

```java
/**
 * 第三方应用管理服务实现类
 * <p>
 * 提供第三方应用的CRUD操作、API密钥管理、权限配置等核心功能
 * </p>
 *
 * @author 开发者姓名
 * @version 1.0.0
 * @since 2024-12-01
 */
public class ThirdPartyAppServiceImpl implements ThirdPartyAppService {
}
```

### 方法注释

```java
/**
 * 查询第三方应用列表（分页）
 *
 * @param query 查询条件对象，包含应用名称、状态等筛选条件
 * @return 分页结果，包含应用列表和总数
 * @throws BusinessException 当查询参数非法时抛出
 */
public PageResult<ThirdPartyAppVO> listApps(ThirdPartyAppQuery query) {
    // 实现代码
}
```

### 字段注释

```java
/**
 * API密钥（32位随机字符串）
 * <p>用于身份验证，创建时自动生成，不可修改</p>
 */
private String apiKey;

/**
 * 应用状态：1-启用，0-禁用
 */
private Integer status;

/**
 * 过期时间（格式：YYYYMMDDHHmmss）
 * <p>为空表示永不过期</p>
 */
private String expireTime;
```

### 关键逻辑注释

```java
// 验证API密钥是否过期
if (StringUtils.isNotBlank(app.getExpireTime())) {
    LocalDateTime expireDateTime = DateUtil.parse(app.getExpireTime());
    if (LocalDateTime.now().isAfter(expireDateTime)) {
        throw new BusinessException("应用已过期");
    }
}

// 计算密码过期时间：当前时间 + 配置的有效月数
int validityMonths = passwordConfig.getValidityMonths();
LocalDateTime expireDateTime = LocalDateTime.now().plusMonths(validityMonths);
```

### 复杂算法注释

```java
/**
 * 计算用户密码强度
 * <p>
 * 评分规则：
 * 1. 长度 >= 8位：+20分
 * 2. 包含大写字母：+20分
 * 3. 包含小写字母：+20分
 * 4. 包含数字：+20分
 * 5. 包含特殊字符：+20分
 * </p>
 *
 * @param password 待评估密码
 * @return 密码强度评分（0-100）
 */
private int calculatePasswordStrength(String password) {
    int score = 0;
    
    // 长度检查
    if (password.length() >= 8) {
        score += 20;
    }
    
    // 字符类型检查
    if (password.matches(".*[A-Z].*")) score += 20;
    if (password.matches(".*[a-z].*")) score += 20;
    if (password.matches(".*\\d.*")) score += 20;
    if (password.matches(".*[!@#$%^&*()].*")) score += 20;
    
    return score;
}
```

## Vue 注释规范

### 组件注释

```vue
<!--
  第三方应用列表页
  
  功能：
  - 分页查询第三方应用列表
  - 支持按应用名称、状态筛选
  - 提供新增、修改、删除、权限配置等操作
  
  @author 开发者姓名
  @since 2024-12-01
-->
<template>
  <div>
    <!-- 查询表单 -->
    <fb-page-search>
      <!-- 内容 -->
    </fb-page-search>
  </div>
</template>
```

### 方法注释

```javascript
/**
 * 打开新增弹窗
 * 
 * @description 弹窗采用动态加载方式，传递 action='add' 元数据用于区分操作类型
 */
handleAdd() {
  let param = {};
  let options = { height: 500, width: 700 };
  this.$refs.TpDialog.show(import('./add.vue'), param, "新增", options, { action: 'add' });
}

/**
 * 删除第三方应用
 * 
 * @param {Object} row 表格行数据
 * @description 删除前进行二次确认，成功后刷新当前页列表
 */
handleDel(row) {
  this.$confirm('确定要删除该应用吗？删除后将无法恢复！', () => {
    this.delete(row.appId, row.passKey);
  })
}
```

### 数据属性注释

```javascript
data() {
  return {
    // 查询表单数据
    formData: {
      appName: '',      // 应用名称
      status: null,     // 状态：1-启用，0-禁用
      logDelete: 0,     // 逻辑删除标记：0-正常，1-已删除
    },
    
    // 表格配置
    table: {
      service: app.$svc.sys.thirdPartyApp,  // 数据服务
      primaryKey: "appId",                   // 主键字段
      columns: [],                           // 列配置
    }
  }
}
```

### 关键业务逻辑注释

```javascript
// 根据操作类型决定列表刷新方式
if (result.action === 'add') {
  // 新增成功：重新查询，定位到第一页
  this.$refs.table.doSearch()
} else if (result.action === 'edit') {
  // 修改成功：刷新当前页
  this.$refs.table.doReload()
}
```

## 注释禁忌

### ❌ 避免无意义注释

```java
// 错误示例
int i = 0;  // 定义变量i

// 正确示例（不需要注释，变量名已说明用途）
int retryCount = 0;
```

### ❌ 避免过时注释

```java
// 错误示例
/**
 * 查询用户列表
 * 注意：此方法已废弃，请使用 queryUserList
 */
public List<User> getUserList() {
  // 但实际代码已经完全重构，注释未更新
}
```

### ❌ 避免冗余注释

```java
// 错误示例
// 设置用户名
user.setUsername(username);
// 设置密码
user.setPassword(password);
// 设置邮箱
user.setEmail(email);

// 正确示例（一条注释说明整体逻辑）
// 构建用户基本信息
user.setUsername(username);
user.setPassword(password);
user.setEmail(email);
```

### ❌ 避免注释代码堆积

```java
// 错误示例
// if (status == 1) {
//     doSomething();
// }
// 上面的代码已经不用了，但是注释掉了没删除

// 正确做法：删除无用代码，通过版本控制系统查看历史
```

## SQL 注释规范

```xml
<!-- 查询第三方应用列表（分页） -->
<select id="selectAppList" resultType="ThirdPartyAppVO">
    SELECT 
        app.app_id,
        app.app_name,
        app.api_key,
        app.status,
        app.expire_time,
        p1.person_name AS create_person_name,  /* 创建人姓名 */
        p2.person_name AS update_person_name   /* 修改人姓名 */
    FROM tp_third_party_app app
    /* 关联创建人信息 */
    LEFT JOIN tp_person_basicinfo p1 ON app.create_person_id = p1.person_id
    /* 关联修改人信息 */
    LEFT JOIN tp_person_basicinfo p2 ON app.update_person_id = p2.person_id
    WHERE app.log_delete = 0
    <if test="appName != null and appName != ''">
        /* 模糊查询应用名称 */
        AND app.app_name LIKE CONCAT('%', #{appName}, '%')
    </if>
    <if test="status != null">
        /* 精确匹配状态 */
        AND app.status = #{status}
    </if>
    ORDER BY app.create_time DESC
</select>
```

## 特殊注释标记

使用标准的注释标记标识代码状态：

- `TODO`: 待实现的功能
- `FIXME`: 待修复的问题
- `HACK`: 临时解决方案
- `OPTIMIZE`: 可优化的代码
- `DEPRECATED`: 已废弃的代码

```java
// TODO: 实现密钥自动轮换机制
// FIXME: 修复并发情况下的缓存不一致问题
// HACK: 临时绕过权限检查，待权限系统完善后移除
// OPTIMIZE: 此处查询可改用缓存提升性能
// DEPRECATED: 此方法已废弃，请使用 newMethod() 替代
```

## 文档型注释

对于公共API、工具类等，需要提供完整的JavaDoc注释：

```java
/**
 * 日期时间格式转换工具类
 * <p>
 * 提供前后端日期格式的相互转换功能，统一项目中的日期处理逻辑
 * </p>
 * 
 * <h3>格式说明</h3>
 * <ul>
 *   <li>数据库存储格式：YYYYMMDDHHmmss（14位数字字符串）</li>
 *   <li>前端显示格式：YYYY-MM-DD HH:mm:ss</li>
 *   <li>前端编辑格式：Date 对象</li>
 * </ul>
 *
 * <h3>使用示例</h3>
 * <pre>{@code
 * // 数据库格式转前端显示格式
 * String display = DateUtil.formatToDisplay("20241201153045");
 * // 输出：2024-12-01 15:30:45
 * 
 * // 前端Date对象转数据库格式
 * String dbFormat = DateUtil.formatToDb(new Date());
 * // 输出：20241201153045
 * }</pre>
 *
 * @author 开发者姓名
 * @version 1.0.0
 * @since 2024-12-01
 * @see java.time.format.DateTimeFormatter
 */
public class DateUtil {
}
```
