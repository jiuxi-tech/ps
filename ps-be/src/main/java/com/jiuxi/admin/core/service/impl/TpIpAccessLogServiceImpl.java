package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpIpAccessLog;
import com.jiuxi.admin.core.bean.query.TpIpAccessLogQuery;
import com.jiuxi.admin.core.bean.vo.TpIpAccessLogVO;
import com.jiuxi.admin.core.mapper.TpIpAccessLogMapper;
import com.jiuxi.admin.core.service.TpIpAccessLogService;
import com.jiuxi.admin.core.util.IpAccessControlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: TpIpAccessLogServiceImpl
 * @Description: IP访问控制日志表服务实现类
 * @Author: Qdd
 * @Date: 2024-01-27
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@Slf4j
@Service
public class TpIpAccessLogServiceImpl implements TpIpAccessLogService {

    @Autowired
    private TpIpAccessLogMapper tpIpAccessLogMapper;

    @Override
    public IPage<TpIpAccessLogVO> queryPage(TpIpAccessLogQuery query) {
        Page<TpIpAccessLogVO> page = new Page<>(query.getCurrent(), query.getSize());
        return tpIpAccessLogMapper.queryPage(page, query);
    }

    @Override
    public List<TpIpAccessLogVO> queryList(TpIpAccessLogQuery query) {
        return tpIpAccessLogMapper.queryList(query);
    }

    @Override
    public TpIpAccessLogVO view(String logId) {
        if (StrUtil.isBlank(logId)) {
            return null;
        }
        return tpIpAccessLogMapper.selectByLogId(logId);
    }

    @Override
    public int deleteByLogIds(List<String> logIds) {
        if (logIds == null || logIds.isEmpty()) {
            return 0;
        }
        return tpIpAccessLogMapper.deleteByLogIds(logIds);
    }

    @Override
    public void logAccess(HttpServletRequest request, String accessResult, String denyReason,
                         String matchedRule, String ruleType, String username) {
        try {
            String clientIp = IpAccessControlUtil.getClientRealIp(request);
            String requestUri = request.getRequestURI();
            String requestMethod = request.getMethod();
            String userAgent = request.getHeader("User-Agent");
            
            logAccessDetail(clientIp, accessResult, denyReason, requestUri, requestMethod,
                          userAgent, username, matchedRule, ruleType, null);
        } catch (Exception e) {
            log.error("记录IP访问日志失败", e);
        }
    }

    @Override
    public void logAccessDetail(String clientIp, String accessResult, String denyReason,
                               String requestUri, String requestMethod, String userAgent,
                               String username, String matchedRule, String ruleType, String cityCode) {
        try {
            TpIpAccessLog accessLog = new TpIpAccessLog();
            accessLog.setLogId(IdUtil.simpleUUID());
            accessLog.setClientIp(clientIp);
            accessLog.setAccessTime(new Date());
            accessLog.setAccessResult(accessResult);
            accessLog.setDenyReason(denyReason);
            accessLog.setRequestUri(requestUri);
            accessLog.setRequestMethod(requestMethod);
            accessLog.setUserAgent(userAgent);
            accessLog.setUsername(username);
            accessLog.setMatchedRule(matchedRule);
            accessLog.setRuleType(ruleType);
            accessLog.setCityCode(cityCode);
            accessLog.setActived(TpIpAccessLog.ACTIVED_YES);
            accessLog.setCreateTime(new Date());
            
            tpIpAccessLogMapper.insert(accessLog);
        } catch (Exception e) {
            log.error("记录IP访问日志详情失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> statisticsIpAccess(TpIpAccessLogQuery query) {
        return tpIpAccessLogMapper.statisticsIpAccess(query);
    }

    @Override
    public List<Map<String, Object>> statisticsAccessResult(TpIpAccessLogQuery query) {
        return tpIpAccessLogMapper.statisticsAccessResult(query);
    }

    @Override
    public List<Map<String, Object>> statisticsDailyAccess(TpIpAccessLogQuery query) {
        return tpIpAccessLogMapper.statisticsDailyAccess(query);
    }

    @Override
    public int cleanExpiredLogs(int days) {
        try {
            String beforeDate = DateUtil.format(DateUtil.offsetDay(new Date(), -days), "yyyy-MM-dd HH:mm:ss");
            return tpIpAccessLogMapper.cleanExpiredLogs(beforeDate);
        } catch (Exception e) {
            log.error("清理过期IP访问日志失败", e);
            return 0;
        }
    }

    @Override
    public List<TpIpAccessLogVO> getRecentAccessByIp(String clientIp, Integer limit) {
        if (StrUtil.isBlank(clientIp)) {
            return null;
        }
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return tpIpAccessLogMapper.selectRecentByIp(clientIp, limit);
    }

    @Override
    public Integer countRecentAccessByIp(String clientIp, Integer minutes) {
        if (StrUtil.isBlank(clientIp) || minutes == null || minutes <= 0) {
            return 0;
        }
        
        Date endTime = new Date();
        Date startTime = DateUtil.offsetMinute(endTime, -minutes);
        
        String startTimeStr = DateUtil.format(startTime, "yyyy-MM-dd HH:mm:ss");
        String endTimeStr = DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss");
        
        Integer count = tpIpAccessLogMapper.countAccessByIpAndTime(clientIp, startTimeStr, endTimeStr);
        return count != null ? count : 0;
    }

    @Override
    public boolean isFrequentAccess(String clientIp, Integer maxAttempts, Integer timeWindowMinutes) {
        if (StrUtil.isBlank(clientIp) || maxAttempts == null || timeWindowMinutes == null) {
            return false;
        }
        
        Integer recentCount = countRecentAccessByIp(clientIp, timeWindowMinutes);
        return recentCount >= maxAttempts;
    }
}