package com.jiuxi.module.user.app.query.handler;

import com.jiuxi.module.user.app.query.dto.UserQuery;
import com.jiuxi.module.user.app.query.dto.UserPageQuery;
import com.jiuxi.module.user.app.dto.UserResponseDTO;
import com.jiuxi.module.user.domain.model.aggregate.User;
import com.jiuxi.module.user.domain.repo.UserRepository;
import com.jiuxi.module.user.app.assembler.UserAssembler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户查询处理器
 * 处理用户相关的查询操作
 */
@Component
public class UserQueryHandler {
    
    private final UserRepository userRepository;
    private final UserAssembler userAssembler;
    
    public UserQueryHandler(UserRepository userRepository, UserAssembler userAssembler) {
        this.userRepository = userRepository;
        this.userAssembler = userAssembler;
    }
    
    /**
     * 根据用户ID查询用户详情
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
     * 分页查询用户列表
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUserPage(UserPageQuery query) {
        // 构建查询关键字
        String keyword = buildSearchKeyword(query);
        
        // 调用仓储层分页查询
        List<User> users = userRepository.findUsers(
            query.getTenantId(),
            query.getDeptId(), 
            keyword,
            query.getCurrent(),
            query.getSize()
        );
        
        // 转换为响应DTO
        return users.stream()
                .map(userAssembler::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 查询用户总数
     */
    @Transactional(readOnly = true)
    public long getUserCount(UserQuery query) {
        // 构建查询关键字
        String keyword = buildSearchKeyword(query);
        
        // 调用仓储层查询总数
        return userRepository.countUsers(
            query.getTenantId(),
            query.getDeptId(),
            keyword
        );
    }
    
    /**
     * 构建搜索关键字
     */
    private String buildSearchKeyword(UserQuery query) {
        StringBuilder keyword = new StringBuilder();
        
        if (query.getPersonName() != null && !query.getPersonName().trim().isEmpty()) {
            if (keyword.length() > 0) keyword.append(" ");
            keyword.append(query.getPersonName().trim());
        }
        
        if (query.getPhone() != null && !query.getPhone().trim().isEmpty()) {
            if (keyword.length() > 0) keyword.append(" ");
            keyword.append(query.getPhone().trim());
        }
        
        if (query.getUsername() != null && !query.getUsername().trim().isEmpty()) {
            if (keyword.length() > 0) keyword.append(" ");
            keyword.append(query.getUsername().trim());
        }
        
        return keyword.length() > 0 ? keyword.toString() : null;
    }
}