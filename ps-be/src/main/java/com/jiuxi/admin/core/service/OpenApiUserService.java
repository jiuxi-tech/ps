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
}
