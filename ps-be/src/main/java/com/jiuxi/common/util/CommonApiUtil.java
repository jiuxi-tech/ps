package com.jiuxi.common.util;

import cn.hutool.extra.spring.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: CommonApiUtil
 * @Description: 公共服务接口 api 工具类
 * @Author: Ypp
 * @Date: 2023/11/14 14:58
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class CommonApiUtil {


    private static final Logger LOGGER = LoggerFactory.getLogger(CommonApiUtil.class);

    /**
     * 获取 公共服务接口 ApiService
     * @author Ypp
     * @date 2023/11/9 13:55
     * @param beanName
     * @return T
     */
    public static <T> T getApiService(String beanName) {
        try {
            return SpringUtil.getBean(beanName);
        } catch (Exception e) {
            LOGGER.error("beanName:{} 找不到实现类, 请检查依赖！", beanName);
            throw new RuntimeException("依赖错误！请联系管理员。");
        }
    }

}
