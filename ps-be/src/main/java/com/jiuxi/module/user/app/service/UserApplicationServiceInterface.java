package com.jiuxi.module.user.app.service;

import com.jiuxi.module.user.app.dto.UserCreateDTO;
import com.jiuxi.module.user.app.dto.UserQueryDTO;
import com.jiuxi.module.user.app.dto.UserResponseDTO;
import com.jiuxi.module.user.app.dto.UserUpdateDTO;

import java.util.List;

/**
 * 用户应用服务接口
 * 定义用户相关的业务用例接口
 * 
 * @author DDD Refactor
 * @date 2025-09-14
 */
public interface UserApplicationServiceInterface {
    
    /**
     * 创建用户
     */
    String createUser(UserCreateDTO createDTO, String tenantId, String operator);
    
    /**
     * 更新用户
     */
    void updateUser(UserUpdateDTO updateDTO, String tenantId, String operator);
    
    /**
     * 删除用户
     */
    void deleteUser(String personId, String operator);
    
    /**
     * 批量删除用户
     */
    void deleteUsers(List<String> personIds, String operator);
    
    /**
     * 查询用户详情
     */
    UserResponseDTO getUserById(String personId);
    
    /**
     * 根据用户名查询用户
     */
    UserResponseDTO getUserByUsername(String username, String tenantId);
    
    /**
     * 分页查询用户列表
     */
    List<UserResponseDTO> getUserPage(UserQueryDTO queryDTO);
    
    /**
     * 查询用户总数
     */
    long getUserCount(UserQueryDTO queryDTO);
    
    /**
     * 根据部门查询用户列表
     */
    List<UserResponseDTO> getUsersByDepartment(String deptId);
    
    /**
     * 激活用户
     */
    void activateUser(String personId, String operator);
    
    /**
     * 停用用户
     */
    void deactivateUser(String personId, String operator);
    
    /**
     * 重置用户密码
     */
    void resetPassword(String personId, String newPassword, String operator);
}