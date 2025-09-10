package com.jiuxi.module.user.domain.entity;

import java.util.Objects;

/**
 * 联系信息值对象
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class ContactInfo {
    
    /**
     * 手机号码
     */
    private String phone;
    
    /**
     * 邮箱地址
     */
    private String email;
    
    /**
     * 固定电话
     */
    private String tel;
    
    /**
     * 通信地址
     */
    private String address;
    
    // 构造器
    public ContactInfo() {
    }
    
    public ContactInfo(String phone, String email, String tel) {
        this.phone = phone;
        this.email = email;
        this.tel = tel;
    }
    
    public ContactInfo(String phone, String email, String tel, String address) {
        this.phone = phone;
        this.email = email;
        this.tel = tel;
        this.address = address;
    }
    
    /**
     * 检查手机号是否有效
     */
    public boolean isValidPhone() {
        return phone != null && phone.matches("^1[3-9]\\d{9}$");
    }
    
    /**
     * 检查邮箱是否有效
     */
    public boolean isValidEmail() {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * 获取脱敏手机号
     */
    public String getMaskedPhone() {
        if (phone == null || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
    
    /**
     * 获取脱敏邮箱
     */
    public String getMaskedEmail() {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String localPart = parts[0];
        if (localPart.length() <= 2) {
            return localPart + "***@" + parts[1];
        }
        return localPart.substring(0, 2) + "***@" + parts[1];
    }
    
    // Getters and Setters
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTel() {
        return tel;
    }
    
    public void setTel(String tel) {
        this.tel = tel;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactInfo that = (ContactInfo) o;
        return Objects.equals(phone, that.phone) &&
                Objects.equals(email, that.email) &&
                Objects.equals(tel, that.tel) &&
                Objects.equals(address, that.address);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(phone, email, tel, address);
    }
    
    @Override
    public String toString() {
        return "ContactInfo{" +
                "phone='" + getMaskedPhone() + '\'' +
                ", email='" + getMaskedEmail() + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}