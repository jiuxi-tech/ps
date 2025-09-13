package com.jiuxi.module.system.interfaces.web.controller;

import com.jiuxi.admin.bean.MenuTreeNode;
import com.jiuxi.admin.core.bean.vo.TpMenuVO;
import com.jiuxi.module.system.app.service.SystemMenuService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: SystemMenuManagementController
 * @Description: 系统菜单管理控制器
 * @Author DDD重构
 * @Date 2025-09-12
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/menu")
@Authorization
public class SystemMenuManagementController {

    @Autowired
    private SystemMenuService systemMenuService;

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
        List<TreeNode> list = systemMenuService.tree(menuId);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 根据人员查询用户拥有的菜单
     */
    @RequestMapping("/mainTree")
    @IgnoreAuthorization
    public JsonResponse mainTree(String jwtpid, String jwtrids) {
        List<MenuTreeNode> list = systemMenuService.mainTree(jwtpid, jwtrids);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 根据人员查询用户拥有的菜单 (向后兼容 main-tree)
     */
    @RequestMapping("/main-tree")
    @IgnoreAuthorization
    public JsonResponse mainTreeCompat(String jwtpid, String jwtrids) {
        List<MenuTreeNode> list = systemMenuService.mainTree(jwtpid, jwtrids);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 根据人员查询用户拥有的菜单（APP端）
     */
    @RequestMapping("/mainAppTree")
    @IgnoreAuthorization
    public JsonResponse mainAppTree(String jwtpid, String jwtrids) {
        Set<MenuTreeNode> list = systemMenuService.mainAppTree(jwtpid, jwtrids);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 异步菜单树查询
     * 同步查询下两级，通过第二级判断下一级是否有叶子结点
     */
    @RequestMapping("/asyncTree")
    public JsonResponse asyncTree(String menuId, String jwtpid) {
        List<TreeNode> list = systemMenuService.asyncTree(menuId, jwtpid);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 新增菜单信息
     */
    @RequestMapping("/add")
    public JsonResponse add(@Validated(AddGroup.class) TpMenuVO vo, String jwtpid) {
        TpMenuVO result = systemMenuService.save(vo, jwtpid);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 更新菜单信息
     */
    @RequestMapping("/update")
    public JsonResponse update(@Validated(UpdateGroup.class) TpMenuVO vo, String jwtpid) {
        TpMenuVO result = systemMenuService.update(vo, jwtpid);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 查看菜单信息
     */
    @RequestMapping("/view")
    public JsonResponse view(String menuId) {
        TpMenuVO result = systemMenuService.view(menuId);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 删除菜单信息
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String menuId, String jwtpid) {
        int result = systemMenuService.delete(menuId, jwtpid);
        return JsonResponse.buildSuccess(result);
    }
}