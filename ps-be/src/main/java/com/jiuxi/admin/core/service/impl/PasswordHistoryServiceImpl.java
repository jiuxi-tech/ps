package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.bean.entity.TpPasswordHistory;
import com.jiuxi.admin.core.bean.query.TpPasswordHistoryQuery;
import com.jiuxi.admin.core.bean.vo.TpPasswordHistoryVO;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.admin.core.mapper.TpPasswordHistoryMapper;
import com.jiuxi.admin.core.mapper.TpPersonBasicinfoMapper;
import com.jiuxi.admin.core.service.PasswordHistoryService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.common.util.CommonDateUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 密码修改历史服务实现
 *
 * @author system
 * @date 2025-12-01
 */
@Service
public class PasswordHistoryServiceImpl implements PasswordHistoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordHistoryServiceImpl.class);

    @Autowired
    private TpPasswordHistoryMapper passwordHistoryMapper;

    @Autowired(required = false)
    private TpPersonBasicinfoMapper personMapper;

    /**
     * 修改类型文本映射
     */
    private static final Map<Integer, String> CHANGE_TYPE_MAP = new HashMap<>();

    static {
        CHANGE_TYPE_MAP.put(1, "用户主动修改");
        CHANGE_TYPE_MAP.put(2, "管理员重置");
        CHANGE_TYPE_MAP.put(3, "密码过期修改");
        CHANGE_TYPE_MAP.put(4, "其他");
    }

    @Override
    public int savePasswordHistory(String accountId, String personId, String username, String passwordHash,
                                   Integer changeType, String changeReason, String changedBy, String changedByName,
                                   String ipAddress, String userAgent, String tenantId) {
        try {
            if (StrUtil.isBlank(accountId) || StrUtil.isBlank(personId) || StrUtil.isBlank(username)
                    || StrUtil.isBlank(passwordHash) || changeType == null) {
                LOGGER.warn("保存密码历史记录失败，必填参数为空: accountId={}, personId={}, username={}, changeType={}",
                        accountId, personId, username, changeType);
                return 0;
            }

            // 如果操作人姓名为空，尝试根据操作人ID查询姓名
            if (StrUtil.isBlank(changedByName) && StrUtil.isNotBlank(changedBy) && personMapper != null) {
                try {
                    TpPersonBasicinfoVO personInfo = personMapper.view(changedBy);
                    if (personInfo != null && StrUtil.isNotBlank(personInfo.getPersonName())) {
                        changedByName = personInfo.getPersonName();
                    }
                } catch (Exception e) {
                    LOGGER.warn("查询操作人姓名失败: changedBy={}, error={}", changedBy, e.getMessage());
                }
            }

            TpPasswordHistory record = new TpPasswordHistory();
            record.setHistoryId(UUID.randomUUID().toString());
            record.setAccountId(accountId);
            record.setPersonId(personId);
            record.setUsername(username);
            record.setPasswordHash(passwordHash);
            record.setChangeType(changeType);
            record.setChangeReason(changeReason);
            record.setChangedBy(changedBy);
            record.setChangedByName(changedByName);
            record.setChangeTime(CommonDateUtil.now());
            record.setIpAddress(ipAddress);
            record.setUserAgent(userAgent);
            record.setTenantId(tenantId);

            int count = passwordHistoryMapper.insertHistory(record);
            LOGGER.info("密码历史记录保存成功: historyId={}, accountId={}, username={}, changeType={}",
                    record.getHistoryId(), accountId, username, changeType);
            return count;
        } catch (Exception e) {
            LOGGER.error("保存密码历史记录失败: accountId={}, error={}", accountId, ExceptionUtils.getStackTrace(e));
            // 不抛出异常，避免影响主业务流程
            return 0;
        }
    }

    @Override
    public Page<TpPasswordHistoryVO> list(TpPasswordHistoryQuery query) {
        try {
            // 设置分页参数
            if (query.getCurrent() == null || query.getCurrent() < 1) {
                query.setCurrent(1);
            }
            if (query.getSize() == null || query.getSize() < 1) {
                query.setSize(20);
            }

            // 计算offset
            int offset = (query.getCurrent() - 1) * query.getSize();
            query.setCurrent(offset);

            // 查询列表和总数
            List<TpPasswordHistoryVO> records = passwordHistoryMapper.list(query);
            int total = passwordHistoryMapper.count(query);

            // 设置修改类型文本
            for (TpPasswordHistoryVO vo : records) {
                vo.setChangeTypeText(CHANGE_TYPE_MAP.getOrDefault(vo.getChangeType(), "未知"));
            }

            // 构建分页结果
            Page<TpPasswordHistoryVO> page = new Page<>();
            page.setRecords(records);
            page.setCurrent((offset / query.getSize()) + 1);
            page.setSize(query.getSize());
            page.setTotal(total);
            page.setPages((int) Math.ceil((double) total / query.getSize()));

            return page;
        } catch (Exception e) {
            LOGGER.error("查询密码历史列表失败: error={}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("查询密码历史列表失败");
        }
    }

    @Override
    public TpPasswordHistoryVO view(String historyId) {
        try {
            if (StrUtil.isBlank(historyId)) {
                throw new RuntimeException("历史记录ID不能为空");
            }

            TpPasswordHistoryVO vo = passwordHistoryMapper.view(historyId);
            if (vo == null) {
                throw new RuntimeException("历史记录不存在");
            }

            // 设置修改类型文本
            vo.setChangeTypeText(CHANGE_TYPE_MAP.getOrDefault(vo.getChangeType(), "未知"));

            return vo;
        } catch (RuntimeException e) {
            // 重新抛出运行时异常
            throw e;
        } catch (Exception e) {
            LOGGER.error("查询密码历史详情失败: historyId={}, error={}", historyId, ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("查询密码历史详情失败");
        }
    }

    @Override
    public List<TpPasswordHistory> getRecentPasswordHistory(String accountId, int limit) {
        try {
            if (StrUtil.isBlank(accountId)) {
                throw new RuntimeException("账号ID不能为空");
            }
            if (limit < 1) {
                limit = 5;
            }
            return passwordHistoryMapper.selectRecentByAccountId(accountId, limit);
        } catch (Exception e) {
            LOGGER.error("查询最近密码历史失败: accountId={}, error={}", accountId, ExceptionUtils.getStackTrace(e));
            return java.util.Collections.emptyList();
        }
    }
}
