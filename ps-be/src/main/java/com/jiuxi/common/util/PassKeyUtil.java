package com.jiuxi.common.util;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @ClassName: PassKeyUtil
 * @Description: PassKey 工具类
 * @Author: Ypp
 * @Date: 2023/10/24 10:05
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class PassKeyUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PassKeyUtil.class);

    /*
     * PassKey 属性名称
     */
    public static final String PASSKEY_NAME = "passKey";

    /*
     * 前缀
     */
    private static final String CLASSNAME_PREFIX = "com.jiuxi.";

    /*
     * 后缀
     */
    private static final String CLASSNAME_SUFFIX = "VO";

    /*
     *  IPage 前缀
     */
    private static final String IPAGE_CLASSNAME_PREFIX = "com.baomidou.mybatisplus.";

    /*
     *  IPage 实现类名称
     */
    private static final String IPAGE_CLASSNAME = "Page";


    /**
     * 判断是否是 Mybatis 的分页IPage对象
     * @author Ypp
     * @date 2023/11/1 20:37
     * @param vo
     * @return boolean
     */
    public static boolean isMybatisPlusIPage(Object vo) {

        // 当前类的Class对象
        Class<?> cls = vo.getClass();
        // 获取全类名
        String className = cls.getName();
        // 获取类名
        String simpleName = cls.getSimpleName();

        // 判断是否是 IPage 分页对象
        if (StrUtil.startWith(className, IPAGE_CLASSNAME_PREFIX) && IPAGE_CLASSNAME.equals(simpleName)) {
            return  true;
        }
        return false;

    }


    /**
     * 判断是否是 VO 对象
     * @author Ypp
     * @date 2023/10/24 10:28
     * @param vo
     * @return boolean
     */
    public static boolean isTopinfoVO(Object vo) {

        // 当前类的Class对象
        Class<?> cls = vo.getClass();
        // 获取全类名
        String className = cls.getName();

        // 判断是否是 VO 对象
        if (StrUtil.startWith(className, CLASSNAME_PREFIX) && StrUtil.endWith(className,CLASSNAME_SUFFIX)) {
            return  true;
        }
        return false;
    }


    /**
     * 生成 PassKey
     *
     * @param businessKeyVal 业务主键值
     * @param jwtpid  当前登录人
     * @return java.lang.String
     * @author Ypp
     * @date 2023/10/24 10:06
     */
    private static String create(String businessKeyVal, String jwtpid) {
        String context = jwtpid + businessKeyVal;
        // 生成 passKey
        return SmUtil.sm3(context);
    }

    /**
     * 验证 passKey
     *
     * @param passKey 前端传过来的 passkey
     * @param businessKeyVal 业务主键值
     * @param jwtpid  当前登录人
     * @return boolean
     * @author Ypp
     * @date 2023/10/24 10:09
     */
    public static boolean check(String passKey, String businessKeyVal, String jwtpid) {
        // 生成 passKey
        String digestHex = create(businessKeyVal, jwtpid);
        // 比对 passKey
        if (StrUtil.equals(passKey, digestHex)) {
            return true;
        }
        return false;
    }


    /**
     * 生成 passKey
     * @author Ypp
     * @date 2023/11/1 20:52
     * @param item 对象
     * @param jwtpid
     * @param businessKey 业务主键
     * @return void
     */
    private static void createPassKey(Object item, String jwtpid, String businessKey) {

        // 判断 passKey  是否存在
        Field passKeyField = ReflectUtil.getField(item.getClass(), PassKeyUtil.PASSKEY_NAME);
        if (null == passKeyField) {
            LOGGER.error("VO对象:{}，该VO对象缺少passKey属性，生成passKey失败！", item.getClass().getName());
            return;
        }

        // 获取主键字段
        Field field = ReflectUtil.getField(item.getClass(), businessKey);
        // 判断 主键字段 是否存在，不存在，则抛出异常。
        if (null == field) {
            LOGGER.error("VO对象:{}，配置的横向越权业务主键字段:{} 不存在，生成passKey失败！", item.getClass().getName(), businessKey);
            // 不存在，终止执行
            return;
        }

        // 获取 业务主键的值
        String businessKeyVal = (String) ReflectUtil.getFieldValue(item, field);

        // 生成 PassKey
        String passKey = PassKeyUtil.create(businessKeyVal, jwtpid);
        // 把值 赋值 给 passKey
        ReflectUtil.setFieldValue(item, PassKeyUtil.PASSKEY_NAME, passKey);
    }


    /**
     * 生成 PassKey
     * @author Ypp
     * @date 2024/7/24 10:05
     * @param data 可以传入：Page对象，VO对象，List对象
     * @param jwtpid
     * @param businessKey  业务主键
     * @return void
     */
    public static void buildPassKey(Object data, String jwtpid, String businessKey) {

        // 如果返回是列表
        if (PassKeyUtil.isMybatisPlusIPage(data)) {

            // 返回 Page分页对象
            List records = (List) ReflectUtil.getFieldValue(data, "records");
            for (Object item : records) {
                PassKeyUtil.createPassKey(item, jwtpid, businessKey);
            }

        } else if (PassKeyUtil.isTopinfoVO(data)) {
            // 返回 VO 对象
            PassKeyUtil.createPassKey(data, jwtpid, businessKey);

        } else if (data instanceof List) {
            // 普通 list 返回
            for (Object item : (List) data) {
                PassKeyUtil.createPassKey(item, jwtpid, businessKey);
            }

        } else {
            // 不需要生成 passKey
            LOGGER.info("传入的对象不属于【Page对象，VO对象，List】，无法识别，故不生成 passKey");
        }
    }

}
