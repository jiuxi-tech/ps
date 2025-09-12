package com.jiuxi.captcha.core.generator.impl;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.captcha.bean.entity.Coordinate;
import com.jiuxi.captcha.bean.vo.ImageCaptchaCheckVO;
import com.jiuxi.captcha.bean.vo.ImageCaptchaVO;
import com.jiuxi.captcha.bean.vo.SliderImageCaptchaVO;
import com.jiuxi.captcha.constant.CaptchaConstant;
import com.jiuxi.captcha.core.cache.CaptchaCacheService;
import com.jiuxi.captcha.core.generator.ImageCaptchaGenerator;
import com.jiuxi.captcha.util.ImageCaptchaUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: ImageSliderCaptchaGenerator
 * @Description: 滑块验证码
 * @Author: 杨攀
 * @Date: 2023/4/19 16:30
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 *
 * <pre>
 *     一开始就生成好，避免高并发的时候，消耗CPU
 * </pre>
 *
 */
public class ImageSliderCaptchaGenerator implements ImageCaptchaGenerator {

    /** 单例 */
    public static volatile ImageSliderCaptchaGenerator instance;

    /**
     * 默认的resource资源文件路径.
     */
    public static final String SLIDER_CAPTCHA_IMAGE_PATH = "META-INF/cut-image/slider";

    /**
     * 图片类型 jpg
     */
    public static final String IMAGE_TYPE_JPG = "jpg";

    /**
     * 图片类型 png
     */
    public static final String IMAGE_TYPE_PNG = "png";


    /**
     * 滑块验证 - 滑块抠图缓存
     */
    private static Map<String, BufferedImage> cutImageCacheMap = new ConcurrentHashMap();

    /**
     * 滑块验证 - 背景缓存
     */
    private static Map<String, BufferedImage> bgImageCacheMap = new ConcurrentHashMap();

    /**
     * 缓存坐标
     */
    private static Map<String, Coordinate> coordinateMap = new HashMap<>(36);

    /** 容错值 */
    private static final double TOLERANT = 10;

    /** 验证码缓存 */
    private CaptchaCacheService cacheService;


    /**
     * 单例
     * @author 杨攀
     * @date 2023/4/19 17:00
     * @param cacheService
     * @return com.jiuxi.captcha.core.generator.impl.ImageSliderCaptchaGenerator
     */
    public static ImageSliderCaptchaGenerator getInstance(CaptchaCacheService cacheService){
        if(instance == null){
            synchronized (ImageRotateCaptchaGenerator.class){
                if(instance == null){
                    instance = new ImageSliderCaptchaGenerator(cacheService);
                }
            }
        }
        return instance;
    }


    private ImageSliderCaptchaGenerator(CaptchaCacheService cacheService) {
        this.cacheService = cacheService;
        this.init();
    }

    /**
     * 初始化，把验证码的图片添加到内存中，避免每次都读取文件
     * @author 杨攀
     * @date 2023/4/19 17:06
     * @param
     * @return void
     *
     * <pre>
     *    1、需要提前生成好滑块图和背景图，避免每次都生成，可以通过执行测试类【TestImageSliderCaptchaGenerator】生成，
     *    2、缓存 滑块图和背景图及坐标，嘎嘎嘎，虽然后漏洞，但是性能好，不知道的人应该不知道漏洞，嘎嘎嘎嘎嘎
     * </pre>
     */
    private void init() {

        initCoordinateMap();

        ClassLoader classLoader = ImageCaptchaUtil.class.getClassLoader();

        // 初始化背景图和滑块图
        for (int i = 0; i < coordinateMap.size(); i++) {

            // 文件名，用下标
            String filename = String.valueOf(i);

            //  滑块图片路径
            String sliderImgPath = SLIDER_CAPTCHA_IMAGE_PATH.concat("/").concat(filename.concat(".png"));
            InputStream sliderResourceAsStream = classLoader.getResourceAsStream(sliderImgPath);
            BufferedImage sliderBufferedImage = ImgUtil.read(sliderResourceAsStream);
            // 滑块图
            cutImageCacheMap.put(filename, sliderBufferedImage);

            // bg 图片路径
            String bgImgPath = SLIDER_CAPTCHA_IMAGE_PATH.concat("/").concat(filename.concat("bg.jpg"));
            InputStream bgResourceAsStream = classLoader.getResourceAsStream(bgImgPath);
            BufferedImage bgBufferedImage = ImgUtil.read(bgResourceAsStream);
            // bg 图
            bgImageCacheMap.put(filename, bgBufferedImage);
        }
    }

