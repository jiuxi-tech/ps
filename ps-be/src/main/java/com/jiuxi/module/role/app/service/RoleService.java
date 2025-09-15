package com.jiuxi.module.role.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpRoleAuthQuery;
import com.jiuxi.admin.core.bean.query.TpRoleQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.common.bean.TreeNode;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * 角色权限应用服务接口
 * @author DDD重构
 * @date 2025-09-12
 */
public interface RoleService {

    LinkedHashSet<TpRoleVO> roleAuthList(TpRoleAuthQuery query, String pid, String deptId, String rids, String jwtaid);

    IPage<TpRoleVO> queryPage(TpRoleQuery query, String pid, String rids, String jwtaid);

    List<TpRoleVO> getList(TpRoleQuery query, String jwtaid);

    int add(TpRoleVO vo, String pid, String aid, int category);

    int update(TpRoleVO vo, String pid);

    TpRoleVO view(String roleId);

    List<TpPersonRoleVO> selectByRoleId(String roleId);

    int delete(String roleId, String creator, String pid);

    List<TreeNode> authTree(String roleId, String rids, String pid);

    int roleMenus(String roleId, String menuIds);
}