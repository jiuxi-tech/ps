package com.jiuxi.module.org.domain.valueobject;

import java.util.Objects;

/**
 * 联系信息值对象
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class ContactInfo {
    
    /**
     * 联系人姓名
     */
    private final String contactName;
    
    /**
     * 联系电话
     */
    private final String contactPhone;
    
    /**
     * 传真
     */
    private final String fax;
    
    /**
     * 邮箱
     */
    private final String email;
    
    /**
     * 地址
     */
    private final String address;
    
    public ContactInfo(String contactName, String contactPhone) {
        this(contactName, contactPhone, null, null, null);
    }
    
    public ContactInfo(String contactName, String contactPhone, String fax, String email, String address) {
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.fax = fax;
        this.email = email;
        this.address = address;
    }
    
    /**
     * 验证联系信息是否有效
     */
    public boolean isValid() {
        return contactName != null && !contactName.trim().isEmpty() &&
               contactPhone != null && !contactPhone.trim().isEmpty();
    }
    
    /**
     * 验证电话格式
     */
    public boolean isValidPhone() {
        if (contactPhone == null || contactPhone.trim().isEmpty()) {
            return false;
        }
        // 简单的电话格式验证
        return contactPhone.matches("^[0-9-+()\\s]+$");
    }
    
    /**
     * 验证邮箱格式
     */
    public boolean isValidEmail() {
        if (email == null || email.trim().isEmpty()) {
            return true; // 邮箱不是必填项
        }
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }
    
    public String getContactName() {
        return contactName;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public String getFax() {
        return fax;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getAddress() {
        return address;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactInfo that = (ContactInfo) o;
        return Objects.equals(contactName, that.contactName) &&
               Objects.equals(contactPhone, that.contactPhone) &&
               Objects.equals(fax, that.fax) &&
               Objects.equals(email, that.email) &&
               Objects.equals(address, that.address);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(contactName, contactPhone, fax, email, address);
    }
    
    @Override
    public String toString() {
        return "ContactInfo{" +
                "contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}