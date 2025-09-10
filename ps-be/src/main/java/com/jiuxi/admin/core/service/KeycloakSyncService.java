package com.jiuxi.admin.core.service;

/**
 * Keycloak同步服务接口
 *
 * @author System
 * @since 2025-01-21
 */
public interface KeycloakSyncService {

    /**
     * 同步账号到Keycloak
     *
     * @param accountId 账号ID
     * @param username 用户名
     * @param password 明文密码
     * @param creator 创建人ID
     * @return 同步结果
     */
    KeycloakSyncResult syncAccountToKeycloak(String accountId, String username, String password, String creator);

    /**
     * 更新Keycloak中的用户信息
     *
     * @param accountId 账号ID
     * @param username 用户名
     * @param password 新密码（可选）
     * @param updater 更新人ID
     * @return 同步结果
     */
    KeycloakSyncResult updateKeycloakUser(String accountId, String username, String password, String updater);

    /**
     * 禁用Keycloak中的用户
     *
     * @param accountId 账号ID
     * @return 同步结果
     */
    KeycloakSyncResult disableKeycloakUser(String accountId);

    /**
     * 启用Keycloak中的用户
     *
     * @param accountId 账号ID
     * @return 同步结果
     */
    KeycloakSyncResult enableKeycloakUser(String accountId);

    /**
     * 删除Keycloak中的用户
     *
     * @param accountId 账号ID
     * @return 同步结果
     */
    KeycloakSyncResult deleteKeycloakUser(String accountId);

    /**
     * Keycloak同步结果
     */
    class KeycloakSyncResult {
        private boolean success;
        private String message;
        private String keycloakUserId;
        private Exception exception;

        public KeycloakSyncResult() {}

        public KeycloakSyncResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public KeycloakSyncResult(boolean success, String message, String keycloakUserId) {
            this.success = success;
            this.message = message;
            this.keycloakUserId = keycloakUserId;
        }

        public static KeycloakSyncResult success(String message) {
            return new KeycloakSyncResult(true, message);
        }

        public static KeycloakSyncResult success(String message, String keycloakUserId) {
            return new KeycloakSyncResult(true, message, keycloakUserId);
        }

        public static KeycloakSyncResult failure(String message) {
            return new KeycloakSyncResult(false, message);
        }

        public static KeycloakSyncResult failure(String message, Exception exception) {
            KeycloakSyncResult result = new KeycloakSyncResult(false, message);
            result.setException(exception);
            return result;
        }

        // Getter and Setter methods
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getKeycloakUserId() {
            return keycloakUserId;
        }

        public void setKeycloakUserId(String keycloakUserId) {
            this.keycloakUserId = keycloakUserId;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }

        @Override
        public String toString() {
            return "KeycloakSyncResult{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    ", keycloakUserId='" + keycloakUserId + '\'' +
                    ", exception=" + exception +
                    '}';
        }
    }
}