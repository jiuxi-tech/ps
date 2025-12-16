package com.jiuxi.admin.core.service;

import com.jiuxi.admin.security.credential.CredentialType;

import java.util.ArrayList;
import java.util.List;

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
     * 同步账号的多个凭据到Keycloak
     * 根据账号信息（username、phone、idcard）创建1-3个Keycloak用户
     *
     * @param accountId 账号ID
     * @param password 明文密码
     * @param creator 创建人ID
     * @return 多凭据同步结果
     */
    MultiCredentialSyncResult syncMultipleCredentials(String accountId, String password, String creator);

    /**
     * 更新账号所有凭据的密码
     * 同步更新所有关联Keycloak用户的密码
     *
     * @param accountId 账号ID
     * @param newPassword 新密码（明文）
     * @param updater 更新人ID
     * @return 多凭据同步结果
     */
    MultiCredentialSyncResult updatePasswordForAllCredentials(String accountId, String newPassword, String updater);

    /**
     * 删除账号的所有凭据
     * 删除账号关联的所有Keycloak用户
     *
     * @param accountId 账号ID
     * @return 多凭据同步结果
     */
    MultiCredentialSyncResult deleteAllCredentials(String accountId);

    /**
     * 启用账号的所有凭据
     *
     * @param accountId 账号ID
     * @return 多凭据同步结果
     */
    MultiCredentialSyncResult enableAllCredentials(String accountId);

    /**
     * 禁用账号的所有凭据
     *
     * @param accountId 账号ID
     * @return 多凭据同步结果
     */
    MultiCredentialSyncResult disableAllCredentials(String accountId);

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

    /**
     * 多凭据同步结果
     */
    class MultiCredentialSyncResult {
        private boolean success;
        private String message;
        private int totalCredentials;
        private int successCount;
        private int failureCount;
        private List<CredentialSyncDetail> details;

        public MultiCredentialSyncResult() {
            this.details = new ArrayList<>();
        }

        public MultiCredentialSyncResult(boolean success, String message) {
            this();
            this.success = success;
            this.message = message;
        }

        /**
         * 添加凭据同步详情
         */
        public void addDetail(CredentialSyncDetail detail) {
            if (this.details == null) {
                this.details = new ArrayList<>();
            }
            this.details.add(detail);
        }

        /**
         * 计算统计数据
         */
        public void calculateStatistics() {
            this.totalCredentials = details != null ? details.size() : 0;
            this.successCount = (int) details.stream().filter(CredentialSyncDetail::isSuccess).count();
            this.failureCount = totalCredentials - successCount;
            this.success = successCount > 0 && failureCount == 0;

            if (this.message == null) {
                this.message = String.format("凭据同步完成: 总数=%d, 成功=%d, 失败=%d",
                        totalCredentials, successCount, failureCount);
            }
        }

        public static MultiCredentialSyncResult success(String message) {
            MultiCredentialSyncResult result = new MultiCredentialSyncResult(true, message);
            result.calculateStatistics();
            return result;
        }

        public static MultiCredentialSyncResult failure(String message) {
            MultiCredentialSyncResult result = new MultiCredentialSyncResult(false, message);
            result.setTotalCredentials(0);
            result.setSuccessCount(0);
            result.setFailureCount(0);
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

        public int getTotalCredentials() {
            return totalCredentials;
        }

        public void setTotalCredentials(int totalCredentials) {
            this.totalCredentials = totalCredentials;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public void setSuccessCount(int successCount) {
            this.successCount = successCount;
        }

        public int getFailureCount() {
            return failureCount;
        }

        public void setFailureCount(int failureCount) {
            this.failureCount = failureCount;
        }

        public List<CredentialSyncDetail> getDetails() {
            return details;
        }

        public void setDetails(List<CredentialSyncDetail> details) {
            this.details = details;
        }

        @Override
        public String toString() {
            return "MultiCredentialSyncResult{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    ", totalCredentials=" + totalCredentials +
                    ", successCount=" + successCount +
                    ", failureCount=" + failureCount +
                    ", details=" + details +
                    '}';
        }
    }

    /**
     * 凭据同步详情
     */
    class CredentialSyncDetail {
        private CredentialType credentialType;
        private String credentialValue;
        private boolean success;
        private String keycloakUserId;
        private String message;
        private Exception exception;

        public CredentialSyncDetail() {}

        public CredentialSyncDetail(CredentialType credentialType, String credentialValue,
                                     boolean success, String keycloakUserId,
                                     String message, Exception exception) {
            this.credentialType = credentialType;
            this.credentialValue = credentialValue;
            this.success = success;
            this.keycloakUserId = keycloakUserId;
            this.message = message;
            this.exception = exception;
        }

        // Getter and Setter methods
        public CredentialType getCredentialType() {
            return credentialType;
        }

        public void setCredentialType(CredentialType credentialType) {
            this.credentialType = credentialType;
        }

        public String getCredentialValue() {
            return credentialValue;
        }

        public void setCredentialValue(String credentialValue) {
            this.credentialValue = credentialValue;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getKeycloakUserId() {
            return keycloakUserId;
        }

        public void setKeycloakUserId(String keycloakUserId) {
            this.keycloakUserId = keycloakUserId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
        
        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }

        @Override
        public String toString() {
            return "CredentialSyncDetail{" +
                    "credentialType=" + credentialType +
                    ", credentialValue='" + credentialValue + '\'' +
                    ", success=" + success +
                    ", keycloakUserId='" + keycloakUserId + '\'' +
                    ", message='" + message + '\'' +
                    ", exception=" + exception +
                    '}';
        }
    }
}