package com.jiuxi.module.user.app.assembler;

import com.jiuxi.module.user.app.dto.UserCreateDTO;
import com.jiuxi.module.user.app.dto.UserResponseDTO;
import com.jiuxi.module.user.app.dto.UserUpdateDTO;
import com.jiuxi.module.user.domain.model.aggregate.User;
import com.jiuxi.module.user.domain.model.entity.UserAccount;
import com.jiuxi.module.user.domain.model.vo.ContactInfo;
import com.jiuxi.module.user.domain.model.vo.UserCategory;
import com.jiuxi.module.user.domain.model.vo.UserProfile;
import com.jiuxi.module.user.domain.model.vo.UserStatus;
import org.springframework.stereotype.Component;

/**
 * 用户装配器
 * 负责DTO与领域对象之间的转换
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Component
public class UserAssembler {
    
    /**
     * 将创建DTO转换为用户领域对象
     */
    public User toUser(UserCreateDTO dto) {
        // 创建联系信息
        ContactInfo contactInfo = new ContactInfo(dto.getPhone(), dto.getEmail(), dto.getTel());
        
        // 创建用户资料
        UserProfile profile = new UserProfile(dto.getPersonName(), dto.getPhone(), dto.getEmail(), dto.getTel());
        profile.setPersonNo(dto.getPersonNo());
        profile.setSex(dto.getSex());
        profile.setIdType(dto.getIdType());
        profile.setIdCard(dto.getIdCard());
        profile.setNativePlace(dto.getNativePlace());
        profile.setNation(dto.getNation());
        profile.setResume(dto.getResume());
        profile.setBirthday(dto.getBirthday());
        profile.setOffice(dto.getOffice());
        profile.setProfilePhoto(dto.getProfilePhoto());
        profile.setContactInfo(contactInfo);
        
        // 创建用户
        User user = new User();
        user.setProfile(profile);
        user.setStatus(UserStatus.ACTIVE);
        
        // 设置用户类别
        if (dto.getCategory() != null) {
            try {
                user.setCategory(UserCategory.fromCode(dto.getCategory()));
            } catch (IllegalArgumentException e) {
                user.setCategory(UserCategory.PERSONAL); // 默认为个人用户
            }
        } else {
            user.setCategory(UserCategory.PERSONAL);
        }
        
        return user;
    }
    
    /**
     * 将更新DTO应用到用户领域对象
     */
    public void updateUser(User user, UserUpdateDTO dto) {
        // 更新联系信息
        ContactInfo contactInfo = new ContactInfo(dto.getPhone(), dto.getEmail(), dto.getTel());
        
        // 更新用户资料
        UserProfile profile = user.getProfile();
        if (profile == null) {
            profile = new UserProfile();
        }
        
        profile.setPersonName(dto.getPersonName());
        profile.setPersonNo(dto.getPersonNo());
        profile.setSex(dto.getSex());
        profile.setIdType(dto.getIdType());
        profile.setIdCard(dto.getIdCard());
        profile.setNativePlace(dto.getNativePlace());
        profile.setNation(dto.getNation());
        profile.setResume(dto.getResume());
        profile.setBirthday(dto.getBirthday());
        profile.setOffice(dto.getOffice());
        profile.setProfilePhoto(dto.getProfilePhoto());
        profile.setContactInfo(contactInfo);
        
        user.updateProfile(profile, "system", new String[]{"profile"}, "资料更新");
    }
    
    /**
     * 将用户领域对象转换为响应DTO
     */
    public UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        
        // 基本信息
        dto.setPersonId(user.getPersonId());
        dto.setStatus(user.getStatus().getValue().toString());
        dto.setStatusName(user.getStatus().getDescription());
        dto.setCategory(user.getCategory().getCode());
        dto.setCategoryName(user.getCategory().getDescription());
        
        // 用户资料信息
        if (user.getProfile() != null) {
            UserProfile profile = user.getProfile();
            dto.setPersonName(profile.getPersonName());
            dto.setPersonNo(profile.getPersonNo());
            dto.setSex(profile.getSex());
            dto.setSexName(profile.getSexDescription());
            dto.setIdType(profile.getIdType());
            dto.setIdCard(profile.getIdCard());
            dto.setNativePlace(profile.getNativePlace());
            dto.setNation(profile.getNation());
            dto.setResume(profile.getResume());
            dto.setBirthday(profile.getBirthday());
            dto.setOffice(profile.getOffice());
            dto.setProfilePhoto(profile.getProfilePhoto());
            
            // 联系信息
            if (profile.getContactInfo() != null) {
                ContactInfo contactInfo = profile.getContactInfo();
                dto.setPhone(contactInfo.getPhone());
                dto.setEmail(contactInfo.getEmail());
                dto.setTel(contactInfo.getTel());
            }
        }
        
        // 账户信息
        if (user.getAccount() != null) {
            dto.setAccountId(user.getAccount().getAccountId());
            dto.setUsername(user.getAccount().getUsername());
            dto.setAccountLocked(user.getAccount().getLocked());
            dto.setAccountEnabled(user.getAccount().getEnabled());
            dto.setExpiredTime(user.getAccount().getExpiredTime());
            dto.setKeycloakId(user.getAccount().getKeycloakId());
        }
        
        // 审计信息
        dto.setCreator(user.getCreator());
        dto.setCreateTime(user.getCreateTime());
        dto.setUpdator(user.getUpdator());
        dto.setUpdateTime(user.getUpdateTime());
        
        return dto;
    }
    
    /**
     * 获取性别描述
     */
    private String getSexDescription(Integer sex) {
        if (sex == null) return "未知";
        switch (sex) {
            case 0: return "保密";
            case 1: return "男";
            case 2: return "女";
            default: return "未知";
        }
    }
}