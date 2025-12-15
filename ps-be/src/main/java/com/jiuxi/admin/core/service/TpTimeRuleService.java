package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpTimeRuleQuery;
import com.jiuxi.admin.core.bean.vo.TpTimeRuleVO;

import java.util.List;

/**
 * @ClassName: TpTimeRuleService
 * @Description: 登录时间段控制规则表 - Service接口
 * @Author: Qdd
 * @Date: 2025-01-20
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
public interface TpTimeRuleService {

    /**
     * 分页查询时间规则
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpTimeRuleVO> queryPage(TpTimeRuleQuery query);

    /**
     * 新增时间规则
     * @param vo 时间规则VO
     * @param creatorId 创建人ID
     * @return 时间规则VO
     */
    TpTimeRuleVO save(TpTimeRuleVO vo, String creatorId);

    /**
     * 更新时间规则
     * @param vo 时间规则VO
     * @param modifierId 修改人ID
     * @return 时间规则VO
     */
    TpTimeRuleVO update(TpTimeRuleVO vo, String modifierId);

    /**
     * 查看时间规则详情
     * @param id 规则ID
     * @return 时间规则VO
     */
    TpTimeRuleVO view(String id);

    /**
     * 删除时间规则（逻辑删除）
     * @param id 规则ID
     * @param modifierId 修改人ID
     */
    void delete(String id, String modifierId);

    /**
     * 批量删除时间规则（逻辑删除）
     * @param ids 规则ID列表
     * @param modifierId 修改人ID
     */
    void deleteBatch(List<String> ids, String modifierId);

    /**
     * 校验用户登录时间权限
     * @param userId 用户ID
     * @param roleIds 用户角色ID列表
     * @return 校验结果：true-允许登录，false-拒绝登录
     */
    boolean validateLoginTime(String userId, List<String> roleIds);

    /**
     * 校验用户登录时间权限并返回拒绝原因
     * @param userId 用户ID
     * @param roleIds 用户角色ID列表
     * @return 校验结果对象，包含是否允许和拒绝原因
     */
    LoginTimeValidationResult validateLoginTimeWithReason(String userId, List<String> roleIds);

    /**
     * 获取所有有效的时间规则
     * @return 时间规则列表
     */
    List<TpTimeRuleVO> getAllValidRules();

    /**
     * 根据用户ID和角色ID查询相关的时间规则
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 时间规则列表
     */
    List<TpTimeRuleVO> getRulesByUserAndRoles(String userId, List<String> roleIds);

    /**
     * 登录时间校验结果类
     */
    class LoginTimeValidationResult {
        private boolean allowed;
        private String reason;
        private String ruleName;

        public LoginTimeValidationResult(boolean allowed, String reason, String ruleName) {
            this.allowed = allowed;
            this.reason = reason;
            this.ruleName = ruleName;
        }

        public boolean isAllowed() {
            return allowed;
        }

        public void setAllowed(boolean allowed) {
            this.allowed = allowed;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getRuleName() {
            return ruleName;
        }

        public void setRuleName(String ruleName) {
            this.ruleName = ruleName;
        }
    }
}