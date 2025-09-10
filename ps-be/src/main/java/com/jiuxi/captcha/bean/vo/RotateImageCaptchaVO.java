package com.jiuxi.captcha.bean.vo;

import com.jiuxi.captcha.constant.CaptchaConstant;

/**
 * @ClassName: RotateImageCaptchaInfo
 * @Description: 旋转图片信息
 * @Author: 杨攀
 * @Date: 2022/12/13 17:21
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class RotateImageCaptchaVO extends ImageCaptchaVO {

    /** 旋转多少度 */
    private Double degree;

    /** 移动图 */
    private String sliderImage;

    /** 滑块图片宽度. */
    private Integer sliderImageWidth;

    /** 滑块图片高度. */
    private Integer sliderImageHeight;


    public static RotateImageCaptchaVO of(String backgroundImage,
                                          String sliderImage,
                                          Integer bgImageWidth,
                                          Integer bgImageHeight,
                                          Integer sliderImageWidth,
                                          Integer sliderImageHeight) {
        RotateImageCaptchaVO vo = new RotateImageCaptchaVO();
        // vo.setDegree(degree);
        // vo.setRandomX(randomX);
        vo.setBackgroundImage(backgroundImage);
        vo.setSliderImage(sliderImage);
        vo.setBgImageWidth(bgImageWidth);
        vo.setBgImageHeight(bgImageHeight);
        vo.setSliderImageWidth(sliderImageWidth);
        vo.setSliderImageHeight(sliderImageHeight);
        // 类型为旋转图片验证码
        vo.setType(CaptchaConstant.CaptchaType.ROTATE);
        return vo;
    }

    public Double getDegree() {
        return degree;
    }

    public void setDegree(Double degree) {
        this.degree = degree;
    }

    public String getSliderImage() {
        return sliderImage;
    }

    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }

    public Integer getSliderImageWidth() {
        return sliderImageWidth;
    }

    public void setSliderImageWidth(Integer sliderImageWidth) {
        this.sliderImageWidth = sliderImageWidth;
    }

    public Integer getSliderImageHeight() {
        return sliderImageHeight;
    }

    public void setSliderImageHeight(Integer sliderImageHeight) {
        this.sliderImageHeight = sliderImageHeight;
    }
}
