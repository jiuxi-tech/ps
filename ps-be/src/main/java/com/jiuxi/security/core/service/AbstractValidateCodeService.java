package com.jiuxi.security.core.service;

import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import com.jiuxi.security.core.entity.vo.ValidateCodeVO;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 验证码的处理，包括生成，保存，发送，校验
 * @ClassName: AbstractValidateCode
 * @Author: pdd
 * @Date: 2020-08-27 13:45
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public abstract class AbstractValidateCodeService implements ValidateCodeService {


    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractValidateCodeService.class);

    @Autowired
    private SecurityConfigurationProperties properties;

    /**
     * 创建校验码
     *
     * @throws Exception
     */
    public boolean create(String phone) {
        ValidateCodeVO validateCode = this.generate();
        try {

            this.save(phone, validateCode);
            // 将验证码发送到手机
            this.send(phone, validateCode.getCode());

            return true;
        } catch (Exception e) {
            LOGGER.error("验证码生成失败！错误信息：{}", e);
            return false;
        }
    }

    /**
     * 生成验证码
     *
     * @return ValidateCodeVO
     * @author pand
     * @date 2020-08-27 11:16
     */
    @Override
    public ValidateCodeVO generate() {
        String code = RandomStringUtils.randomNumeric(properties.getValidateCode().getLength());
        return new ValidateCodeVO(code, properties.getValidateCode().getExpireIn());
    }

}
