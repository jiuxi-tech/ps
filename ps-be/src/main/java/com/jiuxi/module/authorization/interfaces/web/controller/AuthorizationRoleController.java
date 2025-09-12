package com.jiuxi.module.authorization.interfaces.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpRoleAuthQuery;
import com.jiuxi.admin.core.bean.query.TpRoleQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.module.authorization.app.service.RoleService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * @ClassName: AuthorizationRoleController
 * @Description: 系统角色权限控制器
 * @Author DDD重构
 * @Date 2025-09-12
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/role")
@Authorization
public class AuthorizationRoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 角色授权列表
     */
    @RequestMapping("/roleAuthList")
    public JsonResponse roleAuthList(TpRoleAuthQuery query, String jwtpid, String jwtdid, String jwtrids, String jwtaid) {
        LinkedHashSet<TpRoleVO> list = roleService.roleAuthList(query, jwtpid, jwtdid, jwtrids, jwtaid);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 人员授权，待选角色列表，查询自己角色创建的角色以及自己拥有的角色（向后兼容）
     */
    @RequestMapping("/role-auth-list")
    @IgnoreAuthorization
    public JsonResponse roleAuthListCompat(TpRoleAuthQuery query, String jwtpid, String jwtdid, String jwtrids, String jwtaid) {
        LinkedHashSet<TpRoleVO> list = roleService.roleAuthList(query, jwtpid, jwtdid, jwtrids, jwtaid);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 分页查询角色列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpRoleQuery query, String jwtpid, String jwtrids, String jwtaid) {
        IPage<TpRoleVO> page = roleService.queryPage(query, jwtpid, jwtrids, jwtaid);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 角色列表（政府），查询自己创建的政府角色
     */
    @RequestMapping("/org/list")
    public JsonResponse orgList(TpRoleQuery query, String jwtpid, String jwtrids, String jwtaid) {
        query.setCategory(0); // 0代表政府类别
        IPage<TpRoleVO> page = roleService.queryPage(query, jwtpid, jwtrids, jwtaid);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 获取角色列表（不分页）
     */
    @RequestMapping("/getList")
    public JsonResponse getList(TpRoleQuery query, String jwtaid) {
        List<TpRoleVO> list = roleService.getList(query, jwtaid);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 新增角色
     */
    @RequestMapping("/add")
    public JsonResponse add(@Validated(AddGroup.class) TpRoleVO vo, String jwtpid, String jwtaid) {
        int result = roleService.add(vo, jwtpid, jwtaid, 0);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 更新角色信息
     */
    @RequestMapping("/update")
    public JsonResponse update(@Validated(UpdateGroup.class) TpRoleVO vo, String jwtpid) {
        int result = roleService.update(vo, jwtpid);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 查看角色详情
     */
    @RequestMapping("/view")
    public JsonResponse view(String roleId) {
        TpRoleVO result = roleService.view(roleId);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 根据角色ID查询角色人员关系
     */
    @RequestMapping("/selectByRoleId")
    public JsonResponse selectByRoleId(String roleId) {
        List<TpPersonRoleVO> list = roleService.selectByRoleId(roleId);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 删除角色
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String roleId, String creator, String jwtpid) {
        int result = roleService.delete(roleId, creator, jwtpid);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 获取权限树
     */
    @RequestMapping("/authTree")
    @IgnoreAuthorization
    public JsonResponse authTree(String roleId, String jwtrids, String jwtpid) {
        List<TreeNode> list = roleService.authTree(roleId, jwtrids, jwtpid);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 角色菜单权限配置
     */
    @RequestMapping("/roleMenus")
    public JsonResponse roleMenus(String roleId, String menuIds) {
        int result = roleService.roleMenus(roleId, menuIds);
        return JsonResponse.buildSuccess(result);
    }
}