package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.entity.TpApiDefinition;

import java.util.List;

/**
 * @ClassName: TpApiDefinitionService
 * @Description: API定义服务
 * @Author system
 * @Date 2024-01-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpApiDefinitionService {

    /**
     * 查询所有API定义列表
     *
     * @return API列表
     */
    List<TpApiDefinition> getAllApis();

    /**
     * 查询启用状态的API定义列表
     *
     * @return API列表
     */
    List<TpApiDefinition> getEnabledApis();

    /**
     * 根据分类查询API定义列表
     *
     * @param category 分类
     * @return API列表
     */
    List<TpApiDefinition> getApisByCategory(String category);

    /**
     * 根据API ID查询API定义
     *
     * @param apiId API ID
     * @return API定义
     */
    TpApiDefinition getById(String apiId);

    /**
     * 根据API路径和HTTP方法查询API定义
     *
     * @param apiPath API路径
     * @param httpMethod HTTP方法
     * @return API定义
     */
    TpApiDefinition getByPathAndMethod(String apiPath, String httpMethod);
}
