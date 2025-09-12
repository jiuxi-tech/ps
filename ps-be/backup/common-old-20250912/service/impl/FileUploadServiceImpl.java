package com.jiuxi.common.service.impl;

import com.jiuxi.common.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: FileUploadServiceImpl
 * @Description: 文件上传服务实现类
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {
    
    @Value("${file.upload.path:/uploads}")
    private String uploadPath;
    
    @Value("${file.upload.max-size:10485760}")
    private long maxFileSize;
    
    @Value("${file.upload.allowed-types:jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx,txt}")
    private String allowedTypes;
    
    @Value("${file.upload.base-url:http://localhost:8082/ps-be}")
    private String baseUrl;
    
    @Override
    public Map<String, Object> uploadFile(MultipartFile file, String category) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证文件
            if (file.isEmpty()) {
                result.put("success", false);
                result.put("message", "文件不能为空");
                return result;
            }
            
            // 验证文件大小
            if (!validateFileSize(file, maxFileSize)) {
                result.put("success", false);
                result.put("message", "文件大小超过限制");
                return result;
            }
            
            // 验证文件类型
            List<String> allowedTypeList = Arrays.asList(allowedTypes.split(","));
            if (!validateFileType(file, allowedTypeList)) {
                result.put("success", false);
                result.put("message", "不支持的文件类型");
                return result;
            }
            
            // 生成文件路径
            String fileName = generateFileName(file.getOriginalFilename());
            String categoryPath = category != null ? category : "default";
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = categoryPath + "/" + datePath + "/" + fileName;
            String fullPath = uploadPath + "/" + relativePath;
            
            // 创建目录
            File targetFile = new File(fullPath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            
            // 保存文件
            file.transferTo(targetFile);
            
            // 计算MD5
            String md5 = calculateFileMD5(file);
            
            result.put("success", true);
            result.put("fileName", fileName);
            result.put("originalName", file.getOriginalFilename());
            result.put("filePath", relativePath);
            result.put("fullPath", fullPath);
            result.put("fileSize", file.getSize());
            result.put("contentType", file.getContentType());
            result.put("md5", md5);
            result.put("url", generateFileUrl(relativePath));
            result.put("uploadTime", LocalDateTime.now());
            
            log.info("文件上传成功: {}", relativePath);
            
        } catch (Exception e) {
            log.error("文件上传失败", e);
            result.put("success", false);
            result.put("message", "文件上传失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public List<Map<String, Object>> uploadFiles(List<MultipartFile> files, String category) {
        return files.stream()
                .map(file -> uploadFile(file, category))
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> uploadFile(InputStream inputStream, String fileName, String contentType, String category) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 生成文件路径
            String newFileName = generateFileName(fileName);
            String categoryPath = category != null ? category : "default";
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = categoryPath + "/" + datePath + "/" + newFileName;
            String fullPath = uploadPath + "/" + relativePath;
            
            // 创建目录
            File targetFile = new File(fullPath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            
            // 保存文件
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            result.put("success", true);
            result.put("fileName", newFileName);
            result.put("originalName", fileName);
            result.put("filePath", relativePath);
            result.put("fullPath", fullPath);
            result.put("fileSize", targetFile.length());
            result.put("contentType", contentType);
            result.put("url", generateFileUrl(relativePath));
            result.put("uploadTime", LocalDateTime.now());
            
            log.info("文件上传成功: {}", relativePath);
            
        } catch (Exception e) {
            log.error("文件上传失败", e);
            result.put("success", false);
            result.put("message", "文件上传失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> uploadBase64File(String base64Data, String fileName, String category) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 解析Base64数据
            String[] parts = base64Data.split(",");
            String data = parts.length > 1 ? parts[1] : parts[0];
            byte[] bytes = Base64.getDecoder().decode(data);
            
            // 通过输入流上传
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            String contentType = parts.length > 1 ? parts[0].split(":")[1].split(";")[0] : "application/octet-stream";
            
            result = uploadFile(inputStream, fileName, contentType, category);
            
        } catch (Exception e) {
            log.error("Base64文件上传失败", e);
            result.put("success", false);
            result.put("message", "Base64文件上传失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public boolean deleteFile(String filePath) {
        try {
            String fullPath = uploadPath + "/" + filePath;
            File file = new File(fullPath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    log.info("文件删除成功: {}", filePath);
                }
                return deleted;
            }
            return false;
        } catch (Exception e) {
            log.error("文件删除失败: {}", filePath, e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> deleteFiles(List<String> filePaths) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> failedFiles = new ArrayList<>();
        
        for (String filePath : filePaths) {
            if (deleteFile(filePath)) {
                successCount++;
            } else {
                failCount++;
                failedFiles.add(filePath);
            }
        }
        
        result.put("total", filePaths.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedFiles", failedFiles);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getFileInfo(String filePath) {
        Map<String, Object> info = new HashMap<>();
        
        try {
            String fullPath = uploadPath + "/" + filePath;
            File file = new File(fullPath);
            
            if (file.exists()) {
                info.put("exists", true);
                info.put("fileName", file.getName());
                info.put("filePath", filePath);
                info.put("fileSize", file.length());
                info.put("lastModified", new Date(file.lastModified()));
                info.put("isDirectory", file.isDirectory());
                info.put("canRead", file.canRead());
                info.put("canWrite", file.canWrite());
                info.put("url", generateFileUrl(filePath));
            } else {
                info.put("exists", false);
            }
            
        } catch (Exception e) {
            log.error("获取文件信息失败: {}", filePath, e);
            info.put("exists", false);
            info.put("error", e.getMessage());
        }
        
        return info;
    }
    
    @Override
    public boolean fileExists(String filePath) {
        try {
            String fullPath = uploadPath + "/" + filePath;
            return new File(fullPath).exists();
        } catch (Exception e) {
            log.error("检查文件存在性失败: {}", filePath, e);
            return false;
        }
    }
    
    @Override
    public String generateFileUrl(String filePath) {
        return baseUrl + "/files/" + filePath;
    }
    
    @Override
    public String generateTemporaryUrl(String filePath, int expireMinutes) {
        // 简单实现，实际项目中可能需要更复杂的签名机制
        long expireTime = System.currentTimeMillis() + (expireMinutes * 60 * 1000L);
        return generateFileUrl(filePath) + "?expire=" + expireTime;
    }
    
    @Override
    public boolean copyFile(String sourcePath, String targetPath) {
        try {
            String sourceFullPath = uploadPath + "/" + sourcePath;
            String targetFullPath = uploadPath + "/" + targetPath;
            
            File sourceFile = new File(sourceFullPath);
            File targetFile = new File(targetFullPath);
            
            if (!sourceFile.exists()) {
                return false;
            }
            
            // 创建目标目录
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            log.info("文件复制成功: {} -> {}", sourcePath, targetPath);
            return true;
            
        } catch (Exception e) {
            log.error("文件复制失败: {} -> {}", sourcePath, targetPath, e);
            return false;
        }
    }
    
    @Override
    public boolean moveFile(String sourcePath, String targetPath) {
        try {
            if (copyFile(sourcePath, targetPath)) {
                return deleteFile(sourcePath);
            }
            return false;
        } catch (Exception e) {
            log.error("文件移动失败: {} -> {}", sourcePath, targetPath, e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> renameFile(String filePath, String newFileName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String fullPath = uploadPath + "/" + filePath;
            File file = new File(fullPath);
            
            if (!file.exists()) {
                result.put("success", false);
                result.put("message", "文件不存在");
                return result;
            }
            
            String newFilePath = file.getParent() + "/" + newFileName;
            File newFile = new File(newFilePath);
            
            if (file.renameTo(newFile)) {
                String newRelativePath = filePath.substring(0, filePath.lastIndexOf("/") + 1) + newFileName;
                result.put("success", true);
                result.put("oldPath", filePath);
                result.put("newPath", newRelativePath);
                result.put("newUrl", generateFileUrl(newRelativePath));
                log.info("文件重命名成功: {} -> {}", filePath, newRelativePath);
            } else {
                result.put("success", false);
                result.put("message", "重命名失败");
            }
            
        } catch (Exception e) {
            log.error("文件重命名失败: {}", filePath, e);
            result.put("success", false);
            result.put("message", "重命名失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getFileList(String category, int page, int size) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> files = new ArrayList<>();
        
        try {
            String categoryPath = uploadPath + "/" + (category != null ? category : "");
            File dir = new File(categoryPath);
            
            if (dir.exists() && dir.isDirectory()) {
                File[] fileArray = dir.listFiles();
                if (fileArray != null) {
                    Arrays.sort(fileArray, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
                    
                    int start = (page - 1) * size;
                    int end = Math.min(start + size, fileArray.length);
                    
                    for (int i = start; i < end; i++) {
                        File file = fileArray[i];
                        if (file.isFile()) {
                            Map<String, Object> fileInfo = new HashMap<>();
                            String relativePath = file.getAbsolutePath().replace(uploadPath + "/", "").replace("\\", "/");
                            
                            fileInfo.put("fileName", file.getName());
                            fileInfo.put("filePath", relativePath);
                            fileInfo.put("fileSize", file.length());
                            fileInfo.put("lastModified", new Date(file.lastModified()));
                            fileInfo.put("url", generateFileUrl(relativePath));
                            
                            files.add(fileInfo);
                        }
                    }
                    
                    result.put("records", files);
                    result.put("total", fileArray.length);
                    result.put("current", page);
                    result.put("size", size);
                    result.put("pages", (int) Math.ceil((double) fileArray.length / size));
                }
            }
            
        } catch (Exception e) {
            log.error("获取文件列表失败", e);
            result.put("records", files);
            result.put("total", 0);
            result.put("current", page);
            result.put("size", size);
            result.put("pages", 0);
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> searchFiles(String keyword, String category, String fileType, int page, int size) {
        // 简化实现，实际项目中可能需要更复杂的搜索逻辑
        return getFileList(category, page, size);
    }
    
    @Override
    public Map<String, Object> getUploadStatistics(String startDate, String endDate) {
        Map<String, Object> stats = new HashMap<>();
        
        // 简化实现，实际项目中需要从数据库获取统计数据
        stats.put("totalFiles", 0);
        stats.put("totalSize", 0L);
        stats.put("uploadCount", 0);
        stats.put("deleteCount", 0);
        
        return stats;
    }
    
    @Override
    public Map<String, Object> cleanExpiredFiles(int days) {
        Map<String, Object> result = new HashMap<>();
        int cleanedCount = 0;
        long cleanedSize = 0L;
        
        try {
            long expireTime = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L);
            File uploadDir = new File(uploadPath);
            
            if (uploadDir.exists()) {
                cleanedCount = cleanExpiredFilesRecursive(uploadDir, expireTime);
            }
            
            result.put("success", true);
            result.put("cleanedCount", cleanedCount);
            result.put("cleanedSize", cleanedSize);
            
        } catch (Exception e) {
            log.error("清理过期文件失败", e);
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public boolean validateFileType(MultipartFile file, List<String> allowedTypes) {
        if (file == null || file.getOriginalFilename() == null) {
            return false;
        }
        
        String fileName = file.getOriginalFilename().toLowerCase();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        
        return allowedTypes.contains(extension);
    }
    
    @Override
    public boolean validateFileSize(MultipartFile file, long maxSizeBytes) {
        return file != null && file.getSize() <= maxSizeBytes;
    }
    
    @Override
    public String generateThumbnail(String imagePath, int width, int height) {
        try {
            String fullPath = uploadPath + "/" + imagePath;
            File imageFile = new File(fullPath);
            
            if (!imageFile.exists()) {
                return null;
            }
            
            BufferedImage originalImage = ImageIO.read(imageFile);
            BufferedImage thumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            
            Graphics2D g = thumbnailImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(originalImage, 0, 0, width, height, null);
            g.dispose();
            
            String thumbnailPath = imagePath.replace(".", "_thumb.");
            String thumbnailFullPath = uploadPath + "/" + thumbnailPath;
            
            File thumbnailFile = new File(thumbnailFullPath);
            if (!thumbnailFile.getParentFile().exists()) {
                thumbnailFile.getParentFile().mkdirs();
            }
            
            String format = imagePath.substring(imagePath.lastIndexOf(".") + 1);
            ImageIO.write(thumbnailImage, format, thumbnailFile);
            
            return thumbnailPath;
            
        } catch (Exception e) {
            log.error("生成缩略图失败: {}", imagePath, e);
            return null;
        }
    }
    
    @Override
    public String compressImage(String imagePath, float quality) {
        // 简化实现，实际项目中需要更复杂的图片压缩逻辑
        return imagePath;
    }
    
    @Override
    public Map<String, Object> getImageInfo(String imagePath) {
        Map<String, Object> info = new HashMap<>();
        
        try {
            String fullPath = uploadPath + "/" + imagePath;
            File imageFile = new File(fullPath);
            
            if (imageFile.exists()) {
                BufferedImage image = ImageIO.read(imageFile);
                if (image != null) {
                    info.put("width", image.getWidth());
                    info.put("height", image.getHeight());
                    info.put("format", imagePath.substring(imagePath.lastIndexOf(".") + 1));
                    info.put("fileSize", imageFile.length());
                }
            }
            
        } catch (Exception e) {
            log.error("获取图片信息失败: {}", imagePath, e);
        }
        
        return info;
    }
    
    @Override
    public boolean createFolder(String folderPath) {
        try {
            String fullPath = uploadPath + "/" + folderPath;
            File folder = new File(fullPath);
            return folder.mkdirs();
        } catch (Exception e) {
            log.error("创建文件夹失败: {}", folderPath, e);
            return false;
        }
    }
    
    @Override
    public boolean deleteFolder(String folderPath, boolean recursive) {
        try {
            String fullPath = uploadPath + "/" + folderPath;
            File folder = new File(fullPath);
            
            if (!folder.exists()) {
                return false;
            }
            
            if (recursive) {
                return deleteFolderRecursive(folder);
            } else {
                return folder.delete();
            }
            
        } catch (Exception e) {
            log.error("删除文件夹失败: {}", folderPath, e);
            return false;
        }
    }
    
    @Override
    public List<Map<String, Object>> getFolderContents(String folderPath) {
        List<Map<String, Object>> contents = new ArrayList<>();
        
        try {
            String fullPath = uploadPath + "/" + folderPath;
            File folder = new File(fullPath);
            
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        Map<String, Object> item = new HashMap<>();
                        String relativePath = file.getAbsolutePath().replace(uploadPath + "/", "").replace("\\", "/");
                        
                        item.put("name", file.getName());
                        item.put("path", relativePath);
                        item.put("isDirectory", file.isDirectory());
                        item.put("size", file.length());
                        item.put("lastModified", new Date(file.lastModified()));
                        
                        if (!file.isDirectory()) {
                            item.put("url", generateFileUrl(relativePath));
                        }
                        
                        contents.add(item);
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("获取文件夹内容失败: {}", folderPath, e);
        }
        
        return contents;
    }
    
    @Override
    public String calculateFileMD5(MultipartFile file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = file.getBytes();
            byte[] digest = md.digest(bytes);
            
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
            
        } catch (Exception e) {
            log.error("计算文件MD5失败", e);
            return null;
        }
    }
    
    @Override
    public String calculateFileMD5(String filePath) {
        try {
            String fullPath = uploadPath + "/" + filePath;
            File file = new File(fullPath);
            
            if (!file.exists()) {
                return null;
            }
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesRead);
                }
            }
            
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
            
        } catch (Exception e) {
            log.error("计算文件MD5失败: {}", filePath, e);
            return null;
        }
    }
    
    @Override
    public Map<String, Object> checkFileDuplicate(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String md5 = calculateFileMD5(file);
            if (md5 != null) {
                // 简化实现，实际项目中需要在数据库中查找相同MD5的文件
                result.put("md5", md5);
                result.put("isDuplicate", false);
                result.put("existingFiles", new ArrayList<>());
            }
            
        } catch (Exception e) {
            log.error("检查文件重复失败", e);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getStorageUsage() {
        Map<String, Object> usage = new HashMap<>();
        
        try {
            File uploadDir = new File(uploadPath);
            if (uploadDir.exists()) {
                long totalSize = calculateDirectorySize(uploadDir);
                long totalFiles = countFiles(uploadDir);
                
                usage.put("totalSize", totalSize);
                usage.put("totalFiles", totalFiles);
                usage.put("uploadPath", uploadPath);
                
                // 获取磁盘空间信息
                long freeSpace = uploadDir.getFreeSpace();
                long totalSpace = uploadDir.getTotalSpace();
                long usableSpace = uploadDir.getUsableSpace();
                
                usage.put("freeSpace", freeSpace);
                usage.put("totalSpace", totalSpace);
                usage.put("usableSpace", usableSpace);
            }
            
        } catch (Exception e) {
            log.error("获取存储使用情况失败", e);
        }
        
        return usage;
    }
    
    @Override
    public boolean backupFile(String filePath, String backupPath) {
        return copyFile(filePath, backupPath);
    }
    
    @Override
    public boolean restoreFile(String backupPath, String restorePath) {
        return copyFile(backupPath, restorePath);
    }
    
    // 私有辅助方法
    
    private String generateFileName(String originalFileName) {
        if (originalFileName == null) {
            return UUID.randomUUID().toString();
        }
        
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }
        
        return UUID.randomUUID().toString() + extension;
    }
    
    private int cleanExpiredFilesRecursive(File dir, long expireTime) {
        int count = 0;
        
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    count += cleanExpiredFilesRecursive(file, expireTime);
                } else if (file.lastModified() < expireTime) {
                    if (file.delete()) {
                        count++;
                    }
                }
            }
        }
        
        return count;
    }
    
    private boolean deleteFolderRecursive(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolderRecursive(file);
                } else {
                    file.delete();
                }
            }
        }
        return folder.delete();
    }
    
    private long calculateDirectorySize(File dir) {
        long size = 0;
        
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    size += calculateDirectorySize(file);
                } else {
                    size += file.length();
                }
            }
        }
        
        return size;
    }
    
    private long countFiles(File dir) {
        long count = 0;
        
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    count += countFiles(file);
                } else {
                    count++;
                }
            }
        }
        
        return count;
    }
}