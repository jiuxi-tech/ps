# PS-BMP 编码规范索引

## 📋 规范体系总览

本项目建立了完善的编码规范体系，包括前端、后端、数据标准和API契约四大模块。

---

## 🚀 快速访问

### ⭐ 新手必读
- **[快速上手指南](coding-standards/QUICK_START.md)** - 5分钟快速了解核心规范
- **[规范体系总索引](coding-standards/README.md)** - 完整规范体系导航

### 🎯 按角色查看

**🎨 前端开发人员**:
1. [前端编码规范总览](coding-standards/frontend/overview.md)
2. [tp-components业务组件库](coding-standards/frontend/tp-components-guide.md)
3. [核心实体定义](coding-standards/data-standards/core-entities.md)
4. [日期时间格式规范](coding-standards/data-standards/date-time-format.md)
5. [请求响应规范](coding-standards/api-contracts/request-response.md)

**⚙️ 后端开发人员**:
1. [后端编码规范总览](coding-standards/backend/overview.md)
2. [核心实体定义](coding-standards/data-standards/core-entities.md)
3. [日期时间格式规范](coding-standards/data-standards/date-time-format.md)
4. [请求响应规范](coding-standards/api-contracts/request-response.md)

**🤖 AI Agent**:
1. [快速上手指南](coding-standards/QUICK_START.md) - 了解规范结构
2. [请求响应规范](coding-standards/api-contracts/request-response.md) - 前后端通信标准
3. [核心实体定义](coding-standards/data-standards/core-entities.md) - 数据字典
4. 根据任务类型查看对应的前端或后端规范

---

## 📚 规范文档列表

### 1️⃣ 前端编码规范

**主文档**: [frontend/overview.md](coding-standards/frontend/overview.md)

**包含内容**:
- fb-ui组件库使用规范（布局、表单、数据展示、页面模板）
- tp-components业务组件库 👉 [详细文档](coding-standards/frontend/tp-components-guide.md)
- 页面模板标准（list.vue、add.vue、view.vue）
- Service层编码规范（请求方式、超时配置）
- 数据交互规范（响应处理、日期转换）
- 常见易错点与解决方案
- 最佳实践检查清单

**核心要点**:
- ✅ 栅格布局：24列，常用比例8-8-8、12-12、16-8
- ✅ 表格操作：`doSearch()`（新增后）、`doReload()`（修改后）
- ✅ API路径：必须包含`/sys`前缀
- ✅ 表单验证：保存前调用`validate()`
- ✅ 日期处理：提交时转为`YYYYMMDDHHmmss`，显示时转为`YYYY-MM-DD HH:mm:ss`

### 2️⃣ 后端编码规范

**主文档**: [backend/overview.md](coding-standards/backend/overview.md)

**包含内容**:
- Controller层规范（路径前缀、请求方式、参数注解）
- Service层规范（接口定义、实现类、事务管理）
- Mapper层规范（接口定义、XML配置、SQL编写）
- 实体和DTO规范
- 异常处理规范
- 开发检查清单

**核心要点**:
- ✅ 路径前缀：系统管理类接口必须包含`/sys`
- ✅ 参数绑定：表单请求不用`@RequestBody`，JSON请求必须用
- ✅ 响应格式：统一使用`Result<T>`（code/message/data）
- ✅ 事务管理：Service层方法添加`@Transactional`
- ✅ XML路径：Mapper XML文件存放在`mapper/admin/`

### 3️⃣ 数据标准

#### 📊 核心实体定义
**文档**: [data-standards/core-entities.md](coding-standards/data-standards/core-entities.md)

**包含实体**:
1. **人员（Person）** - ⚠️ 表名：`tp_person_basicinfo`
2. **部门（Department）** - 表名：`tp_dept`
3. **单位/组织（Organization）** - 表名：`tp_ascription`
4. **角色（Role）** - 表名：`tp_role`
5. **第三方应用（ThirdPartyApp）** - 表名：`tp_third_party_app`

**核心要点**:
- ✅ 人员表：必须使用`tp_person_basicinfo`（不是`tp_person`）
- ✅ 字段映射：数据库（snake_case）↔ 后端Java（camelCase）↔ 前端JS（camelCase）
- ✅ 标准字段：所有实体包含`passKey`、`logDelete`、`createTime`等
- ✅ 关联字段：查询时需要关联的字段（如`deptName`、`ascnName`）

#### ⏰ 日期时间格式
**文档**: [data-standards/date-time-format.md](coding-standards/data-standards/date-time-format.md)

