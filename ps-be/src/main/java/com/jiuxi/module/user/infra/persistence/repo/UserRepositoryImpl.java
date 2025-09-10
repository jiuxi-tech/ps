package com.jiuxi.module.user.infra.persistence.repo;

import com.jiuxi.module.user.domain.entity.User;
import com.jiuxi.module.user.domain.entity.UserProfile;
import com.jiuxi.module.user.domain.entity.UserAccount;
import com.jiuxi.module.user.domain.entity.ContactInfo;
import com.jiuxi.module.user.domain.entity.UserStatus;
import com.jiuxi.module.user.domain.entity.UserCategory;
import com.jiuxi.module.user.domain.entity.AccountStatus;
import com.jiuxi.module.user.domain.repo.UserRepository;
import com.jiuxi.module.user.infra.persistence.entity.UserPO;
import com.jiuxi.module.user.infra.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 用户仓储实现类
 * 负责用户聚合根的持久化操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    
    private final UserMapper userMapper;
    
    public UserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    @Override
    public User save(User user) {
        UserPO userPO = toUserPO(user);
        
        if (StringUtils.hasText(userPO.getPersonId())) {
            // 更新
            userPO.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(userPO);
        } else {
            // 新增
            userPO.setPersonId(UUID.randomUUID().toString());
            userPO.setCreateTime(LocalDateTime.now());
            userPO.setActived(1);
            userMapper.insert(userPO);
        }
        
        return toUser(userPO);
    }
    
    @Override
    public Optional<User> findById(String userId) {
        UserPO userPO = userMapper.selectById(userId);
        if (userPO == null || userPO.getActived() == 0) {
            return Optional.empty();
        }
        return Optional.of(toUser(userPO));
    }
    
    @Override
    public Optional<User> findByUsername(String username, String tenantId) {
        Optional<UserPO> userPOOpt = userMapper.findByUsername(username, tenantId);
        return userPOOpt.map(this::toUser);
    }
    
    @Override
    public Optional<User> findByEmail(String email, String tenantId) {
        Optional<UserPO> userPOOpt = userMapper.findByEmail(email, tenantId);
        return userPOOpt.map(this::toUser);
    }
    
    @Override
    public Optional<User> findByPhone(String phone, String tenantId) {
        Optional<UserPO> userPOOpt = userMapper.findByPhone(phone, tenantId);
        return userPOOpt.map(this::toUser);
    }
    
    @Override
    public Optional<User> findByIdCard(String idCard, String tenantId) {
        Optional<UserPO> userPOOpt = userMapper.findByIdCard(idCard, tenantId);
        return userPOOpt.map(this::toUser);
    }
    
    @Override
    public boolean existsByUsername(String username, String tenantId, String excludeUserId) {
        return userMapper.existsByUsername(username, tenantId, excludeUserId != null ? excludeUserId : "");
    }
    
    @Override
    public boolean existsByEmail(String email, String tenantId, String excludeUserId) {
        return userMapper.existsByEmail(email, tenantId, excludeUserId != null ? excludeUserId : "");
    }
    
    @Override
    public boolean existsByPhone(String phone, String tenantId, String excludeUserId) {
        return userMapper.existsByPhone(phone, tenantId, excludeUserId != null ? excludeUserId : "");
    }
    
    @Override
    public boolean existsByIdCard(String idCard, String tenantId, String excludeUserId) {
        return userMapper.existsByIdCard(idCard, tenantId, excludeUserId != null ? excludeUserId : "");
    }
    
    @Override
    public List<User> findByDeptId(String deptId) {
        List<UserPO> userPOs = userMapper.findByDeptId(deptId);
        return userPOs.stream()
                .map(this::toUser)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<User> findByTenantId(String tenantId) {
        List<UserPO> userPOs = userMapper.findByTenantId(tenantId);
        return userPOs.stream()
                .map(this::toUser)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(String userId) {
        UserPO userPO = new UserPO();
        userPO.setPersonId(userId);
        userPO.setActived(0);
        userPO.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(userPO);
    }
    
    /**
     * 将用户聚合根转换为持久化对象
     */
    private UserPO toUserPO(User user) {
        UserPO userPO = new UserPO();
        userPO.setPersonId(user.getPersonId());
        
        UserProfile profile = user.getProfile();
        if (profile != null) {
            userPO.setPersonName(profile.getPersonName());
            userPO.setProfilePhoto(profile.getProfilePhoto());
            userPO.setPersonNo(profile.getPersonNo());
            userPO.setSex(profile.getSex());
            userPO.setIdtype(profile.getIdType());
            userPO.setIdcard(profile.getIdCard());
            userPO.setNativePlace(profile.getNativePlace());
            userPO.setSafeprinNation(profile.getNation());
            userPO.setResume(profile.getResume());
            userPO.setBirthday(profile.getBirthday());
            userPO.setOffice(profile.getOffice());
        }
        
        // 账户信息从单独的账户表获取，这里暂时不处理
        
        ContactInfo contact = user.getContactInfo();
        if (contact != null) {
            userPO.setEmail(contact.getEmail());
            userPO.setPhone(contact.getPhone());
            userPO.setTel(contact.getTel());
            // 地址字段在扩展表中，这里暂时不处理
        }
        
        userPO.setAscnId(user.getDeptId());
        userPO.setCategory(user.getCategory() != null ? user.getCategory().name() : null);
        userPO.setActived(user.getStatus() != null && user.getStatus() == UserStatus.ACTIVE ? 1 : 0);
        userPO.setTenantId(user.getTenantId());
        userPO.setCreator(user.getCreator());
        userPO.setCreateTime(user.getCreateTime());
        userPO.setUpdator(user.getUpdator());
        userPO.setUpdateTime(user.getUpdateTime());
        
        return userPO;
    }
    
    /**
     * 将持久化对象转换为用户聚合根
     */
    private User toUser(UserPO userPO) {
        User user = new User();
        user.setPersonId(userPO.getPersonId());
        user.setDeptId(userPO.getAscnId());
        user.setCategory(userPO.getCategory() != null ? UserCategory.valueOf(userPO.getCategory()) : null);
        user.setStatus(userPO.getActived() != null && userPO.getActived() == 1 ? UserStatus.ACTIVE : UserStatus.INACTIVE);
        user.setTenantId(userPO.getTenantId());
        user.setCreator(userPO.getCreator());
        user.setCreateTime(userPO.getCreateTime());
        user.setUpdator(userPO.getUpdator());
        user.setUpdateTime(userPO.getUpdateTime());
        
        // 构建用户档案
        UserProfile profile = new UserProfile();
        profile.setPersonName(userPO.getPersonName());
        profile.setProfilePhoto(userPO.getProfilePhoto());
        profile.setPersonNo(userPO.getPersonNo());
        profile.setSex(userPO.getSex());
        profile.setIdType(userPO.getIdtype());
        profile.setIdCard(userPO.getIdcard());
        profile.setNativePlace(userPO.getNativePlace());
        profile.setNation(userPO.getSafeprinNation());
        profile.setResume(userPO.getResume());
        profile.setBirthday(userPO.getBirthday());
        profile.setOffice(userPO.getOffice());
        user.setProfile(profile);
        
        // 构建用户账户
        UserAccount account = new UserAccount();
        // 账户信息从单独的账户表获取
        user.setAccount(account);
        
        // 构建联系信息
        ContactInfo contact = new ContactInfo(
            userPO.getPhone(),
            userPO.getEmail(),
            userPO.getTel(),
            null // 地址从扩展表获取
        );
        user.setContactInfo(contact);
        
        return user;
    }
}