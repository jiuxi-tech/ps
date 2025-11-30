package com.jiuxi.shared.common.constants;

/**
 * 安全常量类
 * 定义安全相关的常量
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public final class SecurityConstants {

    private SecurityConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    /**
     * JWT相关
     */
    public static final class Jwt {
        /** JWT密钥 */
        public static final String SECRET = "jiuxi-sso-jwt-secret-key-2025";
        /** JWT过期时间（毫秒） */
        public static final long EXPIRATION = 24 * 60 * 60 * 1000L; // 24小时
        /** JWT刷新时间（毫秒） */
        public static final long REFRESH_EXPIRATION = 7 * 24 * 60 * 60 * 1000L; // 7天
        /** JWT令牌前缀 */
        public static final String TOKEN_PREFIX = "Bearer ";
        /** JWT头部名称 */
        public static final String TOKEN_HEADER = "Authorization";
        /** JWT声明：用户ID */
        public static final String CLAIM_USER_ID = "userId";
        /** JWT声明：用户名 */
        public static final String CLAIM_USERNAME = "username";
        /** JWT声明：角色 */
        public static final String CLAIM_ROLES = "roles";
        /** JWT声明：权限 */
        public static final String CLAIM_AUTHORITIES = "authorities";
        /** JWT声明：租户ID */
        public static final String CLAIM_TENANT_ID = "tenantId";
    }

    /**
     * 认证相关
     */
    public static final class Auth {
        /** 登录URL */
        public static final String LOGIN_URL = "/auth/login";
        /** 登出URL */
        public static final String LOGOUT_URL = "/auth/logout";
        /** 刷新令牌URL */
        public static final String REFRESH_URL = "/auth/refresh";
        /** 用户信息URL */
        public static final String USER_INFO_URL = "/auth/userinfo";
        /** 验证码URL */
        public static final String CAPTCHA_URL = "/auth/captcha";
        
        /** 默认登录页面 */
        public static final String DEFAULT_LOGIN_PAGE = "/login.html";
        /** 登录成功默认跳转页面 */
        public static final String DEFAULT_SUCCESS_URL = "/index.html";
        /** 登录失败默认跳转页面 */
        public static final String DEFAULT_FAILURE_URL = "/login.html?error=true";
        
        /** 记住我参数名 */
        public static final String REMEMBER_ME_PARAM = "rememberMe";
        /** 记住我Cookie名 */
        public static final String REMEMBER_ME_COOKIE = "remember-me";
        /** 记住我有效期（秒） */
        public static final int REMEMBER_ME_SECONDS = 7 * 24 * 60 * 60; // 7天
    }

    /**
     * 权限相关
     */
    public static final class Permission {
        /** 权限前缀 */
        public static final String PREFIX = "PERM_";
        /** 角色前缀 */
        public static final String ROLE_PREFIX = "ROLE_";
        /** 管理员角色 */
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        /** 普通用户角色 */
        public static final String ROLE_USER = "ROLE_USER";
        /** 访客角色 */
        public static final String ROLE_GUEST = "ROLE_GUEST";
        
        /** 系统管理权限 */
        public static final String SYS_ADMIN = "sys:admin";
        /** 用户管理权限 */
        public static final String USER_MANAGE = "user:manage";
        /** 角色管理权限 */
        public static final String ROLE_MANAGE = "role:manage";
        /** 菜单管理权限 */
        public static final String MENU_MANAGE = "menu:manage";
        /** 组织管理权限 */
        public static final String ORG_MANAGE = "org:manage";
    }

    /**
     * 会话相关
     */
    public static final class Session {
        /** 会话ID键名 */
        public static final String SESSION_ID = "JSESSIONID";
        /** 用户会话键名 */
        public static final String USER_SESSION_KEY = "USER_SESSION";
        /** 会话过期时间（秒） */
        public static final int SESSION_TIMEOUT = 30 * 60; // 30分钟
        /** 最大并发会话数 */
        public static final int MAX_SESSIONS = 1;
        /** 会话过期处理策略 */
        public static final String SESSION_EXPIRED_STRATEGY = "preventLogin";
    }

    /**
     * 密码相关
     */
    public static final class Password {
        /** 默认密码 */
        public static final String DEFAULT_PASSWORD = "123456";
        
        /** 
         * 密码最小长度 
         * @deprecated 请使用 topinfo.security.authentication.password-policy.length.min 配置
         */
        @Deprecated
        public static final int MIN_LENGTH = 6;
        
        /** 
         * 密码最大长度 
         * @deprecated 请使用 topinfo.security.authentication.password-policy.length.max 配置
         */
        @Deprecated
        public static final int MAX_LENGTH = 20;
        
        /** 密码强度：弱 */
        public static final String STRENGTH_WEAK = "weak";
        /** 密码强度：中 */
        public static final String STRENGTH_MEDIUM = "medium";
        /** 密码强度：强 */
        public static final String STRENGTH_STRONG = "strong";
        
        /** 密码加密算法 */
        public static final String ENCODER_TYPE = "BCrypt";
        
        /** 
         * 密码重试次数 
         * @deprecated 请使用 topinfo.security.authentication.account-lockout.max-attempts 配置
         */
        @Deprecated
        public static final int MAX_RETRY_COUNT = 5;
        
        /** 
         * 密码锁定时间（分钟） 
         * @deprecated 请使用 topinfo.security.authentication.account-lockout.lockout-duration 配置
         */
        @Deprecated
        public static final int LOCK_TIME_MINUTES = 30;
    }

    /**
     * 验证码相关
     */
    public static final class Captcha {
        /** 验证码长度 */
        public static final int CODE_LENGTH = 4;
        /** 验证码有效期（分钟） */
        public static final int EXPIRE_MINUTES = 5;
        /** 验证码类型：数字 */
        public static final String TYPE_NUMBER = "number";
        /** 验证码类型：字母 */
        public static final String TYPE_LETTER = "letter";
        /** 验证码类型：混合 */
        public static final String TYPE_MIXED = "mixed";
        /** 验证码图片宽度 */
        public static final int IMAGE_WIDTH = 120;
        /** 验证码图片高度 */
        public static final int IMAGE_HEIGHT = 40;
        /** 验证码参数名 */
        public static final String CAPTCHA_PARAM = "captcha";
        /** 验证码会话键 */
        public static final String CAPTCHA_SESSION_KEY = "CAPTCHA_CODE";
    }

    /**
     * 加密相关
     */
    public static final class Encrypt {
        /** AES密钥 */
        public static final String AES_KEY = "jiuxi-aes-key-16";
        /** DES密钥 */
        public static final String DES_KEY = "jiuxi-des";
        /** RSA密钥长度 */
        public static final int RSA_KEY_SIZE = 2048;
        /** MD5盐值 */
        public static final String MD5_SALT = "jiuxi-md5-salt";
        /** SHA256盐值 */
        public static final String SHA256_SALT = "jiuxi-sha256-salt";
    }

    /**
     * 安全头部
     */
    public static final class SecurityHeader {
        /** 内容安全策略 */
        public static final String CSP = "Content-Security-Policy";
        /** X-Frame-Options */
        public static final String X_FRAME_OPTIONS = "X-Frame-Options";
        /** X-Content-Type-Options */
        public static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
        /** X-XSS-Protection */
        public static final String X_XSS_PROTECTION = "X-XSS-Protection";
        /** Strict-Transport-Security */
        public static final String HSTS = "Strict-Transport-Security";
    }

    /**
     * Keycloak相关
     */
    public static final class Keycloak {
        /** 默认realm */
        public static final String DEFAULT_REALM = "jiuxi";
        /** 客户端ID */
        public static final String CLIENT_ID = "jiuxi-client";
        /** 资源服务器ID */
        public static final String RESOURCE_SERVER_ID = "jiuxi-resource";
        /** 管理员用户名 */
        public static final String ADMIN_USERNAME = "admin";
        /** 用户属性：部门ID */
        public static final String ATTR_DEPT_ID = "deptId";
        /** 用户属性：组织ID */
        public static final String ATTR_ORG_ID = "orgId";
        /** 用户属性：租户ID */
        public static final String ATTR_TENANT_ID = "tenantId";
    }

    /**
     * 审计相关
     */
    public static final class Audit {
        /** 登录审计 */
        public static final String LOGIN_AUDIT = "LOGIN";
        /** 登出审计 */
        public static final String LOGOUT_AUDIT = "LOGOUT";
        /** 操作审计 */
        public static final String OPERATION_AUDIT = "OPERATION";
        /** 异常审计 */
        public static final String EXCEPTION_AUDIT = "EXCEPTION";
        /** 数据访问审计 */
        public static final String DATA_ACCESS_AUDIT = "DATA_ACCESS";
    }

    /**
     * 安全事件
     */
    public static final class SecurityEvent {
        /** 认证成功 */
        public static final String AUTH_SUCCESS = "AUTH_SUCCESS";
        /** 认证失败 */
        public static final String AUTH_FAILURE = "AUTH_FAILURE";
        /** 权限拒绝 */
        public static final String ACCESS_DENIED = "ACCESS_DENIED";
        /** 会话过期 */
        public static final String SESSION_EXPIRED = "SESSION_EXPIRED";
        /** 密码修改 */
        public static final String PASSWORD_CHANGED = "PASSWORD_CHANGED";
        /** 账户锁定 */
        public static final String ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
        /** 账户解锁 */
        public static final String ACCOUNT_UNLOCKED = "ACCOUNT_UNLOCKED";
    }

    /**
     * Redis发布订阅Topic
     */
    public static final class RedisTopic {
        /** 用户登出Topic */
        public static final String USER_LOGOUT = "platform-security-logout";
        /** 用户状态变更Topic */
        public static final String USER_STATUS_CHANGE = "platform-security-user-status";
        /** 权限变更Topic */
        public static final String PERMISSION_CHANGE = "platform-security-permission";
        /** 配置变更Topic */
        public static final String CONFIG_CHANGE = "platform-security-config";
    }
}