**格式标准**:
| 层次 | 格式 | 示例 |
|------|------|------|
| 数据库存储 | `VARCHAR(14)` | `20241201153045` |
| 后端传输 | `String` | `"20241201153045"` |
| 前端显示 | `string` | `"2024-12-01 15:30:45"` |
| 前端编辑 | `Date` | `new Date()` |

**核心要点**:
- ✅ 存储格式：`YYYYMMDDHHmmss`（14位数字字符串）
- ✅ 显示格式：`YYYY-MM-DD HH:mm:ss`
- ✅ 前端工具：使用`dayjs`处理日期
- ✅ 转换规则：提交时转存储格式，回显时转Date对象

### 4️⃣ API接口契约

#### 🔌 请求响应规范
**文档**: [api-contracts/request-response.md](coding-standards/api-contracts/request-response.md)

**包含内容**:
- 请求路径规范（前缀规则、命名规范）
- 请求方式（GET、POST表单、POST JSON、文件上传）
- 响应格式（成功响应、失败响应、状态码）
- 数据类型映射（基础类型、特殊类型）
- 错误处理规范
- 常见通信场景（查询、新增、修改、删除）
- 文件上传下载

**核心要点**:
- ✅ 路径前缀：系统管理类`/sys/`、认证类`/auth/`、业务类`/biz/`
- ✅ GET请求：查询、删除，参数通过Query String
- ✅ POST表单：Content-Type `application/x-www-form-urlencoded`，不用`@RequestBody`
- ✅ POST JSON：Content-Type `application/json`，必须用`@RequestBody`
- ✅ 响应格式：`{code: 1, message: "成功", data: {...}}`
- ✅ 状态码：1成功，400参数错误，401未认证，403无权限，500服务器错误

---

## ⚠️ 十大常见错误

### 1. API路径缺少前缀 ❌
```javascript
app.service.get('/third-party-app/list')  // 错误
app.service.get('/sys/third-party-app/list')  // ✅ 正确
```

### 2. 人员表名错误 ❌
```sql
SELECT * FROM tp_person  -- 错误
SELECT * FROM tp_person_basicinfo  -- ✅ 正确
```

### 3. 日期格式未转换 ❌
```javascript
formData.expireTime = new Date()  // 错误
formData.expireTime = dayjs(new Date()).format('YYYYMMDDHHmmss')  // ✅ 正确
```

### 4. 表单请求用了@RequestBody ❌
```java
@PostMapping("/add")
public Result add(@RequestBody ThirdPartyAppDTO dto) {  // 错误
public Result add(ThirdPartyAppDTO dto) {  // ✅ 正确
```

### 5. JSON请求未用@RequestBody ❌
```java
@PostMapping("/config")
public Result config(ApiPermissionDTO dto) {  // 错误
public Result config(@RequestBody ApiPermissionDTO dto) {  // ✅ 正确
```

### 6. 列表刷新方式错误 ❌
```javascript
// 新增后应该回到第一页
closeDialog(result) {
  this.$refs.table.doReload()  // 错误
  this.$refs.table.doSearch()  // ✅ 正确
}
```

### 7. 忘记表单验证 ❌
```javascript
save() {
  this.service.add(this.formData)  // 错误
  this.$refs.fbform.validate((result) => {  // ✅ 正确
    if (result === true) {
      this.service.add(this.formData)
    }
  })
}
```

### 8. 字段名不一致 ❌
```javascript
// 前端
formData.person_id = 'P001'  // 错误
formData.personId = 'P001'  // ✅ 正确

// 后端
String person_id;  // 错误
String personId;  // ✅ 正确
```

### 9. 删除操作无二次确认 ❌
```javascript
handleDel(row) {
  this.delete(row.appId)  // 错误
  this.$confirm('确定删除？', () => {  // ✅ 正确
    this.delete(row.appId)
  })
}
```

### 10. Mapper XML路径错误 ❌
```
src/main/resources/mapper/sys/XXXMapper.xml  # 错误
src/main/resources/mapper/admin/XXXMapper.xml  # ✅ 正确
```

---

## 🎯 开发工作流

### 前端开发流程
1. **需求分析** → 确定要开发list/add/view页面
2. **查看规范** → 阅读[前端规范](coding-standards/frontend/overview.md)
3. **查看实体** → 查询[核心实体定义](coding-standards/data-standards/core-entities.md)获取字段信息
4. **开发页面** → 使用规范中的模板代码
5. **自查代码** → 使用检查清单验证
6. **联调测试** → 参考[请求响应规范](coding-standards/api-contracts/request-response.md)

