# 开放API接口文档

## 1. 概述

### 1.1 文档说明
本文档描述了业务中台系统对外提供的开放API接口，第三方应用可以通过这些接口访问系统数据。

### 1.2 版本信息
- **API版本**: v1
- **文档版本**: 1.0.0
- **更新日期**: 2025-11-30

### 1.3 基本信息
- **基础路径**: `/ps-be/open-api/v1`
- **协议**: HTTP/HTTPS
- **数据格式**: JSON
- **字符编码**: UTF-8

## 2. 认证方式

### 2.1 API Key认证

所有开放API接口都需要通过API Key进行身份认证。

#### 请求头要求

```
Authorization: {your-api-key}
```

#### 示例

```bash
curl --location 'http://localhost:10801/ps-be/open-api/v1/users' \
--header 'Authorization: b4a4b6a002b64502b2cc715a6ddec8cc'
```

### 2.2 认证失败响应

当API Key无效或缺失时，系统返回：

```json
{
  "code": "401",
  "message": "未授权访问",
  "data": null
}
```

## 3. 通用响应格式

### 3.1 成功响应

```json
{
  "code": "1",
  "message": "操作成功",
  "data": {
    // 业务数据
  }
}
```

### 3.2 失败响应

```json
{
  "code": "-1",
  "message": "错误描述信息",
  "data": null
}
```

### 3.3 响应码说明

| 响应码 | 说明 |
|--------|------|
| 1 | 成功 |
| -1 | 业务失败 |
| 401 | 未授权（API Key无效） |
| 403 | 禁止访问（IP不在白名单或应用已禁用） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 4. 用户信息接口

### 4.1 查询单个用户信息

#### 接口描述
根据人员ID查询单个用户的详细信息，返回的数据已进行脱敏处理。

#### 接口地址
```
GET /open-api/v1/users/{personId}
```

#### 请求参数

**路径参数**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| personId | String | 是 | 人员ID |

#### 请求示例

```bash
curl --location 'http://localhost:10801/ps-be/open-api/v1/users/1234567890123456789' \
--header 'Authorization: b4a4b6a002b64502b2cc715a6ddec8cc'
```

#### 响应示例

**成功响应**

```json
{
  "code": "1",
  "message": "操作成功",
  "data": {
    "personId": "1234567890123456789",
    "personName": "张**",
    "personNo": "****5678",
    "sex": 1,
    "sexName": "男",
    "phone": "138****5678",
    "email": "zha***@example.com",
    "office": "主任",
    "deptId": "9876543210987654321",
    "deptName": "办公室",
    "actived": 1,
    "createTime": "20250101120000"
  }
}
```

**失败响应**

```json
{
  "code": "-1",
  "message": "用户不存在",
  "data": null
}
```

#### 响应字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| personId | String | 人员ID |
| personName | String | 人员姓名（已脱敏，如：张**） |
| personNo | String | 人员编号（已脱敏，如：****5678） |
| sex | Integer | 性别：1-男，2-女 |
| sexName | String | 性别名称：男、女 |
| phone | String | 手机号码（已脱敏，如：138****5678） |
| email | String | 电子邮箱（已脱敏，如：zha***@example.com） |
| office | String | 职位 |
| deptId | String | 部门ID |
| deptName | String | 部门名称 |
| actived | Integer | 状态：1-有效，0-无效 |
| createTime | String | 创建时间（格式：YYYYMMDDHHmmss） |

---

### 4.2 分页查询用户列表

#### 接口描述
分页查询用户列表，可选择按部门筛选，返回的数据已进行脱敏处理。

#### 接口地址
```
GET /open-api/v1/users
```

#### 请求参数

**Query参数**

| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| deptId | String | 否 | - | 部门ID，不传则查询所有部门 |
| page | Integer | 否 | 1 | 页码，从1开始 |
| size | Integer | 否 | 20 | 每页条数，最大100 |

#### 请求示例

```bash
# 查询所有用户（第1页，每页20条）
curl --location 'http://localhost:10801/ps-be/open-api/v1/users' \
--header 'Authorization: b4a4b6a002b64502b2cc715a6ddec8cc'

# 查询指定部门用户（第2页，每页50条）
curl --location 'http://localhost:10801/ps-be/open-api/v1/users?deptId=9876543210987654321&page=2&size=50' \
--header 'Authorization: b4a4b6a002b64502b2cc715a6ddec8cc'
```

#### 响应示例

```json
{
  "code": "1",
  "message": "操作成功",
  "data": {
    "total": 150,
    "page": 1,
    "size": 20,
    "records": [
      {
        "personId": "1234567890123456789",
        "personName": "张**",
        "personNo": "****5678",
        "sex": 1,
        "sexName": "男",
        "phone": "138****5678",
        "email": "zha***@example.com",
        "office": "主任",
        "deptId": "9876543210987654321",
        "deptName": "办公室",
        "actived": 1,
        "createTime": "20250101120000"
      },
      {
        "personId": "1234567890123456790",
        "personName": "李**",
        "personNo": "****5679",
        "sex": 2,
        "sexName": "女",
        "phone": "139****5679",
        "email": "li***@example.com",
        "office": "副主任",
        "deptId": "9876543210987654321",
        "deptName": "办公室",
        "actived": 1,
        "createTime": "20250102120000"
      }
    ]
  }
}
```

