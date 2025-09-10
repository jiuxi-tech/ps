package com.jiuxi.admin.core.listener.service;

import com.jiuxi.admin.core.bean.entity.TpCity;

/**
 * @Description: 发布事件推送行政区划信息给别的系统
 * @ClassName: TpCityEventService
 * @Author: pand
 * @Date: 2021-06-08 17:31
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */

public interface TpCityEventService {

    /**
     * 新增行政区划基本信息
     *
     * @param tpCity: 行政区划基本信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void add(TpCity tpCity);


    /**
     * 修改行政区划基本信息
     *
     * @param tpCity: 行政区划基本信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void update(TpCity tpCity);


    /**
     * 删除行政区划基本信息
     *
     * @param cityId: 行政区划基本信息id
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void delete(String cityId);

}
