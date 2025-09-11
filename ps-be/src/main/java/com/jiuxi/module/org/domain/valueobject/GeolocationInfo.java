package com.jiuxi.module.org.domain.valueobject;

import java.util.Objects;

/**
 * 地理位置信息值对象
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class GeolocationInfo {
    
    /**
     * 经度
     */
    private final String longitude;
    
    /**
     * 纬度
     */
    private final String latitude;
    
    /**
     * 地理哈希码
     */
    private final String geoHashCode;
    
    /**
     * 地址
     */
    private final String address;
    
    /**
     * 行政区划代码
     */
    private final String areaCode;
    
    public GeolocationInfo(String longitude, String latitude) {
        this(longitude, latitude, null, null, null);
    }
    
    public GeolocationInfo(String longitude, String latitude, String geoHashCode, String address, String areaCode) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.geoHashCode = geoHashCode;
        this.address = address;
        this.areaCode = areaCode;
    }
    
    /**
     * 验证地理位置信息是否有效
     */
    public boolean isValid() {
        return isValidLongitude() && isValidLatitude();
    }
    
    /**
     * 验证经度是否有效
     */
    public boolean isValidLongitude() {
        if (longitude == null || longitude.trim().isEmpty()) {
            return false;
        }
        try {
            double lon = Double.parseDouble(longitude);
            return lon >= -180.0 && lon <= 180.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 验证纬度是否有效
     */
    public boolean isValidLatitude() {
        if (latitude == null || latitude.trim().isEmpty()) {
            return false;
        }
        try {
            double lat = Double.parseDouble(latitude);
            return lat >= -90.0 && lat <= 90.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 计算与另一个地理位置的距离（简单的直线距离，单位：公里）
     */
    public double distanceTo(GeolocationInfo other) {
        if (!this.isValid() || !other.isValid()) {
            throw new IllegalArgumentException("地理位置信息无效");
        }
        
        double lon1 = Double.parseDouble(this.longitude);
        double lat1 = Double.parseDouble(this.latitude);
        double lon2 = Double.parseDouble(other.longitude);
        double lat2 = Double.parseDouble(other.latitude);
        
        // 使用Haversine公式计算球面距离
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double R = 6371; // 地球半径（公里）
        
        return R * c;
    }
    
    public String getLongitude() {
        return longitude;
    }
    
    public String getLatitude() {
        return latitude;
    }
    
    public String getGeoHashCode() {
        return geoHashCode;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getAreaCode() {
        return areaCode;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeolocationInfo that = (GeolocationInfo) o;
        return Objects.equals(longitude, that.longitude) &&
               Objects.equals(latitude, that.latitude) &&
               Objects.equals(geoHashCode, that.geoHashCode) &&
               Objects.equals(address, that.address) &&
               Objects.equals(areaCode, that.areaCode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude, geoHashCode, address, areaCode);
    }
    
    @Override
    public String toString() {
        return "GeolocationInfo{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", address='" + address + '\'' +
                ", areaCode='" + areaCode + '\'' +
                '}';
    }
}