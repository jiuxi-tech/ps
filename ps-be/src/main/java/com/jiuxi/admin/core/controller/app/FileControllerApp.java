package com.jiuxi.admin.core.controller.app;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.jiuxi.admin.core.bean.entity.TpAttachinfo;
import com.jiuxi.admin.core.bean.vo.TpAttachinfoVO;
import com.jiuxi.admin.core.service.FileService;
import com.jiuxi.admin.core.service.TpAttachinfoService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.TokenUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Description: oss附件接口
 * @ClassName: FileController
 * @Author: pand
 * @Date: 2020-10-13 17:34
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/app/sys/file")
public class FileControllerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileControllerApp.class);


    /**
     * 附件表服务
     */
    @Autowired
    private FileService fileService;

    @Autowired
    private TpAttachinfoService tpAttachinfoService;

    /**
     * 附件上传接口
     *
     * @param files     上传的附件
     * @param referId   业务id
     * @param referType 业务类型
     * @param request
     * @return void
     * @author 杨攀
     * @date 2021/4/26 13:39
     */
    @PostMapping(value = "/upload")
    public JsonResponse upload(@RequestParam(value = "file", required = false) MultipartFile[] files,
                               @RequestParam(required = false) String referId,
                               String jwtpid,
                               String referType,
                               HttpServletRequest request) {
        // 校验token
        this.checkToken(request);
        List<TpAttachinfo> list = null;

        // 判断是否是表单上传，如果为空，则是base64上传
        if (null != files && files.length >= 1) {
            list = fileService.uploadFile(files, jwtpid, referType, referId);
        } else {
            String[] base64s = request.getParameterValues("file");
            if (base64s != null) {
                String[] fileName = request.getParameterValues("filename");
                list = fileService.uploadFile(base64s, fileName, jwtpid, referType, referId);
            }
        }
        if (null != list && list.size() == 1) {
            // 数据返回
            return JsonResponse.buildSuccess(list.get(0));
        } else {
            return JsonResponse.buildSuccess(list);
        }
    }

    /**
     * app附件下载接口，如果是pdf直接预览。需要加参数type=pdf
     *
     * @param request
     * @param response
     * @return void
     * @author pand
     * @date 2021-12-13 16:12
     */
    @RequestMapping(value = "/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        // 下载
        this.downloadFile(request, response);
    }

    /**
     * 文件下载
     *
     * @param request
     * @param response
     * @return void
     * @author 杨攀
     * @date 2021/12/27 15:14
     */
    private void downloadFile(HttpServletRequest request, HttpServletResponse response) {

        // 附件id
        String id = request.getParameter("id");

        if (StrUtil.isBlank(id)) {
            throw new TopinfoRuntimeException(-1, "附件id不能为空！");
        }

        // 校验token
        this.checkToken(request);

        try {
            fileService.download(id, request, response);
        } catch (Exception e) {
            LOGGER.error("下载文件失败！{}", ExceptionUtils.getStackTrace(e));
            this.res(response);
        }
    }


    /**
     * 根据文件相对路径下载文件
     *
     * @param request
     * @param response
     * @return void
     * @author 杨攀
     * @date 2021/12/27 15:14
     */
    @RequestMapping(value = "/download-by-path")
    public void downloadByPath(HttpServletRequest request, HttpServletResponse response) {

        // 相对路径
        String relaPath = request.getParameter("relaPath");
        if (StrUtil.isBlank(relaPath)) {
            throw new TopinfoRuntimeException(-1, "附件相对路径不能为空！");
        }
        // 校验token
        this.checkToken(request);
        // 相对路径
        String downFileName = request.getParameter("downFileName");
        if (StrUtil.isBlank(downFileName)) {
            if (relaPath.indexOf("/") >= 0) {
                downFileName = relaPath.substring(relaPath.lastIndexOf("/"), relaPath.lastIndexOf("."));
            } else {
                downFileName = relaPath;
            }
        }

        try {
            fileService.downloadByPath(relaPath, downFileName, request, response);
        } catch (Exception e) {
            LOGGER.error("下载文件失败！{}", ExceptionUtils.getStackTrace(e));
            this.res(response);
        }
    }


    /**
     * 文书生成查看
     *
     * @param id:
     * @param request:
     * @param response:
     * @return void
     * @author pand
     * @date 2021-04-28 16:33
     */
    @RequestMapping(value = "/preview-pdf")
    public void previewPDF(String id, HttpServletRequest request, HttpServletResponse response) {
        // 校验token
        this.checkToken(request);

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

    /**
     * 根据业务id查询附件列表
     *
     * @return com.jiuxi.common.bean.JsonResponse
     * @author pand
     * @date 2021-01-22 11:30
     */
    @RequestMapping(value = "/refer-id")
    @IgnoreAuthorization
    public JsonResponse getByReferId(String referId, String referType, HttpServletRequest request) {
        // 校验token
        this.checkToken(request);
        List<TpAttachinfoVO> list = tpAttachinfoService.selectByReferIdAndType(referId, referType);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 删除附件
     * <pre>
     * 根据 referId 和 attachId 删除附件：
     *    如果 referId 为空，则只能删除当前登录人自己创建的附件，物理删除
     *    如果 referId 不为空， 则根据 数据权限 判断是否能够删除，如果权限不对，则排查异常 逻辑删除
     * </pre>
     *
     * @return com.jiuxi.common.bean.JsonResponse
     * @author pand
     * @date 2021-05-10 10:08
     */
    @RequestMapping(value = "/delete")
    public JsonResponse delete(String referId, String attachId, String jwtpid, HttpServletRequest request) {

        // 如果 referId 不为空， 则根据 数据权限 判断是否能够删除，如果权限不对，则排查异常
        if (StrUtil.isNotBlank(referId)) {

            // 多个 签名逗号分隔开
            String sk = request.getHeader("sk");

            if (StrUtil.isBlank(sk)) {
                throw new RuntimeException("删除附件时，横向鉴权验证失败！");
            }

            // 修改/删除/查看操作
            String sign = DigestUtil.md5Hex16(referId);
            if (!sign.equals(sk)) {
                throw new RuntimeException("附件删除失败，禁止横向越权操作！");
            }
        }

        int count = tpAttachinfoService.remove(referId, attachId, jwtpid);
        return JsonResponse.buildSuccess(count);
    }

    /**
     * 根据referId删除附件
     *
     * @return com.jiuxi.common.bean.JsonResponse
     * @author pand
     * @date 2021-05-10 10:08
     */
    @RequestMapping(value = "/delete-by-refer")
    public JsonResponse deleteByRefer(String referId, String referType, String jwtpid, HttpServletRequest request) {

        // 如果 referId 不为空， 则根据 数据权限 判断是否能够删除，如果权限不对，则排查异常
        if (StrUtil.isNotBlank(referId)) {

            // 多个 签名逗号分隔开
            String sk = request.getHeader("sk");

            if (StrUtil.isBlank(sk)) {
                throw new RuntimeException("删除附件时，横向鉴权验证失败！");
            }

            // 修改/删除/查看操作
            String sign = DigestUtil.md5Hex16(referId);
            if (!sign.equals(sk)) {
                throw new RuntimeException("附件删除失败，禁止横向越权操作！");
            }
        }

        int count = tpAttachinfoService.deleteByReferIdAndReferType(referId, referType, jwtpid);
        return JsonResponse.buildSuccess(count);
    }

    /**
     * 校验token
     *
     * @param request
     * @return void
     * @author 杨占锐
     * @date 2025/3/26 16:43
     */
    private void checkToken(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (StrUtil.isBlank(token)) {
            token = request.getHeader("Token");
            if (StrUtil.isBlank(token)) {
                throw new TopinfoRuntimeException(-1, "鉴权失败！");
            }
        }
        TokenUtil.checkToken(token);
    }

}
