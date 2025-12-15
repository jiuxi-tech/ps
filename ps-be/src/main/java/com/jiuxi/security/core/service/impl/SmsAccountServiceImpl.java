package com.jiuxi.security.core.service.impl;

import com.jiuxi.security.core.entity.vo.AccountVO;
import com.jiuxi.security.core.service.AbstractValidateCodeService;
import com.jiuxi.security.core.service.SmsAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @Description: 手机号/验证码登陆实现类
 * @ClassName: PersonServiceImpl
 * @Author: pdd
 * @Date: 2020-08-28 13:46
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class SmsAccountServiceImpl implements SmsAccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsAccountServiceImpl.class);

    @Autowired(required = false)
    protected JdbcTemplate jdbcTemplate;

    @Autowired(required = false)
    private AbstractValidateCodeService abstractValidateCode;

    /**
     * 是否冻结， 1 冻结，0 未冻结
     */
    protected final String LOCKED = "1";

    /**
     * 根据手机号查询账号
     *
     * @param null:
     * @return null
     * @author pand
     * @date 2020-08-28 15:50
     */
    private static final String phoneSql = "select tpacc.tenant_id, tpacc.person_id, tpacc.phone, tpacc.locked from tp_account tpacc left join tp_person_basicinfo tpper on tpacc.person_id = tpper.person_id where tpper.phone = ? AND tpacc.enabled = '1' AND tpacc.actived = '1' AND tpper.actived = '1' LIMIT 1";

    /**
     * 手机验证码登陆，先校验验证码是否正确，验证码正确在根据手机号查询用户/账号是否存在，存在继续校验账号是否可用
     *
     * @param vo: SmsPersonVO的实体对象
     * @return com.jiuxi.security.core.entity.vo.SmsPersonVO
     * @author pand
     * @date 2020-08-27 14:48
     */
    @Override
    public AccountVO queryAccount(AccountVO vo) {
        String phone = vo.getPhone();
        String code = vo.getCode();
        // 校验验证码是否正确
        boolean validateCode = abstractValidateCode.validate(phone, code);
        if (!validateCode){
            throw new RuntimeException("验证码登陆失败！");
        }
        // 根据手机号查询用户信息
        AccountVO accountVO = this.queryPersonByPhone(phone);
        return accountVO;
    }

    /**
     * 根据手机号查询人员账号
     *
     * @param phone: 手机号
     * @return com.jiuxi.security.core.entity.vo.SmsPersonVO
     * @author pand
     * @date 2020-08-27 18:35
     */
    @Override
    public AccountVO queryPersonByPhone(String phone) {

        if (jdbcTemplate == null) {
            throw new RuntimeException("在执行认证时，jdbcTemplate为null，请先在项目中引入了连接池的配置...");
        }

        List<AccountVO> list = jdbcTemplate.query(phoneSql, new Object[]{phone}, new BeanPropertyRowMapper<>(AccountVO.class));
        if (list != null && list.size() != 1) {
            LOGGER.error("登录失败，手机号码不存在或该手机号码绑定异常，当前号码:{}", phone);
            throw new RuntimeException("登录失败，手机号码不存在！");
        }

        return list.get(0);
    }


    /**
     * 发送短信验证码
     *
     * @param phone: 手机号码
     * @return com.jiuxi.mvc.bean.JsonResponse
     * @author pand
     * @date 2020-08-26 15:29
     */
    @Override
    public boolean sendSmsValidateCode(String phone) {

        AccountVO vo = this.queryPersonByPhone(phone);

        // 校验是否冻结
        if (LOCKED.equals(vo.getLocked())) {
            LOGGER.error("登录失败，用户被冻结，当前号码:{}", phone);
            throw new RuntimeException("登录失败，账号被冻结");
        }

        // 生成验证码并发送
        return abstractValidateCode.create(phone);
    }

    /**
     * 根据用户名查询账户信息
     * 
     * @param userName 用户名
     * @return 返回对应的账户信息，如果未找到则返回null
     */
    @Override
    public AccountVO queryAccountByUsername(String userName) {
        // 这里可以根据实际需求实现，目前返回null
        // 因为SMS登录主要通过手机号，不通过用户名
        LOGGER.warn("SMS账户服务不支持通过用户名查询账户: {}", userName);
        return null;
    }
}
