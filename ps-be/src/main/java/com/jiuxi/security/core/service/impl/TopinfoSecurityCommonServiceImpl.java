package com.jiuxi.security.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.jiuxi.common.bean.ErrorCode;
import com.jiuxi.common.bean.SessionVO;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.common.util.CommonRequestUtil;
import com.jiuxi.common.util.JwtTokenUtils;
import com.jiuxi.common.util.JwtUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import com.jiuxi.security.bean.Authentication;
import com.jiuxi.security.core.entity.vo.AccountThirdVO;
import com.jiuxi.security.core.entity.vo.AccountVO;
import com.jiuxi.security.core.holder.SessionHolder;
import com.jiuxi.security.core.service.TopinfoSecurityCommonService;
import com.jiuxi.security.core.service.TopinfoSecurityLogoutService;
import com.jiuxi.security.core.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName: TopinfoSecurityCommonServiceImpl
 * @Description: 安全组件 公共service
 * @Author: Ypp
 * @Date: 2023/11/1 16:21
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TopinfoSecurityCommonServiceImpl implements TopinfoSecurityCommonService {


    private static final Logger LOGGER = LoggerFactory.getLogger(TopinfoSecurityCommonServiceImpl.class);

    private static final String sessionSql = "select u.TENANT_ID,t.ASCN_ID,a.DEPT_FULL_NAME ASCN_NAME,u.PERSON_ID,u.PERSON_NAME,u.CATEGORY,t.DEPT_ID,t.DEPT_LEVELCODE,t.DEPT_FULL_NAME,t.DEPT_SIMPLE_NAME,t.CITY_CODE,d.DEFAULT_DEPT from tp_person_basicinfo u inner join tp_person_dept d on d.PERSON_ID = u.PERSON_ID inner join tp_dept_basicinfo t on t.DEPT_ID = d.DEPT_ID inner join tp_dept_basicinfo a on a.DEPT_ID = t.ASCN_ID where u.PERSON_ID = ? and u.ACTIVED = 1 ";

    private static final String roleSql = " select ROLE_ID from tp_person_role where PERSON_ID = ? and DEPT_ID = ?";

    /**
     * 合作方获取token,默认时间，单位分钟
     */
    private static final int TOKEN_TIMEOUT = 120;

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SecurityConfigurationProperties properties;

    @Autowired
    private TopinfoSecurityLogoutService logoutService;

    /**
     * 生成token
     *
     * @param accountVO
     * @return java.lang.String
     * @author Ypp
     * @date 2023/11/1 16:23
     */
    @Override
    public String createToken(AccountVO accountVO) {

        if (jdbcTemplate == null) {
            throw new RuntimeException("在执行认证时，jdbcTemplate为null，请先在项目中引入了连接池的配置...");
        }

        SessionVO sessionVO = this.createSession(accountVO.getPersonId(), null);

        Authentication authentication = properties.getAuthentication();
        // 生成 token
        String token = JwtUtil.createToken(JSONUtil.toJsonStr(sessionVO), authentication.getTokenTimeOut());

        // 设置响应头
        HttpServletResponse response = CommonRequestUtil.getHttpServletResponse();
        response.setHeader("token", token);

        return token;
    }


    /**
     * 合作方获取token
     *
     * @param appKey
     * @param accountThirdVO
     * @return java.lang.String
     * @author Ypp
     * @date 2023/11/1 16:49
     */
    @Override
    public String createClientToken(String appKey, AccountThirdVO accountThirdVO) {

        SessionVO sessionVO = new SessionVO();
        sessionVO.setPersonId(appKey);
        // 生成 token
        String token = JwtUtil.createToken(JSONUtil.toJsonStr(sessionVO), TOKEN_TIMEOUT);

        // 设置响应头
        HttpServletResponse response = CommonRequestUtil.getHttpServletResponse();
        response.setHeader("token", token);

        return token;
    }


    /**
     * 人员兼职部门的时候，通过该接口选择要登录的部门
     *
     * @param deptId
     * @param jwtpid
     * @return com.jiuxi.security.core.entity.vo.AccountVO
     * @author Ypp
     * @date 2023/11/1 17:09
     */
    @Override
    public AccountVO selectDept(String deptId, String jwtpid) {

        SessionVO sessionVO = this.createSession(jwtpid, deptId);

        Authentication authentication = properties.getAuthentication();
        // 生成 token
        String token = JwtUtil.createToken(JSONUtil.toJsonStr(sessionVO), authentication.getTokenTimeOut());

        // 设置响应头
        HttpServletResponse response = CommonRequestUtil.getHttpServletResponse();
        response.setHeader("token", token);

        AccountVO vo = new AccountVO();
        vo.setTenantId(sessionVO.getTenantId());
        vo.setPersonId(jwtpid);
        vo.setAscnId(sessionVO.getAscnId());
        vo.setCityCode(sessionVO.getCityCode());
        vo.setDeptIds(sessionVO.getDeptId());
        vo.setRoleIds(sessionVO.getRoleIds());

        vo.setToken(token);

        return vo;
    }


    /**
     * 老版 JwtTokenUtils， 用token放入到response的header中
     *
     * @param vo:
     * @param exinfo:   token自定义字段
     * @param response:
     * @return void
     * @author pand
     * @date 2020-08-27 19:35
     */
    public String createToken(AccountVO vo, String exinfo, HttpServletResponse response) {

        // 生成token
        try {
            String token = JwtTokenUtils.createToken(vo.getTenantId(), vo.getAscnId(), vo.getPersonId(), vo.getDeptIds(), vo.getCityCode(), vo.getRoleIds(), exinfo);

            // 存放到头文件中
            String tokenHeader = properties.getAuthorization().getTokenHeader();
            response.setHeader(tokenHeader, token);
            return token;
        } catch (Exception e) {
            LOGGER.error("Token生成失败: {}", ExceptionUtil.stacktraceToString(e));
            throw new RuntimeException("Token生成失败");
        }
    }


    /**
     * 创建session
     *
     * @param personId
     * @param deptId
     * @return com.jiuxi.security.core.entity.vo.SessionVO
     * @author Ypp
     * @date 2023/11/1 16:46
     */
    private SessionVO createSession(String personId, String deptId) {

        // BeanPropertyRowMapper 字段转换时，注意数据库字段与实体属性的对应
        List<SessionVO> personDeptList = jdbcTemplate.query(sessionSql, new Object[]{personId}, new BeanPropertyRowMapper<>(SessionVO.class));

        SessionVO sessionVO = null;
        // deptId 为空，则用主部门登录，否则，根据指定部门登录
        if (StrUtil.isBlank(deptId)) {
            // 获取主部门
            sessionVO = SecurityUtil.getDefaultDeptSession(personDeptList);
            if (null == sessionVO) {
                LOGGER.error("人员ID:{} 账户未分配部门或没设置主部门!", personId);
                throw new TopinfoRuntimeException(ErrorCode.SYTEM_ERROR.getCode(), ErrorCode.SYTEM_ERROR.getMsg());
            }
        } else {
            // 获取指定部门
            sessionVO = SecurityUtil.getDeptSession(personDeptList, deptId);
            if (null == sessionVO) {
                LOGGER.error("人员ID:{} 选择的部门在数据库中不存在!", personId);
                throw new TopinfoRuntimeException(ErrorCode.SYTEM_ERROR.getCode(), ErrorCode.SYTEM_ERROR.getMsg());
            }
        }

        if (TpConstant.ADMIN.PERSONID.equals(personId)) {
            // 超级管理员，无需配置角色
            return sessionVO;
        }

        // 查询人员 部门的角色
        List<String> roleList = jdbcTemplate.queryForList(roleSql, new Object[]{personId, sessionVO.getDeptId()}, String.class);
        // 角色为空
        if (null == roleList || roleList.size() == 0) {
            LOGGER.info("人员ID:{} 账户未分配角色!", personId);
            throw new TopinfoRuntimeException(ErrorCode.SYTEM_LOGIN_NO_ROLE_ERROR.getCode(), ErrorCode.SYTEM_LOGIN_NO_ROLE_ERROR.getMsg());
        }

        String roles = CollUtil.join(roleList, ",");
        sessionVO.setRoleIds(roles);

        return sessionVO;
    }


    /**
     * 刷新 token
     *
     * @param
     * @return java.lang.String
     * @author Ypp
     * @date 2023/10/23 14:44
     */
    @Override
    public String refreshToken(String jwttoken) {

        // 判断当前token是否是无效的
        boolean isLogout = logoutService.isInvalidToken(jwttoken);
        if (isLogout) {
            throw new TopinfoRuntimeException(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }

        // 获取sessionVO
        SessionVO sessionVO = SessionHolder.get();
        if (null == sessionVO) {
            throw new TopinfoRuntimeException(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }

        // 获取认证的配置
        Authentication authentication = properties.getAuthentication();
        // 生成 token
        String token = JwtUtil.createToken(JSONUtil.toJsonStr(sessionVO), authentication.getTokenTimeOut());

        return token;
    }

    /**
     * 交换token
     *
     * @param token   源token
     * @param version 目标token的版本号，版本号： 2.0、2.1、3.0
     * @return java.lang.String  目标版本的token
     * @author Ypp
     * @date 2023/11/1 17:22
     */
    @Override
    public String exchangeToken(String token, String version) {


        switch (version) {
            case "2.0.0":

                // 3.0.0  --交换-> 2.0.0

                // 校验 源token 是否过期
                String sub = JwtUtil.getToken(token);
                SessionVO sessionVO = JSON.parseObject(sub, SessionVO.class);
                // 生成目标 2.0.0 token
                token = JwtTokenUtils.createToken(sessionVO.getTenantId(), sessionVO.getAscnId(), sessionVO.getPersonId(), sessionVO.getDeptId(), sessionVO.getCityCode(), sessionVO.getRoleIds(), "");


                break;
            case "2.1.0":
            case "3.0.0":

                // 2.0.0  --交换-> 3.0.0
                // 校验 源token 是否过期
                String personId = JwtTokenUtils.getPersonId(token);
                String deptId = JwtTokenUtils.getDepts(token);

                SessionVO session = createSession(personId, deptId);

                Authentication authentication = properties.getAuthentication();
                // 生成 token
                token = JwtUtil.createToken(JSONUtil.toJsonStr(session), authentication.getTokenTimeOut());

                // 设置响应头
                HttpServletResponse response = CommonRequestUtil.getHttpServletResponse();
                response.setHeader("token", token);

                break;

            default:

        }

        return token;
    }
}
