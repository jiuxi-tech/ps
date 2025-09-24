package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpDatabaseBackupLogQuery;
import com.jiuxi.admin.core.bean.vo.TpDatabaseBackupLogVO;

import java.util.List;

/**
 * @ClassName: TpDatabaseBackupLogService
 * @Description: 数据库备份记录Service接口
 * @Author: system
 * @Date: 2025-09-24
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpDatabaseBackupLogService {

    /**
     * 分页查询备份记录列表
     *
     * @param query 查询条件
     * @param tenantId 租户ID
     * @param orgId 机构ID
     * @return 分页结果
     */
    IPage<TpDatabaseBackupLogVO> queryPage(TpDatabaseBackupLogQuery query, String tenantId, String orgId);

    /**
     * 查询备份记录列表
     *
     * @param query 查询条件
     * @param tenantId 租户ID
     * @param orgId 机构ID
     * @return 备份记录列表
     */
    List<TpDatabaseBackupLogVO> getList(TpDatabaseBackupLogQuery query, String tenantId, String orgId);

    /**
     * 查看备份记录详情
     *
     * @param backupId 备份记录ID
     * @return 备份记录详情
     */
    TpDatabaseBackupLogVO view(String backupId);

    /**
     * 删除备份记录
     *
     * @param backupId 备份记录ID
     * @param operator 操作人
     * @return 影响行数
     */
    int delete(String backupId, String operator);

    /**
     * 查询最近的备份记录
     *
     * @param databaseName 数据库名称
     * @param limit 查询条数
     * @return 最近的备份记录列表
     */
    List<TpDatabaseBackupLogVO> getRecentBackups(String databaseName, Integer limit);

    /**
     * 获取备份统计信息
     *
     * @param databaseName 数据库名称
     * @param days 统计天数
     * @return 统计信息
     */
    TpDatabaseBackupLogVO getBackupStatistics(String databaseName, Integer days);
}