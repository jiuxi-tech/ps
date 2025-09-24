package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpDatabaseBackupLogQuery;
import com.jiuxi.admin.core.bean.vo.TpDatabaseBackupLogVO;
import com.jiuxi.admin.core.service.DatabaseBackupService;
import com.jiuxi.admin.core.service.TpDatabaseBackupLogService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map;

/**
 * @ClassName: TpDatabaseBackupController
 * @Description: 数据库备份管理控制器
 * @Author: system
 * @Date: 2025-09-24
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/database-backup")
@Authorization
public class TpDatabaseBackupController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "backupId";

    @Autowired
    private TpDatabaseBackupLogService tpDatabaseBackupLogService;

    @Autowired
    private DatabaseBackupService databaseBackupService;

    /**
     * 备份记录列表（分页查询）
     */
    @RequestMapping("/list")
    public JsonResponse list(TpDatabaseBackupLogQuery query, String jwtpid, String jwtaid) {
        IPage<TpDatabaseBackupLogVO> page = tpDatabaseBackupLogService.queryPage(query, jwtpid, jwtaid);
        return JsonResponse.buildSuccess(page).buildPassKey(jwtpid, PASS_KEY);
    }

    /**
     * 备份记录列表（不分页）
     */
    @RequestMapping("/all-list")
    @IgnoreAuthorization
    public JsonResponse allList(TpDatabaseBackupLogQuery query, String jwtpid, String jwtaid) {
        List<TpDatabaseBackupLogVO> list = tpDatabaseBackupLogService.getList(query, jwtpid, jwtaid);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 查看备份记录详情
     */
    @RequestMapping(value = "/view")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse view(String backupId) {
        TpDatabaseBackupLogVO vo = tpDatabaseBackupLogService.view(backupId);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 删除备份记录
     */
    @RequestMapping("/delete")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse delete(@RequestBody Map<String, Object> requestBody, String jwtpid) {
        String backupId = (String) requestBody.get("backupId");
        
        if (backupId == null || backupId.trim().isEmpty()) {
            return JsonResponse.buildFailure("备份记录ID不能为空");
        }
        
        try {
            int count = tpDatabaseBackupLogService.delete(backupId, jwtpid);
            if (count > 0) {
                return JsonResponse.buildSuccess("删除成功");
            } else {
                return JsonResponse.buildFailure("删除失败，记录不存在或已被删除");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("删除失败: " + e.getMessage());
        }
    }

    /**
     * 手动执行数据库备份
     */
    @RequestMapping("/manual-backup")
    public JsonResponse manualBackup(@RequestBody Map<String, Object> requestBody, String jwtpid) {
        try {
            // 执行手动备份（备份类型为2）
            // 数据库名称从系统配置中获取，不再通过参数传递
            TpDatabaseBackupLogVO result = databaseBackupService.executeBackup(2, jwtpid, jwtpid);
            return JsonResponse.buildSuccess(result);
        } catch (Exception e) {
            // 添加异常处理，避免返回500错误
            return JsonResponse.buildFailure("执行备份失败: " + e.getMessage());
        }
    }

    /**
     * 停止备份任务
     */
    @RequestMapping("/stop-backup")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse stopBackup(@RequestBody Map<String, Object> requestBody, String jwtpid) {
        String backupId = (String) requestBody.get("backupId");
        
        if (backupId == null || backupId.trim().isEmpty()) {
            return JsonResponse.buildFailure("备份记录ID不能为空");
        }
        
        boolean success = databaseBackupService.stopBackup(backupId, jwtpid);
        if (success) {
            return JsonResponse.buildSuccess("备份任务已成功停止");
        } else {
            return JsonResponse.buildFailure("停止备份任务失败");
        }
    }

    /**
     * 检查备份配置
     */
    @RequestMapping("/check-config")
    @IgnoreAuthorization
    public JsonResponse checkConfig() {
        boolean isValid = databaseBackupService.isBackupConfigValid();
        String configInfo = databaseBackupService.getBackupConfigInfo();
        
        Map<String, Object> result = new HashMap<>();
        result.put("isValid", isValid);
        result.put("configInfo", configInfo);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 获取最近的备份记录
     */
    @RequestMapping("/recent-backups")
    @IgnoreAuthorization
    public JsonResponse getRecentBackups(String databaseName, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认获取最近10条记录
        }
        if (databaseName == null || databaseName.trim().isEmpty()) {
            databaseName = "ps-bmp"; // 默认数据库名称
        }
        
        List<TpDatabaseBackupLogVO> list = tpDatabaseBackupLogService.getRecentBackups(databaseName, limit);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 获取备份统计信息
     */
    @RequestMapping("/statistics")
    @IgnoreAuthorization
    public JsonResponse getStatistics(String databaseName, Integer days) {
        if (days == null || days <= 0) {
            days = 30; // 默认统计最近30天
        }
        if (databaseName == null || databaseName.trim().isEmpty()) {
            databaseName = "ps-bmp"; // 默认数据库名称
        }
        
        TpDatabaseBackupLogVO statistics = tpDatabaseBackupLogService.getBackupStatistics(databaseName, days);
        return JsonResponse.buildSuccess(statistics);
    }

    /**
     * 清理过期备份文件
     */
    @RequestMapping("/clean-expired")
    public JsonResponse cleanExpiredFiles(String jwtpid) {
        int cleanedCount = databaseBackupService.cleanExpiredBackupFiles();
        Map<String, Object> result = new HashMap<>();
        result.put("cleanedCount", cleanedCount);
        result.put("message", "已清理 " + cleanedCount + " 个过期备份文件");
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 检查是否有正在进行的备份
     */
    @RequestMapping("/check-running")
    @IgnoreAuthorization
    public JsonResponse checkRunningBackup(String databaseName) {
        if (databaseName == null || databaseName.trim().isEmpty()) {
            databaseName = "ps-bmp"; // 默认数据库名称
        }
        
        boolean hasRunning = databaseBackupService.hasRunningBackup(databaseName);
        Map<String, Object> result = new HashMap<>();
        result.put("hasRunning", hasRunning);
        result.put("databaseName", databaseName);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 测试mysqldump命令（用于调试）
     */
    @RequestMapping("/test-mysqldump")
    @IgnoreAuthorization
    public JsonResponse testMysqldump() {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 获取配置
            boolean isConfigValid = databaseBackupService.isBackupConfigValid();
            String configInfo = databaseBackupService.getBackupConfigInfo();
            
            result.put("configValid", isConfigValid);
            result.put("configInfo", configInfo);
            
            if (isConfigValid) {
                // 构建简化的测试命令
                String testResult = databaseBackupService.testMysqldumpConnection();
                result.put("connectionTest", testResult);
            }
            
            return JsonResponse.buildSuccess(result);
        } catch (Exception e) {
            return JsonResponse.buildFailure("测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试数据库连接和表结构（用于调试备份记录查询失败问题）
     */
    @RequestMapping("/test_database_table")
    @IgnoreAuthorization
    public JsonResponse testDatabaseTable() {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 尝试执行一个简单的查询来测试表是否存在
            TpDatabaseBackupLogQuery testQuery = new TpDatabaseBackupLogQuery();
            testQuery.setActived(1);
            testQuery.setCurrent(1);
            testQuery.setSize(1);
            
            try {
                IPage<TpDatabaseBackupLogVO> page = tpDatabaseBackupLogService.queryPage(testQuery, "system", "system");
                result.put("tableExists", true);
                result.put("querySuccess", true);
                result.put("totalRecords", page.getTotal());
                result.put("message", "数据库表tp_database_backup_log存在且可以正常查询");
            } catch (Exception e) {
                result.put("tableExists", false);
                result.put("querySuccess", false);
                result.put("error", e.getMessage());
                result.put("message", "数据库表tp_database_backup_log可能不存在或查询失败");
            }
            
            return JsonResponse.buildSuccess(result);
        } catch (Exception e) {
            return JsonResponse.buildFailure("测试数据库表失败: " + e.getMessage());
        }
    }
}