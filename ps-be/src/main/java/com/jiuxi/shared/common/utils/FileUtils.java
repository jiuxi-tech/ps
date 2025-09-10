package com.jiuxi.shared.common.utils;

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
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * 文件工具类
 * 提供文件上传、下载、预览等功能
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public final class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Unix路径分隔符
     */
    public static final char UNIX_SEPARATOR = CharUtil.SLASH;

    /**
     * Windows路径分隔符
     */
    public static final char WINDOWS_SEPARATOR = CharUtil.BACKSLASH;

    /**
     * 默认缓冲区大小
     */
    private static final int DEFAULT_BUFFER_SIZE = 256 * 1024;

    /**
     * 允许的文件扩展名白名单
     */
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
        ".gz", ".rar", ".tgz", ".gzip", ".zip", 
        ".jpg", ".jpeg", ".png", ".gif", ".bmp", 
        ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", 
        ".txt", ".wmv", ".wav", ".avi", ".flv", ".mp4", ".mp3", ".mov"
    ));

    /**
     * 检查文件扩展名是否在白名单中
     * 
     * @param extension 文件扩展名（带点，如：.jpg）
     * @throws IOException 如果扩展名不在白名单中
     */
    public static void validateFileExtension(String extension) throws IOException {
        if (StrUtil.isBlank(extension)) {
            throw new IOException("文件扩展名不能为空");
        }
        
        String ext = extension.toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IOException("不支持的文件类型：" + extension);
        }
    }

    /**
     * 获取文件扩展名
     * 
     * @param fileName 文件名
     * @return 扩展名（带点），如果没有扩展名则返回空字符串
     */
    public static String getFileExtension(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return StrUtil.EMPTY;
        }

        int index = fileName.lastIndexOf(StrUtil.DOT);
        if (index == -1) {
            return StrUtil.EMPTY;
        }

        String ext = fileName.substring(index);
        // 扩展名中不能包含路径相关的符号
        return StrUtil.containsAny(ext, UNIX_SEPARATOR, WINDOWS_SEPARATOR) ? StrUtil.EMPTY : ext;
    }

    /**
     * 生成随机文件名
     * 
     * @param extension 文件扩展名（带点）
     * @return 随机文件名
     */
    public static String generateRandomFileName(String extension) {
        if (StrUtil.isBlank(extension)) {
            return IdUtil.fastSimpleUUID();
        }
        return IdUtil.fastSimpleUUID() + extension;
    }

    /**
     * 创建文件的父目录
     * 
     * @param filePath 文件路径
     * @return 创建的父目录
     */
    public static File ensureParentDirectoryExists(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            return null;
        }

        File file = new File(filePath);
        File parentDir = file.getParentFile();
        
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                LOGGER.warn("Failed to create parent directory: {}", parentDir.getAbsolutePath());
            }
        }
        
        return parentDir;
    }

    /**
     * 上传MultipartFile文件
     * 
     * @param multipartFile 上传的文件
     * @param rootDir       根目录
     * @param relativeDir   相对目录
     * @return 文件相对路径，失败返回null
     */
    public static String uploadFile(MultipartFile multipartFile, String rootDir, String relativeDir) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            
            // 验证文件扩展名
            validateFileExtension(extension);
            
            String fileName = generateRandomFileName(extension);
            String relativePath = StrUtil.concat(true, relativeDir, fileName);
            String absolutePath = StrUtil.concat(true, rootDir, relativePath);

            // 创建父目录
            ensureParentDirectoryExists(absolutePath);

            // 保存文件
            File targetFile = new File(absolutePath);
            multipartFile.transferTo(targetFile);
            
            return relativePath;
            
        } catch (IOException e) {
            LOGGER.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传Base64编码的文件
     * 
     * @param base64Data  Base64数据
     * @param extension   文件扩展名
     * @param rootDir     根目录
     * @param relativeDir 相对目录
     * @return 文件相对路径，失败返回null
     */
    public static String uploadBase64File(String base64Data, String extension, String rootDir, String relativeDir) {
        if (StrUtil.isBlank(base64Data)) {
            return null;
        }

        try {
            // 验证文件扩展名
            validateFileExtension(extension);
            
            byte[] fileBytes = Base64.decode(base64Data);
            String fileName = generateRandomFileName(extension);
            String relativePath = StrUtil.concat(true, relativeDir, fileName);
            String absolutePath = StrUtil.concat(true, rootDir, relativePath);

            // 创建父目录
            ensureParentDirectoryExists(absolutePath);

            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(absolutePath)) {
                fos.write(fileBytes);
                fos.flush();
            }
            
            return relativePath;
            
        } catch (IOException e) {
            LOGGER.error("Base64文件上传失败", e);
            throw new RuntimeException("Base64文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传输入流文件
     * 
     * @param inputStream 输入流
     * @param extension   文件扩展名
     * @param rootDir     根目录
     * @param relativeDir 相对目录
     * @return 文件相对路径，失败返回null
     */
    public static String uploadStreamFile(InputStream inputStream, String extension, String rootDir, String relativeDir) {
        if (inputStream == null) {
            return null;
        }

        try {
            // 验证文件扩展名
            validateFileExtension(extension);
            
            String fileName = generateRandomFileName(extension);
            String relativePath = StrUtil.concat(true, relativeDir, fileName);
            String absolutePath = StrUtil.concat(true, rootDir, relativePath);

            // 创建父目录
            ensureParentDirectoryExists(absolutePath);

            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(absolutePath)) {
                IoUtil.copy(inputStream, fos);
                fos.flush();
            }
            
            return relativePath;
            
        } catch (IOException e) {
            LOGGER.error("流文件上传失败", e);
            throw new RuntimeException("流文件上传失败: " + e.getMessage());
        } finally {
            IoUtil.close(inputStream);
        }
    }

    /**
     * 下载文件
     * 
     * @param fileName     下载的文件名
     * @param rootDir      根目录
     * @param relativePath 文件相对路径
     * @param response     HTTP响应对象
     */
    public static void downloadFile(String fileName, String rootDir, String relativePath, HttpServletResponse response) {
        if (StrUtil.hasBlank(fileName, rootDir, relativePath)) {
            throw new IllegalArgumentException("参数不能为空");
        }

        String absolutePath = StrUtil.concat(true, rootDir, relativePath);
        
        try (InputStream inputStream = FileUtil.getInputStream(absolutePath)) {
            downloadFile(fileName, inputStream, response);
        } catch (IOException e) {
            LOGGER.error("文件下载失败：{}", absolutePath, e);
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }

    /**
     * 从输入流下载文件
     * 
     * @param fileName    下载的文件名
     * @param inputStream 文件输入流
     * @param response    HTTP响应对象
     */
    public static void downloadFile(String fileName, InputStream inputStream, HttpServletResponse response) {
        if (StrUtil.isBlank(fileName) || inputStream == null || response == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        try {
            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

            // 写入响应流
            try (OutputStream outputStream = response.getOutputStream()) {
                copyStream(inputStream, outputStream);
            }
            
        } catch (IOException e) {
            LOGGER.error("文件下载失败", e);
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        } finally {
            IoUtil.close(inputStream);
        }
    }

    /**
     * 预览文件
     * 
     * @param fileName     文件名
     * @param rootDir      根目录
     * @param relativePath 文件相对路径
     * @param response     HTTP响应对象
     */
    public static void previewFile(String fileName, String rootDir, String relativePath, HttpServletResponse response) {
        if (StrUtil.hasBlank(fileName, rootDir, relativePath)) {
            throw new IllegalArgumentException("参数不能为空");
        }

        String absolutePath = StrUtil.concat(true, rootDir, relativePath);
        
        try (InputStream inputStream = FileUtil.getInputStream(absolutePath)) {
            previewFile(fileName, inputStream, response);
        } catch (IOException e) {
            LOGGER.error("文件预览失败：{}", absolutePath, e);
            throw new RuntimeException("文件预览失败: " + e.getMessage());
        }
    }

    /**
     * 从输入流预览文件
     * 
     * @param fileName    文件名
     * @param inputStream 文件输入流
     * @param response    HTTP响应对象
     */
    public static void previewFile(String fileName, InputStream inputStream, HttpServletResponse response) {
        if (StrUtil.isBlank(fileName) || inputStream == null || response == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        try {
            String mimeType = FileUtil.getMimeType(fileName);
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            
            if (StrUtil.isNotBlank(mimeType)) {
                // 支持预览的文件类型
                response.setContentType(mimeType);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setHeader("Content-Disposition", "inline; filename*=utf-8''" + encodedFileName);
            } else {
                // 不支持预览，直接下载
                response.setContentType("application/octet-stream");
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            }

            // 写入响应流
            try (OutputStream outputStream = response.getOutputStream()) {
                copyStream(inputStream, outputStream);
            }
            
        } catch (IOException e) {
            LOGGER.error("文件预览失败", e);
            throw new RuntimeException("文件预览失败: " + e.getMessage());
        } finally {
            IoUtil.close(inputStream);
        }
    }

    /**
     * 复制流数据
     * 
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @throws IOException IO异常
     */
    private static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
    }

    // ================ 向后兼容的方法 ================

    /**
     * @deprecated 使用 {@link #validateFileExtension(String)} 替代
     */
    @Deprecated
    public static void checkWhiteExt(String ext) throws IOException {
        validateFileExtension(ext);
    }

    /**
     * @deprecated 使用 {@link #getFileExtension(String)} 替代
     */
    @Deprecated
    public static String getExtName(String fileName) {
        return getFileExtension(fileName);
    }

    /**
     * @deprecated 使用 {@link #generateRandomFileName(String)} 替代
     */
    @Deprecated
    public static String createRandomFileName(String extName) {
        return generateRandomFileName(extName);
    }

    /**
     * @deprecated 使用 {@link #ensureParentDirectoryExists(String)} 替代
     */
    @Deprecated
    public static File mkParentDirs(String path) {
        return ensureParentDirectoryExists(path);
    }

    /**
     * @deprecated 使用 {@link #uploadFile(MultipartFile, String, String)} 替代
     */
    @Deprecated
    public static String fileUpload(MultipartFile multipartFile, String rootDir, String relativeDir) {
        return uploadFile(multipartFile, rootDir, relativeDir);
    }

    /**
     * @deprecated 使用 {@link #uploadBase64File(String, String, String, String)} 替代
     */
    @Deprecated
    public static String fileUpload(String base64, String extName, String rootDir, String relativeDir) {
        return uploadBase64File(base64, extName, rootDir, relativeDir);
    }

    /**
     * @deprecated 使用 {@link #uploadStreamFile(InputStream, String, String, String)} 替代
     */
    @Deprecated
    public static String fileUpload(InputStream inputStream, String extName, String rootDir, String relativeDir) {
        return uploadStreamFile(inputStream, extName, rootDir, relativeDir);
    }
}