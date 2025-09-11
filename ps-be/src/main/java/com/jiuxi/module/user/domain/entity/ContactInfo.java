package com.jiuxi.module.user.domain.entity;

import com.jiuxi.module.user.domain.valueobject.Email;
import com.jiuxi.module.user.domain.valueobject.PhoneNumber;

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
    private PhoneNumber phone;
    
    /**
     * 邮箱地址
     */
    private Email email;
    
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
        this.phone = phone != null ? PhoneNumber.of(phone) : PhoneNumber.empty();
        this.email = email != null ? Email.of(email) : Email.empty();
        this.tel = tel;
    }
    
    public ContactInfo(String phone, String email, String tel, String address) {
        this.phone = phone != null ? PhoneNumber.of(phone) : PhoneNumber.empty();
        this.email = email != null ? Email.of(email) : Email.empty();
        this.tel = tel;
        this.address = address;
    }
    
    /**
     * 检查手机号是否有效
     */
    public boolean isValidPhone() {
        return phone != null && phone.isValid();
    }
    
    /**
     * 检查邮箱是否有效
     */
    public boolean isValidEmail() {
        return email != null && email.isValid();
    }
    
    /**
     * 获取脱敏手机号
     */
    public String getMaskedPhone() {
        return phone != null ? phone.getMaskedValue() : "";
    }
    
    /**
     * 获取脱敏邮箱
     */
    public String getMaskedEmail() {
        return email != null ? email.getMaskedValue() : "";
    }
    
    // Getters and Setters
    public String getPhone() {
        return phone != null ? phone.getValue() : null;
    }
    
    public void setPhone(String phone) {
        this.phone = phone != null ? PhoneNumber.of(phone) : PhoneNumber.empty();
    }
    
    public PhoneNumber getPhoneValue() {
        return phone;
    }
    
    public void setPhoneValue(PhoneNumber phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email != null ? email.getValue() : null;
    }
    
    public void setEmail(String email) {
        this.email = email != null ? Email.of(email) : Email.empty();
    }
    
    public Email getEmailValue() {
        return email;
    }
    
    public void setEmailValue(Email email) {
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