package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpApiCallLog;
import com.jiuxi.admin.core.bean.query.TpApiCallLogQuery;
import com.jiuxi.admin.core.bean.vo.TpApiCallLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * API调用日志Mapper
 *
 * @author system
 * @date 2025-11-30
 */
@Mapper
public interface TpApiCallLogMapper {

    /**
     * 分页查询API调用日志列表
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpApiCallLogVO> getPage(Page<TpApiCallLogVO> page, @Param("query") TpApiCallLogQuery query);

    /**
     * 查看API调用日志详情
     *
     * @param logId 日志ID
     * @return 日志详情
     */
    TpApiCallLogVO view(String logId);

    /**
     * 插入API调用日志
     *
     * @param log 日志实体
     * @return 影响行数
     */
    int insert(TpApiCallLog log);
}
