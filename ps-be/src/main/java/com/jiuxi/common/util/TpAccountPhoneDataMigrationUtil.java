package com.jiuxi.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * tp_account表手机号数据迁移工具
 * 将明文手机号加密存储
 * 
 * @author system
 * @date 2024-01-20
 */
@Component
public class TpAccountPhoneDataMigrationUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TpAccountPhoneDataMigrationUtil.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 迁移tp_account表中所有明文手机号数据
     * 将明文PHONE字段加密后存储
     * 
     * @return 迁移成功的记录数
     */
    public int migrateAccountPhoneData() {
        LOGGER.info("开始执行tp_account表手机号数据迁移...");
        
        try {
            // 查询所有需要加密的手机号记录（非空且未加密的）
            String selectSql = "SELECT ACCOUNT_ID, PHONE FROM tp_account " +
                             "WHERE PHONE IS NOT NULL AND PHONE != '' AND ACTIVED = 1";
            
            List<Map<String, Object>> records = jdbcTemplate.queryForList(selectSql);
            LOGGER.info("找到 {} 条需要迁移的tp_account手机号记录", records.size());
            
            int successCount = 0;
            int errorCount = 0;
            
            for (Map<String, Object> record : records) {
                String accountId = (String) record.get("ACCOUNT_ID");
                String phone = (String) record.get("PHONE");
                
                try {
                    // 检查是否已经是加密数据（通过尝试解密来判断）
                    if (isAlreadyEncrypted(phone)) {
                        LOGGER.debug("手机号已加密，跳过：accountId={}", accountId);
                        continue;
                    }
                    
                    // 加密手机号
                    String encryptedPhone = PhoneEncryptionUtils.encrypt(phone);
                    
                    // 更新数据库记录
                    String updateSql = "UPDATE tp_account SET PHONE = ? WHERE ACCOUNT_ID = ?";
                    int updateCount = jdbcTemplate.update(updateSql, encryptedPhone, accountId);
                    
                    if (updateCount > 0) {
                        successCount++;
                        LOGGER.debug("成功加密tp_account手机号：accountId={}, 原手机号={}", accountId, phone);
                    } else {
                        errorCount++;
                        LOGGER.error("更新tp_account手机号失败：accountId={}", accountId);
                    }
                    
                } catch (Exception e) {
                    errorCount++;
                    LOGGER.error("处理tp_account手机号加密失败：accountId={}, phone={}, 错误：{}", 
                               accountId, phone, e.getMessage(), e);
                }
            }
            
            LOGGER.info("tp_account表手机号数据迁移完成！成功：{} 条，失败：{} 条", successCount, errorCount);
            return successCount;
            
        } catch (Exception e) {
            LOGGER.error("tp_account表手机号数据迁移过程中发生错误：{}", e.getMessage(), e);
            throw new RuntimeException("tp_account表手机号数据迁移失败", e);
        }
    }
    
    /**
     * 检查手机号是否已经加密
     * 通过尝试解密来判断
     * 
     * @param phone 手机号
     * @return true-已加密，false-未加密
     */
    private boolean isAlreadyEncrypted(String phone) {
        try {
            // 如果能成功解密，说明已经是加密数据
            String decrypted = PhoneEncryptionUtils.decrypt(phone);
            // 检查解密后的数据是否像手机号（11位数字）
            return decrypted != null && decrypted.matches("^1[3-9]\\d{9}$");
        } catch (Exception e) {
            // 解密失败，说明是明文数据
            return false;
        }
    }
    
    /**
     * 验证迁移结果
     * 检查是否还有明文手机号数据
     * 
     * @return 验证结果信息
     */
    public String validateMigration() {
        try {
            String selectSql = "SELECT COUNT(*) FROM tp_account " +
                             "WHERE PHONE IS NOT NULL AND PHONE != '' AND ACTIVED = 1";
            
            int totalCount = jdbcTemplate.queryForObject(selectSql, Integer.class);
            
            int encryptedCount = 0;
            int plaintextCount = 0;
            
            // 检查每条记录的加密状态
            String detailSql = "SELECT ACCOUNT_ID, PHONE FROM tp_account " +
                             "WHERE PHONE IS NOT NULL AND PHONE != '' AND ACTIVED = 1";
            
            List<Map<String, Object>> records = jdbcTemplate.queryForList(detailSql);
            
            for (Map<String, Object> record : records) {
                String phone = (String) record.get("PHONE");
                if (isAlreadyEncrypted(phone)) {
                    encryptedCount++;
                } else {
                    plaintextCount++;
                }
            }
            
            String result = String.format(
                "tp_account表手机号迁移验证结果：总记录数=%d, 已加密=%d, 明文=%d", 
                totalCount, encryptedCount, plaintextCount
            );
            
            LOGGER.info(result);
            return result;
            
        } catch (Exception e) {
            String errorMsg = "tp_account表手机号迁移验证失败：" + e.getMessage();
            LOGGER.error(errorMsg, e);
            return errorMsg;
        }
    }
}