package com.jiuxi.module.sys.domain.entity;

import com.jiuxi.module.sys.domain.valueobject.ConfigKey;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 字典实体
 * 表示系统中的数据字典项，使用DDD模式设计
 * 
 * @author System Management (Enhanced by DDD Refactor)
 * @date 2025-09-06
 * @version 2.0 - Enhanced with DDD value objects and domain logic
 */
public class Dictionary {
    
    /**
     * 字典ID
     */
    private String dictId;
    
    /**
     * 字典编码值对象
     */
    private ConfigKey dictCodeObj;
    
    /**
     * 字典编码（兼容性字段）
     */
    private String dictCode;
    
    /**
     * 字典名称
     */
    private String dictName;
    
    /**
     * 字典描述
     */
    private String dictDesc;
    
    /**
     * 字典类型枚举
     */
    private ConfigType dictTypeEnum;
    
    /**
     * 字典状态枚举
     */
    private ConfigStatus dictStatusEnum;
    
    /**
     * 字典类型（兼容性字段）
     */
    private String dictType;
    
    /**
     * 状态（兼容性字段）
     */
    private String status;
    
    /**
     * 字典分组
     */
    private String dictGroup;
    
    /**
     * 是否系统预置
     */
    private Boolean isSystemPreset;
    
    /**
     * 上级字典ID（支持层级结构）
     */
    private String parentDictId;
    
    /**
     * 层级路径
     */
    private String dictPath;
    
    /**
     * 排序序号
     */
    private Integer orderIndex;
    
    /**
     * 创建信息
     */
    private String creator;
    private LocalDateTime createTime;
    
    /**
     * 更新信息
     */
    private String updator;
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    // 构造器
    public Dictionary() {
        this.status = "ACTIVE";
        this.dictStatusEnum = ConfigStatus.ACTIVE;
        this.dictTypeEnum = ConfigType.BUSINESS;
        this.isSystemPreset = false;
        this.orderIndex = 0;
    }
    
    public Dictionary(String dictCode, String dictName) {
        this();
        this.dictCode = dictCode;
        this.dictName = dictName;
        // 创建值对象
        if (dictCode != null) {
            try {
                this.dictCodeObj = new ConfigKey(dictCode);
            } catch (Exception e) {
                // 如果字典编码不符合ConfigKey的格式规则，则不创建值对象
                this.dictCodeObj = null;
            }
        }
    }
    
    /**
     * 使用值对象构造
     * @param dictCode 字典编码对象
     * @param dictName 字典名称
     * @param dictType 字典类型
     */
    public Dictionary(ConfigKey dictCode, String dictName, ConfigType dictType) {
        this();
        this.dictCodeObj = dictCode;
        this.dictName = dictName;
        this.dictTypeEnum = dictType;
        // 同步兼容性字段
        if (dictCode != null) {
            this.dictCode = dictCode.getValue();
        }
        if (dictType != null) {
            this.dictType = dictType.getCode();
        }
    }
    
    // 业务方法
    
    /**
     * 启用字典
     */
    public void enable() {
        if (this.isSystemPreset != null && this.isSystemPreset) {
            throw new IllegalStateException("系统预置字典不允许修改状态");
        }
        this.dictStatusEnum = ConfigStatus.ACTIVE;
        this.status = "ACTIVE";
    }
    
    /**
     * 停用字典
     */
    public void disable() {
        if (this.isSystemPreset != null && this.isSystemPreset) {
            throw new IllegalStateException("系统预置字典不允许修改状态");
        }
        this.dictStatusEnum = ConfigStatus.INACTIVE;
        this.status = "INACTIVE";
    }
    
    /**
     * 设置为草稿状态
     */
    public void setDraft() {
        if (this.isSystemPreset != null && this.isSystemPreset) {
            throw new IllegalStateException("系统预置字典不允许修改状态");
        }
        this.dictStatusEnum = ConfigStatus.DRAFT;
        this.status = "DRAFT";
    }
    
    /**
     * 检查是否激活
     */
    public boolean isActive() {
        return this.dictStatusEnum != null ? 
            this.dictStatusEnum.isActive() : "ACTIVE".equals(this.status);
    }
    
    /**
     * 检查是否可编辑
     */
    public boolean isEditable() {
        if (this.isSystemPreset != null && this.isSystemPreset) {
            return false;
        }
        return this.dictStatusEnum != null ? this.dictStatusEnum.isEditable() : true;
    }
    
