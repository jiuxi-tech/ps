package com.jiuxi.admin.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpAccount;
import com.jiuxi.admin.core.bean.entity.TpPersonBasicinfo;
import com.jiuxi.admin.core.bean.query.TpPersonBasicQuery;
import com.jiuxi.admin.core.bean.vo.OpenApiUserVO;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.admin.core.mapper.TpDeptBasicinfoMapper;
import com.jiuxi.admin.core.mapper.TpPersonBasicinfoMapper;
import com.jiuxi.admin.core.service.KeycloakSyncService;
import com.jiuxi.admin.core.service.OpenApiUserService;
import com.jiuxi.admin.core.util.DataMaskUtil;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SmUtils;
import com.jiuxi.module.user.app.command.dto.UserUpdateCommand;
import com.jiuxi.module.user.app.command.handler.UserCommandHandler;
import com.jiuxi.module.user.app.service.UserAccountService;
import com.jiuxi.module.user.infra.persistence.mapper.UserAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 开放API - 用户信息查询服务实现类
 * 
 * @author system
 * @date 2025-01-30
 */
@Service
public class OpenApiUserServiceImpl implements OpenApiUserService {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiUserServiceImpl.class);

    @Autowired
    private TpPersonBasicinfoMapper personMapper;

    @Autowired
    private TpDeptBasicinfoMapper deptMapper;

    @Autowired
    private UserCommandHandler userCommandHandler;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired(required = false)
    private KeycloakSyncService keycloakSyncService;

    @Autowired
    private UserAccountMapper userAccountMapper;

    /**
     * 根据人员ID查询用户信息（脱敏）
     */
    @Override
    public OpenApiUserVO getUserById(String personId) {
        if (!StringUtils.hasText(personId)) {
            return null;
        }

        // 查询用户信息
        TpPersonBasicinfoVO personVO = personMapper.view(personId);
        if (personVO == null) {
            return null;
        }

        // 转换为脱敏VO
        return convertToMaskedVO(personVO);
    }

    /**
     * 分页查询用户列表（脱敏）
     */
    @Override
    public IPage<OpenApiUserVO> getUserList(String deptId, Integer page, Integer size) {
        // 设置默认值
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 20 : size;
        
        // 限制最大每页条数
        if (size > 100) {
            size = 100;
        }

        // 构建查询条件
        TpPersonBasicQuery query = new TpPersonBasicQuery();
        query.setDeptId(deptId);
        query.setCurrent(page);
        query.setSize(size);

        // 分页查询
        Page<TpPersonBasicinfoVO> pageParam = new Page<>(page, size);
        IPage<TpPersonBasicinfoVO> personPage = personMapper.getPage(pageParam, query);

        // 转换为脱敏VO
        Page<OpenApiUserVO> result = new Page<>(page, size);
        result.setTotal(personPage.getTotal());
        
        java.util.List<OpenApiUserVO> records = new java.util.ArrayList<>();
        personPage.getRecords().forEach(personVO -> {
            records.add(convertToMaskedVO(personVO));
        });
        result.setRecords(records);

        return result;
    }

    /**
     * 搜索用户（脱敏）
     */
    @Override
    public IPage<OpenApiUserVO> searchUsers(String keyword, String deptId, Integer page, Integer size) {
        // 设置默认值
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 20 : size;
        
        // 限制最大每页条数
        if (size > 100) {
            size = 100;
        }

        // 构建查询条件
        TpPersonBasicQuery query = new TpPersonBasicQuery();
        query.setPersonName(keyword);
        query.setDeptId(deptId);
        query.setCurrent(page);
        query.setSize(size);

        // 分页查询
        Page<TpPersonBasicinfoVO> pageParam = new Page<>(page, size);
        IPage<TpPersonBasicinfoVO> personPage = personMapper.getPage(pageParam, query);

        // 转换为脱敏VO
        Page<OpenApiUserVO> result = new Page<>(page, size);
        result.setTotal(personPage.getTotal());
        
        java.util.List<OpenApiUserVO> records = new java.util.ArrayList<>();
        personPage.getRecords().forEach(personVO -> {
            records.add(convertToMaskedVO(personVO));
        });
        result.setRecords(records);

        return result;
    }

    /**
     * 将用户信息转换为脱敏VO
     * 
     * @param personVO 原始用户信息
     * @return 脱敏后的用户信息
     */
    private OpenApiUserVO convertToMaskedVO(TpPersonBasicinfoVO personVO) {
        if (personVO == null) {
            return null;
        }

        OpenApiUserVO vo = new OpenApiUserVO();
        
        // 复制基本字段
        vo.setPersonId(personVO.getPersonId());
        vo.setOffice(personVO.getOffice());
        vo.setDeptId(personVO.getDeptId());
        vo.setDeptName(personVO.getDeptSimpleName()); // 使用部门简称
        vo.setSex(personVO.getSex());
        vo.setActived(personVO.getActived());
        vo.setCreateTime(personVO.getCreateTime());

        // 性别名称
        if (personVO.getSex() != null) {
            vo.setSexName(personVO.getSex() == 1 ? "男" : "女");
        }

        // 脱敏处理
        vo.setPersonName(DataMaskUtil.maskName(personVO.getPersonName()));
        vo.setPersonNo(DataMaskUtil.maskEmployeeNo(personVO.getPersonNo()));
        vo.setPhone(DataMaskUtil.maskPhone(personVO.getPhone()));
        vo.setEmail(DataMaskUtil.maskEmail(personVO.getEmail()));

        return vo;
    }

    /**
     * 修改用户信息
     */
    @Override
    public boolean updateUser(String personId, java.util.Map<String, Object> updateParams) {
        logger.info("开始修改用户信息，personId: {}, updateParams: {}", personId, updateParams);
        
        if (!StringUtils.hasText(personId)) {
            throw new IllegalArgumentException("人员ID不能为空");
        }
        
        if (updateParams == null || updateParams.isEmpty()) {
            throw new IllegalArgumentException("至少需要提供一个待更新的字段");
        }
        
        // 验证用户是否存在
        TpPersonBasicinfoVO personVO = personMapper.view(personId);
        if (personVO == null) {
            throw new IllegalArgumentException("用户不存在: " + personId);
        }
        
        try {
            // 构造更新命令
            UserUpdateCommand command = new UserUpdateCommand();
            command.setPersonId(personId);
            
            // 从请求参数中提取字段
            if (updateParams.containsKey("personName")) {
                command.setPersonName((String) updateParams.get("personName"));
            }
            if (updateParams.containsKey("phone")) {
                command.setPhone((String) updateParams.get("phone"));
            }
            if (updateParams.containsKey("email")) {
                command.setEmail((String) updateParams.get("email"));
            }
            if (updateParams.containsKey("tel")) {
                command.setTel((String) updateParams.get("tel"));
            }
            
            // 设置操作人为api调用（从request中获取appId）
            command.setOperator("api_system");
            command.setTenantId("default");
            
            // 调用用户命令处理器更新用户
            userCommandHandler.handleUpdateUser(command);
            
            logger.info("用户信息修改成功，personId: {}", personId);
            return true;
            
        } catch (IllegalArgumentException e) {
            logger.warn("用户信息修改参数验证失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("用户信息修改失败，personId: {}, error: {}", personId, e.getMessage(), e);
            throw new RuntimeException("用户信息修改失败: " + e.getMessage(), e);
        }
    }

    /**
     * 重置用户密码
     */
    @Override
    public boolean resetPassword(String personId, String newPassword) {
        logger.info("开始重置用户密码，personId: {}", personId);
        
        if (!StringUtils.hasText(personId)) {
            throw new IllegalArgumentException("人员ID不能为空");
        }
        
        if (!StringUtils.hasText(newPassword)) {
            throw new IllegalArgumentException("新密码不能为空");
        }
        
        // 验证用户是否存在
        TpPersonBasicinfoVO personVO = personMapper.view(personId);
        if (personVO == null) {
            throw new IllegalArgumentException("用户不存在: " + personId);
        }
        
        // 查询用户账号信息
        com.jiuxi.admin.core.bean.vo.TpAccountVO accountVO = userAccountService.accountView(personId);
        if (accountVO == null) {
            throw new IllegalArgumentException("用户账号不存在: " + personId);
        }
        
        try {
            // 第一步：更新数据库中的密码
            TpAccount accountEntity = new TpAccount();
            accountEntity.setPersonId(personId);
            // 密码需要SM3加密（使用SmUtils.digestHexSM3，包含盐值）
            accountEntity.setUserpwd(SmUtils.digestHexSM3(newPassword));
            accountEntity.setExtend01("0"); // 不需要强制修改密码
            accountEntity.setUpdateTime(CommonDateUtil.now());
            accountEntity.setLastPasswordChangeTime(CommonDateUtil.now());
            
            // 直接更新数据库密码（不验证旧密码）
            int updateResult = userAccountMapper.updateByPersonId(accountEntity);
            
            if (updateResult <= 0) {
                logger.warn("数据库密码更新失败，personId: {}", personId);
                return false;
            }
            
            // 第二步：同步到Keycloak多凭据（使用专用的密码更新方法）
            if (keycloakSyncService != null) {
                try {
                    String operator = "api_system"; // API调用标识
                    KeycloakSyncService.MultiCredentialSyncResult syncResult = 
                        keycloakSyncService.updatePasswordForAllCredentials(accountVO.getAccountId(), newPassword, operator);
                    
                    if (syncResult != null && syncResult.isSuccess()) {
                        logger.info("Keycloak多凭据密码同步成功，personId: {}, 成功数: {}/{}",
                                personId, syncResult.getSuccessCount(), syncResult.getTotalCredentials());
                    } else {
                        logger.warn("Keycloak多凭据密码同步失败，personId: {}, message: {}",
                                personId, syncResult != null ? syncResult.getMessage() : "unknown");
                        // 注意：即使Keycloak同步失败，数据库密码已经更新，不回滚
                    }
                } catch (Exception e) {
                    logger.error("Keycloak密码同步异常，personId: {}, error: {}", personId, e.getMessage(), e);
                    // 不抛出异常，因为数据库密码已经更新成功
                }
            } else {
                logger.warn("KeycloakSyncService未注入，跳过Keycloak密码同步");
            }
            
            logger.info("用户密码重置成功，personId: {}", personId);
            return true;
            
        } catch (IllegalArgumentException e) {
            logger.warn("密码重置参数验证失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("用户密码重置失败，personId: {}, error: {}", personId, e.getMessage(), e);
            throw new RuntimeException("密码重置失败: " + e.getMessage(), e);
        }
    }

    /**
     * 同步用户到SSO
     */
    @Override
    public boolean syncToSso(String personId) {
        logger.info("开始同步用户到SSO，personId: {}", personId);
        
        if (!StringUtils.hasText(personId)) {
            throw new IllegalArgumentException("人员ID不能为空");
        }
        
        // 验证用户是否存在
        TpPersonBasicinfoVO personVO = personMapper.view(personId);
        if (personVO == null) {
            throw new IllegalArgumentException("用户不存在: " + personId);
        }
        
        // 查询用户账号信息
        com.jiuxi.admin.core.bean.vo.TpAccountVO accountVO = userAccountService.accountView(personId);
        if (accountVO == null) {
            throw new IllegalArgumentException("用户账号不存在: " + personId);
        }
        
        try {
            // 调用同步方法（该方法会发布异步事件，使用syncMultipleCredentials同步多凭据）
            boolean result = userAccountService.syncAccountToKeycloak(accountVO.getAccountId());
            
            if (result) {
                logger.info("用户SSO同步任务已提交，personId: {}, accountId: {}", personId, accountVO.getAccountId());
                return true;
            } else {
                logger.warn("用户SSO同步失败，personId: {}, accountId: {}", personId, accountVO.getAccountId());
                return false;
            }
            
        } catch (Exception e) {
            logger.error("用户SSO同步失败，personId: {}, error: {}", personId, e.getMessage(), e);
            throw new RuntimeException("SSO同步失败: " + e.getMessage(), e);
        }
    }
}
