package com.jiuxi.module.org.intf.web.controller.query;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.module.org.app.service.OrganizationDepartmentService;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpDeptExinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonDeptVO;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import java.beans.PropertyEditorSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织查询控制器
 * 负责组织部门相关的读操作（查询、列表、树形查询）
 * 采用CQRS模式，专门处理Query操作
 * 
 * @author DDD重构
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/sys/dept")
@Authorization
public class OrganizationQueryController {

    @Autowired
    private OrganizationDepartmentService organizationDepartmentService;

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
     * 政府组织机构树
     * 接口路径：POST /sys/dept/org/tree
     * 根据权限返回可见的政府组织机构树
     * 保持原有接口格式完全兼容
     *
     * @param deptId 部门ID（可选）
     * @param jwtdid JWT部门ID  
     * @param sync 同步标识（默认为1）
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/org/tree")
    public JsonResponse orgTree(@RequestParam(required = false) String deptId,
                                String jwtdid,
                                @RequestParam(required = false, defaultValue = "1") int sync) {
        try {
            // deptId传空，获取当前登录人所在单位的id，如果单位为1111111111111111111（即超级管理员），
            // 则查询所有政府组织机构树， 如果当前登录人单位不为1111111111111111111，则根据查询当前登录人所在单位及下级单位的树。
            List<TreeNode> treeNode = organizationDepartmentService.tree(deptId, jwtdid, sync, TpConstant.Category.ORG);
            return JsonResponse.buildSuccess(treeNode);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取政府组织机构树失败: " + e.getMessage());
        }
    }

