package com.jiuxi.admin.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 数据脱敏工具类
 * 提供各种敏感信息的脱敏处理功能
 * 
 * 支持的脱敏类型：
 * 1. 姓名：保留姓氏，其他替换为*
 * 2. 手机号：保留前3后4位，中间4位替换为****
 * 3. 邮箱：用户名保留首尾，域名完整
 * 4. 身份证号：保留前6后4位，中间替换为********
 * 5. 工号：保留前4位，其余替换为***
 * 6. 地址：保留到区级，其余替换为***
 * 
 * @author system
 * @date 2025-01-30
 */
public class DataMaskUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(DataMaskUtil.class);
    
    /**
     * 姓名脱敏
     * 规则：保留姓氏，其他替换为*
     * 示例：张三 -> 张*，欧阳峰 -> 欧阳*
     * 
     * @param name 原始姓名
     * @return 脱敏后的姓名
     */
    public static String maskName(String name) {
        if (!StringUtils.hasText(name)) {
            return name;
        }
        
        try {
            int length = name.length();
            
            // 单字名（如：王）
            if (length == 1) {
                return name;
            }
            
            // 双字名（如：张三）
            if (length == 2) {
                return name.substring(0, 1) + "*";
            }
            
            // 三字名（如：张三丰）
            if (length == 3) {
                // 判断是否是复姓（欧阳、上官、司马等）
                String surname2 = name.substring(0, 2);
                if (isCompoundSurname(surname2)) {
                    return surname2 + "*";
                } else {
                    return name.substring(0, 1) + "**";
                }
            }
            
            // 四字及以上名字（如：爱新觉罗·玄烨）
            if (length >= 4) {
                // 判断是否是复姓
                String surname2 = name.substring(0, 2);
                if (isCompoundSurname(surname2)) {
                    StringBuilder masked = new StringBuilder(surname2);
                    for (int i = 2; i < length; i++) {
                        masked.append("*");
                    }
                    return masked.toString();
                } else {
                    StringBuilder masked = new StringBuilder(name.substring(0, 1));
                    for (int i = 1; i < length; i++) {
                        masked.append("*");
                    }
                    return masked.toString();
                }
            }
            
        } catch (Exception e) {
            logger.warn("姓名脱敏异常: name={}, error={}", name, e.getMessage());
        }
        
        return name;
    }
    
    /**
     * 手机号脱敏
     * 规则：保留前3后4位，中间4位替换为****
     * 示例：13812345678 -> 138****5678
     * 
     * @param phone 原始手机号
     * @return 脱敏后的手机号
     */
    public static String maskPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return phone;
        }
        
        try {
            // 移除所有空格和横杠
            phone = phone.replaceAll("[\\s-]", "");
            
            // 标准11位手机号
            if (phone.length() == 11) {
                return phone.substring(0, 3) + "****" + phone.substring(7);
            }
            
            // 座机号（区号-号码）
            if (phone.contains("-")) {
                String[] parts = phone.split("-");
                if (parts.length == 2 && parts[1].length() >= 4) {
                    return parts[0] + "-****" + parts[1].substring(parts[1].length() - 4);
                }
            }
            
            // 其他格式的电话号码
            if (phone.length() >= 7) {
                int keepStart = 3;
                int keepEnd = 4;
                if (phone.length() < 11) {
                    keepStart = 2;
                    keepEnd = 2;
                }
                return phone.substring(0, keepStart) + "****" + phone.substring(phone.length() - keepEnd);
            }
            
        } catch (Exception e) {
            logger.warn("手机号脱敏异常: phone={}, error={}", phone, e.getMessage());
        }
        
        return phone;
    }
    
    /**
     * 邮箱脱敏
     * 规则：用户名保留首尾，域名完整
     * 示例：zhangsan@example.com -> z***n@example.com
     * 
     * @param email 原始邮箱
     * @return 脱敏后的邮箱
     */
    public static String maskEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return email;
        }
        
        try {
            int atIndex = email.indexOf("@");
            if (atIndex <= 0) {
                return email;
            }
            
            String username = email.substring(0, atIndex);
            String domain = email.substring(atIndex);
            
            // 用户名脱敏
            String maskedUsername;
            if (username.length() == 1) {
                maskedUsername = username;
            } else if (username.length() == 2) {
                maskedUsername = username.charAt(0) + "*";
            } else {
                maskedUsername = username.charAt(0) + "***" + username.charAt(username.length() - 1);
            }
            
            return maskedUsername + domain;
            
        } catch (Exception e) {
            logger.warn("邮箱脱敏异常: email={}, error={}", email, e.getMessage());
        }
        
        return email;
    }
    
    /**
     * 身份证号脱敏
     * 规则：保留前6后4位，中间替换为********
     * 示例：110101199001011234 -> 110101********1234
     * 
     * @param idCard 原始身份证号
     * @return 脱敏后的身份证号
     */
    public static String maskIdCard(String idCard) {
        if (!StringUtils.hasText(idCard)) {
            return idCard;
        }
        
        try {
            // 移除所有空格
            idCard = idCard.replaceAll("\\s", "");
            
            // 标准18位身份证号
            if (idCard.length() == 18) {
                return idCard.substring(0, 6) + "********" + idCard.substring(14);
            }
            
            // 15位身份证号
            if (idCard.length() == 15) {
                return idCard.substring(0, 6) + "*****" + idCard.substring(11);
            }
            
        } catch (Exception e) {
            logger.warn("身份证号脱敏异常: idCard={}, error={}", idCard, e.getMessage());
        }
        
        return idCard;
    }
    
    /**
     * 工号脱敏
     * 规则：保留前4位，其余替换为***
     * 示例：P001234 -> P001***
     * 
     * @param employeeNo 原始工号
     * @return 脱敏后的工号
     */
    public static String maskEmployeeNo(String employeeNo) {
        if (!StringUtils.hasText(employeeNo)) {
            return employeeNo;
        }
        
        try {
            if (employeeNo.length() <= 4) {
                return employeeNo;
            }
            
            return employeeNo.substring(0, 4) + "***";
            
        } catch (Exception e) {
            logger.warn("工号脱敏异常: employeeNo={}, error={}", employeeNo, e.getMessage());
        }
        
        return employeeNo;
    }
    
    /**
     * 地址脱敏
     * 规则：保留到区级，其余替换为***
     * 示例：北京市朝阳区xx路xx号 -> 北京市朝阳区***
     * 
     * @param address 原始地址
     * @return 脱敏后的地址
     */
    public static String maskAddress(String address) {
        if (!StringUtils.hasText(address)) {
            return address;
        }
        
        try {
            // 查找区/县的位置
            int districtIndex = -1;
            
            // 常见的区级行政单位后缀
            String[] districtSuffixes = {"区", "县", "市", "旗", "自治县", "自治旗"};
            
            for (String suffix : districtSuffixes) {
                int index = address.indexOf(suffix);
                if (index > 0) {
                    districtIndex = index + suffix.length();
                    break;
                }
            }
            
            if (districtIndex > 0 && districtIndex < address.length()) {
                return address.substring(0, districtIndex) + "***";
            }
            
            // 如果没有找到区级单位，保留前10个字符
            if (address.length() > 10) {
                return address.substring(0, 10) + "***";
            }
            
        } catch (Exception e) {
            logger.warn("地址脱敏异常: address={}, error={}", address, e.getMessage());
        }
        
        return address;
    }
    
    /**
     * 银行卡号脱敏
     * 规则：保留前4后4位，中间替换为****
     * 示例：6222021234567890123 -> 6222************123
     * 
     * @param bankCard 原始银行卡号
     * @return 脱敏后的银行卡号
     */
    public static String maskBankCard(String bankCard) {
        if (!StringUtils.hasText(bankCard)) {
            return bankCard;
        }
        
        try {
            // 移除所有空格
            bankCard = bankCard.replaceAll("\\s", "");
            
            if (bankCard.length() >= 8) {
                int maskLength = bankCard.length() - 7;
                StringBuilder masked = new StringBuilder(bankCard.substring(0, 4));
                for (int i = 0; i < maskLength; i++) {
                    masked.append("*");
                }
                masked.append(bankCard.substring(bankCard.length() - 3));
                return masked.toString();
            }
            
        } catch (Exception e) {
            logger.warn("银行卡号脱敏异常: bankCard={}, error={}", bankCard, e.getMessage());
        }
        
        return bankCard;
    }
    
    /**
     * 通用脱敏
     * 规则：保留前n后m位，中间替换为指定字符
     * 
     * @param str 原始字符串
     * @param keepStart 保留开始位数
     * @param keepEnd 保留结束位数
     * @param maskChar 脱敏字符
     * @return 脱敏后的字符串
     */
    public static String mask(String str, int keepStart, int keepEnd, char maskChar) {
        if (!StringUtils.hasText(str)) {
            return str;
        }
        
        try {
            int length = str.length();
            
            // 字符串长度不足以脱敏
            if (length <= keepStart + keepEnd) {
                return str;
            }
            
            StringBuilder masked = new StringBuilder();
            
            // 保留开始部分
            masked.append(str.substring(0, keepStart));
            
            // 中间替换为脱敏字符
            int maskLength = length - keepStart - keepEnd;
            for (int i = 0; i < maskLength; i++) {
                masked.append(maskChar);
            }
            
            // 保留结束部分
            masked.append(str.substring(length - keepEnd));
            
            return masked.toString();
            
        } catch (Exception e) {
            logger.warn("通用脱敏异常: str={}, error={}", str, e.getMessage());
        }
        
        return str;
    }
    
    /**
     * 判断是否是复姓
     * 
     * @param surname 姓氏（2个字）
     * @return true表示是复姓，false表示不是
     */
    private static boolean isCompoundSurname(String surname) {
        if (surname == null || surname.length() != 2) {
            return false;
        }
        
        // 常见的复姓列表
        String[] compoundSurnames = {
            "欧阳", "太史", "端木", "上官", "司马", "东方", "独孤", "南宫", "万俟",
            "闻人", "夏侯", "诸葛", "尉迟", "公羊", "赫连", "澹台", "皇甫", "宗政",
            "濮阳", "公冶", "太叔", "申屠", "公孙", "慕容", "仲孙", "钟离", "长孙",
            "宇文", "司徒", "鲜于", "司空", "闾丘", "子车", "亓官", "司寇", "巫马",
            "公西", "颛孙", "壤驷", "公良", "漆雕", "乐正", "宰父", "谷梁", "拓跋",
            "夹谷", "轩辕", "令狐", "段干", "百里", "呼延", "东郭", "南门", "羊舌",
            "微生", "公户", "公玉", "公仪", "梁丘", "公仲", "公上", "公门", "公山",
            "公坚", "左丘", "公伯", "西门", "公祖", "第五", "公乘", "贯丘", "公皙"
        };
        
        for (String compoundSurname : compoundSurnames) {
            if (compoundSurname.equals(surname)) {
                return true;
            }
        }
        
        return false;
    }
}
