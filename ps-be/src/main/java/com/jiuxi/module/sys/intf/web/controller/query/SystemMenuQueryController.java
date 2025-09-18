package com.jiuxi.module.sys.intf.web.controller.query;

import com.jiuxi.admin.bean.MenuTreeNode;
import com.jiuxi.admin.core.bean.vo.TpMenuVO;
import com.jiuxi.admin.core.service.TpMenuService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Set;

/**
 * 系统菜单管理查询控制器
 * 负责系统菜单相关的数据查询操作 (Read)
 * 基于CQRS架构设计，专注于处理查询操作
 * 
 * @author DDD Refactor - Phase 6 (Separated from SystemMenuManagementController)
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/sys/menu")
@Authorization
public class SystemMenuQueryController {

    @Autowired
    private TpMenuService tpMenuService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : text.trim());
            }
        });
    }

    /**
     * 菜单树查询
     * 只有超级管理员具有操作菜单的权限，查询所有菜单树。默认展开
     * 进入该功能，menuId传空，获取全部菜单树。menuId不为空，查询下级菜单树。
     */
    @RequestMapping("/tree")
    public JsonResponse tree(String menuId, String jwtpid) {
        List<TreeNode> list = tpMenuService.tree(menuId);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 根据人员查询用户拥有的菜单
     */
    @RequestMapping("/mainTree")
    @IgnoreAuthorization
    public JsonResponse mainTree(String jwtpid, String jwtrids) {
        List<MenuTreeNode> list = tpMenuService.mainTree(jwtpid, jwtrids);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 根据人员查询用户拥有的菜单 (向后兼容 main-tree)
     */
    @RequestMapping("/main-tree")
    @IgnoreAuthorization
    public JsonResponse mainTreeCompat(String jwtpid, String jwtrids) {
        List<MenuTreeNode> list = tpMenuService.mainTree(jwtpid, jwtrids);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 根据人员查询用户拥有的菜单（APP端）
     */
    @RequestMapping("/mainAppTree")
    @IgnoreAuthorization
    public JsonResponse mainAppTree(String jwtpid, String jwtrids) {
        Set<MenuTreeNode> list = tpMenuService.mainAppTree(jwtpid, jwtrids);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 异步菜单树查询
     * 同步查询下两级，通过第二级判断下一级是否有叶子结点
     */
    @RequestMapping("/asyncTree")
    public JsonResponse asyncTree(String menuId, String jwtpid) {
        List<TreeNode> list = tpMenuService.asyncTree(menuId, jwtpid);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 查看菜单信息
     */
    @RequestMapping("/view")
    public JsonResponse view(String menuId) {
        TpMenuVO result = tpMenuService.view(menuId);
        return JsonResponse.buildSuccess(result);
    }
}