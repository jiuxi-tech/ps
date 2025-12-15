package com.jiuxi.admin.core.service.impl;

import com.jiuxi.admin.core.mapper.TpTenantMapper;
import com.jiuxi.admin.core.service.TpTenantService;
import com.jiuxi.shared.infrastructure.persistence.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName: TpTenantServiceImpl
 * @Description: 租户表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpTenantService")
public class TpTenantServiceImpl implements TpTenantService {

    @Autowired
    private TpTenantMapper tpTenantMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        return null;
    }

}
