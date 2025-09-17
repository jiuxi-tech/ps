package com.jiuxi.module.org.intf.web.controller.query;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.module.org.app.service.OrganizationDepartmentService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织部门APP查询控制器
 * 负责移动端组织部门相关的读操作（查询、树形查询）
 * 采用CQRS模式，专门处理移动端Query操作
 * 保持与主接口一致的DDD模式，保持原有API接口完全兼容
 * 
 * @author DDD重构
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/app/sys/dept")
@Authorization
public class OrganizationAppQueryController {

    @Autowired
    private OrganizationDepartmentService organizationDepartmentService;

    /**
     * 移动端政府组织机构树
     * 接口路径：POST /app/sys/dept/org/tree
     * 移动端专用的机构树查询接口，无需权限验证
     * 保持原有接口格式完全兼容
     *
     * @param sync 同步标识（1:同步查询 0:异步查询，默认1）
     * @param deptId 前端传来的部门ID（可选）
     * @param jwtdid JWT解析出来的部门ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/org/tree")
    @IgnoreAuthorization
    public JsonResponse orgTree(@RequestParam(value = "sync", defaultValue = "1") int sync, 
                               @RequestParam(value = "deptId", required = false) String deptId, 
                               String jwtdid) {
        try {
            // deptId传空，获取当前登录人所在单位的id，如果单位为1111111111111111111（即超级管理员），
            // 则查询所有政府组织机构树， 如果当前登录人单位不为1111111111111111111，则根据查询当前登录人所在单位及下级单位的树。
            List<TreeNode> treeNode = organizationDepartmentService.tree(deptId, jwtdid, sync, TpConstant.Category.ORG);
            return JsonResponse.buildSuccess(treeNode);
        } catch (Exception e) {
            return JsonResponse.buildFailure("移动端获取组织机构树失败: " + e.getMessage());
        }
    }

    /**
     * 移动端完整机构树
     * 接口路径：POST /app/sys/dept/org/all-tree
     * 移动端获取完整的机构树形结构
     * 保持原有接口格式完全兼容
     *
     * @param sync 同步标识（默认1）
     * @param deptId 部门ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/org/all-tree")
    @IgnoreAuthorization
    public JsonResponse orgAllTree(@RequestParam(required = false, defaultValue = "1") int sync, 
                                   @RequestParam(required = false) String deptId) {
        try {
            if (StrUtil.isBlank(deptId)) {
                deptId = TpConstant.NODE.TOP_NODE_ID;
            }
            List<TreeNode> treeNode = organizationDepartmentService.tree(deptId, TpConstant.NODE.TOP_NODE_ID, sync, TpConstant.Category.ORG);
            return JsonResponse.buildSuccess(treeNode);
        } catch (Exception e) {
            return JsonResponse.buildFailure("移动端获取完整机构树失败: " + e.getMessage());
        }
    }

    /**
     * 移动端行政区划树
     * 接口路径：POST /app/sys/dept/org/dept-city-tree
     * 移动端专用的行政区划树查询
     * 保持原有接口格式完全兼容
     *
     * @param jwtaid 当前登录人所在单位ID
     * @param sync 同步标识
     * @param expand 展开标识（默认false）
     * @param showTop 显示顶级节点（默认off）
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/org/dept-city-tree")
    @IgnoreAuthorization
    public JsonResponse deptCityTree(String jwtaid,
                                     @RequestParam int sync,
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
            return JsonResponse.buildFailure("移动端获取行政区划树失败: " + e.getMessage());
        }
    }

    /**
     * 移动端企业部门树
     * 接口路径：POST /app/sys/dept/ent/tree
     * 移动端专用的企业部门树查询，管理员返回空列表
     * 保持原有接口格式完全兼容
     *
     * @param sync 同步标识（默认1）
     * @param deptId 部门ID（可选）
     * @param jwtdid JWT部门ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/ent/tree")
    @IgnoreAuthorization
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
            return JsonResponse.buildFailure("移动端获取企业部门树失败: " + e.getMessage());
        }
    }
}