package com.jiuxi.module.sys.intf.web.controller.command;

import com.jiuxi.admin.core.bean.vo.TpMenuVO;
import com.jiuxi.admin.core.service.TpMenuService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.PropertyEditorSupport;

/**
 * 系统菜单管理命令控制器
 * 负责系统菜单相关的数据变更操作 (Create, Update, Delete)
 * 基于CQRS架构设计，专注于处理命令操作
 * 
 * @author DDD Refactor - Phase 6 (Separated from SystemMenuManagementController)
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/sys/menu")
@Authorization
public class SystemMenuCommandController {

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
     * 保存菜单信息
     */
    @RequestMapping("/add")
    public JsonResponse add(@Validated(value = AddGroup.class) TpMenuVO vo, String jwtpid) {
        TpMenuVO result = tpMenuService.save(vo, jwtpid);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 保存菜单信息
     */
    @RequestMapping("/save")
    public JsonResponse save(@Validated(value = AddGroup.class) TpMenuVO vo, String jwtpid) {
        TpMenuVO result = tpMenuService.save(vo, jwtpid);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 更新菜单信息
     */
    @RequestMapping("/update")
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpMenuVO vo, String jwtpid) {
        TpMenuVO result = tpMenuService.update(vo, jwtpid);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 删除菜单信息
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String menuId, String jwtpid) {
        int result = tpMenuService.delete(menuId, jwtpid);
        return JsonResponse.buildSuccess(result);
    }
}