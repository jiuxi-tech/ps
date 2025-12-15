package com.jiuxi.common.map.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Path
 * @Description: 折线，每条折线的点之间用分号";"分隔;点坐标用逗号","分隔。坐标格式：lng <经度>，lat <纬度>，例如116.39127,39.90712。
 * @Author: Ypp
 * @Date: 2020/7/16 17:26
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class Path {

    private List<Point> path = new ArrayList<>();
    
    /** 颜色color：16进制表示的数值,如默认值蓝色0xff0000, */
    private String color = "0xff0000";

    /** 线宽weight：[1-40]，默认值6， */
    private String weight = "6";

    /** 透明度opacity：[0-1]，默认值0.6， */
    private double opacity = 0.6;

    /** 填充图颜色fillColor：16进制表示的数值，此参数为可选参当由此参数时，这些将自动封闭为面，并以此颜色填充面的颜色。 */
    private String fillColor = "0xff0000";

    
    public void addPoint(Point point){
        path.add(point);
    }

    /**
     * @description: 获取折线
     * @author Ypp
     * @date 2020/7/17 11:06
     * @param
     * @return java.lang.String
     */
    public String getPath(){
        String result = "";

        StringBuffer sb = new StringBuffer();
        for (Point point : path){
            sb.append(point.getPoint()).append(";");
        }

        if (sb.length() > 0){
            result = sb.substring(0, sb.length() - 1);
        }
        sb = null;

        return result;
    }

    /**
     * @description: 折线的样式
     * @author Ypp
     * @date 2020/7/17 11:18
     * @param
     * @return java.lang.String
     */
    public String getPathStyle(){
        StringBuffer sb = new StringBuffer();
        sb.append(color).append(",").append(weight).append(",").append(opacity).append(",").append(fillColor);
        return sb.toString();
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }
}