    /**
     * 初始化图片的 坐标
     * @author 杨攀
     * @date 2023/4/19 17:41
     * @param
     * @return void
     */
    private void initCoordinateMap (){
        coordinateMap.put("0", new Coordinate(115,65));
        coordinateMap.put("1", new Coordinate(139,88));
        coordinateMap.put("2", new Coordinate(336,145));
        coordinateMap.put("3", new Coordinate(115,72));
        coordinateMap.put("4", new Coordinate(302,73));
        coordinateMap.put("5", new Coordinate(234,163));
        coordinateMap.put("6", new Coordinate(463,81));
        coordinateMap.put("7", new Coordinate(237,111));
        coordinateMap.put("8", new Coordinate(156,188));
        coordinateMap.put("9", new Coordinate(206,173));
        coordinateMap.put("10", new Coordinate(334,75));
        coordinateMap.put("11", new Coordinate(440,123));
        coordinateMap.put("12", new Coordinate(317,40));
        coordinateMap.put("13", new Coordinate(174,37));
        coordinateMap.put("14", new Coordinate(317,136));
        coordinateMap.put("15", new Coordinate(137,151));
        coordinateMap.put("16", new Coordinate(412,169));
        coordinateMap.put("17", new Coordinate(402,128));
        coordinateMap.put("18", new Coordinate(193,127));
        coordinateMap.put("19", new Coordinate(179,178));
        coordinateMap.put("20", new Coordinate(201,117));
        coordinateMap.put("21", new Coordinate(231,103));
        coordinateMap.put("22", new Coordinate(297,225));
        coordinateMap.put("23", new Coordinate(362,96));
        coordinateMap.put("24", new Coordinate(245,156));
        coordinateMap.put("25", new Coordinate(302,115));
        coordinateMap.put("26", new Coordinate(345,81));
        coordinateMap.put("27", new Coordinate(409,138));
        coordinateMap.put("28", new Coordinate(329,111));
        coordinateMap.put("29", new Coordinate(397,126));
        coordinateMap.put("30", new Coordinate(130,150));
        coordinateMap.put("31", new Coordinate(338,120));
        coordinateMap.put("32", new Coordinate(354,171));
        coordinateMap.put("33", new Coordinate(427,165));
        coordinateMap.put("34", new Coordinate(246,167));
        coordinateMap.put("35", new Coordinate(375,121));
        coordinateMap.put("36", new Coordinate(270,119));
        coordinateMap.put("37", new Coordinate(139,82));
        coordinateMap.put("38", new Coordinate(172,189));
        coordinateMap.put("39", new Coordinate(374,53));
        coordinateMap.put("40", new Coordinate(238,113));
        coordinateMap.put("41", new Coordinate(253,151));
        coordinateMap.put("42", new Coordinate(335,70));
        coordinateMap.put("43", new Coordinate(115,104));
        coordinateMap.put("44", new Coordinate(304,120));
        coordinateMap.put("45", new Coordinate(237,40));
        coordinateMap.put("46", new Coordinate(315,174));
        coordinateMap.put("47", new Coordinate(243,108));
        coordinateMap.put("48", new Coordinate(231,116));
        coordinateMap.put("49", new Coordinate(197,222));
        coordinateMap.put("50", new Coordinate(188,177));
    }


    /**
     * 获取验证码
     * @author 杨攀
     * @date 2023/4/19 17:42
     * @param
     * @return com.jiuxi.captcha.bean.vo.ImageCaptchaVO
     */
    @Override
    public ImageCaptchaVO generateCaptchaImage() {

        // 1、随机获取一个 旋转抠图图片
        int randomInt = RandomUtil.randomInt(coordinateMap.size());
        String randomKey = String.valueOf(randomInt);

        // 坐标
        Coordinate coordinate = coordinateMap.get(randomKey);
        // 背景图
        BufferedImage bgBufferedImage = bgImageCacheMap.get(randomKey);
        // 滑块
        BufferedImage cutBufferedImage = cutImageCacheMap.get(randomKey);

        ImageCaptchaVO imageCaptchaVO = wrapRotateCaptchaInfo(bgBufferedImage, cutBufferedImage);
        // 生成 客户端ID
        String clientUuid =  IdUtil.simpleUUID();
        imageCaptchaVO.setClientUuid(clientUuid);

        // 开发时，临时返回前端
        // imageCaptchaVO.setRandomX(coordinate.getRandomX());

        String clientUuidKey = CaptchaConstant.RedisKeyPro.PLATFORM_CAPTCHA_CLIENTUUID + clientUuid;
        cacheService.put(clientUuidKey, String.valueOf(coordinate.getRandomX()), CaptchaConstant.CAPTCHA_TIMEOUT);

        return imageCaptchaVO;
    }


    /**
     * 组装 ImageCaptchaVO 对象
     * @author 杨攀
     * @date 2022/12/14 16:28
     * @param bgImage 背景图
     * @param cutImage 滑块图片
     * @return com.jiuxi.captcha.bean.vo.ImageCaptchaVO
     */
    private ImageCaptchaVO wrapRotateCaptchaInfo(BufferedImage bgImage, BufferedImage cutImage) {

        String bgImageBase64 =  ImageCaptchaUtil.bufferedImageToBase64Str(bgImage, IMAGE_TYPE_JPG);
        String rotateImageBase64 =  ImageCaptchaUtil.bufferedImageToBase64Str(cutImage, IMAGE_TYPE_PNG);

        return SliderImageCaptchaVO.of(
                bgImageBase64,
                rotateImageBase64,
                bgImage.getWidth(), bgImage.getHeight(),
                cutImage.getWidth(), cutImage.getHeight()
        );
    }

    /**
     * 校验 验证码，并生成票据
     * @author 杨攀
     * @date 2023/4/19 17:51
     * @param trackVO
     * @return java.lang.String
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
