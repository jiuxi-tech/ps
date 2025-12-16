package com.jiuxi.platform.captcha.infrastructure.generator;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;
import com.jiuxi.platform.captcha.domain.service.CaptchaGenerator;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaCoordinate;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaType;
import com.jiuxi.platform.captcha.infrastructure.config.CaptchaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import java.awt.geom.Path2D;

/**
 * 滑块验证码生成器
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Component
public class SliderCaptchaGenerator implements CaptchaGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(SliderCaptchaGenerator.class);
    
    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 160;
    private static final int PUZZLE_WIDTH = 60;
    private static final int PUZZLE_HEIGHT = 60;
    
    // 难度级别相关常量
    private static final int DIFFICULTY_EASY = 1;
    private static final int DIFFICULTY_MEDIUM = 2;
    private static final int DIFFICULTY_HARD = 3;
    
    private final Random random = new Random();
    
    // 注入验证码配置
    private final CaptchaProperties captchaProperties;
    
    @Autowired
    public SliderCaptchaGenerator(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }
    
    // 拼图形状类型枚举
    private enum PuzzleShapeType {
        CLASSIC, ROUNDED, COMPLEX, IRREGULAR
    }
    
    @Override
    public CaptchaChallenge generateChallenge(CaptchaType captchaType) {
        logger.info("开始生成滑块验证码，类型: {}", captchaType);
        
        if (!supports(captchaType)) {
            throw new IllegalArgumentException("不支持的验证码类型: " + captchaType);
        }
        
        CaptchaChallenge challenge = new CaptchaChallenge(captchaType);
        // 从配置中读取最大尝试次数
        challenge.setMaxAttempts(captchaProperties.getMaxAttempts());
        
        try {
            // 根据时间随机生成难度级别
            int difficulty = generateRandomDifficulty();
            PuzzleShapeType shapeType = selectShapeType(difficulty);
            
            // 生成随机拼图位置
            CaptchaCoordinate puzzlePosition = generatePuzzlePosition(difficulty);
            challenge.setCorrectPosition(puzzlePosition);
            
            // 根据难度调整容差
            double tolerance = calculateToleranceByDifficulty(difficulty);
            challenge.setTolerance(tolerance);
            
            // 生成背景图片和拼图片
            BufferedImage backgroundImage = generateBackgroundImage(difficulty);
            BufferedImage puzzleImage = generatePuzzleImage(backgroundImage, puzzlePosition, shapeType);
            
            // 转换为Base64格式
            String backgroundImageData = imageToBase64(backgroundImage);
            String puzzleImageData = imageToBase64(puzzleImage);
            
            logger.info("背景图片数据长度: {}", backgroundImageData != null ? backgroundImageData.length() : "null");
            logger.info("拼图图片数据长度: {}", puzzleImageData != null ? puzzleImageData.length() : "null");
            
            challenge.setBackgroundImageData(backgroundImageData);
            challenge.setPuzzleImageData(puzzleImageData);
            
            // 设置元数据
            challenge.setMetadata("imageWidth", IMAGE_WIDTH);
            challenge.setMetadata("imageHeight", IMAGE_HEIGHT);
            challenge.setMetadata("puzzleWidth", PUZZLE_WIDTH);
            challenge.setMetadata("puzzleHeight", PUZZLE_HEIGHT);
            challenge.setMetadata("difficulty", difficulty);
            challenge.setMetadata("shapeType", shapeType.name());
            challenge.setMetadata("tolerance", tolerance);
            
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
     * 根据难度选择形状类型
     */
    private PuzzleShapeType selectShapeType(int difficulty) {
        switch (difficulty) {
            case DIFFICULTY_EASY:
                return random.nextBoolean() ? PuzzleShapeType.CLASSIC : PuzzleShapeType.ROUNDED;
            case DIFFICULTY_MEDIUM:
                return random.nextBoolean() ? PuzzleShapeType.ROUNDED : PuzzleShapeType.COMPLEX;
            case DIFFICULTY_HARD:
                return random.nextBoolean() ? PuzzleShapeType.COMPLEX : PuzzleShapeType.IRREGULAR;
            default:
                return PuzzleShapeType.CLASSIC;
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
                return 8.0; // 标准容差
            case DIFFICULTY_HARD:
                return 5.0; // 更严格的容差
            default:
                return 8.0;
        }
    }
    
    /**
     * 生成拼图位置
     */
    private CaptchaCoordinate generatePuzzlePosition() {
        return generatePuzzlePosition(DIFFICULTY_MEDIUM);
    }
    
    /**
     * 根据难度生成拼图位置
     */
    private CaptchaCoordinate generatePuzzlePosition(int difficulty) {
        // 根据难度调整边界限制
        int margin = difficulty == DIFFICULTY_HARD ? 15 : 20;
        int minX = PUZZLE_WIDTH / 2 + margin;
        int maxX = IMAGE_WIDTH - PUZZLE_WIDTH / 2 - margin;
        int minY = PUZZLE_HEIGHT / 2 + margin;
        int maxY = IMAGE_HEIGHT - PUZZLE_HEIGHT / 2 - margin;
        
        // 困难模式下增加一些位置限制
        if (difficulty == DIFFICULTY_HARD) {
            // 避免太靠近中心位置，增加挑战
            int centerX = IMAGE_WIDTH / 2;
            int centerY = IMAGE_HEIGHT / 2;
            int x, y;
            do {
                x = random.nextInt(maxX - minX + 1) + minX;
                y = random.nextInt(maxY - minY + 1) + minY;
            } while (Math.abs(x - centerX) < PUZZLE_WIDTH && Math.abs(y - centerY) < PUZZLE_HEIGHT);
            return new CaptchaCoordinate(x, y);
        } else {
            int x = random.nextInt(maxX - minX + 1) + minX;
            int y = random.nextInt(maxY - minY + 1) + minY;
            return new CaptchaCoordinate(x, y);
        }
    }
    
    /**
     * 生成背景图片
     */
    private BufferedImage generateBackgroundImage() {
        return generateBackgroundImage(DIFFICULTY_MEDIUM);
    }
    
    /**
     * 根据难度生成背景图片
     */
    private BufferedImage generateBackgroundImage(int difficulty) {
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
        
        // 根据难度添加不同程度的装饰元素
        drawRandomDecorations(g2d, difficulty);
        
        g2d.dispose();
        return image;
    }
    
    /**
     * 生成拼图片（从背景图中抠出的部分）
     */
    private BufferedImage generatePuzzleImage(BufferedImage backgroundImage, CaptchaCoordinate position) {
        return generatePuzzleImage(backgroundImage, position, PuzzleShapeType.CLASSIC);
    }
    
    /**
     * 根据形状类型生成拼图片
     */
    private BufferedImage generatePuzzleImage(BufferedImage backgroundImage, CaptchaCoordinate position, PuzzleShapeType shapeType) {
        BufferedImage puzzleImage = new BufferedImage(PUZZLE_WIDTH, PUZZLE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = puzzleImage.createGraphics();
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 根据形状类型创建拼图形状
        Shape puzzleShape = createPuzzleShape(PUZZLE_WIDTH / 2, PUZZLE_HEIGHT / 2, shapeType);
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
        return createPuzzleShape(centerX, centerY, PuzzleShapeType.CLASSIC);
    }
    
    /**
     * 根据类型创建拼图形状
     */
    private Shape createPuzzleShape(int centerX, int centerY, PuzzleShapeType shapeType) {
        switch (shapeType) {
            case CLASSIC:
                return createClassicPuzzleShape(centerX, centerY);
            case ROUNDED:
                return createRoundedPuzzleShape(centerX, centerY);
            case COMPLEX:
                return createComplexPuzzleShape(centerX, centerY);
            case IRREGULAR:
                return createIrregularPuzzleShape(centerX, centerY);
            default:
                return createClassicPuzzleShape(centerX, centerY);
        }
    }
    
    /**
     * 创建经典拼图形状
     */
    private Shape createClassicPuzzleShape(int centerX, int centerY) {
        Path2D path = new Path2D.Double();
        int size = Math.min(PUZZLE_WIDTH, PUZZLE_HEIGHT) / 3;
        
        // 创建基本的拼图块形状（带凸起和凹陷）
        path.moveTo(centerX - size, centerY - size);
        path.lineTo(centerX + size, centerY - size);
        
        // 右边凸起
        path.lineTo(centerX + size, centerY - size/3);
        path.curveTo(centerX + size + size/2, centerY - size/3, 
                    centerX + size + size/2, centerY + size/3, 
                    centerX + size, centerY + size/3);
        path.lineTo(centerX + size, centerY + size);
        
        // 底部凹陷
        path.lineTo(centerX + size/3, centerY + size);
        path.curveTo(centerX + size/3, centerY + size + size/2, 
                    centerX - size/3, centerY + size + size/2, 
                    centerX - size/3, centerY + size);
        path.lineTo(centerX - size, centerY + size);
        path.lineTo(centerX - size, centerY - size);
        path.closePath();
        
        return path;
    }
    
    /**
     * 创建圆润拼图形状
     */
    private Shape createRoundedPuzzleShape(int centerX, int centerY) {
        int radius = Math.min(PUZZLE_WIDTH, PUZZLE_HEIGHT) / 3;
        return new java.awt.geom.Ellipse2D.Double(
            centerX - radius, centerY - radius, 
            radius * 2, radius * 2
        );
    }
    
    /**
     * 创建复杂拼图形状
     */
    private Shape createComplexPuzzleShape(int centerX, int centerY) {
        Path2D path = new Path2D.Double();
        int size = Math.min(PUZZLE_WIDTH, PUZZLE_HEIGHT) / 3;
        
        // 创建更复杂的拼图形状（多个凸起凹陷）
        path.moveTo(centerX - size, centerY - size);
        
        // 顶部凸起
        path.lineTo(centerX - size/3, centerY - size);
        path.curveTo(centerX - size/3, centerY - size - size/2, 
                    centerX + size/3, centerY - size - size/2, 
                    centerX + size/3, centerY - size);
        path.lineTo(centerX + size, centerY - size);
        
        // 右边凸起
        path.lineTo(centerX + size, centerY - size/3);
        path.curveTo(centerX + size + size/2, centerY - size/3, 
                    centerX + size + size/2, centerY + size/3, 
                    centerX + size, centerY + size/3);
        path.lineTo(centerX + size, centerY + size);
        
        // 底部凹陷
        path.lineTo(centerX + size/3, centerY + size);
        path.curveTo(centerX + size/3, centerY + size + size/2, 
                    centerX - size/3, centerY + size + size/2, 
                    centerX - size/3, centerY + size);
        path.lineTo(centerX - size, centerY + size);
        
        // 左边凹陷
        path.lineTo(centerX - size, centerY + size/3);
        path.curveTo(centerX - size - size/2, centerY + size/3, 
                    centerX - size - size/2, centerY - size/3, 
                    centerX - size, centerY - size/3);
        path.lineTo(centerX - size, centerY - size);
        path.closePath();
        
        return path;
    }
    
    /**
     * 创建不规则拼图形状
     */
    private Shape createIrregularPuzzleShape(int centerX, int centerY) {
        Path2D path = new Path2D.Double();
        int size = Math.min(PUZZLE_WIDTH, PUZZLE_HEIGHT) / 3;
        
        // 创建随机的不规则形状
        double angleStep = Math.PI * 2 / 8;
        double angle = 0;
        
        for (int i = 0; i < 8; i++) {
            double radius = size * (0.7 + random.nextDouble() * 0.6);
            double x = centerX + Math.cos(angle) * radius;
            double y = centerY + Math.sin(angle) * radius;
            
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            angle += angleStep;
        }
        path.closePath();
        
        return path;
    }
    
    /**
     * 绘制随机装饰元素
     */
    private void drawRandomDecorations(Graphics2D g2d) {
        drawRandomDecorations(g2d, DIFFICULTY_MEDIUM);
    }
    
    /**
     * 根据难度绘制随机装饰元素
     */
    private void drawRandomDecorations(Graphics2D g2d, int difficulty) {
        // 根据难度调整装饰元素的数量和复杂度
        int baseCircleCount, baseLineCount;
        int maxSize, minAlpha, maxAlpha;
        
        switch (difficulty) {
            case DIFFICULTY_EASY:
                baseCircleCount = 15;
                baseLineCount = 8;
                maxSize = 12;
                minAlpha = 80;
                maxAlpha = 120;
                break;
            case DIFFICULTY_MEDIUM:
                baseCircleCount = 20;
                baseLineCount = 10;
                maxSize = 10;
                minAlpha = 50;
                maxAlpha = 150;
                break;
            case DIFFICULTY_HARD:
                baseCircleCount = 30;
                baseLineCount = 15;
                maxSize = 8;
                minAlpha = 30;
                maxAlpha = 180;
                break;
            default:
                baseCircleCount = 20;
                baseLineCount = 10;
                maxSize = 10;
                minAlpha = 50;
                maxAlpha = 150;
                break;
        }
        
        // 绘制随机圆点
        for (int i = 0; i < baseCircleCount; i++) {
            Color color = getRandomColor(50, 150);
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 
                                  random.nextInt(maxAlpha - minAlpha + 1) + minAlpha));
            int x = random.nextInt(IMAGE_WIDTH);
            int y = random.nextInt(IMAGE_HEIGHT);
            int size = random.nextInt(maxSize) + 3;
            g2d.fillOval(x, y, size, size);
        }
        
        // 绘制随机线条
        for (int i = 0; i < baseLineCount; i++) {
            Color color = getRandomColor(100, 180);
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(),
                                  random.nextInt(maxAlpha - minAlpha + 1) + minAlpha));
            g2d.setStroke(new BasicStroke(random.nextFloat() * 2 + 0.5f));
            int x1 = random.nextInt(IMAGE_WIDTH);
            int y1 = random.nextInt(IMAGE_HEIGHT);
            int x2 = random.nextInt(IMAGE_WIDTH);
            int y2 = random.nextInt(IMAGE_HEIGHT);
            g2d.drawLine(x1, y1, x2, y2);
        }
        
        // 困难模式下添加额外的干扰元素
        if (difficulty == DIFFICULTY_HARD) {
            // 添加一些曲线干扰
            for (int i = 0; i < 5; i++) {
                g2d.setColor(getRandomColor(80, 160));
                g2d.setStroke(new BasicStroke(random.nextFloat() * 2 + 1));
                
                int x1 = random.nextInt(IMAGE_WIDTH);
                int y1 = random.nextInt(IMAGE_HEIGHT);
                int x2 = random.nextInt(IMAGE_WIDTH);
                int y2 = random.nextInt(IMAGE_HEIGHT);
                int ctrlX = random.nextInt(IMAGE_WIDTH);
                int ctrlY = random.nextInt(IMAGE_HEIGHT);
                
                java.awt.geom.QuadCurve2D curve = new java.awt.geom.QuadCurve2D.Float(
                    x1, y1, ctrlX, ctrlY, x2, y2);
                g2d.draw(curve);
            }
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