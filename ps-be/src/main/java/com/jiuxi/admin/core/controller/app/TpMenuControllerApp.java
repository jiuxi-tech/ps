package com.jiuxi.admin.core.controller.app;

import com.jiuxi.shared.common.base.vo.MenuTreeNode;
import com.jiuxi.admin.core.service.TpMenuService;
import com.jiuxi.common.bean.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName: TpMenuController
 * @Description: 菜单表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/app/sys/menu")
public class TpMenuControllerApp {

    @Autowired
    private TpMenuService tpMenuService;

    /**
     * 登陆成功后，根据用户查询用户拥有的菜单树
     */
    @RequestMapping("/main-tree")
    public JsonResponse tree(String jwtpid, String jwtrids) {
        Set<MenuTreeNode> set = tpMenuService.mainAppTree(jwtpid, jwtrids);
        List<String> list = set.stream().map(MenuTreeNode::getCode).collect(Collectors.toList());

        StringBuffer sb = new StringBuffer();
        list.forEach(s ->
                sb.append(s).append(",")
        );


        return JsonResponse.buildSuccess(sb.toString());
    }

}
