package com.jiuxi.admin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuxi.admin.core.bean.entity.TpKeycloakAccount;
import com.jiuxi.admin.core.mapper.TpKeycloakAccountMapper;
import com.jiuxi.admin.core.service.TpKeycloakAccountService;
import com.jiuxi.common.util.SnowflakeIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Keycloak账号关联表 服务实现类
 *
 * @author System
 * @since 2025-01-21
 */
@Service
public class TpKeycloakAccountServiceImpl extends ServiceImpl<TpKeycloakAccountMapper, TpKeycloakAccount> implements TpKeycloakAccountService {

    private static final Logger log = LoggerFactory.getLogger(TpKeycloakAccountServiceImpl.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public TpKeycloakAccount getByAccountId(String accountId) {
        return baseMapper.selectByAccountId(accountId);
    }

    @Override
    public TpKeycloakAccount getByKcUsername(String kcUsername) {
        return baseMapper.selectByKcUsername(kcUsername);
    }

    @Override
    public TpKeycloakAccount getByKcUserId(String kcUserId) {
        return baseMapper.selectByKcUserId(kcUserId);
    }

    @Override
    public boolean createOrUpdateKeycloakAccount(String accountId, String username, String password, String creator) {
        try {
            // 检查是否已存在
            TpKeycloakAccount existing = getByAccountId(accountId);
            String currentTime = LocalDateTime.now().format(FORMATTER);
            
            if (existing != null) {
                // 更新现有记录
                LambdaUpdateWrapper<TpKeycloakAccount> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(TpKeycloakAccount::getAccountId, accountId)
                           .set(TpKeycloakAccount::getKcUsername, username)
                           .set(TpKeycloakAccount::getKcPassword, password)
                           .set(TpKeycloakAccount::getUpdateTime, currentTime)
                           .set(TpKeycloakAccount::getUpdater, creator)
                           .set(TpKeycloakAccount::getEnabled, 1);
                return update(updateWrapper);
            } else {
                // 创建新记录
                TpKeycloakAccount keycloakAccount = new TpKeycloakAccount();
                keycloakAccount.setId(SnowflakeIdUtil.nextIdStr());
                keycloakAccount.setAccountId(accountId);
                keycloakAccount.setKcClientId("ps-be"); // 默认客户端ID
                keycloakAccount.setKcClientSecret(""); // 客户端密钥
                keycloakAccount.setKcGrantType("password"); // 默认授权类型
                keycloakAccount.setKcUsername(username);
                keycloakAccount.setKcPassword(password);
                keycloakAccount.setKcRealm("ps-realm"); // 默认Realm
                keycloakAccount.setKcServerUrl("http://localhost:8180"); // 默认服务器地址
                keycloakAccount.setEnabled(1);
                keycloakAccount.setCreateTime(currentTime);
                keycloakAccount.setUpdateTime(currentTime);
                keycloakAccount.setCreator(creator);
                keycloakAccount.setUpdater(creator);
                return save(keycloakAccount);
            }
        } catch (Exception e) {
            log.error("创建或更新Keycloak账号失败: accountId={}, username={}, error={}", accountId, username, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updateTokenInfo(String accountId, String accessToken, String refreshToken, 
                                 String tokenExpiresAt, String refreshExpiresAt) {
        try {
            return baseMapper.updateTokenInfo(accountId, accessToken, refreshToken, 
                                            tokenExpiresAt, refreshExpiresAt) > 0;
        } catch (Exception e) {
            log.error("更新Keycloak令牌信息失败: accountId={}, error={}", accountId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updateLastLoginTime(String accountId, String lastLoginTime) {
        try {
            return baseMapper.updateLastLoginTime(accountId, lastLoginTime) > 0;
        } catch (Exception e) {
            log.error("更新最后登录时间失败: accountId={}, error={}", accountId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean disableKeycloakAccount(String accountId) {
        try {
            LambdaUpdateWrapper<TpKeycloakAccount> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(TpKeycloakAccount::getAccountId, accountId)
                       .set(TpKeycloakAccount::getEnabled, 0)
                       .set(TpKeycloakAccount::getUpdateTime, LocalDateTime.now().format(FORMATTER));
            return update(updateWrapper);
        } catch (Exception e) {
            log.error("禁用Keycloak账号失败: accountId={}, error={}", accountId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean enableKeycloakAccount(String accountId) {
        try {
            LambdaUpdateWrapper<TpKeycloakAccount> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(TpKeycloakAccount::getAccountId, accountId)
                       .set(TpKeycloakAccount::getEnabled, 1)
                       .set(TpKeycloakAccount::getUpdateTime, LocalDateTime.now().format(FORMATTER));
            return update(updateWrapper);
        } catch (Exception e) {
            log.error("启用Keycloak账号失败: accountId={}, error={}", accountId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean existsByAccountId(String accountId) {
        TpKeycloakAccount account = getByAccountId(accountId);
        return account != null;
    }
}