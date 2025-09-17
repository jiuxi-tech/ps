package com.jiuxi.module.org.intf.web.controller.command;

import com.jiuxi.module.org.app.service.OrganizationDepartmentService;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpDeptExinfoVO;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 组织命令控制器
 * 负责组织部门相关的写操作（创建、更新、删除）
 * 采用CQRS模式，专门处理Command操作
 * 
 * @author DDD重构
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/sys/dept")
@Authorization
public class OrganizationCommandController {

    @Autowired
    private OrganizationDepartmentService organizationDepartmentService;

    /**
     * 添加政府组织根节点或树节点
     * 接口路径：/sys/dept/org/add
     * 创建政府类型的组织机构节点
     * 保持原有接口格式完全兼容
     *
     * @param vo 部门信息
     * @param jwtpid JWT人员ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/org/add")
    public JsonResponse orgAdd(@Validated(value = AddGroup.class) TpDeptBasicinfoVO vo, String jwtpid) {
        try {
            TpDeptBasicinfoVO result = organizationDepartmentService.add(vo, jwtpid, TpConstant.Category.ORG);
            return JsonResponse.buildSuccess(result);
        } catch (Exception e) {
            return JsonResponse.buildFailure("创建政府组织失败: " + e.getMessage());
        }
    }

    /**
     * 添加企业部门根节点或树节点
     * 接口路径：/sys/dept/ent/add
     * 创建企业类型的部门节点
     * 保持原有接口格式完全兼容
     *
     * @param vo 部门信息
     * @param jwtpid JWT人员ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/ent/add")
    public JsonResponse entAdd(@Validated(value = AddGroup.class) TpDeptBasicinfoVO vo, String jwtpid) {
        try {
            TpDeptBasicinfoVO result = organizationDepartmentService.add(vo, jwtpid, TpConstant.Category.ENT);
            return JsonResponse.buildSuccess(result);
        } catch (Exception e) {
            return JsonResponse.buildFailure("创建企业部门失败: " + e.getMessage());
        }
    }

    /**
     * 添加部门扩展信息
     * 接口路径：/sys/dept/exp-add
     * 为部门添加额外的扩展属性信息
     * 保持原有接口格式完全兼容
     *
     * @param vo 扩展信息
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/exp-add")
    public JsonResponse expAdd(@Validated(value = AddGroup.class) TpDeptExinfoVO vo) {
        try {
            vo = organizationDepartmentService.expAdd(vo);
            return JsonResponse.buildSuccess(vo);
        } catch (Exception e) {
            return JsonResponse.buildFailure("添加部门扩展信息失败: " + e.getMessage());
        }
    }

    /**
     * 修改部门基本信息
     * 接口路径：/sys/dept/update
     * 更新部门基本信息
     * 保持原有接口格式完全兼容
     *
     * @param vo 部门信息
     * @param jwtpid JWT人员ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/update")
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpDeptBasicinfoVO vo, String jwtpid) {
        try {
            TpDeptBasicinfoVO result = organizationDepartmentService.update(vo, jwtpid);
            return JsonResponse.buildSuccess(result);
        } catch (Exception e) {
            return JsonResponse.buildFailure("修改部门信息失败: " + e.getMessage());
        }
    }

    /**
     * 根据id删除部门信息
     * 接口路径：/sys/dept/delete
     * 删除指定的部门记录
     * 保持原有接口格式完全兼容
     *
     * @param deptId 部门ID
     * @param jwtpid JWT人员ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String deptId, String jwtpid) {
        try {
            organizationDepartmentService.removeById(deptId, jwtpid);
            return JsonResponse.buildSuccess();
        } catch (Exception e) {
            return JsonResponse.buildFailure("删除部门信息失败: " + e.getMessage());
        }
    }
}