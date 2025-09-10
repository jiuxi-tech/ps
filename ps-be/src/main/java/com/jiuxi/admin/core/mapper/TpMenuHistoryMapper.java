package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpMenuHistory;
import com.jiuxi.admin.core.bean.query.TpMenuHistoryQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpMenuHistoryMapper
 * @Description: 菜单修改历史记录表
 * @Author: AI Assistant
 * @Date: 2025-01-29
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpMenuHistoryMapper {

    /**
     * 保存菜单历史记录
     *
     * @param bean 历史记录对象
     * @return 影响行数
     */
    int save(TpMenuHistory bean);

    /**
     * 根据菜单ID查询历史记录
     *
     * @param menuId 菜单ID
     * @return 历史记录列表
     */
    List<TpMenuHistory> selectByMenuId(@Param("menuId") String menuId);

    /**
     * 根据操作人ID查询历史记录
     *
     * @param operatorId 操作人ID
     * @return 历史记录列表
     */
    List<TpMenuHistory> selectByOperatorId(@Param("operatorId") String operatorId);

    /**
     * 根据操作类型查询历史记录
     *
     * @param operationType 操作类型
     * @return 历史记录列表
     */
    List<TpMenuHistory> selectByOperationType(@Param("operationType") String operationType);

    /**
     * 根据时间范围查询历史记录
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 历史记录列表
     */
    List<TpMenuHistory> selectByTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询所有历史记录
     *
     * @return 历史记录列表
     */
    List<TpMenuHistory> selectAll();

    /**
     * 分页查询历史记录
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpMenuHistory> getPage(Page<TpMenuHistory> page, @Param("query") TpMenuHistoryQuery query);

    /**
     * 根据历史记录ID查询详情
     *
     * @param historyId 历史记录ID
     * @return 历史记录详情
     */
    TpMenuHistory selectById(@Param("historyId") String historyId);
}