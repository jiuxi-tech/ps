package com.jiuxi.captcha.core.generator.impl;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.captcha.bean.vo.ImageCaptchaCheckVO;
import com.jiuxi.captcha.bean.vo.ImageCaptchaVO;
import com.jiuxi.captcha.bean.vo.RotateImageCaptchaVO;
import com.jiuxi.captcha.constant.CaptchaConstant;
import com.jiuxi.captcha.core.cache.CaptchaCacheService;
import com.jiuxi.captcha.core.generator.ImageCaptchaGenerator;
import com.jiuxi.captcha.util.ImageCaptchaUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: ImageRotateCaptchaGenerator
 * @Description: 旋转验证码 器
 * @Author: 杨攀
 * @Date: 2022/12/12 17:43
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class ImageRotateCaptchaGenerator implements ImageCaptchaGenerator {

    /** 单例 */
    private static volatile ImageRotateCaptchaGenerator instance;

    /**
     * 默认的resource资源文件路径.
     */
    public static final String ROTATE_CAPTCHA_IMAGE_PATH = "META-INF/cut-image/rotate";

    /**
     * 图片类型 jpg
     */
    public static final String IMAGE_TYPE_JPG = "jpg";

    /**
     * 图片类型 png
     */
    public static final String IMAGE_TYPE_PNG = "png";

    /**
     * 旋转验证 - 旋转抠图缓存（中间的部分）
     */
    private static Map<String, BufferedImage> cutImageCacheMap = new ConcurrentHashMap();

    /**
     * 旋转验证 - 背景缓存
     */
    private static Map<String, BufferedImage> bgImageCacheMap = new ConcurrentHashMap();


    /** 图片数量 */
    private  int SIZE = 4;

    /** 容错值 */
    private static final double TOLERANT = 8;

    /** fixed.png 图片的宽度 */
    private static final int FIXED_WIDTH = 200;

    /** 验证码缓存 */
    private CaptchaCacheService cacheService;

    /**
     * 单例
     * @author 杨攀
     * @date 2022/12/16 14:42
     * @param cacheService
     * @return com.jiuxi.captcha.core.generator.impl.ImageRotateCaptchaGenerator
     */
    public static ImageRotateCaptchaGenerator getInstance(CaptchaCacheService cacheService){
        if(instance == null){
            synchronized (ImageRotateCaptchaGenerator.class){
                if(instance == null){
                    instance = new ImageRotateCaptchaGenerator(cacheService);
                }
            }
        }
        return instance;
    }


    private ImageRotateCaptchaGenerator(CaptchaCacheService cacheService) {
        this.cacheService = cacheService;
        this.init();
    }


    /**
     * 初始化，把验证码的图片添加到内存中，避免每次都读取文件
     * @author 杨攀
     * @date 2022/12/13 13:36
     * @param
     * @return void
     *
     * <pre>
     *    1、需要提前生成好旋转抠图和背景图，避免每次都生成，可以通过执行测试类【TestImageRotateCaptchaGenerator】生成，
     *    2、缓存 旋转抠图和背景图
     * </pre>
     *
     */
    private void init() {

        // 2、缓存 旋转抠图
        ImageCaptchaUtil.initImage(cutImageCacheMap, ROTATE_CAPTCHA_IMAGE_PATH, SIZE);
        // 缓存 背景图
        ClassLoader classLoader = ImageCaptchaUtil.class.getClassLoader();
        for (int i = 0; i < SIZE; i++) {
            // 文件名，用下标
            String filename = String.valueOf(i);
            // 图片路径
            String imgPath = ROTATE_CAPTCHA_IMAGE_PATH.concat("/").concat(filename.concat("bg.jpg"));
            InputStream resourceAsStream = classLoader.getResourceAsStream(imgPath);
            BufferedImage bufferedImage = ImgUtil.read(resourceAsStream);
            // 原图
            bgImageCacheMap.put(filename, bufferedImage);
        }
    }

    /**
     * 获取旋转验证码
     * @author 杨攀
     * @date 2022/12/13 16:13
     * @param
     * @return com.jiuxi.captcha.bean.vo.ImageCaptchaVO
     */
    @Override
    public ImageCaptchaVO generateCaptchaImage() {

        // 1、随机获取一个 旋转抠图图片
        int randomInt = RandomUtil.randomInt(cutImageCacheMap.size());
        String randomKey = String.valueOf(randomInt);

        // 随机获取一个 旋转抠图图片
        BufferedImage cutImage =  cutImageCacheMap.get(randomKey);
        // 创建一个新的BufferedImage对象, 避免对原始图片修改(旋转方法已经处理，不会对原图进行修改)
        // BufferedImage cutImage =  ImageCaptchaUtil.createBufferedImage(bufferedImage);
        // 获取根据 抠图对应的 背景图
        BufferedImage bgImage = bgImageCacheMap.get(randomKey);

        // 2、随机旋转抠图部分, 随机x， 转换为角度
        // 随机x， 转换为角度
        int randomX = RandomUtil.randomInt(FIXED_WIDTH + 10, bgImage.getWidth() - 10);
        double degree = 360d - randomX / ((bgImage.getWidth()) / 360d);

        // 旋转图片, 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
        BufferedImage rotateImage = ImageCaptchaUtil.rotateImage(cutImage, degree);

        ImageCaptchaVO imageCaptchaVO = wrapRotateCaptchaInfo(bgImage, rotateImage);
        // 生成 客户端ID
        String clientUuid =  IdUtil.simpleUUID();
        imageCaptchaVO.setClientUuid(clientUuid);
        // 开发时，临时返回前端
        // imageCaptchaVO.setRandomX(randomX);

        // 保存图片信息到缓存（本地或redis中）
        String clientUuidKey = CaptchaConstant.RedisKeyPro.PLATFORM_CAPTCHA_CLIENTUUID + clientUuid;
        cacheService.put(clientUuidKey, String.valueOf(randomX), CaptchaConstant.CAPTCHA_TIMEOUT);

        return imageCaptchaVO;
    }

    /**
     * 组装 ImageCaptchaVO 对象
     * @author 杨攀
     * @date 2022/12/14 16:28
     * @param bgImage 背景图
     * @param rotateImage 旋转图片
     * @return com.jiuxi.captcha.bean.vo.ImageCaptchaVO
     */
    private ImageCaptchaVO wrapRotateCaptchaInfo(BufferedImage bgImage, BufferedImage rotateImage) {

        String bgImageBase64 =  ImageCaptchaUtil.bufferedImageToBase64Str(bgImage, IMAGE_TYPE_JPG);
        String rotateImageBase64 =  ImageCaptchaUtil.bufferedImageToBase64Str(rotateImage, IMAGE_TYPE_PNG);

        return RotateImageCaptchaVO.of(
                bgImageBase64,
                rotateImageBase64,
                bgImage.getWidth(), bgImage.getHeight(),
                rotateImage.getWidth(), rotateImage.getHeight()
        );
    }


    /**
     * 校验 验证码
     * @author 杨攀
     * @date 2022/12/14 17:20
     * @param trackVO
     * @return boolean
     */
    @Override
    public String checkCaptcha(ImageCaptchaCheckVO trackVO) {

        // 客户端 id
        String clientUuidKey = CaptchaConstant.RedisKeyPro.PLATFORM_CAPTCHA_CLIENTUUID + trackVO.getClientUuid();;
        String randomX = cacheService.getString(clientUuidKey);
        if(StrUtil.isBlank(randomX)){
            throw new TopinfoRuntimeException(-1, "验证码不存在或过期，请重试！");
        }
        // 结果 X
        Double resultX = Double.valueOf(randomX);
        // 请求的横坐标X
        Integer checkX = trackVO.getX();

        String ticket = null;
        // 容错值
        double maxTolerant = resultX + TOLERANT;
        double minTolerant = resultX - TOLERANT;
        // 为真，则验证通过
        if(minTolerant <= checkX && checkX <= maxTolerant){
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuuMMddHHmmss"));
            ticket = IdUtil.fastSimpleUUID();
            String ticketKey = CaptchaConstant.RedisKeyPro.PLATFORM_CAPTCHA_TICKET + ticket;
            cacheService.put(ticketKey, dateTime, CaptchaConstant.CAPTCHA_TICKET_TIMEOUT);
        }
        cacheService.remove(clientUuidKey);
        return ticket;
    }

}
