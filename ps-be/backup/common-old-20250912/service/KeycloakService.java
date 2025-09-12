package com.jiuxi.common.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: KeycloakService
 * @Description: Keycloak集成服务接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface KeycloakService {
    
    /**
     * 用户认证
     * @param username 用户名
     * @param password 密码
     * @return 认证结果（包含token信息）
     */
    Map<String, Object> authenticate(String username, String password);
    
    /**
     * 刷新Token
     * @param refreshToken 刷新令牌
     * @return 新的token信息
     */
    Map<String, Object> refreshToken(String refreshToken);
    
    /**
     * 注销用户
     * @param refreshToken 刷新令牌
     * @return 注销结果
     */
    boolean logout(String refreshToken);
    
    /**
     * 验证Token
     * @param accessToken 访问令牌
     * @return 验证结果
     */
    boolean validateToken(String accessToken);
    
    /**
     * 获取用户信息
     * @param accessToken 访问令牌
     * @return 用户信息
     */
    Map<String, Object> getUserInfo(String accessToken);
    
    /**
     * 创建用户
     * @param userInfo 用户信息
     * @return 创建结果
     */
    boolean createUser(Map<String, Object> userInfo);
    
    /**
     * 更新用户
     * @param userId 用户ID
     * @param userInfo 用户信息
     * @return 更新结果
     */
    boolean updateUser(String userId, Map<String, Object> userInfo);
    
    /**
     * 删除用户
     * @param userId 用户ID
     * @return 删除结果
     */
    boolean deleteUser(String userId);
    
    /**
     * 启用/禁用用户
     * @param userId 用户ID
     * @param enabled 是否启用
     * @return 操作结果
     */
    boolean setUserEnabled(String userId, boolean enabled);
    
    /**
     * 重置用户密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @param temporary 是否为临时密码
     * @return 重置结果
     */
    boolean resetUserPassword(String userId, String newPassword, boolean temporary);
    
    /**
     * 获取用户列表
     * @param search 搜索条件
     * @param first 起始位置
     * @param max 最大数量
     * @return 用户列表
     */
    List<Map<String, Object>> getUsers(String search, Integer first, Integer max);
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户信息
     */
    Map<String, Object> getUserByUsername(String username);
    
    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @return 用户信息
     */
    Map<String, Object> getUserByEmail(String email);
    
    /**
     * 获取用户角色
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Map<String, Object>> getUserRoles(String userId);
    
    /**
     * 分配角色给用户
     * @param userId 用户ID
     * @param roleNames 角色名称列表
     * @return 分配结果
     */
    boolean assignRolesToUser(String userId, List<String> roleNames);
    
    /**
     * 移除用户角色
     * @param userId 用户ID
     * @param roleNames 角色名称列表
     * @return 移除结果
     */
    boolean removeRolesFromUser(String userId, List<String> roleNames);
    
    /**
     * 获取所有角色
     * @return 角色列表
     */
    List<Map<String, Object>> getAllRoles();
    
    /**
     * 创建角色
     * @param roleName 角色名称
     * @param description 角色描述
     * @return 创建结果
     */
    boolean createRole(String roleName, String description);
    
    /**
     * 删除角色
     * @param roleName 角色名称
     * @return 删除结果
     */
    boolean deleteRole(String roleName);
    
    /**
     * 获取用户组列表
     * @return 用户组列表
     */
    List<Map<String, Object>> getGroups();
    
    /**
     * 创建用户组
     * @param groupName 组名
     * @param attributes 组属性
     * @return 创建结果
     */
    boolean createGroup(String groupName, Map<String, Object> attributes);
    
    /**
     * 删除用户组
     * @param groupId 组ID
     * @return 删除结果
     */
    boolean deleteGroup(String groupId);
    
    /**
     * 将用户加入组
     * @param userId 用户ID
     * @param groupId 组ID
     * @return 操作结果
     */
    boolean addUserToGroup(String userId, String groupId);
    
    /**
     * 将用户从组中移除
     * @param userId 用户ID
     * @param groupId 组ID
     * @return 操作结果
     */
    boolean removeUserFromGroup(String userId, String groupId);
    
    /**
     * 获取用户会话
     * @param userId 用户ID
     * @return 会话列表
     */
    List<Map<String, Object>> getUserSessions(String userId);
    
    /**
     * 注销用户会话
     * @param sessionId 会话ID
     * @return 注销结果
     */
    boolean logoutSession(String sessionId);
    
    /**
     * 注销用户所有会话
     * @param userId 用户ID
     * @return 注销结果
     */
    boolean logoutAllUserSessions(String userId);
    
    /**
     * 获取客户端列表
     * @return 客户端列表
     */
    List<Map<String, Object>> getClients();
    
    /**
     * 获取客户端详情
     * @param clientId 客户端ID
     * @return 客户端信息
     */
    Map<String, Object> getClient(String clientId);
    
    /**
     * 创建客户端
     * @param clientInfo 客户端信息
     * @return 创建结果
     */
    boolean createClient(Map<String, Object> clientInfo);
    
    /**
     * 更新客户端
     * @param clientId 客户端ID
     * @param clientInfo 客户端信息
     * @return 更新结果
     */
    boolean updateClient(String clientId, Map<String, Object> clientInfo);
    
    /**
     * 删除客户端
     * @param clientId 客户端ID
     * @return 删除结果
     */
    boolean deleteClient(String clientId);
    
    /**
     * 获取Realm信息
     * @return Realm信息
     */
    Map<String, Object> getRealmInfo();
    
    /**
     * 更新Realm配置
     * @param realmConfig Realm配置
     * @return 更新结果
     */
    boolean updateRealmConfig(Map<String, Object> realmConfig);
    
    /**
     * 获取身份提供者列表
     * @return 身份提供者列表
     */
    List<Map<String, Object>> getIdentityProviders();
    
    /**
     * 创建身份提供者
     * @param providerInfo 提供者信息
     * @return 创建结果
     */
    boolean createIdentityProvider(Map<String, Object> providerInfo);
    
    /**
     * 删除身份提供者
     * @param providerId 提供者ID
     * @return 删除结果
     */
    boolean deleteIdentityProvider(String providerId);
}