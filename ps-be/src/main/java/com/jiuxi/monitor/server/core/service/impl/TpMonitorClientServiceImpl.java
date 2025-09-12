package com.jiuxi.monitor.server.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.monitor.server.bean.ClientHeartbeatInfo;
import com.jiuxi.monitor.server.constant.MonitorServerConstant;
import com.jiuxi.monitor.server.core.bean.entity.TpMonitorClient;
import com.jiuxi.monitor.server.core.bean.query.TpMonitorClientQuery;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorClientVO;
import com.jiuxi.monitor.server.core.mapper.TpMonitorClientMapper;
import com.jiuxi.monitor.server.core.service.MonitorAlarmService;
import com.jiuxi.monitor.server.core.service.MonitorCacheService;
import com.jiuxi.monitor.server.core.service.TpMonitorClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: TpMonitorClientServiceImpl
 * @Description: 客户端基本信息
 * @Author yangzr
 * @Date 2024-11-18 16:30:42
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service
public class TpMonitorClientServiceImpl implements TpMonitorClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpMonitorClientServiceImpl.class);

    @Autowired
    private TpMonitorClientMapper tpMonitorClientMapper;
    @Autowired
    private MonitorCacheService monitorCacheService;
    @Autowired
    private MonitorAlarmService monitorAlarmService;

    /**
     * 列表
     * <pre>
     *     使用代码进行分页
     *     1. 查询所有客户端信息，不分页
     *     2. 添加在离线状态
     *     3. 过滤在离线状态
     * </pre>
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @Override
    public IPage<TpMonitorClientVO> queryPage(TpMonitorClientQuery query) {

        PageDTO<TpMonitorClientVO> pageDTO = new PageDTO<>();

        try {
            // 1.查询所有客户端信息，不分页
            List<TpMonitorClientVO> clientVOS = tpMonitorClientMapper.listAll(query);
            if (CollectionUtil.isEmpty(clientVOS)) {
                return pageDTO;
            }

            // 2.添加心跳信息
            this.setHeartbeatInfo(clientVOS);

            // 3.过滤在离线状态
            if (null != query.getStatus()) {
                this.filterStatus(clientVOS, query.getStatus());
            }

            // 4.分页
            if (CollectionUtil.isNotEmpty(clientVOS)) {
                int skip = Math.min((query.getCurrent() - 1) * query.getSize(), clientVOS.size() - 1);
                int toIndex = Math.min(skip + query.getSize(), clientVOS.size());
                List<TpMonitorClientVO> records = clientVOS.subList(skip, toIndex);
                pageDTO.setRecords(records);
                pageDTO.setTotal(clientVOS.size());
                pageDTO.setSize(100);
            }

            return pageDTO;
        } catch (Exception e) {
            LOGGER.error("列表信息查询失败！query:{}, e: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "列表信息查询失败！");
        }

    }

    /**
     * 根据查询条件中的状态过滤
     *
     * @param clientVOS
     * @param status
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 17:24
     */
    private void filterStatus(List<TpMonitorClientVO> clientVOS, int status) {
        clientVOS.removeIf(next -> next.getStatus() != status);
    }

    /**
     * 添加在离线状态
     *
     * @param clientVOS
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 17:00
     */
    private void setHeartbeatInfo(List<TpMonitorClientVO> clientVOS) {

        for (TpMonitorClientVO vo : clientVOS) {
            // 默认为离线状态
            vo.setStatus(TpConstant.NO);
            ClientHeartbeatInfo heartbeat = monitorCacheService.getHeartbeat(vo.getClientId());
            if (null != heartbeat) {
                String lastUpdateTime = heartbeat.getLastUpdateTime();
                if (StrUtil.isNotBlank(lastUpdateTime)) {
                    // 判断是否在线
                    if (LocalDateTime.now().isBefore(CommonDateUtil.parse(lastUpdateTime).plusMinutes(5))) {
                        vo.setStatus(TpConstant.YES);
                        // 在线时，再添加报警信息
                        this.addAlarmMsg(vo, heartbeat);
                    }
                }
            }
        }
    }

    /**
     * 添加报警信息
     *
     * @param vo
     * @param heartbeat
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 18:11
     */
    private void addAlarmMsg(TpMonitorClientVO vo, ClientHeartbeatInfo heartbeat) {
        Map<String, String> map = monitorAlarmService.getAlarmSource(heartbeat);
        Set<String> alarmSource = new HashSet<>(map.keySet());

        if (null != heartbeat.getNotHealthServer()) {
            alarmSource.addAll(heartbeat.getNotHealthServer());
        }
        if (alarmSource.size() > 0) {
            StringBuilder builder = new StringBuilder();
            int index = 1;
            for (String source : alarmSource) {
                builder.append(MonitorServerConstant.SOURCE_MAP.get(source));
                if (index < alarmSource.size()) {
                    builder.append(",");
                }
                index++;
            }
            vo.setAlarmMsg(builder.toString());
        }
    }

    /**
     * 查询所有客户端信息
     *
     * @return java.util.List<com.jiuxi.monitor.server.core.bean.vo.TpMonitorClientVO>
     * @author 杨占锐
     * @date 2024/11/19 14:53
     */
    @Override
    public List<TpMonitorClientVO> listAll() {
        return tpMonitorClientMapper.listAll(null);
    }

    /**
     * 查看
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @Override
    public TpMonitorClientVO view(String id) {

        try {
            TpMonitorClientVO vo = tpMonitorClientMapper.view(id);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！id: {}, e: {}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

    /**
     * 查看心跳信息
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @Override
    public ClientHeartbeatInfo viewHeartbeat(String clientId) {
        return monitorCacheService.getHeartbeat(clientId);
    }

    /**
     * 新增客户端信息
     *
     * @param info
     * @param ip
     * @return void
     * @author 杨占锐
     * @date 2024/11/19 13:27
     */
    @Override
    public void add(ClientHeartbeatInfo info, String ip) {

        try {

            TpMonitorClient bean = new TpMonitorClient();
            String psnId = "heartbeat";
            // 创建人
            bean.setCreator(psnId);
            String now = CommonDateUtil.now();
            // 创建时间
            bean.setCreateTime(now);
            // 修改人
            bean.setUpdator(psnId);
            // 修改时间
            bean.setUpdateTime(now);
            // 是否有效
            bean.setActived(TpConstant.YES);

            // 设置主键id
            bean.setClientId(info.getClientId());
            // 系统名称
            bean.setApplicationName(info.getApplicationName());
            // mac地址
            bean.setMacAddr(info.getMacAddr());
            // 部署路径
            bean.setAbsolutePath(info.getAbsolutePath());
            // 系统描述
            bean.setSystemDesc(info.getSystemDesc());
            // ip链路
            bean.setIp(ip);

            // 新增数据
            tpMonitorClientMapper.add(bean);

            // 清空缓存
            monitorCacheService.clearClientInfo(bean.getClientId());
        } catch (Exception e) {
            LOGGER.error("新增失败！vo: {}, e: {}", JSONObject.toJSONString(info), ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 修改
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @Override
    public int update(TpMonitorClientVO vo, String jwtpid) {

        TpMonitorClient bean = new TpMonitorClient();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        bean.setUpdator(jwtpid);
        bean.setUpdateTime(CommonDateUtil.now());

        try {
            int count = tpMonitorClientMapper.update(bean);

            // 清空缓存
            monitorCacheService.clearClientInfo(bean.getClientId());
            return count;
        } catch (Exception e) {
            LOGGER.error("修改失败！vo: {}, e: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

    /**
     * 删除
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int deleteByIds(List<String> ids, String jwtpid) {

        if (null == ids || ids.isEmpty()) {
            throw new TopinfoRuntimeException(-1, "删除数据id不能为空！");
        }
        try {

            ids.forEach(id -> {
                tpMonitorClientMapper.delete(id);
                monitorCacheService.clearClientInfo(id);
            });
            return ids.size();
        } catch (Exception e) {
            LOGGER.error("删除失败！ids: {}, jwtpid:{}, e: {}", ids, jwtpid, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }


}
