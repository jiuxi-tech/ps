package com.jiuxi.platform.captcha.infrastructure.generator;

import com.jiuxi.platform.captcha.domain.entity.CaptchaChallenge;
import com.jiuxi.platform.captcha.domain.service.CaptchaGenerator;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaCoordinate;
import com.jiuxi.platform.captcha.domain.valueobject.CaptchaType;
import com.jiuxi.platform.captcha.infrastructure.config.CaptchaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.nio.file.Files;
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

    // 注入验证码配置
    private final CaptchaProperties captchaProperties;
    
    @Autowired
    public ResourceSliderCaptchaGenerator(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }

    // 图片资源路径配置
    @Value("${captcha.image.external-dir:}")
    private String externalRootDir;  // 外部根目录，对应META-INF同级
    
    private static final String INTERNAL_IMAGE_DIR = "META-INF/cut-image/slider";
    private static final String EXTERNAL_RELATIVE_DIR = "cut-image/slider";  // 外部目录中的相对路径
    private static final String BACKGROUND_IMAGE_SUFFIX = "bg.jpg";
    private static final String SLIDER_IMAGE_SUFFIX = ".png";

    @Override
    public CaptchaChallenge generateChallenge(CaptchaType captchaType) {
        if (!supports(captchaType)) {
            throw new IllegalArgumentException("不支持的验证码类型: " + captchaType);
        }

        CaptchaChallenge challenge = new CaptchaChallenge(captchaType);
        // 从配置中读取最大尝试次数
        challenge.setMaxAttempts(captchaProperties.getMaxAttempts());

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
     * 优先使用外部目录，如果未配置或不存在则使用内部资源
     */
    private List<String> getAvailableImages() {
        List<String> images = new ArrayList<>();
        
        // 优先尝试使用外部目录
        if (StringUtils.hasText(externalRootDir)) {
            logger.info("使用外部验证码图片根目录: {}", externalRootDir);
            images = getImagesFromExternalDir();
            if (!images.isEmpty()) {
                logger.info("从外部目录找到 {} 个可用图片", images.size());
                return images;
            } else {
                logger.warn("外部目录中未找到可用的验证码图片，将使用内部资源");
            }
        }
        
        // 如果外部目录不可用，使用内部资源
        logger.info("使用内部验证码图片资源");
        images = getImagesFromInternalResources();
        logger.info("从内部资源找到 {} 个可用图片", images.size());
        
        return images;
    }
    
    /**
     * 从外部目录获取图片资源
     * 外部目录结构：externalRootDir/cut-image/slider/
     */
    private List<String> getImagesFromExternalDir() {
        List<String> images = new ArrayList<>();
        try {
            // 构建完整的slider目录路径
            File sliderDir = new File(externalRootDir, EXTERNAL_RELATIVE_DIR);
            if (!sliderDir.exists() || !sliderDir.isDirectory()) {
                logger.warn("外部slider目录不存在或不是目录: {}", sliderDir.getAbsolutePath());
                return images;
            }
            
            logger.info("扫描外部slider目录: {}", sliderDir.getAbsolutePath());
            
            // 扫描数字编号 0-50 的图片资源
            for (int i = 0; i <= 50; i++) {
                String imagePrefix = String.valueOf(i);
                File backgroundFile = new File(sliderDir, imagePrefix + BACKGROUND_IMAGE_SUFFIX);
                File sliderFile = new File(sliderDir, imagePrefix + SLIDER_IMAGE_SUFFIX);
                
                if (backgroundFile.exists() && sliderFile.exists()) {
                    images.add(imagePrefix);
                    logger.debug("找到外部验证码图片: {}", imagePrefix);
                }
            }
            
        } catch (Exception e) {
            logger.warn("扫描外部验证码图片目录失败: {}", externalRootDir, e);
        }
        return images;
    }
    
    /**
     * 从内部资源获取图片
     */
    private List<String> getImagesFromInternalResources() {
        List<String> images = new ArrayList<>();
        try {
            // 在JAR包中，根据实际的文件命名格式（数字编号）
            // 验证数字编号 0-50 的图片资源
            for (int i = 0; i <= 50; i++) {
                String imagePrefix = String.valueOf(i);
                String backgroundImagePath = INTERNAL_IMAGE_DIR + "/" + imagePrefix + BACKGROUND_IMAGE_SUFFIX;
                String sliderImagePath = INTERNAL_IMAGE_DIR + "/" + imagePrefix + SLIDER_IMAGE_SUFFIX;
                
                ClassPathResource backgroundResource = new ClassPathResource(backgroundImagePath);
                ClassPathResource sliderResource = new ClassPathResource(sliderImagePath);
                
                if (backgroundResource.exists() && sliderResource.exists()) {
                    images.add(imagePrefix);
                    logger.debug("找到内部验证码图片: {}", imagePrefix);
                }
            }
            
        } catch (Exception e) {
            logger.warn("扫描内部验证码图片资源失败: {}", INTERNAL_IMAGE_DIR, e);
        }
        return images;
    }

    /**
     * 加载背景图片
     * 优先从外部目录加载，如果不存在则从内部资源加载
     */
    private BufferedImage loadBackgroundImage(String imagePrefix) {
        // 优先尝试从外部目录加载
        if (StringUtils.hasText(externalRootDir)) {
            try {
                File sliderDir = new File(externalRootDir, EXTERNAL_RELATIVE_DIR);
                File externalFile = new File(sliderDir, imagePrefix + BACKGROUND_IMAGE_SUFFIX);
                if (externalFile.exists() && externalFile.isFile()) {
                    try (FileInputStream fis = new FileInputStream(externalFile)) {
                        BufferedImage image = ImageIO.read(fis);
                        if (image != null) {
                            logger.debug("从外部目录加载背景图片 {} 尺寸: {}x{}", imagePrefix, image.getWidth(), image.getHeight());
                        }
                        return image;
                    }
                }
            } catch (IOException e) {
                logger.warn("从外部目录加载背景图片失败: {}, 将尝试内部资源", imagePrefix, e);
            }
        }
        
        // 从内部资源加载
        try {
            String imagePath = INTERNAL_IMAGE_DIR + "/" + imagePrefix + BACKGROUND_IMAGE_SUFFIX;
            ClassPathResource resource = new ClassPathResource(imagePath);
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    BufferedImage image = ImageIO.read(is);
                    if (image != null) {
                        logger.debug("从内部资源加载背景图片 {} 尺寸: {}x{}", imagePrefix, image.getWidth(), image.getHeight());
                    }
                    return image;
                }
            }
        } catch (IOException e) {
            logger.error("从内部资源加载背景图片失败: {}", imagePrefix, e);
        }
        return null;
    }

    /**
     * 加载滑块图片
     * 优先从外部目录加载，如果不存在则从内部资源加载
     */
    private BufferedImage loadSliderImage(String imagePrefix) {
        // 优先尝试从外部目录加载
        if (StringUtils.hasText(externalRootDir)) {
            try {
                File sliderDir = new File(externalRootDir, EXTERNAL_RELATIVE_DIR);
                File externalFile = new File(sliderDir, imagePrefix + SLIDER_IMAGE_SUFFIX);
                if (externalFile.exists() && externalFile.isFile()) {
                    try (FileInputStream fis = new FileInputStream(externalFile)) {
                        BufferedImage image = ImageIO.read(fis);
                        if (image != null) {
                            logger.debug("从外部目录加载滑块图片 {} 尺寸: {}x{}", imagePrefix, image.getWidth(), image.getHeight());
                        }
                        return image;
                    }
                }
            } catch (IOException e) {
                logger.warn("从外部目录加载滑块图片失败: {}, 将尝试内部资源", imagePrefix, e);
            }
        }
        
        // 从内部资源加载
        try {
            String imagePath = INTERNAL_IMAGE_DIR + "/" + imagePrefix + SLIDER_IMAGE_SUFFIX;
            ClassPathResource resource = new ClassPathResource(imagePath);
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    BufferedImage image = ImageIO.read(is);
                    if (image != null) {
                        logger.debug("从内部资源加载滑块图片 {} 尺寸: {}x{}", imagePrefix, image.getWidth(), image.getHeight());
                    }
                    return image;
                }
            }
        } catch (IOException e) {
            logger.error("从内部资源加载滑块图片失败: {}", imagePrefix, e);
        }
        return null;
    }

    /**
     * 从坐标文件读取正确位置
     * 坐标信息编码在文件名中，格式为: {imagePrefix}_{x}_{y}.txt
     * 优先从外部目录读取，如果不存在则从内部资源读取
     */
    private CaptchaCoordinate readCoordinateFromFile(String imagePrefix) {
        // 优先尝试从外部目录读取
        if (StringUtils.hasText(externalRootDir)) {
            CaptchaCoordinate externalCoord = readCoordinateFromExternalDir(imagePrefix);
            if (externalCoord != null) {
                return externalCoord;
            }
        }
        
        // 从内部资源读取
        return readCoordinateFromInternalResources(imagePrefix);
    }
    
    /**
     * 从外部目录读取坐标文件
     */
    private CaptchaCoordinate readCoordinateFromExternalDir(String imagePrefix) {
        try {
            File sliderDir = new File(externalRootDir, EXTERNAL_RELATIVE_DIR);
            if (!sliderDir.exists() || !sliderDir.isDirectory()) {
                return null;
            }
            
            String coordFilePattern = imagePrefix + "_";
            
            File[] coordFiles = sliderDir.listFiles((d, name) -> 
                name.startsWith(coordFilePattern) && name.endsWith(".txt"));
            
            if (coordFiles != null && coordFiles.length > 0) {
                String coordFileName = coordFiles[0].getName();
                logger.info("从外部目录找到坐标文件: {}", coordFileName);
                
                // 从文件名解析坐标，格式为: {imagePrefix}_{x}_{y}.txt
                String[] parts = coordFileName.replace(".txt", "").split("_");
                
                if (parts.length >= 3) {
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    logger.info("从外部目录文件名解析坐标: {} -> ({}, {})", coordFileName, x, y);
                    return new CaptchaCoordinate(x, y);
                } else {
                    logger.warn("外部目录坐标文件名格式不正确，期望格式: prefix_x_y.txt，实际: {}", coordFileName);
                }
            }
        } catch (Exception e) {
            logger.warn("从外部目录读取坐标文件失败: {}", imagePrefix, e);
        }
        return null;
    }
    
    /**
     * 从内部资源读取坐标文件
     */
    private CaptchaCoordinate readCoordinateFromInternalResources(String imagePrefix) {
        try {
            // 由于JAR包中无法直接遍历目录，我们先尝试直接读取知道的坐标文件
            // 根据实际文件的命名规律，手动维护一个坐标映射表
            String[] knownCoordinates = {
                "0_115_65", "1_139_88", "2_336_145", "3_155_72", "4_302_73", "5_234_163",
                "6_463_81", "7_237_111", "8_156_188", "9_206_173", "10_334_75", "11_440_123",
                "12_317_40", "13_174_37", "14_317_136", "15_137_151", "16_412_169", "17_402_128",
                "18_193_127", "19_179_178", "20_201_117", "21_231_103", "22_297_225", "23_362_96",
                "24_245_156", "25_302_115", "26_345_81", "27_409_138", "28_329_111", "29_397_126",
                "30_130_150", "31_338_120", "32_354_171", "33_427_165", "34_246_167", "35_375_121",
                "36_270_119", "37_139_82", "38_172_189", "39_374_53", "40_238_113", "41_253_151",
                "42_335_70", "43_155_104", "44_304_120", "45_237_40", "46_315_174", "47_243_108",
                "48_231_116", "49_197_222", "50_188_177"
            };
            
            // 找到匹配的坐标文件
            for (String coord : knownCoordinates) {
                if (coord.startsWith(imagePrefix + "_")) {
                    String coordFileName = coord + ".txt";
                    String fullCoordPath = INTERNAL_IMAGE_DIR + "/" + coordFileName;
                    
                    ClassPathResource coordResource = new ClassPathResource(fullCoordPath);
                    if (coordResource.exists()) {
                        // 从文件名解析坐标
                        String[] parts = coord.split("_");
                        if (parts.length >= 3) {
                            int x = Integer.parseInt(parts[1]);
                            int y = Integer.parseInt(parts[2]);
                            logger.info("从内部资源找到坐标文件: {}", coordFileName);
                            logger.info("从内部资源文件名解析坐标: {} -> ({}, {})", coordFileName, x, y);
                            return new CaptchaCoordinate(x, y);
                        }
                    }
                }
            }
            
            // 如果上面的静态映射找不到，尝试自动扫描（作为备选方案）
            logger.debug("静态映射没有找到坐标文件，尝试自动扫描: {}", imagePrefix);
            return scanForCoordinateFile(imagePrefix);
            
        } catch (Exception e) {
            logger.warn("从内部资源读取坐标文件失败: {}", imagePrefix, e);
        }
        return null;
    }
    
    /**
     * 扫描查找坐标文件（备选方案）
     * 尝试在常用的坐标范围内查找匹配的文件
     */
    private CaptchaCoordinate scanForCoordinateFile(String imagePrefix) {
        try {
            // 根据实际图片尺寸和坐标文件分布，扩大扫描范围
            int[] xRange = {100, 115, 130, 137, 139, 155, 156, 172, 174, 179, 188, 193, 197, 201, 206, 231, 234, 237, 238, 243, 245, 246, 253, 270, 297, 302, 304, 315, 317, 329, 334, 335, 336, 338, 345, 354, 362, 374, 375, 397, 402, 409, 412, 427, 440, 463};
            int[] yRange = {37, 40, 53, 65, 70, 72, 73, 75, 81, 82, 88, 96, 103, 104, 108, 111, 113, 115, 116, 117, 119, 120, 121, 123, 127, 128, 136, 138, 145, 150, 151, 156, 163, 165, 167, 169, 171, 174, 177, 178, 188, 189, 222, 225};
            
            for (int x : xRange) {
                for (int y : yRange) {
                    String coordFileName = imagePrefix + "_" + x + "_" + y + ".txt";
                    String fullCoordPath = INTERNAL_IMAGE_DIR + "/" + coordFileName;
                    
                    ClassPathResource coordResource = new ClassPathResource(fullCoordPath);
                    if (coordResource.exists()) {
                        logger.info("自动扫描找到内部资源坐标文件: {}", coordFileName);
                        logger.info("从扫描结果解析坐标: {} -> ({}, {})", coordFileName, x, y);
                        return new CaptchaCoordinate(x, y);
                    }
                }
            }
            
            logger.info("扫描后仍未在内部资源中找到匹配的坐标文件: {}_*", imagePrefix);
        } catch (Exception e) {
            logger.warn("扫描坐标文件失败: {}", imagePrefix, e);
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