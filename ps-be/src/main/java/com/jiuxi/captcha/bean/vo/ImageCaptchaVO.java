package com.jiuxi.captcha.bean.vo;

/**
 * @ClassName: ImageCaptchaInfo
 * @Description: 图片信息
 * @Author: 杨攀
 * @Date: 2022/12/13 17:20
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class ImageCaptchaVO {

    /** 客户端id */
    private String clientUuid;

    /** 背景图 */
    private String backgroundImage;

    /** 背景图片宽度. */
    private Integer bgImageWidth;

    /** 背景图片高度. */
    private Integer bgImageHeight;

    /** 随机值X. */
    private Integer randomX;

    /** 随机值Y. */
    private Integer randomY;

    /** 验证码类型. */
    private String type;

    public ImageCaptchaVO() {

    }


    public ImageCaptchaVO(String backgroundImage,
                          Integer bgImageWidth,
                          Integer bgImageHeight,
                          Integer randomY,
                          String type) {
        this.backgroundImage = backgroundImage;
        this.bgImageWidth = bgImageWidth;
        this.bgImageHeight = bgImageHeight;
        this.randomY = randomY;
        this.type = type;
    }

    /**
     * 创建 对象
     * @author 杨攀
     * @date 2022/12/12 17:19
     * @param backgroundImage 背景图
     * @param bgImageWidth 背景图片宽度
     * @param bgImageHeight 背景图片高度
     * @param randomY 随机值
     * @param type 验证码类型
     * @return com.jiuxi.captcha.bean.vo.ImageCaptchaVO
     */
    public static ImageCaptchaVO of(String backgroundImage,
                                    Integer bgImageWidth,
                                    Integer bgImageHeight,
                                    Integer randomY,
                                    String type) {
        return new ImageCaptchaVO(backgroundImage, bgImageWidth, bgImageHeight, randomY, type);
    }

    public String getClientUuid() {
        return clientUuid;
    }

    public void setClientUuid(String clientUuid) {
        this.clientUuid = clientUuid;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Integer getBgImageWidth() {
        return bgImageWidth;
    }

    public void setBgImageWidth(Integer bgImageWidth) {
        this.bgImageWidth = bgImageWidth;
    }

    public Integer getBgImageHeight() {
        return bgImageHeight;
    }

    public void setBgImageHeight(Integer bgImageHeight) {
        this.bgImageHeight = bgImageHeight;
    }

    public Integer getRandomX() {
        return randomX;
    }

    public void setRandomX(Integer randomX) {
        this.randomX = randomX;
    }

    public Integer getRandomY() {
        return randomY;
    }

    public void setRandomY(Integer randomY) {
        this.randomY = randomY;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
