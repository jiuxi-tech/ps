# Tasks 目录

本目录用于存储项目重构和优化过程中的任务记录文件。

## 目录结构

### 转换记录文件
- `admin-base-conversion-record.md` - admin-base 组件转换记录
- `component-refactoring-record.md` - 组件重构记录
- `fb-assets-conversion-record.md` - fb-assets 转换记录
- `fb-assets-migration-record.md` - fb-assets 迁移优化记录
- `form-designer-conversion-record.md` - form-designer 转换记录
- `main-exception-handler-optimization-record.md` - main.js 异常处理优化记录
- `project-routes-manifest.md` - 项目路由清单

### 修复脚本
- `fix-admin-base-image-imports.js` - 修复 admin-base 图片引用脚本
- `fix-admin-base-imports.js` - 修复 admin-base 导入路径脚本
- `fix-admin-base-less-imports.js` - 修复 admin-base Less 文件引用脚本
- `fix-admin-base-paths.js` - 修复 admin-base 路径脚本
- `fix-less-extensions.js` - 修复 Less 文件扩展名脚本

### 其他任务文件
- `toNext.js` - 项目迁移相关脚本

## 说明

这些文件记录了项目从原始结构重构为统一 `@fb` 目录结构的完整过程，包括：
1. 组件包的移动和重组
2. 路径引用的修正
3. 别名配置的优化
4. 各种导入路径的统一化

所有的修复脚本都是在重构过程中使用的自动化工具，用于批量处理文件路径和引用的修正。