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
    
    // 难度级别相关常量
    private static final int DIFFICULTY_EASY = 1;
    private static final int DIFFICULTY_MEDIUM = 2;
    private static final int DIFFICULTY_HARD = 3;
    
    private final Random random = new Random();
    
    // 注入验证码配置
    private final CaptchaProperties captchaProperties;
    
    @Autowired
    public ConcatCaptchaGenerator(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }
    
    // 图像主题类型枚举
    private enum ImageThemeType {
        NATURE, GEOMETRIC, ABSTRACT, COLORFUL
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
            ImageThemeType themeType = selectThemeType(difficulty);
            
            // 生成完整图片
            BufferedImage completeImage = generateCompleteImage(themeType, difficulty);
            
            // 根据难度选择要抽取的片段位置
            CaptchaCoordinate piecePosition = generatePiecePosition(difficulty);
            challenge.setCorrectPosition(piecePosition);
            
            // 根据难度调整容差
            double tolerance = calculateToleranceByDifficulty(difficulty);
            challenge.setTolerance(tolerance);
            
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
            challenge.setMetadata("difficulty", difficulty);
            challenge.setMetadata("themeType", themeType.name());
            challenge.setMetadata("tolerance", tolerance);
            
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
     * 生成完整图片（保持向后兼容）
     */
    private BufferedImage generateCompleteImage() {
        return generateCompleteImage(ImageThemeType.NATURE, DIFFICULTY_MEDIUM);
    }
    
    /**
     * 根据主题类型和难度生成完整图片
     */
    private BufferedImage generateCompleteImage(ImageThemeType themeType, int difficulty) {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 根据主题类型绘制背景
        drawThemedBackground(g2d, themeType);
        
        // 根据难度添加装饰元素
        drawThemedDecorations(g2d, themeType, difficulty);
        
        g2d.dispose();
        return image;
    }
    
    /**
     * 根据主题类型绘制背景
     */
    private void drawThemedBackground(Graphics2D g2d, ImageThemeType themeType) {
        switch (themeType) {
            case NATURE:
                drawNatureBackground(g2d);
                break;
            case GEOMETRIC:
                drawGeometricBackground(g2d);
                break;
            case ABSTRACT:
                drawAbstractBackground(g2d);
                break;
            case COLORFUL:
                drawColorfulBackground(g2d);
                break;
        }
    }
    
    /**
     * 绘制自然主题背景
     */
    private void drawNatureBackground(Graphics2D g2d) {
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(135, 206, 250), // 天空蓝
            IMAGE_WIDTH, IMAGE_HEIGHT, new Color(34, 139, 34) // 森林绿
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }
    
    /**
     * 绘制几何主题背景
     */
    private void drawGeometricBackground(Graphics2D g2d) {
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(70, 130, 180),
            IMAGE_WIDTH, IMAGE_HEIGHT, new Color(25, 25, 112)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }
    
    /**
     * 绘制抽象主题背景
     */
    private void drawAbstractBackground(Graphics2D g2d) {
        // 多色渐变背景
        for (int i = 0; i < IMAGE_HEIGHT; i += 20) {
            Color color1 = new Color(
                100 + random.nextInt(155),
                100 + random.nextInt(155),
                100 + random.nextInt(155)
            );
            Color color2 = new Color(
                50 + random.nextInt(155),
                50 + random.nextInt(155),
                50 + random.nextInt(155)
            );
            GradientPaint gradient = new GradientPaint(0, i, color1, IMAGE_WIDTH, i + 20, color2);
            g2d.setPaint(gradient);
            g2d.fillRect(0, i, IMAGE_WIDTH, 20);
        }
    }
    
    /**
     * 绘制彩色主题背景
     */
    private void drawColorfulBackground(Graphics2D g2d) {
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(255, 182, 193), // 浅粉色
            IMAGE_WIDTH, IMAGE_HEIGHT, new Color(255, 165, 0) // 橙色
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }
    
    /**
     * 根据主题类型和难度绘制装饰元素
     */
    private void drawThemedDecorations(Graphics2D g2d, ImageThemeType themeType, int difficulty) {
        switch (themeType) {
            case NATURE:
                drawNatureDecorations(g2d, difficulty);
                break;
            case GEOMETRIC:
                drawGeometricDecorations(g2d, difficulty);
                break;
            case ABSTRACT:
                drawAbstractDecorations(g2d, difficulty);
                break;
            case COLORFUL:
                drawColorfulDecorations(g2d, difficulty);
                break;
        }
    }
    
    /**
     * 绘制自然主题装饰
     */
    private void drawNatureDecorations(Graphics2D g2d, int difficulty) {
        int elementCount = difficulty == DIFFICULTY_EASY ? 8 : (difficulty == DIFFICULTY_MEDIUM ? 12 : 20);
        
        // 绘制叶子形状
        for (int i = 0; i < elementCount / 2; i++) {
            g2d.setColor(new Color(34, 139 + random.nextInt(50), 34, 150));
            int x = random.nextInt(IMAGE_WIDTH - 20);
            int y = random.nextInt(IMAGE_HEIGHT - 20);
            g2d.fillOval(x, y, 15 + random.nextInt(10), 8 + random.nextInt(6));
        }
        
        // 绘制云朵形状
        for (int i = 0; i < elementCount / 2; i++) {
            g2d.setColor(new Color(255, 255, 255, 120));
            int x = random.nextInt(IMAGE_WIDTH - 30);
            int y = random.nextInt(IMAGE_HEIGHT / 2);
            g2d.fillOval(x, y, 25 + random.nextInt(15), 15 + random.nextInt(10));
        }
    }
    
    /**
     * 绘制几何主题装饰
     */
    private void drawGeometricDecorations(Graphics2D g2d, int difficulty) {
        int elementCount = difficulty == DIFFICULTY_EASY ? 6 : (difficulty == DIFFICULTY_MEDIUM ? 10 : 15);
        
        // 绘制几何图形
        for (int i = 0; i < elementCount; i++) {
            g2d.setColor(new Color(random.nextInt(156) + 100, random.nextInt(156) + 100, 
                                  random.nextInt(156) + 100, 120));
            int x = random.nextInt(IMAGE_WIDTH - 40);
            int y = random.nextInt(IMAGE_HEIGHT - 40);
            int size = 15 + random.nextInt(25);
            
            if (random.nextBoolean()) {
                g2d.fillRect(x, y, size, size);
            } else {
                g2d.fillOval(x, y, size, size);
            }
        }
        
        drawRandomText(g2d);
    }
    
    /**
     * 绘制抽象主题装饰
     */
    private void drawAbstractDecorations(Graphics2D g2d, int difficulty) {
        int elementCount = difficulty == DIFFICULTY_EASY ? 10 : (difficulty == DIFFICULTY_MEDIUM ? 15 : 25);
        
        // 绘制抽象线条和形状
        for (int i = 0; i < elementCount; i++) {
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), 
                                  random.nextInt(256), 80 + random.nextInt(120)));
            g2d.setStroke(new BasicStroke(1 + random.nextFloat() * 3));
            
            if (random.nextBoolean()) {
                // 绘制曲线
                int x1 = random.nextInt(IMAGE_WIDTH);
                int y1 = random.nextInt(IMAGE_HEIGHT);
                int x2 = random.nextInt(IMAGE_WIDTH);
                int y2 = random.nextInt(IMAGE_HEIGHT);
                int ctrlX = random.nextInt(IMAGE_WIDTH);
                int ctrlY = random.nextInt(IMAGE_HEIGHT);
                
                java.awt.geom.QuadCurve2D curve = new java.awt.geom.QuadCurve2D.Float(
                    x1, y1, ctrlX, ctrlY, x2, y2);
                g2d.draw(curve);
            } else {
                // 绘制不规则形状
                drawRandomShapes(g2d);
            }
        }
    }
    
    /**
     * 绘制彩色主题装饰
     */
    private void drawColorfulDecorations(Graphics2D g2d, int difficulty) {
        int elementCount = difficulty == DIFFICULTY_EASY ? 12 : (difficulty == DIFFICULTY_MEDIUM ? 18 : 30);
        
        // 绘制彩色圆点和星形
        for (int i = 0; i < elementCount; i++) {
            Color brightColor = new Color(
                150 + random.nextInt(106),
                150 + random.nextInt(106),
                150 + random.nextInt(106),
                100 + random.nextInt(100)
            );
            g2d.setColor(brightColor);
            
            int x = random.nextInt(IMAGE_WIDTH - 20);
            int y = random.nextInt(IMAGE_HEIGHT - 20);
            int size = 8 + random.nextInt(15);
            
            if (random.nextInt(3) == 0) {
                // 绘制星形
                drawStar(g2d, x + size/2, y + size/2, size/2, size/4);
            } else {
                g2d.fillOval(x, y, size, size);
            }
        }
        
        drawRandomText(g2d);
    }
    
    /**
     * 绘制星形
     */
    private void drawStar(Graphics2D g2d, int centerX, int centerY, int outerRadius, int innerRadius) {
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];
        
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI * i / 5 - Math.PI / 2;
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
            xPoints[i] = centerX + (int) (Math.cos(angle) * radius);
            yPoints[i] = centerY + (int) (Math.sin(angle) * radius);
        }
        
        g2d.fillPolygon(xPoints, yPoints, 10);
    }
    
    /**
     * 绘制随机图形（保持向后兼容）
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
     * 根据难度选择主题类型
     */
    private ImageThemeType selectThemeType(int difficulty) {
        switch (difficulty) {
            case DIFFICULTY_EASY:
                return random.nextBoolean() ? ImageThemeType.NATURE : ImageThemeType.GEOMETRIC;
            case DIFFICULTY_MEDIUM:
                ImageThemeType[] mediumTypes = {ImageThemeType.NATURE, ImageThemeType.GEOMETRIC, ImageThemeType.COLORFUL};
                return mediumTypes[random.nextInt(mediumTypes.length)];
            case DIFFICULTY_HARD:
                ImageThemeType[] hardTypes = {ImageThemeType.ABSTRACT, ImageThemeType.COLORFUL};
                return hardTypes[random.nextInt(hardTypes.length)];
            default:
                return ImageThemeType.NATURE;
        }
    }
    
    /**
     * 根据难度计算容差
     */
    private double calculateToleranceByDifficulty(int difficulty) {
        switch (difficulty) {
            case DIFFICULTY_EASY:
                return 12.0; // 更宽松的容差
            case DIFFICULTY_MEDIUM:
                return 10.0; // 标准容差
            case DIFFICULTY_HARD:
                return 6.0; // 更严格的容差
            default:
                return 10.0;
        }
    }
    
    /**
     * 生成片段位置
     */
    private CaptchaCoordinate generatePiecePosition() {
        return generatePiecePosition(DIFFICULTY_MEDIUM);
    }
    
    /**
     * 根据难度生成片段位置
     */
    private CaptchaCoordinate generatePiecePosition(int difficulty) {
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