package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.entity.TpAttachinfo;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 附件接口
 * @ClassName: FileService
 * @Author: pand
 * @Date: 2020-10-15 10:51
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */

public interface FileService {


    /**
     * 上传文件 - 普通表单
     *
     * @param files
     * @param creator
     * @param referType
     * @param referId
     * @return boolean
     * @author 杨攀
     * @date 2021/4/26 13:53
     */
    List<TpAttachinfo> uploadFile(MultipartFile[] files, String creator, String referType, String referId) throws TopinfoRuntimeException;


    /**
     * 上传文件 - base64
     *
     * @param base64s
     * @param base64sFileName
     * @param creator
     * @param referType
     * @param referId
     * @return boolean
     * @author 杨攀
     * @date 2021/4/26 13:53
     */
    List<TpAttachinfo> uploadFile(String[] base64s, String[] base64sFileName,String creator, String referType, String referId) throws TopinfoRuntimeException;


    /**
     * 附件下载
     * @author 杨攀
     * @date 2023/2/15 13:20
     * @param id 附件ID
     * @param request
     * @param response
     * @return void
     */
    void download(String id, HttpServletRequest request, HttpServletResponse response) throws Exception;


    /**
     * 根据路径下载附件
     *
     * @param filePath     附件路径
     * @param downFileName 附件名称
     * @param request
     * @param response
     * @throws Exception
     */
    void downloadByPath(String filePath, String downFileName, HttpServletRequest request, HttpServletResponse response) throws Exception;


}
