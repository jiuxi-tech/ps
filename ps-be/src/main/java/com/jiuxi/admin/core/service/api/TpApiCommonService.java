package com.jiuxi.admin.core.service.api;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TpApiCommonService
 * @Description:
 * @Author 杨占锐
 * @Date 2024/5/27 18:08
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpApiCommonService")
public class TpApiCommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpApiCommonService.class);

    /**
     * 复制数据到指定类型的对象中
     *
     * @param source 源数据
     * @param clazz  返回数据的对象类型
     * @return T    返回对象
     * @author 杨占锐
     * @date 2024/5/27 18:10
     */
    public <T> T copy(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }

        try {
            T target = clazz.newInstance();
            // 属性复制
            BeanUtil.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            LOGGER.error("【复制数据到指定类型的对象中】失败！source：{}, clazz：{}, 错误：{}", JSONObject.toJSONString(source), clazz.getName(), ExceptionUtils.getStackTrace(e));
        }

        return null;
    }

}
