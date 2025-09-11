package com.jiuxi.shared.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 统一安全配置属性类
 * 整合Spring Security和Keycloak相关的所有安全配置
 * 
 * @author Security Refactoring
 * @since Phase 4.2.2
 */
@ConfigurationProperties(prefix = "ps.security")
public class SecurityProperties {

    /**
     * 认证配置
     */
    private Authentication authentication = new Authentication();

    /**
     * 授权配置
     */
    private Authorization authorization = new Authorization();

    /**
     * JWT配置
     */
    private Jwt jwt = new Jwt();

    /**
     * Keycloak SSO配置
     */
    private Keycloak keycloak = new Keycloak();

    /**
     * 会话配置
     */
    private Session session = new Session();

    /**
     * 认证配置
     */
    public static class Authentication {
        /**
         * 是否启用认证，默认启用
         */
        private boolean enabled = true;

        /**
         * 密码传输是否加密，默认不加密
         */
        private boolean passwordEncryption = false;

        /**
         * 排除认证的路径
         */
        private String[] excludePaths = {
            "/static/**",
            "/error/**", 
            "/platform/**",
            "/app/platform/**",
            "/actuator/**",
            "/favicon.ico"
        };

        /**
         * 登录页面URL
         */
        private String loginUrl = "/login";

        /**
         * 登录处理URL
         */
        private String loginProcessingUrl = "/login/process";

        /**
         * 登录成功重定向URL
         */
        private String successUrl = "/";

        /**
         * 登录失败重定向URL
         */
        private String failureUrl = "/login?error";

        /**
         * 注销URL
         */
        private String logoutUrl = "/logout";

        /**
         * 注销成功重定向URL
         */
        private String logoutSuccessUrl = "/login";

        // getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isPasswordEncryption() {
            return passwordEncryption;
        }

        public void setPasswordEncryption(boolean passwordEncryption) {
            this.passwordEncryption = passwordEncryption;
        }

        public String[] getExcludePaths() {
            return excludePaths;
        }

        public void setExcludePaths(String[] excludePaths) {
            this.excludePaths = excludePaths;
        }

        public String getLoginUrl() {
            return loginUrl;
        }

