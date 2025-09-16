package com.jiuxi.platform.captcha.app.controller;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.platform.captcha.app.service.CaptchaService;
import com.jiuxi.platform.captcha.bean.vo.ImageCaptchaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试验证码接口
 * 用于测试修复后的验证码接口返回字段
 * 
 * @author Test
 * @date 2025-09-16
 */
@RestController
@RequestMapping("/captcha")
public class TestCaptchaController {
    
    @Autowired
    private CaptchaService captchaService;
    
    /**
     * 测试滑块验证码接口
     * 验证返回字段是否完整
     */
    @RequestMapping("/test_slider_captcha")
    public JsonResponse testSliderCaptcha() {
        try {
            ImageCaptchaVO captcha = captchaService.getSliderCaptcha();
            
            // 打印返回的字段信息
            System.out.println("=== 滑块验证码返回字段测试 ===");
            System.out.println("clientUuid: " + captcha.getClientUuid());
            System.out.println("backgroundImage: " + (captcha.getBackgroundImage() != null ? "存在" : "null"));
            System.out.println("sliderImage: " + (captcha.getSliderImage() != null ? "存在" : "null"));
            System.out.println("type: " + captcha.getType());
            System.out.println("bgImageWidth: " + captcha.getBgImageWidth());
            System.out.println("bgImageHeight: " + captcha.getBgImageHeight());
            System.out.println("sliderImageWidth: " + captcha.getSliderImageWidth());
            System.out.println("sliderImageHeight: " + captcha.getSliderImageHeight());
            System.out.println("randomX: " + captcha.getRandomX());
            System.out.println("randomY: " + captcha.getRandomY());
            System.out.println("=== 测试完成 ===");
            
            return JsonResponse.buildSuccess(captcha);
        } catch (Exception e) {
            System.out.println("测试失败: " + e.getMessage());
            e.printStackTrace();
            return JsonResponse.buildFailure("测试失败: " + e.getMessage());
        }
    }
}