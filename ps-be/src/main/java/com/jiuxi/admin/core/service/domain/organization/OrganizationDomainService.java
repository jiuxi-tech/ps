package com.jiuxi.admin.core.service.domain.organization;

import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpCityVO;
import com.jiuxi.admin.core.bean.vo.TpMediorgVO;
import com.jiuxi.mybatis.util.Query;
import com.jiuxi.common.bean.JsonResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 组织架构领域服务接口
 * 
 * 定义组织架构相关的核心业务逻辑，包括：
 * - 组织层级管理
 * - 部门生命周期管理
 * - 组织权限验证
 * 
 * @author 系统重构
 * @since 2.2.2
 */
public interface OrganizationDomainService {

    /**
     * 创建部门
     * 包含层级验证、权限检查、数据完整性保证
     * 
     * @param deptVO 部门信息
     * @return 创建结果
     */
    JsonResponse createDepartment(TpDeptBasicinfoVO deptVO);

    /**
     * 更新部门信息
     * 确保组织层级的一致性
     * 
     * @param deptVO 部门信息
     * @return 更新结果
     */
    JsonResponse updateDepartment(TpDeptBasicinfoVO deptVO);

    /**
     * 删除部门
     * 处理子部门和关联用户的迁移
     * 
     * @param deptId 部门ID
     * @return 删除结果
     */
    JsonResponse deleteDepartment(String deptId);

    /**
     * 获取部门树结构
     * 
     * @param rootDeptId 根部门ID，为空则获取全部
     * @return 部门树
     */
    List<TpDeptBasicinfoVO> getDepartmentTree(String rootDeptId);

    /**
     * 分页查询部门
     * 
     * @param query 查询条件
     * @return 分页结果
     */
    Page<TpDeptBasicinfoVO> pageDepartments(Query<TpDeptBasicinfoVO> query);

    /**
     * 移动部门
     * 调整部门在组织架构中的位置
     * 
     * @param deptId 部门ID
     * @param newParentId 新父部门ID
     * @return 移动结果
     */
    JsonResponse moveDepartment(String deptId, String newParentId);

    /**
     * 获取行政区划树
     * 
     * @param parentCode 父级区划代码
     * @return 区划树
     */
    List<TpCityVO> getCityTree(String parentCode);

    /**
     * 获取医疗机构列表
     * 
     * @param cityCode 区划代码
     * @return 医疗机构列表
     */
    List<TpMediorgVO> getMedicalOrganizations(String cityCode);

    /**
     * 验证部门层级是否合法
     * 
     * @param deptId 部门ID
     * @param parentId 父部门ID
     * @return 是否合法
     */
    boolean isValidHierarchy(String deptId, String parentId);

    /**
     * 获取部门路径
     * 
     * @param deptId 部门ID
     * @return 部门路径（从根到当前部门）
     */
    List<TpDeptBasicinfoVO> getDepartmentPath(String deptId);
}