package com.jiuxi.shared.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * IP访问控制工具类
 * 支持多种IP匹配格式：
 * 1. 精确IP：192.168.1.100
 * 2. CIDR表示法：192.168.1.0/24
 * 3. 通配符：192.168.1.*
 * 4. IP范围：192.168.1.1-192.168.1.100
 * 
 * @author system
 * @date 2025-01-20
 */
public class IpAccessControlUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(IpAccessControlUtil.class);
    
    /**
     * 检查IP是否匹配规则列表
     * 
     * @param clientIp 客户端IP
     * @param rules 规则列表，多个规则用分号分隔
     * @return true表示匹配，false表示不匹配
     */
    public static boolean isIpMatched(String clientIp, String rules) {
        if (!StringUtils.hasText(clientIp) || !StringUtils.hasText(rules)) {
            return false;
        }
        
        // 特殊处理：*表示匹配所有IP
        if ("*".equals(rules.trim())) {
            return true;
        }
        
        // 分割规则
        List<String> ruleList = Arrays.asList(rules.split(";"));
        
        for (String rule : ruleList) {
            rule = rule.trim();
            if (StringUtils.hasText(rule) && matchSingleRule(clientIp, rule)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 检查IP是否匹配单个规则
     * 
     * @param clientIp 客户端IP
     * @param rule 单个规则
     * @return true表示匹配，false表示不匹配
     */
    public static boolean matchSingleRule(String clientIp, String rule) {
        try {
            // 1. 精确匹配
            if (clientIp.equals(rule)) {
                return true;
            }
            
            // 2. CIDR表示法匹配
            if (rule.contains("/")) {
                return matchCidr(clientIp, rule);
            }
            
            // 3. 通配符匹配
            if (rule.contains("*")) {
                return matchWildcard(clientIp, rule);
            }
            
            // 4. IP范围匹配
            if (rule.contains("-")) {
                return matchRange(clientIp, rule);
            }
            
        } catch (Exception e) {
            logger.warn("IP规则匹配异常: clientIp={}, rule={}, error={}", clientIp, rule, e.getMessage());
        }
        
        return false;
    }
    
    /**
     * CIDR表示法匹配
     * 
     * @param clientIp 客户端IP
     * @param cidr CIDR规则，如：192.168.1.0/24
     * @return true表示匹配，false表示不匹配
     */
    private static boolean matchCidr(String clientIp, String cidr) {
        try {
            String[] parts = cidr.split("/");
            if (parts.length != 2) {
                return false;
            }
            
            String networkIp = parts[0];
            int prefixLength = Integer.parseInt(parts[1]);
            
            if (prefixLength < 0 || prefixLength > 32) {
                return false;
            }
            
            long clientIpLong = ipToLong(clientIp);
            long networkIpLong = ipToLong(networkIp);
            
            // 计算网络掩码
            long mask = (0xFFFFFFFFL << (32 - prefixLength)) & 0xFFFFFFFFL;
            
            return (clientIpLong & mask) == (networkIpLong & mask);
            
        } catch (Exception e) {
            logger.warn("CIDR匹配异常: clientIp={}, cidr={}, error={}", clientIp, cidr, e.getMessage());
            return false;
        }
    }
    
    /**
     * 通配符匹配
     * 
     * @param clientIp 客户端IP
     * @param pattern 通配符规则，如：192.168.1.*
     * @return true表示匹配，false表示不匹配
     */
    private static boolean matchWildcard(String clientIp, String pattern) {
        try {
            // 将通配符转换为正则表达式
            String regex = pattern.replace(".", "\\.")
                                 .replace("*", "\\d+");
            
            return Pattern.matches(regex, clientIp);
            
        } catch (Exception e) {
            logger.warn("通配符匹配异常: clientIp={}, pattern={}, error={}", clientIp, pattern, e.getMessage());
            return false;
        }
    }
    
    /**
     * IP范围匹配
     * 
     * @param clientIp 客户端IP
     * @param range IP范围规则，如：192.168.1.1-192.168.1.100
     * @return true表示匹配，false表示不匹配
     */
    private static boolean matchRange(String clientIp, String range) {
        try {
            String[] parts = range.split("-");
            if (parts.length != 2) {
                return false;
            }
            
            String startIp = parts[0].trim();
            String endIp = parts[1].trim();
            
            long clientIpLong = ipToLong(clientIp);
            long startIpLong = ipToLong(startIp);
            long endIpLong = ipToLong(endIp);
            
            return clientIpLong >= startIpLong && clientIpLong <= endIpLong;
            
        } catch (Exception e) {
            logger.warn("IP范围匹配异常: clientIp={}, range={}, error={}", clientIp, range, e.getMessage());
            return false;
        }
    }
    
    /**
     * 将IP地址转换为长整型
     * 
     * @param ip IP地址字符串
     * @return 长整型IP值
     */
    private static long ipToLong(String ip) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            byte[] bytes = inetAddress.getAddress();
            
            long result = 0;
            for (int i = 0; i < bytes.length; i++) {
                result = (result << 8) | (bytes[i] & 0xFF);
            }
            
            return result;
            
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("无效的IP地址: " + ip, e);
        }
    }
    
    /**
     * 验证IP地址格式是否正确
     * 
     * @param ip IP地址字符串
     * @return true表示格式正确，false表示格式错误
     */
    public static boolean isValidIp(String ip) {
        if (!StringUtils.hasText(ip)) {
            return false;
        }
        
        try {
            InetAddress.getByName(ip);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }
    
    /**
     * 获取客户端真实IP地址
     * 考虑代理服务器的情况
     * 
     * @param request HTTP请求对象
     * @return 客户端真实IP地址
     */
    public static String getClientRealIp(javax.servlet.http.HttpServletRequest request) {
        String ip = null;
        
        // 1. 检查X-Forwarded-For头
        ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 可能包含多个IP，取第一个
            int index = ip.indexOf(',');
            if (index != -1) {
                ip = ip.substring(0, index);
            }
            return ip.trim();
        }
        
        // 2. 检查X-Real-IP头
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        
        // 3. 检查Proxy-Client-IP头
        ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        
        // 4. 检查WL-Proxy-Client-IP头
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        
        // 5. 检查HTTP_CLIENT_IP头
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        
        // 6. 检查HTTP_X_FORWARDED_FOR头
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        
        // 7. 最后使用getRemoteAddr()
        ip = request.getRemoteAddr();
        
        // 处理IPv6的本地回环地址
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        
        return ip;
    }
}