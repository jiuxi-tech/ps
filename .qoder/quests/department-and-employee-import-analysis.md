# 部门与人员导入功能设计分析

## 1. 概述

本文档旨在分析现有数据库结构与导入需求之间的匹配度，并制定相应的开发调整方案。我们将重点关注如何在遵循开闭原则的前提下，实现部门与人员数据的高效导入。

## 2. 数据库连接信息

根据项目配置文件分析，获取到以下数据库连接信息：

- **数据库URL**: `jdbc:mariadb://alilaoba.cn:13307/ps-bmp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai`
- **用户名**: `root`
- **密码**: `$D8BZ8Qmav`

## 3. 现有表结构分析

### 3.1 部门表(tp_dept_basicinfo)

通过分析数据库表结构，我们得到以下关键信息：

```sql
CREATE TABLE tp_dept_basicinfo (
    dept_id         VARCHAR(50)  PRIMARY KEY,    -- 部门ID
    pdept_id        VARCHAR(50),                 -- 父部门ID
    dept_levelcode  VARCHAR(100),                -- 部门层级编码
    dept_no         VARCHAR(50),                 -- 部门编号
    dept_full_name  VARCHAR(200) NOT NULL,       -- 部门全称
    dept_simple_name VARCHAR(100),               -- 部门简称
    dept_type       VARCHAR(20),                 -- 部门类型
    dept_desc       VARCHAR(500),                -- 部门描述
    order_index     DECIMAL(10,2),               -- 排序索引
    category        INTEGER,                     -- 部门类别(0政府 1企业)
    city_code       VARCHAR(20),                 -- 行政区划代码
    principal_name  VARCHAR(50),                 -- 负责人姓名
    principal_tel   VARCHAR(20),                 -- 负责人电话
    ascn_id         VARCHAR(50),                 -- 所属机构ID
    leaf            INTEGER,                     -- 是否叶子节点
    enabled         INTEGER,                     -- 是否启用
    actived         INTEGER,                     -- 是否有效
    creator         VARCHAR(50),                 -- 创建人
    create_time     DATETIME,                    -- 创建时间
    updator         VARCHAR(50),                 -- 更新人
    update_time     DATETIME,                    -- 更新时间
    tenant_id       VARCHAR(50),                 -- 租户ID
    extend01        VARCHAR(100),                -- 扩展字段01(存储左值)
    extend02        VARCHAR(100),                -- 扩展字段02(存储右值)
    extend03        VARCHAR(100),                -- 扩展字段03
    extend04        VARCHAR(100),                -- 扩展字段04
    extend05        VARCHAR(100)                 -- 扩展字段05
);
```

此外，根据更新脚本，表中已添加以下新字段：
- `FULL_DEPT_CODE` VARCHAR(500) DEFAULT '' COMMENT '部门全路径编码'
- `FULL_DEPT_NAME` VARCHAR(500) DEFAULT '' COMMENT '部门全路径名称'
- `ASSISTANT_NOTE` VARCHAR(500) NULL COMMENT '协办人简单文本'

### 3.2 人员表(tp_person_basicinfo)

通过分析数据库表结构，我们得到以下关键信息：

```sql
-- 部分关键字段展示
CREATE TABLE tp_person_basicinfo (
    person_id       VARCHAR(50)  PRIMARY KEY,    -- 人员ID
    person_name     VARCHAR(100) NOT NULL,       -- 人员姓名
    profile_photo   VARCHAR(200),                -- 头像地址
    person_no       VARCHAR(50),                 -- 人员编号
    sex             INTEGER,                     -- 性别
    idtype          VARCHAR(20),                 -- 证照类型
    idcard          VARCHAR(50),                 -- 证照值
    native_place    VARCHAR(20),                 -- 籍贯
    safeprin_nation VARCHAR(20),                 -- 民族
    resume          TEXT,                        -- 个人简介
    birthday        VARCHAR(10),                 -- 出生年月
    phone           VARCHAR(50),                 -- 手机号码
    tel             VARCHAR(20),                 -- 电话号码
    email           VARCHAR(100),                -- 邮箱
    office          VARCHAR(100),                -- 办公室
    actived         INTEGER,                     -- 是否有效
    category        VARCHAR(20),                 -- 类别
    ascn_id         VARCHAR(50),                 -- 所属机构ID
    creator         VARCHAR(50),                 -- 创建人
    create_time     DATETIME,                    -- 创建时间
    updator         VARCHAR(50),                 -- 更新人
    update_time     DATETIME,                    -- 更新时间
    tenant_id       VARCHAR(50),                 -- 租户ID
    extend01        VARCHAR(100),                -- 扩展字段01
    extend02        VARCHAR(100),                -- 扩展字段02
    extend03        VARCHAR(100)                 -- 扩展字段03
);
```

