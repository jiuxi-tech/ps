package com.jiuxi.platform.captcha.domain.valueobject;

/**
 * 验证码坐标值对象
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class CaptchaCoordinate {
    
    private final Integer x;
    private final Integer y;
    
    public CaptchaCoordinate(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * 计算与另一个坐标的距离
     */
    public double distanceTo(CaptchaCoordinate other) {
        if (other == null) {
            return Double.MAX_VALUE;
        }
        
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * 检查是否在容差范围内
     */
    public boolean isWithinTolerance(CaptchaCoordinate target, double tolerance) {
        return distanceTo(target) <= tolerance;
    }
    
    /**
     * 只验证X坐标的距离（忽略Y坐标）
     */
    public double distanceToX(CaptchaCoordinate other) {
        if (other == null) {
            return Double.MAX_VALUE;
        }
        
        return Math.abs(this.x - other.x);
    }
    
    /**
     * 检查X坐标是否在容差范围内（忽略Y坐标）
     */
    public boolean isXWithinTolerance(CaptchaCoordinate target, double tolerance) {
        return distanceToX(target) <= tolerance;
    }
    
    /**
     * 创建随机坐标
     */
    public static CaptchaCoordinate random(int maxX, int maxY) {
        int x = (int) (Math.random() * maxX);
        int y = (int) (Math.random() * maxY);
        return new CaptchaCoordinate(x, y);
    }
    
    /**
     * 检查坐标是否有效
     */
    public boolean isValid() {
        return x != null && y != null && x >= 0 && y >= 0;
    }
    
    // Getters
    public Integer getX() { return x; }
    public Integer getY() { return y; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CaptchaCoordinate that = (CaptchaCoordinate) obj;
        return x.equals(that.x) && y.equals(that.y);
    }
    
    @Override
    public int hashCode() {
        return x.hashCode() * 31 + y.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("CaptchaCoordinate{x=%d, y=%d}", x, y);
    }
}