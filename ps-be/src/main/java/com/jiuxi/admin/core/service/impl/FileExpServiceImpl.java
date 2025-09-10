package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.service.FileExpService;
// import com.jiuxi.jdfs.common.service.JdfsClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: 附件上传的扩展接口，开发人员可以自己定义上传调用的真实方法，如当使用阿里云oss是，可以指定key
 * @ClassName: FileExpServiceImpl
 * @Author: pand
 * @Date: 2022-03-17 09:43
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class FileExpServiceImpl implements FileExpService {

    /**
     * 文件服务器客户端
     */
    // @Autowired
    // private JdfsClientService jdfsClientService;

    /**
     * <pre>
     * 如果重写，则重写uploadFile方法示例：
     *     // 获取文件输入流
     *     InputStream inputStream = file.getInputStream();
     *     // 获取扩展名
     *     String fileExtName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
     *
     *     MetaData metaData = new MetaData();
     *     // 定义上传的key
     *     String pre_key = SnowflakeIdUtil.nextIdStr();
     *     metaData.setKey("/cxba/" + pre_key + fileExtName);
     *
     *     jdfsClientService.uploadFile(inputStream, file.getSize(), fileExtName, metaData);
     * </pre>
     *
     * 上传文件
     * @author 杨攀
     * @date 2022/3/28 13:23
     * @param file
     * @return java.lang.String
     */
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        try {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = com.jiuxi.common.util.SnowflakeIdUtil.nextIdStr() + fileExtName;
            
            // 创建上传目录
            String rootDir = System.getProperty("user.dir") + "/uploads";
            java.io.File uploadDir = new java.io.File(rootDir);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 保存文件
            String relativePath = "/" + fileName;
            java.io.File targetFile = new java.io.File(rootDir + relativePath);
            file.transferTo(targetFile);
            
            return relativePath;
        } catch (Exception e) {
            throw new IOException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     *
     * <pre>
     * 如果重写，则重写uploadFile方法示例：
     *
     *    MetaData metaData = new MetaData();
     *    String pre_key = SnowflakeIdUtil.nextIdStr();
     *    // 定义上传的key
     *    metaData.setKey("/cxba/" + pre_key + fileExtName);
     *    return jdfsClientService.uploadFile(inputStream, fileSize, fileExtName);
     *
     * </pre>
     *
     * 上传文件
     * @author 杨攀
     * @date 2022/3/28 13:28
     * @param inputStream
     * @param fileSize 文件大小
     * @param fileExtName 扩展名，如：.jpg
     * @return java.lang.String
     */
    @Override
    public String uploadFile(InputStream inputStream, long fileSize, String fileExtName) {
        try {
            // 生成唯一文件名
            String fileName = com.jiuxi.common.util.SnowflakeIdUtil.nextIdStr() + fileExtName;
            
            // 创建上传目录
            String rootDir = System.getProperty("user.dir") + "/uploads";
            java.io.File uploadDir = new java.io.File(rootDir);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 保存文件
            String relativePath = "/" + fileName;
            java.io.File targetFile = new java.io.File(rootDir + relativePath);
            
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(targetFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            
            return relativePath;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * 下载文件
     * @author 杨攀
     * @date 2022/3/28 13:30
     * @param fileid  文件服务器fileid，如：/D1/02/89/xxxxxxxxxxxxxxx.txt
     * @param filename 下载后的文件名称
     * @return java.io.InputStream
     */
    @Override
    public InputStream getInputStream(String fileid, String filename) {
        try {
            String rootDir = System.getProperty("user.dir") + "/uploads";
            String filePath = rootDir + fileid;
            java.io.File file = new java.io.File(filePath);
            
            if (file.exists() && file.isFile()) {
                return new java.io.FileInputStream(file);
            } else {
                throw new RuntimeException("文件不存在: " + filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("获取文件流失败: " + e.getMessage(), e);
        }
    }

    /**
     * 下载文件
     * @author 杨攀
     * @date 2022/3/28 13:37
     * @param fileid 文件服务器fileid，如：/D1/02/89/xxxxxxxxxxxxxxx.txt
     * @param filename 下载后的文件名称
     * @param request
     * @param response
     * @return void
     */
    @Override
    public void downloadFile(String fileid, String filename, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 使用CommonFileUtil进行文件下载
            // 假设文件存储在本地，fileid就是相对路径
            String rootDir = System.getProperty("user.dir") + "/uploads"; // 默认上传目录
            com.jiuxi.common.util.CommonFileUtil.downloadFile(filename, rootDir, fileid, response);
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败: " + e.getMessage(), e);
        }
    }
}
