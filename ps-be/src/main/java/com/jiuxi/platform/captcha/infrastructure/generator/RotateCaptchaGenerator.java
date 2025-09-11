package com.jiuxi.platform.captcha.infrastructure.generator;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;
import com.jiuxi.platform.captcha.domain.service.CaptchaGenerator;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaType;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 旋转验证码生成器
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Component
public class RotateCaptchaGenerator implements CaptchaGenerator {
    
    private static final int IMAGE_SIZE = 200;
    private final Random random = new Random();
    
    @Override
    public CaptchaChallenge generateChallenge(CaptchaType captchaType) {
        if (!supports(captchaType)) {
            throw new IllegalArgumentException("不支持的验证码类型: " + captchaType);
        }
        
        CaptchaChallenge challenge = new CaptchaChallenge(captchaType);
        
        try {
            // 生成随机旋转角度
            double rotationAngle = generateRandomAngle();
            
            // 生成原始图片
            BufferedImage originalImage = generateOriginalImage();
            
            // 生成旋转后的图片
            BufferedImage rotatedImage = rotateImage(originalImage, rotationAngle);
            
            // 转换为Base64格式
            challenge.setBackgroundImageData(imageToBase64(rotatedImage));
            
            // 设置正确角度到元数据
            challenge.setMetadata("correctAngle", 360.0 - rotationAngle); // 用户需要反向旋转
            challenge.setMetadata("imageSize", IMAGE_SIZE);
            
        } catch (Exception e) {
            throw new RuntimeException("生成旋转验证码失败", e);
        }
        
        return challenge;
    }
    
    @Override
    public boolean supports(CaptchaType captchaType) {
        return captchaType == CaptchaType.ROTATE;
    }
    
    @Override
    public String getGeneratorName() {
        return "RotateCaptchaGenerator";
    }
    
    /**
     * 生成随机旋转角度
     */
    private double generateRandomAngle() {
        // 生成45-315度之间的角度，避免0度附近
        double minAngle = 45.0;
        double maxAngle = 315.0;
        return random.nextDouble() * (maxAngle - minAngle) + minAngle;
    }
    
    /**
     * 生成原始图片（简单的几何图形）
     */
    private BufferedImage generateOriginalImage() {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 白色背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
        
        // 绘制一个明显的不对称图形（箭头形状）
        drawArrow(g2d);
        
        g2d.dispose();
        return image;
    }
    
    /**
     * 绘制箭头形状
     */
    private void drawArrow(Graphics2D g2d) {
        int centerX = IMAGE_SIZE / 2;
        int centerY = IMAGE_SIZE / 2;
        int size = IMAGE_SIZE / 3;
        
        // 箭头主体
        g2d.setColor(new Color(70, 130, 180));
        g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // 箭头杆
        g2d.drawLine(centerX, centerY + size/2, centerX, centerY - size/2);
        
        // 箭头头部
        int[] xPoints = {centerX, centerX - size/3, centerX + size/3};
        int[] yPoints = {centerY - size/2, centerY - size/6, centerY - size/6};
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        // 添加一些装饰点
        g2d.setColor(new Color(220, 20, 60));
        for (int i = 0; i < 5; i++) {
            int x = centerX - size/2 + random.nextInt(size);
            int y = centerY - size/2 + random.nextInt(size);
            g2d.fillOval(x - 3, y - 3, 6, 6);
        }
    }
    
    /**
     * 旋转图片
     */
    private BufferedImage rotateImage(BufferedImage originalImage, double angle) {
        BufferedImage rotatedImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = rotatedImage.createGraphics();
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // 白色背景
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
        
        // 设置旋转变换
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), IMAGE_SIZE / 2.0, IMAGE_SIZE / 2.0);
        g2d.setTransform(transform);
        
        // 绘制原始图片
        g2d.drawImage(originalImage, 0, 0, null);
        
        g2d.dispose();
        return rotatedImage;
    }
    
    /**
     * 将图片转换为Base64字符串
     */
    private String imageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        byte[] imageBytes = baos.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }
}