# 组件包重构记录

## 重构概述

本次重构将多个组件包从项目根目录移动到统一的 `@fb` 目录下，并修正了所有相关的引用路径。

## 重构时间

2025年1月

## 重构内容

### 1. 创建 @fb 目录结构

在项目根目录下创建了 `@fb` 文件夹，用于统一管理所有组件包。

### 2. 移动的组件包

以下组件包已从根目录移动到 `@fb` 目录下：

- `fb-core` → `@fb/fb-core`
- `fb-ui` → `@fb/fb-ui`
- `admin-base` → `@fb/admin-base`
- `tp-components` → `@fb/tp-components`
- `screen-base` → `@fb/screen-base`
- `form-designer` → `@fb/form-designer`
- `log-center-ui` → `@fb/log-center-ui`
- `monitor-ui` → `@fb/monitor-ui`
- `schedule-ui` → `@fb/schedule-ui`
- `fb-third` → `@fb/fb-third`

### 3. 修正的引用路径

#### 主要配置文件

- `main.js` - 更新了所有组件包的引用路径
- `screen/main.js` - 更新了屏幕相关组件的引用路径
- `rsbuild.config.js` - 更新了构建配置中的路径引用
- `vue.config.js` - 更新了 Vue 配置中的路径引用
- `toNext.js` - 更新了迁移脚本中的路径引用

#### 样式文件

- `src/assets/styles/main.less` - 更新了 admin-base 样式引用
- `screen/assets/styles/main.less` - 更新了 screen-base 样式引用

#### 组件内部引用

修正了 `@fb` 目录下各组件包内部的相互引用路径，包括：

- `@fb/admin-base` 中对 `fb-ui`、`fb-core`、`tp-components` 的引用
- `@fb/tp-components` 中对 `fb-core`、`fb-ui` 的引用
- `@fb/screen-base` 中对 `fb-core`、`fb-ui`、`fb-third` 的引用
- 各组件包中 Vue 文件对其他组件的引用

### 4. 重构后的目录结构

```
@fb/
├── admin-base/
├── fb-core/
├── fb-third/
├── fb-ui/
├── form-designer/
├── log-center-ui/
├── monitor-ui/
├── schedule-ui/
├── screen-base/
└── tp-components/
```

## 重构优势

1. **统一管理**: 所有组件包集中在 `@fb` 目录下，便于管理和维护
2. **清晰结构**: 项目根目录更加简洁，组件包结构更加清晰
3. **命名规范**: 使用 `@fb` 前缀，符合现代前端项目的命名规范
4. **易于扩展**: 新增组件包可以直接放在 `@fb` 目录下

## 注意事项

1. 所有路径引用已更新为相对路径，确保项目可以正常运行
2. 保持了原有的组件包内部结构不变
3. 所有依赖关系保持不变，只是路径发生了变化
4. 建议在重构后进行完整的功能测试

## 验证步骤

重构完成后，建议执行以下验证步骤：

1. 运行 `npm install` 确保依赖安装正确
2. 运行 `npm run serve` 启动开发服务器
3. 运行 `npm run build` 执行构建测试
4. 测试主要功能模块是否正常工作
5. 检查控制台是否有路径相关的错误信息

## 相关文件

本次重构涉及的主要文件包括：

- 配置文件: `main.js`, `screen/main.js`, `rsbuild.config.js`, `vue.config.js`, `toNext.js`
- 样式文件: `src/assets/styles/main.less`, `screen/assets/styles/main.less`
- 组件文件: `@fb` 目录下的所有 `.vue`, `.js` 文件
- 工具文件: `@fb/admin-base/util/serviceConfig.js`, `@fb/tp-components/src/util/Page.js`

重构已完成，所有路径引用已修正。