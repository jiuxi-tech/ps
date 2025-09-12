package com.jiuxi.common.map.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Paths
 * @Description: 折线，可通过经纬度描述；折线之间用竖线"|"分隔；每条折线的点之间用分号";"分隔;点坐标用逗号","分隔。坐标格式：lng <经度>，lat <纬度>，例如116.39127,39.90712。
 * @Author: 杨攀
 * @Date: 2020/7/16 17:25
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class Paths {

    private List<Path> paths = new ArrayList<>();

    public void addPath(Path path){
        paths.add(path);
    }

    /**
     * @description: 获取折线 多个
     * @author 杨攀
     * @date 2020/7/17 11:21
     * @param
     * @return java.lang.String
     */
    public String getPaths(){
        String result = "";
        StringBuffer sb = new StringBuffer();
        for (Path path : paths){
            sb.append(path.getPath()).append("|");
        }

        if (sb.length() > 0){
            result = sb.substring(0, sb.length() - 1);
        }
        sb = null;
        return result;
    }

    /**
     * @description: 折线的样式多个
     * @author 杨攀
     * @date 2020/7/17 11:21
     * @param
     * @return java.lang.String
     */
    public String getPathStyles(){
        String result = "";
        StringBuffer sb = new StringBuffer();
        for (Path path : paths){
            sb.append(path.getPathStyle()).append("|");
        }

        if (sb.length() > 0){
            result = sb.substring(0, sb.length() - 1);
        }
        sb = null;
        return result;
    }

}
