package com.jiuxi.module.role.infra.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuxi.module.role.infra.persistence.entity.PermissionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单关联Mapper
 * 
 * @author DDD重构
 * @date 2025-09-16
 */
@Mapper
public interface RoleMenuMapper {
    
    /**
     * 根据角色ID查询权限列表
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<PermissionPO> selectPermissionsByRoleId(@Param("roleId") String roleId);
    
    /**
     * 根据角色ID删除关联关系
     * 
     * @param roleId 角色ID
     * @return 删除的记录数
     */
    int deleteByRoleId(@Param("roleId") String roleId);
    
    /**
     * 插入角色菜单关联关系
     * 
     * @param roleId 角色ID
     * @param menuId 菜单ID
     * @return 插入的记录数
     */
    int insertRoleMenu(@Param("roleId") String roleId, @Param("menuId") String menuId);
}