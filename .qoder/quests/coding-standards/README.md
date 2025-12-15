# PS-BMP 编码规范体系

## 文档说明

本目录包含PS-BMP项目的完整编码规范体系，旨在为开发人员（包括AI Agent）提供统一的开发标准和最佳实践指导。

## 规范体系结构

```
coding-standards/
├── README.md                           # 本文件：规范体系总览和索引
├── frontend/                           # 前端编码规范
│   ├── component-usage.md             # 组件使用规范（fb-ui组件库）
│   ├── page-templates.md              # 页面模板规范（list/add/view）
│   ├── service-layer.md               # Service层编码规范
│   ├── data-binding.md                # 数据绑定和格式化规范
│   └── common-pitfalls.md             # 常见错误和解决方案
├── backend/                            # 后端编码规范
│   ├── controller-layer.md            # Controller层规范
│   ├── service-layer.md               # Service层规范
│   ├── mapper-layer.md                # Mapper层规范
│   ├── dto-vo-standards.md            # DTO/VO设计规范
│   └── exception-handling.md          # 异常处理规范
├── data-standards/                     # 数据标准和字典
│   ├── core-entities.md               # 核心实体定义（人员、部门等）
│   ├── field-naming.md                # 字段命名规范
│   ├── data-types.md                  # 数据类型标准
│   └── date-time-format.md            # 日期时间格式规范
└── api-contracts/                      # API接口契约
    ├── request-response.md            # 请求响应规范
    ├── error-codes.md                 # 错误码定义
    ├── api-versioning.md              # API版本管理
    └── common-interfaces.md           # 通用接口规范

```

## 快速导航

### 1. 前端开发规范

#### 🎨 组件使用
- **[组件使用规范](frontend/component-usage.md)** - fb-ui组件库完整使用指南
  - 布局组件（fb-row、fb-col）
  - 表单组件（fb-form、fb-input、fb-select等）
  - 数据展示（fb-simple-table、fb-property）
  - 页面模板（fb-page-search、fb-page-tree-table）

- **[tp-components业务组件库](frontend/tp-components-guide.md)** - 业务组件使用指南
  - tp-dialog（单页面弹窗）
  - tp-dialog-tab（多Tab弹窗）
  - tp-upload（文件上传）
  - tp-datepicker（日期选择器）

#### 📄 页面开发
- **[页面模板规范](frontend/page-templates.md)** - 标准页面开发指南
  - list.vue 列表页模板
  - add.vue 新增/编辑页模板
  - view.vue 查看页模板
  - 弹窗组件使用规范

#### 🔌 服务调用
- **[Service层规范](frontend/service-layer.md)** - API服务层开发规范
  - Service文件结构
  - 请求方式配置
  - 超时时间管理
  - 文件上传下载

#### 🔄 数据处理
- **[数据绑定规范](frontend/data-binding.md)** - 数据处理和格式化
  - 日期格式转换
  - 空值处理
  - 下拉选项绑定
  - 响应数据处理

#### ⚠️ 常见问题
- **[常见错误指南](frontend/common-pitfalls.md)** - 常见错误和解决方案
  - 列表刷新问题
  - 表单验证遗漏
  - 日期格式错误
  - API路径错误

### 2. 后端开发规范

#### 🎯 Controller层
- **[Controller层规范](backend/controller-layer.md)** - 控制器开发规范
  - 路径前缀规范（/sys）
  - 参数注解使用（@RequestBody vs 表单绑定）
  - 响应格式标准
  - 异常处理

#### 💼 Service层
- **[Service层规范](backend/service-layer.md)** - 业务逻辑层规范
  - 接口定义标准
  - 实现类规范
  - 事务管理
  - 业务异常处理

#### 🗄️ Mapper层
- **[Mapper层规范](backend/mapper-layer.md)** - 数据访问层规范
  - Mapper接口定义
  - XML文件存放路径
  - SQL编写规范
  - 结果映射

#### 📦 对象设计
- **[DTO/VO规范](backend/dto-vo-standards.md)** - 数据传输对象规范
  - DTO设计原则
  - VO设计原则
  - 字段命名规范
  - 类型转换

#### 🚨 异常处理
- **[异常处理规范](backend/exception-handling.md)** - 统一异常处理
  - 异常分类
  - 自定义异常
  - 全局异常处理
  - 错误码设计

### 3. 数据标准

