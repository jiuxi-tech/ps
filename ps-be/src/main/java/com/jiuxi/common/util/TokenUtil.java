package com.jiuxi.common.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.jiuxi.common.bean.SessionVO;

import java.util.Map;

/**
 * @ClassName: TokenUtil
 * @Description: token 工具， 屏蔽 token 的版本差异
 * @Author: Ypp
 * @Date: 2023/11/16 13:58
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TokenUtil {

    /**
     * 解析 token
     * @author Ypp
     * @date 2023/11/16 13:59
     * @param token
     * @return boolean
     */
    public static boolean checkToken(String token) {
        return JwtUtil.checkToken(token);
    }


    /**
     * 判断 token 是否是 v3 版本的
     * @author Ypp
     * @date 2023/11/16 14:37
     * @param token
     * @return boolean
     */
    public static boolean isTokenV3(String token) {
        Map<String, Claim> claimMap = JwtUtil.getPayloadClaim(token);
        Claim claim = claimMap.get("version");
        // 如果版本号位空，则是 2.0.0版本以前的token
        if (null == claim) {
            return false;
        } else {
            // 2.1.0 版本的token, 版本没有 扩展信息
            return true;
        }
    }



    /**
     *  获得token中的Exinfo
     * @author Ypp
     * @date 2023/11/16 14:04
     * @param token
     * @return java.lang.String
     */
    public static String getExinfo(String token) {
        Map<String, Claim> claimMap = JwtUtil.getPayloadClaim(token);
        Claim claim = claimMap.get("version");
        // 如果版本号位空，则是 2.0.0版本以前的token
        if (null == claim) {
            return JwtTokenUtils.getExinfo(token);
        } else {
            // 2.1.0 版本的token, 版本没有 扩展信息
            return "";
        }
    }


    /**
     *  获得token中的TenantId
     * @author Ypp
     * @date 2023/11/16 14:04
     * @param token
     * @return java.lang.String
     */
    public static String getTenantId(String token) {
        Map<String, Claim> claimMap = JwtUtil.getPayloadClaim(token);
        Claim claim = claimMap.get("version");
        // 如果版本号位空，则是 2.0.0版本以前的token
        if (null == claim) {
            return JwtTokenUtils.getTenantId(token);
        } else {
            // 2.1.0 版本的token,
            SessionVO sessionVO = getSessionVO(token);
            return sessionVO.getTenantId();
        }
    }


    /**
     *  获得token中的getAscnId
     * @author Ypp
     * @date 2023/11/16 14:04
     * @param token
     * @return java.lang.String
     */
    public static String getAscnId(String token) {
        Map<String, Claim> claimMap = JwtUtil.getPayloadClaim(token);
        Claim claim = claimMap.get("version");
        // 如果版本号位空，则是 2.0.0版本以前的token
        if (null == claim) {
            return JwtTokenUtils.getAscnId(token);
        } else {
            // 2.1.0 版本的token,
            SessionVO sessionVO = getSessionVO(token);
            return sessionVO.getAscnId();
        }
    }

    /**
     *  获得token中的getPersonId
     * @author Ypp
     * @date 2023/11/16 14:04
     * @param token
     * @return java.lang.String
     */
    public static String getPersonId(String token) {
        Map<String, Claim> claimMap = JwtUtil.getPayloadClaim(token);
        Claim claim = claimMap.get("version");
        // 如果版本号位空，则是 2.0.0版本以前的token
        if (null == claim) {
            return JwtTokenUtils.getPersonId(token);
        } else {
            // 2.1.0 版本的token,
            SessionVO sessionVO = getSessionVO(token);
            return sessionVO.getPersonId();
        }
    }


    /**
     *  获得token中的getRoles
     * @author Ypp
     * @date 2023/11/16 14:04
     * @param token
     * @return java.lang.String
     */
    public static String getRoles(String token) {
        Map<String, Claim> claimMap = JwtUtil.getPayloadClaim(token);
        Claim claim = claimMap.get("version");
        // 如果版本号位空，则是 2.0.0版本以前的token
        if (null == claim) {
            return JwtTokenUtils.getRoles(token);
        } else {
            // 2.1.0 版本的token,
            SessionVO sessionVO = getSessionVO(token);
            return sessionVO.getRoleIds();
        }
    }

    /**
     *  获得token中的getRoles
     * @author Ypp
     * @date 2023/11/16 14:04
     * @param token
     * @return java.lang.String
     */
    public static String getDepts(String token) {
        Map<String, Claim> claimMap = JwtUtil.getPayloadClaim(token);
        Claim claim = claimMap.get("version");
        // 如果版本号位空，则是 2.0.0版本以前的token
        if (null == claim) {
            return JwtTokenUtils.getDepts(token);
        } else {
            // 2.1.0 版本的token,
            SessionVO sessionVO = getSessionVO(token);
            return sessionVO.getDeptId();
        }
    }

    /**
     *  获得token中的getCityCode
     * @author Ypp
     * @date 2023/11/16 14:04
     * @param token
     * @return java.lang.String
     */
    public static String getCityCode(String token) {
        Map<String, Claim> claimMap = JwtUtil.getPayloadClaim(token);
        Claim claim = claimMap.get("version");
        // 如果版本号位空，则是 2.0.0版本以前的token
        if (null == claim) {
            return JwtTokenUtils.getCityCode(token);
        } else {
            // 2.1.0 版本的token,
            SessionVO sessionVO = getSessionVO(token);
            return sessionVO.getCityCode();
        }
    }

    /**
     * 获取 SessionVO
     * @author Ypp
     * @date 2023/11/16 14:21
     * @param
     * @return SessionVO
     */
    private static SessionVO getSessionVO(String token) {
        String sub = JwtUtil.getToken(token);
        SessionVO sessionVO = JSON.parseObject(sub, SessionVO.class);
        return sessionVO;
    }
}
