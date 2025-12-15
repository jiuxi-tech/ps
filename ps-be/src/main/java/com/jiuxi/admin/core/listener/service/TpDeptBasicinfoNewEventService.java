package com.jiuxi.admin.core.listener.service;

import com.jiuxi.admin.core.bean.entity.TpDeptBasicinfo;

/**
 * @Description: 发布事件推送部门基本信息给别的系统（新）
 * @ClassName: TpDeptBasicinfoEventServiceImpl
 * @Author: pdd
 * @Date: 2021-12-27 13:05
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpDeptBasicinfoNewEventService {

    /**
     * 新增部门基本信息
     *
     * @param tpDeptBasicinfo: 部门基本信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void add(TpDeptBasicinfo tpDeptBasicinfo);

    /**
     * 修改部门基本信息
     *
     * @param tpDeptBasicinfo: 部门基本信息
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void update(TpDeptBasicinfo tpDeptBasicinfo);


    /**
     * 删除部门基本信息
     *
     * @param deptId: 部门基本信息id
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void delete(String deptId);
}
