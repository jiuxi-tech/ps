package com.jiuxi.admin.core.controller;

import com.jiuxi.common.bean.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * SQL执行控制器（临时用于创建表）
 */
@RestController
@RequestMapping("/api/sql")
public class SqlExecutorController {

    private static final Logger logger = LoggerFactory.getLogger(SqlExecutorController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 执行SQL语句
     */
    @PostMapping("/execute")
    public JsonResponse executeSql(@RequestBody Map<String, String> request) {
        try {
            String sql = request.get("sql");
            if (sql == null || sql.trim().isEmpty()) {
                return JsonResponse.build(-1, "SQL语句不能为空");
            }

            // 分割SQL语句并逐个执行
            String[] sqlStatements = sql.split(";");
            int executedCount = 0;
            
            for (String statement : sqlStatements) {
                statement = statement.trim();
                if (!statement.isEmpty() && !statement.startsWith("--")) {
                    try {
                        jdbcTemplate.execute(statement);
                        executedCount++;
                        logger.info("执行SQL成功: {}", statement.substring(0, Math.min(50, statement.length())));
                    } catch (Exception e) {
                        logger.warn("执行SQL失败: {}, 错误: {}", statement.substring(0, Math.min(50, statement.length())), e.getMessage());
                        // 继续执行下一条SQL
                    }
                }
            }
            
            return JsonResponse.build(0, "SQL执行完成，成功执行 " + executedCount + " 条语句");
        } catch (Exception e) {
            logger.error("执行SQL时发生错误", e);
            return JsonResponse.build(-1, "执行SQL失败: " + e.getMessage());
        }
    }
}