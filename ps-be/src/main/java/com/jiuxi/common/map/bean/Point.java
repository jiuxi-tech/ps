package com.jiuxi.common.map.bean;

/**
 * @ClassName: Point
 * @Description: 坐标点
 * @Author: Ypp
 * @Date: 2020/7/16 17:53
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class Point {

    /** 经度 */
    private String longitude;

    /** 纬度 */
    private String latitude;

    public Point(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getPoint() {
        return longitude + ',' +  latitude;
    }
}
