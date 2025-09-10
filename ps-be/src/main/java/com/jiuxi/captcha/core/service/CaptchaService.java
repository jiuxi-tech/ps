package com.jiuxi.captcha.core.service;


import com.jiuxi.captcha.bean.vo.ImageCaptchaCheckVO;
import com.jiuxi.captcha.bean.vo.ImageCaptchaVO;

/**
 * 验证码业务处理类
 * @author 杨攀
 * @date 2021/3/9 14:19
 */
public interface CaptchaService {


    /**
     * 获取 拼接 验证码
     * @author 杨攀
     * @date 2022/12/12 16:40
     * @return com.jiuxi.captcha.bean.vo.CaptchaVO
     */
    ImageCaptchaVO getConcatCaptcha();


    /**
     * 获取 旋转 验证码
     * @author 杨攀
     * @date 2022/12/12 16:40
     * @return com.jiuxi.captcha.bean.vo.CaptchaVO
     */
    ImageCaptchaVO getRotateCaptcha();

    /**
     * 获取 滑块 验证码
     * @author 杨攀
     * @date 2023/4/19 17:53
     * @param
     * @return com.jiuxi.captcha.bean.vo.ImageCaptchaVO
     */
    ImageCaptchaVO getSliderCaptcha();

    /**
     * 校验 验证码
     * @author 杨攀
     * @date 2022/12/16 14:20
     * @param imageCaptchaCheckVO
     * @return boolean
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
