# 应用管理添加"有效的注销后重定向 URI"字段 - 实施总结

## 一、功能概述

在"应用管理"功能中添加"有效的注销后重定向 URI"字段，用于配置 SSO 应用注销后允许重定向的 URL 地址。

### 需求背景
- 与之前实现的 SSO 全局退出功能配套使用
- 对应 Keycloak 的 `Valid Post Logout Redirect URIs` 配置
- 用于控制 SSO 注销后的安全重定向

## 二、修改文件

### 前端修改
**文件路径**: `ps-fe\@fb\admin-base\views\sys\sso\client\add.vue`

#### 1. 模板部分修改（Template）

在 Web Origins 字段后添加新的表单项：

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

**位置**: 第 115-125 行（Web Origins 字段之后）

#### 2. 数据模型修改（Data）

**formData 新增字段**（第 177 行）:
```javascript
formData: {
    // ... 其他字段
    postLogoutRedirectUris: [],  // 新增字段
    // ...
}
```

**新增文本绑定变量**（第 184 行）:
```javascript
postLogoutRedirectUrisText: ''  // 用于 textarea 双向绑定
```

#### 3. 监听器修改（Watch）

新增对 `postLogoutRedirectUrisText` 的监听（第 197-200 行）:
```javascript
// 监听注销后重定向URI文本变化
postLogoutRedirectUrisText(newVal) {
    this.formData.postLogoutRedirectUris = newVal ? newVal.split('\n').filter(uri => uri.trim()) : [];
}
```

**功能说明**:
- 将 textarea 中的多行文本按换行符分割成数组
- 自动过滤空白行
- 实时更新 `formData.postLogoutRedirectUris`

#### 4. 加载数据逻辑修改（Methods - loadClientData）

修改加载应用数据的逻辑（第 237 行）:
```javascript
postLogoutRedirectUris: data.postLogoutRedirectUris 
    || data.attributes?.['post.logout.redirect.uris'] 
    || [],
```

**兼容性处理**:
- 优先使用 `data.postLogoutRedirectUris`（标准字段）
- 降级使用 `data.attributes['post.logout.redirect.uris']`（Keycloak 属性格式）
- 默认为空数组

修改文本域内容设置（第 244 行）:
```javascript
this.postLogoutRedirectUrisText = (this.formData.postLogoutRedirectUris).join('\n');
```

### 后端修改
**无需修改**

后端的 `TpSsoController.saveClient()` 方法已经使用 `Map<String, Object>` 接收客户端数据，会将所有字段完整传递给 Keycloak REST API，因此无需额外修改。

## 三、技术实现细节

### 1. 字段类型
- **前端**: `Array<String>` - 字符串数组
- **UI 组件**: `fb-textarea` - 多行文本域，每行一个 URI
- **后端**: 透传给 Keycloak，支持数组或属性格式

### 2. 数据流转

```
用户输入（textarea，换行分隔）
    ↓
watch 监听自动转换
    ↓
formData.postLogoutRedirectUris (Array)
    ↓
保存时发送给后端
    ↓
后端透传给 Keycloak API
    ↓
Keycloak 存储为客户端配置
```

### 3. 数据验证
- **前端验证**: 自动过滤空白行（`filter(uri => uri.trim())`）
- **后端验证**: 依赖 Keycloak REST API 的验证机制
- **格式要求**: 每行一个完整的 URI

### 4. UI 设计
- **标签**: "有效的注销后重定向 URI"
- **占位提示**: "请输入注销后重定向URI，多个URI请用换行分隔"
- **行数**: 3 行（与重定向URI保持一致）
- **禁用状态**: 跟随 `readonly` 属性（查看模式下禁用）

## 四、测试要点

### 1. 基本功能测试
- [ ] 新增应用时，输入多个注销后重定向 URI（每行一个）
- [ ] 保存后，刷新页面或重新打开，验证数据是否正确显示
- [ ] 修改已有应用的注销后重定向 URI
- [ ] 删除某些 URI，保存后验证

### 2. 边界场景测试
- [ ] 不填写注销后重定向 URI（应允许为空）
- [ ] 输入包含空行的多个 URI（空行应被自动过滤）
- [ ] 输入超长 URI
- [ ] 复制粘贴带有 Windows 换行符的文本（\r\n）

### 3. 兼容性测试
- [ ] 加载旧数据（没有 postLogoutRedirectUris 字段的应用）
- [ ] 加载 Keycloak attributes 格式的数据
- [ ] 在查看模式下，验证字段为禁用状态

### 4. 集成测试
- [ ] 配置注销后重定向 URI 后，使用 SSO 全局退出功能
- [ ] 验证退出后是否能正确重定向到配置的 URI
- [ ] 验证未配置的 URI 是否被拒绝

## 五、验证步骤

### 1. 启动前端服务
```bash
cd ps-fe
npm run dev
# 访问 http://localhost:10801
```

