package com.jiuxi.admin.core.bean.vo;

import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

/**
 * 标签表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2024-01-27
 */
public class TpTagVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签ID
     */
    @NotBlank(message = "标签ID不能为空", groups = UpdateGroup.class)
    private String tagId;
    
    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空", groups = AddGroup.class)
    private String tagName;
    
    /**
     * 标签描述
     */
    private String tagDesc;
    
    /**
     * 标签颜色
     */
    private String tagColor;
    
    /**
     * 排序号
     */
    private Double orderIndex;
    

    
    /**
     * 租户ID
     */
    private String tenantId;
    
    /**
     * 所属机构ID
     */
    private String ascnId;
    

    

    
    /**
     * 创建人
     */
    @NotBlank(message = "创建人不能为空", groups = UpdateGroup.class)
    private String creator;
    
    /**
     * 创建时间
     */
    private String createTime;
    
    /**
     * 创建人姓名
     */
    private String createPersonName;
    
    /**
     * 修改人
     */
    private String updator;
    
    /**
     * 修改人姓名
     */
    private String updatePersonName;
    
    /**
     * 修改时间
     */
    private String updateTime;
    
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
     * 数据密钥
     */
    private String passKey;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagDesc() {
        return tagDesc;
    }

    public void setTagDesc(String tagDesc) {
        this.tagDesc = tagDesc;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }

    public Double getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Double orderIndex) {
        this.orderIndex = orderIndex;
    }



    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getUpdatePersonName() {
        return updatePersonName;
    }

    public void setUpdatePersonName(String updatePersonName) {
        this.updatePersonName = updatePersonName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TpTagVO tpTagVO = (TpTagVO) o;
        return tagId.equals(tpTagVO.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId);
    }
}