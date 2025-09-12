package com.jiuxi.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.bean.LogicalDisk;
import com.jiuxi.common.enums.FileContentTypeEnum;
import com.jiuxi.common.exception.ExceptionUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: FileUtils
 * @Description: 附件工具类 - 基于Hutool-FileUtil进行封装
 * @Author: 杨攀
 * @Date: 2020/5/28 10:52
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 *
 * 该类已过期，请使用：CommonFileUtil
 */
@Deprecated
public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("/yyyy/MM/");

    /**
     * 后缀名黑名单
     */
    private static final String FILE_EXT = ".exe,.bat,.sh,.js,.html,.jsp,.class,.dll,.jspx";

    /**
     * 后缀名白名单
     */
    private static final String FILE_WHITE_EXT = ".gz,.rar,.tgz,.gzip,.zip,.jpg,.jpeg,.png,.gif,.bmp,.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.wmv,.wav,.avi,.flv,.mp4,.mp3";


    public static final String PDF = "pdf";

    /**
     *  支持的视频的格式
     */
    private static final String VIDEO_EXT = ".wmv,.avi,.flv,.mp4";

    /**
     * 用于存放可读可写的目录名称 -- 过期，后续采用 文件服务器存储
     * @author 杨攀
     * @date 2021/12/31 13:55
     * @param null
     * @return
     */
    @Deprecated
    public static List<String> LOGICAL_DISK_NAMES = new ArrayList<>();


    /**
     * 所有的目录 -- 过期，后续采用 文件服务器存储
     * @author 杨攀
     * @date 2021/12/31 13:55
     * @param null
     * @return
     */
    @Deprecated
    public static Map<String, String> LOGICAL_DISK_All = new HashMap<>();

    /**
     * 所有可读可写的目录 -- 过期，后续采用 文件服务器存储
     * @author 杨攀
     * @date 2021/12/31 13:55
     * @param null
     * @return
     */
    @Deprecated
    public static Map<String, String> LOGICAL_DISK_RW = new HashMap<>();

    /**
     * 初始化文件上传目录 -- 过期，后续采用 文件服务器存储
     *
     * @param fileDirVOMap
     * @return void
     * @author 杨攀
     * @date 2021/4/28 11:05
     */
    @Deprecated
    public static void initLogicalDisk(Map<String, LogicalDisk> fileDirVOMap) {

        fileDirVOMap.forEach((k, v) -> {

            // 保存
            LOGICAL_DISK_All.put(k, v.getDir());

            // 读写
            if ("rw".equalsIgnoreCase(v.getStatus())) {
                LOGICAL_DISK_RW.put(k, v.getDir());
                LOGICAL_DISK_NAMES.add(k);
            }
        });
    }

    /**
     * @param base64
     * @param rootDir     上传的根目录
     * @param logicalDisk 上传的逻辑盘的名称
     * @param fileName    这个是存磁盘的名称，可以通过 String fileName = createFileName() + ".jpg"; 生成
     * @return java.lang.String 返回相对路径 /yyyymm/xxxxxxxxxxx.jpg, 失败返回null
     * @description: 文件上传
     * @author 杨攀
     * @date 2020/5/28 11:11
     */
    public static String fileUploadBase64(String base64, String rootDir, String logicalDisk, String fileName) {

        if (StrUtil.isBlank(base64)) {
            return null;
        }

        // 按字节编码
        byte[] encodeImage = Base64.decode(base64);

        // 生成新的文件名称
        // String fileName = createFileName() + ".jpg";

        // 相对路径       /fileDirName/yyyy/mm/xxx.jpg
        String relDir = createRelDir(logicalDisk) + fileName;

        int index = StrUtil.indexOf(relDir, '/', 1);
        String relativePath = relDir.substring(index);

        // 创建上传的路径  rootDir/fileDirName/yyyy/mm/xxx.jpg
        String uploadDir = rootDir + relativePath;

        // 创建目录和文件
        FileUtil.touch(uploadDir);
        FileUtil.writeBytes(encodeImage, uploadDir);

        return relDir;
    }

    /**
     * @param fileItem
     * @param rootDir     上传的根目录
     * @param logicalDisk 上传的逻辑盘的名称
     * @return java.lang.String 返回相对路径 /yyyymm/xxxxxxxxxxx.jpg, 失败返回null
     * @description: 文件上传
     * @author 杨攀
     * @date 2020/5/28 11:11
     */
    public static String fileUpload(FileItem fileItem, String rootDir, String logicalDisk) {

        if (fileItem == null) {
            return null;
        }

        // 文件的原名
        String originalFilename = fileItem.getFieldName();
        // 获取文件扩展名，扩展名不带“.”
        String extName = FileUtil.extName(originalFilename);
        // 生成新的文件名称
        String fileName = createFileName() + "." + extName;

        // 创建上传的路径  /fileDirName/yyyymm/
        String relDir = createRelDir(logicalDisk) + fileName;

        int index = StrUtil.indexOf(relDir, '/', 1);
        String relativePath = relDir.substring(index);

        // 创建上传的路径  rootDir/fileDirName/yyyy/mm/xxx.jpg
        String uploadDir = rootDir + relativePath;

        InputStream is = null;
        BufferedOutputStream out = null;
        try {

            is = fileItem.getInputStream();
            out = FileUtil.getOutputStream(uploadDir);

            IoUtil.copyByNIO(is, out, IoUtil.DEFAULT_BUFFER_SIZE, null);
            return relDir;
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("附件上传失败!");
        } finally {
            IoUtil.close(is);
            IoUtil.close(out);
        }
    }

    /**
     * @param multipartFile
     * @param rootDir       上传的根目录
     * @param logicalDisk   上传的逻辑盘的名称
     * @return java.lang.String 返回相对路径 /yyyymm/xxxxxxxxxxx.jpg, 失败返回null
     * @description: 文件上传
     * @author 杨攀
     * @date 2020/6/3 14:35
     */
    public static String fileUpload(MultipartFile multipartFile, String rootDir, String logicalDisk) {

        if (multipartFile == null) {
            return null;
        }

        // 文件的原名
        String originalFilename = multipartFile.getOriginalFilename();
        // 获取文件扩展名，扩展名不带“.”
        String extName = FileUtil.extName(originalFilename);
        // 生成新的文件名称
        String fileName = createFileName() + "." + extName;
        // 创建上传的路径  /fileDirName/yyyymm/
        String relDir = createRelDir(logicalDisk) + fileName;
        int index = StrUtil.indexOf(relDir, '/', 1);
        String relativePath = relDir.substring(index);

        // 创建上传的路径  rootDir/fileDirName/yyyy/mm/xxx.jpg
        String uploadDir = rootDir + relativePath;

        //  创建目录
        FileUtil.mkParentDirs(uploadDir);

        File file = new File(uploadDir);
        try {
            multipartFile.transferTo(file);
            return relDir;
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("附件上传失败!");
        }
    }

    /**
     * @param is          文件流
     * @param extName     后缀名
     * @param rootDir     上传的根目录
     * @param fileDirName 上传的逻辑盘的名称
     * @return java.lang.String 返回相对路径 /yyyymm/xxxxxxxxxxx.jpg, 失败返回null
     * @description: 文件上传
     * @author 杨攀
     * @date 2020/6/3 14:39
     */
    public static String fileUpload(InputStream is, String extName, String rootDir, String fileDirName) {

        if (is == null) {
            return null;
        }

        // 生成新的文件名称
        String fileName = createFileName() + "." + extName;
        // 创建上传的路径  /fileDirName/yyyymm/
        String relDir = createRelDir(fileDirName) + fileName;
        // 绝对路径
        String uploadDir = rootDir + relDir;

        BufferedOutputStream out = null;
        try {
            out = FileUtil.getOutputStream(uploadDir);
            IoUtil.copyByNIO(is, out, IoUtil.DEFAULT_BUFFER_SIZE, null);
            return relDir;
        } finally {
            IoUtil.close(is);
            IoUtil.close(out);
        }
    }


    /**
     * 返回一个逻辑盘的名称 -- 过期，后续采用 文件服务器存储
     *
     * @return java.lang.String
     * @author pand
     * @date 2021-04-26 19:53
     */
    @Deprecated
    public static String getLogicalDiskName() {
        int ran = RandomUtil.randomInt(LOGICAL_DISK_NAMES.size());
        // 获取KEY
        String key = LOGICAL_DISK_NAMES.get(ran);
        return key;
    }


    /**
     * 相对路径目录  /yyyy/MM/
     *
     * @return java.lang.String
     * @author 杨攀
     * @date 2021/4/28 11:28
     */
    public static String createRelDir() {
        LocalDate date = LocalDate.now();
        String yyyymm = dateFormat.format(date);
        // 获取上传文件的跟路径
        return yyyymm;
    }

    /**
     * @param preDir 逻辑盘名称
     * @return java.lang.String
     * @description: 相对路径目录  /yyyy/MM/
     * @author 杨攀
     * @date 2021/4/28 11:29
     */
    public static String createRelDir(String preDir) {
        LocalDate date = LocalDate.now();
        String yyyymm = dateFormat.format(date);
        // 获取上传文件的跟路径
        if (StrUtil.isBlank(preDir)) {
            return yyyymm;
        } else {
            return "/" + preDir + yyyymm;
        }
    }

    /**
     * @param
     * @return java.lang.String
     * @description: 新目标文件的名称
     * @author 杨攀
     * @date 2020/5/28 11:11
     */
    public static String createFileName() {
        return IdUtil.objectId();
    }

    /**
     * 文件下载
     *
     * @param absolutePath 文件的绝对路径，如：D:/uplaod/2021/03/xxxxxx.jpg
     * @param fileName     下载的文件名称
     * @param response
     * @return void
     * @author 杨攀
     * @date 2021/4/28 14:54
     */
    public static void downloadFile(String absolutePath, String fileName, HttpServletResponse response) throws Exception {
        downloadFile(absolutePath, fileName, response, FileContentTypeEnum.DOWNLOAD);
    }


    /**
     * 文件下载， 并指定下载内容类型，
     *
     * @param absolutePath 文件的绝对路径，如：D:/uplaod/2021/03/xxxxxx.jpg
     * @param fileName     下载的文件名称
     * @param response
     * @param typeEnum     下载文件内容的枚举类 下载：application/octet-stream; pdf： application/pdf;
     * @return void
     * @author 杨攀
     * @date 2021/4/28 15:12
     */
    public static void downloadFile(String absolutePath, String fileName, HttpServletResponse response, FileContentTypeEnum typeEnum) throws Exception {

        //下载的空格变成+， 括号变成乱码---处理文件名包含%20 不转换成空格加后面的代码 .replaceAll("\\+", "%20")
        String downFileName = URLEncoder.encode(fileName, "UTF-8");

        response.setContentType(typeEnum.getContentType());
        response.setCharacterEncoding("utf-8");

        response.setHeader("Content-disposition", typeEnum.getContentDisposition() + downFileName);


        // 数据写会前端
        InputStream inputStream = null;
        OutputStream outputStream = null;
        byte[] buff = new byte[2048];
        try {

            inputStream = FileUtil.getInputStream(absolutePath);
            outputStream = response.getOutputStream();

            int point;
            while (-1 != (point = inputStream.read(buff))) {
                outputStream.write(buff, 0, point);
            }
            outputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException("附件下载失败！");
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
        }

    }

    /**
     * 文件下载， 并指定下载内容类型，
     *
     * @param inputStream 文件的流
     * @param fileName     下载的文件名称
     * @param response
     * @param typeEnum     下载文件内容的枚举类 下载：application/octet-stream; pdf： application/pdf;
     * @return void
     * @author 杨攀
     * @date 2021/4/28 15:12
     */
    public static void downloadFile(InputStream inputStream, String fileName, HttpServletResponse response, FileContentTypeEnum typeEnum) throws Exception {

        //下载的空格变成+， 括号变成乱码---处理文件名包含%20 不转换成空格加后面的代码 .replaceAll("\\+", "%20")
        String downFileName = URLEncoder.encode(fileName, "UTF-8");

        response.setContentType(typeEnum.getContentType());
        response.setCharacterEncoding("utf-8");

        response.setHeader("Content-disposition", typeEnum.getContentDisposition() + downFileName);

        // 数据写会前端
        OutputStream outputStream = null;
        byte[] buff = new byte[2048];
        try {
            outputStream = response.getOutputStream();
            int point;
            while (-1 != (point = inputStream.read(buff))) {
                outputStream.write(buff, 0, point);
            }
            outputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException("附件下载失败！");
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(outputStream);
        }

    }

    /**
     * 获取绝对路径
     *
     * @param filePath 相对路径
     * @return java.lang.String
     * @author 杨攀
     * @date 2021/4/28 14:49
     */
    public static String getAbsolutePath(String filePath) {
        String key = StrUtil.subBetween(filePath, "/", "/");
        if (StrUtil.isNotBlank(key)) {
            int index = StrUtil.indexOf(filePath, '/', 1);
            String relativePath = filePath.substring(index);
            return LOGICAL_DISK_All.get(key) + relativePath;
        }
        return filePath;
    }

    /**
     * 检查扩展名
     *
     * @param ext 传入后缀名， 如： .exe
     * @return boolean
     * @author 杨攀
     * @date 2021/4/28 15:07
     */
    public static void checkExt(String ext) throws IOException {
        if (FILE_WHITE_EXT.indexOf(ext.toLowerCase()) == -1) {
            throw new IOException("附件上传失败,不支持的类型！");
        }
    }

    /**
     * 检查视频扩展名
     *
     * @param ext 传入后缀名， 如： .mp4
     * @return boolean
     * @author 杨攀
     * @date 2021/4/28 15:07
     */
    public static boolean isVideo(String ext) {
        if (VIDEO_EXT.indexOf(ext.toLowerCase()) != -1) {
            return true;
        }
        return false;
    }


    /**
     * 读取流
     * @author 杨攀
     * @date 2022/6/22 10:32
     * @param inStream
     * @return byte[]
     */
    public  byte[] readInputStream(InputStream inStream) throws IOException{

        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = -1;
        while ((len = inStream.read (buffer)) != -1) {
            outSteam.write (buffer, 0, len);
        }
        outSteam.close ();
        inStream.close ();
        return outSteam.toByteArray ();
    }

}
