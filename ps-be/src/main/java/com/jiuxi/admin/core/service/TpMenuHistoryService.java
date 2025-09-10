package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.entity.TpMenuHistory;
import com.jiuxi.admin.core.bean.query.TpMenuHistoryQuery;
import com.jiuxi.admin.core.bean.vo.TpMenuVO;

import java.util.List;

/**
 * @ClassName: TpMenuHistoryService
 * @Description: 菜单修改历史记录服务接口
 * @Author: AI Assistant
 * @Date: 2025-01-29
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpMenuHistoryService {

    /**
     * 记录菜单新增历史
     *
     * @param menuVO     新增的菜单对象
     * @param operatorId 操作人ID
     */
    void recordAddHistory(TpMenuVO menuVO, String operatorId);

    /**
     * 记录菜单修改历史
     *
     * @param beforeMenuVO 修改前的菜单对象
     * @param afterMenuVO  修改后的菜单对象
     * @param operatorId   操作人ID
     */
    void recordUpdateHistory(TpMenuVO beforeMenuVO, TpMenuVO afterMenuVO, String operatorId);

    /**
     * 记录菜单删除历史
     *
     * @param menuVO     删除的菜单对象
     * @param operatorId 操作人ID
     */
    void recordDeleteHistory(TpMenuVO menuVO, String operatorId);

    /**
     * 根据菜单ID查询历史记录
     *
     * @param menuId 菜单ID
     * @return 历史记录列表
     */
    List<TpMenuHistory> getHistoryByMenuId(String menuId);

    /**
     * 根据操作人ID查询历史记录
     *
     * @param operatorId 操作人ID
     * @return 历史记录列表
     */
    List<TpMenuHistory> getHistoryByOperatorId(String operatorId);

    /**
     * 根据操作类型查询历史记录
     *
     * @param operationType 操作类型
     * @return 历史记录列表
     */
    List<TpMenuHistory> getHistoryByOperationType(String operationType);

    /**
     * 根据历史记录ID查询详情
     *
     * @param historyId 历史记录ID
     * @return 历史记录详情
     */
    TpMenuHistory getHistoryById(String historyId);

    /**
     * 根据时间范围查询历史记录
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 历史记录列表
     */
    List<TpMenuHistory> getHistoryByTimeRange(String startTime, String endTime);

    /**
     * 获取所有历史记录
     *
     * @return 历史记录列表
     */
    List<TpMenuHistory> getAllHistory();

    /**
     * 分页查询历史记录
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpMenuHistory> queryPage(TpMenuHistoryQuery query);

    /**
     * 获取完整菜单树的JSON字符串
     *
     * @return 菜单树JSON字符串
     */
    String getFullMenuTreeJson();
}