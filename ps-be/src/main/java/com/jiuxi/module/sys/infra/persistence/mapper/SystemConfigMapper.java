package com.jiuxi.module.sys.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuxi.module.sys.infra.persistence.entity.SystemConfigPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统配置Mapper接口
 * 定义系统配置持久化对象的数据库操作
 * 
 * @author System Management
 * @date 2025-09-06
 */
@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfigPO> {
    
    /**
     * 根据配置键和租户ID查找系统配置
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 系统配置持久化对象
     */
    SystemConfigPO findByConfigKeyAndTenantId(@Param("configKey") String configKey, @Param("tenantId") String tenantId);
}