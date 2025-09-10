package com.jiuxi.shared.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import java.util.List;
import java.util.Map;

/**
 * Bean工具类
 * 提供对象拷贝、转换等功能
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public final class BeanUtils {

    private BeanUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 对象属性拷贝
     */
    public static void copyProperties(Object source, Object target) {
        if (source != null && target != null) {
            BeanUtil.copyProperties(source, target);
        }
    }

    /**
     * 对象属性拷贝（忽略null值）
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        if (source != null && target != null) {
            BeanUtil.copyProperties(source, target, CopyOptions.create().ignoreNullValue());
        }
    }

    /**
     * 对象转Map
     */
    public static Map<String, Object> toMap(Object bean) {
        return BeanUtil.beanToMap(bean);
    }

    /**
     * Map转对象
     */
    public static <T> T toBean(Map<String, Object> map, Class<T> beanClass) {
        return BeanUtil.mapToBean(map, beanClass, false);
    }

    /**
     * 对象列表拷贝
     */
    public static <T> List<T> copyList(List<?> sourceList, Class<T> targetClass) {
        return BeanUtil.copyToList(sourceList, targetClass);
    }
}