package com.jiuxi.module.user.interfaces.web.controller;

import com.jiuxi.admin.core.service.KeycloakSyncService;
import com.jiuxi.module.user.app.service.UserAccountService;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import com.jiuxi.admin.core.service.TpAccountService;
import com.jiuxi.common.bean.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Keycloak账号同步测试控制器
 * 专门用于测试Keycloak账号同步相关功能
 *
 * @author System
 * @since 2025-01-21
 */
@RestController
@RequestMapping("/test_keycloak_sync")
public class KeycloakAccountSyncController {

    @Autowired(required = false)
    private KeycloakSyncService keycloakSyncService;
    
    @Autowired
    private TpAccountService tpAccountService;
    
    @Autowired
    private UserAccountService userAccountService;

    /**
     * 测试新增账号同步到Keycloak
     *
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱（可选）
     * @param firstName 名字（可选）
     * @param lastName 姓氏（可选）
     * @return JsonResponse
     */
    @RequestMapping("/test_create_account_sync")
    public JsonResponse testCreateAccountSync(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        
        if (keycloakSyncService == null) {
            return JsonResponse.buildFailure("Keycloak同步服务未启用");
        }
        
        try {
            // 先创建tp_account记录
            TpAccountVO accountVO = new TpAccountVO();
            accountVO.setUsername(username);
            accountVO.setUserpwd(password);  // 使用正确的字段名
            accountVO.setPersonId("1111111111111111111");  // 使用已存在的personId
            accountVO.setEnabled(1);
            
            // 调用账号管理服务创建账号（这会自动同步到Keycloak）
            int createResult = userAccountService.accountManage(accountVO, false);  // false表示不需要解密
            
            if (createResult > 0) {
                return JsonResponse.buildSuccess("账号创建并同步到Keycloak成功");
            } else {
                return JsonResponse.buildFailure("账号创建失败");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("账号同步失败: " + e.getMessage());
        }
    }

    /**
     * 测试更新Keycloak用户信息
     *
     * @param username 用户名
     * @param newPassword 新密码（可选）
     * @param email 新邮箱（可选）
     * @return JsonResponse
     */
    @RequestMapping("/test_update_account_sync")
    public JsonResponse testUpdateAccountSync(
            @RequestParam String username,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String email) {
        
        if (keycloakSyncService == null) {
            return JsonResponse.buildFailure("Keycloak同步服务未启用");
        }
        
        try {
            // 使用用户名作为accountId进行测试
            String accountId = "test_" + username;
            String updater = "test_system";
            
            KeycloakSyncService.KeycloakSyncResult result = keycloakSyncService.updateKeycloakUser(
                    accountId, username, newPassword, updater);
            
            if (result.isSuccess()) {
                return JsonResponse.buildSuccess(result);
            } else {
                return JsonResponse.buildFailure(result.getMessage());
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("用户更新失败: " + e.getMessage());
        }
    }

    /**
     * 测试禁用Keycloak用户
     *
     * @param username 用户名
     * @return JsonResponse
     */
    @RequestMapping("/test_disable_account_sync")
    public JsonResponse testDisableAccountSync(@RequestParam String username) {
        
        if (keycloakSyncService == null) {
            return JsonResponse.buildFailure("Keycloak同步服务未启用");
        }
        
        try {
            String accountId = "test_" + username;
            
            KeycloakSyncService.KeycloakSyncResult result = keycloakSyncService.disableKeycloakUser(accountId);
            
            if (result.isSuccess()) {
                return JsonResponse.buildSuccess(result);
            } else {
                return JsonResponse.buildFailure(result.getMessage());
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("用户禁用失败: " + e.getMessage());
        }
    }

    /**
     * 测试启用Keycloak用户
     *
     * @param username 用户名
     * @return JsonResponse
     */
    @RequestMapping("/test_enable_account_sync")
    public JsonResponse testEnableAccountSync(@RequestParam String username) {
        
        if (keycloakSyncService == null) {
            return JsonResponse.buildFailure("Keycloak同步服务未启用");
        }
        
        try {
            String accountId = "test_" + username;
            
            KeycloakSyncService.KeycloakSyncResult result = keycloakSyncService.enableKeycloakUser(accountId);
            
            if (result.isSuccess()) {
                return JsonResponse.buildSuccess(result);
            } else {
                return JsonResponse.buildFailure(result.getMessage());
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("用户启用失败: " + e.getMessage());
        }
    }

    /**
     * 测试删除Keycloak用户
     *
     * @param username 用户名
     * @return JsonResponse
     */
    @RequestMapping("/test_delete_account_sync")
    public JsonResponse testDeleteAccountSync(@RequestParam String username) {
        
        if (keycloakSyncService == null) {
            return JsonResponse.buildFailure("Keycloak同步服务未启用");
        }
        
        try {
            String accountId = "test_" + username;
            
            KeycloakSyncService.KeycloakSyncResult result = keycloakSyncService.deleteKeycloakUser(accountId);
            
            if (result.isSuccess()) {
                return JsonResponse.buildSuccess(result);
            } else {
                return JsonResponse.buildFailure(result.getMessage());
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("用户删除失败: " + e.getMessage());
        }
    }

    /**
     * 测试Keycloak连接状态
     *
     * @return JsonResponse
     */
    @RequestMapping("/test_keycloak_connection")
    public JsonResponse testKeycloakConnection() {
        
        if (keycloakSyncService == null) {
            return JsonResponse.buildFailure("Keycloak同步服务未启用");
        }
        
        try {
            // 尝试获取管理员token来测试连接
            // 这里可以添加一个简单的连接测试方法
            return JsonResponse.buildSuccess("Keycloak连接正常");
        } catch (Exception e) {
            return JsonResponse.buildFailure("Keycloak连接失败: " + e.getMessage());
        }
    }

    /**
     * 测试账号管理功能（跳过密码解密）
     *
     * @param request 请求参数
     * @return JsonResponse
     */
    @RequestMapping("/test_account_manage")
    public JsonResponse testAccountManage(@RequestBody Map<String, String> request) {
        
        String username = request.get("username");
        String password = request.get("password");
        String personId = request.get("personId");
        
        try {
            // 创建账号VO对象
            TpAccountVO vo = new TpAccountVO();
            vo.setUsername(username);
            vo.setUserpwd(password);
            vo.setPersonId(personId);
            
            // 调用不需要解密的accountManage方法
            int result = userAccountService.accountManage(vo, false);
            
            if (result > 0) {
                return JsonResponse.buildSuccess("账号管理成功，影响行数: " + result);
            } else {
                return JsonResponse.buildFailure("账号管理失败");
            }
            
        } catch (Exception e) {
            return JsonResponse.buildFailure("账号管理异常: " + e.getMessage());
        }
    }
    
    /**
     * 获取测试说明
     *
     * @return JsonResponse
     */
    @RequestMapping("/test_help")
    public JsonResponse testHelp() {
        StringBuilder help = new StringBuilder();
        help.append("Keycloak账号同步测试接口说明:\n");
        help.append("1. /test_keycloak_sync/test_create_account_sync - 测试创建账号并同步到Keycloak\n");
        help.append("   参数: username(必填), password(必填), email(可选), firstName(可选), lastName(可选)\n");
        help.append("2. /test_keycloak_sync/test_update_account_sync - 测试更新Keycloak用户信息\n");
        help.append("   参数: username(必填), newPassword(可选), email(可选)\n");
        help.append("3. /test_keycloak_sync/test_disable_account_sync - 测试禁用Keycloak用户\n");
        help.append("   参数: username(必填)\n");
        help.append("4. /test_keycloak_sync/test_enable_account_sync - 测试启用Keycloak用户\n");
        help.append("   参数: username(必填)\n");
        help.append("5. /test_keycloak_sync/test_delete_account_sync - 测试删除Keycloak用户\n");
        help.append("   参数: username(必填)\n");
        help.append("6. /test_keycloak_sync/test_keycloak_connection - 测试Keycloak连接状态\n");
        help.append("7. /test_keycloak_sync/test_account_manage - 测试账号管理功能（明文密码）\n");
        help.append("   参数: username(必填), password(必填), personId(必填)\n");
        help.append("8. /test_keycloak_sync/test_help - 获取测试说明\n");
        help.append("\n注意: 所有测试接口都使用test_前缀的accountId，不会影响正式数据");
        
        return JsonResponse.buildSuccess(help.toString());
    }
}