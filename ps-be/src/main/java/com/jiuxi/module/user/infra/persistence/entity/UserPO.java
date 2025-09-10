package com.jiuxi.module.user.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 用户基本信息持久化对象
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@TableName("tp_person_basicinfo")
public class UserPO {
    
    @TableId
    private String personId;
    private String personName;
    private String profilePhoto;
    private String personNo;
    private Integer sex;
    private String idtype;
    private String idcard;
    private String nativePlace;
    private String safeprinNation;
    private String resume;
    private String birthday;
    private String phone;
    private String tel;
    private String email;
    private String office;
    private Integer actived;
    private String category;
    private String ascnId;
    private String creator;
    private LocalDateTime createTime;
    private String updator;
    private LocalDateTime updateTime;
    private String tenantId;
    private String extend01;
    private String extend02;
    private String extend03;
    
    // Getters and Setters
    public String getPersonId() {
        return personId;
    }
    
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public String getPersonName() {
        return personName;
    }
    
    public void setPersonName(String personName) {
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
    
    public String getIdtype() {
        return idtype;
    }
    
    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }
    
    public String getIdcard() {
        return idcard;
    }
    
    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
    
    public String getNativePlace() {
        return nativePlace;
    }
    
    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }
    
    public String getSafeprinNation() {
        return safeprinNation;
    }
    
    public void setSafeprinNation(String safeprinNation) {
        this.safeprinNation = safeprinNation;
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
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getTel() {
        return tel;
    }
    
    public void setTel(String tel) {
        this.tel = tel;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getOffice() {
        return office;
    }
    
    public void setOffice(String office) {
        this.office = office;
    }
    
    public Integer getActived() {
        return actived;
    }
    
    public void setActived(Integer actived) {
        this.actived = actived;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getAscnId() {
        return ascnId;
    }
    
    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getExtend01() {
        return extend01;
    }
    
    public void setExtend01(String extend01) {
        this.extend01 = extend01;
    }
    
    public String getExtend02() {
        return extend02;
    }
    
    public void setExtend02(String extend02) {
        this.extend02 = extend02;
    }
    
    public String getExtend03() {
        return extend03;
    }
    
    public void setExtend03(String extend03) {
        this.extend03 = extend03;
    }
}