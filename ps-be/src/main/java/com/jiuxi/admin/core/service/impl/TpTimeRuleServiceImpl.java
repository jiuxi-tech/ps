package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpTimeRule;
import com.jiuxi.admin.core.bean.query.TpTimeRuleQuery;
import com.jiuxi.admin.core.bean.vo.TpTimeRuleVO;
import com.jiuxi.admin.core.mapper.TpTimeRuleMapper;
import com.jiuxi.admin.core.service.TpTimeRuleService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: TpTimeRuleServiceImpl
 * @Description: 登录时间段控制规则表 - Service实现类
 * @Author: System
 * @Date: 2025-01-20
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@Service("tpTimeRuleService")
public class TpTimeRuleServiceImpl implements TpTimeRuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpTimeRuleServiceImpl.class);

    @Autowired
    private TpTimeRuleMapper tpTimeRuleMapper;

    /**
     * 分页查询时间规则
     * @param query 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<TpTimeRuleVO> queryPage(TpTimeRuleQuery query) {
        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);

            Page<TpTimeRuleVO> page = new Page<>(pageNum, pageSize);
            IPage<TpTimeRuleVO> iPage = tpTimeRuleMapper.getPage(page, query);

            // 转换状态和类型名称
            if (CollUtil.isNotEmpty(iPage.getRecords())) {
                iPage.getRecords().forEach(this::convertDisplayNames);
            }

            return iPage;
        } catch (Exception e) {
            LOGGER.error("时间规则列表查询失败! query:{}, 错误:{}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "时间规则列表查询失败！");
        }
    }

    /**
     * 新增时间规则
     * @param vo 时间规则VO
     * @param creatorId 创建人ID
     * @return 时间规则VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TpTimeRuleVO save(TpTimeRuleVO vo, String creatorId) {
        try {
            // 参数校验
            validateTimeRule(vo);

            TpTimeRule entity = new TpTimeRule();
            BeanUtil.copyProperties(vo, entity);

            // 设置主键和基础信息
            entity.setId(SnowflakeIdUtil.nextIdStr());
            entity.setCreatorId(creatorId);
            entity.setCreateTime(CommonDateUtil.now());
            entity.setActived(1);
            entity.setLogDelete(0);

            // 执行新增
            int result = tpTimeRuleMapper.add(entity);
            if (result <= 0) {
                throw new TopinfoRuntimeException(-1, "新增时间规则失败！");
            }

            // 返回结果
            vo.setId(entity.getId());
            vo.setCreateTime(entity.getCreateTime());
            convertDisplayNames(vo);

            LOGGER.info("新增时间规则成功，规则ID：{}, 规则名称：{}", entity.getId(), entity.getRuleName());
            return vo;
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("新增时间规则失败! vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增时间规则失败！");
        }
    }

    /**
     * 更新时间规则
     * @param vo 时间规则VO
     * @param modifierId 修改人ID
     * @return 时间规则VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TpTimeRuleVO update(TpTimeRuleVO vo, String modifierId) {
        try {
            // 参数校验
            if (StrUtil.isBlank(vo.getId())) {
                throw new TopinfoRuntimeException(-1, "规则ID不能为空！");
            }
            validateTimeRule(vo);

            TpTimeRule entity = new TpTimeRule();
            BeanUtil.copyProperties(vo, entity);

            // 设置修改信息
            entity.setModifierId(modifierId);
            entity.setModifyTime(CommonDateUtil.now());

            // 执行更新
            int result = tpTimeRuleMapper.update(entity);
            if (result <= 0) {
                throw new TopinfoRuntimeException(-1, "更新时间规则失败！");
            }

            // 返回结果
            vo.setModifierId(modifierId);
            vo.setModifyTime(entity.getModifyTime());
            convertDisplayNames(vo);

            LOGGER.info("更新时间规则成功，规则ID：{}, 规则名称：{}", entity.getId(), entity.getRuleName());
            return vo;
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("更新时间规则失败! vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "更新时间规则失败！");
        }
    }

    /**
     * 查看时间规则详情
     * @param id 规则ID
     * @return 时间规则VO
     */
    @Override
    public TpTimeRuleVO view(String id) {
        try {
            if (StrUtil.isBlank(id)) {
                throw new TopinfoRuntimeException(-1, "规则ID不能为空！");
            }

            TpTimeRuleVO vo = tpTimeRuleMapper.view(id);
            if (vo == null) {
                throw new TopinfoRuntimeException(-1, "时间规则不存在！");
            }

            convertDisplayNames(vo);
            return vo;
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("查看时间规则详情失败! id:{}, 错误:{}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看时间规则详情失败！");
        }
    }

    /**
     * 删除时间规则（物理删除）
     * @param id 规则ID
     * @param modifierId 修改人ID（保留参数以兼容接口）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id, String modifierId) {
        try {
            if (StrUtil.isBlank(id)) {
                throw new TopinfoRuntimeException(-1, "规则ID不能为空！");
            }

            int result = tpTimeRuleMapper.delete(id);
            if (result <= 0) {
                throw new TopinfoRuntimeException(-1, "删除时间规则失败！");
            }

            LOGGER.info("删除时间规则成功，规则ID：{}", id);
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("删除时间规则失败! id:{}, 错误:{}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除时间规则失败！");
        }
    }

    /**
     * 批量删除时间规则（物理删除）
     * @param ids 规则ID列表
     * @param modifierId 修改人ID（保留参数以兼容接口）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<String> ids, String modifierId) {
        try {
            if (CollUtil.isEmpty(ids)) {
                throw new TopinfoRuntimeException(-1, "规则ID列表不能为空！");
            }

            for (String id : ids) {
                int result = tpTimeRuleMapper.delete(id);
                if (result <= 0) {
                    throw new TopinfoRuntimeException(-1, "批量删除时间规则失败，规则ID：" + id);
                }
            }

            LOGGER.info("批量删除时间规则成功，规则数量：{}", ids.size());
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("批量删除时间规则失败! ids:{}, 错误:{}", JSONObject.toJSONString(ids), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "批量删除时间规则失败！");
        }
    }

    /**
     * 校验用户登录时间权限
     * @param userId 用户ID
     * @param roleIds 用户角色ID列表
     * @return 校验结果：true-允许登录，false-拒绝登录
     */
    @Override
    public boolean validateLoginTime(String userId, List<String> roleIds) {
        TpTimeRuleService.LoginTimeValidationResult result = validateLoginTimeWithReason(userId, roleIds);
        return result.isAllowed();
    }

    /**
     * 校验用户登录时间权限并返回拒绝原因
     * @param userId 用户ID
     * @param roleIds 用户角色ID列表
     * @return 校验结果对象，包含是否允许和拒绝原因
     */
    @Override
    public TpTimeRuleService.LoginTimeValidationResult validateLoginTimeWithReason(String userId, List<String> roleIds) {
        try {
            String currentTime = CommonDateUtil.now();
            LOGGER.info("验证登录时间 - 当前时间: {}, 用户ID: {}, 角色IDs: {}", currentTime, userId, JSONObject.toJSONString(roleIds));
            List<TpTimeRuleVO> rules = new ArrayList<>();

            // 查询用户相关的时间规则
            if (StrUtil.isNotBlank(userId)) {
                List<TpTimeRuleVO> userRules = tpTimeRuleMapper.selectValidRulesByUser(userId, currentTime);
                LOGGER.info("查询到用户相关规则数量: {}", userRules != null ? userRules.size() : 0);
                if (CollUtil.isNotEmpty(userRules)) {
                    for (TpTimeRuleVO rule : userRules) {
                        LOGGER.info("用户规则: ID={}, 名称={}, 开始时间={}, 结束时间={}, 允许登录={}", 
                                rule.getId(), rule.getRuleName(), rule.getStartTime(), rule.getEndTime(), rule.getAllowLogin());
                    }
                    rules.addAll(userRules);
                }
            }

            // 查询角色相关的时间规则
            if (CollUtil.isNotEmpty(roleIds)) {
                List<TpTimeRuleVO> roleRules = tpTimeRuleMapper.selectValidRulesByRoles(roleIds, currentTime);
                LOGGER.info("查询到角色相关规则数量: {}", roleRules != null ? roleRules.size() : 0);
                if (CollUtil.isNotEmpty(roleRules)) {
                    for (TpTimeRuleVO rule : roleRules) {
                        LOGGER.info("角色规则: ID={}, 名称={}, 开始时间={}, 结束时间={}, 允许登录={}", 
                                rule.getId(), rule.getRuleName(), rule.getStartTime(), rule.getEndTime(), rule.getAllowLogin());
                    }
                    rules.addAll(roleRules);
                }
            }

            // 如果没有找到相关规则，默认允许登录
            if (CollUtil.isEmpty(rules)) {
                return new TpTimeRuleService.LoginTimeValidationResult(true, null, null);
            }

            // 按规则类型分组处理
            Map<Integer, List<TpTimeRuleVO>> rulesByType = rules.stream()
                    .collect(Collectors.groupingBy(TpTimeRuleVO::getAllowLogin));

            // 优先处理拒绝规则（allowLogin = 0）
            List<TpTimeRuleVO> denyRules = rulesByType.get(0);
            LOGGER.info("拒绝规则数量: {}", denyRules != null ? denyRules.size() : 0);
            if (CollUtil.isNotEmpty(denyRules)) {
                for (TpTimeRuleVO rule : denyRules) {
                    LOGGER.info("检查拒绝规则: ID={}, 当前时间={}, 开始时间={}, 结束时间={}", 
                            rule.getId(), currentTime, rule.getStartTime(), rule.getEndTime());
                    boolean inRange = isTimeInRange(currentTime, rule.getStartTime(), rule.getEndTime());
                    LOGGER.info("时间是否在范围内: {}", inRange);
                    if (inRange) {
                        LOGGER.info("拒绝登录: 规则={}", rule.getRuleName());
                        return new TpTimeRuleService.LoginTimeValidationResult(false, 
                                "当前时间段不允许登录", rule.getRuleName());
                    }
                }
            }

            // 处理允许规则（allowLogin = 1）
            List<TpTimeRuleVO> allowRules = rulesByType.get(1);
            if (CollUtil.isNotEmpty(allowRules)) {
                for (TpTimeRuleVO rule : allowRules) {
                    if (isTimeInRange(currentTime, rule.getStartTime(), rule.getEndTime())) {
                        return new TpTimeRuleService.LoginTimeValidationResult(true, null, rule.getRuleName());
                    }
                }
                // 如果有允许规则但当前时间不在任何允许范围内，则拒绝
                return new TpTimeRuleService.LoginTimeValidationResult(false, 
                        "当前时间段不在允许的登录时间范围内", null);
            }

            // 默认允许登录
            return new TpTimeRuleService.LoginTimeValidationResult(true, null, null);
        } catch (Exception e) {
            LOGGER.error("校验登录时间权限失败! userId:{}, roleIds:{}, 错误:{}", 
                    userId, JSONObject.toJSONString(roleIds), ExceptionUtils.getStackTrace(e));
            // 出现异常时默认允许登录，避免影响正常业务
            return new TpTimeRuleService.LoginTimeValidationResult(true, null, null);
        }
    }

    /**
     * 获取所有有效的时间规则
     * @return 时间规则列表
     */
    @Override
    public List<TpTimeRuleVO> getAllValidRules() {
        try {
            TpTimeRuleQuery query = new TpTimeRuleQuery();
            query.setStatus(1); // 只查询启用的规则
            query.setCurrent(1);
            query.setSize(Integer.MAX_VALUE);

            IPage<TpTimeRuleVO> page = queryPage(query);
            return page.getRecords();
        } catch (Exception e) {
            LOGGER.error("获取所有有效时间规则失败! 错误:{}", ExceptionUtils.getStackTrace(e));
            return new ArrayList<>();
        }
    }

    /**
     * 根据用户ID和角色ID查询相关的时间规则
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 时间规则列表
     */
    @Override
    public List<TpTimeRuleVO> getRulesByUserAndRoles(String userId, List<String> roleIds) {
        try {
            String currentTime = CommonDateUtil.now();
            List<TpTimeRuleVO> rules = tpTimeRuleMapper.selectValidRulesByUserAndRoles(userId, roleIds, currentTime);
            
            // 转换显示名称
            if (CollUtil.isNotEmpty(rules)) {
                rules.forEach(this::convertDisplayNames);
            }
            
            return rules;
        } catch (Exception e) {
            LOGGER.error("根据用户和角色查询时间规则失败! userId:{}, roleIds:{}, 错误:{}", 
                    userId, JSONObject.toJSONString(roleIds), ExceptionUtils.getStackTrace(e));
            return new ArrayList<>();
        }
    }

    /**
     * 校验时间规则参数
     * @param vo 时间规则VO
     */
    private void validateTimeRule(TpTimeRuleVO vo) {
        if (StrUtil.isBlank(vo.getRuleName())) {
            throw new TopinfoRuntimeException(-1, "规则名称不能为空！");
        }
        if (StrUtil.isBlank(vo.getStartTime())) {
            throw new TopinfoRuntimeException(-1, "规则启动时间不能为空！");
        }
        if (StrUtil.isBlank(vo.getEndTime())) {
            throw new TopinfoRuntimeException(-1, "规则结束时间不能为空！");
        }
        if (vo.getStatus() == null) {
            throw new TopinfoRuntimeException(-1, "规则状态不能为空！");
        }
        if (vo.getAllowLogin() == null) {
            throw new TopinfoRuntimeException(-1, "是否允许登录不能为空！");
        }

        // 校验时间格式
        if (!isValidTimeFormat(vo.getStartTime()) || !isValidTimeFormat(vo.getEndTime())) {
            throw new TopinfoRuntimeException(-1, "时间格式不正确，请使用yyyyMMddHHmmss格式！");
        }

        // 校验时间逻辑
        if (vo.getStartTime().compareTo(vo.getEndTime()) >= 0) {
            throw new TopinfoRuntimeException(-1, "规则启动时间必须小于结束时间！");
        }

        // 校验用户和角色不能同时为空
        if (StrUtil.isBlank(vo.getUserIds()) && StrUtil.isBlank(vo.getRoleIds())) {
            throw new TopinfoRuntimeException(-1, "用户和角色不能同时为空！");
        }
    }

    /**
     * 校验时间格式
     * @param timeStr 时间字符串
     * @return 是否有效
     */
    private boolean isValidTimeFormat(String timeStr) {
        if (StrUtil.isBlank(timeStr) || timeStr.length() != 14) {
            return false;
        }
        try {
            // 简单校验是否为数字
            Long.parseLong(timeStr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断当前时间是否在指定时间范围内
     * @param currentTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 是否在范围内
     */
    private boolean isTimeInRange(String currentTime, String startTime, String endTime) {
        boolean result = currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(endTime) <= 0;
        LOGGER.debug("时间范围检查: {} >= {} && {} <= {} = {}", 
                currentTime, startTime, currentTime, endTime, result);
        return result;
    }

    /**
     * 转换显示名称
     * @param vo 时间规则VO
     */
    private void convertDisplayNames(TpTimeRuleVO vo) {
        if (vo == null) {
            return;
        }

        // 转换状态名称
        if (vo.getStatus() != null) {
            vo.setStatusName(vo.getStatus() == 1 ? "启用" : "禁用");
        }

        // 转换规则类型名称
        if (vo.getAllowLogin() != null) {
            vo.setAllowLoginName(vo.getAllowLogin() == 1 ? "允许" : "拒绝");
        }
    }
}