#### 📚 核心实体
- **[核心实体定义](data-standards/core-entities.md)** - 系统核心数据对象
  - 人员（Person）实体
  - 部门（Department）实体
  - 组织（Organization）实体
  - 角色（Role）实体
  - 应用（Application）实体

#### 🏷️ 命名规范
- **[字段命名规范](data-standards/field-naming.md)** - 统一字段命名标准
  - 前端字段命名（camelCase）
  - 后端字段命名（camelCase）
  - 数据库字段命名（snake_case）
  - 常用字段字典

#### 🔢 数据类型
- **[数据类型标准](data-standards/data-types.md)** - 数据类型使用规范
  - 基础类型映射
  - 枚举类型定义
  - 特殊类型处理
  - 类型转换规则

#### ⏰ 日期时间
- **[日期时间格式](data-standards/date-time-format.md)** - 日期时间处理规范
  - 前端格式：`YYYY-MM-DD HH:mm:ss`
  - 后端存储：`YYYYMMDDHHmmss`
  - 数据库存储：`DATETIME` / `BIGINT`
  - 时区处理

### 4. API接口契约

#### 📡 请求响应
- **[请求响应规范](api-contracts/request-response.md)** - API通信标准
  - 请求格式（JSON/FormData）
  - 响应格式（统一结构）
  - 分页参数
  - 排序参数

#### 🔢 错误码
- **[错误码定义](api-contracts/error-codes.md)** - 统一错误码体系
  - 成功码：1
  - 客户端错误：400-499
  - 服务端错误：500-599
  - 业务错误码

#### 🔄 版本管理
- **[API版本管理](api-contracts/api-versioning.md)** - API版本控制
  - 版本命名规则
  - 兼容性保证
  - 废弃策略

#### 🔧 通用接口
- **[通用接口规范](api-contracts/common-interfaces.md)** - 标准API模式
  - 列表查询接口
  - 详情查询接口
  - 新增接口
  - 修改接口
  - 删除接口

## 使用指南

### 开发人员使用

1. **新功能开发前**
   - 查阅对应的规范文档
   - 参考标准模板和示例
   - 使用检查清单自查

2. **代码审查时**
   - 对照规范检查代码质量
   - 验证命名和结构规范
   - 确认错误处理完整性

3. **问题排查时**
   - 查看常见错误指南
   - 检查数据格式和字段命名
   - 验证API路径和参数

### AI Agent使用

1. **理解需求时**
   - 阅读本README了解规范体系
   - 根据任务类型定位具体规范文档

2. **生成代码时**
   - 严格遵循对应规范文档
   - 使用标准模板和代码片段
   - 确保字段命名和数据格式正确

3. **验证代码时**
   - 使用检查清单验证代码
   - 检查是否符合规范要求
   - 确认无常见错误

## 规范优先级

当遇到规范冲突或不确定时，请按以下优先级处理：

1. **项目特定规范** > 通用规范
2. **数据标准** > 代码风格
3. **API契约** > 实现细节
4. **安全规范** > 便利性
5. **向后兼容** > 新特性

## 规范维护

### 更新原则

1. **必须更新的情况**
   - 发现规范错误或遗漏
   - 新增核心功能模块
   - 技术栈重大升级
   - 发现重复的规范冲突

2. **建议更新的情况**
   - 发现更优的实践方式
   - 补充细节说明
   - 添加更多示例
   - 优化文档结构

### 更新流程

1. 修改对应的规范文档
2. 更新本README的索引
3. 通知团队成员
4. 更新相关代码示例

## 相关文档

- **[前端编码规范总览](../frontend-coding-guidelines.md)** - 前端完整编码规范（原始文档）
- **[项目README](../../../README.md)** - 项目整体说明
- **[API文档](../../../ps-fe/API.md)** - API接口文档
- **[组件指南](../../../ps-fe/COMPONENT_GUIDE.md)** - 组件使用指南

## 版本信息

- **版本号**: v1.0.0
- **创建日期**: 2025年12月1日
- **最后更新**: 2025年12月1日
- **维护团队**: PS-BMP开发团队

## 贡献指南

如发现规范问题或有改进建议，请：
1. 在团队沟通渠道提出
2. 提供具体的问题描述和改进建议
3. 附上代码示例或参考链接

---

**注意**: 本规范体系是活文档，会随着项目发展持续更新。请定期关注更新内容。
