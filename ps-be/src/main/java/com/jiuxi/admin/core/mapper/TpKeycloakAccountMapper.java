package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuxi.admin.core.bean.entity.TpKeycloakAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Keycloak账号关联表 Mapper 接口
 *
 * @author System
 * @since 2025-01-21
 */
@Mapper
public interface TpKeycloakAccountMapper extends BaseMapper<TpKeycloakAccount> {

    /**
     * 根据账号ID查询Keycloak账号信息
     *
     * @param accountId 账号ID
     * @return TpKeycloakAccount
     */
    TpKeycloakAccount selectByAccountId(@Param("accountId") String accountId);

    /**
     * 根据Keycloak用户名查询账号信息
     *
     * @param kcUsername Keycloak用户名
     * @return TpKeycloakAccount
     */
    TpKeycloakAccount selectByKcUsername(@Param("kcUsername") String kcUsername);

    /**
     * 根据Keycloak用户ID查询账号信息
     *
     * @param kcUserId Keycloak用户ID
     * @return TpKeycloakAccount
     */
    TpKeycloakAccount selectByKcUserId(@Param("kcUserId") String kcUserId);

    /**
     * 更新Keycloak令牌信息
     *
     * @param accountId 账号ID
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     * @param tokenExpiresAt 令牌过期时间
     * @param refreshExpiresAt 刷新令牌过期时间
     * @return 更新行数
     */
    int updateTokenInfo(@Param("accountId") String accountId,
                       @Param("accessToken") String accessToken,
                       @Param("refreshToken") String refreshToken,
                       @Param("tokenExpiresAt") String tokenExpiresAt,
                       @Param("refreshExpiresAt") String refreshExpiresAt);

    /**
     * 更新最后登录时间
     *
     * @param accountId 账号ID
     * @param lastLoginTime 最后登录时间
     * @return 更新行数
     */
    int updateLastLoginTime(@Param("accountId") String accountId,
                           @Param("lastLoginTime") String lastLoginTime);
}