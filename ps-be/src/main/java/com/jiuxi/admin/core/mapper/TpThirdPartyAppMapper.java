package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpThirdPartyApp;
import com.jiuxi.admin.core.bean.query.TpThirdPartyAppQuery;
import com.jiuxi.admin.core.bean.vo.TpThirdPartyAppVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpThirdPartyAppMapper
 * @Description: 第三方应用管理表
 * @Author system
 * @Date 2024-01-18 11:05:17
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Mapper
public interface TpThirdPartyAppMapper {

    /**
     * 分页查询第三方应用列表
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpThirdPartyAppVO> getPage(Page<TpThirdPartyAppVO> page, @Param("query") TpThirdPartyAppQuery query);

    /**
     * 查询第三方应用列表
     *
     * @param query 查询条件
     * @return 应用列表
     */
    List<TpThirdPartyAppVO> getList(@Param("query") TpThirdPartyAppQuery query);

    /**
     * 新增第三方应用
     *
     * @param bean 应用实体
     * @return 影响行数
     */
    int save(TpThirdPartyApp bean);

    /**
     * 更新第三方应用
     *
     * @param bean 应用实体
     * @return 影响行数
     */
    int update(TpThirdPartyApp bean);

    /**
     * 查看第三方应用详情
     *
     * @param appId 应用ID
     * @return 应用详情
     */
    TpThirdPartyAppVO view(String appId);

    /**
     * 删除第三方应用
     *
     * @param bean 应用实体
     * @return 影响行数
     */
    int delete(TpThirdPartyApp bean);

    /**
     * 根据API Key查询应用
     *
     * @param apiKey API密钥
     * @return 应用信息
     */
    TpThirdPartyApp selectByApiKey(@Param("apiKey") String apiKey);

    /**
     * 更新应用最后调用时间
     *
     * @param appId 应用ID
     * @param lastCallTime 最后调用时间
     * @return 影响行数
     */
    int updateLastCallTime(@Param("appId") String appId, @Param("lastCallTime") String lastCallTime);

    /**
     * 更新应用状态
     *
     * @param bean 应用实体（包含appId, status, updator, updateTime）
     * @return 影响行数
     */
    int updateStatus(TpThirdPartyApp bean);
}
