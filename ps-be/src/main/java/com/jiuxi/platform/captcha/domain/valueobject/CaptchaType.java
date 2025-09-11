package com.jiuxi.platform.captcha.domain.valueobject;

/**
 * 验证码类型值对象
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public enum CaptchaType {
    
    /**
     * 拼接验证码 - 将图片拼接完整
     */
    CONCAT("concat", "拼接验证码", "将打乱的图片片段拼接成完整图片"),
    
    /**
     * 旋转验证码 - 旋转图片到正确角度
     */
    ROTATE("rotate", "旋转验证码", "将旋转的图片转回正确角度"),
    
    /**
     * 滑块验证码 - 滑动拼图到正确位置
     */
    SLIDER("slider", "滑块验证码", "拖拽滑块使拼图块与缺口完全重合"),
    
    /**
     * 点击验证码 - 按顺序点击指定位置
     */
    CLICK("click", "点击验证码", "按照提示顺序点击图片中的指定位置");
    
    private final String code;
    private final String name;
    private final String description;
    
    CaptchaType(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    /**
     * 根据代码获取验证码类型
     */
    public static CaptchaType fromCode(String code) {
        for (CaptchaType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的验证码类型: " + code);
    }
    
    /**
     * 检查是否为图片验证码
     */
    public boolean isImageCaptcha() {
        return this == CONCAT || this == ROTATE || this == SLIDER || this == CLICK;
    }
    
    /**
     * 获取默认容差（像素）
     */
    public double getDefaultTolerance() {
        switch (this) {
            case CONCAT:
                return 10.0;
            case ROTATE:
                return 15.0;
            case SLIDER:
                return 8.0;
            case CLICK:
                return 12.0;
            default:
                return 10.0;
        }
    }
    
    /**
     * 获取默认过期时间（秒）
     */
    public int getDefaultExpirationSeconds() {
        return 300; // 5分钟
    }
    
    // Getters
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    
    @Override
    public String toString() {
        return String.format("CaptchaType{code='%s', name='%s'}", code, name);
    }
}