package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 人员标签关系表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2024-01-27
 */
@TableName("tp_person_tag")
public class TpPersonTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人员ID
     */
    @TableId
    private String personId;
    
    /**
     * 标签ID
     */
    private String tagId;
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 创建人
     */
    private String creator;
    
    /**
     * 创建时间
     */
    private String createTime;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
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
}