package com.jiuxi.admin.core.listener.service;

import com.jiuxi.admin.core.bean.entity.TpAccount;

/**
 * @Description: 发布事件推送账号信息给别的系统
 * @ClassName: TpAccountEventService
 * @Author: pand
 * @Date: 2021-06-08 11:16
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public interface TpAccountEventService {

    /**
     * 新增账号
     *
     * @param tpAccount: 账号信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void add(TpAccount tpAccount);


    /**
     * 修改账号
     *
     * @param tpAccount: 账号信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void update(TpAccount tpAccount);


    /**
     * 修改账号
     *
     * @param tpAccount: 账号信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void updatePwd(TpAccount tpAccount);

    /**
     * 冻结/解冻 账号
     *
     * @param tpAccount: 账号信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void lockedPwd(TpAccount tpAccount);

    /**
     * 启用/停用 账号
     *
     * @param tpAccount: 账号信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void enabledPwd(TpAccount tpAccount);


    /**
     * 删除账号
     *
     * @param accountId: 账号id
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void delete(String accountId);

}