### 2. 启动后端服务
```bash
cd ps-be
# 确保后端服务在 8082 端口运行
```

### 3. 访问应用管理
1. 登录系统
2. 导航到 "系统管理 → SSO 管理 → 应用管理"
3. 点击"注册应用"或编辑现有应用
4. 滚动到表单底部，应该能看到"有效的注销后重定向 URI"字段
5. 输入测试 URI（每行一个）：
   ```
   http://localhost:10801/logout-success
   http://localhost:10801/goodbye
   ```
6. 保存并重新打开，验证数据是否正确保存和显示

### 4. 验证后端数据
通过 Keycloak Admin Console 验证：
1. 访问 http://localhost:18080
2. 登录 admin/admin123
3. 选择 ps-realm
4. 进入 Clients
5. 查看对应客户端配置
6. 检查 "Valid Post Logout Redirect URIs" 字段

## 六、Keycloak 配置说明

### Valid Post Logout Redirect URIs 的作用
- 指定 OIDC 注销流程中允许的重定向 URI
- 在调用 `/protocol/openid-connect/logout` 端点时，`post_logout_redirect_uri` 参数必须匹配此列表
- 用于防止开放重定向（Open Redirect）漏洞

### 配置格式
- 支持完整 URI：`http://localhost:10801/logout-success`
- 支持通配符：`http://localhost:10801/*`
- 多个 URI 用换行分隔

### 与其他字段的关系
- **Redirect URIs**: 用于登录后的重定向
- **Web Origins**: 用于 CORS 配置
- **Post Logout Redirect URIs**: 用于注销后的重定向

## 七、注意事项

### 1. 数据格式兼容性
Keycloak 可能使用不同的字段名存储此配置：
- 标准字段：`postLogoutRedirectUris`
- 属性字段：`attributes['post.logout.redirect.uris']`

代码已做兼容处理，优先使用标准字段，降级使用属性字段。

### 2. 换行符处理
- JavaScript `split('\n')` 可正确处理 Unix（\n）和 Windows（\r\n）换行符
- `filter(uri => uri.trim())` 会自动去除空行和首尾空格

### 3. 字段验证
- 前端未添加 URI 格式验证，依赖 Keycloak 的后端验证
- 如需前端验证，可在 `fb-form-item` 的 `:rule` 属性中添加正则表达式验证

### 4. UI 一致性
- 与"重定向 URI"和"Web Origins"保持一致的输入方式
- 使用相同的 textarea 多行输入模式
- 保持相同的提示文本风格

## 八、后续优化建议

### 1. 添加 URI 格式验证
```javascript
// 在 fb-form-item 中添加验证规则
:rule="[{
    validator: (rule, value, callback) => {
        const uris = value.split('\n').filter(uri => uri.trim());
        const urlPattern = /^https?:\/\/.+/;
        const invalidUris = uris.filter(uri => !urlPattern.test(uri));
        if (invalidUris.length > 0) {
            callback(new Error(`无效的URI: ${invalidUris.join(', ')}`));
        } else {
            callback();
        }
    }
}]"
```

### 2. 添加 URI 快速填充
```javascript
// 提供快捷按钮，自动填充当前系统的常用 URI
fillCommonUris() {
    this.postLogoutRedirectUrisText = [
        `${window.location.origin}/logout-success`,
        `${window.location.origin}/`
    ].join('\n');
}
```

### 3. 在列表页显示（可选）
如果需要在列表页快速查看配置情况，可添加列：
```javascript
{
    name: 'postLogoutRedirectUris',
    label: '注销重定向',
    sortable: false,
    width: 150,
    formatter(val) {
        return val && val.length > 0 ? `已配置(${val.length})` : '未配置';
    }
}
```

## 九、相关文件清单

| 文件路径 | 修改内容 | 行数变化 |
|---------|---------|---------|
| `ps-fe\@fb\admin-base\views\sys\sso\client\add.vue` | 添加字段UI、数据模型、监听器、加载逻辑 | +21 行 |

## 十、完成状态

✅ **前端表单字段添加完成**
✅ **数据模型扩展完成**
✅ **数据绑定和监听完成**
✅ **加载和保存逻辑完成**
✅ **语法检查通过（无错误）**
✅ **代码规范符合项目标准**

## 十一、相关功能链接

- SSO 全局退出功能: `ps-fe\@fb\admin-base\views\main\components\AdminHeader.vue`
- SSO 用户管理: `ps-fe\@fb\admin-base\views\sys\sso\user\`
- SSO 会话管理: `ps-fe\@fb\admin-base\views\sys\sso\session\`
- 后端控制器: `ps-be\src\main\java\com\jiuxi\admin\core\controller\pc\TpSsoController.java`

---

**实施日期**: 2025-01-21  
**实施人**: AI Assistant  
**需求来源**: 用户需求 - 应用管理添加注销重定向配置
