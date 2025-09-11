package com.jiuxi.module.user.app.service;

import com.jiuxi.module.user.app.assembler.UserAssembler;
import com.jiuxi.module.user.app.dto.UserCreateDTO;
import com.jiuxi.module.user.app.dto.UserQueryDTO;
import com.jiuxi.module.user.app.dto.UserResponseDTO;
import com.jiuxi.module.user.app.dto.UserUpdateDTO;
import com.jiuxi.module.user.domain.entity.User;
import com.jiuxi.module.user.domain.event.UserCreatedEvent;
import com.jiuxi.module.user.domain.event.UserDeletedEvent;
import com.jiuxi.module.user.domain.event.UserUpdatedEvent;
import com.jiuxi.module.user.domain.repo.UserRepository;
import com.jiuxi.module.user.domain.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户应用服务
 * 协调领域对象完成业务用例
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Service
@Transactional
public class UserApplicationService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserDomainService userDomainService;
    
    @Autowired
    private UserAssembler userAssembler;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    /**
     * 创建用户
     */
    public String createUser(UserCreateDTO createDTO, String tenantId, String operator) {
        // 转换为领域对象
        User user = userAssembler.toUser(createDTO);
        
        // 生成用户ID和编号
        String personId = generatePersonId();
        user.setPersonId(personId);
        
        if (user.getProfile().getPersonNo() == null) {
            String personNo = userDomainService.generateUserNo(createDTO.getDeptId());
            user.getProfile().setPersonNo(personNo);
        }
        
        // 领域验证
        userDomainService.validateUserCreation(user.getProfile(), createDTO.getUsername(), tenantId);
        
        // 创建账户
        user.createAccount(createDTO.getUsername(), createDTO.getPassword(), operator);
        
        // 设置审计信息
        user.setCreator(operator);
        user.setCreateTime(LocalDateTime.now());
        
        // 保存用户
        User savedUser = userRepository.save(user);
        
        // 发布领域事件已在User.create()方法中处理，此处无需重复发布
        // eventPublisher.publishEvent(new UserCreatedEvent(savedUser, operator));
        
        return savedUser.getPersonId();
    }
    
    /**
     * 更新用户
     */
    public void updateUser(UserUpdateDTO updateDTO, String tenantId, String operator) {
        // 查找用户
        Optional<User> userOpt = userRepository.findById(updateDTO.getPersonId());
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("用户不存在: " + updateDTO.getPersonId());
        }
        
        User user = userOpt.get();
        
        // 领域验证
        User tempUser = new User();
        userAssembler.updateUser(tempUser, updateDTO);
        userDomainService.validateUserUpdate(user, tempUser.getProfile(), user.getTenantId());
        
        // 更新用户信息
        userAssembler.updateUser(user, updateDTO);
        user.setUpdator(operator);
        user.setUpdateTime(LocalDateTime.now());
        
        // 保存用户
        User savedUser = userRepository.save(user);
        
        // 发布领域事件
        eventPublisher.publishEvent(new UserUpdatedEvent(savedUser, operator));
    }
    
    /**
     * 删除用户
     */
    public void deleteUser(String personId, String operator) {
        // 查找用户
        Optional<User> userOpt = userRepository.findById(personId);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("用户不存在: " + personId);
        }
        
        User user = userOpt.get();
        
        // 检查是否可以删除
        if (!userDomainService.canDeleteUser(personId)) {
            throw new IllegalStateException("用户不能被删除，可能存在关联数据");
        }
        
        String userName = user.getProfile().getPersonName();
        
        // 删除用户
        userRepository.deleteById(personId);
        
        // 发布领域事件
        eventPublisher.publishEvent(new UserDeletedEvent(personId, userName, operator));
    }
    
    /**
     * 批量删除用户
     */
    public void deleteUsers(List<String> personIds, String operator) {
        for (String personId : personIds) {
            deleteUser(personId, operator);
        }
    }
    
    /**
     * 查询用户详情
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(String personId) {
        Optional<User> userOpt = userRepository.findById(personId);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("用户不存在: " + personId);
        }
        
        return userAssembler.toResponseDTO(userOpt.get());
    }
    
    /**
     * 根据用户名查询用户
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByUsername(String username, String tenantId) {
        Optional<User> userOpt = userRepository.findByUsername(username, tenantId);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("用户不存在: " + username);
        }
        
        return userAssembler.toResponseDTO(userOpt.get());
    }
    
    /**
     * 分页查询用户列表
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUserPage(UserQueryDTO queryDTO) {
        // 构建查询关键字
        String keyword = buildSearchKeyword(queryDTO);
        
        // 调用仓储层分页查询
        List<User> users = userRepository.findUsers(
            queryDTO.getTenantId(),
            queryDTO.getDeptId(), 
            keyword,
            queryDTO.getCurrent(),
            queryDTO.getSize()
        );
        
        // 转换为响应DTO
        return users.stream()
                .map(userAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 构建搜索关键字
     */
    private String buildSearchKeyword(UserQueryDTO queryDTO) {
        StringBuilder keyword = new StringBuilder();
        
        if (queryDTO.getPersonName() != null && !queryDTO.getPersonName().trim().isEmpty()) {
            if (keyword.length() > 0) keyword.append(" ");
            keyword.append(queryDTO.getPersonName().trim());
        }
        
        if (queryDTO.getPhone() != null && !queryDTO.getPhone().trim().isEmpty()) {
            if (keyword.length() > 0) keyword.append(" ");
            keyword.append(queryDTO.getPhone().trim());
        }
        
        if (queryDTO.getUsername() != null && !queryDTO.getUsername().trim().isEmpty()) {
            if (keyword.length() > 0) keyword.append(" ");
            keyword.append(queryDTO.getUsername().trim());
        }
        
        return keyword.length() > 0 ? keyword.toString() : null;
    }
    
    /**
     * 查询用户总数
     */
    @Transactional(readOnly = true)
    public long getUserCount(UserQueryDTO queryDTO) {
        // 构建查询关键字
        String keyword = buildSearchKeyword(queryDTO);
        
        // 调用仓储层查询总数
        return userRepository.countUsers(
            queryDTO.getTenantId(),
            queryDTO.getDeptId(),
            keyword
        );
    }
    
    /**
     * 根据部门查询用户列表
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByDepartment(String deptId) {
        List<User> users = userRepository.findByDeptId(deptId);
        return users.stream()
                .map(userAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 激活用户
     */
    public void activateUser(String personId, String operator) {
        Optional<User> userOpt = userRepository.findById(personId);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("用户不存在: " + personId);
        }
        
        User user = userOpt.get();
        user.activate();
        user.setUpdator(operator);
        
        userRepository.save(user);
        
        // 发布领域事件
        eventPublisher.publishEvent(new UserUpdatedEvent(user, operator));
    }
    
    /**
     * 停用用户
     */
    public void deactivateUser(String personId, String operator) {
        Optional<User> userOpt = userRepository.findById(personId);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("用户不存在: " + personId);
        }
        
        User user = userOpt.get();
        user.deactivate();
        user.setUpdator(operator);
        
        userRepository.save(user);
        
        // 发布领域事件
        eventPublisher.publishEvent(new UserUpdatedEvent(user, operator));
    }
    
    /**
     * 重置用户密码
     */
    public void resetPassword(String personId, String newPassword, String operator) {
        Optional<User> userOpt = userRepository.findById(personId);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("用户不存在: " + personId);
        }
        
        User user = userOpt.get();
        if (!user.hasAccount()) {
            throw new IllegalStateException("用户没有账户信息");
        }
        
        // 更新密码（这里应该加密处理）
        user.getAccount().updatePassword(newPassword);
        user.setUpdator(operator);
        
        userRepository.save(user);
        
        // 发布领域事件
        eventPublisher.publishEvent(new UserUpdatedEvent(user, operator));
    }
    
    /**
     * 生成用户ID
     */
    private String generatePersonId() {
        // 简单的ID生成策略，实际项目中应该使用更复杂的ID生成器
        return String.valueOf(System.currentTimeMillis());
    }
}