package com.jiuxi.module.auth.intf.web.controller.query;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.module.auth.app.dto.MenuResponseDTO;
import com.jiuxi.module.auth.app.service.MenuApplicationService;
import com.jiuxi.module.auth.app.assembler.MenuAssembler;
import com.jiuxi.module.auth.domain.model.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单查询控制器
 * 采用CQRS架构的菜单管理查询接口
 * 
 * @author DDD Refactor
 * @date 2025-09-19
 */
@RestController("authMenuQueryController")
@RequestMapping("/api/v1/auth/menus")
@Authorization
public class MenuQueryController {

    @Autowired
    private MenuApplicationService menuApplicationService;
    
    @Autowired
    @Qualifier("menuAppAssembler")
    private MenuAssembler menuAssembler;

    /**
     * 根据ID获取菜单
     * @param menuId 菜单ID
     * @return 菜单详细信息
     */
    @GetMapping("/{menuId}")
    public JsonResponse<MenuResponseDTO> getMenuById(@PathVariable String menuId) {
        Menu menu = menuApplicationService.getMenuById(menuId);
        MenuResponseDTO responseDTO = menuAssembler.toResponseDTO(menu);
        return JsonResponse.buildSuccess(responseDTO);
    }

    /**
     * 根据菜单编码获取菜单
     */
    @GetMapping("/code/{menuCode}")
    public JsonResponse<MenuResponseDTO> getMenuByCode(
            @PathVariable String menuCode,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        Menu menu = menuApplicationService.getMenuByCode(menuCode, tenantId);
        MenuResponseDTO responseDTO = menuAssembler.toResponseDTO(menu);
        return JsonResponse.buildSuccess(responseDTO);
    }

    /**
     * 获取菜单树
     */
    @GetMapping("/tree")
    public JsonResponse<List<MenuResponseDTO>> getMenuTree(
            @RequestHeader("X-Tenant-Id") String tenantId) {
        List<Menu> menuTree = menuApplicationService.getMenuTree(tenantId);
        List<MenuResponseDTO> responseDTOs = menuTree.stream()
            .map(menuAssembler::toResponseDTO)
            .collect(java.util.stream.Collectors.toList());
        return JsonResponse.buildSuccess(responseDTOs);
    }

    /**
     * 获取子菜单列表
     */
    @GetMapping("/{parentMenuId}/children")
    public JsonResponse<List<MenuResponseDTO>> getChildMenus(
            @PathVariable String parentMenuId,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        List<Menu> childMenus = menuApplicationService.getChildMenus(parentMenuId, tenantId);
        List<MenuResponseDTO> responseDTOs = childMenus.stream()
            .map(menuAssembler::toResponseDTO)
            .collect(java.util.stream.Collectors.toList());
        return JsonResponse.buildSuccess(responseDTOs);
    }

    /**
     * 批量获取菜单
     */
    @PostMapping("/batch")
    public JsonResponse<List<MenuResponseDTO>> getMenusByIds(
            @RequestBody List<String> menuIds,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        List<Menu> menus = menuApplicationService.getMenusByIds(menuIds, tenantId);
        List<MenuResponseDTO> responseDTOs = menus.stream()
            .map(menuAssembler::toResponseDTO)
            .collect(java.util.stream.Collectors.toList());
        return JsonResponse.buildSuccess(responseDTOs);
    }

    /**
     * 检查菜单编码是否存在
     */
    @GetMapping("/exists")
    public JsonResponse<Boolean> isMenuCodeExists(
            @RequestParam String menuCode,
            @RequestHeader("X-Tenant-Id") String tenantId) {
        boolean exists = menuApplicationService.isMenuCodeExists(menuCode, tenantId);
        return JsonResponse.buildSuccess(exists);
    }
}