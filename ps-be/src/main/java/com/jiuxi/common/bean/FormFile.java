package com.jiuxi.common.bean;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @ClassName: FormFile
 * @Description: 上传的文件对象
 * @Author: 杨攀
 * @Date: 2022/6/22 9:47
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class FormFile {


    /* 上传文件的数据 */
    @Deprecated
    private byte[]      data;

    /** 文件 */
    private File file;

    /** 文件流 */
    private InputStream inputStream;

    /** 文件名称 */
    private String  fileName;

    /** 文件请求参数名称（表单的名称） */
    private String  parameterName;

    /** 内容类型 */
    private String  contentType = "application/octet-stream";

    /**
     * 文件上传对象
     * @author 杨攀
     * @date 2022/6/22 12:23
     * @param file
     * @param parameterName 文件请求参数名称（表单的名称）
     * @return
     */
    public FormFile(File file, String parameterName) throws IOException {
        this.file = file;
        this.fileName = file.getName ();
        this.parameterName = parameterName;
    }


    /**
     * 文件上传对象, 大文件上传时，这个性能不是太好
     * @author 杨攀
     * @date 2022/6/22 12:19
     * @param inputStream
     * @param fileName  文件名称， qq.exe
     * @param parameterName 文件请求参数名称（表单的名称）
     * @return
     */
    public FormFile(InputStream inputStream, String fileName, String parameterName) throws IOException {
        this.inputStream = inputStream;
        this.fileName = fileName;
        this.parameterName = parameterName;
    }

    /**
     * 文件上传对象
     * @author 杨攀
     * @date 2022/6/22 12:22
     * @param multipartFile
     * @param parameterName 文件请求参数名称（表单的名称）
     * @return
     */
    public FormFile(MultipartFile multipartFile, String parameterName) throws IOException {
        this.fileName = multipartFile.getOriginalFilename();
        this.parameterName = parameterName;
        this.inputStream = multipartFile.getInputStream();
    }

    /**
     * 文件上传对象
     * @author 杨攀
     * @date 2022/6/22 12:38
     * @param data 文件 byte 数组
     * @param fileName  文件名称， qq.exe
     * @param parameterName 文件请求参数名称（表单的名称）
     * @return
     */
    /*public FormFile(byte[] data, String fileName, String parameterName) {
        this.data = data;
        this.fileName = fileName;
        this.parameterName = parameterName;
    }*/


    public File getFile() {
        return file;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getContentType() {
        return contentType;
    }
}
