package com.jiuxi.admin.core.bean.vo;

import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 人员基本信息表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:18
 */
public class TpPersonBasicinfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "部门id不能为空", groups = AddGroup.class)
    private String deptId;
    private String deptSimpleName;
    private String deptFullName;

    /**
     * 人员id
     */
    @NotBlank(message = "人员id不能为空", groups = UpdateGroup.class)
    private String personId;
    /**
     * 人员姓名
     */
    @NotBlank(message = "人员姓名不能为空", groups = AddGroup.class)
    private String personName;
    /**
     * 头像地址
     */
    private String profilePhoto;
    /**
     * 人员编号
     */
    private String personNo;
    /**
     * 性别
     */
    private Integer sex;
    private String sexName;
    /**
     * 证照类型H38
     */
    private String idtype;
    private String idtypeName;
    /**
     * 证照值
     */
    private String idcard;
    /**
     * 籍贯
     */
    private String nativePlace;
    private String nativePlaceName;
    /**
     * 民族 字典编码：H51
     */
    private String safeprinNation;
    private String safeprinNationName;
    /**
     * 个人简介
     */
    private String resume;
    /**
     * 出生年月
     */
    private String birthday;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 固定号码
     */
    private String tel;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 职位
     * @deprecated 已废弃，请使用 zwzj 字段
     */
    @Deprecated
    private String office;
    /**
     * 职称 字典编码SYS12
     * @deprecated 已废弃，请使用 zhicheng 字段
     */
    @Deprecated
    private String titleCode;
    /**
     * 职务职级（新字段，推荐使用）
     */
    private String zwzj;
    /**
     * 职称（新字段，推荐使用）
     */
    private String zhicheng;
    /**
     * 参加工作时间
     */
    private String partWorkDate;
    /**
     * 是否有效
     */
    private Integer actived;
    /**
     * 人员类别 政府人员or企业人员or其他 0政府 1企业 2其他
     */
    private Integer category;
    /**
     * 所属机构（单位）id 政府存机构ID，企业存单位id，存在分公司的，存所在分公司单位id
     */
    @NotBlank(message = "机构（单位）id不能为空", groups = AddGroup.class)
    private String ascnId;
    private String ascnName;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改人
     */
    private String updator;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 扩展字段01
     */
    private String extend01;
    /**
     * 扩展字段02
     */
    private String extend02;
    /**
     * 扩展字段03
     */
    private String extend03;

    /**
     * 默认所属部门 （1是所属部门，0是兼职部门）
     */
    private Integer defaultDept;
    private String defaultDeptName;
    /**
     * 数据密钥
     */
    private String passKey;

    /**
     * 兼职部门,多个逗号隔开
     */
    private String deptIds;
    private String deptFullNames;


    private TpAttachinfoRefVO file;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptSimpleName() {
        return deptSimpleName;
    }

    public void setDeptSimpleName(String deptSimpleName) {
        this.deptSimpleName = deptSimpleName;
    }

    public String getDeptFullName() {
        return deptFullName;
    }

    public void setDeptFullName(String deptFullName) {
        this.deptFullName = deptFullName;
    }

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

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getIdtypeName() {
        return idtypeName;
    }

    public void setIdtypeName(String idtypeName) {
        this.idtypeName = idtypeName;
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

    public String getNativePlaceName() {
        return nativePlaceName;
    }

    public void setNativePlaceName(String nativePlaceName) {
        this.nativePlaceName = nativePlaceName;
    }

    public String getSafeprinNation() {
        return safeprinNation;
    }

    public void setSafeprinNation(String safeprinNation) {
        this.safeprinNation = safeprinNation;
    }

    public String getSafeprinNationName() {
        return safeprinNationName;
    }

    public void setSafeprinNationName(String safeprinNationName) {
        this.safeprinNationName = safeprinNationName;
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

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public String getZwzj() {
        return zwzj;
    }

    public void setZwzj(String zwzj) {
        this.zwzj = zwzj;
    }

    public String getZhicheng() {
        return zhicheng;
    }

    public void setZhicheng(String zhicheng) {
        this.zhicheng = zhicheng;
    }

    public Integer getActived() {
        return actived;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getAscnId() {
        return ascnId;
    }

    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }

    public String getAscnName() {
        return ascnName;
    }

    public void setAscnName(String ascnName) {
        this.ascnName = ascnName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
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

    public Integer getDefaultDept() {
        return defaultDept;
    }

    public void setDefaultDept(Integer defaultDept) {
        this.defaultDept = defaultDept;
    }

    public String getDefaultDeptName() {
        return defaultDeptName;
    }

    public void setDefaultDeptName(String defaultDeptName) {
        this.defaultDeptName = defaultDeptName;
    }

    public String getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }

    public String getDeptFullNames() {
        return deptFullNames;
    }

    public void setDeptFullNames(String deptFullNames) {
        this.deptFullNames = deptFullNames;
    }

    public TpAttachinfoRefVO getFile() {
        return file;
    }

    public void setFile(TpAttachinfoRefVO file) {
        this.file = file;
    }

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }

    public String getPartWorkDate() {
        return partWorkDate;
    }

    public void setPartWorkDate(String partWorkDate) {
        this.partWorkDate = partWorkDate;
    }
}
