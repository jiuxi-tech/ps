package com.jiuxi.captcha.core.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.captcha.autoconfig.TopinfoCaptchaAutoProperties;
import com.jiuxi.captcha.bean.vo.ImageCaptchaCheckVO;
import com.jiuxi.captcha.bean.vo.ImageCaptchaVO;
import com.jiuxi.captcha.constant.CaptchaConstant;
import com.jiuxi.captcha.core.cache.CaptchaCacheService;
import com.jiuxi.captcha.core.generator.ImageCaptchaGenerator;
import com.jiuxi.captcha.core.generator.impl.ImageRotateCaptchaGenerator;
import com.jiuxi.captcha.core.generator.impl.ImageSliderCaptchaGenerator;
import com.jiuxi.captcha.core.service.CaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 验证码业务实现类
 *
 * @author 杨攀
 * @date 2021/3/8 11:33
 */
public class CaptchaServiceImpl implements CaptchaService {


    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaServiceImpl.class);


    /** 验证码缓存 */
    @Autowired
    private CaptchaCacheService cacheService;

    /** 配置 */
    @Autowired
    private TopinfoCaptchaAutoProperties captchaAutoProperties;

    /**
     * 获取 拼接 验证码
     * @author 杨攀
     * @date 2022/12/12 16:41
     * @param
     * @return com.jiuxi.captcha.bean.vo.CaptchaVO
     */
    @Override
    public ImageCaptchaVO getConcatCaptcha() {
        // 获取验证码
        return null;
    }

    /**
     * 获取 旋转 验证码
     * @author 杨攀
     * @date 2022/12/12 16:41
     * @param
     * @return com.jiuxi.captcha.bean.vo.CaptchaVO
     */
    @Override
    public ImageCaptchaVO getRotateCaptcha() {
        // 获取验证码
        ImageCaptchaGenerator instance = ImageRotateCaptchaGenerator.getInstance(cacheService);
        return instance.generateCaptchaImage();
    }

    /**
     * 获取 滑块 验证码
     * @author 杨攀
     * @date 2023/4/19 17:53
     * @param
     * @return com.jiuxi.captcha.bean.vo.ImageCaptchaVO
     */
    @Override
    public ImageCaptchaVO getSliderCaptcha() {
        ImageSliderCaptchaGenerator instance = ImageSliderCaptchaGenerator.getInstance(cacheService);
        return instance.generateCaptchaImage();
    }

    /**
     * 校验 验证码
     * @author 杨攀
     * @date 2022/12/16 14:20
     * @param imageCaptchaCheckVO
     * @return boolean
     */
    @Override
    public String checkCaptcha(ImageCaptchaCheckVO imageCaptchaCheckVO) {

        String enable = captchaAutoProperties.getEnable();
        // 如果不启用，则直接返回 true
        if(!CaptchaConstant.CAPTCHA_DEFAULT_SWITCH.equalsIgnoreCase(enable)){
            // 随便返回一个 ticket字符串，因为不开启验证，后续也不需要校验
            return IdUtil.fastSimpleUUID();
        }

        if(CaptchaConstant.CaptchaType.ROTATE.equals(imageCaptchaCheckVO.getType())){
            // 旋转验证码
            ImageCaptchaGenerator instance = ImageRotateCaptchaGenerator.getInstance(cacheService);
            return instance.checkCaptcha(imageCaptchaCheckVO);

        }else if(CaptchaConstant.CaptchaType.SLIDER.equals(imageCaptchaCheckVO.getType())){
            // 滑块验证码
            ImageCaptchaGenerator instance = ImageSliderCaptchaGenerator.getInstance(cacheService);
            return instance.checkCaptcha(imageCaptchaCheckVO);

        }else if(CaptchaConstant.CaptchaType.CONCAT.equals(imageCaptchaCheckVO.getType())){
            LOGGER.error("暂不支持拼接验证码，类型:{}！", imageCaptchaCheckVO.getType());
        }else {
            LOGGER.error("不支持该类型的验证码，类型:{}！", imageCaptchaCheckVO.getType());
        }
        return null;
    }

    /**
     * 校验 票据
     * @author 杨攀
     * @date 2022/12/16 14:21
     * @param ticket 票据
     * @return boolean
     */
    @Override
    public boolean checkTicket(String ticket) {

        String enable = captchaAutoProperties.getEnable();
        // 如果不启用，则直接返回 true
        if(!CaptchaConstant.CAPTCHA_DEFAULT_SWITCH.equalsIgnoreCase(enable)){
            // 返回true，因为不开启验证
            return true;
        }

        // 票据前缀
        String ticketKey = CaptchaConstant.RedisKeyPro.PLATFORM_CAPTCHA_TICKET + ticket;
        String cacheTicket =  cacheService.getString(ticketKey);
        if(StrUtil.isNotBlank(cacheTicket)){
            return true;
        }
        return false;
    }
}
