package com.jiuxi.platform.captcha.infrastructure.generator;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;
import com.jiuxi.platform.captcha.domain.service.CaptchaGenerator;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaCoordinate;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于资源目录图片的滑块验证码生成器
 * 从META-INF/cut-image/slider目录加载背景图片和滑块图片
 *
 * @author Custom Implementation
 * @date 2025-09-14
 */
@Component
public class ResourceSliderCaptchaGenerator implements CaptchaGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ResourceSliderCaptchaGenerator.class);

    // 根据实际图片尺寸调整这些常量
    private static final int IMAGE_WIDTH = 590;
    private static final int IMAGE_HEIGHT = 360;
    private static final int PUZZLE_WIDTH = 60;
    private static final int PUZZLE_HEIGHT = 60;

    private final Random random = new Random();

    // 图片资源路径
    private static final String BACKGROUND_IMAGE_DIR = "META-INF/cut-image/slider";
    private static final String BACKGROUND_IMAGE_SUFFIX = "bg.jpg";
    private static final String SLIDER_IMAGE_SUFFIX = ".png";

    @Override
    public CaptchaChallenge generateChallenge(CaptchaType captchaType) {
        if (!supports(captchaType)) {
            throw new IllegalArgumentException("不支持的验证码类型: " + captchaType);
        }

        CaptchaChallenge challenge = new CaptchaChallenge(captchaType);

        try {
            // 获取可用的图片资源
            List<String> availableImages = getAvailableImages();
            if (availableImages.isEmpty()) {
                logger.warn("未找到可用的验证码图片资源");
                throw new RuntimeException("未找到可用的验证码图片资源");
            }

            // 随机选择一个图片
            String selectedImagePrefix = availableImages.get(random.nextInt(availableImages.size()));
            logger.info("=== 验证码图片选择信息 ===");
            logger.info("可用图片列表: {}", availableImages);
            logger.info("选择的图片前缀: {}", selectedImagePrefix);
            logger.info("背景图片文件: {}{}", selectedImagePrefix, BACKGROUND_IMAGE_SUFFIX);
            logger.info("滑块图片文件: {}{}", selectedImagePrefix, SLIDER_IMAGE_SUFFIX);

            // 加载背景图片
            BufferedImage backgroundImage = loadBackgroundImage(selectedImagePrefix);
            if (backgroundImage == null) {
                throw new RuntimeException("无法加载背景图片: " + selectedImagePrefix);
            }

            // 加载滑块图片
            BufferedImage sliderImage = loadSliderImage(selectedImagePrefix);
            if (sliderImage == null) {
                throw new RuntimeException("无法加载滑块图片: " + selectedImagePrefix);
            }

            // 读取坐标文件获取正确位置
            CaptchaCoordinate correctPosition = readCoordinateFromFile(selectedImagePrefix);
            logger.info("=== 坐标信息 ===");
            if (correctPosition != null) {
                logger.info("从坐标文件读取到的坐标: ({}, {})", correctPosition.getX(), correctPosition.getY());
            } else {
                logger.info("未找到坐标文件，将使用随机坐标");
            }
            
            if (correctPosition == null) {
                // 如果没有坐标文件，随机生成位置
                correctPosition = generateRandomPosition();
                logger.info("随机生成的坐标: ({}, {})", correctPosition.getX(), correctPosition.getY());
            }

            // 设置验证码数据
            challenge.setCorrectPosition(correctPosition);
            challenge.setTolerance(8.0); // 设置容差

            // 转换为Base64格式
            challenge.setBackgroundImageData(imageToBase64(backgroundImage));
            challenge.setPuzzleImageData(imageToBase64(sliderImage));

            // 设置元数据
            challenge.setMetadata("imageWidth", IMAGE_WIDTH);
            challenge.setMetadata("imageHeight", IMAGE_HEIGHT);
            challenge.setMetadata("puzzleWidth", PUZZLE_WIDTH);
            challenge.setMetadata("puzzleHeight", PUZZLE_HEIGHT);

            logger.debug("成功生成基于资源的滑块验证码: {}", selectedImagePrefix);

        } catch (Exception e) {
            logger.error("生成基于资源的滑块验证码失败", e);
            throw new RuntimeException("生成验证码失败: " + e.getMessage(), e);
        }

        return challenge;
    }

    @Override
    public boolean supports(CaptchaType captchaType) {
        return captchaType == CaptchaType.SLIDER;
    }

    @Override
    public String getGeneratorName() {
        return "ResourceSliderCaptchaGenerator";
    }

    /**
     * 获取可用的图片资源列表
     */
    private List<String> getAvailableImages() {
        List<String> images = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource(BACKGROUND_IMAGE_DIR);
            File dir = resource.getFile();
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles((d, name) -> name.endsWith(BACKGROUND_IMAGE_SUFFIX));
                if (files != null) {
                    for (File file : files) {
                        String name = file.getName();
                        String prefix = name.substring(0, name.indexOf(BACKGROUND_IMAGE_SUFFIX));
                        images.add(prefix);
                    }
                }
            }
        } catch (IOException e) {
            logger.warn("无法访问图片资源目录: {}", BACKGROUND_IMAGE_DIR, e);
        }
        return images;
    }

    /**
     * 加载背景图片
     */
    private BufferedImage loadBackgroundImage(String imagePrefix) {
        try {
            String imagePath = BACKGROUND_IMAGE_DIR + "/" + imagePrefix + BACKGROUND_IMAGE_SUFFIX;
            ClassPathResource resource = new ClassPathResource(imagePath);
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    BufferedImage image = ImageIO.read(is);
                    if (image != null) {
                        logger.debug("背景图片 {} 尺寸: {}x{}", imagePrefix, image.getWidth(), image.getHeight());
                    }
                    return image;
                }
            }
        } catch (IOException e) {
            logger.error("加载背景图片失败: {}", imagePrefix, e);
        }
        return null;
    }

    /**
     * 加载滑块图片
     */
    private BufferedImage loadSliderImage(String imagePrefix) {
        try {
            String imagePath = BACKGROUND_IMAGE_DIR + "/" + imagePrefix + SLIDER_IMAGE_SUFFIX;
            ClassPathResource resource = new ClassPathResource(imagePath);
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    BufferedImage image = ImageIO.read(is);
                    if (image != null) {
                        logger.debug("滑块图片 {} 尺寸: {}x{}", imagePrefix, image.getWidth(), image.getHeight());
                    }
                    return image;
                }
            }
        } catch (IOException e) {
            logger.error("加载滑块图片失败: {}", imagePrefix, e);
        }
        return null;
    }

    /**
     * 从坐标文件读取正确位置
     * 坐标信息编码在文件名中，格式为: {imagePrefix}_{x}_{y}.txt
     */
    private CaptchaCoordinate readCoordinateFromFile(String imagePrefix) {
        try {
            // 查找格式为 {imagePrefix}_{x}_{y}.txt 的坐标文件
            String coordFilePattern = imagePrefix + "_";
            String coordFilePath = BACKGROUND_IMAGE_DIR + "/" + coordFilePattern;
            logger.info("查找坐标文件模式: {}*", coordFilePath);
            
            // 通过ClassPathResource查找匹配的坐标文件
            ClassPathResource dirResource = new ClassPathResource(BACKGROUND_IMAGE_DIR);
            if (dirResource.exists()) {
                File dir = dirResource.getFile();
                if (dir.exists() && dir.isDirectory()) {
                    File[] coordFiles = dir.listFiles((d, name) -> 
                        name.startsWith(coordFilePattern) && name.endsWith(".txt"));
                    
                    if (coordFiles != null && coordFiles.length > 0) {
                        String coordFileName = coordFiles[0].getName();
                        logger.info("找到坐标文件: {}", coordFileName);
                        
                        // 从文件名解析坐标，格式为: {imagePrefix}_{x}_{y}.txt
                        String[] parts = coordFileName.replace(".txt", "").split("_");
                        logger.info("文件名分割结果: {}", java.util.Arrays.toString(parts));
                        
                        if (parts.length >= 3) {
                            int x = Integer.parseInt(parts[1]);
                            int y = Integer.parseInt(parts[2]);
                            logger.info("从文件名解析坐标: {} -> ({}, {})", coordFileName, x, y);
                            return new CaptchaCoordinate(x, y);
                        } else {
                            logger.warn("坐标文件名格式不正确，期望格式: prefix_x_y.txt，实际: {}", coordFileName);
                        }
                    } else {
                        logger.info("未找到匹配的坐标文件: {}*", coordFilePattern);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("读取坐标文件失败: {}", imagePrefix, e);
        }
        return null;
    }

    /**
     * 生成随机位置
     */
    private CaptchaCoordinate generateRandomPosition() {
        int minX = PUZZLE_WIDTH / 2 + 20;
        int maxX = IMAGE_WIDTH - PUZZLE_WIDTH / 2 - 20;
        int minY = PUZZLE_HEIGHT / 2 + 20;
        int maxY = IMAGE_HEIGHT - PUZZLE_HEIGHT / 2 - 20;

        int x = random.nextInt(maxX - minX + 1) + minX;
        int y = random.nextInt(maxY - minY + 1) + minY;
        return new CaptchaCoordinate(x, y);
    }

    /**
     * 将图片转换为Base64字符串
     */
    private String imageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 检查图片是否包含透明度信息
        boolean hasAlpha = image.getColorModel().hasAlpha();
        
        if (hasAlpha) {
            // 对于包含透明度的图片(PNG)，使用PNG格式
            ImageIO.write(image, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } else {
            // 对于不包含透明度的图片(JPG)，使用JPEG格式
            ImageIO.write(image, "JPEG", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
        }
    }
}