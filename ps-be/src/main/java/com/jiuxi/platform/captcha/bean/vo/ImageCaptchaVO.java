package com.jiuxi.platform.captcha.bean.vo;

/**
 * 图片验证码VO
 * 
 * @author DDD Refactor
 * @date 2025-09-14
 */
public class ImageCaptchaVO {
    
    private String clientUuid;
    private String backgroundImage;
    private String puzzleImage;
    private String sliderImage;
    private String type;
    private Integer bgImageWidth;
    private Integer bgImageHeight;
    private Integer randomX;
    private Integer randomY;
    private Integer sliderImageWidth;
    private Integer sliderImageHeight;
    
    // Getters and Setters
    
    public String getClientUuid() {
        return clientUuid;
    }
    
    public void setClientUuid(String clientUuid) {
        this.clientUuid = clientUuid;
    }
    
    public String getBackgroundImage() {
        return backgroundImage;
    }
    
    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
    
    public String getPuzzleImage() {
        return puzzleImage;
    }
    
    public void setPuzzleImage(String puzzleImage) {
        this.puzzleImage = puzzleImage;
    }
    
    public String getSliderImage() {
        return sliderImage;
    }
    
    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Integer getBgImageWidth() {
        return bgImageWidth;
    }
    
    public void setBgImageWidth(Integer bgImageWidth) {
        this.bgImageWidth = bgImageWidth;
    }
    
    public Integer getBgImageHeight() {
        return bgImageHeight;
    }
    
    public void setBgImageHeight(Integer bgImageHeight) {
        this.bgImageHeight = bgImageHeight;
    }
    
    public Integer getRandomX() {
        return randomX;
    }
    
    public void setRandomX(Integer randomX) {
        this.randomX = randomX;
    }
    
    public Integer getRandomY() {
        return randomY;
    }
    
    public void setRandomY(Integer randomY) {
        this.randomY = randomY;
    }
    
    public Integer getSliderImageWidth() {
        return sliderImageWidth;
    }
    
    public void setSliderImageWidth(Integer sliderImageWidth) {
        this.sliderImageWidth = sliderImageWidth;
    }
    
    public Integer getSliderImageHeight() {
        return sliderImageHeight;
    }
    
    public void setSliderImageHeight(Integer sliderImageHeight) {
        this.sliderImageHeight = sliderImageHeight;
    }
}