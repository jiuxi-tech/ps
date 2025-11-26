# 业务中台统一身份认证用户信息说明

## 测试阶段使用的账号如下（3个）

### 测试账号1
- 账号：sso1
- 密码：1111qqqq
- 用户名称：测试账号1
- 用户编号：A00001
- 电子邮件：A00001@shxdx.com

userinfo 接口返回
``` json
{"user":{"PERSON_NAME":"测试账号1","sub":"6282616a-9f33-4793-9e3b-2465bd1a5cf6","email_verified":true,"name":"测试账号1","PERSON_NO":"A00001","USERNAME":"sso1","preferred_username":"sso1","given_name":"测试账号1","EMAIL":"A00001@shxdx.com","email":"a00001@shxdx.com","tokenExpiry":1760470926704},"isLoggedIn":true,"tokenExpiry":1760470926704}

```

### 测试账号2
- 账号：sso2
- 密码：1111qqqq
- 用户名称：测试账号2
- 用户编号：A00002
- 电子邮件：A00002@shxdx.com
userinfo 接口返回
``` json
{"user":{"PERSON_NAME":"测试账号2","sub":"a9ff6944-9431-47e3-bab2-125e8d0d30cb","email_verified":true,"name":"测试账号2","PERSON_NO":"A00002","USERNAME":"sso2","preferred_username":"sso2","given_name":"测试账号2","EMAIL":"A00002@shxdx.com","email":"a00002@shxdx.com","tokenExpiry":1760470981909},"isLoggedIn":true,"tokenExpiry":1760470981909}

```


### 办公室测试1
- 账号：bgs1
- 密码：4rfv3edc
- 用户名称：办公室测试1
- 用户编号：BGS00001
- 电子邮件：BGS00001@shxdx.com

userinfo 接口返回
``` json
{"user":{"PERSON_NAME":"办公室测试1","sub":"42270749-7f31-4b06-896f-e5016263e720","email_verified":false,"name":"办公室测试1","PERSON_NO":"BGS00001","USERNAME":"bgs1","preferred_username":"bgs1","given_name":"办公室测试1","EMAIL":"bgs00001@shxdx.com","email":"bgs00001@shxdx.com","tokenExpiry":1760470675580},"isLoggedIn":true,"tokenExpiry":1760470675580}
```

 
### 业务字段
以下4个是业务字段，其中USERNAME在中台是唯一值

PERSON_NAME 用户名称|姓名
PERSON_NO 用户编号|工号
EMAIL 电子邮件
USERNAME 登录账号
