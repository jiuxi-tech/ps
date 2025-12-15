package com.jiuxi.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @ClassName: CommonFileUtil
 * @Description: 文件工具类
 * @Author: Ypp
 * @Date: 2023/10/13 9:58
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class CommonFileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonFileUtil.class);

    /**
     * 类Unix路径分隔符
     */
    public static final char UNIX_SEPARATOR = CharUtil.SLASH;

    /**
     * Windows路径分隔符
     */
    public static final char WINDOWS_SEPARATOR = CharUtil.BACKSLASH;

    /**
     * 后缀名白名单
     */
    private static final String FILE_WHITE_EXT = ".gz,.rar,.tgz,.gzip,.zip,.jpg,.jpeg,.png,.gif,.bmp,.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.wmv,.wav,.avi,.flv,.mp4,.mp3,.mov";


    /**
     * 检查扩展名是否在 白名单中，如果不在，则抛出异常。
     *
     * @param ext 传入后缀名， 如： .exe
     * @return boolean
     * @author Ypp
     * @date 2021/4/28 15:07
     */
    public static void checkWhiteExt(String ext) throws IOException {
        if (FILE_WHITE_EXT.indexOf(ext.toLowerCase()) == -1) {
            throw new IOException("附件上传失败,不支持该类型的文件上传！");
        }
    }

    /**
     * 获得文件的扩展名（后缀名），扩展名带“.”
     * @author Ypp
     * @date 2023/10/13 10:26
     * @param fileName 文件名
     * @return java.lang.String  扩展名带“.”，如： .exe
     */
    public static String getExtName(String fileName) {

        if (fileName == null) {
            return null;
        }

        int index = fileName.lastIndexOf(StrUtil.DOT);
        if (index == -1) {
            return StrUtil.EMPTY;
        } else {
            String ext = fileName.substring(index);
            // 扩展名中不能包含路径相关的符号
            return StrUtil.containsAny(ext, UNIX_SEPARATOR, WINDOWS_SEPARATOR) ? StrUtil.EMPTY : ext;
        }
    }

    /**
     * 随机创建文件名称
     * @author Ypp
     * @date 2023/10/13 10:30
     * @param extName 扩展名带“.”，如： .exe
     * @return java.lang.String
     */
    public static String createRandomFileName(String extName) {
        return IdUtil.fastSimpleUUID() + extName;
    }


    /**
     * 创建所给文件或目录的父目录，如果存在直接返回此文件夹
     * @author Ypp
     * @date 2023/10/13 10:45
     * @param path 文件夹路径，使用POSIX格式，无论哪个平台，如：D:/upload/yyyymm/xxxxxxxxxxx.jpg
     * @return java.io.File 创建的目录
     */
    public static File mkParentDirs(String path) {

        if (path == null) {
            return null;
        }

        File file =  new File(path);

        // 获取父目录
        File parentDir = file.getParentFile();
        if (false == parentDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            parentDir.mkdirs();
        }
        return parentDir;
    }


    /**
     * 文件上传
     * @author Ypp
     * @date 2023/10/13 10:22
     * @param multipartFile
     * @param rootDir  上传的根目录，如：D:/upload
     * @param relativeDir 保存的相对路径，如：/yyyymm/
     * @return java.lang.String 返回相对路径 /yyyymm/xxxxxxxxxxx.jpg, 失败返回null
     */
    public static String fileUpload(MultipartFile multipartFile, String rootDir, String relativeDir) {

        if (multipartFile == null) {
            return null;
        }

        // 文件的原名
        String originalFilename = multipartFile.getOriginalFilename();
        // 获取文件扩展名，扩展名带“.”
        String extName = getExtName(originalFilename);
        // 生成新的文件名称
        String fileName = createRandomFileName(extName);

        // 相对路径
        String relativePath = StrUtil.concat(true, relativeDir, fileName);
        // 绝对路径
        String filePath = StrUtil.concat(true, rootDir, relativePath);

        // 创建 父目录
        mkParentDirs(filePath);

        File file = new File(filePath);

        try {
            // 写入文件
            multipartFile.transferTo(file);
            return relativePath;
        } catch (IOException e) {
            LOGGER.error("附件上传失败!", e);
            throw new RuntimeException("附件上传失败!");
        }
    }

    /**
     * 文件上传
     * @author Ypp
     * @date 2023/10/13 14:47
     * @param base64
     * @param extName 图片扩展名，带“.” ， 如:  .jpg
     * @param rootDir  上传的根目录，如：D:/upload
     * @param relativeDir 保存的相对路径，如：/yyyymm/
     * @return java.lang.String
     */
    public static String fileUpload(String base64, String extName, String rootDir, String relativeDir) {

        if (StrUtil.isBlank(base64)) {
            return null;
        }

        // 按字节编码
        byte[] encodeImage = Base64.decode(base64);

        // 生成新的文件名称
        String fileName = createRandomFileName(extName);

        // 相对路径
        String relativePath = StrUtil.concat(true, relativeDir, fileName);
        // 绝对路径
        String filePath = StrUtil.concat(true, rootDir, relativePath);

        FileOutputStream out = null;
        try {

            // 创建 父目录
            mkParentDirs(filePath);

            File file = new File(filePath);

            out = new FileOutputStream(file);
            out.write(encodeImage, 0, encodeImage.length);
            out.flush();

            return relativePath;

        }catch(IOException e){
            LOGGER.error("附件上传失败!", e);
            throw new RuntimeException("附件上传失败!");
        } finally {
            IoUtil.close(out);
        }
    }

    /**
     * 文件上传
     * @author Ypp
     * @date 2023/10/13 15:14
     * @param inputStream
     * @param extName 图片扩展名，带“.” ， 如:  .jpg
     * @param rootDir  上传的根目录，如：D:/upload
     * @param relativeDir 保存的相对路径，如：/yyyymm/
     * @return java.lang.String
     */
    public static String fileUpload(InputStream inputStream, String extName, String rootDir, String relativeDir) {

        if (inputStream == null) {
            return null;
        }

        // 生成新的文件名称
        String fileName = createRandomFileName(extName);

        // 相对路径
        String relativePath = StrUtil.concat(true, relativeDir, fileName);
        // 绝对路径
        String filePath = StrUtil.concat(true, rootDir, relativePath);

        FileOutputStream out = null;
        try {

            // 创建 父目录
            mkParentDirs(filePath);

            File file = new File(filePath);
            out = new FileOutputStream(file);

            IoUtil.copyByNIO(inputStream, out, IoUtil.DEFAULT_BUFFER_SIZE, null);
            out.flush();

            return relativePath;

        }catch(IOException e){
            LOGGER.error("附件上传失败!", e);
            throw new RuntimeException("附件上传失败!");
        } finally {
            IoUtil.close(out);
            IoUtil.close(inputStream);
        }

    }

    /**
     * 下载文件
     * @author Ypp
     * @date 2023/10/13 15:19
     * @param fileName 下载后的文件名称，如：QQ.exe
     * @param rootDir  上传的根目录，如：D:/upload
     * @param relativePath 保存的相对路径，如：/yyyymm/xxxxxxxxxxx.jpg
     * @param response
     * @return void
     */
    public static void downloadFile(String fileName, String rootDir, String relativePath, HttpServletResponse response) {

        // 数据写会前端
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // 设置文件类型
            response.setContentType ("applicatoin/octet-stream");
            // 设置文件名
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

            // 绝对路径
            String filePath = StrUtil.concat(true, rootDir, relativePath);

            inputStream = FileUtil.getInputStream(filePath);
            outputStream = response.getOutputStream();

            byte[] buffer = new byte[256 * 1024];
            int point;
            while (-1 != (point = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, point);
            }
            outputStream.flush();

        } catch (IOException e) {
            LOGGER.error("附件下载失败！失败原因:{}", e);
            throw new RuntimeException("附件下载失败！");
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
        }
    }

    /**
     * 下载文件
     * @author Ypp
     * @date 2023/10/13 15:19
     * @param fileName 下载后的文件名称，如：QQ.exe
     * @param inputStream
     * @param response
     * @return void
     */
    public static void downloadFile(String fileName, InputStream inputStream, HttpServletResponse response) {

        // 数据写会前端
        OutputStream outputStream = null;
        try {
            // 设置文件类型
            response.setContentType ("applicatoin/octet-stream");
            // 设置文件名
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

            outputStream = response.getOutputStream();

            byte[] buffer = new byte[256 * 1024];
            int point;
            while (-1 != (point = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, point);
            }
            outputStream.flush();

        } catch (IOException e) {
            LOGGER.error("附件下载失败！失败原因:{}", e);
            throw new RuntimeException("附件下载失败！");
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
        }
    }


    /**
     * 预览文件
     * @author Ypp
     * @date 2023/10/13 15:19
     * @param fileName 下载后的文件名称，如：QQ.exe
     * @param rootDir  上传的根目录，如：D:/upload
     * @param relativePath 保存的相对路径，如：/yyyymm/xxxxxxxxxxx.jpg
     * @param response
     * @return void
     */
    public static void previewFile(String fileName, String rootDir, String relativePath, HttpServletResponse response) {

        // 数据写会前端
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {

            // 设置文件类型
            String mimeType = FileUtil.getMimeType(fileName);
            if(null == mimeType){
                // 设置文件类型
                response.setContentType ("applicatoin/octet-stream");
                // 设置文件名
                response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
            }else {
                // 设置文件类型
                response.setContentType (mimeType);
                // 设置文件名
                response.setHeader("Content-Disposition", "inline; filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8"));
            }

            // 绝对路径
            String filePath = StrUtil.concat(true, rootDir, relativePath);

            inputStream = FileUtil.getInputStream(filePath);
            outputStream = response.getOutputStream();

            byte[] buffer = new byte[256 * 1024];
            int point;
            while (-1 != (point = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, point);
            }
            outputStream.flush();

        } catch (IOException e) {
            LOGGER.error("附件预览失败！失败原因:{}", e);
            throw new RuntimeException("附件预览失败！");
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
        }
    }

    /**
     * 预览文件
     * @author Ypp
     * @date 2023/10/13 15:19
     * @param fileName 下载后的文件名称，如：QQ.exe
     * @param inputStream
     * @param response
     * @return void
     */
    public static void previewFile(String fileName, InputStream inputStream, HttpServletResponse response) {

        // 数据写会前端
        OutputStream outputStream = null;
        try {

            // 设置文件类型
            String mimeType = FileUtil.getMimeType(fileName);
            if(null == mimeType){
                // 设置文件类型
                response.setContentType ("applicatoin/octet-stream");
                // 设置文件名
                response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
            }else {
                // 设置文件类型
                response.setContentType (mimeType);
                // 设置文件名
                response.setHeader("Content-Disposition", "inline; filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8"));
            }

            outputStream = response.getOutputStream();

            byte[] buffer = new byte[256 * 1024];
            int point;
            while (-1 != (point = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, point);
            }
            outputStream.flush();

        } catch (IOException e) {
            LOGGER.error("附件预览失败！失败原因:{}", e);
            throw new RuntimeException("附件预览失败！");
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
        }
    }
}
