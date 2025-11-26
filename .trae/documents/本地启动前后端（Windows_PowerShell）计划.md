# 启动前后端（Windows）

## 环境前置检查

* 安装并确认 `JDK 11/17`、`Maven`、`Node.js` 与包管理器（`npm` 或 `pnpm`）。

* 使用 PowerShell；命令分隔用 `;`，不要使用 `&&`。

* 端口占用检查：后端 `8082`、前端 `10801`、Keycloak `18080`。

* 数据库可用性：`alilaoba.cn:13307`，库 `ps-bmp`，用户 `root`，密码 `$D8BZ8Qmav`（项目按 `application.yml` 配置）。

* 认证服务：确保 Keycloak 运行在 `http://localhost:18080`，`realm=ps-realm`，`clientId=ps-be`。

## 后端启动

* 进入后端目录：`D:\projects\ps-bmp\keycloak-sb-sso\ps-be`。

* 可选预构建：`mvn -e clean package -DskipTests`。

* 开发运行：`mvn -e spring-boot:run`。

* 期望日志：Spring Boot 启动完成，监听 `8082`，`context-path=/ps-be`。

* 验证（任选其一）：

  * 如启用 Actuator：访问 `http://localhost:8082/ps-be/actuator/health`。

  * 若有测试接口：访问示例 `http://localhost:8082/ps-be/test_user_is_enable`。

  * 无内置健康检查时：查看日志无错误且端口开放。

## 前端启动

* 进入前端目录：`D:\projects\ps-bmp\keycloak-sb-sso\ps-fe`。

* 安装依赖（任选）：

  * `npm install`

  * 或 `pnpm install`

* 启动开发服务（根据 `package.json` 的 scripts，常见两种）：

  * `npm run dev`

  * 或 `npm start`

* 打开浏览器访问 `http://localhost:10801`。

## 集成与联调

* 前端 API 基础路径应指向后端 `http://localhost:8082/ps-be`；若存在跨域，使用前端开发代理或后端允许指定源。

* 登录与鉴权：使用 Keycloak（`http://localhost:18080`，`ps-realm`，`ps-be`）。管理员账号 `admin/admin123` 验证。

## 故障排查

* 端口被占用：在 PowerShell 用 `Get-NetTCPConnection | Select-Object -Property LocalAddress,LocalPort,State,OwningProcess | Where-Object {$_.LocalPort -in 8082,10801,18080}`；结束占用进程后重试。

* Maven 下载慢：切换镜像或重试；命令默认带 `-e` 便于定位错误。

* Node 版本不兼容：使用 LTS（如 18/20）；若脚本不存在，查看 `package.json` 的 `scripts`。

* 数据库连接失败：检查公网连通性与凭据；如需本地复刻数据库，调整 `application.yml`。

* Keycloak 连接失败：确认服务可达与客户端配置（回调地址、密钥）。

## 其他可选方案

* 直接运行可执行包：`mvn -e clean package -DskipTests` 后，用 `java -jar target\*.jar --server.port=8082 --server.servlet.context-path=/ps-be`。

* 容器化：若仓库提供 Docker/Compose，可统一启动三方服务（后端/前端/Keycloak/DB）。

## 立刻可执行的摘要

* 后端：`cd D:\projects\ps-bmp\keycloak-sb-sso\ps-be ; mvn -e spring-boot:run`。

* 前端：`cd D:\projects\ps-bmp\keycloak-sb-sso\ps-fe ; npm install ; npm run dev`。

* 验证：打开 `http://localhost:10801` 与 `http://localhost:8082/ps-be`（或健康检查/测试接口）。

## 分阶段任务清单

* 检查本地 JDK/Maven/Node 环境版本与端口占用。

* 启动后端并确认 `8082` 与 `/ps-be` 正常。

* 启动前端并确认 `10801` 正常可访问。

* 校验 Keycloak 登录流程与接口鉴权。

* 前后端接口联调，确保基本页面/列表加载成功。

* 记录并解决启动过程的任何错误（依赖安装、端口冲突、网络问题）。

