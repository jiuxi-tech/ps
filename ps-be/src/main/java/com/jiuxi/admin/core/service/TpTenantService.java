package com.jiuxi.admin.core.service;

import com.jiuxi.shared.infrastructure.persistence.util.PageUtils;

import java.util.Map;

/**
 * @ClassName: TpTenantService
 * @Description: 租户表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpTenantService {

    PageUtils queryPage(Map<String, Object> params);
}

