package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiuxi.admin.core.bean.entity.TpPasswordHistory;
import com.jiuxi.admin.core.bean.query.TpPasswordHistoryQuery;
import com.jiuxi.admin.core.bean.vo.TpPasswordHistoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 密码修改历史Mapper
 *
 * @author system
 * @date 2025-12-01
 */
public interface TpPasswordHistoryMapper extends BaseMapper<TpPasswordHistory> {

    /**
     * 分页查询密码历史列表
     *
     * @param query 查询条件
     * @return 历史记录列表
     */
    List<TpPasswordHistoryVO> list(@Param("query") TpPasswordHistoryQuery query);

    /**
     * 统计记录数
     *
     * @param query 查询条件
     * @return 记录数
     */
    int count(@Param("query") TpPasswordHistoryQuery query);

    /**
     * 根据ID查询详情
     *
     * @param historyId 历史记录ID
     * @return 历史记录详情
     */
    TpPasswordHistoryVO view(@Param("historyId") String historyId);

    /**
     * 插入密码历史记录
     *
     * @param record 历史记录
     * @return 影响行数
     */
    int insertHistory(@Param("record") TpPasswordHistory record);

    /**
     * 根据账号ID查询最近N条密码历史
     *
     * @param accountId 账号ID
     * @param limit     查询数量
     * @return 历史记录列表
     */
    List<TpPasswordHistory> selectRecentByAccountId(@Param("accountId") String accountId, @Param("limit") int limit);
}
