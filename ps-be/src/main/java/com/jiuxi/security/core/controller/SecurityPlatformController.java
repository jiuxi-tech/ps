package com.jiuxi.security.core.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.jiuxi.common.bean.ErrorCode;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.SessionVO;
import com.jiuxi.common.util.CommonRequestUtil;
import com.jiuxi.common.util.JwtUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import com.jiuxi.security.core.entity.vo.AccountThirdVO;
import com.jiuxi.security.core.entity.vo.AccountVO;
import com.jiuxi.security.core.service.*;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: LoginController
 * @Description: 登录 Controller
 * @Author: 杨攀
 * @Date: 2020/5/25 17:17
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/platform")
public class SecurityPlatformController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityPlatformController.class);

    @Autowired
    @Qualifier(value = "pwdLoginService")
    private AbstractLoginService pwdLoginService;

    @Autowired
    @Qualifier(value = "smsLoginService")
    private AbstractLoginService smsLoginService;

    @Autowired
    @Qualifier(value = "clientLoginService")
    private AbstractLoginService clientLoginService;

    @Autowired
    private TokenExinfoService tokenExinfoService;

    @Autowired(required = false)
    private SmsAccountService smsPersonService;

    @Autowired
    private ClientTokenService clientTokenService;

    @Autowired
    private TopinfoSecurityCommonService topinfoSecurityCommonService;

    @Autowired
    private TopinfoSecurityLogoutService topinfoSecurityLogoutService;

    /**
     * 验证token是否有效，如果有效，将token解析出来，返回解析数据
     *
     * @param token:
     * @return com.jiuxi.common.bean.JsonResponse
     * @author pand
     * @date 2021-04-21 11:29
     */
    @PostMapping(value = "/analysis-token")
    public JsonResponse analysisToken(String token) {

        if (StrUtil.isBlank(token)) {
            return JsonResponse.buildFailure("解析token失败");
        }

        try {
            // 校验 token
            String sub = JwtUtil.getToken(token);
            SessionVO sessionVO = JSON.parseObject(sub, SessionVO.class);
            return JsonResponse.buildSuccess(sessionVO);
        }catch (RuntimeException e){
            throw new TopinfoRuntimeException(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
    }

    /**
     * 验证token是否有效
     *
     * @param token:
     * @return com.jiuxi.common.bean.JsonResponse
     * @author pand
     * @date 2021-04-21 11:29
     */
    @PostMapping(value = "/check-token")
    public JsonResponse checkToken(String token) {
        try {
            // 校验 token
            JwtUtil.checkToken(token);
        }catch (RuntimeException e){
            throw new TopinfoRuntimeException(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        return JsonResponse.buildSuccess("token是合法的");
    }

    /**
     * 客户端模式获取token
     *
     * @param vo:
     * @param response:
     * @return com.jiuxi.common.bean.JsonResponse
     * @author pand
     * @date 2021-04-21 11:29
     */
    @PostMapping(value = "/client-login")
    public JsonResponse clientLogin(AccountVO vo, HttpServletResponse response) {

        AccountVO accountVO = clientLoginService.login(vo);
        if (accountVO != null) {

            // 获取token扩展信息
            String exinfo = tokenExinfoService.exinfo();

            // 生成token
            String token =  topinfoSecurityCommonService.createToken(accountVO);

            // 生成token
            // String token = pwdLoginService.createToken(personVO, exinfo, response);
            return JsonResponse.buildSuccess(token);
        }

        return JsonResponse.buildFailure("获取token失败！");
    }


    /**
     * 合作方获取token
     *
     * @param appKey:    合作方key
     * @param appSecret: 合作方secret
     * @param response:
     * @return com.jiuxi.common.bean.JsonResponse
     * @author pand
     * @date 2021-04-21 11:29
     */
    @PostMapping(value = "/auth/client-token")
    public JsonResponse clientToken(String appKey, String appSecret, HttpServletResponse response) {

        AccountThirdVO accountThirdVO = clientTokenService.login(appKey, appSecret);
        if (accountThirdVO != null) {
            String token =  topinfoSecurityCommonService.createClientToken(appKey, accountThirdVO);
            // String token = JwtTokenUtils.createToken(appKey, JSONUtil.toJsonStr(accountThirdVO), Calendar.HOUR, 2);
            return JsonResponse.buildSuccess(token);
        }

        return JsonResponse.buildFailure("获取token失败！");
    }

    /**
     * 图片验证登录接口
     *
     * @param vo:
     * @param request:
     * @param response:
     * @return com.jiuxi.common.bean.JsonResponse
     * @author pand
     * @date 2021-04-21 11:29
     */
    @PostMapping("/captcha-login")
    public JsonResponse captchaLogin(AccountVO vo, HttpServletRequest request, HttpServletResponse response) {

        Validate.notNull(vo.getUserName(), "账户不能为空");
        Validate.notNull(vo.getUserpwd(), "密码不能为空");
        Validate.notNull(vo.getTicket(), "验证码票据不能为空");

        AccountVO accountVO = pwdLoginService.captchaLogin(vo);
        if (accountVO != null) {

            // 生成token
            String token =  topinfoSecurityCommonService.createToken(accountVO);

            // 生成token
            // String token = pwdLoginService.createToken(accountVO, exinfo, response);
            accountVO.setToken(token);
            return JsonResponse.buildSuccess(accountVO);
        }

        return JsonResponse.buildFailure("登录失败，用户名或密码错误!");
    }


    /**
     * 发送短信验证码
     *
     * @param phone:
     * @return com.jiuxi.common.bean.JsonResponse
     * @author pand
     * @date 2021-04-21 11:30
     */
    @PostMapping("/sms-validate-code")
    public JsonResponse sendSmsValidateCode(String phone) {
        if (smsPersonService.sendSmsValidateCode(phone)) {
            return JsonResponse.buildSuccess();
        } else {
            return JsonResponse.buildFailure("发送失败！");
        }
    }

    /**
     * 手机号/验证码登陆
     *
     * @return com.jiuxi.mvc.bean.JsonResponse
     * @author pand
     * @date 2020-08-26 15:06
     */
    @PostMapping("/sms-login")
    public JsonResponse smsLogin(AccountVO vo, HttpServletResponse response) {
        AccountVO accountVO = smsLoginService.login(vo);
        if (accountVO != null) {
            // 获取token扩展信息
            String exinfo = tokenExinfoService.exinfo();
            // 生成token
            String token =  topinfoSecurityCommonService.createToken(accountVO);

            // 生成token
            // String token = smsLoginService.createToken(personVO, exinfo, response);
            accountVO.setToken(token);
            return JsonResponse.buildSuccess(accountVO);
        }
        return JsonResponse.buildSuccess("发送成功！");
    }


    /**
     * 人员兼职部门的时候，通过该接口选择要登录的部门
     *
     * @param deptId 前台传参数部门id
     * @param jwtpid jwt中解析的人员id
     * @return com.jiuxi.mvc.bean.JsonResponse
     * @description: 选择部门
     * @author 杨攀
     * @date 2020/7/21 14:10
     */
    @RequestMapping("/select-dept")
    public JsonResponse selectDept(String deptId, String jwtpid) {
        AccountVO accountVO = topinfoSecurityCommonService.selectDept(deptId, jwtpid);
        return JsonResponse.buildSuccess(accountVO);
    }


    /**
     * 刷新 token, 前端定时每 60分钟，刷新一次token
     *
     * @param jwttoken 自动注入
     * @return com.jiuxi.common.bean.JsonResponse
     * @author 杨攀
     * @date 2023/10/23 14:28
     */
    @RequestMapping(value = "/refresh-token")
    public JsonResponse refreshToken(String jwttoken) {

        // 没有 token，则放行
        if(StrUtil.isBlank(jwttoken)){
            LOGGER.error("刷新token失败，token:{}", jwttoken);
            return JsonResponse.buildFailure("刷新token失败");
        }

        // 刷新 token
        String token = topinfoSecurityCommonService.refreshToken(jwttoken);

        return JsonResponse.buildSuccess(token);
    }


    /**
     * 交换token
     * @author 杨攀
     * @date 2023/11/1 17:19
     * @param version 目标token的版本号，版本号： 2.0、2.1、3.0
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/exchange-token")
    public JsonResponse exchangeToken(String version) {

        // 获取头部的token信息
        String token = CommonRequestUtil.getHeaderToken();

        // 生成 目标版本的token
        String targetToken = topinfoSecurityCommonService.exchangeToken(token, version);
        return JsonResponse.buildSuccess(targetToken);
    }

    /**
     * 退出登录
     *
     * @param token
     * @return com.jiuxi.common.bean.JsonResponse
     * @author 杨占锐
     * @date 2024/10/8 13:26
     */
    @RequestMapping("/logout")
    public JsonResponse logout(@RequestParam("token") String token) {

        topinfoSecurityLogoutService.logout(token);

        return JsonResponse.buildSuccess();
    }

    /**
     * 将token失效
     *
     * @param token
     * @return com.jiuxi.common.bean.JsonResponse
     * @author 杨占锐
     * @date 2024/10/8 13:26
     */
    @RequestMapping("/invalidate-token")
    @Deprecated // 刷新token后无需将原token失效，退出登录时将token失效即可
    public JsonResponse invalidateToken(@RequestParam("token") String token) {

        return JsonResponse.buildSuccess();
    }
}
