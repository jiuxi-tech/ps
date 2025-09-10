package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: TpRoleMenuMapper
 * @Description: 角色_菜单表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpRoleMenuMapper {

    int save(TpRoleMenu bean);

    int delete(@Param("roleId") String roleId, @Param("menuId") String menuId);

}
