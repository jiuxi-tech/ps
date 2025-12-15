package com.jiuxi.common.geo;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceCalculator;
import org.locationtech.spatial4j.io.GeohashUtils;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.SpatialRelation;

import java.awt.geom.Point2D;
import java.util.List;

import static org.locationtech.spatial4j.distance.DistanceUtils.DEG_TO_KM;
import static org.locationtech.spatial4j.distance.DistanceUtils.KM_TO_DEG;

/**
 * 距离资料： https://github.com/locationtech/spatial4j/blob/master/src/test/java/org/locationtech/spatial4j/distance/TestDistances.java
 *
 * @ClassName: Spatial4jDistancesUtils
 * @Description: Spatial4j 距离的封装 : API: https://locationtech.github.io/spatial4j/apidocs/
 * @Author: Ypp
 * @Date: 2020/5/29 15:51
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class GeoUtils {


    private GeoUtils() {
    }

    private static class Spatial4jDistances {
        private static final SpatialContext ctx = SpatialContext.GEO;
    }

    private static SpatialContext getContextInstance() {
        return Spatial4jDistances.ctx;
    }

    /**
     * SpatialContext 上下文
     */
    private static SpatialContext ctx;

    static {
        ctx = getContextInstance();
    }


    /**
     * @description: 计算两点之间的距离
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @return double
     * @author Ypp
     * @date 2020/5/29 16:53
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2) {

        Point p1 = pLL(lat1, lon1);
        Point p2 = pLL(lat2, lon2);
        return distance(p1, p2);
    }

    /**
     * @description: 计算两点之间的距离
     * @param point1 第一个点
     * @param point2 第二个点
     * @return double
     * @author Ypp
     * @date 2020/5/29 17:45
     */
    public static double distance(Point point1, Point point2) {

        double deg = distanceCalculator().distance(point1, point2);

        // 返回距离，单位 KM
        double degKm = deg * DEG_TO_KM;
        return degKm;
    }

    /**
     * @description: 根据经纬度构建坐标的点 Point
     * @param lat 纬度
     * @param lon 经度
     * @return org.locationtech.spatial4j.shape.Point
     * @author Ypp
     * @date 2020/5/29 16:47
     */
    public static Point pLL(double lat, double lon) {
        //return ctx.getShapeFactory().pointLatLon(lat, lon);
        return ctx.getShapeFactory().pointXY(lon, lat);
    }


    /**
     * @description: 判断一个点是否在另一个点位中心，radius为半径的矩形内
     * @param lat1   第一个点的纬度
     * @param lon1   第一个点的经度
     * @param lat2   第二个点的纬度
     * @param lon2   第二个点的经度
     * @param radius 半径，单位：千米
     * @return boolean
     * @author Ypp
     * @date 2020/5/29 17:51
     */
    public static boolean calcBoxContainsByDistFromPt(double lat1, double lon1, double lat2, double lon2, double radius) {

        double d = radius * KM_TO_DEG;
        // 两个坐标点
        Point point1 = pLL(lat1, lon1);
        Point point2 = pLL(lat2, lon2);

        return calcBoxContainsByDistFromPt(point1, point2, d);
    }


    /**
     * @description: 判断一个点是否在另一个点位中心，radius为半径的矩形内
     * @param point1 第一个点
     * @param point2 第二个点
     * @param radius 半径，单位：千米
     * @return boolean
     * @author Ypp
     * @date 2020/5/29 17:51
     */
    public static boolean calcBoxContainsByDistFromPt(Point point1, Point point2, double radius) {

        double d = radius * KM_TO_DEG;

        // 根据第一个点和半径，画出区域
        Rectangle r = distanceCalculator().calcBoxByDistFromPt(point1, d, ctx, null);
        SpatialRelation spatialRelation = r.relate(point2);
        // 判断 point2点十分在边界框内
        if (SpatialRelation.CONTAINS.equals(spatialRelation)) {
            return true;
        }
        return false;
    }


    /**
     * @description: 当给定一个Point(x, y)和距离时如何计算四角点的坐标
     * @param lat    纬度
     * @param lon    经度
     * @param radius
     * @return org.locationtech.spatial4j.shape.Rectangle
     * <p>
     * r.getMinY() - r.getMinX(); 左下角
     * r.getMaxY(); - r.getMaxX(); 右上角
     * @author Ypp
     * @date 2020/5/29 18:31
     */
    public static Rectangle calcBoxByDistFromPt(double lat, double lon, double radius) {

        double d = radius * KM_TO_DEG;
        Point pointCenter = pLL(lat, lon);

        return calcBoxByDistFromPt(pointCenter, d);
    }

    /**
     * @description: 当给定一个Point(x, y)和距离时如何计算四角点的坐标
     * @param pointCenter
     * @param radius
     * @return org.locationtech.spatial4j.shape.Rectangle
     * <p>
     * r.getMinY() - r.getMinX(); 左下角
     * r.getMaxY(); - r.getMaxX(); 右上角
     * @author Ypp
     * @date 2020/5/29 18:31
     */
    public static Rectangle calcBoxByDistFromPt(Point pointCenter, double radius) {
        double d = radius * KM_TO_DEG;
        // 根据第一个点和半径，画出区域
        Rectangle r = distanceCalculator().calcBoxByDistFromPt(pointCenter, d, ctx, null);

        return r;
    }


    /**
     * @description: 距离计算器, 默认： Haversine公式
     * @param
     * @return org.locationtech.spatial4j.distance.DistanceCalculator
     * @author Ypp
     * @date 2020/5/29 16:49
     */
    private static DistanceCalculator distanceCalculator() {

        // Haversine：如果在地球的同一侧，则精确到一厘米， 否则可能相距1m（对映）
        //DistanceCalculator haversine = new GeodesicSphereDistCalc.Haversine();

        // vincenty公式  精度很高能达到0.5毫米，但是很慢。
        //DistanceCalculator vincenty = new GeodesicSphereDistCalc.Vincenty();

        // LawOfCosines：精确到1米以内（或更好？）
        //DistanceCalculator lawOfCos = new GeodesicSphereDistCalc.LawOfCosines();

        // 默认用的是 Haversine 算法，
        return ctx.getDistCalc();
    }


    /**
     * @description: geohash
     * @author Ypp
     * @date 2020/6/1 11:12
     * @param lat
     * @param lon
     * @return java.lang.String
     */
    public static String encodeLatLon(double lat, double lon) {
        String geoCode = GeohashUtils.encodeLatLon(lat, lon);
        return geoCode;
    }


    /**    
     * @description: geohash
     * @author Ypp
     * @date 2020/6/1 11:17
     * @param lat
     * @param lon
     * @param geohashLength 范围在 1 ~ 12 之间
     * @return java.lang.String
     *
     * geohash码长度	宽度	高度
     * 1	5,009.4km	4,992.6km
     * 2	1,252.3km	624.1km
     * 3	156.5km	156km
     * 4	39.1km	19.5km
     * 5	4.9km	4.9km
     * 6	1.2km	609.4m
     * 7	152.9m	152.4m
     * 8	38.2m	19m
     * 9	4.8m	4.8m
     * 10	1.2m	59.5cm
     * 11	14.9cm	14.9cm
     * 12	3.7cm	1.9cm
     */
    public static String encodeLatLon(double lat, double lon, int geohashLength) {
        String geoCode = GeohashUtils.encodeLatLon(lat, lon, geohashLength);
        return geoCode;
    }

    /**
     * @description: 根据geohash解析出对应的坐标
     * @author Ypp
     * @date 2020/6/1 11:20
     * @param geohash
     * @return org.locationtech.spatial4j.shape.Point
     */
    public static Point decode(String geohash) {
        Point point = GeohashUtils.decode(geohash, ctx);
        return point;
    }

    /**
     * 判断点是否在多边形内
     * @author Ypp
     * @date 2021/6/29 11:11
     * @param point  测试点
     * @param polygon 多边形的点
     * @return boolean
     *
     *  <pre>
     *  Point2D.Double point = new Point2D.Double(116.382640,39.910260);
     *
     *  List<Point2D.Double> polygon = new ArrayList<>();
     *  polygon.add(new Point2D.Double(116.41136, 39.97569));
     *  polygon.add(new Point2D.Double(116.411794, 39.9068));
     *  polygon.add(new Point2D.Double(116.32969, 39.92940));
     *  polygon.add(new Point2D.Double(116.385438, 39.90610));
     *
     *  boolean bool = isInPolygonGeneralPath(point, polygon);
     *  <pre/>
     */
    public static boolean isInPolygon(Point2D.Double point, List<Point2D.Double> polygon) {

        java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();

        Point2D.Double first = polygon.get(0);
        p.moveTo(first.x, first.y);
        polygon.remove(0);

        for (Point2D.Double d : polygon) {
            p.lineTo(d.x, d.y);
        }

        p.lineTo(first.x, first.y);
        p.closePath();

        return p.contains(point);
    }

}
