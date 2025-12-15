package com.jiuxi.admin.core.controller.pc;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.jiuxi.admin.autoconfig.AdminConfigurationProperties;
import com.jiuxi.admin.core.service.FileService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SmUtils;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 第三方附件接口附件接口
 * @ClassName: FileController
 * @Author: Ypp
 * @Date: 2020-10-13 17:34
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/third/file")
public class FileThirdController {


    private static final Logger LOGGER = LoggerFactory.getLogger(FileThirdController.class);

    /**
     * 附件表服务
     */
    @Autowired
    private FileService fileService;

    @Autowired
    private AdminConfigurationProperties properties;

    /**
     * 获取token
     *
     * @param request
     * @param response
     * @return com.jiuxi.common.bean.JsonResponse
     * @author Ypp
     * @date 2021/12/27 15:27
     */
    @RequestMapping(value = "/token")
    public JsonResponse token(HttpServletRequest request, HttpServletResponse response) {

        String thirdid = request.getParameter("thirdid");

        if (StrUtil.isBlank(thirdid)) {
            throw new TopinfoRuntimeException(-1, "第三方应用thirdid不能为空！");
        }

        String[] thirdids = properties.getThirdids();

        // 判断 thirdid 是否有效
        if (!ArrayUtil.contains(thirdids, thirdid)) {
            throw new TopinfoRuntimeException(-1, "非法的第三方应用thirdid！");
        }

        // 当前时间
        long ctime = CommonDateUtil.currentTimeMillis();
        // 验证字符串
        String verification = ctime + "";
        // 加密传输给前端
        String token = SmUtils.encryptHexSM4(verification);

        return JsonResponse.buildSuccess(token);
    }


    /**
     * 附件下载
     *
     * @param request
     * @param response
     * @return void
     * @author Ypp
     * @date 2021/12/27 15:04
     */
    @RequestMapping(value = "/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {

        String token = request.getParameter("token");

        if (StrUtil.isBlank(token)) {
            throw new TopinfoRuntimeException(-1, "第三方应用token不能为空！");
        }

        try {
            // 加密传输给前端
            String verification = SmUtils.decryptStrSM4(token);

            // 当前时间
            long ctime = CommonDateUtil.currentTimeMillis();
            // 需要校验的实际
            long vtime = Long.parseLong(verification);
            // 转为 秒
            long interval = (ctime - vtime) / 1000;

            // 大于 12 小时
            if (interval > 43200) {
                throw new TopinfoRuntimeException(-1, "第三方应用token过期！");
            }

        }catch (Exception e){
            LOGGER.error("附件下载失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "第三方应用token无效！");
        }

        // 下载
        this.downloadFile(request, response);
    }

    /**
     * 文件下载
     *
     * @param request
     * @param response
     * @return void
     * @author Ypp
     * @date 2021/12/27 15:14
     */
    private void downloadFile(HttpServletRequest request, HttpServletResponse response) {

        // 附件id
        String id = request.getParameter("id");

        if (StrUtil.isBlank(id)) {
            throw new TopinfoRuntimeException(-1, "附件id不能为空！");
        }

        try {
            fileService.download(id, request, response);
        } catch (Exception e) {
            LOGGER.error("下载文件失败！{}", ExceptionUtils.getStackTrace(e));
            this.res(response);
        }
    }


    private void res(HttpServletResponse response) {
        // 重置response
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        JsonResponse jsonResponse = JsonResponse.buildFailure("下载文件失败！");
        try {
            response.getWriter().println(JSON.toJSONString(jsonResponse));
        } catch (IOException e) {
            throw new TopinfoRuntimeException(-1, "下载文件失败!");
        }
    }

}
