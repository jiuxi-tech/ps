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
 * 拼接验证码生成器
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Component
public class ConcatCaptchaGenerator implements CaptchaGenerator {
    
    private static final int IMAGE_WIDTH = 300;
    private static final int IMAGE_HEIGHT = 150;
    private static final int PIECE_WIDTH = 60;
    private static final int PIECE_HEIGHT = 60;
    
    private final Random random = new Random();
    
    @Override
    public CaptchaChallenge generateChallenge(CaptchaType captchaType) {
        if (!supports(captchaType)) {
            throw new IllegalArgumentException("不支持的验证码类型: " + captchaType);
        }
        
        CaptchaChallenge challenge = new CaptchaChallenge(captchaType);
        
        try {
            // 生成完整图片
            BufferedImage completeImage = generateCompleteImage();
            
            // 选择要抽取的片段位置
            CaptchaCoordinate piecePosition = generatePiecePosition();
            challenge.setCorrectPosition(piecePosition);
            
            // 生成有缺失的背景图片和片段图片
            BufferedImage backgroundImage = createImageWithMissingPiece(completeImage, piecePosition);
            BufferedImage pieceImage = extractPiece(completeImage, piecePosition);
            
            // 转换为Base64格式
            challenge.setBackgroundImageData(imageToBase64(backgroundImage));
            challenge.setPuzzleImageData(imageToBase64(pieceImage));
            
            // 设置元数据
            challenge.setMetadata("imageWidth", IMAGE_WIDTH);
            challenge.setMetadata("imageHeight", IMAGE_HEIGHT);
            challenge.setMetadata("pieceWidth", PIECE_WIDTH);
            challenge.setMetadata("pieceHeight", PIECE_HEIGHT);
            
        } catch (Exception e) {
            throw new RuntimeException("生成拼接验证码失败", e);
        }
        
        return challenge;
    }
    
    @Override
    public boolean supports(CaptchaType captchaType) {
        return captchaType == CaptchaType.CONCAT;
    }
    
    @Override
    public String getGeneratorName() {
        return "ConcatCaptchaGenerator";
    }
    
    /**
     * 生成完整图片
     */
    private BufferedImage generateCompleteImage() {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 生成渐变背景
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(135, 206, 250),
            IMAGE_WIDTH, IMAGE_HEIGHT, new Color(70, 130, 180)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        
        // 添加一些几何图形
        drawRandomShapes(g2d);
        
        // 添加一些文字
        drawRandomText(g2d);
        
        g2d.dispose();
        return image;
    }
    
    /**
     * 绘制随机图形
     */
    private void drawRandomShapes(Graphics2D g2d) {
        // 绘制一些圆形
        for (int i = 0; i < 8; i++) {
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 100));
            int x = random.nextInt(IMAGE_WIDTH - 50);
            int y = random.nextInt(IMAGE_HEIGHT - 50);
            int size = random.nextInt(30) + 10;
            g2d.fillOval(x, y, size, size);
        }
        
        // 绘制一些矩形
        for (int i = 0; i < 5; i++) {
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 80));
            int x = random.nextInt(IMAGE_WIDTH - 40);
            int y = random.nextInt(IMAGE_HEIGHT - 40);
            int width = random.nextInt(30) + 15;
            int height = random.nextInt(30) + 15;
            g2d.fillRect(x, y, width, height);
        }
    }
    
    /**
     * 绘制随机文字
     */
    private void drawRandomText(Graphics2D g2d) {
        String[] words = {"CLICK", "DRAG", "MOVE", "FIND", "MATCH"};
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        
        for (int i = 0; i < 3; i++) {
            g2d.setColor(new Color(random.nextInt(100), random.nextInt(100), random.nextInt(100)));
            String word = words[random.nextInt(words.length)];
            int x = random.nextInt(IMAGE_WIDTH - 100);
            int y = random.nextInt(IMAGE_HEIGHT - 30) + 30;
            g2d.drawString(word, x, y);
        }
    }
    
    /**
     * 生成片段位置
     */
    private CaptchaCoordinate generatePiecePosition() {
        int x = random.nextInt(IMAGE_WIDTH - PIECE_WIDTH);
        int y = random.nextInt(IMAGE_HEIGHT - PIECE_HEIGHT);
        return new CaptchaCoordinate(x, y);
    }
    
    /**
     * 创建有缺失片段的图片
     */
    private BufferedImage createImageWithMissingPiece(BufferedImage original, CaptchaCoordinate position) {
        BufferedImage result = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = result.createGraphics();
        
        // 复制原图
        g2d.drawImage(original, 0, 0, null);
        
        // 挖掉一块（用白色填充）
        g2d.setColor(Color.WHITE);
        g2d.fillRect(position.getX(), position.getY(), PIECE_WIDTH, PIECE_HEIGHT);
        
        // 添加边框
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(position.getX(), position.getY(), PIECE_WIDTH, PIECE_HEIGHT);
        
        g2d.dispose();
        return result;
    }
    
    /**
     * 提取片段图片
     */
    private BufferedImage extractPiece(BufferedImage original, CaptchaCoordinate position) {
        BufferedImage piece = new BufferedImage(PIECE_WIDTH, PIECE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = piece.createGraphics();
        
        // 提取指定区域
        g2d.drawImage(original,
            0, 0, PIECE_WIDTH, PIECE_HEIGHT,
            position.getX(), position.getY(),
            position.getX() + PIECE_WIDTH, position.getY() + PIECE_HEIGHT,
            null);
        
        // 添加边框
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(0, 0, PIECE_WIDTH - 1, PIECE_HEIGHT - 1);
        
        g2d.dispose();
        return piece;
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