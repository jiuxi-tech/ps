package com.jiuxi.monitor.server.core.service;


import com.jiuxi.monitor.server.bean.ClientHeartbeatInfo;
import com.jiuxi.monitor.server.core.bean.query.TpMonitorClientQuery;
import com.jiuxi.monitor.server.core.bean.entity.TpMonitorClient;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorClientVO;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @ClassName: TpMonitorClientService
 * @Description: 客户端基本信息
 * @Author yangzr
 * @Date 2024-11-18 16:30:42
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpMonitorClientService {

    /**
     * 列表
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    IPage<TpMonitorClientVO>  queryPage(TpMonitorClientQuery query);

    /**
     * 查看
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    TpMonitorClientVO view(String id);

    /**
     * 修改
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    int update(TpMonitorClientVO vo, String jwtpid);

    /**
     * 删除
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    int deleteByIds(List<String> ids, String jwtpid);


    /**
     * 新增客户端信息
     *
     * @param heartbeatInfo
     * @param ip
     * @return void
     * @author jiuxx
     * @date 2024/11/19 13:27
     */
    void add(ClientHeartbeatInfo heartbeatInfo, String ip);

    /**
     * 查询所有客户端信息
     *
     * @return java.util.List<com.jiuxi.monitor.server.core.bean.vo.TpMonitorClientVO>
     * @author jiuxx
     * @date 2024/11/19 14:53
     */
    List<TpMonitorClientVO> listAll();

    /**
     * 查看心跳信息
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    ClientHeartbeatInfo viewHeartbeat(String clientId);

}

