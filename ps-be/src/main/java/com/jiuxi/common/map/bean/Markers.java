package com.jiuxi.common.map.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Markers
 * @Description: 标注，为经纬度格式，多个标注之间用竖线隔开，如: lng1,lat1|lng2,lat2|lng3,lat3
 * @Author: Ypp
 * @Date: 2020/7/16 17:01
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class Markers {

    private List<Marker> markers = new ArrayList<>();

    public void addMarker(Marker marker){
        markers.add(marker);
    }


    public String getMarkers(){

        String result = "";

        StringBuffer sb = new StringBuffer();
        for (Marker marker : markers){
            sb.append(marker.getMarker()).append("|");
        }

        if (sb.length() > 0){
            result = sb.substring(0, sb.length() - 1);
        }
        sb = null;
        return result;
    }


    public String getMarkerStyles(){
        String result = "";

        StringBuffer sb = new StringBuffer();
        for (Marker marker : markers){
            sb.append(marker.getMarkerStyle()).append("|");
        }

        if (sb.length() > 0){
            result = sb.substring(0, sb.length() - 1);
        }
        sb = null;

        return result;
    }


}
