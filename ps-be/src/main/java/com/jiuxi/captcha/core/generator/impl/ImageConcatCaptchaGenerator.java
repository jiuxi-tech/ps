package com.jiuxi.captcha.core.generator.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.jiuxi.captcha.bean.vo.ImageCaptchaVO;
import com.jiuxi.captcha.bean.vo.ImageCaptchaCheckVO;
import com.jiuxi.captcha.constant.CaptchaConstant;
import com.jiuxi.captcha.core.cache.CaptchaCacheService;
import com.jiuxi.captcha.core.generator.ImageCaptchaGenerator;
import com.jiuxi.captcha.util.ImageCaptchaUtil;

import javax.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: ImageConcatCaptchaGenerator
 * @Description: 拼接验证码 器
 * @Author: 杨攀
 * @Date: 2022/12/12 17:43
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class ImageConcatCaptchaGenerator implements ImageCaptchaGenerator {


    /**
     * 默认的bgimage资源文件路径.
     */
    public static final String DEFAULT_CAPTCHA_IMAGE_BGIMAGE_PATH = "META-INF/cut-image/bgimage";

    /**
     * 图片类型
     */
    public static final String IMAGE_TYPE = "jpg";

    /**
     * 滑动验证 - 原图缓存
     */
    private static Map<String, BufferedImage> bgimageCacheMap = new ConcurrentHashMap();

    /**
     * 默认的template资源文件路径.
     */
    public static final String DEFAULT_CAPTCHA_IMAGE_TEMPLATE_PATH = "META-INF/cut-image/template";


    /** 图片数量 */
    private static final int SIZE = 2;

    /** 容错值 */
    private static final double TOLERANT = 0.3;


    /** 验证码缓存 */
    private CaptchaCacheService cacheService;

    public ImageConcatCaptchaGenerator(CaptchaCacheService cacheService) {
        this.cacheService = cacheService;
        this.init();
    }

    /**
     * 初始化，把验证码的图片添加到内存中，避免每次都读取文件
     * @author 杨攀
     * @date 2022/12/13 13:36
     * @param
     * @return void
     */
    @PostConstruct
    public void init() {
        // 拼接验证码 只需要 背景原图
        ImageCaptchaUtil.initImage(bgimageCacheMap, DEFAULT_CAPTCHA_IMAGE_BGIMAGE_PATH, SIZE);
    }

    @Override
    public ImageCaptchaVO generateCaptchaImage() {

        // 1、随机获取一个图片
        BufferedImage bgImage = ImageCaptchaUtil.randomImage(bgimageCacheMap);
        // 2、生成图片的切图
        int spacingY = bgImage.getHeight() / 4;
        int randomY = RandomUtil.randomInt(spacingY, bgImage.getHeight() - spacingY);
        // 横向切割的图片
        BufferedImage[] splitImgHorizontal = ImageCaptchaUtil.splitImage(randomY, true, bgImage);

        int spacingX = bgImage.getWidth() / 8;
        // 横坐标
        int randomX = RandomUtil.randomInt(spacingX, bgImage.getWidth() - bgImage.getWidth() / 5);
        // 取第一个图片，然后垂直切割
        BufferedImage[] splitImageVertical = ImageCaptchaUtil.splitImage(randomX, false, splitImgHorizontal[0]);

        // 第一个图片垂直切割后，拼接图片后的图片宽度
        int concatImageWidth = splitImageVertical[0].getWidth() + splitImageVertical[1].getWidth();

        BufferedImage sliderImage = ImageCaptchaUtil.concatImage(true, concatImageWidth, splitImageVertical[0].getHeight(), splitImageVertical[1], splitImageVertical[0]);
        bgImage = ImageCaptchaUtil.concatImage(false, splitImgHorizontal[1].getWidth(), sliderImage.getHeight() + splitImgHorizontal[1].getHeight(),
                sliderImage, splitImgHorizontal[1]);

        // 创建验证图片对象
        ImageCaptchaVO imageCaptchaVO = wrapConcatCaptchaInfo(randomY, bgImage);
        // 生成 客户端ID
        String clientUuid =  IdUtil.simpleUUID();
        imageCaptchaVO.setClientUuid(clientUuid);
        // 保存图片信息到缓存（本地或redis中）
        cacheService.put(clientUuid, String.valueOf(randomX), CaptchaConstant.CAPTCHA_TIMEOUT);

        return imageCaptchaVO;
    }

    /**
     * 组装 ImageCaptchaVO 对象
     * @author 杨攀
     * @date 2022/12/13 18:03
     * @param randomY
     * @param bgImage
     * @return com.jiuxi.captcha.bean.vo.ImageCaptchaVO
     */
    private ImageCaptchaVO wrapConcatCaptchaInfo(int randomY, BufferedImage bgImage) {
        String bgImageBase64 =  ImageCaptchaUtil.bufferedImageToBase64Str(bgImage, IMAGE_TYPE);
        ImageCaptchaVO imageCaptchaVO = ImageCaptchaVO.of(bgImageBase64,
                bgImage.getWidth(),
                bgImage.getHeight(),
                randomY,
                CaptchaConstant.CaptchaType.CONCAT);
        return imageCaptchaVO;
    }


    /**
     * 校验 验证码
     * @author 杨攀
     * @date 2022/12/14 17:20
     * @param trackVO
     * @return String
     */
    @Override
    public String checkCaptcha(ImageCaptchaCheckVO trackVO) {
        return null;
    }


}
