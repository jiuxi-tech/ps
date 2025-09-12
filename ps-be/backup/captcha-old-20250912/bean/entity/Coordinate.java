package com.jiuxi.captcha.bean.entity;

/**
 * @ClassName: Coordinate
 * @Description: 坐标
 * @Author: 杨攀
 * @Date: 2023/4/19 17:18
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 */
public class Coordinate {

    /** 随机值X. */
    private Integer randomX;

    /** 随机值Y. */
    private Integer randomY;

    public Coordinate(Integer randomX, Integer randomY) {
        this.randomX = randomX;
        this.randomY = randomY;
    }

    public Integer getRandomX() {
        return randomX;
    }

    public Integer getRandomY() {
        return randomY;
    }

}
