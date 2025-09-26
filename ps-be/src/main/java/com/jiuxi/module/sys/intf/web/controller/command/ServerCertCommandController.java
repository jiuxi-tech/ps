package com.jiuxi.module.sys.intf.web.controller.command;

import com.jiuxi.admin.core.bean.vo.TpServerCertVO;
import com.jiuxi.admin.core.service.TpServerCertService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 服务器证书管理命令控制器
 * 负责服务器证书相关的数据变更操作 (Create, Update, Delete)
 * 基于CQRS架构设计，专注于处理命令操作
 * 
 * @author 系统生成
 * @date 2025-09-25
 */
@RestController
@RequestMapping("/sys/server-cert")
@Authorization
public class ServerCertCommandController {

    @Autowired
    private TpServerCertService tpServerCertService;

    /**
     * 新增服务器证书
     * 
     * @param vo 证书信息
     * @return 操作结果
     */
    @RequestMapping("/add")
    public JsonResponse add(@Validated(AddGroup.class) @RequestBody TpServerCertVO vo) {
        try {
            boolean success = tpServerCertService.save(vo);
            if (success) {
                return JsonResponse.buildSuccess("新增成功");
            } else {
                return JsonResponse.buildFailure("新增失败");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改服务器证书
     * 
     * @param vo 证书信息
     * @return 操作结果
     */
    @RequestMapping("/update")
    public JsonResponse update(@Validated(UpdateGroup.class) @RequestBody TpServerCertVO vo) {
        try {
            boolean success = tpServerCertService.update(vo);
            if (success) {
                return JsonResponse.buildSuccess("修改成功");
            } else {
                return JsonResponse.buildFailure("修改失败");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除服务器证书
     * 支持JSON格式请求体和表单参数两种方式
     * 
     * @param requestBody JSON请求体（可选）
     * @param certId      证书ID（表单参数，可选）
     * @return 操作结果
     */
    @RequestMapping("/delete")
    public JsonResponse delete(@RequestBody(required = false) Map<String, String> requestBody,
                              @RequestParam(required = false) String certId) {
        try {
            // 优先从JSON请求体获取参数
            String targetCertId = certId;
            if (requestBody != null && requestBody.containsKey("certId")) {
                targetCertId = requestBody.get("certId");
            }

            if (targetCertId == null || targetCertId.trim().isEmpty()) {
                return JsonResponse.buildFailure("证书ID不能为空");
            }

            boolean success = tpServerCertService.delete(targetCertId);
            if (success) {
                return JsonResponse.buildSuccess("删除成功");
            } else {
                return JsonResponse.buildFailure("删除失败");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("删除失败: " + e.getMessage());
        }
    }

    /**
     * 应用证书到文件系统
     * 
     * @param requestBody JSON请求体
     * @return 操作结果
     */
    @RequestMapping("/apply")
    public JsonResponse applyCert(@RequestBody Map<String, String> requestBody) {
        try {
            String certId = requestBody.get("certId");
            if (certId == null || certId.trim().isEmpty()) {
                return JsonResponse.buildFailure("证书ID不能为空");
            }

            boolean success = tpServerCertService.applyCert(certId);
            if (success) {
                return JsonResponse.buildSuccess("应用成功");
            } else {
                return JsonResponse.buildFailure("应用失败");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("应用失败: " + e.getMessage());
        }
    }

    /**
     * 重启Nginx服务
     * 
     * @return 操作结果
     */
    @RequestMapping("/restart")
    public JsonResponse restartNginx() {
        try {
            boolean success = tpServerCertService.restartNginx();
            if (success) {
                return JsonResponse.buildSuccess("重启成功");
            } else {
                return JsonResponse.buildFailure("重启失败");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("重启失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除服务器证书
     * 
     * @param requestBody 包含证书ID数组的JSON请求体
     * @return 操作结果
     */
    @RequestMapping("/batch-delete")
    public JsonResponse batchDelete(@RequestBody Map<String, Object> requestBody) {
        try {
            @SuppressWarnings("unchecked")
            java.util.List<String> certIds = (java.util.List<String>) requestBody.get("certIds");
            
            if (certIds == null || certIds.isEmpty()) {
                return JsonResponse.buildFailure("请选择要删除的证书");
            }

            int successCount = 0;
            StringBuilder errorMessages = new StringBuilder();

            for (String certId : certIds) {
                try {
                    boolean success = tpServerCertService.delete(certId);
                    if (success) {
                        successCount++;
                    } else {
                        errorMessages.append("证书ID ").append(certId).append(" 删除失败; ");
                    }
                } catch (Exception e) {
                    errorMessages.append("证书ID ").append(certId).append(" 删除失败: ").append(e.getMessage()).append("; ");
                }
            }

            if (successCount == certIds.size()) {
                return JsonResponse.buildSuccess("批量删除成功，共删除 " + successCount + " 个证书");
            } else if (successCount > 0) {
                return JsonResponse.buildSuccess("部分删除成功，成功删除 " + successCount + " 个证书。失败原因: " + errorMessages.toString());
            } else {
                return JsonResponse.buildFailure("批量删除失败: " + errorMessages.toString());
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 更新证书过期状态（手动触发）
     * 
     * @return 操作结果
     */
    @RequestMapping("/update-expired-status")
    public JsonResponse updateExpiredStatus() {
        try {
            tpServerCertService.updateExpiredStatus();
            return JsonResponse.buildSuccess("更新成功");
        } catch (Exception e) {
            return JsonResponse.buildFailure("更新失败: " + e.getMessage());
        }
    }
}