package com.jiuxi.admin.core.service;

/**
 * 人员账号服务适配器接口 - 为了兼容旧代码
 * 实际实现将委托给新的用户模块服务
 * 
 * @deprecated 请使用 com.jiuxi.module.user.app.service.PersonAccountApplicationService
 */
@Deprecated
public interface PersonAccountService {

    boolean checkUserNameExists(String userName);
    
    boolean checkPhoneExists(String phone);
    
    boolean checkEmailExists(String email);
    
    String generateUniqueUsername(String baseName);
    
    boolean validateUserInfo(String personId);
    
    // 添加缺失的方法以兼容现有调用
    boolean selectByUsername(String userName);
    
    boolean selectByUsernameAndAccountId(String userName, String accountId);
    
    boolean selectByPhone(String phone);
    
    void updatePhone(String personId, String phone);
    
    void updateIdCard(String personId, String idCard);
}