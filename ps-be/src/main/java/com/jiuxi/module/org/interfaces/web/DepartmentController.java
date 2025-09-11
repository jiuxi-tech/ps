package com.jiuxi.module.org.interfaces.web;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.core.core.annotation.IgnoreAuthorization;
import com.jiuxi.core.core.validator.group.AddGroup;
import com.jiuxi.core.core.validator.group.UpdateGroup;
import com.jiuxi.module.org.app.dto.DepartmentCreateDTO;
import com.jiuxi.module.org.app.dto.DepartmentResponseDTO;
import com.jiuxi.module.org.app.dto.DepartmentUpdateDTO;
import com.jiuxi.module.org.app.dto.DepartmentQueryDTO;
import com.jiuxi.module.org.app.dto.DepartmentStatisticsDTO;
import com.jiuxi.module.org.app.service.DepartmentApplicationService;
import com.jiuxi.module.org.app.service.DepartmentQueryService;
import com.jiuxi.module.org.app.service.DepartmentStatisticsService;
import com.jiuxi.module.org.app.assembler.DepartmentAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 部门管理控制器
 * 采用DDD架构的部门管理接口
 * 提供部门增删改查、树形查询、统计分析等功能
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
    
    @Autowired
    private DepartmentQueryService departmentQueryService;
    
    @Autowired
    private DepartmentStatisticsService departmentStatisticsService;
    
    @Autowired
    private DepartmentAssembler departmentAssembler;

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
    
    // ===== 高级查询接口 =====
    
    /**
     * 查询部门的祖先链
     * 获取指定部门的所有祖先部门，从根部门到直接父部门
     * @param deptId 部门ID
     * @return 祖先部门列表
     */
    @GetMapping("/{deptId}/ancestors")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse getDepartmentAncestors(@PathVariable String deptId) {
        try {
            List<DepartmentResponseDTO> ancestors = departmentQueryService.findAncestorChain(deptId);
            return JsonResponse.buildSuccess(ancestors);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询祖先部门失败: " + e.getMessage());
        }
    }
    
    /**
     * 查询部门的完整子树
     */
    @GetMapping("/{deptId}/descendants")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse getDepartmentDescendants(
            @PathVariable String deptId,
            @RequestParam(defaultValue = "false") Boolean includeInactive) {
        try {
            List<DepartmentResponseDTO> descendants = departmentQueryService.findCompleteSubTree(deptId, includeInactive);
            return JsonResponse.buildSuccess(descendants);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询子部门失败: " + e.getMessage());
        }
    }
    
    /**
     * 按层级查询部门
     */
    @GetMapping("/level/{level}")
    public JsonResponse getDepartmentsByLevel(
            @PathVariable Integer level,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        try {
            List<DepartmentResponseDTO> departments = departmentQueryService.findDepartmentsByLevel(level, tenantId);
            return JsonResponse.buildSuccess(departments);
        } catch (Exception e) {
            return JsonResponse.buildFailure("按层级查询部门失败: " + e.getMessage());
        }
    }
    
    /**
     * 左右值编码查询（高效树形查询）
     */
    @GetMapping("/nested-set")
    public JsonResponse getDepartmentsByNestedSet(
            @RequestParam Integer leftValue,
            @RequestParam Integer rightValue,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        try {
            List<DepartmentResponseDTO> departments = departmentQueryService.findByLeftRightValue(leftValue, rightValue, tenantId);
            return JsonResponse.buildSuccess(departments);
        } catch (Exception e) {
            return JsonResponse.buildFailure("嵌套集合查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证部门移动操作
     */
    @PostMapping("/{deptId}/validate-move")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse validateDepartmentMove(
            @PathVariable String deptId,
            @RequestParam String targetParentId) {
        try {
            DepartmentQueryService.DepartmentMoveValidationResult validation = 
                departmentQueryService.validateDepartmentMove(deptId, targetParentId);
            return JsonResponse.buildSuccess(validation);
        } catch (Exception e) {
            return JsonResponse.buildFailure("移动验证失败: " + e.getMessage());
        }
    }
    
    // ===== 统计分析接口 =====
    
    /**
     * 获取部门详细统计信息
     * 获取部门的详细统计信息，包括子部门数量、用户数量、层级信息等
     * @param deptId 部门ID
     * @return 部门详细统计信息
     */
    @GetMapping("/{deptId}/statistics")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse getDepartmentStatistics(@PathVariable String deptId) {
        try {
            DepartmentStatisticsService.DepartmentStatistics statistics = 
                departmentStatisticsService.getDepartmentStatistics(deptId);
            DepartmentStatisticsDTO statisticsDTO = departmentAssembler.toStatisticsDTO(statistics);
            return JsonResponse.buildSuccess(statisticsDTO);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取部门统计失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取租户部门概览统计
     * 获取租户下所有部门的概览统计，包括总数、激活数、层级分布等
     * @param tenantId 租户ID
     * @return 租户部门概览统计
     */
    @GetMapping("/overview")
    public JsonResponse getTenantDepartmentOverview(@RequestHeader("X-Tenant-Id") String tenantId) {
        try {
            DepartmentStatisticsService.DepartmentOverview overview = 
                departmentStatisticsService.getTenantDepartmentOverview(tenantId);
            DepartmentAssembler.TenantDepartmentOverviewDTO overviewDTO = 
                departmentAssembler.toOverviewDTO(overview);
            return JsonResponse.buildSuccess(overviewDTO);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取部门概览失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取部门层级分布统计
     */
    @GetMapping("/level-distribution")
    public JsonResponse getLevelDistribution(@RequestHeader("X-Tenant-Id") String tenantId) {
        try {
            Map<Integer, Long> distribution = departmentStatisticsService.getLevelDistribution(tenantId);
            return JsonResponse.buildSuccess(distribution);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取层级分布失败: " + e.getMessage());
        }
    }
    
    /**
     * 统计直接子部门数量
     */
    @GetMapping("/{deptId}/direct-children-count")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse countDirectChildren(@PathVariable String deptId) {
        try {
            long count = departmentStatisticsService.countDirectChildren(deptId);
            return JsonResponse.buildSuccess(count);
        } catch (Exception e) {
            return JsonResponse.buildFailure("统计直接子部门失败: " + e.getMessage());
        }
    }
    
    /**
     * 统计所有后代部门数量
     */
    @GetMapping("/{deptId}/all-descendants-count")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse countAllDescendants(@PathVariable String deptId) {
        try {
            long count = departmentStatisticsService.countAllDescendants(deptId);
            return JsonResponse.buildSuccess(count);
        } catch (Exception e) {
            return JsonResponse.buildFailure("统计后代部门失败: " + e.getMessage());
        }
    }
    
    /**
     * 查询租户最大部门层级
     */
    @GetMapping("/max-level")
    public JsonResponse getMaxDepartmentLevel(@RequestHeader("X-Tenant-Id") String tenantId) {
        try {
            Integer maxLevel = departmentQueryService.findMaxDepartmentLevel(tenantId);
            return JsonResponse.buildSuccess(maxLevel);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询最大层级失败: " + e.getMessage());
        }
    }
    
    // ===== 批量操作接口 =====
    
    /**
     * 批量查询部门及其子部门
     */
    @PostMapping("/batch-with-children")
    public JsonResponse getBatchDepartmentsWithChildren(
            @RequestBody List<String> deptIds,
            @RequestParam(defaultValue = "false") Boolean includeDescendants) {
        try {
            List<DepartmentResponseDTO> departments = 
                departmentApplicationService.findDepartmentsWithChildren(deptIds, includeDescendants);
            return JsonResponse.buildSuccess(departments);
        } catch (Exception e) {
            return JsonResponse.buildFailure("批量查询部门失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查部门祖先关系
     */
    @GetMapping("/{ancestorId}/is-ancestor-of/{descendantId}")
    public JsonResponse checkAncestorRelation(
            @PathVariable String ancestorId,
            @PathVariable String descendantId) {
        try {
            boolean isAncestor = departmentApplicationService.isAncestorDepartment(ancestorId, descendantId);
            return JsonResponse.buildSuccess(isAncestor);
        } catch (Exception e) {
            return JsonResponse.buildFailure("检查祖先关系失败: " + e.getMessage());
        }
    }
}