package com.jiuxi.captcha.core.controller;


import cn.hutool.core.util.StrUtil;
import com.jiuxi.captcha.bean.vo.ImageCaptchaCheckVO;
import com.jiuxi.captcha.bean.vo.ImageCaptchaVO;
import com.jiuxi.captcha.core.service.CaptchaService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.core.core.annotation.RateLimiterAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 滑动验证码
 * @author 杨攀
 * @date 2021/3/8 11:24
 */
@RestController
@RequestMapping("/platform/captcha")
public class CaptchaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaController.class);

    /**
     * 验证码业务处理对象
     */
    @Autowired
    private CaptchaService captchaService;


    /**
     * 获取 拼接 验证码, 防攻击限流 10 分钟，1千次
     */
    @RequestMapping("/get-concat-captcha")
    @RateLimiterAnnotation(count = 1000, timeout = 600 * 1000)
    public JsonResponse getConcatCaptcha(HttpServletRequest request) {
        ImageCaptchaVO captchaVO = captchaService.getConcatCaptcha();
        if(null != captchaVO){
            return JsonResponse.buildSuccess(captchaVO);
        }
        return JsonResponse.buildFailure("获取拼接验证码失败");
    }

    /**
     * 获取 旋转 验证码， 防攻击限流 10 分钟，1千次
     * @author 杨攀
     * @date 2022/12/1 19:17
     * @param
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/get-rotate-captcha")
    @RateLimiterAnnotation(count = 1000, timeout = 600 * 1000)
    public JsonResponse getRotateCaptcha(HttpServletRequest request) {
        ImageCaptchaVO captchaVO = captchaService.getRotateCaptcha();
        if(null != captchaVO){
            return  JsonResponse.buildSuccess(captchaVO);
        }
        return JsonResponse.buildFailure("获取旋转验证码失败");
    }

    /**
     * 获取 滑块 验证码， 防攻击限流 10 分钟，1千次
     * @author 杨攀
     * @date 2022/12/1 19:17
     * @param
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/get-slider-captcha")
    @RateLimiterAnnotation(count = 1000, timeout = 600 * 1000)
    public JsonResponse getSliderCaptcha(HttpServletRequest request) {
        ImageCaptchaVO captchaVO = captchaService.getSliderCaptcha();
        if(null != captchaVO){
            return  JsonResponse.buildSuccess(captchaVO);
        }
        return JsonResponse.buildFailure("获取旋转验证码失败");
    }

    /**
     * 校验 验证码
     * @return 验证结果
     */
    @RequestMapping(value = "/check-captcha", method = {RequestMethod.GET, RequestMethod.POST})
    public JsonResponse checkCaptcha(ImageCaptchaCheckVO imageCaptchaCheckVO) {

        String ticket = captchaService.checkCaptcha(imageCaptchaCheckVO);
        if(StrUtil.isNotBlank(ticket)){
            return  JsonResponse.buildSuccess(ticket);
        }
        return JsonResponse.buildFailure("验证失败，请重试！");
    }


    /**
     * 校验 验证码 票据
     * @return 验证结果
     */
    @RequestMapping("/check-ticket")
    public JsonResponse checkTicket(String ticket) {
        boolean  bool = captchaService.checkTicket(ticket);
        if(bool){
            return  JsonResponse.buildSuccess();
        }
        return JsonResponse.buildFailure("验证失败，请重试！");
    }

}
