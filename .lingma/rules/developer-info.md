---
trigger: always
description: 开发人员信息配置
---

# 开发人员信息配置

## 默认开发者信息

在生成代码注释时，使用以下默认信息：

```java
/**
 * @author PS-BMP Development Team
 * @since 2024-12-01
 * @version 1.0.0
 */
```

## 代码作者标识规范

### Java 类注释

```java
/**
 * 类描述
 *
 * @author PS-BMP Development Team
 * @version 1.0.0
 * @since 2024-12-01
 */
public class ClassName {
}
```

### Vue 组件注释

```vue
<!--
  组件描述
  
  @author PS-BMP Development Team
  @since 2024-12-01
-->
<template>
</template>
```

### SQL Mapper 注释

```xml
<!--
  功能描述
  
  @author PS-BMP Development Team
  @since 2024-12-01
-->
<mapper namespace="com.jiuxi.xxx.mapper.XXXMapper">
</mapper>
```

## Git Commit 信息规范

提交信息格式：`<type>(<scope>): <subject>`

**类型（type）**：
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整（不影响功能）
- `refactor`: 重构（既不是新功能也不是修复bug）
- `perf`: 性能优化
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

**范围（scope）**：
- `frontend`: 前端相关
- `backend`: 后端相关
- `api`: API接口相关
- `db`: 数据库相关
- `config`: 配置相关

**示例**：
```bash
feat(backend): 添加第三方应用管理功能
fix(frontend): 修复列表页日期格式显示错误
docs(api): 更新API接口文档
refactor(backend): 重构人员查询服务层代码
```

## 文件头注释模板

### Java 文件

```java
/*
 * Copyright (c) 2024 PS-BMP Development Team
 * 
 * Project: PS-BMP 管理系统
 * Module: 系统管理
 * 
 * Created: 2024-12-01
 */
package com.jiuxi.xxx;
```

### Vue 文件

```vue
<!--
  Copyright (c) 2024 PS-BMP Development Team
  
  Project: PS-BMP 管理系统
  Module: 前端管理界面
  
  Created: 2024-12-01
-->
<template>
</template>
```

## 版本号管理

遵循语义化版本规范（Semantic Versioning）：

**格式**：`主版本号.次版本号.修订号` (MAJOR.MINOR.PATCH)

- **主版本号（MAJOR）**：不兼容的API修改
- **次版本号（MINOR）**：向下兼容的功能性新增
- **修订号（PATCH）**：向下兼容的问题修正

**示例**：
- `1.0.0`：第一个正式版本
- `1.1.0`：新增功能（向下兼容）
- `1.1.1`：bug修复（向下兼容）
- `2.0.0`：重大更新（可能不兼容）

## 代码审查标记

在代码审查时添加审查信息：

```java
/**
 * 类描述
 *
 * @author PS-BMP Development Team
 * @reviewer 审查人姓名
 * @reviewDate 2024-12-01
 * @version 1.0.0
 */
```

## 联系方式（可选）

如需添加具体开发人员联系方式，可在注释中包含：

```java
/**
 * 核心工具类
 *
 * @author 张三 (zhangsan@example.com)
 * @author 李四 (lisi@example.com)
 * @version 1.0.0
 * @since 2024-12-01
 */
```