#### 响应字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| total | Integer | 总记录数 |
| page | Integer | 当前页码 |
| size | Integer | 每页条数 |
| records | Array | 用户记录列表 |

records数组中每个元素的字段说明参考 [4.1 响应字段说明](#响应字段说明)

---

### 4.3 搜索用户

#### 接口描述
根据关键词搜索用户，支持按姓名、工号模糊查询，可选择按部门筛选。

#### 接口地址
```
POST /open-api/v1/users/search
```

#### 请求参数

**Body参数（JSON格式）**

| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| keyword | String | 是 | - | 搜索关键词（姓名、工号） |
| deptId | String | 否 | - | 部门ID，不传则搜索所有部门 |
| page | Integer | 否 | 1 | 页码，从1开始 |
| size | Integer | 否 | 20 | 每页条数，最大100 |

#### 请求示例

```bash
curl --location 'http://localhost:10801/ps-be/open-api/v1/users/search' \
--header 'Authorization: b4a4b6a002b64502b2cc715a6ddec8cc' \
--header 'Content-Type: application/json' \
--data '{
  "keyword": "张三",
  "deptId": "9876543210987654321",
  "page": 1,
  "size": 20
}'
```

#### 响应示例

```json
{
  "code": "1",
  "message": "操作成功",
  "data": {
    "total": 5,
    "page": 1,
    "size": 20,
    "records": [
      {
        "personId": "1234567890123456789",
        "personName": "张**",
        "personNo": "****5678",
        "sex": 1,
        "sexName": "男",
        "phone": "138****5678",
        "email": "zha***@example.com",
        "office": "主任",
        "deptId": "9876543210987654321",
        "deptName": "办公室",
        "actived": 1,
        "createTime": "20250101120000"
      }
    ]
  }
}
```

**失败响应**

```json
{
  "code": "-1",
  "message": "搜索关键词不能为空",
  "data": null
}
```

#### 响应字段说明

响应格式与 [4.2 分页查询用户列表](#42-分页查询用户列表) 相同。

---

## 5. 数据脱敏规则

为保护用户隐私，所有开放API返回的数据都经过脱敏处理：

### 5.1 姓名脱敏
- **规则**: 保留姓氏，其余用`*`代替
- **示例**: 
  - 张三 → 张*
  - 李明明 → 李**
  - 王晓晓晓 → 王***

### 5.2 工号脱敏
- **规则**: 显示后4位，其余用`*`代替
- **示例**: 
  - 12345678 → ****5678
  - EMP001234 → *****1234

### 5.3 手机号脱敏
- **规则**: 保留前3位和后4位，中间用`*`代替
- **示例**: 
  - 13812345678 → 138****5678
  - 18900001234 → 189****1234

### 5.4 邮箱脱敏
- **规则**: 用户名保留前3个字符，其余用`***`代替，域名完整显示
- **示例**: 
  - zhangsan@example.com → zha***@example.com
  - lisi123@company.com → lis***@company.com

---

## 6. 访问限制

### 6.1 IP白名单
- 第三方应用可以配置IP白名单
- 只有白名单内的IP才能访问接口
- 白名单为空时不限制IP

### 6.2 应用状态
- 应用必须处于启用状态才能访问接口
- 禁用的应用访问时返回403错误

### 6.3 应用过期
- 应用可以设置过期时间
- 过期后访问接口返回403错误

### 6.4 访问频率限制
- 每个应用可以配置访问频率限制（rate_limit）
- 超过限制后返回429错误（Too Many Requests）

---

## 7. 错误码说明

### 7.1 常见错误码

| 错误码 | 说明 | 解决方案 |
|--------|------|----------|
| 400 | 请求参数错误 | 检查请求参数格式和必填项 |
| 401 | API Key无效 | 检查Authorization头中的API Key是否正确 |
| 403 | 禁止访问 | 检查应用状态、过期时间、IP白名单 |
| 404 | 资源不存在 | 检查请求的URL路径是否正确 |
| 429 | 请求过于频繁 | 降低请求频率，遵守rate_limit限制 |
| 500 | 服务器内部错误 | 联系系统管理员 |

### 7.2 业务错误码

| 错误码 | 说明 |
|--------|------|
| -1 | 通用业务错误（具体查看message字段） |

---

## 8. 接口调用日志

### 8.1 日志记录
系统会自动记录所有开放API的调用日志，包括：
- 应用ID和名称
- API路径和HTTP方法
- 请求IP地址
- 请求参数（已脱敏）
- 响应状态码
- 响应时间（毫秒）
- 错误信息（如有）
- 调用时间

### 8.2 日志用途
- 审计追踪
- 性能分析
- 问题排查
- 使用统计

---

## 9. 最佳实践

### 9.1 API Key安全
- ✅ 将API Key存储在环境变量或配置文件中
- ✅ 不要将API Key硬编码在代码中
- ✅ 不要将API Key提交到版本控制系统
- ✅ 定期更换API Key
- ❌ 不要在客户端（浏览器、移动应用）中暴露API Key

### 9.2 请求优化
- 使用合适的分页大小，避免一次请求过多数据
- 合理使用部门ID过滤，减少不必要的数据传输
- 缓存不经常变化的数据
- 实现请求重试机制（指数退避）

### 9.3 错误处理
- 实现完善的错误处理逻辑
- 根据不同的响应码采取不同的处理策略
- 记录错误日志便于问题排查

### 9.4 性能考虑
- 避免频繁调用接口，合理使用批量查询
- 在非高峰期执行大量数据同步
- 监控API响应时间，及时发现性能问题

---

## 10. 测试环境

### 10.1 测试地址
```
http://localhost:10801/ps-be/open-api/v1
```

### 10.2 测试API Key
```
b4a4b6a002b64502b2cc715a6ddec8cc
```

**注意**: 测试API Key仅用于开发和测试环境，生产环境请联系管理员获取正式API Key。

---

## 11. 联系方式

如有疑问或需要技术支持，请联系：

- **技术支持**: support@example.com
- **文档反馈**: docs@example.com

---

## 12. 变更记录

| 版本 | 日期 | 变更内容 | 作者 |
|------|------|----------|------|
| 1.0.0 | 2025-11-30 | 初始版本，包含用户查询相关接口 | System |

---

## 附录A: 完整请求示例（Python）

```python
import requests
import json

# 配置
BASE_URL = "http://localhost:10801/ps-be/open-api/v1"
API_KEY = "b4a4b6a002b64502b2cc715a6ddec8cc"

headers = {
    "Authorization": API_KEY,
    "Content-Type": "application/json"
}

# 1. 查询单个用户
def get_user_by_id(person_id):
    url = f"{BASE_URL}/users/{person_id}"
    response = requests.get(url, headers=headers)
    return response.json()

# 2. 分页查询用户列表
def get_user_list(dept_id=None, page=1, size=20):
    url = f"{BASE_URL}/users"
    params = {
        "page": page,
        "size": size
    }
    if dept_id:
        params["deptId"] = dept_id
    
    response = requests.get(url, headers=headers, params=params)
    return response.json()

# 3. 搜索用户
def search_users(keyword, dept_id=None, page=1, size=20):
    url = f"{BASE_URL}/users/search"
    data = {
        "keyword": keyword,
        "page": page,
        "size": size
    }
    if dept_id:
        data["deptId"] = dept_id
    
    response = requests.post(url, headers=headers, json=data)
    return response.json()

# 使用示例
if __name__ == "__main__":
    # 查询单个用户
    user = get_user_by_id("1234567890123456789")
    print("单个用户:", json.dumps(user, indent=2, ensure_ascii=False))
    
    # 查询用户列表
    users = get_user_list(page=1, size=10)
    print("用户列表:", json.dumps(users, indent=2, ensure_ascii=False))
    
    # 搜索用户
    search_result = search_users("张三")
    print("搜索结果:", json.dumps(search_result, indent=2, ensure_ascii=False))
```

---

## 附录B: 完整请求示例（Java）

```java
import okhttp3.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OpenApiClient {
    
    private static final String BASE_URL = "http://localhost:10801/ps-be/open-api/v1";
    private static final String API_KEY = "b4a4b6a002b64502b2cc715a6ddec8cc";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    
    /**
     * 查询单个用户
     */
    public String getUserById(String personId) throws IOException {
        String url = BASE_URL + "/users/" + personId;
        
        Request request = new Request.Builder()
            .url(url)
            .header("Authorization", API_KEY)
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    /**
     * 分页查询用户列表
     */
    public String getUserList(String deptId, int page, int size) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "/users").newBuilder();
        urlBuilder.addQueryParameter("page", String.valueOf(page));
        urlBuilder.addQueryParameter("size", String.valueOf(size));
        if (deptId != null) {
            urlBuilder.addQueryParameter("deptId", deptId);
        }
        
        Request request = new Request.Builder()
            .url(urlBuilder.build())
            .header("Authorization", API_KEY)
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    /**
     * 搜索用户
     */
    public String searchUsers(String keyword, String deptId, int page, int size) throws IOException {
        String url = BASE_URL + "/users/search";
        
        Map<String, Object> data = new HashMap<>();
        data.put("keyword", keyword);
        data.put("page", page);
        data.put("size", size);
        if (deptId != null) {
            data.put("deptId", deptId);
        }
        
        String json = gson.toJson(data);
        RequestBody body = RequestBody.create(json, JSON);
        
        Request request = new Request.Builder()
            .url(url)
            .header("Authorization", API_KEY)
            .post(body)
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    
    public static void main(String[] args) {
        OpenApiClient client = new OpenApiClient();
        
        try {
            // 查询单个用户
            String user = client.getUserById("1234567890123456789");
            System.out.println("单个用户: " + user);
            
            // 查询用户列表
            String users = client.getUserList(null, 1, 10);
            System.out.println("用户列表: " + users);
            
            // 搜索用户
            String searchResult = client.searchUsers("张三", null, 1, 10);
            System.out.println("搜索结果: " + searchResult);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
