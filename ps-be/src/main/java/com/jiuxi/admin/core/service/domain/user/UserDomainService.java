package com.jiuxi.admin.core.service.domain.user;

import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import com.jiuxi.admin.core.bean.query.TpPersonBasicQuery;
import com.jiuxi.common.bean.JsonResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 用户领域服务接口
 * 
 * 定义用户相关的核心业务逻辑，包括：
 * - 用户生命周期管理
 * - 用户权限验证
 * - 用户数据完整性保证
 * 
 * @author 系统重构
 * @since 2.2.2
 */
public interface UserDomainService {

    /**
     * 创建用户
     * 包含用户基本信息验证、账户创建、权限分配等完整流程
     * 
     * @param personVO 用户基本信息
     * @param accountVO 用户账户信息
     * @return 创建结果
     */
    JsonResponse createUser(TpPersonBasicinfoVO personVO, TpAccountVO accountVO);

    /**
     * 更新用户信息
     * 确保数据一致性和业务规则完整性
     * 
     * @param personVO 用户信息
     * @return 更新结果
     */
    JsonResponse updateUser(TpPersonBasicinfoVO personVO);

    /**
     * 删除用户
     * 处理用户相关的所有关联数据
     * 
     * @param personId 用户ID
     * @return 删除结果
     */
    JsonResponse deleteUser(String personId);

    /**
     * 分页查询用户
     * 
     * @param query 查询条件
     * @return 分页结果
     */
    Page<TpPersonBasicinfoVO> pageUsers(TpPersonBasicQuery query);

    /**
     * 根据部门查询用户
     * 
     * @param deptId 部门ID
     * @return 用户列表
     */
    List<TpPersonBasicinfoVO> getUsersByDept(String deptId);

    /**
     * 验证用户是否有效
     * 
     * @param personId 用户ID
     * @return 是否有效
     */
    boolean isValidUser(String personId);

    /**
     * 重置用户密码
     * 
     * @param personId 用户ID
     * @param newPassword 新密码
     * @return 重置结果
     */
    JsonResponse resetPassword(String personId, String newPassword);

    /**
     * 分配用户角色
     * 
     * @param personId 用户ID
     * @param roleIds 角色ID列表
     * @return 分配结果
     */
    JsonResponse assignRoles(String personId, List<String> roleIds);
}