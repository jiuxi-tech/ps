package com.jiuxi.module.system.app.service.impl;

import com.jiuxi.shared.common.base.vo.MenuTreeNode;
import com.jiuxi.admin.core.bean.vo.TpMenuVO;
import com.jiuxi.admin.core.service.TpMenuService;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.module.system.app.service.SystemMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 系统菜单应用服务实现类（适配器模式）
 * @author DDD重构
 * @date 2025-09-12
 */
@Service
public class SystemMenuServiceImpl implements SystemMenuService {

    @Autowired
    private TpMenuService tpMenuService;

    @Override
    public List<TreeNode> tree(String menuId) {
        return tpMenuService.tree(menuId);
    }

    @Override
    public List<MenuTreeNode> mainTree(String pid, String rids) {
        return tpMenuService.mainTree(pid, rids);
    }

    @Override
    public Set<MenuTreeNode> mainAppTree(String pid, String rids) {
        return tpMenuService.mainAppTree(pid, rids);
    }

    @Override
    public List<TreeNode> asyncTree(String menuId, String jwtpid) {
        return tpMenuService.asyncTree(menuId, jwtpid);
    }

    @Override
    public TpMenuVO save(TpMenuVO vo, String pid) {
        return tpMenuService.save(vo, pid);
    }

    @Override
    public TpMenuVO update(TpMenuVO vo, String pid) {
        return tpMenuService.update(vo, pid);
    }

    @Override
    public TpMenuVO view(String menuId) {
        return tpMenuService.view(menuId);
    }

    @Override
    public int delete(String menuId, String pid) {
        return tpMenuService.delete(menuId, pid);
    }
}