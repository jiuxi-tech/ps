package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
// import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.jiuxi.admin.autoconfig.AdminConfigurationProperties;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpMemVerificationCode;
import com.jiuxi.admin.core.bean.vo.TpMemVerificationCodeVO;
import com.jiuxi.admin.core.mapper.TpMemVerificationCodeMapper;
import com.jiuxi.admin.core.service.TpSmsSendService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
// import com.jiuxi.sms.core.service.SmsSendService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @ClassName: TpMemVerificationCodeServiceImpl
 * @Description:
 * @Author pand
 * @Date 2021-04-22 15:23:29
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class TpDbVerificationCodeServiceImpl implements TpSmsSendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpDbVerificationCodeServiceImpl.class);

    @Autowired
    private TpMemVerificationCodeMapper tpMemVerificationCodeMapper;

    @Autowired
    private SecurityConfigurationProperties properties;

    @Autowired
    private AdminConfigurationProperties adminProperties;

    // @Autowired(required = false)
    // @Qualifier(value = "smsSendService")
    // private SmsSendService smsSendService;

    /**
     * 发送短信验证码，模版替换的key固定为code，如果不为code需要将key值传入
     * 1。生成验证码并发送短信（同一个号码同一种业务需要控制发送间隔时长）
     * 2。短信发送成功，将手机号，验证码等信息入库
     * 3。返回成功/失败
     *
     * @param vo:
     * @param templatCode: 发送短信的模版code
     * @param codeKey:     模版替换的key，默认code
     * @return java.lang.String
     * @author pand
     * @date 2021-04-22 17:15
     */
    @Override
    public void send(TpMemVerificationCodeVO vo, String templatCode, String codeKey) {
        // if (null == smsSendService) {
        //     throw new TopinfoRuntimeException(-1, "没有短信服务可用！");
        // }
        throw new TopinfoRuntimeException(-1, "短信服务不可用！");

        /*
        String phone = vo.getPhone();
        String busType = vo.getBusType();
        if (StrUtil.isBlank(busType)) {
            throw new TopinfoRuntimeException(-1, "请指定业务类型");
        }
        // 当前时间的时间戳
        Long currentTimeMillis = CommonDateUtil.currentTimeMillis();
        // 1。查询最近的一条短信验证码，校验发送时间是否超过1分钟
        TpMemVerificationCode verificationCode = tpMemVerificationCodeMapper.select(phone, vo.getBusType());
        if (null != verificationCode) {
            String oldTime = verificationCode.getSendTimeStamp();
            Long diff = (currentTimeMillis - Long.valueOf(oldTime)) / 1000;
            // 验证发送的间隔时长
            if (diff < adminProperties.getSmsCode().getTimeInterval()) {
                throw new TopinfoRuntimeException(500, "请勿1分钟后再次发送验证码！");
            }
        }

        // 生成验证码
        String code = RandomStringUtils.randomNumeric(properties.getValidateCode().getLength());
        LOGGER.info("验证码 = {}", code);

        try {
            // 给用户发短信
            JSONObject templateParam = new JSONObject();
            templateParam.put(Optional.ofNullable(codeKey).orElse("code"), code);
            // 发短信
            // SendSmsResponse sendSmsResponse = smsSendService.sendSms(phone, templatCode, templateParam);

            // 验证码信息入库
            if (null != verificationCode) {
                // 重新生成新验证码，更新掉原来的验证码
                tpMemVerificationCodeMapper.updateCodeByCodeId(verificationCode.getCodeId(), code, String.valueOf(currentTimeMillis));
            } else {
                //
                TpMemVerificationCode bean = new TpMemVerificationCode();
                // 转换成数据库对象
                BeanUtil.copyProperties(vo, bean);
                // 设置主键id
                bean.setCodeId(SnowflakeIdUtil.nextIdStr());
                bean.setPhone(phone);
                bean.setBusType(busType);
                bean.setVerificationCode(code);
                bean.setSendTimeStamp(String.valueOf(currentTimeMillis));
                bean.setCreateTime(CommonDateUtil.now());
                tpMemVerificationCodeMapper.add(bean);
            }

        } catch (Exception e) {
            LOGGER.error("发送验证码失败！vo:{}, templatCode:{}, codeKey:{}, 错误:{}", JSONObject.toJSONString(vo), templatCode, codeKey, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "发送验证码失败！" : e.getMessage());
        }
        */
    }

    /**
     * 校验短信验证码
     * 1。根据手机号，业务类型查询最近的一条短信验证码
     * 2。判断短信验证码是否过期
     *
     * @param phone:
     * @param busType:
     * @param vcode:
     * @return com.jiuxi.admin.core.bean.entity.TpMemVerificationCode
     * @author pand
     * @date 2021-04-22 17:17
     */
    @Override
    public boolean check(String phone, String busType, String vcode) {
        TpMemVerificationCode verificationCode = null;
        try {
            verificationCode = tpMemVerificationCodeMapper.selectByVcode(phone, busType, vcode);
            if (null == verificationCode) {
                throw new TopinfoRuntimeException(-1, "验证码无效");
            }

            // 时间戳转换成秒
            Long diff = (CommonDateUtil.currentTimeMillis() - Long.valueOf(verificationCode.getSendTimeStamp())) / 1000;
            if (diff > properties.getValidateCode().getExpireIn()) {
                throw new TopinfoRuntimeException(-1, "验证码已失效！");
            }

            return true;
        } catch (Exception e) {
            LOGGER.error("验证码无效！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "验证码无效！");
        } finally {
            if (null != verificationCode) {
                this.delete(verificationCode.getCodeId());
            }
        }
    }

    /**
     * 验证码取出后手动删除，保证一个验证码只能使用一次
     *
     * @param codeId:
     * @return void
     * @author pand
     * @date 2021-04-23 13:51
     */
    public void delete(String codeId) {
        try {
            tpMemVerificationCodeMapper.delete(codeId);
        } catch (Exception e) {
            LOGGER.error("验证码删除失败! codeId:{}, 错误:{}", codeId, ExceptionUtils.getStackTrace(e));
        }
    }

}
