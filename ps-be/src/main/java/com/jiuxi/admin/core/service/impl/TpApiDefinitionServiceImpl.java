package com.jiuxi.admin.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jiuxi.admin.core.bean.entity.TpApiDefinition;
import com.jiuxi.admin.core.mapper.TpApiDefinitionMapper;
import com.jiuxi.admin.core.service.TpApiDefinitionService;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: TpApiDefinitionServiceImpl
 * @Description: API定义服务实现
 * @Author system
 * @Date 2024-01-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpApiDefinitionService")
public class TpApiDefinitionServiceImpl implements TpApiDefinitionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpApiDefinitionServiceImpl.class);

    @Autowired
    private TpApiDefinitionMapper tpApiDefinitionMapper;

    /**
     * 查询所有API定义列表
     *
     * @return API列表
     */
    @Override
    public List<TpApiDefinition> getAllApis() {
        try {
            return tpApiDefinitionMapper.selectAll();
        } catch (Exception e) {
            LOGGER.error("查询所有API定义失败！错误: {}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询所有API定义失败！");
        }
    }

    /**
     * 查询启用状态的API定义列表
     *
     * @return API列表
     */
    @Override
    public List<TpApiDefinition> getEnabledApis() {
        try {
            return tpApiDefinitionMapper.selectByStatus(1);
        } catch (Exception e) {
            LOGGER.error("查询启用的API定义失败！错误: {}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询启用的API定义失败！");
        }
    }

    /**
     * 根据分类查询API定义列表
     *
     * @param category 分类
     * @return API列表
     */
    @Override
    public List<TpApiDefinition> getApisByCategory(String category) {
        try {
            return tpApiDefinitionMapper.selectByCategory(category);
        } catch (Exception e) {
            LOGGER.error("根据分类查询API定义失败！category:{}, 错误: {}", category, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "根据分类查询API定义失败！");
        }
    }

    /**
     * 根据API ID查询API定义
     *
     * @param apiId API ID
     * @return API定义
     */
    @Override
    public TpApiDefinition getById(String apiId) {
        try {
            return tpApiDefinitionMapper.selectById(apiId);
        } catch (Exception e) {
            LOGGER.error("根据ID查询API定义失败！apiId:{}, 错误: {}", apiId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "根据ID查询API定义失败！");
        }
    }

    /**
     * 根据API路径和HTTP方法查询API定义
     *
     * @param apiPath API路径
     * @param httpMethod HTTP方法
     * @return API定义
     */
    @Override
    public TpApiDefinition getByPathAndMethod(String apiPath, String httpMethod) {
        try {
            return tpApiDefinitionMapper.selectByPathAndMethod(apiPath, httpMethod);
        } catch (Exception e) {
            LOGGER.error("根据路径和方法查询API定义失败！apiPath:{}, httpMethod:{}, 错误: {}", 
                    apiPath, httpMethod, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "根据路径和方法查询API定义失败！");
        }
    }
}
