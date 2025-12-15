package com.jiuxi.admin.core.listener.service;

import com.jiuxi.admin.core.bean.entity.TpDeptBasicinfo;

/**
 * @Description: 发布事件推送部门基本信息给别的系统, 废弃原因：TpDeptBasicinfoEvent类的add,update,delete方法中的if控制方法，不在框架中控制，需要业务上自己控制。
 * @ClassName: TpDeptBasicinfoEventService
 * @Author: pdd
 * @Date: 2021-06-08 17:39
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
@Deprecated
public interface TpDeptBasicinfoEventService {

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