### 后端开发流程
1. **需求分析** → 确定要开发的接口
2. **查看规范** → 阅读[后端规范](coding-standards/backend/overview.md)
3. **查看实体** → 查询[核心实体定义](coding-standards/data-standards/core-entities.md)获取表结构
4. **开发接口** → Controller → Service → Mapper → XML
5. **自查代码** → 使用检查清单验证
6. **联调测试** → 参考[请求响应规范](coding-standards/api-contracts/request-response.md)

---

## 📖 学习路径

### 🚀 快速入门（30分钟）
1. [快速上手指南](coding-standards/QUICK_START.md) - 5分钟
2. [请求响应规范](coding-standards/api-contracts/request-response.md) - 15分钟
3. [核心实体定义](coding-standards/data-standards/core-entities.md) - 10分钟

### 📚 深入学习（前端方向，2小时）
1. [前端编码规范总览](coding-standards/frontend/overview.md) - 60分钟
2. [日期时间格式规范](coding-standards/data-standards/date-time-format.md) - 30分钟
3. [请求响应规范](coding-standards/api-contracts/request-response.md) - 30分钟

### 📚 深入学习（后端方向，2小时）
1. [后端编码规范总览](coding-standards/backend/overview.md) - 60分钟
2. [核心实体定义](coding-standards/data-standards/core-entities.md) - 30分钟
3. [请求响应规范](coding-standards/api-contracts/request-response.md) - 30分钟

---

## 🔍 快速查询表

### 问：人员表叫什么？
**答**：`tp_person_basicinfo`（不是tp_person）
**详见**：[核心实体-人员](coding-standards/data-standards/core-entities.md#1-人员person实体)

### 问：日期用什么格式？
**答**：存储`YYYYMMDDHHmmss`，显示`YYYY-MM-DD HH:mm:ss`
**详见**：[日期时间格式](coding-standards/data-standards/date-time-format.md)

### 问：API路径需要什么前缀？
**答**：系统管理类必须包含`/sys/`
**详见**：[请求响应-路径前缀](coding-standards/api-contracts/request-response.md#11-请求路径规范)

### 问：表单请求要用@RequestBody吗？
**答**：不要，直接对象绑定
**详见**：[请求响应-POST表单](coding-standards/api-contracts/request-response.md#post请求表单格式)

### 问：JSON请求要用@RequestBody吗？
**答**：必须使用
**详见**：[请求响应-POST JSON](coding-standards/api-contracts/request-response.md#post请求json格式)

### 问：新增后列表怎么刷新？
**答**：`this.$refs.table.doSearch()`（回第一页）
**详见**：[前端规范-列表刷新](coding-standards/frontend/overview.md#71-列表刷新问题)

### 问：修改后列表怎么刷新？
**答**：`this.$refs.table.doReload()`（刷新当前页）
**详见**：[前端规范-列表刷新](coding-standards/frontend/overview.md#71-列表刷新问题)

### 问：Mapper XML放哪里？
**答**：`src/main/resources/mapper/admin/`
**详见**：[后端规范-Mapper层](coding-standards/backend/overview.md#42-mapper-xml)

---

## 💡 使用建议

### 对于开发人员
1. ⭐ **收藏本文档**，作为日常开发参考
2. 📖 **新功能开发前**查看对应规范
3. ✅ **代码提交前**使用检查清单自查
4. 🔍 **遇到问题时**先查"常见错误"章节

### 对于AI Agent
1. 📖 **开发前**先阅读对应的规范文档
2. 📋 **使用**规范中的代码模板和示例
3. ✅ **验证**生成的代码是否符合规范
4. 🔍 **查看**"常见错误"避免重复问题

### 对于Code Review
1. 📋 使用规范中的检查清单
2. 🔍 重点关注"十大常见错误"
3. ✅ 验证字段命名、日期格式、API路径
4. 📊 检查是否使用了正确的表名

---

## 📞 反馈与改进

发现问题或有改进建议？
1. 在团队沟通渠道提出
2. 提供具体问题描述和改进建议
3. 附上代码示例或参考链接

---

## 📝 版本信息

- **版本号**: v1.0.0
- **创建日期**: 2025年12月1日
- **最后更新**: 2025年12月1日
- **维护团队**: PS-BMP开发团队

---

**祝编码愉快！** 🎉

