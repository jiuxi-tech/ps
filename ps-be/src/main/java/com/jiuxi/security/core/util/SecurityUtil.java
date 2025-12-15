package com.jiuxi.security.core.util;

import com.jiuxi.common.bean.SessionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName: SecurityUtil
 * @Description: 安全组件 工具类
 * @Author: Ypp
 * @Date: 2023/10/18 16:46
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class SecurityUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtil.class);

    /**
     * 默认所属部门 （1是所属部门，0是兼职部门）
     */
    private static final int DEFAULT_DEPT = 1;


    /**
     * 永久锁，错误次数
     */
    private static final int LOCKED_ALWAYS_ERROR_NUM = 10;

    /**
     * 临时锁，错误次数
     */
    private static final int LOCKED_TEMPORARY_ERROR_NUM = 5;

    /**
     * 永久锁
     */
    public static final String LOCKED_ALWAYS = "-1";


    /**
     * 临时锁， 10 分钟
     */
    public static final String LOCKED_TEMPORARY = "1";


    /**
     * 没锁
     */
    public static final String NO_LOCKED = "0";

    /**
     * 账户过期： -1或空，则永远不过期
     */
    public static final String NO_EXPIRATION = "-1";



    /**
     * 获取默认的 主部门 Session
     *
     * @param personDeptList
     * @return com.jiuxi.security.core.bean.vo.SessionVO
     * @author Ypp
     * @date 2023/10/19 16:08
     */
    public static SessionVO getDefaultDeptSession(List<SessionVO> personDeptList) {

        if (null == personDeptList || personDeptList.size() == 0) {
            return null;
        }

        for (SessionVO session : personDeptList){
            if(session.getDefaultDept() != null && session.getDefaultDept().equals(DEFAULT_DEPT)){
                return session;
            }
        }
        return null;
    }

    /**
     * 获取指定的 部门 Session
     * @author Ypp
     * @date 2023/10/19 18:15
     * @param personDeptList
     * @param deptId
     * @return com.jiuxi.security.core.bean.vo.SessionVO
     */
    public static SessionVO getDeptSession(List<SessionVO> personDeptList, String deptId) {

        if (null == personDeptList || personDeptList.size() == 0) {
            return null;
        }

        for (SessionVO session : personDeptList){
            if(session.getDeptId().equals(deptId)){
                return session;
            }
        }
        return null;
    }
}
