package com.jiuxi.module.sys.intf.web.controller.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpServerCertQuery;
import com.jiuxi.admin.core.bean.vo.TpServerCertVO;
import com.jiuxi.admin.core.service.TpServerCertService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 服务器证书管理查询控制器
 * 负责服务器证书相关的数据查询操作 (Read)
 * 基于CQRS架构设计，专注于处理查询操作
 * 
 * @author 系统生成
 * @date 2025-09-25
 */
@RestController
@RequestMapping("/sys/server-cert")
@Authorization
public class ServerCertQueryController {

    @Autowired
    private TpServerCertService tpServerCertService;

    /**
     * 分页查询服务器证书列表
     * 
     * @param query 查询条件
     * @return 分页结果
     */
    @RequestMapping("/list")
    public JsonResponse list(@RequestBody TpServerCertQuery query) {
        try {
            IPage<TpServerCertVO> page = tpServerCertService.queryPage(query);
            return JsonResponse.buildSuccess(page);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有服务器证书列表（不分页）
     * 
     * @param query 查询条件
     * @return 证书列表
     */
    @RequestMapping("/all")
    public JsonResponse all(@RequestBody TpServerCertQuery query) {
        try {
            List<TpServerCertVO> list = tpServerCertService.queryList(query);
            return JsonResponse.buildSuccess(list);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询服务器证书详细信息
     * 
     * @param certId 证书ID
     * @return 证书详细信息
     */
    @RequestMapping("/view")
    public JsonResponse view(@RequestParam("certId") String certId) {
        try {
            TpServerCertVO vo = tpServerCertService.queryById(certId);
            if (vo == null) {
                return JsonResponse.buildFailure("证书不存在");
            }
            return JsonResponse.buildSuccess(vo);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }

    /**
     * 验证证书名称是否重复
     * 
     * @param certName 证书名称
     * @param certId   证书ID（修改时传入）
     * @return 验证结果
     */
    @RequestMapping("/check-name")
    public JsonResponse checkCertName(@RequestParam("certName") String certName,
                                     @RequestParam(value = "certId", required = false) String certId) {
        try {
            boolean exists = tpServerCertService.checkCertNameExists(certName, certId);
            return JsonResponse.buildSuccess(!exists);  // 返回true表示可用，false表示重复
        } catch (Exception e) {
            return JsonResponse.buildFailure("验证失败: " + e.getMessage());
        }
    }

    /**
     * 查询当前正在使用的证书
     * 
     * @return 正在使用的证书
     */
    @RequestMapping("/current")
    public JsonResponse getCurrentInUseCert() {
        try {
            TpServerCertVO cert = tpServerCertService.getCurrentInUseCert();
            return JsonResponse.buildSuccess(cert);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询即将过期的证书
     * 
     * @param days 天数，默认30天
     * @return 即将过期的证书列表
     */
    @RequestMapping("/expiring")
    public JsonResponse getExpiringCerts(@RequestParam(value = "days", defaultValue = "30") int days) {
        try {
            List<TpServerCertVO> list = tpServerCertService.getExpiringCerts(days);
            return JsonResponse.buildSuccess(list);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询失败: " + e.getMessage());
        }
    }

    /**
     * 解析上传的证书文件
     * 
     * @param requestBody JSON请求体，包含pemContent字段
     * @return 解析出的证书信息
     */
    @RequestMapping("/parse")
    public JsonResponse parseCertificate(@RequestBody Map<String, String> requestBody) {
        try {
            String pemContent = requestBody.get("pemContent");
            if (pemContent == null || pemContent.trim().isEmpty()) {
                return JsonResponse.buildFailure("PEM证书内容不能为空");
            }
            
            TpServerCertVO certInfo = tpServerCertService.parseCertificate(pemContent);
            if (certInfo == null) {
                return JsonResponse.buildFailure("无法解析证书文件，请检查文件格式");
            }
            return JsonResponse.buildSuccess(certInfo);
        } catch (Exception e) {
            return JsonResponse.buildFailure("解析失败: " + e.getMessage());
        }
    }

    /**
     * 验证证书和私钥是否匹配
     * 
     * @param requestBody JSON请求体，包含pemContent和keyContent字段
     * @return 验证结果
     */
    @RequestMapping("/validate")
    public JsonResponse validateCertAndKey(@RequestBody Map<String, String> requestBody) {
        try {
            String pemContent = requestBody.get("pemContent");
            String keyContent = requestBody.get("keyContent");
            
            if (pemContent == null || pemContent.trim().isEmpty()) {
                return JsonResponse.buildFailure("PEM证书内容不能为空");
            }
            
            if (keyContent == null || keyContent.trim().isEmpty()) {
                return JsonResponse.buildFailure("私钥内容不能为空");
            }
            
            boolean valid = tpServerCertService.validateCertAndKey(pemContent, keyContent);
            return JsonResponse.buildSuccess(valid);
        } catch (Exception e) {
            return JsonResponse.buildFailure("验证失败: " + e.getMessage());
        }
    }
}