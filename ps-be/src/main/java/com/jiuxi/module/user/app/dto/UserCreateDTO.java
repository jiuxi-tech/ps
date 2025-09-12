package com.jiuxi.module.user.app.dto;

import com.jiuxi.shared.common.validation.annotations.Phone;
import com.jiuxi.shared.common.validation.annotations.IdCard;
import com.jiuxi.shared.common.validation.groups.AddGroup;
import com.jiuxi.shared.common.validation.groups.UpdateGroup;

import javax.validation.constraints.*;
import javax.validation.constraints.Email;

/**
 * 用户创建请求DTO
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class UserCreateDTO {
    
    @NotBlank(message = "部门ID不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String deptId;
    
    @NotBlank(message = "用户姓名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(max = 50, message = "用户姓名长度不能超过50个字符", groups = {AddGroup.class, UpdateGroup.class})
    private String personName;
    
    @Phone(message = "手机号格式不正确", groups = {AddGroup.class, UpdateGroup.class})
    private String phone;
    
    @Email(message = "邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
    private String email;
    
    private String tel;
    
    private String personNo;
    
    private Integer sex;
    
    private String idType;
    
    @IdCard(message = "身份证号格式不正确", allowEmpty = true, groups = {AddGroup.class, UpdateGroup.class})
    private String idCard;
    
    private String nativePlace;
    
    private String nation;
    
    private String resume;
    
    private String birthday;
    
    private String office;
    
    private String profilePhoto;
    
    private String category;
    
    // 账户信息
    @NotBlank(message = "用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间", groups = {AddGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$", message = "用户名只能包含字母、数字、下划线和中文", groups = {AddGroup.class, UpdateGroup.class})
    private String username;

    @NotBlank(message = "密码不能为空", groups = {AddGroup.class})
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间", groups = {AddGroup.class})
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{6,}$", 
             message = "密码必须包含至少一个小写字母、一个大写字母和一个数字", 
             groups = {AddGroup.class})
    private String password;
    
    // Getters and Setters
    public String getDeptId() {
        return deptId;
    }
    
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    
    public String getPersonName() {
        return personName;
    }
    
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
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
    
    public String getOffice() {
        return office;
    }
    
    public void setOffice(String office) {
        this.office = office;
    }
    
    public String getProfilePhoto() {
        return profilePhoto;
    }
    
    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}