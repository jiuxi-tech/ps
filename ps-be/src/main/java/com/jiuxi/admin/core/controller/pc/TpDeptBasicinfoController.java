package com.jiuxi.admin.core.controller.pc;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.service.TpDeptBasicinfoService;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpDeptExinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonDeptVO;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import java.beans.PropertyEditorSupport;

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
@RequestMapping("/sys/dept")
@Authorization
public class TpDeptBasicinfoController {

    @Autowired
    private TpDeptBasicinfoService tpDeptBasicinfoService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 处理Boolean类型字段的"null"字符串
        binder.registerCustomEditor(Boolean.class, "checked", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty() || "null".equals(text)) {
                    setValue(null);
                } else {
                    setValue(Boolean.valueOf(text));
                }
            }
        });
        
        binder.registerCustomEditor(Boolean.class, "expand", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty() || "null".equals(text)) {
                    setValue(null);
                } else {
                    setValue(Boolean.valueOf(text));
                }
            }
        });
        
        // 处理Integer类型字段的"null"字符串
        binder.registerCustomEditor(Integer.class, "defaultDept", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty() || "null".equals(text)) {
                    setValue(null);
                } else {
                    setValue(Integer.valueOf(text));
                }
            }
        });
    }

    /**
     * 机构树
     *
     * @return com.jiuxi.mvc.bean.JsonResponse
     * @author 杨攀
     * @date 2020/11/20 11:17
     */
    @RequestMapping("/org/tree")
    public JsonResponse orgTree(String deptId,
                                String jwtdid,
                                @RequestParam(required = false, defaultValue = "1") int sync) {
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
    public JsonResponse orgAllTree(@RequestParam(required = false, defaultValue = "1") int sync, String deptId) {
        if (StrUtil.isBlank(deptId)) {
            deptId = TpConstant.NODE.TOP_NODE_ID;
        }
        List<TreeNode> treeNode = tpDeptBasicinfoService.tree(deptId, TpConstant.NODE.TOP_NODE_ID, sync, TpConstant.Category.ORG);
        return JsonResponse.buildSuccess(treeNode);
    }

    /**
     * 查询下一级单位或者部门
     * deptType=SYS0501，查下级单位
     * deptType=SYS0502，查下级部门
     * deptType=null，查所有下级
     *
     * @param filterCommAscn 过滤委员会  1：过滤
     * @return com.jiuxi.mvc.bean.JsonResponse
     * @author pand
     * @date 2023/11/20 11:17
     */
    @RequestMapping("/children")
    public JsonResponse children(String parentId, String deptType, String filterCommAscn, Integer category) {

        List<TreeNode> treeNode = tpDeptBasicinfoService.getChildren(parentId, deptType, filterCommAscn, category);
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
    public JsonResponse deptCityTree(String jwtaid,
                                     @RequestParam(required = false, defaultValue = "1") int sync,
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
     * 人员兼职机构树，给某人挂兼职部门，先查询当前登陆人所能查询出的部门，然后比较当前需要被赋予兼职部门已经所在部门。
     *
     * @param personId: 选中需要被赋予兼职部门人员id
     * @param jwtdid:   当前登陆人token中的部门id
     * @return com.jiuxi.mvc.bean.JsonResponse
     * @author pand
     * @date 2021/1/6 11:17
     */
    @RequestMapping("/org/parttimejob-tree")
    public JsonResponse orgParttimejobTree(String personId, String jwtdid) {

        List<TreeNode> treeNode = tpDeptBasicinfoService.parttimeJobTree(personId, jwtdid, TpConstant.Category.ORG);
        return JsonResponse.buildSuccess(treeNode);
    }

    /**
     * 部门树
     *
     * @return com.jiuxi.mvc.bean.JsonResponse
     * @author 杨攀
     * @date 2020/11/20 11:17
     */
    @RequestMapping("/ent/tree")
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

    /**
     * 完整的单位树
     *
     * @return com.jiuxi.mvc.bean.JsonResponse
     * @author 杨攀
     * @date 2020/11/20 11:17
     */
    @RequestMapping("/ent/all-tree")
    public JsonResponse entAllTree(@RequestParam(required = false, defaultValue = "1") int sync, String jwtaid) {

        List<TreeNode> treeNode = tpDeptBasicinfoService.tree(jwtaid, jwtaid, sync, TpConstant.Category.ENT);
        return JsonResponse.buildSuccess(treeNode);
    }

    /**
     * 人员兼职部门树，给某人挂兼职部门，先查询当前登陆人所能查询出的部门，然后比较当前需要被赋予兼职部门已经所在部门。
     *
     * @param personId: 需要被赋予兼职部门人员id
     * @param jwtdid:   当前登陆人token中的部门id
     * @return com.jiuxi.mvc.bean.JsonResponse
     * @author pand
     * @date 2021/1/6 11:17
     */
    @RequestMapping("/ent/parttimejob-tree")
    public JsonResponse entParttimejobTree(String personId, String jwtdid) {
        List<TreeNode> treeNode = tpDeptBasicinfoService.parttimeJobTree(personId, jwtdid, TpConstant.Category.ENT);
        return JsonResponse.buildSuccess(treeNode);
    }

    /**
     * 添加根/树节点（政府）
     */
    @RequestMapping(value = "/org/add")
    public JsonResponse orgAdd(@Validated(value = AddGroup.class) TpDeptBasicinfoVO vo, String jwtpid) {
        TpDeptBasicinfoVO result = tpDeptBasicinfoService.add(vo, jwtpid, TpConstant.Category.ORG);

        return JsonResponse.buildSuccess(result);
    }

    /**
     * 添加根/树节点（企业）
     */
    @RequestMapping(value = "/ent/add")
    public JsonResponse entAdd(@Validated(value = AddGroup.class) TpDeptBasicinfoVO vo, String jwtpid) {
        TpDeptBasicinfoVO result = tpDeptBasicinfoService.add(vo, jwtpid, TpConstant.Category.ENT);

        return JsonResponse.buildSuccess(result);
    }

    /**
     * 添加部门扩展信息
     */
    @RequestMapping(value = "/exp-add")
    public JsonResponse expAdd(@Validated(value = AddGroup.class) TpDeptExinfoVO vo) {
        vo = tpDeptBasicinfoService.expAdd(vo);

        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 查询部门信息
     */
    @RequestMapping(value = "/view")
    public JsonResponse view(String deptId, String jwtpid, String jwtaid) {
        TpDeptBasicinfoVO vo = tpDeptBasicinfoService.getById(deptId, jwtpid, jwtaid);

        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 查询部门扩展信息
     */
    @RequestMapping(value = "/exp-view")
    public JsonResponse expView(String deptId) {
        TpDeptExinfoVO vo = tpDeptBasicinfoService.expView(deptId);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpDeptBasicinfoVO vo, String jwtpid) {
        TpDeptBasicinfoVO result = tpDeptBasicinfoService.update(vo, jwtpid);

        return JsonResponse.buildSuccess(result);
    }

    /**
     * 删除部门前查询，如果有绑定人员，提醒是否确定删除。
     */
    @RequestMapping("/select/deptid")
    public JsonResponse selectBindByDeptid(String deptId) {
        List<TpPersonDeptVO> list = tpDeptBasicinfoService.selectBindByDeptId(deptId);

        if (null != list && !list.isEmpty()) {
            return JsonResponse.buildSuccess(list.size());
        } else {
            return JsonResponse.buildSuccess(0);
        }
    }

    /**
     * 根据id删除部门信息
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String deptId, String jwtpid) {
        tpDeptBasicinfoService.removeById(deptId, jwtpid);

        return JsonResponse.buildSuccess();
    }

}
