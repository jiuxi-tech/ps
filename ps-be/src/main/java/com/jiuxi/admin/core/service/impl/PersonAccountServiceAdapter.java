package com.jiuxi.admin.core.service.impl;

import com.jiuxi.admin.core.service.PersonAccountService;
import com.jiuxi.module.user.app.service.PersonAccountApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 人员账号服务适配器实现 - 委托给新的用户模块服务
 * 
 * @deprecated 为了兼容旧代码而保留
 */
@Deprecated
@Service("personAccountService")
public class PersonAccountServiceAdapter implements PersonAccountService {

    @Autowired
    private PersonAccountApplicationService personAccountApplicationService;

    @Override
    public boolean checkUserNameExists(String userName) {
        return personAccountApplicationService.selectByUsername(userName);
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        return personAccountApplicationService.selectByPhone(phone);
    }

    @Override
    public boolean checkEmailExists(String email) {
        // PersonAccountApplicationService没有这个方法，返回false
        return false;
    }

    @Override
    public String generateUniqueUsername(String baseName) {
        // PersonAccountApplicationService没有这个方法，返回原始名称
        return baseName;
    }

    @Override
    public boolean validateUserInfo(String personId) {
        // PersonAccountApplicationService没有这个方法，返回true
        return true;
    }
    
    @Override
    public boolean selectByUsername(String userName) {
        return personAccountApplicationService.selectByUsername(userName);
    }
    
    @Override
    public boolean selectByUsernameAndAccountId(String userName, String accountId) {
        return personAccountApplicationService.selectByUsernameAndAccountId(userName, accountId);
    }
    
    @Override
    public boolean selectByPhone(String phone) {
        return personAccountApplicationService.selectByPhone(phone);
    }
    
    @Override
    public void updatePhone(String personId, String phone) {
        personAccountApplicationService.updatePhone(personId, phone);
    }
}