package com.jiuxi.admin.core.bean.query;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @ClassName: TpEntBasicQuery
 * @Description: 企业账号列表查询
 * @Author: pdd
 * @Date: 2021/09/13 13:43
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TpEntAccountQuery {

    /**
     * 企业id
     */
    private String entId;

    /**
     * 企业层级code
     */
    @JsonIgnore
    private String deptLevelcode;

    /**
     * 人员名称
     */
    private String personName;

    /**
     * 用户名/账号
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页记录数
     */
    private Integer size;

    public String getEntId() {
        return entId;
    }

    public void setEntId(String entId) {
        this.entId = entId;
    }

    public String getDeptLevelcode() {
        return deptLevelcode;
    }

    public void setDeptLevelcode(String deptLevelcode) {
        this.deptLevelcode = deptLevelcode;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
