package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpDatabaseBackupLog;
import com.jiuxi.admin.core.bean.query.TpDatabaseBackupLogQuery;
import com.jiuxi.admin.core.bean.vo.TpDatabaseBackupLogVO;
import com.jiuxi.admin.core.mapper.TpDatabaseBackupLogMapper;
import com.jiuxi.admin.core.service.TpDatabaseBackupLogService;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.DateUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: TpDatabaseBackupLogServiceImpl
 * @Description: 数据库备份记录Service实现类
 * @Author: system
 * @Date: 2025-09-24
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpDatabaseBackupLogService")
public class TpDatabaseBackupLogServiceImpl implements TpDatabaseBackupLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpDatabaseBackupLogServiceImpl.class);

    @Autowired
    private TpDatabaseBackupLogMapper tpDatabaseBackupLogMapper;

    /**
     * 分页查询备份记录列表
     */
    @Override
    public IPage<TpDatabaseBackupLogVO> queryPage(TpDatabaseBackupLogQuery query, String tenantId, String orgId) {
        try {
            // 设置租户和机构信息
            query.setTenantId(tenantId);
            query.setOrgId(orgId);
            query.setActived(1); // 只查询有效记录
            
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);
            
            Page<TpDatabaseBackupLogVO> page = new Page<>(pageNum, pageSize);
            IPage<TpDatabaseBackupLogVO> iPage = tpDatabaseBackupLogMapper.getPage(page, query);
            
            // 处理显示字段
            if (iPage.getRecords() != null) {
                for (TpDatabaseBackupLogVO vo : iPage.getRecords()) {
                    processDisplayFields(vo);
                }
            }
            
            return iPage;
        } catch (Exception e) {
            LOGGER.error("备份记录列表查询失败！query:{}, 错误: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "备份记录列表查询失败！");
        }
    }

    /**
     * 查询备份记录列表
     */
    @Override
    public List<TpDatabaseBackupLogVO> getList(TpDatabaseBackupLogQuery query, String tenantId, String orgId) {
        try {
            // 设置租户和机构信息
           // query.setTenantId(tenantId);
           // query.setOrgId(orgId);
            query.setActived(1); // 只查询有效记录
            
            List<TpDatabaseBackupLogVO> list = tpDatabaseBackupLogMapper.getList(query);
            
            // 处理显示字段
            if (list != null) {
                for (TpDatabaseBackupLogVO vo : list) {
                    processDisplayFields(vo);
                }
            }
            
            return list;
        } catch (Exception e) {
            LOGGER.error("备份记录列表查询失败！query:{}, 错误: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "备份记录列表查询失败！");
        }
    }

    /**
     * 查看备份记录详情
     */
    @Override
    public TpDatabaseBackupLogVO view(String backupId) {
        try {
            TpDatabaseBackupLogVO vo = tpDatabaseBackupLogMapper.view(backupId);
            if (vo != null) {
                processDisplayFields(vo);
            }
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看备份记录详情失败！backupId:{}, 错误: {}", backupId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看备份记录详情失败！");
        }
    }

    /**
     * 删除备份记录
     */
    @Override
    public int delete(String backupId, String operator) {
        try {
            LOGGER.info("开始删除备份记录，备份ID: {}, 操作人: {}", backupId, operator);
            
            if (StrUtil.isBlank(backupId)) {
                LOGGER.error("删除备份记录失败：备份ID为空");
                return 0;
            }
            
            // 先查询记录是否存在
            TpDatabaseBackupLogVO existingRecord = tpDatabaseBackupLogMapper.view(backupId);
            if (existingRecord == null) {
                LOGGER.error("删除备份记录失败：找不到指定的备份记录，backupId: {}", backupId);
                return 0;
            }
            
            LOGGER.info("找到备份记录，备份名称: {}, 当前状态: {}", existingRecord.getBackupName(), existingRecord.getBackupStatusName());
            
            TpDatabaseBackupLog bean = new TpDatabaseBackupLog();
            bean.setBackupId(backupId);
            bean.setUpdator(operator);
            bean.setUpdateTime(CommonDateUtil.now());
            
            int count = tpDatabaseBackupLogMapper.delete(bean);
            LOGGER.info("删除备份记录结果，影响行数: {}", count);
            
            if (count > 0) {
                LOGGER.info("删除备份记录成功，backupId: {}", backupId);
            } else {
                LOGGER.error("删除备份记录失败，数据库更新失败，backupId: {}", backupId);
            }
            
            return count;
        } catch (Exception e) {
            LOGGER.error("删除备份记录失败！backupId:{}, 错误: {}", backupId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除备份记录失败！");
        }
    }

    /**
     * 查询最近的备份记录
     */
    @Override
    public List<TpDatabaseBackupLogVO> getRecentBackups(String databaseName, Integer limit) {
        try {
            List<TpDatabaseBackupLogVO> list = tpDatabaseBackupLogMapper.getRecentBackups(databaseName, limit);
            
            // 处理显示字段
            if (list != null) {
                for (TpDatabaseBackupLogVO vo : list) {
                    processDisplayFields(vo);
                }
            }
            
            return list;
        } catch (Exception e) {
            LOGGER.error("查询最近备份记录失败！databaseName:{}, limit:{}, 错误: {}", 
                        databaseName, limit, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询最近备份记录失败！");
        }
    }

    /**
     * 获取备份统计信息
     */
    @Override
    public TpDatabaseBackupLogVO getBackupStatistics(String databaseName, Integer days) {
        try {
            // 构建查询条件
            TpDatabaseBackupLogQuery query = new TpDatabaseBackupLogQuery();
            query.setDatabaseName(databaseName);
            query.setActived(1);
            
            // 设置时间范围
            if (days != null && days > 0) {
                // 计算开始时间
                long startTime = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L);
                query.setBackupStartTimeBegin(DateUtils.formatMillisToString(startTime, "yyyy-MM-dd HH:mm:ss"));
            }
            
            List<TpDatabaseBackupLogVO> list = tpDatabaseBackupLogMapper.getList(query);
            
            // 统计信息
            TpDatabaseBackupLogVO statistics = new TpDatabaseBackupLogVO();
            statistics.setDatabaseName(databaseName);
            
            if (list != null && !list.isEmpty()) {
                int totalCount = list.size();
                int successCount = 0;
                int failedCount = 0;
                long totalSize = 0;
                int totalDuration = 0;
                
                for (TpDatabaseBackupLogVO vo : list) {
                    if (vo.getBackupStatus() != null) {
                        if (vo.getBackupStatus() == 2) { // 成功
                            successCount++;
                            if (vo.getBackupFileSize() != null) {
                                totalSize += vo.getBackupFileSize();
                            }
                            if (vo.getBackupDuration() != null) {
                                totalDuration += vo.getBackupDuration();
                            }
                        } else if (vo.getBackupStatus() == 3) { // 失败
                            failedCount++;
                        }
                    }
                }
                
                // 设置统计信息（使用扩展字段存储）
                statistics.setExtend01(String.valueOf(totalCount)); // 总数
                statistics.setExtend02(String.valueOf(successCount)); // 成功数
                statistics.setExtend03(String.valueOf(failedCount)); // 失败数
                
                // 设置文件大小和耗时信息
                statistics.setBackupFileSize(totalSize);
                statistics.setBackupFileSizeDisplay(formatFileSize(totalSize));
                statistics.setBackupDuration(totalDuration);
                statistics.setBackupDurationDisplay(formatDuration(totalDuration));
            }
            
            return statistics;
            
        } catch (Exception e) {
            LOGGER.error("获取备份统计信息失败！databaseName:{}, days:{}, 错误: {}", 
                        databaseName, days, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "获取备份统计信息失败！");
        }
    }

    /**
     * 处理显示字段
     */
    private void processDisplayFields(TpDatabaseBackupLogVO vo) {
        if (vo == null) return;
        
        // 处理文件大小显示
        if (vo.getBackupFileSize() != null) {
            vo.setBackupFileSizeDisplay(formatFileSize(vo.getBackupFileSize()));
        }
        
        // 处理耗时显示
        if (vo.getBackupDuration() != null) {
            vo.setBackupDurationDisplay(formatDuration(vo.getBackupDuration()));
        }
        
        // 处理备份类型显示名称（如果XML中没有处理）
        if (vo.getBackupType() != null && StrUtil.isBlank(vo.getBackupTypeName())) {
            switch (vo.getBackupType()) {
                case 1:
                    vo.setBackupTypeName("自动备份");
                    break;
                case 2:
                    vo.setBackupTypeName("手动备份");
                    break;
                default:
                    vo.setBackupTypeName("未知");
                    break;
            }
        }
        
        // 处理备份状态显示名称（如果XML中没有处理）
        if (vo.getBackupStatus() != null && StrUtil.isBlank(vo.getBackupStatusName())) {
            switch (vo.getBackupStatus()) {
                case 1:
                    vo.setBackupStatusName("进行中");
                    break;
                case 2:
                    vo.setBackupStatusName("成功");
                    break;
                case 3:
                    vo.setBackupStatusName("失败");
                    break;
                default:
                    vo.setBackupStatusName("未知");
                    break;
            }
        }
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(Long size) {
        if (size == null || size <= 0) {
            return "0 B";
        }
        
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        
        if (digitGroups >= units.length) {
            digitGroups = units.length - 1;
        }
        
        BigDecimal value = new BigDecimal(size).divide(
            new BigDecimal(Math.pow(1024, digitGroups)), 2, RoundingMode.HALF_UP);
        
        return value.toString() + " " + units[digitGroups];
    }

    /**
     * 格式化耗时
     */
    private String formatDuration(Integer seconds) {
        if (seconds == null || seconds <= 0) {
            return "0秒";
        }
        
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append("小时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        if (secs > 0 || sb.length() == 0) {
            sb.append(secs).append("秒");
        }
        
        return sb.toString();
    }
}