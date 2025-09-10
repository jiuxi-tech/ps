package com.jiuxi.admin.core.controller.app;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.service.TpDeptBasicinfoService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.core.core.annotation.IgnoreAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TpDeptBasicinfoController
 * @Description: 单位/部门/网格/其他基本信息表
 * @Author pand
 * @Date 2020-11-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/app/sys/dept")
@Authorization
public class TpDeptBasicinfoControllerApp {

    @Autowired
    private TpDeptBasicinfoService tpDeptBasicinfoService;

    /**
     * 企业部门树
     *
     * @param sync:   1:同部查询  0：异步查询
     * @param deptId: 前端传来的deptId
     * @param jwtdid: token解析出来的token
     * @return com.jiuxi.common.bean.JsonResponse
     * @author pand
     * @date 2021-05-08 10:53
     */
    @RequestMapping("/org/tree")
    @IgnoreAuthorization
    public JsonResponse orgTree(@RequestParam(value = "sync", defaultValue = "1") int sync, @RequestParam(value = "deptId", required = false) String deptId, String jwtdid) {
        // deptId传空，获取当前登录人所在单位的id，如果单位为1111111111111111111（即超级管理员），
        // 则查询所有政府组织机构树， 如果当前登录人单位不为1111111111111111111，则根据查询当前登录人所在单位及下级单位的树。
        List<TreeNode> treeNode = tpDeptBasicinfoService.tree(deptId, jwtdid, sync, TpConstant.Category.ORG);
        return JsonResponse.buildSuccess(treeNode);
    }

    /**
     * 完整的机构树
     *
     * @return com.jiuxi.mvc.bean.JsonResponse
     * @author 杨攀
     * @date 2020/11/20 11:17
     */
    @RequestMapping("/org/all-tree")
    @IgnoreAuthorization
    public JsonResponse orgAllTree(@RequestParam(required = false, defaultValue = "1") int sync, String deptId) {
        if (StrUtil.isBlank(deptId)) {
            deptId = TpConstant.NODE.TOP_NODE_ID;
        }
        List<TreeNode> treeNode = tpDeptBasicinfoService.tree(deptId, TpConstant.NODE.TOP_NODE_ID, sync, TpConstant.Category.ORG);
        return JsonResponse.buildSuccess(treeNode);
    }

    /**
     * 当前登陆人所在单位的行政区划树 本级及下级树
     *
     * @param jwtaid 当前登陆人所在单位id
     * @return com.jiuxi.mvc.bean.JsonResponse
     * @author 杨攀
     * @date 2020/11/20 11:17
     */
    @RequestMapping("/org/dept-city-tree")
    @IgnoreAuthorization
    public JsonResponse deptCityTree(String jwtaid,
                                     int sync,
                                     @RequestParam(value = "expand", required = false, defaultValue = "false") boolean expand,
                                     @RequestParam(value = "showTop", required = false, defaultValue = "off") String showTop) {
        List<TreeNode> list = new ArrayList<>();
        TreeNode rootNode = tpDeptBasicinfoService.deptCityTree(jwtaid, sync, expand);

        if (StrUtil.equals(showTop, "off")) {
            list = rootNode.getChildren();
        } else {
            list.add(rootNode);
        }

        return JsonResponse.buildSuccess(list);
    }

    /**
     * 部门树
     *
     * @return com.jiuxi.mvc.bean.JsonResponse
     * @author 杨攀
     * @date 2020/11/20 11:17
     */
    @RequestMapping("/ent/tree")
    @IgnoreAuthorization
    public JsonResponse entTree(@RequestParam(value = "sync", defaultValue = "1") int sync,
                                @RequestParam(value = "deptId", required = false) String deptId,
                                String jwtdid) {

        if (StrUtil.equals(jwtdid, TpConstant.ADMIN.PERSONID)) {
            // 如果是admin账号查询，为了防止查询数据太多，页面卡死，admin查询返回空
            return JsonResponse.buildSuccess(new ArrayList<>());
        }

        List<TreeNode> treeNode = tpDeptBasicinfoService.tree(deptId, jwtdid, sync, TpConstant.Category.ENT);
        return JsonResponse.buildSuccess(treeNode);
    }

}
