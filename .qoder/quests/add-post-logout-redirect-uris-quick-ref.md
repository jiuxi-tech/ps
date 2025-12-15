# 应用管理添加"有效的注销后重定向 URI"字段 - 快速参考

## 📋 修改概览

### ✅ 完成状态
- **修改文件数**: 1 个
- **新增代码行**: 21 行
- **语法检查**: 通过 ✓
- **编译状态**: 无错误 ✓

---

## 🎯 核心修改

### 文件: `ps-fe\@fb\admin-base\views\sys\sso\client\add.vue`

#### 1️⃣ 新增表单字段（第 115-125 行）
```vue
<fb-row>
    <fb-col span="24">
        <fb-form-item label="有效的注销后重定向 URI" prop="postLogoutRedirectUris">
            <fb-textarea rows="3" v-model="postLogoutRedirectUrisText"
                         type="text"
                         placeholder="请输入注销后重定向URI，多个URI请用换行分隔"
                         :disabled="readonly">
            </fb-textarea>
        </fb-form-item>
    </fb-col>
</fb-row>
```

#### 2️⃣ 数据模型（第 189、196 行）
```javascript
formData: {
    postLogoutRedirectUris: [],  // 新增
}

postLogoutRedirectUrisText: ''  // 新增
```

#### 3️⃣ 监听器（第 209-212 行）
```javascript
postLogoutRedirectUrisText(newVal) {
    this.formData.postLogoutRedirectUris = newVal ? newVal.split('\n').filter(uri => uri.trim()) : [];
}
```

#### 4️⃣ 数据加载（第 249、256 行）
```javascript
// 加载时兼容处理
postLogoutRedirectUris: data.postLogoutRedirectUris 
    || data.attributes?.['post.logout.redirect.uris'] 
    || [],

// 显示时
this.postLogoutRedirectUrisText = (this.formData.postLogoutRedirectUris).join('\n');
```

---

## 🔑 关键特性

### 输入方式
- **组件**: `fb-textarea`（多行文本框）
- **行数**: 3 行
- **格式**: 每行一个 URI

### 数据处理
- ✅ 自动过滤空行
- ✅ 自动去除首尾空格
- ✅ 支持 Windows/Unix 换行符
- ✅ 实时转换为数组

### 兼容性
- ✅ 支持 Keycloak 标准字段
- ✅ 支持 Keycloak attributes 格式
- ✅ 空值兼容
- ✅ 旧数据兼容

---

## 📝 使用示例

### 输入示例
```
http://localhost:10801/logout-success
http://localhost:10801/goodbye
http://localhost:10801/
```

### 保存后数组格式
```javascript
postLogoutRedirectUris: [
    "http://localhost:10801/logout-success",
    "http://localhost:10801/goodbye",
    "http://localhost:10801/"
]
```

---

## 🧪 快速测试步骤

1. **启动服务**
   ```bash
   cd ps-fe && npm run dev
   # 访问 http://localhost:10801
   ```

2. **打开应用管理**
   - 系统管理 → SSO 管理 → 应用管理

3. **新增或编辑应用**
   - 滚动到底部
   - 找到"有效的注销后重定向 URI"字段

4. **输入测试数据**
   ```
   http://localhost:10801/logout
   http://localhost:10801/goodbye
   ```

5. **保存并验证**
   - 点击保存
   - 重新打开，检查数据是否正确

---

## 📚 相关文档

- 📄 [完整实施总结](./add-post-logout-redirect-uris-summary.md)
- 📋 [详细测试指南](./add-post-logout-redirect-uris-test-guide.md)

---

## 🔗 相关功能

- **SSO 全局退出**: `AdminHeader.vue` - 退出后可重定向到配置的 URI
- **Keycloak 配置**: Admin Console → Clients → Valid Post Logout Redirect URIs
- **后端 API**: `/sso/admin/client/save` - 透传所有字段给 Keycloak

---

## ⚠️ 注意事项

1. **URI 格式**: 必须包含协议（http:// 或 https://）
2. **空行处理**: 自动过滤，无需手动清理
3. **查看模式**: 字段自动禁用，不可编辑
4. **后端兼容**: 无需修改后端代码，自动透传

---

## 🎨 UI 效果

```
┌─────────────────────────────────────────────┐
│ 有效的注销后重定向 URI                      │
├─────────────────────────────────────────────┤
│ http://localhost:10801/logout-success       │
│ http://localhost:10801/goodbye              │
│ http://localhost:10801/                     │
└─────────────────────────────────────────────┘
  请输入注销后重定向URI，多个URI请用换行分隔
```

---

**实施日期**: 2025-01-21  
**状态**: ✅ 完成并验证通过