        public void setLoginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
        }

        public String getLoginProcessingUrl() {
            return loginProcessingUrl;
        }

        public void setLoginProcessingUrl(String loginProcessingUrl) {
            this.loginProcessingUrl = loginProcessingUrl;
        }

        public String getSuccessUrl() {
            return successUrl;
        }

        public void setSuccessUrl(String successUrl) {
            this.successUrl = successUrl;
        }

        public String getFailureUrl() {
            return failureUrl;
        }

        public void setFailureUrl(String failureUrl) {
            this.failureUrl = failureUrl;
        }

        public String getLogoutUrl() {
            return logoutUrl;
        }

        public void setLogoutUrl(String logoutUrl) {
            this.logoutUrl = logoutUrl;
        }

        public String getLogoutSuccessUrl() {
            return logoutSuccessUrl;
        }

        public void setLogoutSuccessUrl(String logoutSuccessUrl) {
            this.logoutSuccessUrl = logoutSuccessUrl;
        }
    }

    /**
     * 授权配置
     */
    public static class Authorization {
        /**
         * 是否启用授权，默认启用
         */
        private boolean enabled = true;

        /**
         * 排除授权的路径
         */
        private String[] excludePaths = {
            "/static/**",
            "/error/**",
            "/platform/**",
            "/app/platform/**"
        };

        /**
         * 是否启用缓存，默认启用
         */
        private boolean cacheEnabled = true;

        /**
         * 缓存过期时间（秒），默认10分钟
         */
        private int cacheExpiration = 600;

        /**
         * 权限查询超时时间（毫秒），默认5秒
         */
        private int queryTimeout = 5000;

        // getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String[] getExcludePaths() {
            return excludePaths;
        }

        public void setExcludePaths(String[] excludePaths) {
            this.excludePaths = excludePaths;
        }

        public boolean isCacheEnabled() {
            return cacheEnabled;
        }

        public void setCacheEnabled(boolean cacheEnabled) {
            this.cacheEnabled = cacheEnabled;
        }

        public int getCacheExpiration() {
            return cacheExpiration;
        }

        public void setCacheExpiration(int cacheExpiration) {
            this.cacheExpiration = cacheExpiration;
        }

        public int getQueryTimeout() {
            return queryTimeout;
        }

        public void setQueryTimeout(int queryTimeout) {
            this.queryTimeout = queryTimeout;
        }
    }

    /**
     * JWT配置
     */
    public static class Jwt {
        /**
         * JWT签名密钥
         */
        private String secret = "ps-be-default-jwt-secret-key";

        /**
         * JWT过期时间（秒），默认24小时
         */
        private int expiration = 86400;

        /**
         * 是否启用JWT缓存，默认启用
         */
        private boolean cacheEnabled = true;

        /**
         * JWT缓存过期时间（秒），默认5分钟
         */
        private int cacheExpiration = 300;

        /**
         * 时钟偏差容忍时间（秒），默认1分钟
         */
        private int clockSkew = 60;

        /**
         * JWT头名称
         */
        private String header = "Authorization";

        /**
         * JWT前缀
         */
        private String prefix = "Bearer ";

        // getters and setters
        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public int getExpiration() {
            return expiration;
        }

        public void setExpiration(int expiration) {
            this.expiration = expiration;
        }

        public boolean isCacheEnabled() {
            return cacheEnabled;
        }

        public void setCacheEnabled(boolean cacheEnabled) {
            this.cacheEnabled = cacheEnabled;
        }

        public int getCacheExpiration() {
            return cacheExpiration;
        }

        public void setCacheExpiration(int cacheExpiration) {
            this.cacheExpiration = cacheExpiration;
        }

        public int getClockSkew() {
            return clockSkew;
        }

        public void setClockSkew(int clockSkew) {
            this.clockSkew = clockSkew;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }
    }

    /**
     * Keycloak配置
     */
    public static class Keycloak {
        /**
         * 是否启用Keycloak SSO，默认禁用
         */
        private boolean enabled = false;

        /**
         * Keycloak服务器地址
         */
        private String serverUrl = "http://localhost:8180";

        /**
         * Realm名称
         */
        private String realm = "ps-realm";

        /**
         * 客户端ID
         */
        private String clientId = "ps-be";

        /**
         * 客户端密钥
         */
        private String clientSecret;

        /**
         * 管理员用户名
         */
        private String adminUsername = "admin";

        /**
         * 管理员密码
         */
        private String adminPassword = "admin123";

        /**
         * 连接超时时间（毫秒）
         */
        private int connectionTimeout = 5000;

        /**
         * 读取超时时间（毫秒）
         */
        private int readTimeout = 10000;

        /**
         * 连接请求超时时间（毫秒）
         */
        private int connectionRequestTimeout = 3000;

        /**
         * 是否启用用户同步
         */
        private boolean syncEnabled = false;

        /**
         * 用户信息头配置
         */
        private UserHeader userHeader = new UserHeader();

        /**
         * 重定向配置
         */
        private Redirect redirect = new Redirect();

        /**
         * 用户信息头配置
         */
        public static class UserHeader {
            private String userIdHeader = "X-User-ID";
            private String usernameHeader = "X-User-Name";
            private String emailHeader = "X-User-Email";
            private String rolesHeader = "X-User-Roles";

            // getters and setters
            public String getUserIdHeader() {
                return userIdHeader;
            }

            public void setUserIdHeader(String userIdHeader) {
                this.userIdHeader = userIdHeader;
            }

            public String getUsernameHeader() {
                return usernameHeader;
            }

            public void setUsernameHeader(String usernameHeader) {
                this.usernameHeader = usernameHeader;
            }

            public String getEmailHeader() {
                return emailHeader;
            }

            public void setEmailHeader(String emailHeader) {
                this.emailHeader = emailHeader;
            }

            public String getRolesHeader() {
                return rolesHeader;
            }

            public void setRolesHeader(String rolesHeader) {
                this.rolesHeader = rolesHeader;
            }
        }

        /**
         * 重定向配置
         */
        public static class Redirect {
            private String successUrl = "http://localhost:10801/#/main";
            private String errorUrl = "http://localhost:10801/#/login";

            // getters and setters
            public String getSuccessUrl() {
                return successUrl;
            }

            public void setSuccessUrl(String successUrl) {
                this.successUrl = successUrl;
            }

            public String getErrorUrl() {
                return errorUrl;
            }

            public void setErrorUrl(String errorUrl) {
                this.errorUrl = errorUrl;
            }
        }

        // getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getServerUrl() {
            return serverUrl;
        }

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public String getRealm() {
            return realm;
        }

        public void setRealm(String realm) {
            this.realm = realm;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public UserHeader getUserHeader() {
            return userHeader;
        }

        public void setUserHeader(UserHeader userHeader) {
            this.userHeader = userHeader;
        }

        public Redirect getRedirect() {
            return redirect;
        }

        public void setRedirect(Redirect redirect) {
            this.redirect = redirect;
        }

        public String getAdminUsername() {
            return adminUsername;
        }

        public void setAdminUsername(String adminUsername) {
            this.adminUsername = adminUsername;
        }

        public String getAdminPassword() {
            return adminPassword;
        }

        public void setAdminPassword(String adminPassword) {
            this.adminPassword = adminPassword;
        }

        public int getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
        }

        public int getConnectionRequestTimeout() {
            return connectionRequestTimeout;
        }

        public void setConnectionRequestTimeout(int connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
        }

        public boolean isSyncEnabled() {
            return syncEnabled;
        }

        public void setSyncEnabled(boolean syncEnabled) {
            this.syncEnabled = syncEnabled;
        }

        /**
         * 获取Issuer URI
         */
        public String getIssuerUri() {
            return serverUrl + "/realms/" + realm;
        }
    }

    /**
     * 会话配置
     */
    public static class Session {
        /**
         * 是否启用会话，默认禁用（无状态）
         */
        private boolean enabled = false;

        /**
         * 会话超时时间（秒），默认30分钟
         */
        private int timeout = 1800;

        /**
         * 最大并发会话数，-1表示无限制
         */
        private int maxConcurrentSessions = -1;

        /**
         * 是否阻止新会话创建
         */
        private boolean preventSessionCreation = false;

        // getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public int getMaxConcurrentSessions() {
            return maxConcurrentSessions;
        }

        public void setMaxConcurrentSessions(int maxConcurrentSessions) {
            this.maxConcurrentSessions = maxConcurrentSessions;
        }

        public boolean isPreventSessionCreation() {
            return preventSessionCreation;
        }

        public void setPreventSessionCreation(boolean preventSessionCreation) {
            this.preventSessionCreation = preventSessionCreation;
        }
    }

    // Main getters and setters
    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public Keycloak getKeycloak() {
        return keycloak;
    }

    public void setKeycloak(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}