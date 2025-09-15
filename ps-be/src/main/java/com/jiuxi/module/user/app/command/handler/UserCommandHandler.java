package com.jiuxi.module.user.app.command.handler;

import com.jiuxi.module.user.app.command.dto.UserCreateCommand;
import com.jiuxi.module.user.app.command.dto.UserUpdateCommand;
import com.jiuxi.module.user.app.command.dto.UserDeleteCommand;
import com.jiuxi.module.user.app.command.dto.UserActivateCommand;
import com.jiuxi.module.user.app.command.dto.UserDeactivateCommand;
import com.jiuxi.module.user.domain.model.aggregate.User;
import com.jiuxi.module.user.domain.model.vo.UserProfile;
import com.jiuxi.module.user.domain.model.vo.ContactInfo;
import com.jiuxi.module.user.domain.repo.UserRepository;
import com.jiuxi.module.user.domain.service.UserDomainService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户命令处理器
 * 处理用户相关的命令操作
 */
@Component
public class UserCommandHandler {
    
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    
    public UserCommandHandler(UserRepository userRepository, UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
    }
    
    /**
     * 处理创建用户命令
     */
    @Transactional
    public String handleCreateUser(UserCreateCommand command) {
        // 创建用户资料
        UserProfile profile = new UserProfile();
        profile.setPersonName(command.getUsername());
        
        // 创建联系信息
        ContactInfo contactInfo = new ContactInfo(command.getPhone(), command.getEmail(), null);
        profile.setContactInfo(contactInfo);
        
        // 创建用户聚合根
        User user = User.create(
            null, // ID由系统生成
            profile,
            command.getCategory(),
            command.getOperator(),
            command.getTenantId()
        );
        
        // 验证用户创建规则
        userDomainService.validateUserCreation(profile, command.getUsername(), command.getTenantId());
        
        // 创建账户
        user.createAccount(command.getUsername(), command.getPassword(), command.getOperator());
        
        // 保存用户
        User savedUser = userRepository.save(user);
        return savedUser.getPersonId();
    }
    
    /**
     * 处理更新用户命令
     */
    @Transactional
    public void handleUpdateUser(UserUpdateCommand command) {
        // 查找用户
        User user = userRepository.findById(command.getPersonId())
            .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + command.getPersonId()));
        
        // 更新用户资料
        UserProfile profile = user.getProfile();
        if (profile == null) {
            profile = new UserProfile();
        }
        
        profile.setPersonName(command.getPersonName());
        
        // 更新联系信息
        ContactInfo contactInfo = profile.getContactInfo();
        if (contactInfo == null) {
            contactInfo = new ContactInfo();
        }
        contactInfo.setPhone(command.getPhone());
        contactInfo.setEmail(command.getEmail());
        contactInfo.setTel(command.getTel());
        profile.setContactInfo(contactInfo);
        user.setContactInfo(contactInfo);
        
        // 验证用户更新规则
        userDomainService.validateUserUpdate(user, profile, command.getTenantId());
        
        // 更新用户
        user.setProfile(profile);
        user.setUpdator(command.getOperator());
        user.setUpdateTime(LocalDateTime.now());
        
        // 保存用户
        userRepository.save(user);
    }
    
    /**
     * 处理删除用户命令
     */
    @Transactional
    public void handleDeleteUser(UserDeleteCommand command) {
        // 检查用户是否存在
        User user = userRepository.findById(command.getPersonId())
            .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + command.getPersonId()));
        
        // 检查是否可以删除
        if (!userDomainService.canDeleteUser(command.getPersonId())) {
            throw new IllegalStateException("用户不能被删除，可能存在关联数据");
        }
        
        // 删除用户
        userRepository.deleteById(command.getPersonId());
    }
    
    /**
     * 处理激活用户命令
     */
    @Transactional
    public void handleActivateUser(UserActivateCommand command) {
        // 查找用户
        User user = userRepository.findById(command.getPersonId())
            .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + command.getPersonId()));
        
        // 激活用户
        user.activate();
        user.setUpdator(command.getOperator());
        user.setUpdateTime(LocalDateTime.now());
        
        // 保存用户
        userRepository.save(user);
    }
    
    /**
     * 处理停用用户命令
     */
    @Transactional
    public void handleDeactivateUser(UserDeactivateCommand command) {
        // 查找用户
        User user = userRepository.findById(command.getPersonId())
            .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + command.getPersonId()));
        
        // 停用用户
        user.deactivate();
        user.setUpdator(command.getOperator());
        user.setUpdateTime(LocalDateTime.now());
        
        // 保存用户
        userRepository.save(user);
    }
}