package com.jiuxi.module.org.intf.web.controller.query;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import com.jiuxi.module.org.app.query.dto.DepartmentResponseDTO;
import com.jiuxi.module.org.app.query.dto.DepartmentStatisticsDTO;
import com.jiuxi.module.org.app.service.DepartmentQueryService;
import com.jiuxi.module.org.app.service.DepartmentStatisticsService;
import com.jiuxi.module.org.app.assembler.DepartmentAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 部门查询控制器
 * 负责部门相关的读操作（查询、列表、统计、分析）
 * 采用CQRS模式，专门处理Query操作
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/api/v1/departments")
@Authorization
public class DepartmentQueryController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "deptId";

    @Autowired
    private DepartmentQueryService departmentQueryService;
    
    @Autowired
    private DepartmentStatisticsService departmentStatisticsService;
    
    @Autowired
    private DepartmentAssembler departmentAssembler;

    /**
     * 查看部门详情
     * 接口路径：GET /api/v1/departments/{deptId}
     */
    @GetMapping("/{deptId}")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse getDepartmentDetail(@PathVariable String deptId) {
        try {
            DepartmentResponseDTO department = departmentQueryService.getDepartmentById(deptId);
            return JsonResponse.buildSuccess(department);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询部门失败: " + e.getMessage());
        }
    }

    /**
     * 获取部门树形结构
     * 接口路径：GET /api/v1/departments/tree
     */
    @GetMapping("/tree")
    @IgnoreAuthorization
    public JsonResponse getDepartmentTree(@RequestHeader("X-Tenant-Id") String tenantId) {
        try {
            List<DepartmentResponseDTO> departmentTree = departmentQueryService.getDepartmentTree(tenantId);
            return JsonResponse.buildSuccess(departmentTree);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取部门树失败: " + e.getMessage());
        }
    }

    /**
     * 根据父部门查询子部门列表
     * 接口路径：GET /api/v1/departments/parent/{parentDeptId}
     */
    @GetMapping("/parent/{parentDeptId}")
    public JsonResponse getChildDepartments(@PathVariable String parentDeptId) {
        try {
            List<DepartmentResponseDTO> children = departmentQueryService.getChildDepartments(parentDeptId);
            return JsonResponse.buildSuccess(children);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询子部门失败: " + e.getMessage());
        }
    }

    /**
     * 获取根部门列表
     * 接口路径：GET /api/v1/departments/root
     */
    @GetMapping("/root")
    public JsonResponse getRootDepartments(@RequestHeader("X-Tenant-Id") String tenantId) {
        try {
            List<DepartmentResponseDTO> rootDepartments = departmentQueryService.getRootDepartments(tenantId);
            return JsonResponse.buildSuccess(rootDepartments);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询根部门失败: " + e.getMessage());
        }
    }

    /**
     * 查询部门下的用户数量
     * 接口路径：GET /api/v1/departments/{deptId}/user-count
     */
    @GetMapping("/{deptId}/user-count")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse getDepartmentUserCount(@PathVariable String deptId) {
        try {
            long userCount = departmentQueryService.getDepartmentUserCount(deptId);
            return JsonResponse.buildSuccess(userCount);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询用户数量失败: " + e.getMessage());
        }
    }
    
    // ===== 高级查询接口 =====
    
    /**
     * 查询部门的祖先链
     * 接口路径：GET /api/v1/departments/{deptId}/ancestors
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
     * 接口路径：GET /api/v1/departments/{deptId}/descendants
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
     * 接口路径：GET /api/v1/departments/level/{level}
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
     * 接口路径：GET /api/v1/departments/nested-set
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
     * 接口路径：POST /api/v1/departments/{deptId}/validate-move
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
     * 接口路径：GET /api/v1/departments/{deptId}/statistics
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
     * 接口路径：GET /api/v1/departments/overview
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
     * 接口路径：GET /api/v1/departments/level-distribution
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
     * 接口路径：GET /api/v1/departments/{deptId}/direct-children-count
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
     * 接口路径：GET /api/v1/departments/{deptId}/all-descendants-count
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
     * 接口路径：GET /api/v1/departments/max-level
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
     * 接口路径：POST /api/v1/departments/batch-with-children
     */
    @PostMapping("/batch-with-children")
    public JsonResponse getBatchDepartmentsWithChildren(
            @RequestBody List<String> deptIds,
            @RequestParam(defaultValue = "false") Boolean includeDescendants) {
        try {
            List<DepartmentResponseDTO> departments = 
                departmentQueryService.findDepartmentsWithChildren(deptIds, includeDescendants);
            return JsonResponse.buildSuccess(departments);
        } catch (Exception e) {
            return JsonResponse.buildFailure("批量查询部门失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查部门祖先关系
     * 接口路径：GET /api/v1/departments/{ancestorId}/is-ancestor-of/{descendantId}
     */
    @GetMapping("/{ancestorId}/is-ancestor-of/{descendantId}")
    public JsonResponse checkAncestorRelation(
            @PathVariable String ancestorId,
            @PathVariable String descendantId) {
        try {
            boolean isAncestor = departmentQueryService.isAncestorDepartment(ancestorId, descendantId);
            return JsonResponse.buildSuccess(isAncestor);
        } catch (Exception e) {
            return JsonResponse.buildFailure("检查祖先关系失败: " + e.getMessage());
        }
    }
}