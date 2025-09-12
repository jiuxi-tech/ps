package com.jiuxi.admin.core.controller.pc;

import com.jiuxi.admin.core.service.TpMenuService;
import com.jiuxi.admin.bean.MenuTreeNode;
import com.jiuxi.admin.core.bean.vo.TpMenuVO;
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

/**
 * @ClassName: TpMenuController
 * @Description: 菜单表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/menu")
@Authorization
public class TpMenuController {

    @Autowired
    private TpMenuService tpMenuService;
    


    /**
     * 自定义参数绑定，处理Boolean类型的'null'字符串
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Boolean.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if ("null".equals(text) || text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    setValue(Boolean.valueOf(text));
                }
            }
        });
    }

    /**
     * 查询所有菜单-菜单管理页面使用
     *
     * @param menuId 菜单id
     * @author 杨占锐
     * @date 2024/5/16 14:49
     */
    @RequestMapping("/tree")
    public JsonResponse tree(String menuId) {

        List<TreeNode> list = tpMenuService.tree(menuId);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 登陆成功后，根据用户查询用户拥有的菜单树
     */
    @RequestMapping("/main-tree")
    @IgnoreAuthorization
    public JsonResponse mainTree(String jwtpid, String jwtrids) {
        List<MenuTreeNode> list = tpMenuService.mainTree(jwtpid, jwtrids);

        return JsonResponse.buildSuccess(list);
    }

    /**
     * 信息
     */
    @RequestMapping("/view")
    public JsonResponse view(String menuId) {
        TpMenuVO vo = tpMenuService.view(menuId);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/add")
    public JsonResponse save(@Validated(value = AddGroup.class) TpMenuVO vo, String jwtpid) {
        vo = tpMenuService.save(vo, jwtpid);

        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpMenuVO vo, String jwtpid) {
        vo = tpMenuService.update(vo, jwtpid);

        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String menuId, String jwtpid) {
        tpMenuService.delete(menuId, jwtpid);

        return JsonResponse.buildSuccess();
    }
    


}
