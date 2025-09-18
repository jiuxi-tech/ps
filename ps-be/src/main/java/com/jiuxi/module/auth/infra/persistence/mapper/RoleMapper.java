package com.jiuxi.module.auth.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuxi.module.auth.infra.persistence.entity.RolePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 角色Mapper接口
 * 定义角色持久化对象的数据库操作
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@Mapper
public interface RoleMapper extends BaseMapper<RolePO> {
    
    /**
     * 检查角色是否有关联的用户
     * @param roleId 角色ID
     * @return 关联用户数量
     */
    @Select("SELECT COUNT(1) FROM sys_user_role WHERE role_id = #{roleId}")
    int checkRoleHasUsers(@Param("roleId") String roleId);
}