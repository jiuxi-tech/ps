package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpOperateLog;
import com.jiuxi.admin.core.bean.vo.TpOperateLogVO;
import com.jiuxi.admin.core.bean.query.TpOperateLogQuery;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: TpOperateLogMapper
 * @Description: 操作日志表 - Mapper
 * @Author: System
 * @Date: 2024-01-17
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@Mapper
public interface TpOperateLogMapper {

    /**
     * 分页查询操作日志
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpOperateLogVO> getPage(Page<TpOperateLogVO> page, @Param("query") TpOperateLogQuery query);

    /**
     * 新增操作日志
     * @param bean 操作日志实体
     * @return 影响行数
     */
    int add(TpOperateLog bean);

    /**
     * 查看操作日志详情
     * @param id 日志ID
     * @return 操作日志VO
     */
    TpOperateLogVO view(String id);
}