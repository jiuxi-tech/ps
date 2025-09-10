package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpIpAccessLog;
import com.jiuxi.admin.core.bean.query.TpIpAccessLogQuery;
import com.jiuxi.admin.core.bean.vo.TpIpAccessLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: TpIpAccessLogMapper
 * @Description: IP访问控制日志表Mapper接口
 * @Author: System
 * @Date: 2024-01-27
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@Mapper
public interface TpIpAccessLogMapper extends BaseMapper<TpIpAccessLog> {

    /**
     * 分页查询IP访问日志
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpIpAccessLogVO> queryPage(Page<TpIpAccessLogVO> page, @Param("query") TpIpAccessLogQuery query);

    /**
     * 查询IP访问日志列表
     * @param query 查询条件
     * @return 日志列表
     */
    List<TpIpAccessLogVO> queryList(@Param("query") TpIpAccessLogQuery query);

    /**
     * 根据ID查询IP访问日志详情
     * @param logId 日志ID
     * @return 日志详情
     */
    TpIpAccessLogVO selectByLogId(@Param("logId") String logId);

    /**
     * 批量删除IP访问日志
     * @param logIds 日志ID列表
     * @return 影响行数
     */
    int deleteByLogIds(@Param("logIds") List<String> logIds);

    /**
     * 统计IP访问次数
     * @param query 查询条件
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsIpAccess(@Param("query") TpIpAccessLogQuery query);

    /**
     * 统计访问结果分布
     * @param query 查询条件
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsAccessResult(@Param("query") TpIpAccessLogQuery query);

    /**
     * 统计每日访问量
     * @param query 查询条件
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsDailyAccess(@Param("query") TpIpAccessLogQuery query);

    /**
     * 清理过期日志
     * @param beforeDate 清理此日期之前的日志
     * @return 影响行数
     */
    int cleanExpiredLogs(@Param("beforeDate") String beforeDate);

    /**
     * 查询最近的访问记录
     * @param clientIp 客户端IP
     * @param limit 限制条数
     * @return 访问记录列表
     */
    List<TpIpAccessLogVO> selectRecentByIp(@Param("clientIp") String clientIp, @Param("limit") Integer limit);

    /**
     * 统计IP在指定时间段内的访问次数
     * @param clientIp 客户端IP
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 访问次数
     */
    Integer countAccessByIpAndTime(@Param("clientIp") String clientIp, 
                                   @Param("startTime") String startTime, 
                                   @Param("endTime") String endTime);
}