package com.jiuxi.security.core.service;

import com.jiuxi.security.core.entity.vo.AccountThirdVO;
import com.jiuxi.security.core.entity.vo.AccountVO;

/**
 * @ClassName: TopinfoSecurityCommonService
 * @Description: 安全组件 公共service
 * @Author: 杨攀
 * @Date: 2023/11/1 16:18
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 */
public interface TopinfoSecurityCommonService {


    /**
     * 生成token
     * @author 杨攀
     * @date 2023/11/1 16:23
     * @param accountVO
     * @return java.lang.String
     */
    String createToken(AccountVO accountVO);

    /**
     * 合作方获取token
     * @author 杨攀
     * @date 2023/11/1 16:49
     * @param appKey
     * @param accountThirdVO
     * @return java.lang.String
     */
    String createClientToken(String appKey, AccountThirdVO accountThirdVO);


    /**
     * 人员兼职部门的时候，通过该接口选择要登录的部门
     * @author 杨攀
     * @date 2023/11/1 17:09
     * @param deptId
     * @param jwtpid
     * @return com.jiuxi.security.core.entity.vo.AccountVO
     */
    AccountVO selectDept(String deptId, String jwtpid);


    /**
     * 刷新 token
     * @author 杨攀
     * @date 2023/10/23 14:44
     * @param
     * @return java.lang.String
     */
    String refreshToken(String jwttoken);


    /**
     * 交换token
     * @author 杨攀
     * @date 2023/11/1 17:22
     * @param token 源token
     * @param version 目标token的版本号，版本号： 2.0、2.1、3.0
     * @return java.lang.String  目标版本的token
     */
    String exchangeToken(String token, String version);
}
