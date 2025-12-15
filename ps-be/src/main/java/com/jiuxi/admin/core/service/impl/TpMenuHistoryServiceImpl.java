package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpMenuHistory;
import com.jiuxi.admin.core.bean.query.TpMenuHistoryQuery;
import com.jiuxi.admin.core.bean.vo.TpMenuVO;
import com.jiuxi.admin.core.mapper.TpMenuHistoryMapper;
import com.jiuxi.admin.core.mapper.TpMenuMapper;
import com.jiuxi.admin.core.service.TpMenuHistoryService;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: TpMenuHistoryServiceImpl
 * @Description: 菜单修改历史记录服务实现类
 * @Author: AI Assistant
 * @Date: 2025-01-29
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpMenuHistoryService")
public class TpMenuHistoryServiceImpl implements TpMenuHistoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpMenuHistoryServiceImpl.class);

    @Autowired
    private TpMenuHistoryMapper tpMenuHistoryMapper;

    @Autowired
    private TpMenuMapper tpMenuMapper;

    /**
     * 记录菜单新增历史
     *
     * @param menuVO     新增的菜单对象
     * @param operatorId 操作人ID
     */
    @Override
    public void recordAddHistory(TpMenuVO menuVO, String operatorId) {
        try {
            TpMenuHistory history = new TpMenuHistory();
            history.setHistoryId(SnowflakeIdUtil.nextIdStr());
            history.setMenuId(menuVO.getMenuId());
            history.setMenuName(menuVO.getMenuName());
            history.setOperationType("ADD");
            history.setNodeDataBefore(""); // 新增前无数据
            history.setNodeDataAfter(JSONObject.toJSONString(menuVO));
            history.setFullTreeBefore(getFullMenuTreeJson());
            history.setFullTreeAfter(getFullMenuTreeJson()); // 新增后的完整树
            history.setOperatorId(operatorId);
            history.setOperatorName(getOperatorName(operatorId));
            history.setOperationTime(getCurrentTimeString());
            
            String now = CommonDateUtil.now();
            history.setCreateTime(now);
            history.setCreator(operatorId);
            history.setUpdateTime(now);
            history.setUpdator(operatorId);
            
            tpMenuHistoryMapper.save(history);
            LOGGER.info("菜单新增历史记录成功，菜单ID：{}, 操作人：{}", menuVO.getMenuId(), operatorId);
        } catch (Exception e) {
            LOGGER.error("记录菜单新增历史失败，菜单ID：{}, 操作人：{}, 错误：{}", menuVO.getMenuId(), operatorId, e.getMessage(), e);
        }
    }

    /**
     * 记录菜单修改历史
     *
     * @param beforeMenuVO 修改前的菜单对象
     * @param afterMenuVO  修改后的菜单对象
     * @param operatorId   操作人ID
     */
    @Override
    public void recordUpdateHistory(TpMenuVO beforeMenuVO, TpMenuVO afterMenuVO, String operatorId) {
        try {
            TpMenuHistory history = new TpMenuHistory();
            history.setHistoryId(SnowflakeIdUtil.nextIdStr());
            history.setMenuId(afterMenuVO.getMenuId());
            history.setMenuName(afterMenuVO.getMenuName());
            history.setOperationType("UPDATE");
            history.setNodeDataBefore(JSONObject.toJSONString(beforeMenuVO));
            history.setNodeDataAfter(JSONObject.toJSONString(afterMenuVO));
            history.setFullTreeBefore(getFullMenuTreeJson());
            history.setFullTreeAfter(getFullMenuTreeJson()); // 修改后的完整树
            history.setOperatorId(operatorId);
            history.setOperatorName(getOperatorName(operatorId));
            history.setOperationTime(getCurrentTimeString());
            
            String now = CommonDateUtil.now();
            history.setCreateTime(now);
            history.setCreator(operatorId);
            history.setUpdateTime(now);
            history.setUpdator(operatorId);
            
            tpMenuHistoryMapper.save(history);
            LOGGER.info("菜单修改历史记录成功，菜单ID：{}, 操作人：{}", afterMenuVO.getMenuId(), operatorId);
        } catch (Exception e) {
            LOGGER.error("记录菜单修改历史失败，菜单ID：{}, 操作人：{}, 错误：{}", afterMenuVO.getMenuId(), operatorId, e.getMessage(), e);
        }
    }

    /**
     * 记录菜单删除历史
     *
     * @param menuVO     删除的菜单对象
     * @param operatorId 操作人ID
     */
    @Override
    public void recordDeleteHistory(TpMenuVO menuVO, String operatorId) {
        try {
            TpMenuHistory history = new TpMenuHistory();
            history.setHistoryId(SnowflakeIdUtil.nextIdStr());
            history.setMenuId(menuVO.getMenuId());
            history.setMenuName(menuVO.getMenuName());
            history.setOperationType("DELETE");
            history.setNodeDataBefore(JSONObject.toJSONString(menuVO));
            history.setNodeDataAfter(""); // 删除后无数据
            history.setFullTreeBefore(getFullMenuTreeJson());
            history.setFullTreeAfter(getFullMenuTreeJson()); // 删除后的完整树
            history.setOperatorId(operatorId);
            history.setOperatorName(getOperatorName(operatorId));
            history.setOperationTime(getCurrentTimeString());
            
            String now = CommonDateUtil.now();
            history.setCreateTime(now);
            history.setCreator(operatorId);
            history.setUpdateTime(now);
            history.setUpdator(operatorId);
            
            tpMenuHistoryMapper.save(history);
            LOGGER.info("菜单删除历史记录成功，菜单ID：{}, 操作人：{}", menuVO.getMenuId(), operatorId);
        } catch (Exception e) {
            LOGGER.error("记录菜单删除历史失败，菜单ID：{}, 操作人：{}, 错误：{}", menuVO.getMenuId(), operatorId, e.getMessage(), e);
        }
    }

    /**
     * 根据菜单ID查询历史记录
     *
     * @param menuId 菜单ID
     * @return 历史记录列表
     */
    @Override
    public List<TpMenuHistory> getHistoryByMenuId(String menuId) {
        try {
            return tpMenuHistoryMapper.selectByMenuId(menuId);
        } catch (Exception e) {
            LOGGER.error("查询菜单历史记录失败，菜单ID：{}, 错误：{}", menuId, e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "查询菜单历史记录失败");
        }
    }

    /**
     * 根据操作人ID查询历史记录
     *
     * @param operatorId 操作人ID
     * @return 历史记录列表
     */
    @Override
    public List<TpMenuHistory> getHistoryByOperatorId(String operatorId) {
        try {
            return tpMenuHistoryMapper.selectByOperatorId(operatorId);
        } catch (Exception e) {
            LOGGER.error("查询操作人历史记录失败，操作人ID：{}, 错误：{}", operatorId, e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "查询操作人历史记录失败");
        }
    }

    /**
     * 根据操作类型查询历史记录
     *
     * @param operationType 操作类型
     * @return 历史记录列表
     */
    @Override
    public List<TpMenuHistory> getHistoryByOperationType(String operationType) {
        try {
            return tpMenuHistoryMapper.selectByOperationType(operationType);
        } catch (Exception e) {
            LOGGER.error("查询操作类型历史记录失败，操作类型：{}, 错误：{}", operationType, e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "查询操作类型历史记录失败");
        }
    }

    /**
     * 根据时间范围查询历史记录
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 历史记录列表
     */
    @Override
    public List<TpMenuHistory> getHistoryByTimeRange(String startTime, String endTime) {
        try {
            return tpMenuHistoryMapper.selectByTimeRange(startTime, endTime);
        } catch (Exception e) {
            LOGGER.error("查询时间范围历史记录失败，开始时间：{}, 结束时间：{}, 错误：{}", startTime, endTime, e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "查询时间范围历史记录失败");
        }
    }

    /**
     * 查询所有历史记录
     *
     * @return 历史记录列表
     */
    @Override
    public List<TpMenuHistory> getAllHistory() {
        try {
            return tpMenuHistoryMapper.selectAll();
        } catch (Exception e) {
            LOGGER.error("查询所有历史记录失败，错误：{}", e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "查询所有历史记录失败");
        }
    }

    /**
     * 分页查询历史记录
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<TpMenuHistory> queryPage(TpMenuHistoryQuery query) {
        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);
            
            LOGGER.info("分页查询历史记录: pageNum={}, pageSize={}, query={}", pageNum, pageSize, query);
            
            Page<TpMenuHistory> page = new Page<>(pageNum, pageSize);
            IPage<TpMenuHistory> iPage = tpMenuHistoryMapper.getPage(page, query);
            
            LOGGER.info("分页查询结果: total={}, current={}, size={}, records={}", 
                iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), iPage.getRecords().size());
            
            return iPage;
        } catch (Exception e) {
            LOGGER.error("分页查询历史记录失败: query:{}, 错误: {}", query, e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "分页查询历史记录失败");
        }
    }

    /**
     * 根据历史记录ID查询详情
     *
     * @param historyId 历史记录ID
     * @return 历史记录详情
     */
    @Override
    public TpMenuHistory getHistoryById(String historyId) {
        try {
            if (StrUtil.isBlank(historyId)) {
                throw new TopinfoRuntimeException(-1, "历史记录ID不能为空");
            }
            return tpMenuHistoryMapper.selectById(historyId);
        } catch (Exception e) {
            LOGGER.error("根据ID查询历史记录失败，historyId: {}", historyId, e);
            throw new TopinfoRuntimeException(-1, "查询历史记录失败: " + e.getMessage());
        }
    }

    /**
     * 获取完整菜单树数据（JSON格式）
     *
     * @return 菜单树JSON字符串
     */
    @Override
    public String getFullMenuTreeJson() {
        try {
            // 这里需要根据实际的菜单树查询方法来获取完整的菜单树数据
            // 暂时返回空字符串，后续可以根据具体需求实现
            return "";
        } catch (Exception e) {
            LOGGER.error("获取完整菜单树数据失败，错误：{}", e.getMessage(), e);
            return "";
        }
    }

    /**
     * 获取操作人姓名
     *
     * @param operatorId 操作人ID
     * @return 操作人姓名
     */
    private String getOperatorName(String operatorId) {
        // 这里需要根据实际的用户查询方法来获取操作人姓名
        // 暂时返回操作人ID，后续可以根据具体需求实现
        if (StrUtil.isBlank(operatorId)) {
            return "未知用户";
        }
        return operatorId;
    }

    /**
     * 获取当前时间字符串（yyyyMMddHHmmss格式）
     *
     * @return 时间字符串
     */
    private String getCurrentTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }
}