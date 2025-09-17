package com.jiuxi.module.org.intf.web.controller.command;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import com.jiuxi.module.org.app.command.dto.DepartmentCreateDTO;
import com.jiuxi.module.org.app.query.dto.DepartmentResponseDTO;
import com.jiuxi.module.org.app.command.dto.DepartmentUpdateDTO;
import com.jiuxi.module.org.app.service.DepartmentApplicationService;
import com.jiuxi.module.org.app.service.DepartmentQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门命令控制器
 * 负责部门相关的写操作（创建、更新、删除、状态变更）
 * 采用CQRS模式，专门处理Command操作
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/api/v1/departments")
@Authorization
public class DepartmentCommandController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "deptId";

    @Autowired
    private DepartmentApplicationService departmentApplicationService;
    
    @Autowired
    private DepartmentQueryService departmentQueryService;

    /**
     * 创建部门
     * 接口路径：POST /api/v1/departments
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
     * 接口路径：PUT /api/v1/departments/{deptId}
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
     * 删除部门
     * 接口路径：DELETE /api/v1/departments/{deptId}
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
     * 接口路径：DELETE /api/v1/departments/batch
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
     * 接口路径：PUT /api/v1/departments/{deptId}/enable
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
     * 接口路径：PUT /api/v1/departments/{deptId}/disable
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
     * 移动部门到新的父部门
     * 接口路径：PUT /api/v1/departments/{deptId}/move
     */
    @PutMapping("/{deptId}/move")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse moveDepartment(
            @PathVariable String deptId,
            @RequestParam String newParentDeptId,
            @RequestHeader("X-User-Person-Id") String operator,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        
        try {
            // 先进行移动验证
            DepartmentQueryService.DepartmentMoveValidationResult validation = 
                departmentQueryService.validateDepartmentMove(deptId, newParentDeptId);
            if (!validation.isValid()) {
                return JsonResponse.buildFailure("移动验证失败: " + validation.getMessage());
            }
            
            DepartmentUpdateDTO updateDTO = new DepartmentUpdateDTO();
            updateDTO.setParentDeptId(newParentDeptId);
            DepartmentResponseDTO department = departmentApplicationService.updateDepartment(deptId, updateDTO, tenantId, operator);
            return JsonResponse.buildSuccess(department);
        } catch (Exception e) {
            return JsonResponse.buildFailure("移动部门失败: " + e.getMessage());
        }
    }
}