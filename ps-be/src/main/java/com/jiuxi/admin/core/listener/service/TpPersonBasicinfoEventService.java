package com.jiuxi.admin.core.listener.service;

import com.jiuxi.admin.core.bean.entity.TpPersonBasicinfo;

/**
 * @Description: 发布事件推送人员信息给别的系统
 * @ClassName: TpPersonBasicinfoEventService
 * @Author: pand
 * @Date: 2021-06-08 11:16
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public interface TpPersonBasicinfoEventService {

    /**
     * 新增人员
     *
     * @param tpPersonBasicinfo: 用户信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void add(TpPersonBasicinfo tpPersonBasicinfo);


    /**
     * 修改人员
     *
     * @param tpPersonBasicinfo: 用户信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void update(TpPersonBasicinfo tpPersonBasicinfo);


    /**
     * 删除人员
     *
     * @param personId: 用户id
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void delete(String personId);

}
