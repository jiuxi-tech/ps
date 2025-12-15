package com.jiuxi.monitor.client.constant;

import com.jiuxi.monitor.common.constant.MonitorCommonConstant;

/**
 * @ClassName: MonitorClientConstant
 * @Description:
 * @Author jiuxx
 * @Date 2024/11/15 13:37
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public class MonitorClientConstant extends MonitorCommonConstant {

    /**
     * 长期有效的token
     */
    public static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3YjA0MzE3YWM3ZjUzNThhZGIzMDQ4ZmViZDQzMjg3MWMzMDcwNGIzMjlmNWVlN2U1MDlmMTk4NmEyMzZkNmRhOTI4OWIxMzljZDk3YzI5MGQwOGExNWEwZmFkMzU5OTYiLCJpc3MiOiJjb20udG9waW5mbyIsImV4cCI6NDg4NTI0NzM5OSwidmVyc2lvbiI6InYzIiwiaWF0IjoxNzMxNjQ3Mzk5LCJqdGkiOiIzMmIyZjJlZTc4NDE0MGEzYWUyYWUxNDVhMmJlZTY4ZSJ9.sah2xCx87lYSjrkLRF9x3883WK7Lb55yo_6axnXcqMw";


    /**
     * 测试redis是否存活时使用的key
     */
    public static final String TEST_REDIS_KEY = "test_health_redis_key";

    /**
     * 健康检查专用topic
     */
    public static final String JIUXI_HEALTH_MONITOR = "jiuxi_health_monitor";

    /**
     * 健康检查单个服务等待时间
     */
    public static final int WAIT_SECONDS = 5;
}
