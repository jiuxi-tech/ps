package com.jiuxi.monitor.server.core.service;


import com.jiuxi.monitor.server.core.bean.query.TpSendMailRecordQuery;
import com.jiuxi.monitor.server.core.bean.entity.TpSendMailRecord;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO;
import com.jiuxi.monitor.server.core.bean.vo.TpSendMailRecordVO;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @ClassName: TpSendMailRecordService
 * @Description: 邮件发送记录表
 * @Author yangzr
 * @Date 2024-11-20 10:36:59
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpSendMailRecordService {

    /**
     * 列表
     *
     * @Author yangzr
     * @Date 2024-11-20 10:36:59
     */
    IPage<TpSendMailRecordVO>  queryPage(TpSendMailRecordQuery query);

    /**
     * 查看
     *
     * @Author yangzr
     * @Date 2024-11-20 10:36:59
     */
    TpSendMailRecordVO view(String id);

    /**
     * 删除
     *
     * @Author yangzr
     * @Date 2024-11-20 10:36:59
     */
    int deleteByIds(List<String> ids, String jwtpid);

    /**
     * 保存
     *
     * @param user    收件人姓名
     * @param email   收件人邮箱
     * @param bool    是否发送成功
     * @param content 发送内容
     * @return void
     * @author jiuxx
     * @date 2024/11/20 16:45
     */
    void add(String user, String email, boolean bool, String content);
}

