package com.jiuxi.admin.core.bean.vo;

import com.jiuxi.core.core.validator.group.AddGroup;
import com.jiuxi.core.core.validator.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 人员基本信息扩展表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:17
 */
public class TpPersonExinfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    @NotBlank(message = "用户id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String personId;
    /**
     * 职称 字典编码SYS12
     */
    private String titleCode;
    private String titleCodeName;
    /**
     * 通信地址
     */
    private String maddress;
    /**
     * 网格职务 字典编码：SYS18，注：该字段只对网格人员有效。
     */
    private String gridDuty;
    private String gridDutyName;
    /**
     * 网格职责
     */
    private String gridBurden;
    /**
     * 执法证号
     */
    private String checkcardNo;
    /**
     * 执法证有效期
     */
    private String checkcardLimitdate;
    /**
     * （企业）是否注安师
     */
    private Integer safetyEngineer;
    /**
     * （企业）开始从事时间 安全工程师、
     */
    private String safetyEngineerDate;
    /**
     * （企业）安全生产职务代码 字典编码:X58，企业人员类型
     */
    private String safetyDutyCode;
    private String safetyDutyCodeName;
    /**
     * （企业）安全生产职务是否专职
     */
    private Integer fullJob;
    /**
     * 工号
     */
    private String jobNumber;
    /**
     * 政治面貌 字典编码：SYS13
     */
    private String politicsCode;
    private String politicsCodeName;
    /**
     * 毕业学校
     */
    private String school;
    /**
     * 所学专业
     */
    private String sepcSubject;
    /**
     * 学历 字典编码：SYS14
     */
    private String diplomaCode;
    private String diplomaCodeName;
    /**
     * 最高学位
     */
    private String degree;
    /**
     * 岗位
     */
    private String position;
    /**
     * 参加工作时间
     */
    private String partWorkDate;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 单兵设备地址
     */
    private String soldierUrl;
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
     * 扩展字段04
     */
    private String extend04;
    /**
     * 扩展字段05
     */
    private String extend05;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public String getMaddress() {
        return maddress;
    }

    public void setMaddress(String maddress) {
        this.maddress = maddress;
    }

    public String getGridDuty() {
        return gridDuty;
    }

    public void setGridDuty(String gridDuty) {
        this.gridDuty = gridDuty;
    }

    public String getGridBurden() {
        return gridBurden;
    }

    public void setGridBurden(String gridBurden) {
        this.gridBurden = gridBurden;
    }

    public String getCheckcardNo() {
        return checkcardNo;
    }

    public void setCheckcardNo(String checkcardNo) {
        this.checkcardNo = checkcardNo;
    }

    public String getCheckcardLimitdate() {
        return checkcardLimitdate;
    }

    public void setCheckcardLimitdate(String checkcardLimitdate) {
        this.checkcardLimitdate = checkcardLimitdate;
    }

    public String getSafetyEngineerDate() {
        return safetyEngineerDate;
    }

    public void setSafetyEngineerDate(String safetyEngineerDate) {
        this.safetyEngineerDate = safetyEngineerDate;
    }

    public String getSafetyDutyCode() {
        return safetyDutyCode;
    }

    public void setSafetyDutyCode(String safetyDutyCode) {
        this.safetyDutyCode = safetyDutyCode;
    }

    public Integer getSafetyEngineer() {
        return safetyEngineer;
    }

    public void setSafetyEngineer(Integer safetyEngineer) {
        this.safetyEngineer = safetyEngineer;
    }

    public Integer getFullJob() {
        return fullJob;
    }

    public void setFullJob(Integer fullJob) {
        this.fullJob = fullJob;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getPoliticsCode() {
        return politicsCode;
    }

    public void setPoliticsCode(String politicsCode) {
        this.politicsCode = politicsCode;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSepcSubject() {
        return sepcSubject;
    }

    public void setSepcSubject(String sepcSubject) {
        this.sepcSubject = sepcSubject;
    }

    public String getDiplomaCode() {
        return diplomaCode;
    }

    public void setDiplomaCode(String diplomaCode) {
        this.diplomaCode = diplomaCode;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPartWorkDate() {
        return partWorkDate;
    }

    public void setPartWorkDate(String partWorkDate) {
        this.partWorkDate = partWorkDate;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getSoldierUrl() {
        return soldierUrl;
    }

    public void setSoldierUrl(String soldierUrl) {
        this.soldierUrl = soldierUrl;
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

    public String getExtend04() {
        return extend04;
    }

    public void setExtend04(String extend04) {
        this.extend04 = extend04;
    }

    public String getExtend05() {
        return extend05;
    }

    public void setExtend05(String extend05) {
        this.extend05 = extend05;
    }

    public String getTitleCodeName() {
        return titleCodeName;
    }

    public void setTitleCodeName(String titleCodeName) {
        this.titleCodeName = titleCodeName;
    }

    public String getGridDutyName() {
        return gridDutyName;
    }

    public void setGridDutyName(String gridDutyName) {
        this.gridDutyName = gridDutyName;
    }

    public String getSafetyDutyCodeName() {
        return safetyDutyCodeName;
    }

    public void setSafetyDutyCodeName(String safetyDutyCodeName) {
        this.safetyDutyCodeName = safetyDutyCodeName;
    }

    public String getPoliticsCodeName() {
        return politicsCodeName;
    }

    public void setPoliticsCodeName(String politicsCodeName) {
        this.politicsCodeName = politicsCodeName;
    }

    public String getDiplomaCodeName() {
        return diplomaCodeName;
    }

    public void setDiplomaCodeName(String diplomaCodeName) {
        this.diplomaCodeName = diplomaCodeName;
    }
}
