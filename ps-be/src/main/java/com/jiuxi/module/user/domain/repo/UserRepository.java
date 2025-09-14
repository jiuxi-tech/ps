package com.jiuxi.module.user.domain.repo;

import com.jiuxi.module.user.domain.model.aggregate.User;
import java.util.List;
import java.util.Optional;

/**
 * 用户仓储接口
 * 领域层的仓储接口，定义用户聚合根的持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public interface UserRepository {
    
    /**
     * 保存用户
     */
    User save(User user);
    
    /**
     * 根据用户ID查找用户
     */
    Optional<User> findById(String userId);
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username, String tenantId);
    
    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone, String tenantId);
    
    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email, String tenantId);
    
    /**
     * 根据身份证号查找用户
     */
    Optional<User> findByIdCard(String idCard, String tenantId);
    
    /**
     * 根据部门ID查找用户列表
     */
    List<User> findByDeptId(String deptId);
    
    /**
     * 根据租户ID查找用户列表
     */
    List<User> findByTenantId(String tenantId);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username, String tenantId, String excludeUserId);
    
    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone, String tenantId, String excludeUserId);
    
    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email, String tenantId, String excludeUserId);
    
    /**
     * 检查身份证号是否存在
     */
    boolean existsByIdCard(String idCard, String tenantId, String excludeUserId);
    
    /**
     * 删除用户（逻辑删除）
     */
    void deleteById(String userId);
    
    /**
     * 分页查询用户
     */
    List<User> findUsers(String tenantId, String deptId, String keyword, Integer pageNum, Integer pageSize);
    
    /**
     * 统计用户数量
     */
    long countUsers(String tenantId, String deptId, String keyword);
    
    /**
     * 查找有账户的用户
     */
    List<User> findUsersWithAccount(String tenantId);
    
    /**
     * 查找某个状态的用户
     */
    List<User> findByStatus(Integer status, String tenantId);
}