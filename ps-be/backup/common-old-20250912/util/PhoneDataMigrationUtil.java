package com.jiuxi.common.util;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 手机号数据迁移工具类
 * 用于将tp_person_basicinfo表中的明文手机号加密
 * 
 * @author System
 * @date 2024-01-20
 */
@Component
public class PhoneDataMigrationUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneDataMigrationUtil.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 迁移所有明文手机号数据
     * 将tp_person_basicinfo表中的明文PHONE字段加密后存储
     * 
     * @return 迁移成功的记录数
     */
    public int migratePhoneData() {
        LOGGER.info("开始执行手机号数据迁移...");
        
        try {
            // 查询所有需要加密的手机号记录（非空且未加密的）
            String selectSql = "SELECT PERSON_ID, PHONE FROM tp_person_basicinfo " +
                             "WHERE PHONE IS NOT NULL AND PHONE != '' AND ACTIVED = 1";
            
            List<Map<String, Object>> records = jdbcTemplate.queryForList(selectSql);
            LOGGER.info("找到 {} 条需要迁移的手机号记录", records.size());
            
            int successCount = 0;
            int errorCount = 0;
            
            for (Map<String, Object> record : records) {
                String personId = (String) record.get("PERSON_ID");
                String phone = (String) record.get("PHONE");
                
                try {
                    // 检查是否已经是加密数据（通过尝试解密来判断）
                    if (isAlreadyEncrypted(phone)) {
                        LOGGER.debug("手机号已加密，跳过：personId={}", personId);
                        continue;
                    }
                    
                    // 加密手机号
                    String encryptedPhone = PhoneEncryptionUtils.encrypt(phone);
                    
                    // 更新数据库
                    String updateSql = "UPDATE tp_person_basicinfo SET PHONE = ? WHERE PERSON_ID = ?";
                    int updateCount = jdbcTemplate.update(updateSql, encryptedPhone, personId);
                    
                    if (updateCount > 0) {
                        successCount++;
                        LOGGER.debug("成功加密手机号：personId={}, 原手机号={}", personId, maskPhone(phone));
                    } else {
                        errorCount++;
                        LOGGER.error("更新失败：personId={}", personId);
                    }
                    
                } catch (Exception e) {
                    errorCount++;
                    LOGGER.error("加密手机号失败：personId={}, phone={}, error={}", 
                               personId, maskPhone(phone), e.getMessage());
                }
            }
            
            LOGGER.info("手机号数据迁移完成！成功：{} 条，失败：{} 条", successCount, errorCount);
            return successCount;
            
        } catch (Exception e) {
            LOGGER.error("手机号数据迁移异常：{}", e.getMessage(), e);
            throw new RuntimeException("手机号数据迁移失败", e);
        }
    }
    
    /**
     * 检查手机号是否已经加密
     * 通过尝试解密来判断是否为加密数据
     * 
     * @param phone 手机号
     * @return true-已加密，false-未加密
     */
    private boolean isAlreadyEncrypted(String phone) {
        if (StrUtil.isBlank(phone)) {
            return false;
        }
        
        try {
            // 如果是明文手机号，通常是11位数字
            if (phone.matches("^1[3-9]\\d{9}$")) {
                return false;
            }
            
            // 尝试解密，如果解密成功且结果是有效手机号，说明已加密
            String decrypted = PhoneEncryptionUtils.decrypt(phone);
            return StrUtil.isNotBlank(decrypted) && decrypted.matches("^1[3-9]\\d{9}$");
            
        } catch (Exception e) {
            // 解密失败，可能是明文数据或损坏数据
            return false;
        }
    }
    
    /**
     * 掩码显示手机号（用于日志输出）
     * 
     * @param phone 手机号
     * @return 掩码后的手机号
     */
    private String maskPhone(String phone) {
        if (StrUtil.isBlank(phone) || phone.length() < 7) {
            return "***";
        }
        
        if (phone.length() == 11) {
            return phone.substring(0, 3) + "****" + phone.substring(7);
        } else {
            return phone.substring(0, 3) + "***";
        }
    }
    
    /**
     * 回滚手机号数据（将加密数据解密回明文）
     * 注意：此方法仅用于测试或紧急回滚，生产环境慎用
     * 
     * @return 回滚成功的记录数
     */
    public int rollbackPhoneData() {
        LOGGER.warn("开始执行手机号数据回滚（解密）...");
        
        try {
            String selectSql = "SELECT PERSON_ID, PHONE FROM tp_person_basicinfo " +
                             "WHERE PHONE IS NOT NULL AND PHONE != '' AND ACTIVED = 1";
            
            List<Map<String, Object>> records = jdbcTemplate.queryForList(selectSql);
            LOGGER.info("找到 {} 条记录进行回滚检查", records.size());
            
            int successCount = 0;
            int errorCount = 0;
            
            for (Map<String, Object> record : records) {
                String personId = (String) record.get("PERSON_ID");
                String phone = (String) record.get("PHONE");
                
                try {
                    // 检查是否为加密数据
                    if (!isAlreadyEncrypted(phone)) {
                        LOGGER.debug("手机号未加密，跳过：personId={}", personId);
                        continue;
                    }
                    
                    // 解密手机号
                    String decryptedPhone = PhoneEncryptionUtils.decrypt(phone);
                    
                    // 更新数据库
                    String updateSql = "UPDATE tp_person_basicinfo SET PHONE = ? WHERE PERSON_ID = ?";
                    int updateCount = jdbcTemplate.update(updateSql, decryptedPhone, personId);
                    
                    if (updateCount > 0) {
                        successCount++;
                        LOGGER.debug("成功解密手机号：personId={}", personId);
                    } else {
                        errorCount++;
                        LOGGER.error("回滚更新失败：personId={}", personId);
                    }
                    
                } catch (Exception e) {
                    errorCount++;
                    LOGGER.error("解密手机号失败：personId={}, error={}", personId, e.getMessage());
                }
            }
            
            LOGGER.warn("手机号数据回滚完成！成功：{} 条，失败：{} 条", successCount, errorCount);
            return successCount;
            
        } catch (Exception e) {
            LOGGER.error("手机号数据回滚异常：{}", e.getMessage(), e);
            throw new RuntimeException("手机号数据回滚失败", e);
        }
    }
    
    /**
     * 统计当前数据库中手机号的加密状态
     * 
     * @return 统计信息
     */
    public String getPhoneEncryptionStatus() {
        try {
            String selectSql = "SELECT PERSON_ID, PHONE FROM tp_person_basicinfo " +
                             "WHERE PHONE IS NOT NULL AND PHONE != '' AND ACTIVED = 1";
            
            List<Map<String, Object>> records = jdbcTemplate.queryForList(selectSql);
            
            int totalCount = records.size();
            int encryptedCount = 0;
            int plaintextCount = 0;
            
            for (Map<String, Object> record : records) {
                String phone = (String) record.get("PHONE");
                
                if (isAlreadyEncrypted(phone)) {
                    encryptedCount++;
                } else {
                    plaintextCount++;
                }
            }
            
            return String.format("手机号加密状态统计 - 总计：%d，已加密：%d，明文：%d", 
                               totalCount, encryptedCount, plaintextCount);
            
        } catch (Exception e) {
            LOGGER.error("统计手机号加密状态失败：{}", e.getMessage(), e);
            return "统计失败：" + e.getMessage();
        }
    }

    public String validateMigration() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateMigration'");
    }
}