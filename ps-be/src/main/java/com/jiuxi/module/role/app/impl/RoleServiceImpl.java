package com.jiuxi.module.role.app.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpRoleAuthQuery;
import com.jiuxi.admin.core.bean.query.TpRoleQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.admin.core.service.TpRoleService;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.module.role.app.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * 角色权限应用服务实现类（适配器模式）
 * @author DDD重构
 * @date 2025-09-12
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private TpRoleService tpRoleService;

    @Override
    public LinkedHashSet<TpRoleVO> roleAuthList(TpRoleAuthQuery query, String pid, String deptId, String rids, String jwtaid) {
        return tpRoleService.roleAuthList(query, pid, deptId, rids, jwtaid);
    }

    @Override
    public IPage<TpRoleVO> queryPage(TpRoleQuery query, String pid, String rids, String jwtaid) {
        return tpRoleService.queryPage(query, pid, rids, jwtaid);
    }

    @Override
    public List<TpRoleVO> getList(TpRoleQuery query, String jwtaid) {
        return tpRoleService.getList(query, jwtaid);
    }

    @Override
    public int add(TpRoleVO vo, String pid, String aid, int category) {
        return tpRoleService.add(vo, pid, aid, category);
    }

    @Override
    public int update(TpRoleVO vo, String pid) {
        return tpRoleService.update(vo, pid);
    }

    @Override
    public TpRoleVO view(String roleId) {
        return tpRoleService.view(roleId);
    }

    @Override
    public List<TpPersonRoleVO> selectByRoleId(String roleId) {
        return tpRoleService.selectByRoleId(roleId);
    }

    @Override
    public int delete(String roleId, String creator, String pid) {
        return tpRoleService.delete(roleId, creator, pid);
    }

    @Override
    public List<TreeNode> authTree(String roleId, String rids, String pid) {
        return tpRoleService.authTree(roleId, rids, pid);
    }

    @Override
    public int roleMenus(String roleId, String menuIds) {
        return tpRoleService.roleMenus(roleId, menuIds);
    }
}