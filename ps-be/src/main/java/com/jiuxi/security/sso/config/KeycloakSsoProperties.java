package com.jiuxi.security.sso.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Keycloak SSO 配置属性
 * 
 * @author SSO Integration
 * @since 2.2.2
 */
@ConfigurationProperties(prefix = "keycloak.sso")
public class KeycloakSsoProperties {
    
    /**
     * 是否启用 Keycloak SSO
     */
    private boolean enabled = false;
    
    /**
     * Keycloak 服务器地址
     */
    private String serverUrl = "http://localhost:8180";
    
    /**
     * Realm 名称
     */
    private String realm = "ps-realm";
    
    /**
     * 客户端 ID
     */
    private String clientId = "ps-be";
    
    /**
     * 客户端密钥
     */
    private String clientSecret;
    
    /**
     * JWT 验证配置
     */
    private JwtConfig jwt = new JwtConfig();
    
    /**
     * 用户信息头配置
     */
    private UserHeaderConfig userHeader = new UserHeaderConfig();
    
    /**
     * 回调重定向配置
     */
    private RedirectConfig redirect = new RedirectConfig();
    
    public static class JwtConfig {
        /**
         * 时钟偏差容忍时间（秒）
         */
        private int clockSkew = 60;
        
        /**
         * 是否缓存解析的 Token
         */
        private boolean cacheParsedTokens = true;
        
        /**
         * 缓存 TTL（秒）
         */
        private int cacheTtl = 300;
        
        // getters and setters
        public int getClockSkew() {
            return clockSkew;
        }
        
        public void setClockSkew(int clockSkew) {
            this.clockSkew = clockSkew;
        }
        
        public boolean isCacheParsedTokens() {
            return cacheParsedTokens;
        }
        
        public void setCacheParsedTokens(boolean cacheParsedTokens) {
            this.cacheParsedTokens = cacheParsedTokens;
        }
        
        public int getCacheTtl() {
            return cacheTtl;
        }
        
        public void setCacheTtl(int cacheTtl) {
            this.cacheTtl = cacheTtl;
        }
    }
    
    public static class UserHeaderConfig {
        /**
         * 用户 ID 头名称
         */
        private String userIdHeader = "X-User-ID";
        
        /**
         * 用户名头名称
         */
        private String usernameHeader = "X-User-Name";
        
        /**
         * 用户邮箱头名称
         */
        private String emailHeader = "X-User-Email";
        
        /**
         * 用户角色头名称
         */
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
     * 回调重定向配置类
     */
    public static class RedirectConfig {
        /**
         * 登录成功后重定向地址
         */
        private String successUrl = "http://localhost:10801/#/main";
        
        /**
         * 登录失败后重定向地址
         */
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
    
    public JwtConfig getJwt() {
        return jwt;
    }
    
    public void setJwt(JwtConfig jwt) {
        this.jwt = jwt;
    }
    
    public UserHeaderConfig getUserHeader() {
        return userHeader;
    }
    
    public void setUserHeader(UserHeaderConfig userHeader) {
        this.userHeader = userHeader;
    }
    
    public RedirectConfig getRedirect() {
        return redirect;
    }
    
    public void setRedirect(RedirectConfig redirect) {
        this.redirect = redirect;
    }
    
    /**
     * 获取 Issuer URI
     * @return Issuer URI
     */
    public String getIssuerUri() {
        return serverUrl + "/realms/" + realm;
    }
}