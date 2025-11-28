package com.jiuxi.module.user.app.service;

import com.jiuxi.module.user.app.dto.ImportResultDTO;
import com.jiuxi.module.user.app.dto.UserExportDTO;
import com.jiuxi.module.user.app.dto.UserImportDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * 用户导入导出应用服务接口
 * 提供用户批量导入、导出和模板下载功能
 * 
 * @author User Import Refactor
 * @date 2025-11-27
 */
public interface UserImportExportService {

    /**
     * 导入用户数据（从Excel文件）
     * 
     * @param file Excel文件
     * @param operatorId 操作人ID
     * @param tenantId 租户ID
     * @param ascnId 所属机构ID
     * @return 导入结果
     */
    ImportResultDTO importUsers(MultipartFile file, String operatorId, String tenantId, String ascnId);

    /**
     * 导出用户数据（到Excel文件）
     * 
     * @param deptId 部门ID
     * @param deptLevelcode 部门层级码（用于查询子部门）
     * @param selectedUserIds 指定导出的用户ID列表（可选，为空则导出部门所有用户）
     * @param tenantId 租户ID
     * @param outputStream 输出流
     * @throws Exception 导出异常
     */
    void exportUsers(String deptId, String deptLevelcode, List<String> selectedUserIds, 
                     String tenantId, OutputStream outputStream) throws Exception;

    /**
     * 生成并下载导入模板
     * 
     * @param outputStream 输出流
     * @throws Exception 生成异常
     */
    void downloadTemplate(OutputStream outputStream) throws Exception;
}
