package com.jiuxi.captcha.bean.vo;

import com.jiuxi.captcha.constant.CaptchaConstant;

/**
 * @ClassName: SliderImageCaptchaVO
 * @Description: 滑块验证码
 * @Author: 杨攀
 * @Date: 2023/4/19 15:27
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 */
public class SliderImageCaptchaVO extends ImageCaptchaVO {

    /** 移动图 */
    private String sliderImage;

    /** 滑块图片宽度. */
    private Integer sliderImageWidth;

    /** 滑块图片高度. */
    private Integer sliderImageHeight;


    public static SliderImageCaptchaVO of(String backgroundImage,
                                          String sliderImage,
                                          Integer bgImageWidth,
                                          Integer bgImageHeight,
                                          Integer sliderImageWidth,
                                          Integer sliderImageHeight){

        SliderImageCaptchaVO vo = new SliderImageCaptchaVO();
        vo.setBackgroundImage(backgroundImage);
        vo.setSliderImage(sliderImage);
        vo.setBgImageWidth(bgImageWidth);
        vo.setBgImageHeight(bgImageHeight);
        vo.setSliderImageWidth(sliderImageWidth);
        vo.setSliderImageHeight(sliderImageHeight);
        // 类型为旋转图片验证码
        vo.setType(CaptchaConstant.CaptchaType.SLIDER);
        return vo;
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
