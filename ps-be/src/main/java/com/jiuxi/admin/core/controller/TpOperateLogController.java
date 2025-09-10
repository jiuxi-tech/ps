package com.jiuxi.admin.core.controller;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.common.bean.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

import com.jiuxi.admin.core.bean.vo.TpOperateLogVO;
import com.jiuxi.admin.core.bean.query.TpOperateLogQuery;
import com.jiuxi.admin.core.service.TpOperateLogService;

/**
 * @ClassName: TpOperateLogController
 * @Description: 操作日志表控制器
 * @Author: System
 * @Date: 2024-01-17
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@RestController
@RequestMapping("/platform/logger")
public class TpOperateLogController {

    @Autowired
    private TpOperateLogService tpOperateLogService;

    /**
     * 列表查询
     * @param query 查询条件
     * @param request HTTP请求
     * @return 分页结果
     */
    @RequestMapping("/list")
    public JsonResponse list(TpOperateLogQuery query, String jwtpid) {

        // 判断token是否为空
        // if (StrUtil.isBlank(token)) {
        //     return JsonResponse.buildFailure("token不能为空！");
        // }

        // 校验token是否有效
        

        // 校验token是否有效
        // try {
        //     JwtTokenUtils.checkToken(token);
        // } catch (Exception e) {
        //     return JsonResponse.buildFailure("token无效！");
        // }

        IPage<TpOperateLogVO> page = tpOperateLogService.queryPage(query, jwtpid);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 登录日志采集
     * @param ticket 滑动验证票据
     * @param username 用户名
     * @param category 用户类别
     * @param jt token
     * @param request HTTP请求
     * @return 操作结果
     */
    @RequestMapping("/login")
    public JsonResponse login(String ticket, String username, String category, String jt, HttpServletRequest request) {
        try {
            String ip = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            
            tpOperateLogService.login(ticket, username, category, jt, ip, userAgent, null, null, null);
            return JsonResponse.buildSuccess("登录日志记录成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("登录日志记录失败：" + e.getMessage());
        }
    }

    /**
     * 退出日志采集
     * @param username 用户名
     * @param category 用户类别
     * @param jt token
     * @param request HTTP请求
     * @return 操作结果
     */
    @RequestMapping("/logout")
    public JsonResponse logout(String username, String category, String jt, HttpServletRequest request) {
        try {
            String ip = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            
            tpOperateLogService.logout(username, category, jt, ip, userAgent, null, null, null);
            return JsonResponse.buildSuccess("退出日志记录成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("退出日志记录失败：" + e.getMessage());
        }
    }

    /**
     * 操作日志采集
     * @param moduleCode 模块代码
     * @param operterType 操作类型
     * @param operterRid 操作资源ID
     * @param username 用户名
     * @param category 用户类别
     * @param jt token
     * @param request HTTP请求
     * @return 操作结果
     */
    @RequestMapping("/collection")
    public JsonResponse collection(String moduleCode, String operterType, String operterRid, String username, String category, String jt, HttpServletRequest request) {
        try {
            String ip = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            
            // 获取operterMsg和appName参数
            String operterMsg = request.getParameter("operterMsg");
            operterMsg = Optional.ofNullable(operterMsg).orElse("");
            if (!operterMsg.isEmpty()) {
                operterMsg = URLDecoder.decode(operterMsg, Charset.forName("UTF-8"));
            }
            
            String appName = request.getParameter("appName");
            appName = Optional.ofNullable(appName).orElse("");
            if (!appName.isEmpty()) {
                appName = URLDecoder.decode(appName, Charset.forName("UTF-8"));
            }
            
            String extend03 = request.getParameter("extend03");
            extend03 = Optional.ofNullable(extend03).orElse("");
            if (!extend03.isEmpty()) {
                extend03 = URLDecoder.decode(extend03, Charset.forName("UTF-8"));
            }
            
            System.out.println("===collection operterMsg: " + operterMsg + ", appName: " + appName);
            
            tpOperateLogService.collection(moduleCode, operterType, operterRid, username, category, jt, ip, userAgent, operterMsg, appName, extend03);
            return JsonResponse.buildSuccess("操作日志记录成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("操作日志记录失败：" + e.getMessage());
        }
    }

    /**
     * 查看日志详情
     * @param logId 日志ID
     * @return 日志详情
     */
    @RequestMapping("/view")
    public JsonResponse view(String logId) {
        if (StrUtil.isBlank(logId)) {
            return JsonResponse.buildFailure("日志ID不能为空！");
        }
        
        TpOperateLogVO log = tpOperateLogService.view(logId);
        if (log == null) {
            return JsonResponse.buildFailure("日志不存在！");
        }
        
        return JsonResponse.buildSuccess(log);
    }

    /**
     * 新增操作日志
     * @param tpOperateLog 操作日志VO
     * @param jwtpid JWT中的人员ID
     * @return 操作结果
     */
    @RequestMapping("/add")
    public JsonResponse add(TpOperateLogVO tpOperateLog, String jwtpid) {
        try {
            String logId = tpOperateLogService.add(tpOperateLog, jwtpid);
            return JsonResponse.buildSuccess("新增成功", logId);
        } catch (Exception e) {
            return JsonResponse.buildFailure("新增失败：" + e.getMessage());
        }
    }

    /**
     * 导出操作日志到Excel
     * @param query 查询条件
     * @param jwtpid JWT中的人员ID
     * @param response HTTP响应
     */
    @RequestMapping("/export-excel")
    public void exportExcel(@RequestBody TpOperateLogQuery query, String jwtpid, HttpServletResponse response) {
        try {
            tpOperateLogService.exportExcel(query, jwtpid, response);
        } catch (Exception e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }

    /**
     * 获取客户端IP地址
     * @param request HTTP请求
     * @return IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StrUtil.isNotBlank(xForwardedFor) && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (StrUtil.isNotBlank(xRealIp) && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}