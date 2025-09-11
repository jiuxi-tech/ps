package com.jiuxi.shared.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 安全配置属性
 * 统一管理安全认证、Keycloak、文件服务等配置
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Data
@Component("enhancedSecurityConfig")
@ConfigurationProperties(prefix = "app.security")
public class SecurityConfig {

    /**
     * 基础安全配置
     */
    private Basic basic = new Basic();

    /**
     * 认证配置
     */
    private Authentication authentication = new Authentication();

    /**
     * Keycloak配置
     */
    private Keycloak keycloak = new Keycloak();

    /**
     * JWT配置
     */
    private Jwt jwt = new Jwt();

    /**
     * 文件服务配置
     */
    private FileService fileService = new FileService();

    /**
     * 加密配置
     */
    private Encryption encryption = new Encryption();

    @Data
    public static class Basic {
        /**
         * 是否启用安全控制
         */
        private boolean enabled = true;

        /**
         * 是否启用密码传输加密
         */
        private boolean passwordEncryption = true;

        /**
         * 会话超时时间（秒）
         */
        private long sessionTimeout = 3600;

        /**
         * 是否启用CSRF保护
         */
        private boolean csrfEnabled = true;

        /**
         * 是否启用XSS保护
         */
        private boolean xssEnabled = true;
    }

    @Data
    public static class Authentication {
        /**
         * 不需要认证的路径
         */
        private List<String> excludePaths = List.of(
            "/static/**",
            "/sys/captcha/**",
            "/actuator/**",
            "/druid/**"
        );

        /**
         * 认证失败重试次数
         */
        private int maxRetryAttempts = 3;

        /**
         * 账户锁定时间（分钟）
         */
        private int lockoutDuration = 30;

        /**
         * 是否启用验证码
         */
        private boolean captchaEnabled = true;

        /**
         * 验证码有效期（秒）
         */
        private int captchaExpiry = 300;
    }

    @Data
    public static class Keycloak {
        /**
         * Keycloak服务器地址
         */
        private String serverUrl = "http://localhost:18080";

        /**
         * Realm名称
         */
        private String realm = "ps-realm";

        /**
         * SSO配置
         */
        private Sso sso = new Sso();

        /**
         * 管理员配置
         */
        private Admin admin = new Admin();

        @Data
        public static class Sso {
            /**
             * 是否启用SSO
             */
            private boolean enabled = true;

            /**
             * 客户端ID
             */
            private String clientId = "ps-be";

            /**
             * 客户端密钥
             */
            private String clientSecret;

            /**
             * 重定向配置
             */
            private Redirect redirect = new Redirect();

            /**
             * 用户信息头配置
             */
            private UserHeader userHeader = new UserHeader();

            @Data
            public static class Redirect {
                /**
                 * 登录成功重定向地址
                 */
                private String successUrl = "http://localhost:10801/#/sso/login";

                /**
                 * 登录失败重定向地址
                 */
                private String errorUrl = "http://localhost:10801/#/login";

                /**
                 * 登出重定向地址
                 */
                private String logoutUrl = "http://localhost:10801/#/logout";
            }

            @Data
            public static class UserHeader {
                /**
                 * 用户ID头名称
                 */
                private String userIdHeader = "X-User-Id";

                /**
                 * 用户名头名称
                 */
                private String usernameHeader = "X-Username";

                /**
                 * 邮箱头名称
                 */
                private String emailHeader = "X-User-Email";

                /**
                 * 姓名头名称
                 */
                private String nameHeader = "X-User-Name";

                /**
                 * 角色头名称
                 */
                private String rolesHeader = "X-User-Roles";

                /**
                 * 权限头名称
                 */
                private String permissionsHeader = "X-User-Permissions";
            }
        }

        @Data
        public static class Admin {
            /**
             * 管理员客户端ID
             */
            private String clientId = "admin-cli";

            /**
             * 管理员用户名
             */
            private String username = "admin";

            /**
             * 管理员密码
             */
            private String password = "admin123";

            /**
             * 连接超时时间（毫秒）
             */
            private int connectionTimeout = 5000;

            /**
             * 读取超时时间（毫秒）
             */
            private int readTimeout = 10000;
        }
    }

    @Data
    public static class Jwt {
        /**
         * 是否验证Token过期时间
         */
        private boolean verifyExpiration = true;

        /**
         * 是否验证签发者
         */
        private boolean verifyIssuer = true;

        /**
         * 是否验证受众
         */
        private boolean verifyAudience = false;

        /**
         * Token缓存时间（秒）
         */
        private int cacheTtl = 300;

        /**
         * JWT密钥
         */
        private String secret = "ps-be-jwt-secret-key-2024";

        /**
         * Token有效期（秒）
         */
        private long expiration = 3600;

        /**
         * 刷新Token有效期（秒）
         */
        private long refreshExpiration = 7200;

        /**
         * Token头名称
         */
        private String tokenHeader = "Authorization";

        /**
         * Token前缀
         */
        private String tokenPrefix = "Bearer ";
    }

    @Data
    public static class FileService {
        /**
         * 文件服务模式：local, server
         */
        private String mode = "local";

        /**
         * 本地文件服务配置
         */
        private Local local = new Local();

        /**
         * 远程文件服务配置
         */
        private Remote remote = new Remote();

        @Data
        public static class Local {
            /**
             * 存储目录
             */
            private String baseDir = "/upload/jdfs";

            /**
             * 访问URL前缀
             */
            private String urlPrefix = "/files";

            /**
             * 最大文件大小
             */
            private String maxFileSize = "100MB";

            /**
             * 允许的文件类型
             */
            private List<String> allowedExtensions = List.of(
                "jpg", "jpeg", "png", "gif", "bmp",
                "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
                "txt", "zip", "rar"
            );
        }

        @Data
        public static class Remote {
            /**
             * 远程服务器地址
             */
            private String serverUrl = "http://localhost:8080";

            /**
             * 访问密钥
             */
            private String accessKey;

            /**
             * 访问密钥
             */
            private String secretKey;

            /**
             * 存储桶名称
             */
            private String bucketName = "ps-files";
        }
    }

    @Data
    public static class Encryption {
        /**
         * 密码加密算法：BCrypt, MD5, SHA256
         */
        private String passwordAlgorithm = "BCrypt";

        /**
         * BCrypt强度
         */
        private int bcryptStrength = 10;

        /**
         * AES密钥
         */
        private String aesKey = "ps-be-aes-key-2024";

        /**
         * RSA密钥长度
         */
        private int rsaKeyLength = 2048;

        /**
         * SM2/SM3/SM4国密算法配置
         */
        private Sm sm = new Sm();

        @Data
        public static class Sm {
            /**
             * 是否启用国密算法
             */
            private boolean enabled = false;

            /**
             * SM2公钥
             */
            private String sm2PublicKey;

            /**
             * SM2私钥
             */
            private String sm2PrivateKey;

            /**
             * SM4密钥
             */
            private String sm4Key;
        }
    }
}