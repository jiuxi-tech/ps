package com.jiuxi.module.role.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuxi.module.role.infra.persistence.entity.PermissionPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限基础Mapper
 * 
 * @author DDD重构
 * @date 2025-09-16
 */
@Mapper
public interface PermissionBaseMapper extends BaseMapper<PermissionPO> {
}