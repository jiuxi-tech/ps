package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiuxi.admin.core.bean.entity.TpKeycloakAccount;

/**
 * Keycloak账号关联表 服务类
 *
 * @author System
 * @since 2025-01-21
 */
public interface TpKeycloakAccountService extends IService<TpKeycloakAccount> {

    /**
     * 根据账号ID查询Keycloak账号信息
     *
     * @param accountId 账号ID
     * @return TpKeycloakAccount
     */
    TpKeycloakAccount getByAccountId(String accountId);

    /**
     * 根据Keycloak用户名查询账号信息
     *
     * @param kcUsername Keycloak用户名
     * @return TpKeycloakAccount
     */
    TpKeycloakAccount getByKcUsername(String kcUsername);

    /**
     * 根据Keycloak用户ID查询账号信息
     *
     * @param kcUserId Keycloak用户ID
     * @return TpKeycloakAccount
     */
    TpKeycloakAccount getByKcUserId(String kcUserId);

    /**
     * 创建或更新Keycloak账号关联信息
     *
     * @param accountId 账号ID
     * @param username 用户名
     * @param password 密码
     * @param creator 创建人ID
     * @return 是否成功
     */
    boolean createOrUpdateKeycloakAccount(String accountId, String username, String password, String creator);

    /**
     * 更新Keycloak令牌信息
     *
     * @param accountId 账号ID
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     * @param tokenExpiresAt 令牌过期时间
     * @param refreshExpiresAt 刷新令牌过期时间
     * @return 是否成功
     */
    boolean updateTokenInfo(String accountId, String accessToken, String refreshToken, 
                           String tokenExpiresAt, String refreshExpiresAt);

    /**
     * 更新最后登录时间
     *
     * @param accountId 账号ID
     * @param lastLoginTime 最后登录时间
     * @return 是否成功
     */
    boolean updateLastLoginTime(String accountId, String lastLoginTime);

    /**
     * 禁用Keycloak账号
     *
     * @param accountId 账号ID
     * @return 是否成功
     */
    boolean disableKeycloakAccount(String accountId);

    /**
     * 启用Keycloak账号
     *
     * @param accountId 账号ID
     * @return 是否成功
     */
    boolean enableKeycloakAccount(String accountId);

    /**
     * 检查账号是否已存在Keycloak关联记录
     *
     * @param accountId 账号ID
     * @return 是否存在
     */
    boolean existsByAccountId(String accountId);
}