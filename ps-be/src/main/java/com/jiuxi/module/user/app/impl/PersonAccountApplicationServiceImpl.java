package com.jiuxi.module.user.app.impl;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.bean.entity.TpAccount;
import com.jiuxi.admin.core.bean.entity.TpPersonBasicinfo;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import com.jiuxi.module.user.infra.persistence.mapper.UserAccountMapper;
import com.jiuxi.module.user.infra.persistence.mapper.UserPersonMapper;
import com.jiuxi.admin.core.mapper.TpPersonBasicinfoMapper;
import org.springframework.stereotype.Service;
import com.jiuxi.module.user.app.service.PersonAccountApplicationService;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.PhoneEncryptionUtils;
// import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 可以被替换掉，人员，账号相关接口
 * @ClassName: PersonAccountServiceImpl
 * @Author: pand
 * @Date: 2021-06-18 14:29
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
@Service
public class PersonAccountApplicationServiceImpl implements PersonAccountApplicationService {


    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private TpPersonBasicinfoMapper tpPersonBasicinfoMapper;

    @Override
    public boolean selectByUsername(String userName) {
        TpAccount tpAccount = userAccountMapper.selectByUsername(userName);
        if (null == tpAccount) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean selectByPhone(String phone) {
        TpPersonBasicinfo tpPersonBasicinfo = tpPersonBasicinfoMapper.selectByPhone(phone);
        if (null == tpPersonBasicinfo) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean selectByUsernameAndAccountId(String userName, String accountId) {
        TpAccount tpAccount = userAccountMapper.selectByUsername(userName);
        if (null == tpAccount) {
            return false;
        } else {
            if (StrUtil.equals(tpAccount.getAccountId(), accountId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 修改手机号
     *
     * @param personId 人员id
     * @param phone    手机号
     * @return void
     * @author 杨占锐
     * @date 2024/6/5 17:32
     */
    @Override
    public void updatePhone(String personId, String phone) {
        TpAccountVO tpAccountVO = userAccountMapper.viewByPersonId(personId);
        if (tpAccountVO == null) {
            return;
        }
        TpAccount bean = new TpAccount();
        bean.setAccountId(tpAccountVO.getAccountId());
        
        // 对手机号进行加密
        if (StrUtil.isNotBlank(phone)) {
            String encryptedPhone = PhoneEncryptionUtils.encrypt(phone);
            bean.setPhone(encryptedPhone);
        } else {
            bean.setPhone(phone);
        }
        
        bean.setUpdateTime(CommonDateUtil.now());
        userAccountMapper.update(bean);
    }
}