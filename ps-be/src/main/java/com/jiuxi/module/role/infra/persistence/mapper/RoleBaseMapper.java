package com.jiuxi.module.role.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuxi.module.role.infra.persistence.entity.RolePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色基础Mapper
 * 
 * @author DDD重构
 * @date 2025-09-16
 */
@Mapper
public interface RoleBaseMapper extends BaseMapper<RolePO> {
}