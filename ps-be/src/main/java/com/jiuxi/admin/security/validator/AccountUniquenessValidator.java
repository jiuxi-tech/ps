package com.jiuxi.admin.security.validator;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.bean.entity.TpAccount;
import com.jiuxi.admin.core.bean.entity.TpPersonBasicinfo;
import com.jiuxi.admin.core.mapper.TpPersonBasicinfoMapper;
import com.jiuxi.common.util.PhoneEncryptionUtils;
import com.jiuxi.module.user.infra.persistence.mapper.UserAccountMapper;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 账号唯一性校验服务
 * 用于校验账号表和人员表中手机号、身份证号的唯一性
 *
 * @author Qoder AI
 * @since 2024-12-15
 */
@Component
public class AccountUniquenessValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountUniquenessValidator.class);
    
    @Autowired
    private UserAccountMapper userAccountMapper;
    
    @Autowired
    private TpPersonBasicinfoMapper personBasicinfoMapper;
    
    /**
     * 校验账号表手机号唯一性(新增场景)
     *
     * @param phone 手机号
     * @param tenantId 租户ID
     * @throws TopinfoRuntimeException 如果手机号重复
     */
    public void validateAccountPhoneUniqueness(String phone, String tenantId) {
        if (StrUtil.isBlank(phone)) {
            return;
        }
        
        try {
            // 加密手机号进行查询
            String encryptedPhone = PhoneEncryptionUtils.encrypt(phone);
            List<TpAccount> accounts = userAccountMapper.selectByPhone(encryptedPhone);
            
            if (accounts != null && !accounts.isEmpty()) {
                // 过滤有效账号(ACTIVED=1且tenantId匹配)
                boolean exists = accounts.stream()
                        .anyMatch(account -> account.getActived() == 1 
                                && StrUtil.equals(account.getTenantId(), tenantId));
                
                if (exists) {
                    logger.warn("账号手机号重复: phone={}, tenantId={}", phone, tenantId);
                    throw new TopinfoRuntimeException(-1, "该手机号已被其他账号使用");
                }
            }
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("校验账号手机号唯一性失败: {}", e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "校验手机号唯一性失败");
        }
    }
    
    /**
     * 校验账号表手机号唯一性(修改场景)
     *
     * @param phone 手机号
     * @param accountId 当前账号ID
     * @param tenantId 租户ID
     * @throws TopinfoRuntimeException 如果手机号重复
     */
    public void validateAccountPhoneUniqueness(String phone, String accountId, String tenantId) {
        if (StrUtil.isBlank(phone)) {
            return;
        }
        
        try {
            // 加密手机号进行查询
            String encryptedPhone = PhoneEncryptionUtils.encrypt(phone);
            List<TpAccount> accounts = userAccountMapper.selectByPhone(encryptedPhone);
            
            if (accounts != null && !accounts.isEmpty()) {
                // 过滤有效账号(ACTIVED=1且tenantId匹配且非当前账号)
                boolean exists = accounts.stream()
                        .anyMatch(account -> account.getActived() == 1
                                && StrUtil.equals(account.getTenantId(), tenantId)
                                && !StrUtil.equals(account.getAccountId(), accountId));
                
                if (exists) {
                    logger.warn("账号手机号重复: phone={}, accountId={}, tenantId={}", phone, accountId, tenantId);
                    throw new TopinfoRuntimeException(-1, "该手机号已被其他账号使用");
                }
            }
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("校验账号手机号唯一性失败: {}", e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "校验手机号唯一性失败");
        }
    }
    
    /**
     * 校验账号表身份证号唯一性(新增场景)
     *
     * @param idCard 身份证号
     * @param tenantId 租户ID
     * @throws TopinfoRuntimeException 如果身份证号重复
     */
    public void validateAccountIdCardUniqueness(String idCard, String tenantId) {
        if (StrUtil.isBlank(idCard)) {
            return;
        }
        
        try {
            List<TpAccount> accounts = userAccountMapper.selectByIdCard(idCard);
            
            if (accounts != null && !accounts.isEmpty()) {
                // 过滤有效账号(ACTIVED=1且tenantId匹配)
                boolean exists = accounts.stream()
                        .anyMatch(account -> account.getActived() == 1
                                && StrUtil.equals(account.getTenantId(), tenantId));
                
                if (exists) {
                    logger.warn("账号身份证号重复: idCard={}, tenantId={}", idCard, tenantId);
                    throw new TopinfoRuntimeException(-1, "该身份证号已被其他账号使用");
                }
            }
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("校验账号身份证号唯一性失败: {}", e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "校验身份证号唯一性失败");
        }
    }
    
    /**
     * 校验账号表身份证号唯一性(修改场景)
     *
     * @param idCard 身份证号
     * @param accountId 当前账号ID
     * @param tenantId 租户ID
     * @throws TopinfoRuntimeException 如果身份证号重复
     */
    public void validateAccountIdCardUniqueness(String idCard, String accountId, String tenantId) {
        if (StrUtil.isBlank(idCard)) {
            return;
        }
        
        try {
            List<TpAccount> accounts = userAccountMapper.selectByIdCard(idCard);
            
            if (accounts != null && !accounts.isEmpty()) {
                // 过滤有效账号(ACTIVED=1且tenantId匹配且非当前账号)
                boolean exists = accounts.stream()
                        .anyMatch(account -> account.getActived() == 1
                                && StrUtil.equals(account.getTenantId(), tenantId)
                                && !StrUtil.equals(account.getAccountId(), accountId));
                
                if (exists) {
                    logger.warn("账号身份证号重复: idCard={}, accountId={}, tenantId={}", idCard, accountId, tenantId);
                    throw new TopinfoRuntimeException(-1, "该身份证号已被其他账号使用");
                }
            }
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("校验账号身份证号唯一性失败: {}", e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "校验身份证号唯一性失败");
        }
    }
    
    /**
     * 校验人员表手机号唯一性(新增场景)
     *
     * @param phone 手机号
     * @param tenantId 租户ID
     * @throws TopinfoRuntimeException 如果手机号重复
     */
    public void validatePersonPhoneUniqueness(String phone, String tenantId) {
        if (StrUtil.isBlank(phone)) {
            return;
        }
        
        try {
            // 加密手机号进行查询
            String encryptedPhone = PhoneEncryptionUtils.encrypt(phone);
            TpPersonBasicinfo person = personBasicinfoMapper.selectByPhone(encryptedPhone);
            
            if (person != null && person.getActived() == 1 
                    && StrUtil.equals(person.getTenantId(), tenantId)) {
                logger.warn("人员手机号重复: phone={}, tenantId={}", phone, tenantId);
                throw new TopinfoRuntimeException(-1, "该手机号已被其他人员使用");
            }
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("校验人员手机号唯一性失败: {}", e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "校验手机号唯一性失败");
        }
    }
    
    /**
     * 校验人员表手机号唯一性(修改场景)
     *
     * @param phone 手机号
     * @param personId 当前人员ID
     * @param tenantId 租户ID
     * @throws TopinfoRuntimeException 如果手机号重复
     */
    public void validatePersonPhoneUniqueness(String phone, String personId, String tenantId) {
        if (StrUtil.isBlank(phone)) {
            return;
        }
        
        try {
            // 加密手机号进行查询
            String encryptedPhone = PhoneEncryptionUtils.encrypt(phone);
            TpPersonBasicinfo person = personBasicinfoMapper.selectByPhone(encryptedPhone);
            
            if (person != null && person.getActived() == 1
                    && StrUtil.equals(person.getTenantId(), tenantId)
                    && !StrUtil.equals(person.getPersonId(), personId)) {
                logger.warn("人员手机号重复: phone={}, personId={}, tenantId={}", phone, personId, tenantId);
                throw new TopinfoRuntimeException(-1, "该手机号已被其他人员使用");
            }
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("校验人员手机号唯一性失败: {}", e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "校验手机号唯一性失败");
        }
    }
    
    /**
     * 校验人员表身份证号唯一性(新增场景)
     *
     * @param idCard 身份证号
     * @param tenantId 租户ID
     * @throws TopinfoRuntimeException 如果身份证号重复
     */
    public void validatePersonIdCardUniqueness(String idCard, String tenantId) {
        if (StrUtil.isBlank(idCard)) {
            return;
        }
        
        try {
            TpPersonBasicinfo person = personBasicinfoMapper.selectByIdCard(idCard);
            
            if (person != null && person.getActived() == 1
                    && StrUtil.equals(person.getTenantId(), tenantId)) {
                logger.warn("人员身份证号重复: idCard={}, tenantId={}", idCard, tenantId);
                throw new TopinfoRuntimeException(-1, "该身份证号已被其他人员使用");
            }
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("校验人员身份证号唯一性失败: {}", e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "校验身份证号唯一性失败");
        }
    }
    
    /**
     * 校验人员表身份证号唯一性(修改场景)
     *
     * @param idCard 身份证号
     * @param personId 当前人员ID
     * @param tenantId 租户ID
     * @throws TopinfoRuntimeException 如果身份证号重复
     */
    public void validatePersonIdCardUniqueness(String idCard, String personId, String tenantId) {
        if (StrUtil.isBlank(idCard)) {
            return;
        }
        
        try {
            TpPersonBasicinfo person = personBasicinfoMapper.selectByIdCard(idCard);
            
            if (person != null && person.getActived() == 1
                    && StrUtil.equals(person.getTenantId(), tenantId)
                    && !StrUtil.equals(person.getPersonId(), personId)) {
                logger.warn("人员身份证号重复: idCard={}, personId={}, tenantId={}", idCard, personId, tenantId);
                throw new TopinfoRuntimeException(-1, "该身份证号已被其他人员使用");
            }
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("校验人员身份证号唯一性失败: {}", e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "校验身份证号唯一性失败");
        }
    }
}
