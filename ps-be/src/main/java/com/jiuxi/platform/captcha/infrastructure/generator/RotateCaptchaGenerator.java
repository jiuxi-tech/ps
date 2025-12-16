package com.jiuxi.platform.captcha.infrastructure.generator;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;
import com.jiuxi.platform.captcha.domain.service.CaptchaGenerator;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaCoordinate;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaType;
import com.jiuxi.platform.captcha.infrastructure.config.CaptchaProperties;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    // 难度级别相关常量
    private static final int DIFFICULTY_EASY = 1;
    private static final int DIFFICULTY_MEDIUM = 2;
    private static final int DIFFICULTY_HARD = 3;
    
    private final Random random = new Random();
    
    // 注入验证码配置
    private final CaptchaProperties captchaProperties;
    
    @Autowired
    public RotateCaptchaGenerator(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }
    
    // 旋转图案类型枚举
    private enum RotatePatternType {
        ARROW, POINTER, GEOMETRIC, COMPLEX, STAR
    }
    
    @Override
    public CaptchaChallenge generateChallenge(CaptchaType captchaType) {
        if (!supports(captchaType)) {
            throw new IllegalArgumentException("不支持的验证码类型: " + captchaType);
        }
        
        CaptchaChallenge challenge = new CaptchaChallenge(captchaType);
        // 从配置中读取最大尝试次数
        challenge.setMaxAttempts(captchaProperties.getMaxAttempts());
        
        try {
            // 根据时间随机生成难度级别
            int difficulty = generateRandomDifficulty();
            RotatePatternType patternType = selectPatternType(difficulty);
            
            // 生成目标角度和旋转角度
            double targetAngle = generateTargetAngle(difficulty);
            double rotationAngle = generateRandomAngle(difficulty);
            
            // 设置正确位置（用角度表示，转换为整数）
            challenge.setCorrectPosition(new CaptchaCoordinate((int)targetAngle, 0));
            
            // 根据难度调整容差
            double tolerance = calculateAngleToleranceByDifficulty(difficulty);
            challenge.setTolerance(tolerance);
            
            // 生成原始图片和旋转后的图片
            BufferedImage originalImage = generateOriginalImage(patternType, difficulty);
            BufferedImage rotatedImage = rotateImage(originalImage, rotationAngle);
            
            // 转换为Base64格式
            challenge.setBackgroundImageData(imageToBase64(rotatedImage));
            
            // 设置元数据（保持向后兼容）
            challenge.setMetadata("correctAngle", targetAngle);
            challenge.setMetadata("imageSize", IMAGE_SIZE);
            challenge.setMetadata("rotationAngle", rotationAngle);
            challenge.setMetadata("difficulty", difficulty);
            challenge.setMetadata("patternType", patternType.name());
            challenge.setMetadata("tolerance", tolerance);
            
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
     * 生成随机难度级别
     */
    private int generateRandomDifficulty() {
        // 40%简单，40%中等，20%困难
        int rand = random.nextInt(100);
        if (rand < 40) {
            return DIFFICULTY_EASY;
        } else if (rand < 80) {
            return DIFFICULTY_MEDIUM;
        } else {
            return DIFFICULTY_HARD;
        }
    }
    
    /**
     * 根据难度选择图案类型
     */
    private RotatePatternType selectPatternType(int difficulty) {
        switch (difficulty) {
            case DIFFICULTY_EASY:
                return random.nextBoolean() ? RotatePatternType.ARROW : RotatePatternType.POINTER;
            case DIFFICULTY_MEDIUM:
                RotatePatternType[] mediumTypes = {RotatePatternType.ARROW, RotatePatternType.POINTER, RotatePatternType.GEOMETRIC};
                return mediumTypes[random.nextInt(mediumTypes.length)];
            case DIFFICULTY_HARD:
                RotatePatternType[] hardTypes = {RotatePatternType.GEOMETRIC, RotatePatternType.COMPLEX, RotatePatternType.STAR};
                return hardTypes[random.nextInt(hardTypes.length)];
            default:
                return RotatePatternType.ARROW;
        }
    }
    
    /**
     * 生成目标角度
     */
    private double generateTargetAngle(int difficulty) {
        switch (difficulty) {
            case DIFFICULTY_EASY:
                // 简单模式：0, 90, 180, 270度
                return random.nextInt(4) * 90;
            case DIFFICULTY_MEDIUM:
                // 中等模式：每30度一个点
                return random.nextInt(12) * 30;
            case DIFFICULTY_HARD:
                // 困难模式：每15度一个点
                return random.nextInt(24) * 15;
            default:
                return random.nextInt(12) * 30;
        }
    }
    
    /**
     * 根据难度生成随机旋转角度
     */
    private double generateRandomAngle(int difficulty) {
        return generateRandomAngle(); // 保持向后兼容
    }
    
    /**
     * 生成随机旋转角度（原有方法，保持向后兼容）
     */
    private double generateRandomAngle() {
        // 生成45-315度之间的角度，避免0度附近
        double minAngle = 45.0;
        double maxAngle = 315.0;
        return random.nextDouble() * (maxAngle - minAngle) + minAngle;
    }
    
    /**
     * 根据难度计算角度容差
     */
    private double calculateAngleToleranceByDifficulty(int difficulty) {
        switch (difficulty) {
            case DIFFICULTY_EASY:
                return 20.0; // 更宽松的容差
            case DIFFICULTY_MEDIUM:
                return 15.0; // 标准容差
            case DIFFICULTY_HARD:
                return 8.0; // 更严格的容差
            default:
                return 15.0;
        }
    }
    
    /**
     * 生成原始图片（简单的几何图形）
     */
    private BufferedImage generateOriginalImage() {
        return generateOriginalImage(RotatePatternType.ARROW, DIFFICULTY_MEDIUM);
    }
    
    /**
     * 根据图案类型和难度生成原始图片
     */
    private BufferedImage generateOriginalImage(RotatePatternType patternType, int difficulty) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 根据难度设置背景
        drawBackground(g2d, difficulty);
        
        // 根据图案类型绘制不同的旋转元素
        drawRotatePattern(g2d, patternType);
        
        g2d.dispose();
        return image;
    }
    
    /**
     * 根据难度绘制背景
     */
    private void drawBackground(Graphics2D g2d, int difficulty) {
        if (difficulty == DIFFICULTY_EASY) {
            // 简单模式：纯白色背景
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
        } else {
            // 中等和困难模式：渐变背景
            GradientPaint gradient = new GradientPaint(
                0, 0, Color.WHITE,
                IMAGE_SIZE, IMAGE_SIZE, new Color(240, 240, 245)
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
            
            // 困难模式添加更多背景装饰
            if (difficulty == DIFFICULTY_HARD) {
                drawBackgroundDecorations(g2d);
            }
        }
    }
    
    /**
     * 绘制背景装饰
     */
    private void drawBackgroundDecorations(Graphics2D g2d) {
        // 添加一些淡色圆圈作为干扰
        g2d.setColor(new Color(200, 200, 210, 100));
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(IMAGE_SIZE - 40) + 20;
            int y = random.nextInt(IMAGE_SIZE - 40) + 20;
            int size = random.nextInt(30) + 20;
            g2d.drawOval(x, y, size, size);
        }
        
        // 添加一些淡色线条
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i < 8; i++) {
            int x1 = random.nextInt(IMAGE_SIZE);
            int y1 = random.nextInt(IMAGE_SIZE);
            int x2 = random.nextInt(IMAGE_SIZE);
            int y2 = random.nextInt(IMAGE_SIZE);
            g2d.drawLine(x1, y1, x2, y2);
        }
    }
    
    /**
     * 绘制旋转图案
     */
    private void drawRotatePattern(Graphics2D g2d, RotatePatternType patternType) {
        switch (patternType) {
            case ARROW:
                drawArrowPattern(g2d);
                break;
            case POINTER:
                drawPointerPattern(g2d);
                break;
            case GEOMETRIC:
                drawGeometricPattern(g2d);
                break;
            case COMPLEX:
                drawComplexPattern(g2d);
                break;
            case STAR:
                drawStarPattern(g2d);
                break;
        }
    }
    
    /**
     * 绘制箭头形状（保持向后兼容）
     */
    private void drawArrow(Graphics2D g2d) {
        drawArrowPattern(g2d);
    }
    
    /**
     * 绘制箭头图案
     */
    private void drawArrowPattern(Graphics2D g2d) {
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
     * 绘制指针图案
     */
    private void drawPointerPattern(Graphics2D g2d) {
        int centerX = IMAGE_SIZE / 2;
        int centerY = IMAGE_SIZE / 2;
        int size = IMAGE_SIZE / 3;
        
        g2d.setColor(new Color(20, 100, 220));
        g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // 指针主体
        g2d.drawLine(centerX, centerY, centerX, centerY - size);
        
        // 指针头部（三角形）
        int[] xPoints = {centerX, centerX - 15, centerX + 15};
        int[] yPoints = {centerY - size, centerY - size + 20, centerY - size + 20};
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        // 指针中心圆
        g2d.setColor(new Color(50, 50, 50));
        g2d.fillOval(centerX - 12, centerY - 12, 24, 24);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(centerX - 8, centerY - 8, 16, 16);
    }
    
    /**
     * 绘制几何图案
     */
    private void drawGeometricPattern(Graphics2D g2d) {
        int centerX = IMAGE_SIZE / 2;
        int centerY = IMAGE_SIZE / 2;
        int size = IMAGE_SIZE / 4;
        
        g2d.setColor(new Color(200, 150, 20));
        g2d.setStroke(new BasicStroke(4));
        
        // 绘制不规则多边形指针
        int[] xPoints = {centerX, centerX - size/2, centerX - size/3, centerX, centerX + size/3, centerX + size/2};
        int[] yPoints = {centerY - size, centerY - size/3, centerY + size/2, centerY + size/3, centerY + size/2, centerY - size/3};
        g2d.fillPolygon(xPoints, yPoints, 6);
        
        // 添加边框
        g2d.setColor(new Color(150, 100, 10));
        g2d.drawPolygon(xPoints, yPoints, 6);
        
        // 添加中心标记
        g2d.setColor(Color.WHITE);
        g2d.fillOval(centerX - 6, centerY - 6, 12, 12);
    }
    
    /**
     * 绘制复杂图案
     */
    private void drawComplexPattern(Graphics2D g2d) {
        int centerX = IMAGE_SIZE / 2;
        int centerY = IMAGE_SIZE / 2;
        int size = IMAGE_SIZE / 4;
        
        // 渐变填充
        GradientPaint gradient = new GradientPaint(
            centerX, centerY - size, new Color(255, 100, 100),
            centerX, centerY + size, new Color(100, 100, 255)
        );
        g2d.setPaint(gradient);
        g2d.setStroke(new BasicStroke(3));
        
        // 复杂的箭头形状
        int[] xPoints = {centerX, centerX - size/2, centerX - size/4, centerX - size/4, 
                         centerX + size/4, centerX + size/4, centerX + size/2};
        int[] yPoints = {centerY - size, centerY - size/3, centerY - size/3, centerY + size, 
                         centerY + size, centerY - size/3, centerY - size/3};
        g2d.fillPolygon(xPoints, yPoints, 7);
        
        // 添加装饰线条
        g2d.setColor(new Color(50, 50, 50));
        g2d.setStroke(new BasicStroke(2));
        for (int i = 1; i < 6; i += 2) {
            g2d.drawLine(xPoints[i], yPoints[i], xPoints[i+1], yPoints[i+1]);
        }
    }
    
    /**
     * 绘制星形图案
     */
    private void drawStarPattern(Graphics2D g2d) {
        int centerX = IMAGE_SIZE / 2;
        int centerY = IMAGE_SIZE / 2;
        int outerRadius = IMAGE_SIZE / 4;
        int innerRadius = outerRadius / 2;
        
        // 五角星
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];
        
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI * i / 5 - Math.PI / 2; // 从顶部开始
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
            xPoints[i] = centerX + (int) (Math.cos(angle) * radius);
            yPoints[i] = centerY + (int) (Math.sin(angle) * radius);
        }
        
        // 渐变填充
        GradientPaint gradient = new GradientPaint(
            centerX, centerY - outerRadius, new Color(255, 215, 0),
            centerX, centerY + outerRadius, new Color(255, 140, 0)
        );
        g2d.setPaint(gradient);
        g2d.fillPolygon(xPoints, yPoints, 10);
        
        // 添加边框
        g2d.setColor(new Color(200, 100, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawPolygon(xPoints, yPoints, 10);
        
        // 添加中心圆
        g2d.setColor(new Color(100, 50, 0));
        g2d.fillOval(centerX - 8, centerY - 8, 16, 16);
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