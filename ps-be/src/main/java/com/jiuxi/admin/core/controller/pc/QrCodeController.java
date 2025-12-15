package com.jiuxi.admin.core.controller.pc;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Description: 二维码生成控制器
 * @ClassName: QrCodeController
 * @Author: pdd
 * @Date: 2021-08-12 17:47
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
@Controller
@RequestMapping("/platform/qr")
public class QrCodeController {

    @Autowired
    private QrCodeService qrCodeService;

    /**
     * app下载二维码生成
     */
    @RequestMapping("/app-download")
    public void qrcode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String widthStr = request.getParameter("width");
        String heightStr = request.getParameter("height");

        int width = StrUtil.isBlank(widthStr) ? 300 : Integer.valueOf(widthStr);
        int height = StrUtil.isBlank(heightStr) ? 300 : Integer.valueOf(heightStr);

        OutputStream outputStream = response.getOutputStream();

        qrCodeService.qrCodeGenerate(width, height, ImgUtil.IMAGE_TYPE_PNG, outputStream);
    }

}
