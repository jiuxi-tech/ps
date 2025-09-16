# DDD迁移工具套件 v1.0

> 细粒度重构实施表 - 阶段1.3 迁移工具准备

## 概述

本工具套件用于支持Java项目向DDD（领域驱动设计）分层架构的重构迁移。提供包迁移、import更新、依赖检查等功能，确保重构过程的安全性和一致性。

## 工具列表

| 工具 | 脚本文件 | 功能描述 | 状态 |
|------|----------|----------|------|
| 包迁移脚本 | `package-migration-script.py` | 批量移动类文件，创建DDD目录结构 | ✅ 已完成 |
| Import更新工具 | `import-update-tool.py` | 自动更新import语句，修正包路径引用 | ✅ 已完成 |
| 依赖检查工具 | `dependency-checker.py` | 检测循环依赖和违规依赖，确保架构合规性 | ✅ 已完成 |
| 工具测试脚本 | `migration-tool-test.py` | 测试所有工具的功能正确性 | ✅ 已完成 |
| 统一执行脚本 | `run-migration.py` | 按顺序执行完整迁移流程 | ✅ 已完成 |

## 快速开始

### 方式一：一键迁移（推荐）

```bash
# 进入项目根目录
cd D:\keycloak_sb_sso_new0910_claude\ps\ps-be

# 执行完整迁移流程
python migration-tools/run-migration.py

# 跳过可选工具
python migration-tools/run-migration.py --skip-optional

# 不创建备份（不推荐）
python migration-tools/run-migration.py --no-backup
```

### 方式二：工具测试

```bash
# 安全模式测试（推荐）
python migration-tools/migration-tool-test.py

# 完整功能测试
python migration-tools/migration-tool-test.py --full
```

### 方式三：单独执行

```bash
# 1. 包迁移脚本
python migration-tools/package-migration-script.py

# 2. Import更新工具
python migration-tools/import-update-tool.py

# 3. 依赖检查工具
python migration-tools/dependency-checker.py
```

## 详细功能介绍

### 1. 包迁移脚本 (T1.3.1)

**功能**：
- 自动将 `interfaces` 目录重命名为 `intf`
- 创建标准DDD四层架构目录结构
- 生成package-info.java文件
- 清理空目录

**输出文件**：
- `migration.log` - 迁移日志
- `migration-report.json` - 详细迁移报告

**目标架构**：
```
module/
├── app/                    # 应用服务层
│   ├── service/
│   ├── command/handler/
│   ├── query/handler/
│   └── orchestrator/
├── domain/                 # 领域层
│   ├── model/aggregate/
│   ├── entity/
│   ├── valueobject/
│   ├── service/
│   └── repo/
├── infra/                  # 基础设施层
│   ├── persistence/
│   ├── gateway/
│   ├── cache/
│   └── messaging/
└── intf/                   # 接口适配器层
    ├── web/controller/
    ├── facade/
    └── event/
```

### 2. Import更新工具 (T1.3.2)

**功能**：
- 自动更新Java文件中的import语句
- 处理包路径变更（interfaces → intf等）
- 更新package声明
- 验证import有效性

**支持的路径转换**：
- `interfaces` → `intf`
- `repository` → `repo`
- `valueobject` → `vo`
- `infrastructure` → `infra`

**输出文件**：
- `import-update.log` - 更新日志
- `import-update-report.json` - 更新统计报告

### 3. 依赖检查工具 (T1.3.3)

**功能**：
- 检测循环依赖
- 验证DDD分层架构合规性
- 分析模块间依赖关系
- 生成依赖矩阵

**检查规则**：
- 依赖方向：intf → app → domain ← infra
- 禁止：domain层依赖infra层
- 禁止：下层依赖上层

**输出文件**：
- `dependency-check.log` - 检查日志
- `dependency-check-report.json` - 详细依赖分析报告

### 4. 工具测试脚本 (T1.3.4)

**功能**：
- 验证所有工具的正确性
- 检查工具脚本语法
- 执行干运行测试
- 生成测试报告

**测试类型**：
- 文件存在性检查
- 语法检查
- 干运行测试
- 完整功能测试（可选）

## 使用指南

### 前置条件

1. **Python环境**：Python 3.6+
2. **项目状态**：建议在干净的Git工作树中执行
3. **备份**：工具会自动创建备份，但建议额外备份

### 执行顺序

建议按以下顺序执行：

1. **测试工具** → 确保工具正常工作
2. **包迁移** → 创建DDD目录结构
3. **Import更新** → 修正包路径引用
4. **依赖检查** → 验证架构合规性

### 注意事项

⚠️ **重要提醒**：
- 执行前请确保项目已提交到版本控制系统
- 大型项目建议先在测试分支上验证
- 如遇到问题，可从backup目录恢复

### 常见问题

**Q: 工具执行失败怎么办？**
A: 查看对应的.log文件了解详细错误信息，必要时从backup目录恢复

**Q: 如何跳过某个工具？**
A: 使用run-migration.py的--skip-optional参数，或者单独执行需要的工具

**Q: 迁移后编译失败？**
A: 检查import-update-report.json中的invalid_imports，手动修正无效引用

**Q: 如何验证迁移结果？**
A: 运行依赖检查工具，查看dependency-check-report.json

## 输出文件说明

### 日志文件
- `*.log` - 详细执行日志，包含调试信息
- `migration-execution.log` - 统一执行脚本日志

### 报告文件
- `*-report.json` - JSON格式的详细报告
- `migration-execution-report.json` - 完整迁移执行报告

### 备份目录
- `backup/` - 自动创建的备份文件
- 按时间戳组织：`backup/module_20250916_143022/`

## 技术实现

### 架构原则
- DDD四层架构：intf → app → domain ← infra
- 依赖倒置：下层不依赖上层
- 模块边界：明确的模块职责分离

### 代码规范
- 包命名：全小写，使用标准DDD术语
- 目录结构：统一的层级组织
- Import路径：自动化的路径更新

## 版本历史

- **v1.0** (2025-09-16)
  - ✅ 包迁移脚本完成
  - ✅ Import更新工具完成  
  - ✅ 依赖检查工具完成
  - ✅ 工具测试脚本完成
  - ✅ 统一执行脚本完成

## 联系方式

如有问题或建议，请通过以下方式联系：
- 项目文档：查看refactor目录下的相关文档
- 错误报告：检查生成的日志文件
- 技术支持：参考role模块重构完成案例

---

**DDD重构工具** - 让架构重构更安全、更高效！