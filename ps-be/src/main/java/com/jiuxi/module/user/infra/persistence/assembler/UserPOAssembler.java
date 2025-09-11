package com.jiuxi.module.user.infra.persistence.assembler;

import com.jiuxi.module.user.domain.entity.User;
import com.jiuxi.module.user.domain.entity.UserProfile;
import com.jiuxi.module.user.domain.entity.UserAccount;
import com.jiuxi.module.user.domain.entity.ContactInfo;
import com.jiuxi.module.user.domain.entity.UserStatus;
import com.jiuxi.module.user.domain.entity.UserCategory;
import com.jiuxi.module.user.domain.entity.AccountStatus;
import com.jiuxi.module.user.infra.persistence.entity.UserPO;
import com.jiuxi.module.user.infra.persistence.entity.AccountPO;
import org.springframework.stereotype.Component;

/**
 * 用户领域模型与持久化对象转换器
 * 
 * @author DDD Refactor Phase 4.3.1.2
 * @date 2025-09-11
 */
@Component
public class UserPOAssembler {
    
    /**
     * 将User领域模型转换为UserPO
     */
    public UserPO toUserPO(User user) {
        if (user == null) {
            return null;
        }
        
        UserPO userPO = new UserPO();
        userPO.setPersonId(user.getPersonId());
        
        // 基本信息
        userPO.setTenantId(user.getTenantId());
        userPO.setCreator(user.getCreator());
        userPO.setCreateTime(user.getCreateTime());
        userPO.setUpdator(user.getUpdator());
        userPO.setUpdateTime(user.getUpdateTime());
        userPO.setAscnId(user.getDeptId());
        
        // 状态和类别
        if (user.getStatus() != null) {
            userPO.setActived(user.getStatus() == UserStatus.ACTIVE ? 1 : 0);
        }
        if (user.getCategory() != null) {
            userPO.setCategory(user.getCategory().name());
        }
        
        // 用户资料
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
        
        // 联系信息
        ContactInfo contact = user.getContactInfo();
        if (contact != null) {
            userPO.setPhone(contact.getPhone());
            userPO.setEmail(contact.getEmail());
            userPO.setTel(contact.getTel());
        }
        
        return userPO;
    }
    
    /**
     * 将UserAccount领域模型转换为AccountPO
     */
    public AccountPO toAccountPO(UserAccount account) {
        if (account == null) {
            return null;
        }
        
        AccountPO accountPO = new AccountPO();
        accountPO.setAccountId(account.getAccountId());
        accountPO.setUsername(account.getUsername());
        accountPO.setUserpwd(account.getPassword());
        accountPO.setPersonId(account.getPersonId());
        accountPO.setExpiredTime(account.getExpiredTime());
        accountPO.setLocked(account.getLocked());
        accountPO.setEnabled(account.getEnabled());
        accountPO.setWeixin(account.getWeixinOpenId());
        accountPO.setDingding(account.getDingdingId());
        accountPO.setThreeId(account.getThirdId());
        accountPO.setCreateTime(account.getCreateTime());
        accountPO.setUpdateTime(account.getUpdateTime());
        
        // 状态转换
        if (account.getStatus() != null) {
            accountPO.setActived(account.getStatus() == AccountStatus.ACTIVE ? 1 : 0);
        }
        
        // Keycloak ID存储在扩展字段中
        accountPO.setExtend01(account.getKeycloakId());
        
        return accountPO;
    }
    
    /**
     * 将UserPO和AccountPO转换为User领域模型
     */
    public User toUser(UserPO userPO, AccountPO accountPO) {
        if (userPO == null) {
            return null;
        }
        
        User user = new User();
        user.setPersonId(userPO.getPersonId());
        user.setDeptId(userPO.getAscnId());
        user.setTenantId(userPO.getTenantId());
        user.setCreator(userPO.getCreator());
        user.setCreateTime(userPO.getCreateTime());
        user.setUpdator(userPO.getUpdator());
        user.setUpdateTime(userPO.getUpdateTime());
        
        // 状态和类别转换
        user.setStatus(userPO.getActived() != null && userPO.getActived() == 1 ? 
                      UserStatus.ACTIVE : UserStatus.INACTIVE);
        
        if (userPO.getCategory() != null) {
            try {
                user.setCategory(UserCategory.valueOf(userPO.getCategory()));
            } catch (IllegalArgumentException e) {
                // 如果类别值无效，设置为默认值
                user.setCategory(UserCategory.PERSONAL);
            }
        }
        
        // 构建用户资料
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
        
        // 构建联系信息
        ContactInfo contact = new ContactInfo(
            userPO.getPhone(),
            userPO.getEmail(),
            userPO.getTel()
        );
        profile.setContactInfo(contact);
        user.setProfile(profile);
        user.setContactInfo(contact);
        
        // 构建用户账户
        if (accountPO != null) {
            UserAccount account = toUserAccount(accountPO);
            user.setAccount(account);
        }
        
        return user;
    }
    
    /**
     * 将AccountPO转换为UserAccount领域模型
     */
    public UserAccount toUserAccount(AccountPO accountPO) {
        if (accountPO == null) {
            return null;
        }
        
        UserAccount account = new UserAccount();
        account.setAccountId(accountPO.getAccountId());
        account.setUsername(accountPO.getUsername());
        account.setPassword(accountPO.getUserpwd());
        account.setPersonId(accountPO.getPersonId());
        account.setExpiredTime(accountPO.getExpiredTime());
        account.setLocked(accountPO.getLocked());
        account.setEnabled(accountPO.getEnabled());
        account.setWeixinOpenId(accountPO.getWeixin());
        account.setDingdingId(accountPO.getDingding());
        account.setThirdId(accountPO.getThreeId());
        account.setCreateTime(accountPO.getCreateTime());
        account.setUpdateTime(accountPO.getUpdateTime());
        
        // 状态转换
        account.setStatus(accountPO.getActived() != null && accountPO.getActived() == 1 ? 
                         AccountStatus.ACTIVE : AccountStatus.INACTIVE);
        
        // Keycloak ID从扩展字段获取
        account.setKeycloakId(accountPO.getExtend01());
        
        return account;
    }
}