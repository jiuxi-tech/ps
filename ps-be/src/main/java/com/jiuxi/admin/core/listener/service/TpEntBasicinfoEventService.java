package com.jiuxi.admin.core.listener.service;

import com.jiuxi.admin.core.bean.entity.TpEntBasicinfo;

/**
 * @Description: 发布事件推送企业基本信息给别的系统
 * @ClassName: TpEntBasicinfoEventService
 * @Author: pdd
 * @Date: 2021-06-08 17:16
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */

public interface TpEntBasicinfoEventService {

    /**
     * 新增企业基本信息
     *
     * @param tpEntBasicinfo: 企业基本信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void add(TpEntBasicinfo tpEntBasicinfo);


    /**
     * 修改企业基本信息
     *
     * @param tpEntBasicinfo: 企业基本信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void update(TpEntBasicinfo tpEntBasicinfo);


    /**
     * 删除企业基本信息
     *
     * @param entId: 企业基本信息id
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void delete(String entId);

}
