package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpDatabaseBackupLog;
import com.jiuxi.admin.core.bean.query.TpDatabaseBackupLogQuery;
import com.jiuxi.admin.core.bean.vo.TpDatabaseBackupLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpDatabaseBackupLogMapper
 * @Description: 数据库备份记录表Mapper接口
 * @Author: system
 * @Date: 2025-09-24
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpDatabaseBackupLogMapper {

    /**
     * 分页查询备份记录列表
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpDatabaseBackupLogVO> getPage(Page<TpDatabaseBackupLogVO> page, @Param("query") TpDatabaseBackupLogQuery query);

    /**
     * 查询备份记录列表
     *
     * @param query 查询条件
     * @return 备份记录列表
     */
    List<TpDatabaseBackupLogVO> getList(@Param("query") TpDatabaseBackupLogQuery query);

    /**
     * 新增备份记录
     *
     * @param bean 备份记录实体
     * @return 影响行数
     */
    int save(TpDatabaseBackupLog bean);

    /**
     * 更新备份记录
     *
     * @param bean 备份记录实体
     * @return 影响行数
     */
    int update(TpDatabaseBackupLog bean);

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
     * @param bean 备份记录实体
     * @return 影响行数
     */
    int delete(TpDatabaseBackupLog bean);

    /**
     * 查询正在进行中的备份任务
     *
     * @param databaseName 数据库名称
     * @return 备份记录列表
     */
    List<TpDatabaseBackupLog> getRunningBackups(@Param("databaseName") String databaseName);

    /**
     * 查询最近的备份记录
     *
     * @param databaseName 数据库名称
     * @param limit 查询条数
     * @return 备份记录列表
     */
    List<TpDatabaseBackupLogVO> getRecentBackups(@Param("databaseName") String databaseName, @Param("limit") Integer limit);
}