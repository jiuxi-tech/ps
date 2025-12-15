package com.jiuxi.admin.core.service.api;

import cn.hutool.core.bean.BeanUtil;
import com.jiuxi.admin.core.bean.entity.TpAttachinfo;
import com.jiuxi.admin.core.service.FileService;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
// import com.jiuxi.plugin.api.bean.dto.TpAttachinfoDTO;
// import com.jiuxi.plugin.api.interfaces.FileServiceApi; // Commented out - interface not available
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName: FileServiceApiImpl
 * @Description:
 * @Author jiuxx
 * @Date 2024/7/5 14:35
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service
public class FileServiceApiImpl {

    @Autowired
    private FileService fileService;

    /**
     * 上传文件 - 普通表单
     *
     * @param files
     * @param creator
     * @param referType
     * @param referId
     * @return java.util.List<com.jiuxi.plugin.api.bean.dto.TpAttachinfoDTO>
     * @author jiuxx
     * @date 2024/7/5 14:37
     */
    public List<Object> uploadFile(MultipartFile[] files, String creator, String referType, String referId) throws TopinfoRuntimeException {
        List<TpAttachinfo> tpAttachinfos = fileService.uploadFile(files, creator, referType, referId);
        // return BeanUtil.copyToList(tpAttachinfos, TpAttachinfoDTO.class); // Commented out - TpAttachinfoDTO not available
        return null;
    }

    /**
     * 上传文件 - base64
     *
     * @param base64s
     * @param base64sFileName
     * @param creator
     * @param referType
     * @param referId
     * @return java.util.List<com.jiuxi.plugin.api.bean.dto.TpAttachinfoDTO>
     * @author jiuxx
     * @date 2024/7/5 14:37
     */
    public List<Object> uploadFile(String[] base64s, String[] base64sFileName, String creator, String referType, String referId) throws TopinfoRuntimeException {
        List<TpAttachinfo> tpAttachinfos = fileService.uploadFile(base64s, base64sFileName, creator, referType, referId);
        // return BeanUtil.copyToList(tpAttachinfos, TpAttachinfoDTO.class); // Commented out - TpAttachinfoDTO not available
        return null;
    }

    /**
     * 附件下载
     *
     * @param id
     * @param request
     * @param response
     * @return void
     * @author jiuxx
     * @date 2024/7/5 14:37
     */
    public void download(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        fileService.download(id, request, response);
    }

    /**
     * 根据路径下载附件
     *
     * @param filePath
     * @param downFileName
     * @param request
     * @param response
     * @return void
     * @author jiuxx
     * @date 2024/7/5 14:37
     */
    public void downloadByPath(String filePath, String downFileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        fileService.downloadByPath(filePath, downFileName, request, response);
    }
}
