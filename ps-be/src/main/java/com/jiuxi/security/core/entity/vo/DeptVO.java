package com.jiuxi.security.core.entity.vo;

import com.jiuxi.shared.common.base.vo.BaseVO;

/**
 * @ClassName: DeptVO
 * @Description: 部门VO
 * @Author: 杨攀
 * @Date: 2020/7/21 14:50
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class DeptVO extends BaseVO {

    /** 部门id */
    private String deptId;

    /**
     * 当前登陆人所在部门的行政区划code
     */
    private String cityCode;

    /** 单位id */
    private String ascnId;

    /** 部门全称 */
    private String deptFullName;

    /** 部门简称 */
    private String deptSimpleName;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAscnId() {
        return ascnId;
    }

    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }

    public String getDeptFullName() {
        return deptFullName;
    }

    public void setDeptFullName(String deptFullName) {
        this.deptFullName = deptFullName;
    }

    public String getDeptSimpleName() {
        return deptSimpleName;
    }

    public void setDeptSimpleName(String deptSimpleName) {
        this.deptSimpleName = deptSimpleName;
    }
}
