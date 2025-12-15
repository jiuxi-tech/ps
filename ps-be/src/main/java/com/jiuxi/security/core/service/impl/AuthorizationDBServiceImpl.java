package com.jiuxi.security.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import com.jiuxi.common.bean.SessionVO;
import com.jiuxi.security.core.holder.SessionHolder;
import com.jiuxi.security.core.service.AuthorizationCacheService;
import com.jiuxi.security.core.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: AuthorizationServiceImpl
 * @Description: 鉴权实现类 - 数据库模式
 * @Author: Ypp
 * @Date: 2020/5/22 16:50
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class AuthorizationDBServiceImpl implements AuthorizationService {

    /**
     * 判断是否具有权限的sql
     */
    private static final String sql = "SELECT rm.menu_id FROM tp_role_menu  rm LEFT JOIN tp_menu m ON  m.menu_id = rm.menu_id WHERE role_id in (:roles) AND m.menu_uri in (:path) LIMIT 1";

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Autowired(required = false)
    private AuthorizationCacheService authorizationCacheService;

    @Autowired
    private SecurityConfigurationProperties properties;


    /** 有权限 */
    private static final String AUTHORIZATION_TRUE = "true";

    /** 无权限 */
    private static final String AUTHORIZATION_FALSE = "false";

    /**
     * 校验鉴权
     *
     * @param token:
     * @param path:
     * @return boolean
     * @author Ypp
     * @date 2020-08-25 17:03
     */
    @Override
    public boolean authorization(String token, String path) {

        if (jdbcTemplate == null) {
            throw new RuntimeException("在执行鉴权时，jdbcTemplate为null，请先在项目中引入了连接池的配置...");
        }

        // 获取 sesssion
        SessionVO sessionVO = SessionHolder.get();

        // 获取 token 中的角色id
        String roles = sessionVO.getRoleIds();
        if (StrUtil.isBlank(roles)) {
            return false;
        }

        // ---- 缓存authorizationCacheService为空，则直接校验
        if (authorizationCacheService == null) {
            return checkAuthorization(roles, path);
        }

        // ---- 缓存authorizationCacheService不为空，则执行缓存校验
        // 根据角色列表和访问的path，获取权限菜单是否存在
        String cacheResult = authorizationCacheService.getAuthorizationCacheInfo(roles, path);
        // 如果为空，则执行 DB 查询
        if(StrUtil.isBlank(cacheResult)){
           // 检查授权
           boolean bool = checkAuthorization(roles, path);
           // 为 true,则表示 用户有访问该 菜单的权限
           if(bool){
               // 缓存
               authorizationCacheService.putAuthorizationCacheInfo(roles, path, AUTHORIZATION_TRUE);
               return true;
           }else {
               // 缓存
               authorizationCacheService.putAuthorizationCacheInfo(roles, path, AUTHORIZATION_FALSE);
               return false;
           }
        }else {
            // 为 true,则表示 用户有访问该 菜单的权限
            if(AUTHORIZATION_TRUE.equals(cacheResult)){
                return true;
            }else {
                return false;
            }
        }
    }


    /**
     * @description: 检查授权
     * @param roles
     * @param path  请求路径
     * @return boolean
     * @author Ypp
     * @date 2020/5/28 15:56
     */
    private boolean checkAuthorization(String roles, String path) {

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("roles", Arrays.asList(roles.split(",")));
        List<String> list = new ArrayList<>();
        list.add(path);
        try {
            // 兼容路径经过编码的情况
            list.add(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        parameters.addValue("path", list);

        List<String> result = namedParameterJdbcTemplate.queryForList(sql, parameters, String.class);
        if (result != null && result.size() > 0) {
            // 表示 用户有访问该 菜单的权限
            return true;
        }

        return false;
    }
}
