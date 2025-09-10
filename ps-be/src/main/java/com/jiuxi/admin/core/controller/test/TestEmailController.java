package com.jiuxi.admin.core.controller.test;

import com.jiuxi.admin.core.service.EmailService;
import com.jiuxi.common.bean.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 邮件服务测试控制器
 * @ClassName: TestEmailController
 * @Author: Trae AI
 * @Date: 2025/01/21
 * @Copyright: 2025 www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/test_email")
public class TestEmailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestEmailController.class);

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    @Autowired(required = false)
    @Qualifier("adminEmailService")
    private EmailService adminEmailService;

    @Autowired(required = false)
    private EmailService commonEmailService;

    /**
     * 测试邮件服务配置状态
     *
     * @return JsonResponse
     */
    @RequestMapping("/test_config_status")
    public JsonResponse testConfigStatus() {
        LOGGER.info("开始测试邮件服务配置状态");
        
        StringBuilder result = new StringBuilder();
        result.append("邮件服务配置状态检查:\n");
        
        // 检查JavaMailSender注入状态
        result.append("1. JavaMailSender注入状态: ");
        if (javaMailSender != null) {
            result.append("成功 - 类型: ").append(javaMailSender.getClass().getName()).append("\n");
        } else {
            result.append("失败 - JavaMailSender未注入\n");
        }
        
        // 检查adminEmailService注入状态
        result.append("2. adminEmailService注入状态: ");
        if (adminEmailService != null) {
            result.append("成功 - 类型: ").append(adminEmailService.getClass().getName()).append("\n");
        } else {
            result.append("失败 - adminEmailService未注入\n");
        }
        
        // 检查commonEmailService注入状态
        result.append("3. commonEmailService注入状态: ");
        if (commonEmailService != null) {
            result.append("成功 - 类型: ").append(commonEmailService.getClass().getName()).append("\n");
        } else {
            result.append("失败 - commonEmailService未注入\n");
        }
        
        LOGGER.info("邮件服务配置状态检查结果: {}", result.toString());
        return JsonResponse.buildSuccess(result.toString());
    }

    /**
     * 测试发送邮件
     *
     * @param email 收件人邮箱
     * @return JsonResponse
     */
    @RequestMapping("/test_send_email")
    public JsonResponse testSendEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            email = "159257119@qq.com"; // 默认测试邮箱
        }
        
        LOGGER.info("开始测试发送邮件到: {}", email);
        
        if (adminEmailService == null) {
            return JsonResponse.buildFailure("adminEmailService未注入，无法发送邮件");
        }
        
        try {
            boolean result = adminEmailService.sendVerificationCode(email, "测试邮件", "123456");
            if (result) {
                LOGGER.info("测试邮件发送成功到: {}", email);
                return JsonResponse.buildSuccess("邮件发送成功");
            } else {
                LOGGER.warn("测试邮件发送失败到: {}", email);
                return JsonResponse.buildFailure("邮件发送失败");
            }
        } catch (Exception e) {
            LOGGER.error("测试邮件发送异常到: {}, 错误: {}", email, e.getMessage(), e);
            return JsonResponse.buildFailure("邮件发送异常: " + e.getMessage());
        }
    }
}