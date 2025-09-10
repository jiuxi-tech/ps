package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.jiuxi.admin.core.bean.vo.TpParameterConfigVO;
import com.jiuxi.admin.core.service.QrCodeService;
import com.jiuxi.admin.core.service.TpParameterConfigService;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;

/**
 * @Description: 二维码生成实现
 * @ClassName: QrCodeServiceImpl
 * @Author: pand
 * @Date: 2021-08-12 17:48
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
@Service("qrCodeService")
public class QrCodeServiceImpl implements QrCodeService {


    /**
     * 系统参数表
     */
    @Autowired
    private TpParameterConfigService tpParameterConfigService;

    /**
     * app下载地址二维码
     */
    private String APP_DOWNLOAD_QR_PM_KEY = "APP_DOWNLOAD_QR_PM_KEY";

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
    @Override
    public void qrCodeGenerate(int width, int height, String imageType, OutputStream outputStream) {
        TpParameterConfigVO tpParameterConfigVO = tpParameterConfigService.viewByPmKey(APP_DOWNLOAD_QR_PM_KEY);
        if (null == tpParameterConfigVO || StrUtil.isBlank(tpParameterConfigVO.getPmVal())) {
            throw new TopinfoRuntimeException(-1, "请配置app下载地址二维码生成参数！");
        }
        if (tpParameterConfigVO.getEnabled() == 0){
            throw new TopinfoRuntimeException(-1, "app下载地址二维码生成参数未启用！");
        }

        QrCodeUtil.generate(tpParameterConfigVO.getPmVal(), width, height, imageType, outputStream);
    }

}