    /**
     * 完整的政府组织机构树
     * 接口路径：POST /sys/dept/org/all-tree
     * 返回从指定节点开始的完整机构树
     * 保持原有接口格式完全兼容
     *
     * @param sync 同步标识（默认为1）
     * @param deptId 起始部门ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/org/all-tree")
    public JsonResponse orgAllTree(@RequestParam(required = false, defaultValue = "1") int sync, 
                                   @RequestParam(required = false) String deptId) {
        try {
            if (StrUtil.isBlank(deptId)) {
                deptId = TpConstant.NODE.TOP_NODE_ID;
            }
            List<TreeNode> treeNode = organizationDepartmentService.tree(deptId, TpConstant.NODE.TOP_NODE_ID, sync, TpConstant.Category.ORG);
            return JsonResponse.buildSuccess(treeNode);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取完整组织机构树失败: " + e.getMessage());
        }
    }

    /**
     * 查询指定部门的下一级单位或部门
     * 接口路径：POST /sys/dept/children
     * 支持按类型过滤查询下级组织
     * 保持原有接口格式完全兼容
     *
     * @param parentId 父部门ID
     * @param deptType 部门类型（SYS0501:单位, SYS0502:部门, null:全部）
     * @param filterCommAscn 过滤委员会（1:过滤）
     * @param category 分类
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/children")
    public JsonResponse children(@RequestParam String parentId, 
                                @RequestParam(required = false) String deptType, 
                                @RequestParam(required = false) String filterCommAscn, 
                                @RequestParam(required = false) Integer category) {
        try {
            List<TreeNode> treeNode = organizationDepartmentService.getChildren(parentId, deptType, filterCommAscn, category);
            return JsonResponse.buildSuccess(treeNode);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询下级部门失败: " + e.getMessage());
        }
    }

    /**
     * 当前登录人所在单位的行政区划树 本级及下级树
     * 接口路径：POST /sys/dept/org/dept-city-tree
     * 保持原有接口格式完全兼容
     *
     * @param jwtaid 当前登录人所在单位ID
     * @param sync 同步标识（默认为1）
     * @param expand 展开标识（默认false）
     * @param showTop 显示顶级节点（默认off）
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/org/dept-city-tree")
    public JsonResponse deptCityTree(String jwtaid,
                                     @RequestParam(required = false, defaultValue = "1") int sync,
                                     @RequestParam(value = "expand", required = false, defaultValue = "false") boolean expand,
                                     @RequestParam(value = "showTop", required = false, defaultValue = "off") String showTop) {
        try {
            List<TreeNode> list = new ArrayList<>();
            TreeNode rootNode = organizationDepartmentService.deptCityTree(jwtaid, sync, expand);

            if (StrUtil.equals(showTop, "off")) {
                list = rootNode.getChildren();
            } else {
                list.add(rootNode);
            }

            return JsonResponse.buildSuccess(list);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取行政区划树失败: " + e.getMessage());
        }
    }

    /**
     * 人员兼职机构树，给某人挂兼职部门
     * 接口路径：POST /sys/dept/org/parttimejob-tree
     * 用于人员兼职部门设置的机构树查询
     * 保持原有接口格式完全兼容
     *
     * @param personId 选中需要被赋予兼职部门人员ID
     * @param jwtdid 当前登录人仮称中的部门ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/org/parttimejob-tree")
    public JsonResponse orgParttimejobTree(String personId, String jwtdid) {
        try {
            List<TreeNode> treeNode = organizationDepartmentService.parttimeJobTree(personId, jwtdid, TpConstant.Category.ORG);
            return JsonResponse.buildSuccess(treeNode);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取兼职机构树失败: " + e.getMessage());
        }
    }

    /**
     * 企业部门树
     * 接口路径：POST /sys/dept/ent/tree
     * 返回企业的部门树形结构，管理员账号返回空列表
     * 保持原有接口格式完全兼容
     *
     * @param sync 同步标识（默认为1）
     * @param deptId 部门ID（可选）
     * @param jwtdid JWT部门ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/ent/tree")
    public JsonResponse entTree(@RequestParam(value = "sync", defaultValue = "1") int sync,
                                @RequestParam(value = "deptId", required = false) String deptId,
                                String jwtdid) {
        try {
            if (StrUtil.equals(jwtdid, TpConstant.ADMIN.PERSONID)) {
                // 如果是admin账号查询，为了防止查询数据太多，页面卡死，admin查询返回空
                return JsonResponse.buildSuccess(new ArrayList<>());
            }

            List<TreeNode> treeNode = organizationDepartmentService.tree(deptId, jwtdid, sync, TpConstant.Category.ENT);
            return JsonResponse.buildSuccess(treeNode);
        } catch (Exception e) {
            return JsonResponse.buildFailure("获取企业部门树失败: " + e.getMessage());
        }
    }

    /**
     * 查询部门信息
     * 接口路径：POST /sys/dept/view
     * 根据部门ID查询部门详细信息
     * 保持原有接口格式完全兼容
     *
     * @param deptId 部门ID
     * @param jwtpid JWT人员ID
     * @param jwtaid JWT单位ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/view")
    public JsonResponse view(String deptId, String jwtpid, String jwtaid) {
        try {
            TpDeptBasicinfoVO vo = organizationDepartmentService.getById(deptId, jwtpid, jwtaid);
            return JsonResponse.buildSuccess(vo);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询部门信息失败: " + e.getMessage());
        }
    }

    /**
     * 查询部门扩展信息
     * 接口路径：POST /sys/dept/exp-view
     * 获取部门的扩展属性信息
     * 保持原有接口格式完全兼容
     *
     * @param deptId 部门ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/exp-view")
    public JsonResponse expView(@RequestParam String deptId) {
        try {
            TpDeptExinfoVO vo = organizationDepartmentService.expView(deptId);
            return JsonResponse.buildSuccess(vo);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询部门扩展信息失败: " + e.getMessage());
        }
    }

    /**
     * 删除部门前查询，如果有绑定人员，提醒是否确定删除
     * 接口路径：POST /sys/dept/select/deptid
     * 删除前检查，如果有绑定人员需要确认删除
     * 保持原有接口格式完全兼容
     *
     * @param deptId 部门ID
     * @return com.jiuxi.common.bean.JsonResponse 绑定人员数量
     */
    @RequestMapping("/select/deptid")
    public JsonResponse selectBindByDeptid(@RequestParam String deptId) {
        try {
            List<TpPersonDeptVO> list = organizationDepartmentService.selectBindByDeptId(deptId);

            if (null != list && !list.isEmpty()) {
                return JsonResponse.buildSuccess(list.size());
            } else {
                return JsonResponse.buildSuccess(0);
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询部门绑定人员失败: " + e.getMessage());
        }
    }
}