package com.jiuxi.module.role.intf.web.controller.command;

import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.module.role.app.service.RoleService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.module.user.intf.web.controller.UserPersonController;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: RoleCommandController
 * @Description: 角色命令控制器 - CQRS命令端
 * @Author DDD重构
 * @Date 2025-09-15
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/role")
@Authorization
public class RoleCommandController {

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private UserPersonController userPersonController;

    /**
     * 新增角色
     */
    @RequestMapping("/add")
    public JsonResponse add(@Validated(AddGroup.class) TpRoleVO vo, String jwtpid, String jwtaid) {
        int result = roleService.add(vo, jwtpid, jwtaid, 0);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 新增政府角色
     */
    @RequestMapping("/org/add")
    public JsonResponse orgAdd(@Validated(AddGroup.class) TpRoleVO vo, String jwtpid, String jwtaid) {
        int result = roleService.add(vo, jwtpid, jwtaid, 0); // 0代表政府类别
        return JsonResponse.buildSuccess(result);
    }
    
    /**
     * 用户授权，增加角色（转发到UserPersonController）
     * 处理前端传递的roleIds参数，确保格式正确
     *
     * @param personId 人员ID
     * @param deptId 部门ID
     * @param roleIds 角色ID列表
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/auth-add")
    public JsonResponse authAdd(String personId, String deptId, String roleIds) {
        // 检查参数
        if (personId == null || personId.isEmpty()) {
            return JsonResponse.buildFailure("人员ID不能为空");
        }
        if (deptId == null || deptId.isEmpty()) {
            return JsonResponse.buildFailure("部门ID不能为空");
        }
        
        // 处理roleIds，去除开头的逗号
        if (roleIds != null && roleIds.startsWith(",")) {
            roleIds = roleIds.substring(1);
        }
        
        // 转发到UserPersonController的auth-add方法
        return userPersonController.authAdd(personId, deptId, roleIds);
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
     * 删除角色
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String roleId, String creator, String jwtpid) {
        int result = roleService.delete(roleId, creator, jwtpid);
        return JsonResponse.buildSuccess(result);
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