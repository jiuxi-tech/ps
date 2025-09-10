package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import com.alibaba.fastjson.JSONObject;
// import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.jiuxi.admin.autoconfig.AdminConfigurationProperties;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.constant.enums.OpertionTypeEnum;
import com.jiuxi.admin.core.bean.entity.TpAccount;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.admin.core.event.TpAccountEvent;
import com.jiuxi.admin.core.listener.service.TpAccountEventService;
import com.jiuxi.admin.core.mapper.TpAccountMapper;
import com.jiuxi.admin.core.mapper.TpPersonBasicinfoMapper;
import com.jiuxi.admin.core.service.EmailService;
import com.jiuxi.admin.core.service.KeycloakSyncService;
import com.jiuxi.admin.core.service.PersonAccountService;
import com.jiuxi.admin.core.service.TpAccountService;
import com.jiuxi.admin.core.service.TpKeycloakAccountService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.*;
import com.jiuxi.common.util.PhoneEncryptionUtils;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
// import com.jiuxi.sms.core.service.SmsSendService;
import org.apache.commons.lang3.RandomStringUtils;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.util.List;
import java.util.Optional;

/**
 * @Description: 账户Service
 * @ClassName: TpAccountServiceImpl
 * @Author: pand
 * @Date: 2021-05-07 11:32
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
@Service("tpAccountService")
public class TpAccountServiceImpl implements TpAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpAccountServiceImpl.class);

    @Autowired
    private TpAccountMapper tpAccountMapper;

    @Autowired
    private TpPersonBasicinfoMapper tpPersonBasicinfoMapper;

    @Autowired
    private SecurityConfigurationProperties properties;

    @Autowired
    private AdminConfigurationProperties adminProperties;

    // @Autowired(required = false)
    // @Qualifier(value = "smsSendService")
    // private SmsSendService smsSendService;

    @Autowired
    private PersonAccountService personAccountService;

    /**
     * 上下文对象
     */
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private TpAccountEventService tpAccountEventService;

    @Autowired(required = false)
    @Qualifier("adminEmailService")
    private EmailService emailService;

    @Autowired(required = false)
    private KeycloakSyncService keycloakSyncService;

    @Autowired(required = false)
    private TpKeycloakAccountService tpKeycloakAccountService;



    /**
     * 新增账号
     *
     * @param vo              账号信息
     * @param decryptStrSM2   是否需要解密账号密码
     * @return int
     * @author 杨占锐
     * @date 2024/6/5 9:47
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int accountManage(TpAccountVO vo, boolean decryptStrSM2) {

        // 根据用户id查询人员信息
        TpPersonBasicinfoVO personBasicinfoVO = tpPersonBasicinfoMapper.view(vo.getPersonId());
        if (null == personBasicinfoVO) {
            throw new TopinfoRuntimeException(-1, "用户不存在！");
        }

        // 用户名
        String username = vo.getUsername();
        // 密码
        String userpwd = vo.getUserpwd();
        // 判断是非开启密码加密传输
        if (properties.isPasswordEncryption() && decryptStrSM2) {
            try {
                // 先将账号密码解密，username,passWord是经过sm2加密的密文传输
                // 前端传入的需要在 密文前 加 "04"
                username = SmUtils.decryptStrSM2("04" + username, KeyType.PrivateKey);
                vo.setUsername(username);
                // 修改的时候，密码可能为空。
                userpwd = StrUtil.isBlank(userpwd) ? userpwd : SmUtils.decryptStrSM2("04" + userpwd, KeyType.PrivateKey);
                vo.setUserpwd(userpwd);
            } catch (Exception e) {
                throw new RuntimeException("账号密码解密失败");
            }
        }

        // 根据人员id，查询账号
        TpAccountVO tpAccountVO = tpAccountMapper.viewByPersonId(personBasicinfoVO.getPersonId());
        if (tpAccountVO != null) {
            vo.setAccountId(tpAccountVO.getAccountId());
            // 如果accountId已经存在，进行修改操作。
            return this.accountUpdate(vo);
        }


        // TODO TAG 判断 undefined 是因为向前兼容，密码修改的时候，传过来了 undefined. 经过1个大版本更新时，会删掉。目前版本1.0.6-SNAPSHOT
        if (StrUtil.equals(userpwd, "undefined")) {
            vo.setUserpwd("");
        }
        // 校验密码是否符合弱密码等级要求
        boolean regularFla = PwdRegularUtils.pwdRegular(properties.getAuthentication().getRegular(), userpwd);
        if (!regularFla) {
            throw new TopinfoRuntimeException(-1, "密码不符合安全等级要求！");
        }

        // 设置手机号
        vo.setPhone(personBasicinfoVO.getPhone());

        return this.accountAdd(vo);
    }

    /**
     * 新增人员账号信息
     *
     * @param vo: 账户信息
     * @return int
     * @author pand
     * @date 2020-11-24 14:56
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int accountManage(TpAccountVO vo) {
        return this.accountManage(vo, true);
    }

    /**
     * 人员的账号数据入库，该接口接收的密码需要明文
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2021-03-03 16:51
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int accountAdd(TpAccountVO vo) {

        if (personAccountService.selectByUsername(vo.getUsername())) {
            throw new TopinfoRuntimeException(-1, "账号已被占用！");
        }

        return this.accountInsert(vo);
    }

    /**
     * 账号入库操作
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2021-06-24 16:07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int accountInsert(TpAccountVO vo) {
        // 明文密码
        String denUserpwd = vo.getUserpwd();
        // 原密码经过sm3加密入库
        vo.setUserpwd(SmUtils.digestHexSM3(denUserpwd));

        TpAccount bean = new TpAccount();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        
        // 对手机号进行加密
        if (StrUtil.isNotBlank(vo.getPhone())) {
            String encryptedPhone = PhoneEncryptionUtils.encrypt(vo.getPhone());
            bean.setPhone(encryptedPhone);
            LOGGER.debug("手机号加密处理完成，personId: {}", vo.getPersonId());
        }
        
        String accountId = SnowflakeIdUtil.nextIdStr();
        bean.setAccountId(accountId);
        String now = CommonDateUtil.now();
        bean.setCreateTime(now);
        // 修改时间
        bean.setUpdateTime(now);
        // 默认未冻结
        bean.setLocked(0);
        // 默认启用
        bean.setEnabled(Optional.ofNullable(bean.getEnabled()).orElse(1));

        try {
            int count = tpAccountMapper.save(bean);

            // 发布事件，推送账号给第三方系统
            if (null != tpAccountEventService) {
                // 将明文推给第三方，第三方根据自己的需求自己在加密
                bean.setUserpwd(denUserpwd);
                applicationContext.publishEvent(new TpAccountEvent("账号信息新增同步监听", tpAccountEventService, bean, OpertionTypeEnum.ADD.getOpertionType()));
            }

            // 发布Keycloak同步事件，在事务提交后异步处理
            if (null != keycloakSyncService) {
                KeycloakSyncEvent keycloakEvent = new KeycloakSyncEvent(accountId, vo.getUsername(), denUserpwd, "system");
                applicationContext.publishEvent(keycloakEvent);
            }

            return count;
        } catch (Exception e) {
            LOGGER.error("新增账号信息失败！vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "新增账号信息失败！" : e.getMessage());
        }
    }

    /**
     * 查看用户账号信息
     *
     * @param personId:
     * @return com.jiuxi.admin.core.bean.vo.TpAccountVO
     * @author pand
     * @date 2020-11-24 20:02
     */
    @Override
    public TpAccountVO accountView(String personId) {
        try {
            TpAccountVO vo = tpAccountMapper.viewByPersonId(personId);
            // 解密手机号
            if (vo != null && StrUtil.isNotBlank(vo.getPhone())) {
                String decryptedPhone = PhoneEncryptionUtils.safeDecrypt(vo.getPhone());
                vo.setPhone(decryptedPhone);
            }
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看用户账号信息失败！personId:{}, 错误:{}", personId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看用户账号信息失败！");
        }
    }


    /**
     * 查看用户账号信息
     *
     * @param accountId:
     * @return com.jiuxi.admin.core.bean.vo.TpAccountVO
     * @author pand
     * @date 2020-11-24 20:02
     */
    @Override
    public TpAccountVO selectByAccountId(String accountId) {
        try {
            TpAccountVO vo = tpAccountMapper.selectByAccountId(accountId);
            // 解密手机号
            if (vo != null && StrUtil.isNotBlank(vo.getPhone())) {
                String decryptedPhone = PhoneEncryptionUtils.safeDecrypt(vo.getPhone());
                vo.setPhone(decryptedPhone);
            }
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看用户账号信息失败！accountId:{}, 错误:{}", accountId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看用户账号信息失败！");
        }
    }

    /**
     * 更新用户账号信息
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2020-11-24 20:13
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int accountUpdate(TpAccountVO vo) {

        if (personAccountService.selectByUsernameAndAccountId(vo.getUsername(), vo.getAccountId())) {
            throw new TopinfoRuntimeException(-1, "账号已被占用！");
        }

        try {
            TpAccount bean = new TpAccount();
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            
            // 对手机号进行加密
            if (StrUtil.isNotBlank(vo.getPhone())) {
                String encryptedPhone = PhoneEncryptionUtils.encrypt(vo.getPhone());
                bean.setPhone(encryptedPhone);
                LOGGER.debug("手机号加密处理完成，accountId: {}", vo.getAccountId());
            }
            
            bean.setUpdateTime(CommonDateUtil.now());
            // 禁止修改密码
            bean.setUserpwd(null);
            int count = tpAccountMapper.update(bean);

            // 发布事件，修改账号给第三方系统
            if (null != tpAccountEventService) {
                applicationContext.publishEvent(new TpAccountEvent("账号信息修改同步监听", tpAccountEventService, bean, OpertionTypeEnum.UPDATE.getOpertionType()));
            }

            // 同步账号信息到Keycloak（不包括密码）
            if (null != keycloakSyncService) {
                try {
                    // 由于TpAccountVO没有creator字段，使用系统默认值
                    String creator = "system"; // 默认创建者
                    KeycloakSyncService.KeycloakSyncResult syncResult = keycloakSyncService.updateKeycloakUser(
                            vo.getAccountId(), vo.getUsername(), null, creator); // 密码传null，不更新密码
                    if (syncResult.isSuccess()) {
                        LOGGER.info("账号信息同步到Keycloak成功: accountId={}, username={}", 
                                vo.getAccountId(), vo.getUsername());
                    } else {
                        LOGGER.warn("账号信息同步到Keycloak失败: accountId={}, username={}, message={}", 
                                vo.getAccountId(), vo.getUsername(), syncResult.getMessage());
                    }
                } catch (Exception e) {
                    LOGGER.error("同步账号信息到Keycloak时发生异常: accountId={}, username={}, error={}", 
                            vo.getAccountId(), vo.getUsername(), e.getMessage(), e);
                }
            }

            return count;
        } catch (Exception e) {
            LOGGER.error("账号信息修改失败！vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "账号信息修改失败！");
        }
    }

    /**
     * 找回密码
     *
     * @param phone: 手机号
     * @return int
     * @author pand
     * @date 2021-02-04 15:38
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public String accountFindpwd(String phone) {
        // if (null == smsSendService) {
        //     throw new TopinfoRuntimeException(-1, "没有短信服务可用！");
        // }
        throw new TopinfoRuntimeException(-1, "短信服务不可用！");
        
        // try {
        //     // 判断手机号是否存在，并且判断手机号对应的用户是否有账号
        //     List<TpAccount> list = tpAccountMapper.selectByPhone(phone);
        //     if (list.size() == 0) {
        //         LOGGER.error("{} 该手机号未注册！", phone);
        //         throw new TopinfoRuntimeException(-1, "找回密码失败！");
        //     } else if (list.size() > 1) {
        //         LOGGER.error("{} 该手机号对应多个账号！", phone);
        //         throw new TopinfoRuntimeException(-1, "找回密码失败！");
        //     }
        //     // 判断上个验证码发送时间距离当前验证码发送时间的间隔
        //     String verificationCode = list.get(0).getVerificationCode();
        //     if (StrUtil.isNotBlank(verificationCode)) {
        //         List<String> verificationCodes = StrUtil.split(verificationCode, ":");
        //         String oldTime = verificationCodes.get(2);
        //         Long diff = (CommonDateUtil.currentTimeMillis() - Long.valueOf(oldTime)) / 1000;
        //         if (diff < 60) {
        //             throw new TopinfoRuntimeException(-1, "请1分钟后再次发送验证码！");
        //         }
        //     }


        //     // 有一个对应的用户，生成验证码存入数据库，同时发送邮件给用户code，数据库存储验证码规则:email:code:time，time为时间戳
        //     String code = RandomStringUtils.randomNumeric(properties.getValidateCode().getLength());
        //     // 验证码生成，更新账号表保存数据库的VERIFICATION_CODE字段中。
        //     String time = String.valueOf(CommonDateUtil.currentTimeMillis());
        //     String newVerificationCode = phone.concat(":").concat(code).concat(":").concat(time);
        //     LOGGER.info("验证码 = {}", newVerificationCode);
        //     TpAccount tpAccount = new TpAccount();
        //     tpAccount.setAccountId(list.get(0).getAccountId());
        //     tpAccount.setVerificationCode(newVerificationCode);
        //     tpAccountMapper.update(tpAccount);
        //     // 给用户发短信
        //     JSONObject templateParam = new JSONObject();
        //     templateParam.put(TpConstant.SMSCode.PWDCODEKEY, code);
        //     // 发短信
        //     // SendSmsResponse sendSmsResponse = smsSendService.sendSms(phone, adminProperties.getSmsCode().getTemplatecode().get(TpConstant.SMSCode.PWDTEMPLATECODE), templateParam);
        //     return list.get(0).getUsername();
        // } catch (MyBatisSystemException e) {
        //     LOGGER.error("验证码发送失败！手机号:{}, 错误:{}", phone, ExceptionUtils.getStackTrace(e));
        //     throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "验证码发送失败！" : e.getMessage());
        // }
    }

    /**
     * 新密码和账号验证码一起提交，修改密码
     *
     * @param phone:
     * @param vcode:
     * @return int
     * @author pand
     * @date 2021-02-04 15:38
     */
    @Override
    public int accountCheckVcode(String phone, String vcode, String userpwd) {
        List<TpAccount> list = tpAccountMapper.selectByPhone(phone);
        if (list.size() == 0) {
            LOGGER.error("{} 该手机号未注册！", phone);
            throw new TopinfoRuntimeException(-1, "密码修改失败！");
        } else if (list.size() > 1) {
            LOGGER.error("{} 该手机号对应多个账号！", phone);
            throw new TopinfoRuntimeException(-1, "密码修改失败！");
        }
        String verificationCode = list.get(0).getVerificationCode();
        if (StrUtil.isBlank(verificationCode)) {
            throw new TopinfoRuntimeException(-1, "验证码无效！");
        }
        List<String> verificationCodes = StrUtil.split(verificationCode, ":");
        // 检查验证码格式是否正确（应该包含3个部分：phone:code:time）
        if (verificationCodes.size() < 3) {
            throw new TopinfoRuntimeException(-1, "验证码格式无效！");
        }
        String oldPhone = verificationCodes.get(0);
        String oldVcode = verificationCodes.get(1);
        String oldTime = verificationCodes.get(2);
        if (!StrUtil.equals(oldPhone, phone)) {
            throw new TopinfoRuntimeException(-1, "验证码无效！");
        }
        if (!StrUtil.equals(oldVcode, vcode)) {
            throw new TopinfoRuntimeException(-1, "验证码无效！");
        }

        // 时间戳转换成秒
        Long diff = (CommonDateUtil.currentTimeMillis() - Long.valueOf(oldTime)) / 1000;
        if (diff > properties.getValidateCode().getExpireIn()) {
            throw new TopinfoRuntimeException(-1, "验证码已失效！");
        }

        // 校验密码是否符合弱密码等级要求
        boolean regularFla = PwdRegularUtils.pwdRegular(properties.getAuthentication().getRegular(), userpwd);
        if (!regularFla) {
            throw new TopinfoRuntimeException(-1, "密码不符合安全等级要求！");
        }

        // 修改用户账号密码
        TpAccount bean = new TpAccount();
        // 转换成数据库对象
        bean.setPersonId(list.get(0).getPersonId());
        bean.setUserpwd(SmUtils.digestHexSM3(userpwd));
        bean.setUpdateTime(CommonDateUtil.now());
        bean.setLastPasswordChangeTime(CommonDateUtil.now());

        int count = tpAccountMapper.updateByPersonId(bean);

        // 发布事件，修改账号给第三方系统
        if (null != tpAccountEventService) {
            // 将明文推给第三方，第三方根据自己的需求自己在加密
            bean.setUserpwd(userpwd);
            applicationContext.publishEvent(new TpAccountEvent("账号密码修改同步监听", tpAccountEventService, bean, OpertionTypeEnum.DELETE.getOpertionType()));
        }

        return count;
    }

    /**
     * 修改账号密码，先校验原密码
     *
     * @param personId:
     * @param oldUserpwd:
     * @param userpwd:
     * @return int
     * @author pand
     * @date 2020-12-08 18:26
     */
    @Override
    public int updatePwd(String personId, String oldUserpwd, String userpwd) {
        try {
            if (StrUtil.isBlank(oldUserpwd)) {
                // 原密码不对
                throw new TopinfoRuntimeException(-1, "请输入原密码！");
            }

            if (StrUtil.isBlank(userpwd)) {
                // 原密码不对
                throw new TopinfoRuntimeException(-1, "请输入新密码！");
            }

            // 查询原账号信息，需要进行原密码信息校验
            TpAccountVO tpAccountVO = tpAccountMapper.viewByPersonId(personId);
            if (null == tpAccountVO) {
                LOGGER.error("根据用户id {} 没有查到账号信息！", personId);
                throw new TopinfoRuntimeException(-1, "账号密码修改失败！");
            }

            // 判断是非开启密码加密传输
            if (properties.isPasswordEncryption()) {
                try {
                    // 先将账号密码解密，username,passWord是经过sm2加密的密文传输
                    // 前端传入的需要在 密文前 加 "04"
                    oldUserpwd = SmUtils.decryptStrSM2("04" + oldUserpwd, KeyType.PrivateKey);
                    userpwd = SmUtils.decryptStrSM2("04" + userpwd, KeyType.PrivateKey);
                } catch (Exception e) {
                    throw new RuntimeException("登录失败，用户名或密码错误");
                }
            }

            // 校验原密码
            if (!StrUtil.equals(SmUtils.digestHexSM3(oldUserpwd), tpAccountVO.getUserpwd())) {
                // 原密码不对
                throw new TopinfoRuntimeException(-1, "原密码不正确！");
            }

            // 校验密码是否符合弱密码等级要求
            boolean regularFla = PwdRegularUtils.pwdRegular(properties.getAuthentication().getRegular(), userpwd);
            if (!regularFla) {
                throw new TopinfoRuntimeException(-1, "密码不符合安全等级要求！");
            }

            TpAccount bean = new TpAccount();
            // 转换成数据库对象
            bean.setPersonId(personId);
            bean.setUserpwd(SmUtils.digestHexSM3(userpwd));
            bean.setExtend01("0"); // 重置密码修改标识
            bean.setUpdateTime(CommonDateUtil.now());
            bean.setLastPasswordChangeTime(CommonDateUtil.now());

            int count = tpAccountMapper.updateByPersonId(bean);

            // 发布事件，修改账号给第三方系统
            if (null != tpAccountEventService) {
                // 将所有字段重新复制到bean
                BeanUtil.copyProperties(tpAccountVO, bean);
                // 将明文推给第三方，第三方根据自己的需求自己在加密
                bean.setUserpwd(userpwd);
                applicationContext.publishEvent(new TpAccountEvent("账号密码修改同步监听", tpAccountEventService, bean, OpertionTypeEnum.DELETE.getOpertionType()));
            }

            return count;
        } catch (Exception e) {
            LOGGER.error("账号密码修改失败！personId:{}, 错误:{}", personId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "账号密码修改失败！" : e.getMessage());
        }
    }

    /**
     * 强制修改密码，密码被管理员重置后，用户第一次登陆强制修改密码
     *
     * @param personId:
     * @param userpwd:
     * @return int
     * @author pand
     * @date 2020-12-08 18:26
     */
    @Override
    public int updatePwd(String personId, String userpwd) {
        try {

            if (StrUtil.isBlank(userpwd)) {
                throw new TopinfoRuntimeException(-1, "请输入密码！");
            }

            // 查询原账号信息
            TpAccountVO tpAccountVO = tpAccountMapper.viewByPersonId(personId);
            if (null == tpAccountVO) {
                LOGGER.error("根据用户id {} 没有查到账号信息！", personId);
                throw new TopinfoRuntimeException(-1, "账号密码修改失败！");
            }

            // 判断是非开启密码加密传输
            if (properties.isPasswordEncryption()) {
                try {
                    // 先将账号密码解密，username,passWord是经过sm2加密的密文传输
                    // 前端传入的需要在 密文前 加 "04"
                    userpwd = SmUtils.decryptStrSM2("04" + userpwd, KeyType.PrivateKey);
                } catch (Exception e) {
                    throw new RuntimeException("登录失败，用户名或密码错误");
                }
            }

            // 校验密码是否符合弱密码等级要求
            boolean regularFla = PwdRegularUtils.pwdRegular(properties.getAuthentication().getRegular(), userpwd);
            if (!regularFla) {
                throw new TopinfoRuntimeException(-1, "密码不符合安全等级要求！");
            }

            TpAccount bean = new TpAccount();
            // 转换成数据库对象
            bean.setPersonId(personId);
            bean.setUserpwd(SmUtils.digestHexSM3(userpwd));
            bean.setExtend01("0");
            bean.setUpdateTime(CommonDateUtil.now());
            bean.setLastPasswordChangeTime(CommonDateUtil.now());

            int count = tpAccountMapper.updateByPersonId(bean);

            // 发布事件，修改账号给第三方系统
            if (null != tpAccountEventService) {
                // 将所有字段重新复制到bean
                BeanUtil.copyProperties(tpAccountVO, bean);
                // 将明文推给第三方，第三方根据自己的需求自己在加密
                bean.setUserpwd(userpwd);
                applicationContext.publishEvent(new TpAccountEvent("账号密码修改同步监听", tpAccountEventService, bean, OpertionTypeEnum.DELETE.getOpertionType()));
            }

            return count;
        } catch (Exception e) {
            LOGGER.error("账号密码修改失败！personId:{}, 错误:{}", personId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "账号密码修改失败！" : e.getMessage());
        }
    }

    /**
     * 账号密码重置
     *
     * @param accountId: 账号id
     * @return int
     * @author pand
     * @date 2020-11-24 20:13
     */
    @Override
    public String accountResetpwd(String accountId) {
        try {
            TpAccount bean = new TpAccount();
            bean.setAccountId(accountId);
            // 随机生成重置密码
            String resetPwd = PwdRegularUtils.randomPwd(8);
            String pwd = SmUtils.digestHexSM3(resetPwd);
            bean.setUserpwd(pwd);
            bean.setExtend01("1");
            bean.setUpdateTime(CommonDateUtil.now());
            bean.setLastPasswordChangeTime(CommonDateUtil.now());

            int count = tpAccountMapper.update(bean);

            // 发布事件，修改账号给第三方系统
            if (null != tpAccountEventService) {
                bean.setUserpwd(resetPwd);
                applicationContext.publishEvent(new TpAccountEvent("账号信息修改同步监听", tpAccountEventService, bean, OpertionTypeEnum.DELETE.getOpertionType()));
            }

            return resetPwd;
        } catch (Exception e) {
            LOGGER.error("账号信息修改失败！accountId:{}, 错误:{}", accountId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "账号信息修改失败！");
        }
    }

    /**
     * 账号 冻结/解冻
     *
     * @param accountId: 账号id
     * @param locked:    冻结/解冻 0解冻，1冻结
     * @return int
     * @author pand
     * @date 2020-11-24 20:13
     */
    @Override
    public int accountLocked(String accountId, int locked) {
        try {
            TpAccount bean = new TpAccount();
            bean.setAccountId(accountId);
            bean.setLocked(locked);
            bean.setUpdateTime(CommonDateUtil.now());

            int count = tpAccountMapper.update(bean);

            // 发布事件，修改账号给第三方系统
            if (null != tpAccountEventService) {
                applicationContext.publishEvent(new TpAccountEvent("账号信息修改同步监听", tpAccountEventService, bean, OpertionTypeEnum.LOCKED.getOpertionType()));
            }

            return count;
        } catch (Exception e) {
            LOGGER.error("账号冻结/解冻修改失败！accountId:{}, 错误:{}", accountId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "账号冻结/解冻失败！");
        }
    }

    /**
     * 账号 启动/禁用
     *
     * @param accountId: 账号id
     * @param enabled:   启动/禁用
     * @return int
     * @author pand
     * @date 2020-11-24 20:15
     */
    @Override
    public int accountEnabled(String accountId, int enabled) {
        try {
            TpAccount bean = new TpAccount();
            bean.setAccountId(accountId);
            bean.setEnabled(enabled);
            bean.setUpdateTime(CommonDateUtil.now());

            int count = tpAccountMapper.update(bean);

            // 发布事件，修改账号给第三方系统
            if (null != tpAccountEventService) {
                applicationContext.publishEvent(new TpAccountEvent("账号信息修改同步监听", tpAccountEventService, bean, OpertionTypeEnum.ENABLED.getOpertionType()));
            }

            return count;
        } catch (Exception e) {
            LOGGER.error("账号启动/禁用失败！accountId:{}, 错误:{}", accountId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "账号启动/禁用失败！");
        }
    }


    /**
     * 根据手机号查询账户信息
     * @author 杨攀
     * @date 2024/5/27 13:44
     * @param phone
     * @return com.jiuxi.admin.core.bean.vo.TpAccountVO
     */
    @Override
    public TpAccountVO getTpAccountByPhone(String phone) {

        try {
            // 先加密手机号进行查询
            String encryptedPhone = PhoneEncryptionUtils.encrypt(phone);
            TpAccountVO vo =  tpAccountMapper.getTpAccountByPhone(encryptedPhone);
            // 解密查询结果中的手机号
            if (vo != null && StrUtil.isNotBlank(vo.getPhone())) {
                String decryptedPhone = PhoneEncryptionUtils.safeDecrypt(vo.getPhone());
                vo.setPhone(decryptedPhone);
            }
            return vo;
        } catch (Exception e) {
            LOGGER.error("根据手机号查询账户信息失败！手机号:{}，错误:{}", phone, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "根据手机号查询账户信息失败！");
        }
    }

    /**
     * 根据用户名查询账号信息
     *
     * @param username 用户名
     * @return com.jiuxi.admin.core.bean.vo.TpAccountVO
     * @author 杨占锐
     * @date 2024/5/29 9:15
     */
    @Override
    public TpAccountVO getTpAccountByUsername(String username) {
        return tpAccountMapper.getTpAccountByUsername(username);
    }

    /**
     * 根据人员id，删除账号信息
     *
     * @param personId 人员id
     * @return void
     * @author 杨占锐
     * @date 2024/5/29 11:08
     */
    @Override
    public void deleteByPersonId(String personId) {
        TpAccountVO view = tpAccountMapper.viewByPersonId(personId);
        if (view == null) {
            LOGGER.error("根据人员id未查询到账号信息！personId:{}", personId);
            return;
        }
        String updateTime = CommonDateUtil.now();
        tpAccountMapper.deleteByPersonId(personId, updateTime, CommonUniqueIndexUtil.addDeleteTime(view.getUsername()), CommonUniqueIndexUtil.addDeleteTime(view.getPhone()));
    }

    /**
     * 根据邮箱获取账号信息
     *
     * @param email 邮箱
     * @return TpAccountVO
     * @author Trae AI
     * @date 2024/12/19
     */
    @Override
    public TpAccountVO getTpAccountByEmail(String email) {
        return tpAccountMapper.getTpAccountByEmail(email);
    }

    /**
     * 邮箱找回密码
     *
     * @param email 邮箱
     * @return String
     * @author Trae AI
     * @date 2024/12/19
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public String accountFindpwdByEmail(String email) {
        try {
            // 判断邮箱是否存在，并且判断邮箱对应的用户是否有账号
            List<TpAccount> list = tpAccountMapper.selectByEmail(email);
            if (list.size() == 0) {
                LOGGER.error("{} 该邮箱未注册！", email);
                throw new TopinfoRuntimeException(-1, "该邮箱地址未注册，请检查邮箱地址是否正确或联系管理员");
            } else if (list.size() > 1) {
                LOGGER.error("{} 该邮箱对应多个账号！", email);
                throw new TopinfoRuntimeException(-1, "该邮箱地址关联了多个账号，请联系管理员处理");
            }
            // 判断上个验证码发送时间距离当前验证码发送时间的间隔
            String verificationCode = list.get(0).getVerificationCode();
            if (StrUtil.isNotBlank(verificationCode)) {
                List<String> verificationCodes = StrUtil.split(verificationCode, ":");
                // 检查验证码格式是否正确（应该包含3个部分：email:code:time）
                if (verificationCodes.size() >= 3) {
                    String oldTime = verificationCodes.get(2);
                    Long diff = (CommonDateUtil.currentTimeMillis() - Long.valueOf(oldTime)) / 1000;
                    if (diff < 60) {
                        throw new TopinfoRuntimeException(-1, "请1分钟后再次发送验证码！");
                    }
                }
            }

            // 有一个对应的用户，生成验证码存入数据库，同时发送邮件给用户code，数据库存储验证码规则:email:code:time，time为时间戳
            String code = RandomStringUtils.randomNumeric(properties.getValidateCode().getLength());
            // 验证码生成，更新账号表保存数据库的VERIFICATION_CODE字段中。
            String time = String.valueOf(CommonDateUtil.currentTimeMillis());
            String newVerificationCode = email.concat(":").concat(code).concat(":").concat(time);
            LOGGER.info("验证码 = {}", newVerificationCode);
            TpAccount tpAccount = new TpAccount();
            tpAccount.setAccountId(list.get(0).getAccountId());
            tpAccount.setVerificationCode(newVerificationCode);
            tpAccountMapper.update(tpAccount);
            // 给用户发邮件
             if (emailService != null) {
                 String subject = "密码找回验证码";
                 boolean emailSent = emailService.sendVerificationCode(email, subject, code);
                 if (emailSent) {
                     LOGGER.info("验证码邮件发送成功，邮箱: {}", email);
                 } else {
                     LOGGER.error("验证码邮件发送失败，邮箱: {}", email);
                     throw new TopinfoRuntimeException(-1, "验证码邮件发送失败！");
                 }
             } else {
                 LOGGER.warn("邮件服务不可用，无法发送验证码邮件");
                 throw new TopinfoRuntimeException(-1, "邮件服务不可用！");
             }
            return list.get(0).getUsername();
        } catch (MyBatisSystemException e) {
            LOGGER.error("验证码发送失败！邮箱:{}, 错误:{}", email, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "验证码发送失败！" : e.getMessage());
        }
    }

    /**
     * 邮箱验证码验证并修改密码
     *
     * @param email   邮箱
     * @param vcode   验证码
     * @param userpwd 新密码
     * @return int
     * @author Trae AI
     * @date 2024/12/19
     */
    @Override
    public int accountCheckVcodeByEmail(String email, String vcode, String userpwd) {
        List<TpAccount> list = tpAccountMapper.selectByEmail(email);
        if (list.size() == 0) {
            LOGGER.error("{} 该邮箱未注册！", email);
            throw new TopinfoRuntimeException(-1, "密码修改失败！");
        } else if (list.size() > 1) {
            LOGGER.error("{} 该邮箱对应多个账号！", email);
            throw new TopinfoRuntimeException(-1, "密码修改失败！");
        }
        String verificationCode = list.get(0).getVerificationCode();
        if (StrUtil.isBlank(verificationCode)) {
            throw new TopinfoRuntimeException(-1, "验证码无效！");
        }
        List<String> verificationCodes = StrUtil.split(verificationCode, ":");
        // 检查验证码格式是否正确（应该包含3个部分：email:code:time）
        if (verificationCodes.size() < 3) {
            throw new TopinfoRuntimeException(-1, "验证码格式无效！");
        }
        String oldEmail = verificationCodes.get(0);
        String oldVcode = verificationCodes.get(1);
        String oldTime = verificationCodes.get(2);
        if (!StrUtil.equals(oldEmail, email)) {
            throw new TopinfoRuntimeException(-1, "验证码无效！");
        }
        if (!StrUtil.equals(oldVcode, vcode)) {
            throw new TopinfoRuntimeException(-1, "验证码无效！");
        }

        // 时间戳转换成秒
        Long diff = (CommonDateUtil.currentTimeMillis() - Long.valueOf(oldTime)) / 1000;
        if (diff > properties.getValidateCode().getExpireIn()) {
            throw new TopinfoRuntimeException(-1, "验证码已失效！");
        }

        // 校验密码是否符合弱密码等级要求
        boolean regularFla = PwdRegularUtils.pwdRegular(properties.getAuthentication().getRegular(), userpwd);
        if (!regularFla) {
            throw new TopinfoRuntimeException(-1, "密码不符合安全等级要求！");
        }

        // 修改用户账号密码
        TpAccount bean = new TpAccount();
        // 转换成数据库对象
        bean.setPersonId(list.get(0).getPersonId());
        bean.setUserpwd(SmUtils.digestHexSM3(userpwd));
        bean.setUpdateTime(CommonDateUtil.now());
        bean.setLastPasswordChangeTime(CommonDateUtil.now());

        int count = tpAccountMapper.updateByPersonId(bean);

        // 发布事件，修改账号给第三方系统
        if (null != tpAccountEventService) {
            // 将明文推给第三方，第三方根据自己的需求自己在加密
            bean.setUserpwd(userpwd);
            applicationContext.publishEvent(new TpAccountEvent("账号密码修改同步监听", tpAccountEventService, bean, OpertionTypeEnum.DELETE.getOpertionType()));
        }

        return count;
    }

    /**
     * 异步处理Keycloak同步
     * 在事务提交后执行，避免外键约束问题
     */
    @Async
    @EventListener
    public void handleKeycloakSync(KeycloakSyncEvent event) {
        System.out.println("=== handleKeycloakSync方法被调用 === accountId=" + event.getAccountId() + ", username=" + event.getUsername());
        LOGGER.info("=== handleKeycloakSync方法被调用 === accountId={}, username={}", 
                event.getAccountId(), event.getUsername());
        
        if (null != keycloakSyncService) {
            try {
                LOGGER.info("开始异步同步账号到Keycloak: accountId={}, username={}", 
                        event.getAccountId(), event.getUsername());
                
                KeycloakSyncService.KeycloakSyncResult syncResult = keycloakSyncService.syncAccountToKeycloak(
                        event.getAccountId(), event.getUsername(), event.getPassword(), event.getCreator());
                
                LOGGER.info("Keycloak同步结果: accountId={}, username={}, success={}, keycloakUserId={}, message={}", 
                        event.getAccountId(), event.getUsername(), syncResult.isSuccess(), 
                        syncResult.getKeycloakUserId(), syncResult.getMessage());
                
                if (syncResult.isSuccess()) {
                    // 同步成功后，创建或更新tp_keycloak_account表记录
                    String keycloakUserId = syncResult.getKeycloakUserId();
                    LOGGER.info("准备创建或更新tp_keycloak_account表记录: accountId={}, keycloakUserId={}", 
                            event.getAccountId(), keycloakUserId);
                    
                    if (StrUtil.isNotBlank(keycloakUserId) && tpKeycloakAccountService != null) {
                        try {
                            LOGGER.info("开始执行tp_keycloak_account表操作: accountId={}, username={}, keycloakUserId={}", 
                                    event.getAccountId(), event.getUsername(), keycloakUserId);
                            
                            // 使用TpKeycloakAccountService创建或更新Keycloak账号关联信息
                            boolean success = tpKeycloakAccountService.createOrUpdateKeycloakAccount(
                                    event.getAccountId(), event.getUsername(), event.getPassword(), event.getCreator());
                            
                            if (success) {
                                LOGGER.info("tp_keycloak_account表操作成功: accountId={}, username={}, keycloakUserId={}", 
                                        event.getAccountId(), event.getUsername(), keycloakUserId);
                            } else {
                                LOGGER.warn("tp_keycloak_account表操作失败: accountId={}, username={}, keycloakUserId={}", 
                                        event.getAccountId(), event.getUsername(), keycloakUserId);
                            }
                        } catch (Exception updateException) {
                            LOGGER.error("操作tp_keycloak_account表失败: accountId={}, username={}, keycloakUserId={}, error={}", 
                                    event.getAccountId(), event.getUsername(), keycloakUserId, updateException.getMessage(), updateException);
                        }
                    } else {
                        if (StrUtil.isBlank(keycloakUserId)) {
                            LOGGER.warn("keycloakUserId为空，跳过tp_keycloak_account表操作: accountId={}", event.getAccountId());
                        }
                        if (tpKeycloakAccountService == null) {
                            LOGGER.warn("tpKeycloakAccountService为空，跳过tp_keycloak_account表操作: accountId={}", event.getAccountId());
                        }
                    }
                    
                    LOGGER.info("账号异步同步到Keycloak成功: accountId={}, username={}, keycloakUserId={}", 
                            event.getAccountId(), event.getUsername(), syncResult.getKeycloakUserId());
                } else {
                    LOGGER.warn("账号异步同步到Keycloak失败: accountId={}, username={}, message={}", 
                            event.getAccountId(), event.getUsername(), syncResult.getMessage());
                }
            } catch (Exception e) {
                LOGGER.error("异步同步账号到Keycloak时发生异常: accountId={}, username={}, error={}", 
                        event.getAccountId(), event.getUsername(), e.getMessage(), e);
            }
        } else {
            LOGGER.warn("keycloakSyncService为空，跳过Keycloak同步: accountId={}, username={}", 
                    event.getAccountId(), event.getUsername());
        }
    }

    /**
     * 同步账号到Keycloak
     *
     * @param accountId 账号ID
     * @return boolean 同步是否成功
     */
    @Override
    public boolean syncAccountToKeycloak(String accountId) {
        try {
            // 根据账号ID获取账号信息
            TpAccountVO accountVO = selectByAccountId(accountId);
            if (accountVO == null) {
                LOGGER.error("未找到账号信息，accountId: {}", accountId);
                return false;
            }

            // 检查是否已经同步到Keycloak
            if (tpKeycloakAccountService != null) {
                // 检查是否已存在Keycloak账号记录
                boolean exists = tpKeycloakAccountService.existsByAccountId(accountId);
                if (exists) {
                    LOGGER.info("账号已同步到Keycloak，accountId: {}", accountId);
                    return true;
                }
            }

            // 发布Keycloak同步事件
            KeycloakSyncEvent event = new KeycloakSyncEvent(
                accountVO.getAccountId(),
                accountVO.getUsername(),
                accountVO.getUserpwd(), // 这里传入的是加密后的密码，在事件处理中会生成新密码
                "system" // 使用默认创建者，因为TpAccountVO没有creator字段
            );
            applicationContext.publishEvent(event);
            
            LOGGER.info("已发布Keycloak同步事件，accountId: {}, username: {}", accountId, accountVO.getUsername());
            return true;
        } catch (Exception e) {
            LOGGER.error("同步账号到Keycloak失败，accountId: {}, error: {}", accountId, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * Keycloak同步事件
     */
    public static class KeycloakSyncEvent extends ApplicationEvent {
        private final String accountId;
        private final String username;
        private final String password;
        private final String creator;

        public KeycloakSyncEvent(String accountId, String username, String password, String creator) {
            super(accountId);
            this.accountId = accountId;
            this.username = username;
            this.password = password;
            this.creator = creator;
        }

        public String getAccountId() {
            return accountId;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getCreator() {
            return creator;
        }
    }

}
