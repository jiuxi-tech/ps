package com.jiuxi.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: FileUploadService
 * @Description: 文件上传服务接口
 * @Author: Qdd
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface FileUploadService {
    
    /**
     * 上传单个文件
     * @param file 文件
     * @param category 文件分类（如：avatar, document, image等）
     * @return 上传结果（包含文件路径、URL等信息）
     */
    Map<String, Object> uploadFile(MultipartFile file, String category);
    
    /**
     * 上传多个文件
     * @param files 文件列表
     * @param category 文件分类
     * @return 上传结果列表
     */
    List<Map<String, Object>> uploadFiles(List<MultipartFile> files, String category);
    
    /**
     * 通过输入流上传文件
     * @param inputStream 输入流
     * @param fileName 文件名
     * @param contentType 文件类型
     * @param category 文件分类
     * @return 上传结果
     */
    Map<String, Object> uploadFile(InputStream inputStream, String fileName, String contentType, String category);
    
    /**
     * 上传Base64编码的文件
     * @param base64Data Base64数据
     * @param fileName 文件名
     * @param category 文件分类
     * @return 上传结果
     */
    Map<String, Object> uploadBase64File(String base64Data, String fileName, String category);
    
    /**
     * 删除文件
     * @param filePath 文件路径
     * @return 删除结果
     */
    boolean deleteFile(String filePath);
    
    /**
     * 批量删除文件
     * @param filePaths 文件路径列表
     * @return 删除结果统计
     */
    Map<String, Object> deleteFiles(List<String> filePaths);
    
    /**
     * 获取文件信息
     * @param filePath 文件路径
     * @return 文件信息
     */
    Map<String, Object> getFileInfo(String filePath);
    
    /**
     * 检查文件是否存在
     * @param filePath 文件路径
     * @return 是否存在
     */
    boolean fileExists(String filePath);
    
    /**
     * 生成文件访问URL
     * @param filePath 文件路径
     * @return 访问URL
     */
    String generateFileUrl(String filePath);
    
    /**
     * 生成临时访问URL（带过期时间）
     * @param filePath 文件路径
     * @param expireMinutes 过期时间（分钟）
     * @return 临时URL
     */
    String generateTemporaryUrl(String filePath, int expireMinutes);
    
    /**
     * 复制文件
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @return 复制结果
     */
    boolean copyFile(String sourcePath, String targetPath);
    
    /**
     * 移动文件
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @return 移动结果
     */
    boolean moveFile(String sourcePath, String targetPath);
    
    /**
     * 重命名文件
     * @param filePath 文件路径
     * @param newFileName 新文件名
     * @return 重命名结果
     */
    Map<String, Object> renameFile(String filePath, String newFileName);
    
    /**
     * 获取文件列表
     * @param category 文件分类
     * @param page 页码
     * @param size 每页大小
     * @return 文件列表
     */
    Map<String, Object> getFileList(String category, int page, int size);
    
    /**
     * 搜索文件
     * @param keyword 关键词
     * @param category 文件分类
     * @param fileType 文件类型
     * @param page 页码
     * @param size 每页大小
     * @return 搜索结果
     */
    Map<String, Object> searchFiles(String keyword, String category, String fileType, int page, int size);
    
    /**
     * 获取文件上传统计
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计信息
     */
    Map<String, Object> getUploadStatistics(String startDate, String endDate);
    
    /**
     * 清理过期文件
     * @param days 保留天数
     * @return 清理结果
     */
    Map<String, Object> cleanExpiredFiles(int days);
    
    /**
     * 验证文件类型
     * @param file 文件
     * @param allowedTypes 允许的文件类型列表
     * @return 验证结果
     */
    boolean validateFileType(MultipartFile file, List<String> allowedTypes);
    
    /**
     * 验证文件大小
     * @param file 文件
     * @param maxSizeBytes 最大文件大小（字节）
     * @return 验证结果
     */
    boolean validateFileSize(MultipartFile file, long maxSizeBytes);
    
    /**
     * 生成缩略图（仅适用于图片）
     * @param imagePath 图片路径
     * @param width 缩略图宽度
     * @param height 缩略图高度
     * @return 缩略图路径
     */
    String generateThumbnail(String imagePath, int width, int height);
    
    /**
     * 压缩图片
     * @param imagePath 图片路径
     * @param quality 压缩质量（0.0-1.0）
     * @return 压缩后的图片路径
     */
    String compressImage(String imagePath, float quality);
    
    /**
     * 获取图片信息
     * @param imagePath 图片路径
     * @return 图片信息（宽度、高度、格式等）
     */
    Map<String, Object> getImageInfo(String imagePath);
    
    /**
     * 创建文件夹
     * @param folderPath 文件夹路径
     * @return 创建结果
     */
    boolean createFolder(String folderPath);
    
    /**
     * 删除文件夹
     * @param folderPath 文件夹路径
     * @param recursive 是否递归删除
     * @return 删除结果
     */
    boolean deleteFolder(String folderPath, boolean recursive);
    
    /**
     * 获取文件夹内容
     * @param folderPath 文件夹路径
     * @return 文件夹内容列表
     */
    List<Map<String, Object>> getFolderContents(String folderPath);
    
    /**
     * 计算文件MD5值
     * @param file 文件
     * @return MD5值
     */
    String calculateFileMD5(MultipartFile file);
    
    /**
     * 计算文件MD5值
     * @param filePath 文件路径
     * @return MD5值
     */
    String calculateFileMD5(String filePath);
    
    /**
     * 检查文件是否重复
     * @param file 文件
     * @return 重复检查结果
     */
    Map<String, Object> checkFileDuplicate(MultipartFile file);
    
    /**
     * 获取存储空间使用情况
     * @return 存储空间信息
     */
    Map<String, Object> getStorageUsage();
    
    /**
     * 备份文件
     * @param filePath 文件路径
     * @param backupPath 备份路径
     * @return 备份结果
     */
    boolean backupFile(String filePath, String backupPath);
    
    /**
     * 恢复文件
     * @param backupPath 备份路径
     * @param restorePath 恢复路径
     * @return 恢复结果
     */
    boolean restoreFile(String backupPath, String restorePath);
}