package com.jiuxi.monitor.server.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.monitor.server.autoconfig.MonitorServerAutoConfigurationProperties;
import com.jiuxi.monitor.server.bean.MailConfig;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO;
import com.jiuxi.monitor.server.core.service.MonitorSendMailService;
import com.jiuxi.monitor.server.core.service.TpSendMailRecordService;
import com.jiuxi.monitor.server.core.util.MonitorMailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: MonitorSendMailServiceImpl
 * @Description:
 * @Author jiuxx
 * @Date 2024/11/20 15:59
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service
public class MonitorSendMailServiceImpl implements MonitorSendMailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorSendMailServiceImpl.class);

    private static final String SUBJECT = "服务器报警";

    @Autowired
    private MonitorServerAutoConfigurationProperties properties;
    @Autowired
    private TpSendMailRecordService sendMailRecordService;

    /**
     * 发送邮件, 有一个发送成功即判定为成功
     *
     * @param config  邮件配置
     * @param content 发送的内容
     * @return boolean
     * @author jiuxx
     * @date 2024/11/20 16:16
     */
    @Override
    public boolean sendMail(TpMonitorConfigVO config, String content) {

        LOGGER.info("开始发送邮件，config： {}, 邮件内容：{}" , JSONObject.toJSONString(config), content);

        boolean result = false;

        String[] users = config.getPrincipal().split(",");
        String[] emails = config.getEmail().split(",");

        if (users.length != emails.length) {
            LOGGER.error("发送邮件失败, principal 与 email 数量不一致");
            return result;
        }

        for (int i = 0; i < users.length; i++) {

            // 1. 发送邮件
            boolean bool = false;
            try {

                bool = sendMail(emails[i], SUBJECT, content);
                if (bool) {
                    result = true;
                }
            } catch (Exception e) {
                // 异常堆栈信息已在sendMail（）方法中打印，此处不再打印
                LOGGER.error("发送邮件失败，config：{}，content：{}", JSONObject.toJSONString(config), content);
            }

            // 2. 保存邮件发送记录
            try {
                sendMailRecordService.add(users[i], emails[i], bool, content);
            } catch (Exception e) {
                LOGGER.error("保存邮件发送记录失败，错误：{}，config：{}，content：{}", ExceptionUtils.getStackTrace(e), JSONObject.toJSONString(config), content);
            }
        }

        return result;
    }


    /**
     * 发送邮件
     *
     * @param email
     * @param subject
     * @param content
     * @return boolean
     * @author jiuxx
     * @date 2024/11/20 16:16
     */
    private boolean sendMail(String email, String subject, String content) {
        MailConfig mailConfig = properties.getMailConfig();
        if (null == mailConfig) {
            LOGGER.error("邮件配置为空，不发送邮件，content： {}", content);
            return false;
        }
        return MonitorMailUtil.send(mailConfig, email, subject, content);
    }
}
