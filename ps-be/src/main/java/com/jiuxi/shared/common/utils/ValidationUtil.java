package com.jiuxi.shared.common.utils;

import java.util.regex.Pattern;

/**
 * 数据校验工具类
 * 提供各种业务数据的格式校验功能
 * 
 * @author User Import Refactor
 * @date 2025-11-27
 */
public class ValidationUtil {

    /**
     * 账号名正则：字母、数字、下划线组合，长度4-20位
     */
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{4,20}$");

    /**
     * 参加工作时间正则：YYYY.MM格式
     */
    private static final Pattern WORK_DATE_PATTERN = Pattern.compile("^\\d{4}\\.\\d{2}$");

    /**
     * 身份证号正则：15位或18位
     */
    private static final Pattern IDCARD_PATTERN = Pattern.compile("^(\\d{15}|\\d{17}[0-9Xx])$");

    /**
     * 身份证号加权因子
     */
    private static final int[] IDCARD_WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 身份证号校验码
     */
    private static final char[] IDCARD_CHECK_CODES = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * 校验账号名格式
     * 
     * @param username 账号名
     * @return 校验结果，null表示通过，否则返回错误信息
     */
    public static String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "账号名不能为空";
        }
        
        username = username.trim();
        
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return "账号名格式错误，只能包含字母、数字、下划线，长度4-20位";
        }
        
        return null;
    }

    /**
     * 校验密码
     * 
     * @param password 密码
     * @return 校验结果，null表示通过，否则返回错误信息
     */
    public static String validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "密码不能为空";
        }
        
        if (password.trim().length() < 6) {
            return "密码长度不能少于6位";
        }
        
        return null;
    }

    /**
     * 校验姓名
     * 
     * @param personName 姓名
     * @return 校验结果，null表示通过，否则返回错误信息
     */
    public static String validatePersonName(String personName) {
        if (personName == null || personName.trim().isEmpty()) {
            return "姓名不能为空";
        }
        
        if (personName.trim().length() > 100) {
            return "姓名长度不能超过100个字符";
        }
        
        return null;
    }

    /**
     * 校验性别
     * 
     * @param sex 性别
     * @return 校验结果，null表示通过，否则返回错误信息
     */
    public static String validateSex(String sex) {
        if (sex == null || sex.trim().isEmpty()) {
            // 性别非必填，空值表示保密
            return null;
        }
        
        sex = sex.trim();
        if (!"男".equals(sex) && !"女".equals(sex)) {
            return "性别只能为[男]或[女]";
        }
        
        return null;
    }

    /**
     * 校验部门路径
     * 
     * @param deptPath 部门路径
     * @return 校验结果，null表示通过，否则返回错误信息
     */
    public static String validateDeptPath(String deptPath) {
        if (deptPath == null || deptPath.trim().isEmpty()) {
            return "部门不能为空";
        }
        
        if (deptPath.trim().length() > 500) {
            return "部门路径长度不能超过500个字符";
        }
        
        return null;
    }

    /**
     * 校验参加工作时间
     * 支持三种格式：
     * - YYYY：年，如2020
     * - YYYY-MM：年-月，如2020-01
     * - YYYY-MM-DD：年-月-日，如1988-08-15
     * 
     * @param partWorkDate 参加工作时间
     * @return 校验结果，null表示通过，否则返回错误信息
     */
    public static String validatePartWorkDate(String partWorkDate) {
        if (partWorkDate == null || partWorkDate.trim().isEmpty()) {
            // 参加工作时间非必填
            return null;
        }
        
        partWorkDate = partWorkDate.trim();
        
        // 判断格式类型
        String[] parts = partWorkDate.split("-");
        int year, month = 1, day = 1;
        
        try {
            if (parts.length == 1) {
                // 格式1: YYYY (只有年)
                if (!partWorkDate.matches("^\\d{4}$")) {
                    return "参加工作时间格式错误，应为YYYY、YYYY-MM或YYYY-MM-DD格式";
                }
                year = Integer.parseInt(parts[0]);
                
            } else if (parts.length == 2) {
                // 格式2: YYYY-MM (年-月)
                if (!partWorkDate.matches("^\\d{4}-\\d{2}$")) {
                    return "参加工作时间格式错误，应为YYYY、YYYY-MM或YYYY-MM-DD格式";
                }
                year = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]);
                
                // 校验月份范围
                if (month < 1 || month > 12) {
                    return "参加工作时间的月份应在01-12之间";
                }
                
            } else if (parts.length == 3) {
                // 格式3: YYYY-MM-DD (年-月-日)
                if (!partWorkDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    return "参加工作时间格式错误，应为YYYY、YYYY-MM或YYYY-MM-DD格式";
                }
                year = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]);
                day = Integer.parseInt(parts[2]);
                
                // 校验月份范围
                if (month < 1 || month > 12) {
                    return "参加工作时间的月份应在01-12之间";
                }
                
                // 校验日期范围
                if (day < 1 || day > 31) {
                    return "参加工作时间的日期应在01-31之间";
                }
                
                // 使用Java日期API验证日期是否合法（例如2001-02-30是非法的）
                try {
                    java.time.LocalDate.of(year, month, day);
                } catch (Exception e) {
                    return "参加工作时间的日期不合法";
                }
                
            } else {
                return "参加工作时间格式错误，应为YYYY、YYYY-MM或YYYY-MM-DD格式";
            }
            
            // 校验年份合理性
            int currentYear = java.time.Year.now().getValue();
            if (year < 1900 || year > currentYear) {
                return "参加工作时间的年份应在1900-" + currentYear + "之间";
            }
            
        } catch (NumberFormatException e) {
            return "参加工作时间格式错误，应为YYYY、YYYY-MM或YYYY-MM-DD格式";
        }
        
        return null;
    }

    /**
     * 校验身份证号码
     * 
     * @param idcard 身份证号码
     * @return 校验结果，null表示通过，否则返回错误信息
     */
    public static String validateIdcard(String idcard) {
        if (idcard == null || idcard.trim().isEmpty()) {
            // 身份证号非必填
            return null;
        }
        
        idcard = idcard.trim().toUpperCase();
        
        // 格式校验
        if (!IDCARD_PATTERN.matcher(idcard).matches()) {
            return "身份证号码格式错误，应为15位或18位";
        }
        
        // 18位身份证号校验码验证
        if (idcard.length() == 18) {
            if (!validateIdcard18CheckCode(idcard)) {
                return "身份证号码校验位错误";
            }
            
            // 校验日期部分
            String birthDate = idcard.substring(6, 14);
            if (!validateBirthDate(birthDate)) {
                return "身份证号码中的出生日期不合法";
            }
        } else if (idcard.length() == 15) {
            // 校验15位身份证的日期部分
            String birthDate = "19" + idcard.substring(6, 12);
            if (!validateBirthDate(birthDate)) {
                return "身份证号码中的出生日期不合法";
            }
        }
        
        return null;
    }

    /**
     * 校验18位身份证号的校验码
     */
    private static boolean validateIdcard18CheckCode(String idcard) {
        if (idcard.length() != 18) {
            return false;
        }
        
        // 计算校验码
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char ch = idcard.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
            sum += (ch - '0') * IDCARD_WEIGHTS[i];
        }
        
        int mod = sum % 11;
        char checkCode = IDCARD_CHECK_CODES[mod];
        
        return checkCode == idcard.charAt(17);
    }

    /**
     * 校验出生日期（格式：yyyyMMdd）
     */
    private static boolean validateBirthDate(String birthDate) {
        if (birthDate.length() != 8) {
            return false;
        }
        
        try {
            int year = Integer.parseInt(birthDate.substring(0, 4));
            int month = Integer.parseInt(birthDate.substring(4, 6));
            int day = Integer.parseInt(birthDate.substring(6, 8));
            
            // 基本范围校验
            if (year < 1900 || year > java.time.Year.now().getValue()) {
                return false;
            }
            if (month < 1 || month > 12) {
                return false;
            }
            if (day < 1 || day > 31) {
                return false;
            }
            
            // 使用Java日期API进行严格校验
            java.time.LocalDate.of(year, month, day);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验职务职级
     * 
     * @param position 职务职级
     * @return 校验结果，null表示通过，否则返回错误信息
     */
    public static String validateRank(String rank) {
        if (rank == null || rank.trim().isEmpty()) {
            // 职务职级非必填
            return null;
        }
        
        if (rank.trim().length() > 100) {
            return "职务职级长度不能超过100个字符";
        }
        
        return null;
    }

    /**
     * 校验职称名称
     * 
     * @param titleName 职称名称
     * @return 校验结果，null表示通过，否则返回错误信息
     */
    public static String validateTitleName(String titleName) {
        if (titleName == null || titleName.trim().isEmpty()) {
            // 职称非必填
            return null;
        }
        
        if (titleName.trim().length() > 100) {
            return "职称名称长度不能超过100个字符";
        }
        
        return null;
    }

    /**
     * 判断字符串是否为空
     * 
     * @param str 字符串
     * @return true表示为空（null或空白字符串）
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否非空
     * 
     * @param str 字符串
     * @return true表示非空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 将多种日期格式转换为 YYYYMMDD 格式
     * 支持输入格式：
     * - YYYY -> YYYY0101
     * - YYYY-MM -> YYYYMM01
     * - YYYY-MM-DD -> YYYYMMDD
     * 
     * @param date 多种格式的日期
     * @return YYYYMMDD 格式，如果输入为空则返回空
     */
    public static String formatDateToYYYYMMDD(String date) {
        if (date == null || date.trim().isEmpty()) {
            return "";
        }
        
        date = date.trim();
        
        // 如果已经是 YYYYMMDD 格式，直接返回
        if (date.matches("^\\d{8}$")) {
            return date;
        }
        
        // 判断格式类型
        String[] parts = date.split("-");
        
        if (parts.length == 1 && date.matches("^\\d{4}$")) {
            // YYYY -> YYYY0101
            return date + "0101";
            
        } else if (parts.length == 2 && date.matches("^\\d{4}-\\d{2}$")) {
            // YYYY-MM -> YYYYMM01
            return date.replace("-", "") + "01";
            
        } else if (parts.length == 3 && date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            // YYYY-MM-DD -> YYYYMMDD
            return date.replace("-", "");
        }
        
        return date; // 其他格式直接返回
    }
    
    /**
     * 将 YYYYMMDD 格式转换为 YYYY-MM-DD 格式
     * 
     * @param date YYYYMMDD 格式的日期
     * @return YYYY-MM-DD 格式，如果输入为空或格式不匹配则返回原值
     */
    public static String formatDateToYYYYMMDD_Hyphen(String date) {
        if (date == null || date.trim().isEmpty()) {
            return "";
        }
        
        date = date.trim();
        
        // 如果已经是 YYYY-MM-DD 格式，直接返回
        if (date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return date;
        }
        
        // 如果是 YYYYMMDD 格式，转换为 YYYY-MM-DD
        if (date.matches("^\\d{8}$")) {
            return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
        }
        
        return date; // 其他格式直接返回
    }
}
