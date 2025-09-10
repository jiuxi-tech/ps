package com.jiuxi.admin.core.service;

import java.io.OutputStream;

/**
 * @Description: 二维码生成接口
 * @ClassName: QrCodeService
 * @Author: pand
 * @Date: 2021-08-12 17:47
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public interface QrCodeService {


    /**
     * 生成app下载地址二维码接口
     *
     * @param width        二维码生成的图片宽
     * @param height       二维码生成的图片高
     * @param imageType    二维码生成的图片类型
     * @param outputStream 二维码生成的图片输出流
     * @return void
     * @author pand
     * @date 2021-08-12 17:42
     */
    void qrCodeGenerate(int width, int height, String imageType, OutputStream outputStream);

}