此外，根据更新脚本，表中已添加以下新字段：
- `EMP_STATUS` VARCHAR(20) DEFAULT '在职' COMMENT '人员状态 在职/离职'

## 4. 导入需求分析

### 4.1 部门导入需求

根据提供的部门导入模板和说明，我们需要支持以下功能：

1. 部门层级关系通过">"字符分隔
2. 部门全路径字段(`FULL_DEPT_CODE`, `FULL_DEPT_NAME`)的计算与存储
3. 部门协办人信息存储在`ASSISTANT_NOTE`字段中
4. 支持自动生成部门编号
5. 支持同层级部门显示顺序自动计算

### 4.2 人员导入需求

根据提供的人員导入模板和说明，我们需要支持以下功能：

1. 人员状态字段(`EMP_STATUS`)的处理，并与`ACTIVED`字段联动
2. 部门信息通过">"字符分隔的全路径匹配
3. 支持多种字段类型的导入格式（数字、关联类型、check类型、下拉框等）
4. 支持复杂字段格式（人员范围选择、人员组织多选等）

## 5. 现有结构与需求匹配度分析

### 5.1 部门导入匹配度分析

| 需求项 | 现有结构支持 | 差异分析 |
|--------|-------------|----------|
| 层级分隔符">" | 支持，通过`dept_levelcode`实现树形结构 | 需要解析导入数据中的">"符号并转换为树形结构 |
| 全路径字段 | 已新增`FULL_DEPT_CODE`和`FULL_DEPT_NAME`字段 | 需要在导入过程中计算并填充这两个字段 |
| 协办人信息 | 已新增`ASSISTANT_NOTE`字段 | 可直接存储导入的协办人信息 |
| 自动生成编号 | 现有`dept_no`字段 | 需要结合系统参数`dept.no.auto.enabled`实现 |
| 显示顺序 | 现有`order_index`字段 | 需要实现同层级最小值+1的逻辑 |

### 5.2 人员导入匹配度分析

| 需求项 | 现有结构支持 | 差异分析 |
|--------|-------------|----------|
| 人员状态 | 已新增`EMP_STATUS`字段 | 需要实现与`ACTIVED`字段的联动逻辑 |
| 部门层级 | 通过`tp_person_dept`表关联 | 需要解析导入数据中的">"符号并匹配到具体部门 |
| 多种字段类型 | 部分支持，依赖于具体字段实现 | 需要开发通用的字段解析器 |
| 复杂字段格式 | 需要专门处理 | 需要实现特定的解析逻辑 |

## 6. 开发调整方案

### 6.1 部门导入调整方案

1. **实体类更新**：
   - 在`DepartmentPO`类中增加`fullDeptCode`, `fullDeptName`, `assistantNote`字段
   - 更新对应的Mapper XML文件，添加新字段的映射

2. **服务层逻辑**：
   - 实现部门层级解析逻辑，将">"分隔的路径转换为树形结构
   - 实现全路径字段计算逻辑，在部门保存时自动填充`FULL_DEPT_CODE`和`FULL_DEPT_NAME`
   - 实现显示顺序自动计算逻辑
   - 实现部门编号自动生成逻辑

3. **导入适配层**：
   - 修改部门导入处理器，支持新字段的导入
   - 实现层级路径解析与匹配逻辑

### 6.2 人员导入调整方案

1. **实体类更新**：
   - 在`UserPO`类中增加`empStatus`字段
   - 更新对应的Mapper XML文件，添加新字段的映射

2. **服务层逻辑**：
   - 实现人员状态与`ACTIVED`字段的联动逻辑
   - 实现部门层级解析逻辑，支持通过">"分隔的路径匹配部门
   - 实现各种字段类型的解析逻辑

3. **导入适配层**：
   - 修改人员导入处理器，支持新字段的导入
   - 实现复杂字段格式的解析逻辑
   - 实现部门路径匹配逻辑

## 7. 风险与注意事项

1. **数据一致性**：在导入过程中需要确保数据的一致性，特别是在处理部门层级关系时
2. **性能考虑**：对于大量数据的导入，需要考虑性能优化，避免长时间阻塞
3. **错误处理**：需要完善的错误处理机制，能够准确定位导入失败的原因并提供友好的错误提示
4. **兼容性**：新功能需要与现有系统功能保持兼容，避免影响已有业务流程

## 8. 后续步骤

1. 冻结字段定义与索引方案
2. 编写数据回填与联动策略（服务层实现）
3. 更新实体与Mapper（部门/人员）
4. 更新导入适配层（层级解析、路径计算、状态联动）
5. 小样本联调与失败 Excel 回写
6. 文档与运维脚本交付