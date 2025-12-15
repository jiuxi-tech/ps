# PS-BMP Keycloak 自定义主题

PS-BMP 项目的 Keycloak 登录主题,基于 Keycloakify v11 构建。

## 快速开始

```bash
cd ps-theme
npm install
```

# Testing the theme locally

[Documentation](https://docs.keycloakify.dev/testing-your-theme)

# How to customize the theme

[Documentation](https://docs.keycloakify.dev/customization-strategies)

# Building the theme

You need to have [Maven](https://maven.apache.org/) installed to build the theme (Maven >= 3.1.1, Java >= 7).  
The `mvn` command must be in the $PATH.

-   On macOS: `brew install maven`
-   On Debian/Ubuntu: `sudo apt-get install maven`
-   On Windows: `choco install openjdk` and `choco install maven` (Or download from [here](https://maven.apache.org/download.cgi))

```bash
npm run build-keycloak-theme
```

Note that by default Keycloakify generates multiple .jar files for different versions of Keycloak.  
You can customize this behavior, see documentation [here](https://docs.keycloakify.dev/features/compiler-options/keycloakversiontargets).

# Initializing the account theme

```bash
npx keycloakify initialize-account-theme
```

# Initializing the email theme

```bash
npx keycloakify initialize-email-theme
```

## 构建主题

```bash
# 开发模式
npm run dev

# 生产构建(生成 JAR 包)
npm run prod
```

构建后的 JAR 包位于 `dist_keycloak/` 目录,需要部署到 Keycloak 的 `providers/` 目录。
