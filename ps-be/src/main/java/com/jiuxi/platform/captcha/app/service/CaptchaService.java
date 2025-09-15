package com.jiuxi.platform.captcha.app.service;

import com.jiuxi.platform.captcha.bean.vo.ImageCaptchaCheckVO;
import com.jiuxi.platform.captcha.bean.vo.ImageCaptchaVO;

/**
 * 验证码业务处理接口
 * 保持与原有接口的兼容性
 * 
 * @author DDD Refactor
 * @date 2025-09-12
 */
public interface CaptchaService {

    /**
     * 获取 拼接 验证码
     * @author 杨攀
     * @date 2022/12/12 16:40
     * @return com.jiuxi.platform.captcha.bean.vo.ImageCaptchaVO
     */
    ImageCaptchaVO getConcatCaptcha();

    /**
     * 获取 旋转 验证码
     * @author 杨攀
     * @date 2022/12/12 16:40
     * @return com.jiuxi.platform.captcha.bean.vo.ImageCaptchaVO
     */
    ImageCaptchaVO getRotateCaptcha();

    /**
     * 获取 滑块 验证码
     * @author 杨攀
     * @date 2023/4/19 17:53
     * @param
     * @return com.jiuxi.platform.captcha.bean.vo.ImageCaptchaVO
     */
    ImageCaptchaVO getSliderCaptcha();

    /**
     * 校验 验证码
     * @author 杨攀
     * @date 2022/12/16 14:20
     * @param imageCaptchaCheckVO
     * @return String
     */
    String checkCaptcha(ImageCaptchaCheckVO imageCaptchaCheckVO);

    /**
     * 校验 票据
     * @author 杨攀
     * @date 2022/12/16 14:21
     * @param ticket 票据
     * @return boolean
     */
    boolean checkTicket(String ticket);
}