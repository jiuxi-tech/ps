package com.jiuxi.module.user.intf.web.controller.query;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import com.jiuxi.module.user.app.query.handler.UserQueryHandler;
import com.jiuxi.module.user.app.query.dto.UserQuery;
import com.jiuxi.module.user.app.query.dto.UserPageQuery;
import com.jiuxi.module.user.app.dto.UserResponseDTO;
import com.jiuxi.module.user.intf.web.dto.PageResult;
import com.jiuxi.security.core.entity.vo.PersonVO;
import com.jiuxi.security.core.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 用户查询控制器
 * 处理用户相关的查询操作（详情、列表、分页等）
 */
@RestController
@RequestMapping("/api/v1/users")
@Authorization
@Slf4j
public class UserQueryController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "personId";

    @Autowired
    private UserQueryHandler userQueryHandler;

    @Autowired
    private PersonService personService;

    // 已将getCurrentUserInfo方法迁移到UserController中，避免URL映射冲突
    // GET [/api/v1/users/me]

    /**
     * 查看用户详情
     */
    @GetMapping("/{personId}")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse getUserDetail(@PathVariable String personId) {
        try {
            UserResponseDTO user = userQueryHandler.getUserById(personId);
            return JsonResponse.buildSuccess(user);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询用户失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户名查询用户
     */
    @GetMapping("/username/{username}")
    public JsonResponse getUserByUsername(
            @PathVariable String username,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        
        try {
            UserResponseDTO user = userQueryHandler.getUserByUsername(username, tenantId);
            return JsonResponse.buildSuccess(user);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询用户失败: " + e.getMessage());
        }
    }

    /**
     * 根据部门查询用户列表
     */
    @GetMapping("/departments/{deptId}")
    public JsonResponse getUsersByDepartment(@PathVariable String deptId) {
        try {
            List<UserResponseDTO> users = userQueryHandler.getUsersByDepartment(deptId);
            return JsonResponse.buildSuccess(users);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询部门用户失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询用户列表
     */
    @PostMapping("/search")
    public JsonResponse searchUsers(
            @RequestBody UserPageQuery query,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        try {
            // 设置租户ID
            if (query.getTenantId() == null || query.getTenantId().isEmpty()) {
                query.setTenantId(tenantId);
            }
            
            // 获取分页数据
            List<UserResponseDTO> users = userQueryHandler.getUserPage(query);
            long total = userQueryHandler.getUserCount(query);
            
            // 构建分页结果
            PageResult<UserResponseDTO> pageResult = new PageResult<>();
            pageResult.setData(users);
            pageResult.setTotal(total);
            pageResult.setCurrent(query.getCurrent());
            pageResult.setSize(query.getSize());
            pageResult.setPages((total + query.getSize() - 1) / query.getSize());
            
            return JsonResponse.buildSuccess(pageResult);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询用户失败: " + e.getMessage());
        }
    }
}