package com.jiuxi.captcha.core.generator;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.captcha.bean.vo.ImageCaptchaCheckVO;
import com.jiuxi.captcha.bean.vo.ImageCaptchaVO;
import com.jiuxi.captcha.core.cache.CaptchaCacheService;

/**
 * @ClassName: ImageCaptchaGenerator
 * @Description: 图片验证码生成器
 * @Author: 杨攀
 * @Date: 2022/12/12 17:11
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public interface ImageCaptchaGenerator {


    /**
     * 获取验证码
     * @author 杨攀
     * @date 2022/12/13 16:13
     * @param
     * @return com.jiuxi.captcha.bean.vo.ImageCaptchaVO
     */
    ImageCaptchaVO generateCaptchaImage();


    /**
     * 校验 验证码
     * @author 杨攀
     * @date 2022/12/14 17:20
     * @param trackVO
     * @return String
     */
    String checkCaptcha(ImageCaptchaCheckVO trackVO);


}
