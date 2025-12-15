package com.jiuxi.common.map.bean;

/**
 * @ClassName: Center
 * @Description: 地图中心点位置，参数为经纬度坐标。坐标格式：lng <经度>，lat <纬度>，116.39127,39.90712。
 * @Author: Ypp
 * @Date: 2020/7/16 16:59
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class Center {

    /** 经度 */
    private String longitude;

    /** 纬度 */
    private String latitude;

    public Center(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCenter() {
        return longitude + ',' +  latitude;
    }
}

