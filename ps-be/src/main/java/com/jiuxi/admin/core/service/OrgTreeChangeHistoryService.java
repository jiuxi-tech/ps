package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.OrgTreeChangeHistory;
import com.jiuxi.admin.core.bean.query.OrgTreeChangeHistoryQuery;
import com.jiuxi.admin.core.bean.vo.OrgTreeChangeHistoryVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 组织机构树变更历史记录服务接口
 *
 * @author 系统生成
 * @since 1.0
 */
public interface OrgTreeChangeHistoryService {

    /**
     * 分页查询组织机构变更历史记录
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<OrgTreeChangeHistoryVO> queryPage(OrgTreeChangeHistoryQuery query);

    /**
     * 记录组织机构树变更
     *
     * @param operationType 操作类型
     * @param operatorId    操作用户ID
     * @param beforeData    变更前数据
     * @param afterData     变更后数据
     * @return 记录ID
     */
    Long recordChange(String operationType, Long operatorId, String beforeData, String afterData);

    /**
     * 记录组织机构树变更（包含全节点树）
     *
     * @param operationType 操作类型
     * @param operatorId    操作用户ID
     * @param beforeData    变更前数据
     * @param afterData     变更后数据
     * @param beforeFullTree 变更前完整节点树
     * @param afterFullTree  变更后完整节点树
     * @return 记录ID
     */
    Long recordChangeWithFullTree(String operationType, Long operatorId, String beforeData, String afterData, String beforeFullTree, String afterFullTree);



    /**
     * 根据ID查询记录
     *
     * @param id 记录ID
     * @return 记录详情
     */
    OrgTreeChangeHistory getById(String id);

    /**
     * 获取最新记录
     *
     * @return 最新记录
     */
    OrgTreeChangeHistory getLatestVersion();



    /**
     * 根据时间范围查询记录
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 记录列表
     */
    List<OrgTreeChangeHistory> getByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据操作用户ID查询记录
     *
     * @param operatorId 操作用户ID
     * @return 记录列表
     */
    List<OrgTreeChangeHistory> getByOperatorId(Long operatorId);

    /**
     * 根据操作类型查询记录
     *
     * @param operationType 操作类型
     * @return 记录列表
     */
    List<OrgTreeChangeHistory> getByOperationType(String operationType);

    /**
     * 分页查询记录
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 记录列表
     */
    List<OrgTreeChangeHistory> getByPage(int pageNum, int pageSize);

    /**
     * 统计总记录数
     *
     * @return 总记录数
     */
    Long countTotal();

    /**
     * 获取操作统计信息
     *
     * @return 统计信息
     */
    List<Map<String, Object>> getOperationStatistics();



    /**
     * 比较两个记录的差异
     *
     * @param fromId 起始记录ID
     * @param toId   目标记录ID
     * @return 差异信息
     */
    Map<String, Object> compareVersions(Long fromId, Long toId);

    /**
     * 清理指定时间之前的记录
     *
     * @param beforeTime 时间点
     * @return 删除的记录数
     */
    int cleanupBefore(LocalDateTime beforeTime);

    /**
     * 根据操作用户和时间范围查询记录
     *
     * @param operatorId 操作用户ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 记录列表
     */
    List<OrgTreeChangeHistory> getByOperatorAndTimeRange(Long operatorId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据操作类型和时间范围查询记录
     *
     * @param operationType 操作类型
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @return 记录列表
     */
    List<OrgTreeChangeHistory> getByTypeAndTimeRange(String operationType, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 验证记录完整性
     *
     * @return 验证结果
     */
    Map<String, Object> validateVersionIntegrity();
}