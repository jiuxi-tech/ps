package com.jiuxi.security.constant;

import com.jiuxi.shared.common.constants.SecurityConstants;

/**
 * 安全组件 - 常量
 *
 * @author 杨占锐
 * @date 2024/10/8 14:42
 * @deprecated 请使用 {@link SecurityConstants} 替代
 */
@Deprecated
public class SecurityConstant {

    /**
     * 退出登录时redis发布订阅的topic
     * @deprecated 请使用 {@link SecurityConstants.RedisTopic#USER_LOGOUT} 替代
     */
    @Deprecated
    public static final String LOGOUT_TOPIC = SecurityConstants.RedisTopic.USER_LOGOUT;
}