    /**
     * 获取字典层级
     */
    public String[] getHierarchy() {
        return this.dictCodeObj != null ? 
            this.dictCodeObj.getHierarchy() : 
            (this.dictCode != null ? this.dictCode.split("\\."): new String[0]);
    }
    
    /**
     * 检查是否为根级字典
     */
    public boolean isRootLevel() {
        return this.parentDictId == null || this.parentDictId.isEmpty();
    }
    
    /**
     * 检查是否是指定类型的字典
     */
    public boolean isOfType(ConfigType type) {
        return this.dictTypeEnum == type;
    }
    
    /**
     * 设置为系统预置字典
     */
    public void markAsSystemPreset() {
        this.isSystemPreset = true;
    }
    
    /**
     * 更新字典名称和描述
     */
    public void updateInfo(String dictName, String dictDesc) {
        if (!isEditable()) {
            throw new IllegalStateException("字典不可编辑");
        }
        this.dictName = dictName;
        this.dictDesc = dictDesc;
    }
    
    // Getters and Setters
    public String getDictId() {
        return dictId;
    }
    
    public void setDictId(String dictId) {
        this.dictId = dictId;
    }
    
    public String getDictCode() {
        return dictCode;
    }
    
    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
        // 同步更新值对象
        if (dictCode != null) {
            try {
                this.dictCodeObj = new ConfigKey(dictCode);
            } catch (Exception e) {
                // 保持向后兼容，如果编码格式不正确则不创建值对象
                this.dictCodeObj = null;
            }
        } else {
            this.dictCodeObj = null;
        }
    }
    
    public String getDictName() {
        return dictName;
    }
    
    public void setDictName(String dictName) {
        this.dictName = dictName;
    }
    
    public String getDictDesc() {
        return dictDesc;
    }
    
    public void setDictDesc(String dictDesc) {
        this.dictDesc = dictDesc;
    }
    
    public String getDictType() {
        return dictType;
    }
    
    public void setDictType(String dictType) {
        this.dictType = dictType;
        // 同步更新枚举
        if (dictType != null) {
            try {
                this.dictTypeEnum = ConfigType.fromCode(dictType);
            } catch (Exception e) {
                // 保持向后兼容，如果类型代码不正确则设为默认值
                this.dictTypeEnum = ConfigType.BUSINESS;
            }
        } else {
            this.dictTypeEnum = ConfigType.BUSINESS;
        }
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        // 同步更新枚举
        if (status != null) {
            try {
                this.dictStatusEnum = ConfigStatus.fromCode(status);
            } catch (Exception e) {
                // 保持向后兼容，如果状态代码不正确则设为默认值
                this.dictStatusEnum = ConfigStatus.ACTIVE;
            }
        } else {
            this.dictStatusEnum = ConfigStatus.ACTIVE;
        }
    }
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
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
    
    // 新增的值对象和枚举getter/setter
    
    public ConfigKey getDictCodeObj() {
        return dictCodeObj;
    }
    
    public void setDictCodeObj(ConfigKey dictCodeObj) {
        this.dictCodeObj = dictCodeObj;
        if (dictCodeObj != null) {
            this.dictCode = dictCodeObj.getValue();
        }
    }
    
    public ConfigType getDictTypeEnum() {
        return dictTypeEnum;
    }
    
    public void setDictTypeEnum(ConfigType dictTypeEnum) {
        this.dictTypeEnum = dictTypeEnum;
        if (dictTypeEnum != null) {
            this.dictType = dictTypeEnum.getCode();
        }
    }
    
    public ConfigStatus getDictStatusEnum() {
        return dictStatusEnum;
    }
    
    public void setDictStatusEnum(ConfigStatus dictStatusEnum) {
        this.dictStatusEnum = dictStatusEnum;
        if (dictStatusEnum != null) {
            this.status = dictStatusEnum.getCode();
        }
    }
    
    public String getDictGroup() {
        return dictGroup;
    }
    
    public void setDictGroup(String dictGroup) {
        this.dictGroup = dictGroup;
    }
    
    public Boolean getIsSystemPreset() {
        return isSystemPreset;
    }
    
    public void setIsSystemPreset(Boolean isSystemPreset) {
        this.isSystemPreset = isSystemPreset;
    }
    
    public String getParentDictId() {
        return parentDictId;
    }
    
    public void setParentDictId(String parentDictId) {
        this.parentDictId = parentDictId;
    }
    
    public String getDictPath() {
        return dictPath;
    }
    
    public void setDictPath(String dictPath) {
        this.dictPath = dictPath;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dictionary that = (Dictionary) o;
        return Objects.equals(dictId, that.dictId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(dictId);
    }
}