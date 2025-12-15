package com.jiuxi.common.util;

import cn.hutool.core.util.StrUtil;

/**
 * @ClassName: CommonUniqueIndexUtil
 * @Description: 唯一索引工具类
 * <pre>
 *     唯一索引使用场景：
 *         1. 统一信用代码
 *         2. 账号
 *         3. 手机号
 *         4. 部门层级编码
 *         5. 字典编码
 *         6. 行政区划编码
 *         ...
 *     唯一索引问题：
 *         删除一条数据，再次新增时，唯一索引重复，无法新增
 *     解决方案：
 *         1. 数据删除时，存在唯一索引的字段添加当前时间
 *            如： 统一信用代码 = 统一信用代码 + "#" + 20240529184600
 *     注意事项：
 *         1. 一个表如果存在多个唯一索引，则删除数据时，每个字段都需要修改（添加时间后缀）
 *         2. 唯一索引字段的长度需能够满足增加15位字符
 * </pre>
 * @Author jiuxx
 * @Date 2024/5/29 18:46
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public class CommonUniqueIndexUtil {

    /**
     * 分割符号
     */
    private static final String SPLIT_SYMBOL = "#";

    /**
     * 添加删除时间
     *
     * @param code 唯一索引字段
     * @return java.lang.String
     * @author jiuxx
     * @date 2024/5/29 18:59
     */
    public static String addDeleteTime(String code) {

        return code + SPLIT_SYMBOL + CommonDateUtil.now();
    }

    /**
     * 去除删除时间
     *
     * @param code 唯一索引字段
     * @return java.lang.String
     * @author jiuxx
     * @date 2024/5/29 18:59
     */
    public static String removeDeleteTime(String code){
        // 为空或长度小于15，直接返回
        if (StrUtil.isBlank(code) || code.length() < 15){
            return code;
        }

        int index = code.lastIndexOf(SPLIT_SYMBOL);
        // 如果分割符的位置不是在指定位置，说明数据不需要处理
        if (code.length() != index + 15){
            return code;
        }

        return code.substring(0, index);
    }

}
