package com.jiuxi.admin.core.bean.query;

import java.util.List;

/**
 * @ClassName: TpTreePermQuery
 * @Description: 数据权限部门树查询
 * @Author jiuxx
 * @Date 2023/11/6 9:07
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TpTreePermQuery {

    /**
     * 人员id
     */
    private String personId;

    /**
     * 部门id
     */
    private String depId;

    /**
     * 部门类型，可以传多个，逗号分隔; 最终转化为 deptTypes 进行查询
     */
    private String deptType;

    /**
     * 部门类型, 此参数不接收前端的传值；请使用参数deptType
     */
    private List<String> deptTypes;

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public List<String> getDeptTypes() {
        return deptTypes;
    }

    public void setDeptTypes(List<String> deptTypes) {
        this.deptTypes = deptTypes;
    }
}
