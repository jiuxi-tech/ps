package com.jiuxi.security.autoconfig;

import cn.hutool.core.util.ArrayUtil;
import com.jiuxi.security.bean.Authentication;
import com.jiuxi.security.bean.Authorization;
import com.jiuxi.security.bean.ValidateCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Optional;

@ConfigurationProperties(prefix = "topinfo.security")
public class SecurityConfigurationProperties {

    /**
     * 认证
     */
    private Authentication authentication;

    /**
     * 授权
     */
    private Authorization authorization;

    /**
     * 验证码
     */
    private ValidateCode validateCode;


    /**
     * 密码传输是否加密，默认不加密
     */
    private boolean passwordEncryption = false;

    /**
     * 默认不过滤路径
     * /static/** - 静态资源
     * /error/**  - 错误资源
     * /platform/** - 平台资源
     * /app/platform/** - App平台资源
     *
     * 备注：由于前期设计问题，以下几个路径，不需要认证授权，后期修改影响太大，所以这里暂不调整，在自己的配置文件中加上这两个。
     * # 附件下载
     * - /app/sys/file/download
     * # pdf查看
     * - /sys/file/preview-pdf
     */
    private String[] defaultExcludePath = {"/static/**", "/error/**", "/platform/**", "/app/platform/**"};

    /**
     * 获取排除认证路径
     *
     * @return java.lang.String[]：排除认证的路径集合
     * @author pand
     * @date 2020-08-24 10:57
     */
    public String[] getExcludePath() {

        // 默认排除的请求
        // 配置文件中排除的请求
        String[] excludePath = this.getAuthentication().getExcludePaths();
        // 合并
        excludePath = ArrayUtil.addAll(excludePath, defaultExcludePath);

        return excludePath;
    }

    public Authentication getAuthentication() {
        authentication = Optional.ofNullable(authentication).orElse(new Authentication());
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Authorization getAuthorization() {
        authorization = Optional.ofNullable(authorization).orElse(new Authorization());
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    public ValidateCode getValidateCode() {
        validateCode = Optional.ofNullable(validateCode).orElse(new ValidateCode());
        return validateCode;
    }

    public void setValidateCode(ValidateCode validateCode) {
        this.validateCode = validateCode;
    }

    public boolean isPasswordEncryption() {
        return passwordEncryption;
    }

    public void setPasswordEncryption(boolean passwordEncryption) {
        this.passwordEncryption = passwordEncryption;
    }
}
