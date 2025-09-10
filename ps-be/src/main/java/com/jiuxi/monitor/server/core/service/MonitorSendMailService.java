package com.jiuxi.monitor.server.core.service;

import com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO;

/**
 * @ClassName: MonitorSendMailService
 * @Description: 发送邮件服务
 * @Author 杨占锐
 * @Date 2024/11/20 15:59
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface MonitorSendMailService {


    /**
     * 发送邮件
     *
     * @param config  邮件配置
     * @param content 发送的内容
     * @return boolean
     * @author 杨占锐
     * @date 2024/11/20 16:16
     */
    boolean sendMail(TpMonitorConfigVO config, String content);
}
