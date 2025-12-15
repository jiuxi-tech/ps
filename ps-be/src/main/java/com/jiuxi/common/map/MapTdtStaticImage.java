package com.jiuxi.common.map;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.jiuxi.common.map.bean.Center;
import com.jiuxi.common.map.bean.Markers;
import com.jiuxi.common.map.bean.Paths;
import org.apache.commons.lang3.Validate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * @ClassName: MapTdtStaticImage
 * @Description: 天地图 - 静态图片工具类
 * @Author: Ypp
 * @Date: 2020/7/16 16:43
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 *
 *   官网API： http://lbs.tianditu.gov.cn/staticapi/static.html
 *   使用示例：
 *         String rootDir = "d:/soft";
 *
 *         String tk = "4d8c4557d89a899def2eea0108fa41d5";
 *         int width = 800;
 *         int height = 800;
 *         int zoom = 11;
 *         Center center = new Center("116.40","39.93");
 *         String layers = "vec_c,cva_c";
 *
 *         // 标点
 *         Marker marker3 = new Marker("116.4551", "39.90267", "-1", "http://lbsyun.baidu.com/jsdemo/img/fox.gif");
 *         Marker marker = new Marker("116.34867", "39.94593");
 *         Marker marker2 = new Marker("116.42626", "39.94731");
 *
 *         Markers markers = new Markers();
 *         markers.addMarker(marker);
 *         markers.addMarker(marker2);
 *         markers.addMarker(marker3);
 *
 *         Point point = new Point("116.34867", "39.94593");
 *         Point point2 = new Point("116.34249", "39.87178");
 *         Point point3 = new Point("116.32807", "39.90748");
 *         Point point4 = new Point("116.34867", "39.94593");
 *
 *         // 多个折线
 *         Paths paths = new Paths();
 *
 *         // 单个折线
 *         Path path = new Path();
 *         // 设置折线的点
 *         path.addPoint(point);
 *         path.addPoint(point2);
 *         path.addPoint(point3);
 *         path.addPoint(point4);
 *
 *         // 设置折线的样式
 *         path.setColor("0x0000ff");
 *
 *         paths.addPath(path);
 *
 *         String dir =  MapTdtStaticImage.staticImage(rootDir, tk, width, height, zoom, layers, center,  markers, paths);
 *
 *
 */
public class MapTdtStaticImage {

    private static String STATICIMAGE_URL = "http://api.tianditu.gov.cn/staticimage?center=CENTER&width=WIDTH&height=HEIGHT&zoom=ZOOM&layers=LAYERS&markers=MARKERS&markerStyles=MSTYLES&paths=PATHS&pathStyles=PSTYLES&tk=TK";

    /**
     * @description: 静态地图 , URL请求长度为2048， 点标记数量26个
     * @author Ypp
     * @date 2020/7/17 13:07
     * @param rootDir 图片下载的跟路径
     * @param tk 您的密钥
     * @param width 图片宽度 取值范围：[1, 1024]。
     * @param height 图片高度 取值范围：[1, 1024]。
     * @param zoom 地图级别,取值范围[3,18]。
     * @param layers 表示静态图叠加层的类型,  img_c--影像图,vec_c--矢量底图，ter_c--地形图， cva_c--中文注记，eva_c--英文注记，cta_c--地形注记。如：vec_c,cva_c 或 img_c,cva_c,cta_c
     * @param center 地图中心点位置
     * @param markers 标注
     * @param paths 折线
     * @return 图片的路径
     */
    public static String staticImage(String rootDir, String tk, int width, int height, int zoom, String layers, Center center, Markers markers, Paths paths) {

        Validate.notBlank(rootDir, "密钥不能为空");
        Validate.notBlank(tk, "密钥不能为空");
        Validate.notNull(center, "地图中心点位置不能为空");
        Validate.inclusiveBetween(1,1024, width, "图片宽度 取值范围：[1, 1024]");
        Validate.inclusiveBetween(1,1024, height, "图片高度 取值范围：[1, 1024]");
        Validate.inclusiveBetween(3,18, zoom, "地图级别,取值范围[3,18]");

        // 如果 Optional 中有值则将其返回，否则返回 orElse 方法传入的参数。
        layers = Optional.ofNullable(layers).orElse("vec_c,cva_c");
        markers = Optional.ofNullable(markers).orElse(new Markers());
        paths = Optional.ofNullable(paths).orElse(new Paths());

        String fileDir = null;

        try {
            String url = STATICIMAGE_URL.replaceAll("TK", tk).replaceAll("CENTER", center.getCenter())
                    .replaceAll("ZOOM", zoom+"") .replaceAll("LAYERS", layers)
                    .replaceAll("WIDTH", width+"").replaceAll("HEIGHT", height+"")
                    .replaceAll("MARKERS", URLEncoder.encode(markers.getMarkers(), "UTF-8")).replaceAll("MSTYLES", URLEncoder.encode(markers.getMarkerStyles(), "UTF-8"))
                    .replaceAll("PATHS", URLEncoder.encode(paths.getPaths(), "UTF-8")).replaceAll("PSTYLES", URLEncoder.encode(paths.getPathStyles(), "UTF-8"));

            // 生成图片的保持路径
            String fileName = IdUtil.simpleUUID() + ".jpg";

            DateTime now = DateTime.now();
            String yyyyMM =  now.toString("yyyyMM");
            // 生成分片文件下载目录
            fileDir = rootDir + "/" + yyyyMM + "/" + fileName;

            System.out.println(url);

            // 将文件下载后保存，返回结果为下载文件大小
            HttpUtil.downloadFile(url, FileUtil.file(fileDir));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fileDir;
    }

}
