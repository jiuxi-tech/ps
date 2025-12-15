package com.jiuxi.common.map.bean;

/**
 * @ClassName: Point
 * @Description: 坐标点 - lng1,lat1
 * @Author: Ypp
 * @Date: 2020/7/16 16:48
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class Marker {

    /** 经度 */
    private String longitude;

    /** 纬度 */
    private String latitude;

    /**
     * 图片类型： 取值为l，m，s，-1。
     * l--大图标 m--中等图标 s--小图标，-1表示自定义图标，
     */
    private String icon = "m";

    /** 默认的图标标准label, label为图标的标签，取值为[0-9],[A-Z]，我们这里默认空字符串 */
    private String label = "";

    /** 当且仅当第一参数为-1，url值不为空时，子标签为自定义图标标签。注意：对于中文标签要用URI编码格式。 */
    private String url;

    /**    
     * @description: 默认图标标注
     * @author Ypp
     * @date 2020/7/16 17:20 
     * @param longitude 经度
     * @param latitude 纬度
     * @return 
     */
    public Marker(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @description: 自定义标签
     * @author Ypp
     * @date 2020/7/16 17:19
     * @param longitude 经度
     * @param latitude 纬度
     * @param icon 图片大小，l--大图标 m--中等图标 s--小图标，-1表示自定义图标
     * @param url 当icon参数为-1时，url值不为空时，子标签为自定义图标地址
     * @return
     */
    public Marker(String longitude, String latitude, String icon, String url) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.icon = icon;
        this.url = url;
    }

    /**    
     * @description: 获取标注
     * @author Ypp
     * @date 2020/7/17 10:57
     * @param 
     * @return java.lang.String
     */
    public String getMarker(){
        return longitude + ',' +  latitude;
    }

    /**
     * @description: 获取标注样式
     * @author Ypp
     * @date 2020/7/17 10:57
     * @param
     * @return java.lang.String
     */
    public String getMarkerStyle(){
        if("-1".equals(icon)){
            return icon + ',' +  url;
        }else{
            return icon + ',' +  label;
        }
    }

}
