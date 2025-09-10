package com.jiuxi.admin.core.controller.pc;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.bean.query.TpTreePermQuery;
import com.jiuxi.admin.core.bean.vo.TpDataPermissionsVO;
import com.jiuxi.admin.core.service.TpDataPermissionsService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.core.core.annotation.IgnoreAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: TpDataPermissionsController
 * @Description: 数据权限
 * @Author 杨占锐
 * @Date 2023/11/1 15:39
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/data-perm")
@Authorization
public class TpDataPermissionsController {

    @Autowired
    private TpDataPermissionsService tpDataPermissionsService;

    /**
     * 保存人员的数据权限 数据权限
     * <pre>
     *     参数示例：
     *     {
     *         personId："xxxxxx"
     *         permList: [
     *             {
     *                 deptId："xxxxxx",
     *                 ascnId："xxxxxx"
     *             },
     *             {
     *                deptId："xxxxxx",
     *                ascnId："xxxxxx"
     *             }
     *         ]
     *     }
     * </pre>
     *
     * @param permissionsVO 数据权限封装
     * @author 杨占锐
     * @date 2023/11/1 15:41
     */
    @RequestMapping(value = "/add")
    public JsonResponse add(@RequestBody TpDataPermissionsVO permissionsVO) {

        // 保存，删除原有的数据
        tpDataPermissionsService.add(permissionsVO);

        return JsonResponse.buildSuccess();
    }

    /**
     * 查询所有数据权限
     *
     * @param personId 人员id
     * @return com.jiuxi.common.bean.JsonResponse
     * @author 杨占锐
     * @date 2023/11/1 16:05
     */
    @RequestMapping(value = "/listPerm")
    @IgnoreAuthorization
    public JsonResponse listPerm(String jwtpid, String personId) {
        if (StrUtil.isBlank(personId)) {
            personId = jwtpid;
        }

        List<TpDataPermissionsVO> list = tpDataPermissionsService.listPerm(personId);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 查询所有数据权限
     *
     * @param personId 人员id
     * @return com.jiuxi.common.bean.JsonResponse
     * @author 杨占锐
     * @date 2023/11/1 16:05
     */
    @RequestMapping(value = "/listPermIds")
    @IgnoreAuthorization
    public JsonResponse listPermIds(String jwtpid, String personId) {
        if (StrUtil.isBlank(personId)) {
            personId = jwtpid;
        }

        Set<String> list = tpDataPermissionsService.listPermIds(personId);
        return JsonResponse.buildSuccess(list);
    }

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

    /**
     * 判断是否具有权限
     *
     * @param jwtpid   当前登录人id
     * @param personId 人员id，为空时取当前登录人id
     * @param deptId   部门id
     * @return 有权限返回true
     * @author 杨占锐
     * @date 2023/11/2 13:09
     */
    @RequestMapping(value = "/isExistsDataPermissions")
    @IgnoreAuthorization
    public JsonResponse isExistsDataPermissions(String jwtpid, String personId, String deptId) {
        if (StrUtil.isBlank(personId)) {
            personId = jwtpid;
        }

        boolean hasPerm = tpDataPermissionsService.isExistsDataPermissions(personId, deptId);
        return JsonResponse.buildSuccess(hasPerm);
    }
}
