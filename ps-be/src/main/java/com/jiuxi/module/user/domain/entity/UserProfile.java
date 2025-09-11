package com.jiuxi.module.user.domain.entity;

import com.jiuxi.module.user.domain.valueobject.UserName;
import com.jiuxi.module.user.domain.valueobject.Email;
import com.jiuxi.module.user.domain.valueobject.PhoneNumber;

import java.util.Objects;

/**
 * 用户资料值对象
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class UserProfile {
    
    /**
     * 用户姓名
     */
    private UserName personName;
    
    /**
     * 头像地址
     */
    private String profilePhoto;
    
    /**
     * 用户编号
     */
    private String personNo;
    
    /**
     * 性别 (0-保密, 1-男, 2-女)
     */
    private Integer sex;
    
    /**
     * 证件类型
     */
    private String idType;
    
    /**
     * 证件号码
     */
    private String idCard;
    
    /**
     * 籍贯
     */
    private String nativePlace;
    
    /**
     * 民族
     */
    private String nation;
    
    /**
     * 个人简介
     */
    private String resume;
    
    /**
     * 出生日期
     */
    private String birthday;
    
    /**
     * 联系方式
     */
    private ContactInfo contactInfo;
    
    /**
     * 办公室
     */
    private String office;
    
    // 构造器
    public UserProfile() {
    }
    
    public UserProfile(String personName, String phone) {
        this.personName = UserName.of(personName);
        this.contactInfo = new ContactInfo(phone, null, null);
    }
    
    public UserProfile(String personName, String phone, String email, String tel) {
        this.personName = UserName.of(personName);
        this.contactInfo = new ContactInfo(phone, email, tel);
    }
    
    /**
     * 更新联系方式
     */
    public void updateContactInfo(String phone, String email, String tel) {
        this.contactInfo = new ContactInfo(phone, email, tel);
    }
    
    /**
     * 检查是否有效用户名
     */
    public boolean isValidName() {
        return personName != null && personName.isValid();
    }
    
    /**
     * 获取性别描述
     */
    public String getSexDescription() {
        if (sex == null) return "未知";
        switch (sex) {
            case 0: return "保密";
            case 1: return "男";
            case 2: return "女";
            default: return "未知";
        }
    }
    
    // Getters and Setters
    public String getPersonName() {
        return personName != null ? personName.getValue() : null;
    }
    
    public void setPersonName(String personName) {
        this.personName = personName != null ? UserName.of(personName) : null;
    }
    
    public UserName getPersonNameValue() {
        return personName;
    }
    
    public void setPersonNameValue(UserName personName) {
        this.personName = personName;
    }
    
    public String getProfilePhoto() {
        return profilePhoto;
    }
    
    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
    
    public String getPersonNo() {
        return personNo;
    }
    
    public void setPersonNo(String personNo) {
        this.personNo = personNo;
    }
    
    public Integer getSex() {
        return sex;
    }
    
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    
    public String getIdType() {
        return idType;
    }
    
    public void setIdType(String idType) {
        this.idType = idType;
    }
    
    public String getIdCard() {
        return idCard;
    }
    
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    
    public String getNativePlace() {
        return nativePlace;
    }
    
    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }
    
    public String getNation() {
        return nation;
    }
    
    public void setNation(String nation) {
        this.nation = nation;
    }
    
    public String getResume() {
        return resume;
    }
    
    public void setResume(String resume) {
        this.resume = resume;
    }
    
    public String getBirthday() {
        return birthday;
    }
    
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    public ContactInfo getContactInfo() {
        return contactInfo;
    }
    
    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    public String getOffice() {
        return office;
    }
    
    public void setOffice(String office) {
        this.office = office;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(personName, that.personName) &&
                Objects.equals(idCard, that.idCard);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(personName, idCard);
    }
    
    @Override
    public String toString() {
        return "UserProfile{" +
                "personName='" + personName + '\'' +
                ", personNo='" + personNo + '\'' +
                ", sex=" + sex +
                ", contactInfo=" + contactInfo +
                '}';
    }
}