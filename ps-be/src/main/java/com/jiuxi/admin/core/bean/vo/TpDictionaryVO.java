package com.jiuxi.admin.core.bean.vo;

import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 字典表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:18
 */
public class TpDictionaryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典id
     */
    @NotBlank(message = "字典名称不能为空", groups = UpdateGroup.class)
    private String dicId;
    /**
     * 字典编码
     */
    @NotBlank(message = "字典编码不能为空", groups = AddGroup.class)
    private String dicCode;
    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空", groups = AddGroup.class)
    private String dicName;
    /**
     * 字典类型 （系统组件字典、业务字典）
     */
    private String dicType;
    private String dicTypeName;
    /**
     * 上级字典ID
     */
    private String pdicId;
    /**
     * 字典描述
     */
    private String dicDesc;
    /**
     * 字典排序
     */
    private Double orderIndex;
    /**
     * 是否启用
     */
    private Integer enabled;
    /**
     * 是否有效
     */
    private Integer actived;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 创建人
     */
    private String createor;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改人
     */
    private String updateor;
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

    public String getDicId() {
        return dicId;
    }

    public void setDicId(String dicId) {
        this.dicId = dicId;
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getDicTypeName() {
        return dicTypeName;
    }

    public void setDicTypeName(String dicTypeName) {
        this.dicTypeName = dicTypeName;
    }

    public String getPdicId() {
        return pdicId;
    }

    public void setPdicId(String pdicId) {
        this.pdicId = pdicId;
    }

    public String getDicDesc() {
        return dicDesc;
    }

    public void setDicDesc(String dicDesc) {
        this.dicDesc = dicDesc;
    }

    public Double getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Double orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getActived() {
        return actived;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCreateor() {
        return createor;
    }

    public void setCreateor(String createor) {
        this.createor = createor;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateor() {
        return updateor;
    }

    public void setUpdateor(String updateor) {
        this.updateor = updateor;
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
}
