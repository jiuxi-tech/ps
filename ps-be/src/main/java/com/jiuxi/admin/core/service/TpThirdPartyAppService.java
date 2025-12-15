package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.entity.TpApiDefinition;
import com.jiuxi.admin.core.bean.query.TpThirdPartyAppQuery;
import com.jiuxi.admin.core.bean.vo.TpThirdPartyAppVO;

import java.util.List;

/**
 * @ClassName: TpThirdPartyAppService
 * @Description: 第三方应用管理服务
 * @Author system
 * @Date 2024-01-18 11:05:17
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpThirdPartyAppService {

    /**
     * 分页查询第三方应用列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpThirdPartyAppVO> queryPage(TpThirdPartyAppQuery query);

    /**
     * 查询第三方应用列表
     *
     * @param query 查询条件
     * @return 应用列表
     */
    List<TpThirdPartyAppVO> getList(TpThirdPartyAppQuery query);

    /**
     * 新增第三方应用
     *
     * @param vo 应用信息
     * @param creator 创建人
     * @return 应用VO（包含生成的API Key和Secret）
     */
    TpThirdPartyAppVO add(TpThirdPartyAppVO vo, String creator);

    /**
     * 更新第三方应用
     *
     * @param vo 应用信息
     * @param updator 修改人
     * @return 影响行数
     */
    int update(TpThirdPartyAppVO vo, String updator);

    /**
     * 查看第三方应用详情
     *
     * @param appId 应用ID
     * @return 应用详情（包含已授权的API列表）
     */
    TpThirdPartyAppVO view(String appId);

    /**
     * 删除第三方应用
     *
     * @param appId 应用ID
     * @param operator 操作人
     * @return 影响行数
     */
    int delete(String appId, String operator);

    /**
     * 重新生成API Secret
     *
     * @param appId 应用ID
     * @param operator 操作人
     * @return 新的API Secret（明文）
     */
    String regenerateSecret(String appId, String operator);

    /**
     * 配置应用API权限
     *
     * @param appId 应用ID
     * @param apiIds API ID列表
     * @param operator 操作人
     * @return 影响行数
     */
    int configPermissions(String appId, List<String> apiIds, String operator);

    /**
     * 查询应用已授权的API列表
     *
     * @param appId 应用ID
     * @return API定义列表
     */
    List<TpApiDefinition> getAppApis(String appId);

    /**
     * 验证API Key并返回应用信息
     *
     * @param apiKey API密钥
     * @return 应用信息
     */
    TpThirdPartyAppVO validateApiKey(String apiKey);

    /**
     * 根据API Key查询应用信息（不验证状态）
     *
     * @param apiKey API密钥
     * @return 应用信息，如果不存在返回null
     */
    TpThirdPartyAppVO getByApiKey(String apiKey);

    /**
     * 验证API Secret
     *
     * @param apiKey API密钥
     * @param apiSecret API密钥（明文）
     * @return 是否验证通过
     */
    boolean validateApiSecret(String apiKey, String apiSecret);

    /**
     * 更新应用最后调用时间
     *
     * @param appId 应用ID
     * @return 影响行数
     */
    int updateLastCallTime(String appId);

    /**
     * 更新应用状态
     *
     * @param appId 应用ID
     * @param status 状态（1:启用 0:禁用）
     * @param updator 修改人
     * @return 影响行数
     */
    int updateStatus(String appId, Integer status, String updator);
}
