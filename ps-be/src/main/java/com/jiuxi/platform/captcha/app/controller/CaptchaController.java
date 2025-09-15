package com.jiuxi.platform.captcha.app.controller;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.platform.captcha.app.service.CaptchaService;
import com.jiuxi.platform.captcha.bean.vo.ImageCaptchaCheckVO;
import com.jiuxi.platform.captcha.bean.vo.ImageCaptchaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 验证码控制器
 * 提供滑块、旋转、拼接验证码的生成和验证功能
 */
@RestController
@RequestMapping("/platform/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    /**
     * 获取滑块验证码
     * @return JsonResponse 包含验证码图片和相关信息
     */
    @GetMapping("/get-slider-captcha")
    public JsonResponse getSliderCaptcha() {
        try {
            ImageCaptchaVO captcha = captchaService.getSliderCaptcha();
            return JsonResponse.buildSuccess(captcha);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取滑块验证码失败: " + e.getMessage());
        }
    }

    /**
     * 获取旋转验证码
     * @return JsonResponse 包含验证码图片和相关信息
     */
    @GetMapping("/get-rotate-captcha")
    public JsonResponse getRotateCaptcha() {
        try {
            ImageCaptchaVO captcha = captchaService.getRotateCaptcha();
            return JsonResponse.buildSuccess(captcha);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取旋转验证码失败: " + e.getMessage());
        }
    }

    /**
     * 获取拼接验证码
     * @return JsonResponse 包含验证码图片和相关信息
     */
    @GetMapping("/get-concat-captcha")
    public JsonResponse getConcatCaptcha() {
        try {
            ImageCaptchaVO captcha = captchaService.getConcatCaptcha();
            return JsonResponse.buildSuccess(captcha);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取拼接验证码失败: " + e.getMessage());
        }
    }

    /**
     * 验证验证码
     * @param checkVO 验证码验证信息
     * @return JsonResponse 验证结果
     */
    @PostMapping("/check-captcha")
    public JsonResponse checkCaptcha(@RequestBody ImageCaptchaCheckVO checkVO) {
        try {
            String result = captchaService.checkCaptcha(checkVO);
            if ("success".equals(result)) {
                return JsonResponse.buildSuccess("验证成功");
            } else {
                return JsonResponse.buildFailure(result);
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("验证码验证失败: " + e.getMessage());
        }
    }
}