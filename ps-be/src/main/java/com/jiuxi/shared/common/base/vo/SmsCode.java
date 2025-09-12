package com.jiuxi.shared.common.base.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 短信配置
 * @ClassName: Code
 * @Author: pand
 * @Date: 2021-04-20 09:55
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class SmsCode {

    /**
     * 发送短信的时间间隔控制，单位：秒
     */
    private Integer timeInterval = 60;

    /**
     * 模版code
     */
    private Map<String, String> templatecode = new HashMap<>(16);

    public Map<String, String> getTemplatecode() {
        return templatecode;
    }

    public void setTemplatecode(Map<String, String> templatecode) {
        this.templatecode = templatecode;
    }

    public Integer getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Integer timeInterval) {
        this.timeInterval = timeInterval;
    }

}
