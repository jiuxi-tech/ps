package com.jiuxi.module.auth.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuxi.module.auth.infra.persistence.entity.PermissionPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限Mapper接口
 * 定义权限持久化对象的数据库操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionPO> {
    
}