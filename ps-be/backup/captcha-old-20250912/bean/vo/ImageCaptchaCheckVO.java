package com.jiuxi.captcha.bean.vo;

import java.util.Date;

/**
 * @ClassName: ImageCaptchaTrack
 * @Description: 滑动框结果
 * @Author: 杨攀
 * @Date: 2022/12/13 16:17
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class ImageCaptchaCheckVO {

    /** 客户端id */
    private String clientUuid;

    /** 滑动开始时间. */
    private String startSlidingTime;

    /** 滑动结束时间. */
    private String endSlidingTime;

    /** x. */
    private Integer x;

    /** 验证码类型. 拼接验证码：concat 旋转验证码：rotate  */
    private String type;

    public String getClientUuid() {
        return clientUuid;
    }

    public void setClientUuid(String clientUuid) {
        this.clientUuid = clientUuid;
    }

    public String getStartSlidingTime() {
        return startSlidingTime;
    }

    public void setStartSlidingTime(String startSlidingTime) {
        this.startSlidingTime = startSlidingTime;
    }

    public String getEndSlidingTime() {
        return endSlidingTime;
    }

    public void setEndSlidingTime(String endSlidingTime) {
        this.endSlidingTime = endSlidingTime;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
