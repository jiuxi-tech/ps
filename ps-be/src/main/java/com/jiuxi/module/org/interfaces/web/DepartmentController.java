package com.jiuxi.module.org.interfaces.web;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.core.core.annotation.IgnoreAuthorization;
import com.jiuxi.core.core.validator.group.AddGroup;
import com.jiuxi.core.core.validator.group.UpdateGroup;
import com.jiuxi.module.org.app.dto.DepartmentCreateDTO;
import com.jiuxi.module.org.app.dto.DepartmentResponseDTO;
import com.jiuxi.module.org.app.dto.DepartmentUpdateDTO;
import com.jiuxi.module.org.app.service.DepartmentApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 * 采用DDD架构的部门管理接口
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@RestController
@RequestMapping("/api/v1/departments")
@Authorization
public class DepartmentController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "deptId";

    @Autowired
    private DepartmentApplicationService departmentApplicationService;

    /**
     * 创建部门
     */
    @PostMapping
    public JsonResponse createDepartment(
            @Validated(AddGroup.class) @RequestBody DepartmentCreateDTO createDTO,
            @RequestHeader("X-User-Person-Id") String operator,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        
        try {
            DepartmentResponseDTO department = departmentApplicationService.createDepartment(createDTO, tenantId, operator);
            return JsonResponse.buildSuccess(department);
        } catch (Exception e) {
            return JsonResponse.buildFailure("部门创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新部门信息
     */
    @PutMapping("/{deptId}")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse updateDepartment(
            @PathVariable String deptId,
            @Validated(UpdateGroup.class) @RequestBody DepartmentUpdateDTO updateDTO,
            @RequestHeader("X-User-Person-Id") String operator,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        
        try {
            DepartmentResponseDTO department = departmentApplicationService.updateDepartment(deptId, updateDTO, tenantId, operator);
            return JsonResponse.buildSuccess(department);
        } catch (Exception e) {
            return JsonResponse.buildFailure("部门更新失败: " + e.getMessage());
        }
    }

    /**
     * 查看部门详情
     */
    @GetMapping("/{deptId}")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse getDepartmentDetail(@PathVariable String deptId) {
        try {
            DepartmentResponseDTO department = departmentApplicationService.getDepartmentById(deptId);
            return JsonResponse.buildSuccess(department);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询部门失败: " + e.getMessage());
        }
    }

    /**
     * 获取部门树形结构
     */
    @GetMapping("/tree")
    @IgnoreAuthorization
    public JsonResponse getDepartmentTree(@RequestHeader("X-Tenant-Id") String tenantId) {
        try {
            List<DepartmentResponseDTO> departmentTree = departmentApplicationService.getDepartmentTree(tenantId);
            return JsonResponse.buildSuccess(departmentTree);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取部门树失败: " + e.getMessage());
        }
    }

    /**
     * 根据父部门查询子部门列表
     */
    @GetMapping("/parent/{parentDeptId}")
    public JsonResponse getChildDepartments(@PathVariable String parentDeptId) {
        try {
            List<DepartmentResponseDTO> children = departmentApplicationService.getChildDepartments(parentDeptId);
            return JsonResponse.buildSuccess(children);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询子部门失败: " + e.getMessage());
        }
    }

    /**
     * 获取根部门列表
     */
    @GetMapping("/root")
    public JsonResponse getRootDepartments(@RequestHeader("X-Tenant-Id") String tenantId) {
        try {
            List<DepartmentResponseDTO> rootDepartments = departmentApplicationService.getRootDepartments(tenantId);
            return JsonResponse.buildSuccess(rootDepartments);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询根部门失败: " + e.getMessage());
        }
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{deptId}")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse deleteDepartment(
            @PathVariable String deptId,
            @RequestHeader("X-User-Person-Id") String operator,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        
        try {
            departmentApplicationService.deleteDepartment(deptId, tenantId, operator);
            return JsonResponse.buildSuccess("删除成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("删除部门失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除部门
     */
    @DeleteMapping("/batch")
    public JsonResponse deleteDepartments(
            @RequestBody List<String> deptIds,
            @RequestHeader("X-User-Person-Id") String operator,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        
        try {
            departmentApplicationService.deleteDepartments(deptIds, tenantId, operator);
            return JsonResponse.buildSuccess("批量删除成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 启用部门
     */
    @PutMapping("/{deptId}/enable")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse enableDepartment(
            @PathVariable String deptId,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            departmentApplicationService.enableDepartment(deptId, operator);
            return JsonResponse.buildSuccess("启用成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("启用部门失败: " + e.getMessage());
        }
    }

    /**
     * 停用部门
     */
    @PutMapping("/{deptId}/disable")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse disableDepartment(
            @PathVariable String deptId,
            @RequestHeader("X-User-Person-Id") String operator) {
        
        try {
            departmentApplicationService.disableDepartment(deptId, operator);
            return JsonResponse.buildSuccess("停用成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("停用部门失败: " + e.getMessage());
        }
    }

    /**
     * 查询部门下的用户数量
     */
    @GetMapping("/{deptId}/user-count")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse getDepartmentUserCount(@PathVariable String deptId) {
        try {
            long userCount = departmentApplicationService.getDepartmentUserCount(deptId);
            return JsonResponse.buildSuccess(userCount);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询用户数量失败: " + e.getMessage());
        }
    }

    /**
     * 移动部门到新的父部门
     */
    @PutMapping("/{deptId}/move")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse moveDepartment(
            @PathVariable String deptId,
            @RequestParam String newParentDeptId,
            @RequestHeader("X-User-Person-Id") String operator,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        
        try {
            DepartmentUpdateDTO updateDTO = new DepartmentUpdateDTO();
            updateDTO.setParentDeptId(newParentDeptId);
            DepartmentResponseDTO department = departmentApplicationService.updateDepartment(deptId, updateDTO, tenantId, operator);
            return JsonResponse.buildSuccess(department);
        } catch (Exception e) {
            return JsonResponse.buildFailure("移动部门失败: " + e.getMessage());
        }
    }
}