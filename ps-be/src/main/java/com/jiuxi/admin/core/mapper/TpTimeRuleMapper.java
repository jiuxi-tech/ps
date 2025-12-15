package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpTimeRule;
import com.jiuxi.admin.core.bean.query.TpTimeRuleQuery;
import com.jiuxi.admin.core.bean.vo.TpTimeRuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpTimeRuleMapper
 * @Description: 登录时间段控制规则表 - Mapper
 * @Author: Qdd
 * @Date: 2025-01-20
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@Mapper
public interface TpTimeRuleMapper extends BaseMapper<TpTimeRule> {

    /**
     * 分页查询时间规则
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpTimeRuleVO> getPage(Page<TpTimeRuleVO> page, @Param("query") TpTimeRuleQuery query);

    /**
     * 新增时间规则
     * @param bean 时间规则实体
     * @return 影响行数
     */
    int add(TpTimeRule bean);

    /**
     * 更新时间规则
     * @param bean 时间规则实体
     * @return 影响行数
     */
    int update(TpTimeRule bean);

    /**
     * 查看时间规则详情
     * @param id 规则ID
     * @return 时间规则VO
     */
    TpTimeRuleVO view(String id);

    /**
     * 删除时间规则（物理删除）
     * @param id 规则ID
     * @return 影响行数
     */
    int delete(@Param("id") String id);

    /**
     * 根据用户ID和角色ID列表查询有效的时间规则
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @param currentTime 当前时间（yyyyMMddHHmmss格式）
     * @return 时间规则列表
     */
    List<TpTimeRuleVO> selectValidRulesByUserAndRoles(@Param("userId") String userId, 
                                                     @Param("roleIds") List<String> roleIds, 
                                                     @Param("currentTime") String currentTime);

    /**
     * 根据用户ID查询有效的时间规则
     * @param userId 用户ID
     * @param currentTime 当前时间（yyyyMMddHHmmss格式）
     * @return 时间规则列表
     */
    List<TpTimeRuleVO> selectValidRulesByUser(@Param("userId") String userId, 
                                             @Param("currentTime") String currentTime);

    /**
     * 根据角色ID列表查询有效的时间规则
     * @param roleIds 角色ID列表
     * @param currentTime 当前时间（yyyyMMddHHmmss格式）
     * @return 时间规则列表
     */
    List<TpTimeRuleVO> selectValidRulesByRoles(@Param("roleIds") List<String> roleIds, 
                                              @Param("currentTime") String currentTime);
}