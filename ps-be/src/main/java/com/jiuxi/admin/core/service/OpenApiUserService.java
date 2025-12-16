package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.vo.OpenApiUserVO;

import java.util.List;

/**
 * 开放API - 用户信息查询服务
 * 
 * @author system
 * @date 2025-01-30
 */
public interface OpenApiUserService {

    /**
     * 根据人员ID查询用户信息（脱敏）
     * 
     * @param personId 人员ID
     * @return 脱敏后的用户信息
     */
    OpenApiUserVO getUserById(String personId);

    /**
     * 分页查询用户列表（脱敏）
     * 
     * @param deptId 部门ID（可选）
     * @param page 页码
     * @param size 每页条数
     * @return 脱敏后的用户列表
     */
    IPage<OpenApiUserVO> getUserList(String deptId, Integer page, Integer size);

    /**
     * 搜索用户（脱敏）
     * 
     * @param keyword 关键词（姓名、工号）
     * @param deptId 部门ID（可选）
     * @param page 页码
     * @param size 每页条数
     * @return 脱敏后的用户列表
     */
    IPage<OpenApiUserVO> searchUsers(String keyword, String deptId, Integer page, Integer size);

    /**
     * 修改用户信息
     * 
     * @param personId 人员ID
     * @param updateParams 更新参数（包含personName、phone、email、tel等字段）
     * @return 是否更新成功
     */
    boolean updateUser(String personId, java.util.Map<String, Object> updateParams);

    /**
     * 重置用户密码
     * 
     * @param personId 人员ID
     * @param newPassword 新密码
     * @return 是否重置成功
     */
    boolean resetPassword(String personId, String newPassword);

    /**
     * 同步用户到SSO
     * 
     * @param personId 人员ID
     * @return 是否同步成功
     */
    boolean syncToSso(String personId);
}
