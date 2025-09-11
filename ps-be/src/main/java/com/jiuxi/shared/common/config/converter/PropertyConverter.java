package com.jiuxi.shared.common.config.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 配置属性转换器
 * 提供各种类型的配置属性转换功能
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Slf4j
@Component
public class PropertyConverter {

    /**
     * 转换文件大小字符串为字节数
     * 支持格式：10MB, 50KB, 1GB 等
     */
    public long parseFileSize(String sizeStr) {
        if (!StringUtils.hasText(sizeStr)) {
            throw new IllegalArgumentException("文件大小字符串不能为空");
        }
        
        String upperCaseSize = sizeStr.toUpperCase().trim();
        
        try {
            if (upperCaseSize.endsWith("KB")) {
                String numStr = upperCaseSize.substring(0, upperCaseSize.length() - 2);
                return Long.parseLong(numStr) * 1024;
            } else if (upperCaseSize.endsWith("MB")) {
                String numStr = upperCaseSize.substring(0, upperCaseSize.length() - 2);
                return Long.parseLong(numStr) * 1024 * 1024;
            } else if (upperCaseSize.endsWith("GB")) {
                String numStr = upperCaseSize.substring(0, upperCaseSize.length() - 2);
                return Long.parseLong(numStr) * 1024 * 1024 * 1024;
            } else if (upperCaseSize.endsWith("B")) {
                String numStr = upperCaseSize.substring(0, upperCaseSize.length() - 1);
                return Long.parseLong(numStr);
            } else {
                // 默认认为是字节数
                return Long.parseLong(upperCaseSize);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的文件大小格式: " + sizeStr, e);
        }
    }

    /**
     * 转换字节数为人类可读的文件大小字符串
     */
    public String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + "B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1fKB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1fMB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.1fGB", bytes / (1024.0 * 1024 * 1024));
        }
    }

    /**
     * 转换时间字符串为毫秒数
     * 支持格式：30s, 5m, 2h, 1d 等
     */
    public long parseDuration(String durationStr) {
        if (!StringUtils.hasText(durationStr)) {
            throw new IllegalArgumentException("时间字符串不能为空");
        }
        
        try {
            Duration duration = Duration.parse("PT" + durationStr.toUpperCase());
            return duration.toMillis();
        } catch (Exception e) {
            // 尝试其他格式
            return parseDurationCustom(durationStr);
        }
    }

    /**
     * 自定义时间格式解析
     */
    private long parseDurationCustom(String durationStr) {
        String str = durationStr.toLowerCase().trim();
        
        try {
            if (str.endsWith("ms")) {
                String numStr = str.substring(0, str.length() - 2);
                return Long.parseLong(numStr);
            } else if (str.endsWith("s")) {
                String numStr = str.substring(0, str.length() - 1);
                return Long.parseLong(numStr) * 1000;
            } else if (str.endsWith("m")) {
                String numStr = str.substring(0, str.length() - 1);
                return Long.parseLong(numStr) * 60 * 1000;
            } else if (str.endsWith("h")) {
                String numStr = str.substring(0, str.length() - 1);
                return Long.parseLong(numStr) * 60 * 60 * 1000;
            } else if (str.endsWith("d")) {
                String numStr = str.substring(0, str.length() - 1);
                return Long.parseLong(numStr) * 24 * 60 * 60 * 1000;
            } else {
                // 默认认为是毫秒数
                return Long.parseLong(str);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的时间格式: " + durationStr, e);
        }
    }

    /**
     * 转换毫秒数为人类可读的时间字符串
     */
    public String formatDuration(long millis) {
        if (millis < 1000) {
            return millis + "ms";
        } else if (millis < 60 * 1000) {
            return String.format("%.1fs", millis / 1000.0);
        } else if (millis < 60 * 60 * 1000) {
            return String.format("%.1fm", millis / (60.0 * 1000));
        } else if (millis < 24 * 60 * 60 * 1000) {
            return String.format("%.1fh", millis / (60.0 * 60 * 1000));
        } else {
            return String.format("%.1fd", millis / (24.0 * 60 * 60 * 1000));
        }
    }

    /**
     * 转换逗号分隔的字符串为列表
     */
    public List<String> parseStringList(String str) {
        if (!StringUtils.hasText(str)) {
            return List.of();
        }
        
        return Arrays.stream(str.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

    /**
     * 转换列表为逗号分隔的字符串
     */
    public String formatStringList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        
        return String.join(",", list);
    }

    /**
     * 转换字符串为布尔值（更宽松的解析）
     */
    public boolean parseBoolean(String str) {
        if (!StringUtils.hasText(str)) {
            return false;
        }
        
        String lowerStr = str.toLowerCase().trim();
        return "true".equals(lowerStr) || 
               "yes".equals(lowerStr) || 
               "on".equals(lowerStr) || 
               "1".equals(lowerStr) ||
               "enabled".equals(lowerStr);
    }

    /**
     * 转换端口号字符串为整数（验证范围）
     */
    public int parsePort(String portStr) {
        if (!StringUtils.hasText(portStr)) {
            throw new IllegalArgumentException("端口号不能为空");
        }
        
        try {
            int port = Integer.parseInt(portStr.trim());
            if (port <= 0 || port > 65535) {
                throw new IllegalArgumentException("端口号必须在1-65535范围内: " + port);
            }
            return port;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的端口号格式: " + portStr, e);
        }
    }

    /**
     * 转换URL字符串（添加协议前缀等标准化处理）
     */
    public String normalizeUrl(String url) {
        if (!StringUtils.hasText(url)) {
            return url;
        }
        
        String trimmedUrl = url.trim();
        
        // 如果没有协议前缀，默认添加http://
        if (!trimmedUrl.startsWith("http://") && !trimmedUrl.startsWith("https://")) {
            trimmedUrl = "http://" + trimmedUrl;
        }
        
        // 移除末尾的斜杠
        if (trimmedUrl.endsWith("/")) {
            trimmedUrl = trimmedUrl.substring(0, trimmedUrl.length() - 1);
        }
        
        return trimmedUrl;
    }

    /**
     * 转换百分比字符串为小数
     */
    public double parsePercentage(String percentageStr) {
        if (!StringUtils.hasText(percentageStr)) {
            throw new IllegalArgumentException("百分比字符串不能为空");
        }
        
        String str = percentageStr.trim();
        
        try {
            if (str.endsWith("%")) {
                String numStr = str.substring(0, str.length() - 1);
                return Double.parseDouble(numStr) / 100.0;
            } else {
                return Double.parseDouble(str);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的百分比格式: " + percentageStr, e);
        }
    }

    /**
     * 转换小数为百分比字符串
     */
    public String formatPercentage(double value) {
        return String.format("%.1f%%", value * 100);
    }

    /**
     * 验证并转换邮箱地址
     */
    public String normalizeEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return email;
        }
        
        String trimmedEmail = email.trim().toLowerCase();
        
        // 简单的邮箱格式验证
        if (!trimmedEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("无效的邮箱格式: " + email);
        }
        
        return trimmedEmail;
    }

    /**
     * 转换字符串为安全的密码（添加验证）
     */
    public String validatePassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        String trimmedPassword = password.trim();
        
        if (trimmedPassword.length() < 6) {
            log.warn("密码长度过短，建议至少6位字符");
        }
        
        return trimmedPassword;
    }

    /**
     * 转换路径字符串（标准化路径分隔符）
     */
    public String normalizePath(String path) {
        if (!StringUtils.hasText(path)) {
            return path;
        }
        
        String normalizedPath = path.trim();
        
        // 统一使用正斜杠作为路径分隔符
        normalizedPath = normalizedPath.replace("\\", "/");
        
        // 移除重复的斜杠
        normalizedPath = normalizedPath.replaceAll("/+", "/");
        
        // 确保路径以斜杠开头（如果不是相对路径）
        if (!normalizedPath.startsWith("./") && !normalizedPath.startsWith("../") && 
            !normalizedPath.startsWith("/") && !normalizedPath.matches("^[a-zA-Z]:.*")) {
            normalizedPath = "/" + normalizedPath;
        }
        
        return normalizedPath;
    }
}