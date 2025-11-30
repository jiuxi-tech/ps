package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.query.TpPasswordHistoryQuery;
import com.jiuxi.admin.core.bean.vo.TpPasswordHistoryVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 密码修改历史服务接口
 *
 * @author system
 * @date 2025-12-01
 */
public interface PasswordHistoryService {

    /**
     * 保存密码历史记录
     *
     * @param accountId    账号ID
     * @param personId     人员ID
     * @param username     用户名
     * @param passwordHash 密码哈希值
     * @param changeType   修改类型：1-用户主动修改 2-管理员重置 3-密码过期强制修改 4-其他
     * @param changeReason 修改原因
     * @param changedBy    操作人ID
     * @param changedByName 操作人姓名
     * @param ipAddress    IP地址
     * @param userAgent    用户代理
     * @param tenantId     租户ID
     * @return 保存结果
     */
    int savePasswordHistory(String accountId, String personId, String username, String passwordHash,
                           Integer changeType, String changeReason, String changedBy, String changedByName,
                           String ipAddress, String userAgent, String tenantId);

    /**
     * 分页查询密码历史列表
     *
     * @param query 查询条件
     * @return 分页数据
     */
    Page<TpPasswordHistoryVO> list(TpPasswordHistoryQuery query);

    /**
     * 根据ID查询详情
     *
     * @param historyId 历史记录ID
     * @return 历史记录详情
     */
    TpPasswordHistoryVO view(String historyId);

    /**
     * 根据账号ID查询最近N条密码历史
     *
     * @param accountId 账号ID
     * @param limit     查询数量
     * @return 历史记录列表
     */
    java.util.List<com.jiuxi.admin.core.bean.entity.TpPasswordHistory> getRecentPasswordHistory(String accountId, int limit);
}
