package com.jiuxi.monitor.server.core.bean.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AlarmSource
 * @Description: 报警资源
 * @Author jiuxx
 * @Date 2024/11/28 14:43
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public class AlarmSource {

    /**
     * 超阈值的的报警资源 key:报警资源， val: 阈值
     */
    private Map<String, String> alarmMap = new HashMap<>();

    /**
     * 需要发送邮件的报警资源
     */
    private List<String> alarmList = new ArrayList<>();

    public Map<String, String> getAlarmMap() {
        return alarmMap;
    }

    public void setAlarmMap(Map<String, String> alarmMap) {
        this.alarmMap = alarmMap;
    }

    public List<String> getAlarmList() {
        return alarmList;
    }

    public void setAlarmList(List<String> alarmList) {
        this.alarmList = alarmList;
    }
}
