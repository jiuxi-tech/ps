package com.jiuxi.platform.captcha.infrastructure.generator;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;
import com.jiuxi.platform.captcha.domain.service.CaptchaGenerator;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaCoordinate;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaType;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 滑块验证码生成器
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Component
public class SliderCaptchaGenerator implements CaptchaGenerator {
    
    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 160;
    private static final int PUZZLE_WIDTH = 60;
    private static final int PUZZLE_HEIGHT = 60;
    
    private final Random random = new Random();
    
    @Override
    public CaptchaChallenge generateChallenge(CaptchaType captchaType) {
        if (!supports(captchaType)) {
            throw new IllegalArgumentException("不支持的验证码类型: " + captchaType);
        }
        
        CaptchaChallenge challenge = new CaptchaChallenge(captchaType);
        
        try {
            // 生成随机拼图位置
            CaptchaCoordinate puzzlePosition = generatePuzzlePosition();
            challenge.setCorrectPosition(puzzlePosition);
            
            // 生成背景图片和拼图片
            BufferedImage backgroundImage = generateBackgroundImage();
            BufferedImage puzzleImage = generatePuzzleImage(backgroundImage, puzzlePosition);
            
            // 转换为Base64格式
            challenge.setBackgroundImageData(imageToBase64(backgroundImage));
            challenge.setPuzzleImageData(imageToBase64(puzzleImage));
            
            // 设置元数据
            challenge.setMetadata("imageWidth", IMAGE_WIDTH);
            challenge.setMetadata("imageHeight", IMAGE_HEIGHT);
            challenge.setMetadata("puzzleWidth", PUZZLE_WIDTH);
            challenge.setMetadata("puzzleHeight", PUZZLE_HEIGHT);
            
        } catch (Exception e) {
            throw new RuntimeException("生成滑块验证码失败", e);
        }
        
        return challenge;
    }
    
    @Override
    public boolean supports(CaptchaType captchaType) {
        return captchaType == CaptchaType.SLIDER;
    }
    
    @Override
    public String getGeneratorName() {
        return "SliderCaptchaGenerator";
    }
    
    /**
     * 生成拼图位置
     */
    private CaptchaCoordinate generatePuzzlePosition() {
        // 确保拼图不会超出图片边界，且不会太靠边
        int minX = PUZZLE_WIDTH / 2 + 20;
        int maxX = IMAGE_WIDTH - PUZZLE_WIDTH / 2 - 20;
        int minY = PUZZLE_HEIGHT / 2 + 20;
        int maxY = IMAGE_HEIGHT - PUZZLE_HEIGHT / 2 - 20;
        
        int x = random.nextInt(maxX - minX + 1) + minX;
        int y = random.nextInt(maxY - minY + 1) + minY;
        
        return new CaptchaCoordinate(x, y);
    }
    
    /**
     * 生成背景图片
     */
    private BufferedImage generateBackgroundImage() {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 生成渐变背景
        GradientPaint gradient = new GradientPaint(
            0, 0, getRandomColor(150, 200), 
            IMAGE_WIDTH, IMAGE_HEIGHT, getRandomColor(100, 150)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        
        // 添加一些随机的装饰元素
        drawRandomDecorations(g2d);
        
        g2d.dispose();
        return image;
    }
    
    /**
     * 生成拼图片（从背景图中抠出的部分）
     */
    private BufferedImage generatePuzzleImage(BufferedImage backgroundImage, CaptchaCoordinate position) {
        BufferedImage puzzleImage = new BufferedImage(PUZZLE_WIDTH, PUZZLE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = puzzleImage.createGraphics();
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 创建拼图形状（简化的拼图块形状）
        Shape puzzleShape = createPuzzleShape(PUZZLE_WIDTH / 2, PUZZLE_HEIGHT / 2);
        g2d.setClip(puzzleShape);
        
        // 从背景图中提取拼图区域
        int sourceX = position.getX() - PUZZLE_WIDTH / 2;
        int sourceY = position.getY() - PUZZLE_HEIGHT / 2;
        
        g2d.drawImage(backgroundImage, 
            0, 0, PUZZLE_WIDTH, PUZZLE_HEIGHT,
            sourceX, sourceY, sourceX + PUZZLE_WIDTH, sourceY + PUZZLE_HEIGHT,
            null);
        
        // 在背景图上挖出拼图形状（创建缺口）
        Graphics2D bgG2d = backgroundImage.createGraphics();
        bgG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bgG2d.translate(sourceX, sourceY);
        bgG2d.setComposite(AlphaComposite.Clear);
        bgG2d.fill(puzzleShape);
        
        // 添加缺口边框
        bgG2d.setComposite(AlphaComposite.SrcOver);
        bgG2d.setColor(new Color(0, 0, 0, 100));
        bgG2d.setStroke(new BasicStroke(2));
        bgG2d.draw(puzzleShape);
        
        bgG2d.dispose();
        g2d.dispose();
        
        return puzzleImage;
    }
    
    /**
     * 创建拼图形状
     */
    private Shape createPuzzleShape(int centerX, int centerY) {
        // 简化的拼图块形状（圆形）
        int radius = Math.min(PUZZLE_WIDTH, PUZZLE_HEIGHT) / 3;
        return new java.awt.geom.Ellipse2D.Double(
            centerX - radius, centerY - radius, 
            radius * 2, radius * 2
        );
    }
    
    /**
     * 绘制随机装饰元素
     */
    private void drawRandomDecorations(Graphics2D g2d) {
        // 绘制一些随机的圆点和线条作为装饰
        for (int i = 0; i < 20; i++) {
            g2d.setColor(getRandomColor(50, 150));
            int x = random.nextInt(IMAGE_WIDTH);
            int y = random.nextInt(IMAGE_HEIGHT);
            int size = random.nextInt(10) + 5;
            g2d.fillOval(x, y, size, size);
        }
        
        // 绘制一些随机线条
        for (int i = 0; i < 10; i++) {
            g2d.setColor(getRandomColor(100, 180));
            g2d.setStroke(new BasicStroke(random.nextFloat() * 3 + 1));
            int x1 = random.nextInt(IMAGE_WIDTH);
            int y1 = random.nextInt(IMAGE_HEIGHT);
            int x2 = random.nextInt(IMAGE_WIDTH);
            int y2 = random.nextInt(IMAGE_HEIGHT);
            g2d.drawLine(x1, y1, x2, y2);
        }
    }
    
    /**
     * 获取随机颜色
     */
    private Color getRandomColor(int minBrightness, int maxBrightness) {
        int range = maxBrightness - minBrightness;
        int r = random.nextInt(range) + minBrightness;
        int g = random.nextInt(range) + minBrightness;
        int b = random.nextInt(range) + minBrightness;
        return new Color(r, g, b);
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