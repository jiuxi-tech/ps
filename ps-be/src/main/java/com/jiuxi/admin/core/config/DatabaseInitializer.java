package com.jiuxi.admin.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 数据库初始化器
 * 用于在应用启动时检查并创建必要的数据库表
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // 检查org_tree_change_history表是否存在
            String checkTableSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'org_tree_change_history'";
            Integer tableCount = jdbcTemplate.queryForObject(checkTableSql, Integer.class);
            
            if (tableCount == null || tableCount == 0) {
                logger.info("org_tree_change_history表不存在，开始创建...");
                createOrgTreeChangeHistoryTable();
                logger.info("org_tree_change_history表创建成功");
            } else {
                logger.info("org_tree_change_history表已存在");
                // 检查表结构是否完整
                checkTableStructure();
            }
        } catch (Exception e) {
            logger.error("数据库初始化失败", e);
        }
    }

    private void createOrgTreeChangeHistoryTable() {
        String createTableSql = "CREATE TABLE `org_tree_change_history` (" +
                "`id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '操作ID（主键自增）'," +
                "`operation_type` ENUM('CREATE','UPDATE','DELETE','QUERY') NOT NULL COMMENT '操作类型'," +
                "`operation_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间'," +
                "`operator_id` BIGINT NOT NULL COMMENT '操作用户ID（外键关联用户表）'," +
                "`before_data` LONGTEXT COMMENT '变更前的组织机构树JSON'," +
                "`after_data` LONGTEXT COMMENT '变更后的组织机构树JSON'," +
                "`before_full_tree` LONGTEXT COMMENT '变更前的完整组织机构节点树JSON'," +
                "`after_full_tree` LONGTEXT COMMENT '变更后的完整组织机构节点树JSON'," +
                "`version` BIGINT DEFAULT 1 COMMENT '版本号（数据库字段，不参与业务逻辑，用ID代替版本功能）'," +
                "`dept_id` BIGINT DEFAULT 0 COMMENT '部门ID（数据库字段，不参与业务逻辑）'" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织机构树变更历史记录表'";
        
        jdbcTemplate.execute(createTableSql);
        
        // 创建索引
        jdbcTemplate.execute("CREATE INDEX `idx_operation_time` ON `org_tree_change_history`(`operation_time`)");
        jdbcTemplate.execute("CREATE INDEX `idx_operator_id` ON `org_tree_change_history`(`operator_id`)");
        jdbcTemplate.execute("CREATE INDEX `idx_type_time` ON `org_tree_change_history`(`operation_type`, `operation_time`)");
        jdbcTemplate.execute("CREATE INDEX `idx_operator_time` ON `org_tree_change_history`(`operator_id`, `operation_time`)");
    }

    private void checkTableStructure() {
        try {
            // 检查version字段是否存在
            String checkVersionSql = "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'org_tree_change_history' AND column_name = 'version'";
            Integer versionCount = jdbcTemplate.queryForObject(checkVersionSql, Integer.class);
            
            if (versionCount == null || versionCount == 0) {
                logger.info("添加version字段...");
                jdbcTemplate.execute("ALTER TABLE org_tree_change_history ADD COLUMN `version` BIGINT DEFAULT 1 COMMENT '版本号（数据库字段，不参与业务逻辑，用ID代替版本功能）'");
            }
            
            // 检查dept_id字段是否存在
            String checkDeptIdSql = "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'org_tree_change_history' AND column_name = 'dept_id'";
            Integer deptIdCount = jdbcTemplate.queryForObject(checkDeptIdSql, Integer.class);
            
            if (deptIdCount == null || deptIdCount == 0) {
                logger.info("添加dept_id字段...");
                jdbcTemplate.execute("ALTER TABLE org_tree_change_history ADD COLUMN `dept_id` BIGINT DEFAULT 0 COMMENT '部门ID（数据库字段，不参与业务逻辑）'");
            }
            
            logger.info("表结构检查完成");
        } catch (Exception e) {
            logger.error("表结构检查失败", e);
        }
    }
}