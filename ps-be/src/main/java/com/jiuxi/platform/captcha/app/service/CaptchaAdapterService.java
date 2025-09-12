package com.jiuxi.platform.captcha.app.service;

import com.jiuxi.captcha.bean.vo.ImageCaptchaCheckVO;
import com.jiuxi.captcha.bean.vo.ImageCaptchaVO;
import com.jiuxi.platform.captcha.app.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 验证码适配器服务
 * 保持与原有接口的兼容性
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class CaptchaAdapterService implements CaptchaService {
    
    private final CaptchaApplicationService captchaApplicationService;
    
    // 原有的验证码服务作为后备
    @Autowired
    @Qualifier("captchaService")
    private com.jiuxi.captcha.core.service.CaptchaService legacyCaptchaService;
    
    @Autowired
    public CaptchaAdapterService(CaptchaApplicationService captchaApplicationService) {
        this.captchaApplicationService = captchaApplicationService;
    }
    
    @Override
    public ImageCaptchaVO getConcatCaptcha() {
        try {
            // 优先使用旧系统生成拼接验证码
            ImageCaptchaVO legacyResult = legacyCaptchaService.getConcatCaptcha();
            if (legacyResult != null) {
                System.out.println("使用旧系统生成拼接验证码");
                return legacyResult;
            }
        } catch (Exception legacyError) {
            System.out.println("旧系统生成拼接验证码失败: " + legacyError.getMessage());
        }
        
        try {
            CaptchaApplicationService.CaptchaResponse response = 
                captchaApplicationService.generateCaptcha("concat");
            
            if (response.isSuccess()) {
                System.out.println("使用新系统生成拼接验证码");
                return createImageCaptchaVO(response);
            } else {
                return createErrorImageCaptchaVO(response.getMessage());
            }
        } catch (Exception e) {
            return createErrorImageCaptchaVO("生成拼接验证码失败: " + e.getMessage());
        }
    }
    
    @Override
    public ImageCaptchaVO getRotateCaptcha() {
        try {
            // 优先使用旧系统生成旋转验证码
            ImageCaptchaVO legacyResult = legacyCaptchaService.getRotateCaptcha();
            if (legacyResult != null) {
                System.out.println("使用旧系统生成旋转验证码");
                return legacyResult;
            }
        } catch (Exception legacyError) {
            System.out.println("旧系统生成旋转验证码失败: " + legacyError.getMessage());
        }
        
        try {
            CaptchaApplicationService.CaptchaResponse response = 
                captchaApplicationService.generateCaptcha("rotate");
            
            if (response.isSuccess()) {
                System.out.println("使用新系统生成旋转验证码");
                return createImageCaptchaVO(response);
            } else {
                return createErrorImageCaptchaVO(response.getMessage());
            }
        } catch (Exception e) {
            return createErrorImageCaptchaVO("生成旋转验证码失败: " + e.getMessage());
        }
    }
    
    @Override
    public ImageCaptchaVO getSliderCaptcha() {
        try {
            // 优先使用旧系统生成滑块验证码
            ImageCaptchaVO legacyResult = legacyCaptchaService.getSliderCaptcha();
            if (legacyResult != null) {
                System.out.println("使用旧系统生成滑块验证码");
                return legacyResult;
            }
        } catch (Exception legacyError) {
            System.out.println("旧系统生成滑块验证码失败: " + legacyError.getMessage());
        }
        
        try {
            CaptchaApplicationService.CaptchaResponse response = 
                captchaApplicationService.generateCaptcha("slider");
            
            if (response.isSuccess()) {
                System.out.println("使用新系统生成滑块验证码");
                return createImageCaptchaVO(response);
            } else {
                return createErrorImageCaptchaVO(response.getMessage());
            }
        } catch (Exception e) {
            return createErrorImageCaptchaVO("生成滑块验证码失败: " + e.getMessage());
        }
    }
    
    @Override
    public String checkCaptcha(ImageCaptchaCheckVO imageCaptchaCheckVO) {
        try {
            if (imageCaptchaCheckVO == null) {
                return "请求参数不能为空";
            }
            
            String clientUuid = imageCaptchaCheckVO.getClientUuid();
            if (clientUuid == null || clientUuid.trim().isEmpty()) {
                return "验证码标识不能为空";
            }
            
            System.out.println("开始验证验证码: " + clientUuid);
            
            // 首先尝试使用旧系统验证，因为旧系统可能生成了这个验证码
            try {
                String legacyTicket = legacyCaptchaService.checkCaptcha(imageCaptchaCheckVO);
                if (legacyTicket != null && !legacyTicket.trim().isEmpty()) {
                    System.out.println("旧系统验证成功，票据: " + legacyTicket);
                    return legacyTicket;
                }
            } catch (Exception legacyError) {
                System.out.println("旧系统验证失败: " + legacyError.getMessage());
            }
            
            // 如果旧系统验证失败，尝试新系统
            CaptchaApplicationService.VerificationResponse response;
            
            // 检查是否有X坐标信息（滑块验证码）
            if (imageCaptchaCheckVO.getX() != null) {
                // 使用X坐标进行验证，Y坐标设为0
                Double x = imageCaptchaCheckVO.getX().doubleValue();
                Double y = 0.0;
                
                response = captchaApplicationService.verifyAnswer(clientUuid, x, y);
            } else {
                // 其他类型的验证码处理
                response = captchaApplicationService.verifyAnswer(clientUuid, 0.0, 0.0);
            }
            
            if (response.isSuccess()) {
                System.out.println("新系统验证成功，票据: " + response.getTicket());
                return response.getTicket();
            } else {
                System.out.println("新系统验证失败: " + response.getMessage());
                return response.getMessage();
            }
            
        } catch (Exception e) {
            System.err.println("验证码验证异常: " + e.getMessage());
            return "验证过程出错: " + e.getMessage();
        }
    }
    
    @Override
    public boolean checkTicket(String ticket) {
        try {
            if (ticket == null || ticket.trim().isEmpty()) {
                System.err.println("票据为空");
                return false;
            }
            
            System.out.println("开始验证票据: " + ticket);
            
            // 首先尝试新系统验证票据
            boolean newSystemResult = captchaApplicationService.consumeTicket(ticket);
            System.out.println("新系统票据验证结果: " + newSystemResult);
            
            if (newSystemResult) {
                return true;
            }
            
            // 如果新系统验证失败，尝试使用旧系统验证
            System.out.println("新系统验证失败，尝试旧系统验证票据: " + ticket);
            boolean legacyResult = legacyCaptchaService.checkTicket(ticket);
            System.out.println("旧系统票据验证结果: " + legacyResult);
            
            return legacyResult;
            
        } catch (Exception e) {
            System.err.println("验证票据失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 创建ImageCaptchaVO对象
     */
    private ImageCaptchaVO createImageCaptchaVO(CaptchaApplicationService.CaptchaResponse response) {
        ImageCaptchaVO vo = new ImageCaptchaVO();
        vo.setClientUuid(response.getChallengeId());
        vo.setBackgroundImage(response.getBackgroundImage());
        vo.setType(response.getCaptchaType());
        
        // 设置图片尺寸信息
        if (response.getMetadata() != null) {
            Object width = response.getMetadata().get("imageWidth");
            Object height = response.getMetadata().get("imageHeight");
            if (width instanceof Integer) {
                vo.setBgImageWidth((Integer) width);
            }
            if (height instanceof Integer) {
                vo.setBgImageHeight((Integer) height);
            }
        }
        
        return vo;
    }
    
    /**
     * 创建错误的ImageCaptchaVO对象
     */
    private ImageCaptchaVO createErrorImageCaptchaVO(String errorMessage) {
        ImageCaptchaVO vo = new ImageCaptchaVO();
        // 原有的VO没有result和repMsg字段，所以设置type为错误标识
        vo.setType("error");
        vo.setClientUuid("error");
        return vo;
    }
    
}