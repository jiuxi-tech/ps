package com.jiuxi.module.user.domain.service;

import com.jiuxi.module.user.domain.entity.User;
import com.jiuxi.module.user.domain.entity.UserProfile;
import com.jiuxi.module.user.domain.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户领域服务
 * 处理用户相关的业务逻辑和规则
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Service
public class UserDomainService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 验证用户创建规则
     */
    public void validateUserCreation(UserProfile profile, String username, String tenantId) {
        // 验证用户名唯一性
        if (userRepository.existsByUsername(username, tenantId, null)) {
            throw new IllegalArgumentException("用户名已存在: " + username);
        }
        
        // 验证手机号唯一性
        if (profile.getContactInfo() != null && profile.getContactInfo().getPhone() != null) {
            if (userRepository.existsByPhone(profile.getContactInfo().getPhone(), tenantId, null)) {
                throw new IllegalArgumentException("手机号已存在: " + profile.getContactInfo().getPhone());
            }
        }
        
        // 验证邮箱唯一性
        if (profile.getContactInfo() != null && profile.getContactInfo().getEmail() != null) {
            if (userRepository.existsByEmail(profile.getContactInfo().getEmail(), tenantId, null)) {
                throw new IllegalArgumentException("邮箱已存在: " + profile.getContactInfo().getEmail());
            }
        }
        
        // 验证身份证号唯一性
        if (profile.getIdCard() != null) {
            if (userRepository.existsByIdCard(profile.getIdCard(), tenantId, null)) {
                throw new IllegalArgumentException("身份证号已存在: " + profile.getIdCard());
            }
        }
        
        // 验证用户基本信息完整性
        if (!profile.isValidName()) {
            throw new IllegalArgumentException("用户姓名不能为空");
        }
        
        // 验证联系方式有效性
        if (profile.getContactInfo() != null) {
            if (profile.getContactInfo().getPhone() != null && !profile.getContactInfo().isValidPhone()) {
                throw new IllegalArgumentException("手机号格式不正确");
            }
            
            if (profile.getContactInfo().getEmail() != null && !profile.getContactInfo().isValidEmail()) {
                throw new IllegalArgumentException("邮箱格式不正确");
            }
        }
    }
    
    /**
     * 验证用户更新规则
     */
    public void validateUserUpdate(User user, UserProfile newProfile, String tenantId) {
        // 验证手机号变更
        if (newProfile.getContactInfo() != null && newProfile.getContactInfo().getPhone() != null) {
            String oldPhone = user.getProfile().getContactInfo() != null ? 
                user.getProfile().getContactInfo().getPhone() : null;
            String newPhone = newProfile.getContactInfo().getPhone();
            
            if (!newPhone.equals(oldPhone) && userRepository.existsByPhone(newPhone, tenantId, user.getPersonId())) {
                throw new IllegalArgumentException("手机号已被其他用户使用: " + newPhone);
            }
        }
        
        // 验证邮箱变更
        if (newProfile.getContactInfo() != null && newProfile.getContactInfo().getEmail() != null) {
            String oldEmail = user.getProfile().getContactInfo() != null ? 
                user.getProfile().getContactInfo().getEmail() : null;
            String newEmail = newProfile.getContactInfo().getEmail();
            
            if (!newEmail.equals(oldEmail) && userRepository.existsByEmail(newEmail, tenantId, user.getPersonId())) {
                throw new IllegalArgumentException("邮箱已被其他用户使用: " + newEmail);
            }
        }
        
        // 验证身份证号变更
        if (newProfile.getIdCard() != null) {
            String oldIdCard = user.getProfile().getIdCard();
            String newIdCard = newProfile.getIdCard();
            
            if (!newIdCard.equals(oldIdCard) && userRepository.existsByIdCard(newIdCard, tenantId, user.getPersonId())) {
                throw new IllegalArgumentException("身份证号已被其他用户使用: " + newIdCard);
            }
        }
    }
    
    /**
     * 验证账户创建规则
     */
    public void validateAccountCreation(User user, String username, String password, String tenantId) {
        // 检查用户是否已有账户
        if (user.hasAccount()) {
            throw new IllegalStateException("用户已存在账户，不能重复创建");
        }
        
        // 验证用户名格式
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        
        if (username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("用户名长度必须在3-20个字符之间");
        }
        
        // 验证密码格式
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        if (password.length() < 6) {
            throw new IllegalArgumentException("密码长度不能少于6位");
        }
        
        // 验证用户名唯一性
        if (userRepository.existsByUsername(username, tenantId, null)) {
            throw new IllegalArgumentException("用户名已存在: " + username);
        }
    }
    
    /**
     * 生成用户编号
     */
    public String generateUserNo(String deptCode) {
        // 根据部门代码和时间戳生成用户编号
        long timestamp = System.currentTimeMillis();
        return deptCode + "_" + timestamp % 1000000;
    }
    
    /**
     * 检查用户是否可以删除
     */
    public boolean canDeleteUser(String personId) {
        // 可以添加更多业务规则，比如检查用户是否有关联数据
        // 这里简化处理，所有用户都可以删除
        return true;
    }
}