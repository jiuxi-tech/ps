package com.jiuxi.admin.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuxi.admin.core.bean.OrgTreeChangeHistory;
import com.jiuxi.admin.core.bean.query.OrgTreeChangeHistoryQuery;
import com.jiuxi.admin.core.bean.vo.OrgTreeChangeHistoryVO;
import com.jiuxi.admin.core.mapper.OrgTreeChangeHistoryMapper;
import com.jiuxi.admin.core.service.OrgTreeChangeHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Optional;

/**
 * 组织机构树变更历史记录服务实现类
 *
 * @author 系统生成
 * @since 1.0
 */
@Service
@Transactional
public class OrgTreeChangeHistoryServiceImpl implements OrgTreeChangeHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(OrgTreeChangeHistoryServiceImpl.class);

    @Autowired
    private OrgTreeChangeHistoryMapper changeHistoryMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public IPage<OrgTreeChangeHistoryVO> queryPage(OrgTreeChangeHistoryQuery query) {
        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);
            
            Page<OrgTreeChangeHistoryVO> page = new Page<>(pageNum, pageSize);
            IPage<OrgTreeChangeHistoryVO> iPage = changeHistoryMapper.getPage(page, query);
            
            return iPage;
        } catch (Exception e) {
            logger.error("查询组织机构变更历史失败！query:{}, 错误: {}", query, e.getMessage(), e);
            throw new RuntimeException("查询组织机构变更历史失败！");
        }
    }

    @Override
    public Long recordChange(String operationType, Long operatorId, String beforeData, String afterData) {
        try {
            // 创建变更记录
            OrgTreeChangeHistory changeHistory = new OrgTreeChangeHistory();
            changeHistory.setOperationType(operationType);
            changeHistory.setOperationTime(LocalDateTime.now());
            changeHistory.setOperatorId(operatorId);
            changeHistory.setBeforeData(beforeData);
            changeHistory.setAfterData(afterData);
            // 设置 version 默认值，满足数据库约束（不参与业务逻辑，用ID代替）
            changeHistory.setVersion(1L);
            // 设置 dept_id 默认值，满足数据库约束（不参与业务逻辑）
            changeHistory.setDeptId(0L);

            // 保存记录
            changeHistoryMapper.insert(changeHistory);
            
            logger.info("记录组织机构树变更成功: 操作类型={}, 记录ID={}, 操作用户={}", 
                       operationType, changeHistory.getId(), operatorId);
            
            return changeHistory.getId();
        } catch (Exception e) {
            logger.error("记录组织机构树变更失败: 操作类型={}, 操作用户={}", operationType, operatorId, e);
            // 不抛出异常，返回 null 表示失败，不影响主业务
            return null;
        }
    }

    @Override
    public Long recordChangeWithFullTree(String operationType, Long operatorId, String beforeData, String afterData, String beforeFullTree, String afterFullTree) {
        try {
            // 创建变更记录
            OrgTreeChangeHistory changeHistory = new OrgTreeChangeHistory();
            changeHistory.setOperationType(operationType);
            changeHistory.setOperationTime(LocalDateTime.now());
            changeHistory.setOperatorId(operatorId);
            changeHistory.setBeforeData(beforeData);
            changeHistory.setAfterData(afterData);
            changeHistory.setBeforeFullTree(beforeFullTree);
            changeHistory.setAfterFullTree(afterFullTree);
            // 设置 version 默认值，满足数据库约束（不参与业务逻辑，用ID代替）
            changeHistory.setVersion(1L);
            // 设置 dept_id 默认值，满足数据库约束（不参与业务逻辑）
            changeHistory.setDeptId(0L);

            // 保存记录
            changeHistoryMapper.insert(changeHistory);
            
            logger.info("记录组织机构树变更成功（包含全节点树）: 操作类型={}, 记录ID={}, 操作用户={}", 
                       operationType, changeHistory.getId(), operatorId);
            
            return changeHistory.getId();
        } catch (Exception e) {
            logger.error("记录组织机构树变更失败（包含全节点树）: 操作类型={}, 操作用户={}", operationType, operatorId, e);
            // 不抛出异常，返回 null 表示失败，不影响主业务
            return null;
        }
    }



    @Override
    public OrgTreeChangeHistory getById(String id) {
        return changeHistoryMapper.selectById(Long.valueOf(id));
    }

    @Override
    public OrgTreeChangeHistory getLatestVersion() {
        return changeHistoryMapper.selectLatestVersion();
    }



    @Override
    public List<OrgTreeChangeHistory> getByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return changeHistoryMapper.selectByTimeRange(startTime, endTime);
    }

    @Override
    public List<OrgTreeChangeHistory> getByOperatorId(Long operatorId) {
        return changeHistoryMapper.selectByOperatorId(operatorId);
    }

    @Override
    public List<OrgTreeChangeHistory> getByOperationType(String operationType) {
        return changeHistoryMapper.selectByOperationType(operationType);
    }

    @Override
    public List<OrgTreeChangeHistory> getByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return changeHistoryMapper.selectByPage(offset, pageSize);
    }

    @Override
    public Long countTotal() {
        return changeHistoryMapper.countTotal();
    }

    @Override
    public List<Map<String, Object>> getOperationStatistics() {
        return changeHistoryMapper.selectOperationStatistics();
    }



    @Override
    public Map<String, Object> compareVersions(Long fromId, Long toId) {
        try {
            OrgTreeChangeHistory fromRecord = changeHistoryMapper.selectById(fromId);
            OrgTreeChangeHistory toRecord = changeHistoryMapper.selectById(toId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("fromId", fromId);
            result.put("toId", toId);
            result.put("fromRecord", fromRecord);
            result.put("toRecord", toRecord);
            
            if (fromRecord != null && toRecord != null) {
                // 比较JSON数据差异
                Map<String, Object> differences = compareJsonData(
                    fromRecord.getAfterData(), 
                    toRecord.getAfterData()
                );
                result.put("differences", differences);
            }
            
            return result;
        } catch (Exception e) {
            logger.error("比较记录差异失败: fromId={}, toId={}", fromId, toId, e);
            throw new RuntimeException("比较记录差异失败", e);
        }
    }

    @Override
    public int cleanupBefore(LocalDateTime beforeTime) {
        try {
            int deletedCount = changeHistoryMapper.deleteBeforeTime(beforeTime);
            logger.info("清理历史记录完成，删除了 {} 条记录", deletedCount);
            return deletedCount;
        } catch (Exception e) {
            logger.error("清理历史记录失败", e);
            throw new RuntimeException("清理记录失败", e);
        }
    }

    @Override
    public List<OrgTreeChangeHistory> getByOperatorAndTimeRange(Long operatorId, LocalDateTime startTime, LocalDateTime endTime) {
        return changeHistoryMapper.selectByOperatorAndTimeRange(operatorId, startTime, endTime);
    }

    @Override
    public List<OrgTreeChangeHistory> getByTypeAndTimeRange(String operationType, LocalDateTime startTime, LocalDateTime endTime) {
        return changeHistoryMapper.selectByTypeAndTimeRange(operationType, startTime, endTime);
    }

    @Override
    public Map<String, Object> validateVersionIntegrity() {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 检查记录完整性
            Long totalCount = countTotal();
            OrgTreeChangeHistory latestRecord = getLatestVersion();
            
            result.put("totalCount", totalCount);
            result.put("latestRecord", latestRecord);
            result.put("hasRecords", totalCount > 0);
            
            // 检查是否有记录存在
            result.put("isIntegrityValid", totalCount > 0 && latestRecord != null);
            
            return result;
        } catch (Exception e) {
            logger.error("验证记录完整性失败", e);
            throw new RuntimeException("验证完整性失败", e);
        }
    }

    /**
     * 比较两个JSON数据的差异
     *
     * @param fromJson 源JSON
     * @param toJson   目标JSON
     * @return 差异信息
     */
    private Map<String, Object> compareJsonData(String fromJson, String toJson) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (!StringUtils.hasText(fromJson) && !StringUtils.hasText(toJson)) {
                result.put("hasChanges", false);
                return result;
            }
            
            if (!StringUtils.hasText(fromJson) || !StringUtils.hasText(toJson)) {
                result.put("hasChanges", true);
                result.put("changeType", !StringUtils.hasText(fromJson) ? "CREATED" : "DELETED");
                return result;
            }
            
            JsonNode fromNode = objectMapper.readTree(fromJson);
            JsonNode toNode = objectMapper.readTree(toJson);
            
            boolean hasChanges = !fromNode.equals(toNode);
            result.put("hasChanges", hasChanges);
            
            if (hasChanges) {
                result.put("changeType", "MODIFIED");
                // 这里可以添加更详细的差异分析逻辑
            }
            
        } catch (JsonProcessingException e) {
            logger.warn("解析JSON数据失败，无法比较差异", e);
            result.put("hasChanges", true);
            result.put("changeType", "UNKNOWN");
            result.put("error", "JSON解析失败");
        }
        
        return result;
    }
}