package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.jiuxi.admin.core.bean.entity.TpAttachinfo;
import com.jiuxi.admin.core.service.FileExpService;
import com.jiuxi.admin.core.service.FileService;
import com.jiuxi.admin.core.service.TpAttachinfoService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.interfaces.VideoService;
import com.jiuxi.common.util.CommonFileUtil;
import com.jiuxi.common.util.FileUtils;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 图片上传业务逻辑
 * @ClassName: FileServiceImpl
 * @Author: pdd
 * @Date: 2020-10-15 10:51
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 附件表服务
     */
    @Autowired
    private TpAttachinfoService tpAttachInfoService;

    /**
     * 文件服务器扩展实现
     */
    @Autowired
    private FileExpService fileExpService;

    /**
     * 视频服务
     */
    @Autowired(required = false)
    private VideoService videoService;

    /**
     * 上传文件 - 普通表单
     *
     * @param files:     附件
     * @param creator:   创建人
     * @param referType: 图片类型
     * @param referId:   业务id
     * @return boolean
     * @author Ypp
     * @date 2021/4/26 13:47
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public List<TpAttachinfo> uploadFile(MultipartFile[] files, String creator, String referType, String referId) {

        try {
            List<TpAttachinfo> list = new ArrayList<>();

            for (MultipartFile file : files) {
                // 附件名
                String fileName = file.getOriginalFilename();
                // 获取后缀名 .txt
                String ext = fileName.substring(fileName.lastIndexOf("."));
                // 检查后缀名
                CommonFileUtil.checkWhiteExt(ext);

                // 附件大小
                long size = file.getSize();
                // 上传文件
                String relDir = fileExpService.uploadFile(file);

                // 初始化数据库数据
                TpAttachinfo model = this.initModel(fileName, relDir, Double.valueOf(size), creator, referType, referId);
                list.add(model);

                // 如果是视频文件上传，则需要截取封面
                if(FileUtils.isVideo(ext)){
                    // 引入视频服务时才截取封面
                    if (videoService != null){
                        // 获取封面 图片流
                        InputStream videoCover = videoService.getVideoCover(file);
                        // 上传图片， 在网络模式下videoCover.available() 禁用
                        String videoCoverDir =  fileExpService.uploadFile(videoCover, videoCover.available(), ext);
                        model.setSavePathBak1(videoCoverDir);
                    } else {
                        LOGGER.warn("视频服务topinfo-platform-video-starter未依赖，不执行图片封面截取！");
                    }
                }

                // 数据入库
                this.tpAttachInfoService.save(model);
            }
            return list;
        } catch (Exception e) {

            LOGGER.error("附件上传失败! referId: {}, 错误:{}", referId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-101, "附件上传失败!");
        }
    }


    /**
     * 上传文件 - base64
     *
     * @param base64s
     * @param base64sFileName
     * @param creator:   创建人
     * @param referType: 图片类型
     * @param referId:   业务id
     * @return boolean
     * @author Ypp
     * @date 2021/4/26 13:53
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public List<TpAttachinfo> uploadFile(String[] base64s, String[] base64sFileName,String creator, String referType, String referId) {

        try {

            List<TpAttachinfo> list = new ArrayList<>();

            for (int i = 0; i < base64s.length; i++) {

                String item = base64s[i];

                // 如果格式是：data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAABIUAA......，需要去掉,以前的数据
                String base64;
                int index = item.indexOf(";base64,");
                if (index != -1) {
                    index = index + 8;
                    base64 = item.substring(index);
                } else {
                    base64 = item;
                }

                // 按字节编码
                byte[] encodeImage = Base64.decode(base64);

                // 创建文件名
                String fileName = "";
                String fileExtName = ".jpg";
                if(null != base64sFileName && base64sFileName.length >= 1){
                    fileName = base64sFileName[i];
                    int dot = fileName.lastIndexOf(StrUtil.DOT);
                    fileExtName = fileName.substring(dot);
                }else{
                    fileName = FileUtils.createFileName() + fileExtName;
                }

                InputStream inputStream = new ByteArrayInputStream(encodeImage);
                // 上传文件
                String relDir = fileExpService.uploadFile(inputStream, encodeImage.length, fileExtName);
                // 初始化数据库数据
                TpAttachinfo model = this.initModel(fileName, relDir, Double.valueOf(encodeImage.length), creator, referType, referId);
                list.add(model);
                // 数据入库
                this.tpAttachInfoService.save(model);
            }
            return list;
        } catch (Exception e) {
            LOGGER.error("base64图片上传失败! referId: {}, 错误:{}", referId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-101, "base64图片上传失败!");
        }
    }

    public TpAttachinfo initModel(String filename, String savePath, Double size, String creator, String referType, String referId) {
        TpAttachinfo model = new TpAttachinfo();
        model.setAttachId(SnowflakeIdUtil.nextIdStr());
        model.setSavePath(savePath);
        model.setAttachSize(size);
        model.setAttachName(filename);
        model.setReferType(Optional.ofNullable(referType).orElse(""));
        model.setCreator(creator);
        model.setReferId(Optional.ofNullable(referId).orElse(""));
        return model;
    }

    @Override
    public void download(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {

        TpAttachinfo model = tpAttachInfoService.view(id);
        if (model != null && model.getSavePath() != null) {
            // 下载文件名称
            String downFileName = model.getAttachName();
            // 下载文件路径
            String filePath = model.getSavePath();
            // 下载
            this.downloadByPath(filePath, downFileName, request, response);
        } else {
            LOGGER.error("id: {} 附件不存在！", id);
            throw new TopinfoRuntimeException(-210, "该附件不存在！");
        }
    }

    @Override
    public void downloadByPath(String filePath, String downFileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 下载文件
        fileExpService.downloadFile(filePath, downFileName, request, response);
    }
}
