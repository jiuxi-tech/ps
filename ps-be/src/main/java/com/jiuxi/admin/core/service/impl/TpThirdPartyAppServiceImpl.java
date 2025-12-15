package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpApiDefinition;
import com.jiuxi.admin.core.bean.entity.TpAppApiPermission;
import com.jiuxi.admin.core.bean.entity.TpThirdPartyApp;
import com.jiuxi.admin.core.bean.query.TpThirdPartyAppQuery;
import com.jiuxi.admin.core.bean.vo.TpThirdPartyAppVO;
import com.jiuxi.admin.core.mapper.TpApiDefinitionMapper;
import com.jiuxi.admin.core.mapper.TpAppApiPermissionMapper;
import com.jiuxi.admin.core.mapper.TpThirdPartyAppMapper;
import com.jiuxi.admin.core.service.TpThirdPartyAppService;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @ClassName: TpThirdPartyAppServiceImpl
 * @Description: 第三方应用管理服务实现
 * @Author system
 * @Date 2024-01-18 11:05:17
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpThirdPartyAppService")
public class TpThirdPartyAppServiceImpl implements TpThirdPartyAppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpThirdPartyAppServiceImpl.class);

    @Autowired
    private TpThirdPartyAppMapper tpThirdPartyAppMapper;

    @Autowired
    private TpAppApiPermissionMapper tpAppApiPermissionMapper;

    @Autowired
    private TpApiDefinitionMapper tpApiDefinitionMapper;

    /**
     * 分页查询第三方应用列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<TpThirdPartyAppVO> queryPage(TpThirdPartyAppQuery query) {
        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);

            Page<TpThirdPartyAppVO> page = new Page<>(pageNum, pageSize);
            IPage<TpThirdPartyAppVO> iPage = tpThirdPartyAppMapper.getPage(page, query);
            
            // 查询每个应用已授权的API数量
            for (TpThirdPartyAppVO vo : iPage.getRecords()) {
                List<String> apiIds = tpAppApiPermissionMapper.selectApiIdsByAppId(vo.getAppId());
                vo.setApiIds(apiIds);
            }
            
            return iPage;
        } catch (Exception e) {
            LOGGER.error("第三方应用列表查询失败！query:{}, 错误: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "第三方应用列表查询失败！");
        }
    }

    /**
     * 查询第三方应用列表
     *
     * @param query 查询条件
     * @return 应用列表
     */
    @Override
    public List<TpThirdPartyAppVO> getList(TpThirdPartyAppQuery query) {
        try {
            List<TpThirdPartyAppVO> list = tpThirdPartyAppMapper.getList(query);
            
            // 查询每个应用已授权的API数量
            for (TpThirdPartyAppVO vo : list) {
                List<String> apiIds = tpAppApiPermissionMapper.selectApiIdsByAppId(vo.getAppId());
                vo.setApiIds(apiIds);
            }
            
            return list;
        } catch (Exception e) {
            LOGGER.error("第三方应用列表查询失败！query:{}, 错误: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "第三方应用列表查询失败！");
        }
    }

    /**
     * 新增第三方应用
     *
     * @param vo 应用信息
     * @param creator 创建人
     * @return 应用VO（包含生成的API Key）
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public TpThirdPartyAppVO add(TpThirdPartyAppVO vo, String creator) {
        try {
            // 检查应用名称是否重复
            TpThirdPartyAppQuery query = new TpThirdPartyAppQuery();
            query.setAppName(vo.getAppName());
            List<TpThirdPartyAppVO> existApps = tpThirdPartyAppMapper.getList(query);
            if (existApps != null && !existApps.isEmpty()) {
                throw new TopinfoRuntimeException(-1, "应用名称已存在！");
            }

            TpThirdPartyApp bean = new TpThirdPartyApp();
            String appId = SnowflakeIdUtil.nextIdStr();

            // 生成API Key
            String apiKey = generateApiKey();

            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            bean.setAppId(appId);
            bean.setApiKey(apiKey);

            // 设置默认值
            if (bean.getStatus() == null) {
                bean.setStatus(1); // 默认启用
            }
            if (bean.getRateLimit() == null) {
                bean.setRateLimit(100); // 默认100次/秒
            }

            String now = CommonDateUtil.now();
            bean.setCreator(creator);
            bean.setCreateTime(now);
            bean.setUpdator(creator);
            bean.setUpdateTime(now);

            int count = tpThirdPartyAppMapper.save(bean);

            // 保存API权限
            if (vo.getApiIds() != null && !vo.getApiIds().isEmpty()) {
                saveAppPermissions(appId, vo.getApiIds(), creator);
            }

            // 返回VO
            TpThirdPartyAppVO result = new TpThirdPartyAppVO();
            BeanUtil.copyProperties(bean, result);
            result.setApiIds(vo.getApiIds());

            LOGGER.info("创建第三方应用成功，应用ID：{}, 应用名称：{}", appId, vo.getAppName());
            return result;
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("新增第三方应用失败！vo:{}, 错误: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增第三方应用失败！");
        }
    }

    /**
     * 更新第三方应用
     *
     * @param vo 应用信息
     * @param updator 修改人
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int update(TpThirdPartyAppVO vo, String updator) {
        try {
            // 检查应用名称是否重复（排除当前应用）
            TpThirdPartyAppQuery query = new TpThirdPartyAppQuery();
            query.setAppName(vo.getAppName());
            List<TpThirdPartyAppVO> existApps = tpThirdPartyAppMapper.getList(query);
            if (existApps != null && !existApps.isEmpty()) {
                for (TpThirdPartyAppVO existApp : existApps) {
                    if (!existApp.getAppId().equals(vo.getAppId())) {
                        throw new TopinfoRuntimeException(-1, "应用名称已存在！");
                    }
                }
            }

            TpThirdPartyApp bean = new TpThirdPartyApp();
            BeanUtil.copyProperties(vo, bean);

            String now = CommonDateUtil.now();
            bean.setUpdator(updator);
            bean.setUpdateTime(now);
            
            // 不更新API Key
            bean.setApiKey(null);

            int count = tpThirdPartyAppMapper.update(bean);

            LOGGER.info("更新第三方应用成功，应用ID：{}", vo.getAppId());
            return count;
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("更新第三方应用失败！vo:{}, 错误: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "更新第三方应用失败！");
        }
    }

    /**
     * 查看第三方应用详情
     *
     * @param appId 应用ID
     * @return 应用详情（包含已授权的API列表）
     */
    @Override
    public TpThirdPartyAppVO view(String appId) {
        try {
            TpThirdPartyAppVO vo = tpThirdPartyAppMapper.view(appId);
            if (vo != null) {
                // 查询已授权的API ID列表
                List<String> apiIds = tpAppApiPermissionMapper.selectApiIdsByAppId(appId);
                vo.setApiIds(apiIds);
            }
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看第三方应用详情失败！appId:{}, 错误: {}", appId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看第三方应用详情失败！");
        }
    }

    /**
     * 删除第三方应用
     *
     * @param appId 应用ID
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int delete(String appId, String operator) {
        try {
            LOGGER.warn("操作人：{}, 删除了第三方应用：{}", operator, appId);

            // 1、删除应用的所有API权限
            tpAppApiPermissionMapper.deleteByAppId(appId);

            // 2、删除应用信息
            TpThirdPartyApp bean = new TpThirdPartyApp();
            bean.setAppId(appId);
            bean.setUpdator(operator);
            bean.setUpdateTime(CommonDateUtil.now());

            int count = tpThirdPartyAppMapper.delete(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("删除第三方应用失败！appId:{}, 错误: {}", appId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除第三方应用失败！");
        }
    }

    /**
     * 重新生成API Key（已废弃，不需要Secret）
     *
     * @param appId 应用ID
     * @param operator 操作人
     * @return 新的API Key
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public String regenerateSecret(String appId, String operator) {
        try {
            // 生成新的API Key
            String newApiKey = generateApiKey();

            TpThirdPartyApp bean = new TpThirdPartyApp();
            bean.setAppId(appId);
            bean.setApiKey(newApiKey);
            bean.setUpdator(operator);
            bean.setUpdateTime(CommonDateUtil.now());

            tpThirdPartyAppMapper.update(bean);

            LOGGER.warn("操作人：{}, 重新生成了应用：{} 的API Key", operator, appId);
            return newApiKey;
        } catch (Exception e) {
            LOGGER.error("重新生成API Key失败！appId:{}, 错误: {}", appId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "重新生成API Key失败！");
        }
    }

    /**
     * 配置应用API权限
     *
     * @param appId 应用ID
     * @param apiIds API ID列表
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int configPermissions(String appId, List<String> apiIds, String operator) {
        try {
            // 先删除该应用的所有权限
            tpAppApiPermissionMapper.deleteByAppId(appId);

            // 批量新增权限关系
            if (apiIds != null && !apiIds.isEmpty()) {
                return saveAppPermissions(appId, apiIds, operator);
            }

            return 0;
        } catch (Exception e) {
            LOGGER.error("配置应用API权限失败！appId:{}, apiIds:{}, 错误: {}", appId, apiIds, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "配置应用API权限失败！");
        }
    }

    /**
     * 查询应用已授权的API列表
     *
     * @param appId 应用ID
     * @return API定义列表
     */
    @Override
    public List<TpApiDefinition> getAppApis(String appId) {
        try {
            List<String> apiIds = tpAppApiPermissionMapper.selectApiIdsByAppId(appId);
            if (apiIds == null || apiIds.isEmpty()) {
                return new ArrayList<>();
            }

            // 根据API ID列表查询API定义
            List<TpApiDefinition> apis = new ArrayList<>();
            for (String apiId : apiIds) {
                TpApiDefinition api = tpApiDefinitionMapper.selectById(apiId);
                if (api != null) {
                    apis.add(api);
                }
            }
            return apis;
        } catch (Exception e) {
            LOGGER.error("查询应用已授权的API列表失败！appId:{}, 错误: {}", appId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询应用已授权的API列表失败！");
        }
    }

    /**
     * 验证API Key并返回应用信息
     *
     * @param apiKey API密钥
     * @return 应用信息
     */
    @Override
    public TpThirdPartyAppVO validateApiKey(String apiKey) {
        try {
            if (StrUtil.isBlank(apiKey)) {
                throw new TopinfoRuntimeException(401, "API Key不能为空！");
            }

            TpThirdPartyApp app = tpThirdPartyAppMapper.selectByApiKey(apiKey);
            if (app == null) {
                throw new TopinfoRuntimeException(401, "无效的API Key！");
            }

            // 检查应用状态
            if (app.getStatus() == null || app.getStatus() != 1) {
                throw new TopinfoRuntimeException(403, "应用已被禁用！");
            }

            // 检查是否过期
            if (StrUtil.isNotBlank(app.getExpireTime())) {
                String now = CommonDateUtil.now();
                if (now.compareTo(app.getExpireTime()) > 0) {
                    throw new TopinfoRuntimeException(403, "应用已过期！");
                }
            }

            TpThirdPartyAppVO vo = new TpThirdPartyAppVO();
            BeanUtil.copyProperties(app, vo);
            return vo;
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("验证API Key失败！apiKey:{}, 错误: {}", apiKey, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(401, "验证API Key失败！");
        }
    }

    @Override
    public TpThirdPartyAppVO getByApiKey(String apiKey) {
        try {
            if (StrUtil.isBlank(apiKey)) {
                return null;
            }

            TpThirdPartyApp app = tpThirdPartyAppMapper.selectByApiKey(apiKey);
            if (app == null) {
                return null;
            }

            TpThirdPartyAppVO vo = new TpThirdPartyAppVO();
            BeanUtil.copyProperties(app, vo);
            return vo;
        } catch (Exception e) {
            LOGGER.error("根据API Key查询应用失败！apiKey:{}, 错误: {}", apiKey, ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * 验证API Secret（已废弃，不需要Secret）
     *
     * @param apiKey API密钥
     * @param apiSecret API密钥（明文）
     * @return 总是返回true
     */
    @Override
    public boolean validateApiSecret(String apiKey, String apiSecret) {
        // 已不需要Secret验证，总是返回true
        return true;
    }

    /**
     * 更新应用最后调用时间
     *
     * @param appId 应用ID
     * @return 影响行数
     */
    @Override
    public int updateLastCallTime(String appId) {
        try {
            String now = CommonDateUtil.now();
            return tpThirdPartyAppMapper.updateLastCallTime(appId, now);
        } catch (Exception e) {
            // 更新最后调用时间失败不影响业务，只记录日志
            LOGGER.error("更新应用最后调用时间失败！appId:{}, 错误: {}", appId, ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    /**
     * 生成API Key
     */
    private String generateApiKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 保存应用权限
     */
    private int saveAppPermissions(String appId, List<String> apiIds, String operator) {
        List<TpAppApiPermission> permissions = new ArrayList<>();
        String now = CommonDateUtil.now();

        for (String apiId : apiIds) {
            TpAppApiPermission permission = new TpAppApiPermission();
            permission.setPermissionId(SnowflakeIdUtil.nextIdStr());
            permission.setAppId(appId);
            permission.setApiId(apiId);
            permission.setCreator(operator);
            permission.setCreateTime(now);
            permissions.add(permission);
        }

        return tpAppApiPermissionMapper.batchInsert(permissions);
    }

    /**
     * 更新应用状态
     *
     * @param appId 应用ID
     * @param status 状态（1:启用 0:禁用）
     * @param updator 修改人
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int updateStatus(String appId, Integer status, String updator) {
        try {
            TpThirdPartyApp bean = new TpThirdPartyApp();
            bean.setAppId(appId);
            bean.setStatus(status);
            bean.setUpdator(updator);
            bean.setUpdateTime(CommonDateUtil.now());

            int count = tpThirdPartyAppMapper.updateStatus(bean);
            LOGGER.info("更新应用状态成功，应用ID：{}, 状态：{}", appId, status);
            return count;
        } catch (Exception e) {
            LOGGER.error("更新应用状态失败！appId:{}, status:{}, 错误: {}", appId, status, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "更新应用状态失败！");
        }
    }
}
