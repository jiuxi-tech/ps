package com.jiuxi.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @ClassName: ExceptionUtils
 * @Description: 异常工具类
 * @Author: Ypp
 * @Date: 2021/8/4 10:03
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class ExceptionUtils {

    /**
     * 获取异常的堆栈信息
     * @author Ypp
     * @date 2021/8/4 10:08
     * @param t
     * @return java.lang.String
     */
    public static String getStackTrace(Throwable t){
        
        StringWriter sw = new StringWriter ();
        
        PrintWriter pw = new PrintWriter (sw);
        try {
            t.printStackTrace (pw);
            return sw.toString ();
        } finally {
            pw.close ();
        }
    }
}
