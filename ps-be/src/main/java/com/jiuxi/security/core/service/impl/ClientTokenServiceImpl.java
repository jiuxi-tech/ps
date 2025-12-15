package com.jiuxi.security.core.service.impl;

import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.security.core.entity.vo.AccountThirdVO;
import com.jiuxi.security.core.service.ClientTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @Description: 合作方登陆实现
 * @ClassName: ClientTokenServiceImpl
 * @Author: pdd
 * @Date: 2022-05-25 16:36
 * @Copyright: 2022 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class ClientTokenServiceImpl implements ClientTokenService {

    @Autowired(required = false)
    protected JdbcTemplate jdbcTemplate;

    private static final String loginSql = "SELECT app_key, app_secret, partner_name FROM tp_account_third WHERE app_key = ? and app_secret = ? AND actived = '1' LIMIT 1";

    @Override
    public AccountThirdVO login(String appKey, String appSecret) {
        if (jdbcTemplate == null) {
            throw new RuntimeException("在执行认证时，jdbcTemplate为null，请先在项目中引入了连接池的配置...");
        }

        // BeanPropertyRowMapper 字段转换时，注意数据库字段与实体属性的对应
        List<AccountThirdVO> list = jdbcTemplate.query(loginSql, new Object[]{appKey, appSecret}, new BeanPropertyRowMapper<>(AccountThirdVO.class));
        if (list != null && list.size() != 1) {
            throw new TopinfoRuntimeException(-1, "登录失败，用户名或密码错误");
        }

        return list.get(0);
    }
}
