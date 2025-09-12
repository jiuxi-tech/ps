package com.jiuxi.admin.core.controller.app;

import com.jiuxi.admin.core.bean.query.TpTreePermQuery;
import com.jiuxi.admin.core.service.TpDataPermissionsService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: TpDataPermissionsController
 * @Description: 数据权限
 * @Author 杨占锐
 * @Date 2023/11/1 15:39
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/app/sys/data-perm")
@Authorization
public class TpDataPermissionsControllerApp {

    @Autowired
    private TpDataPermissionsService tpDataPermissionsService;



    /**
     * 查询当前登录人的所有权限部门id，以及权限部门id的父级id
     * <pre>
     *     1. 查询当前登录人的部门
     *     2. 判断部门类型，是企业还是政府
     *     3. 企业则执行企业的查询方法
     *     4. 政府则执行政府的查询方法
     * </pre>
     *
     * @param jwtpid 当前登录人id
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author 杨占锐
     * @date 2023/11/1 17:00
     */
    @RequestMapping(value = "/treePerm")
    @IgnoreAuthorization
    public JsonResponse treePerm(String jwtpid, String jwtdid, TpTreePermQuery permQuery) {

        permQuery.setPersonId(jwtpid);
        permQuery.setDepId(jwtdid);
        List<TreeNode> list = tpDataPermissionsService.treePerm(permQuery);
        return JsonResponse.buildSuccess(list);
    }

}
