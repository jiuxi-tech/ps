package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpApiDefinition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpApiDefinitionMapper
 * @Description: API定义表
 * @Author system
 * @Date 2024-01-18 11:05:17
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Mapper
public interface TpApiDefinitionMapper {

    /**
     * 查询所有API定义列表
     *
     * @return API列表
     */
    List<TpApiDefinition> selectAll();

    /**
     * 根据状态查询API定义列表
     *
     * @param status 状态（1:启用 0:禁用）
     * @return API列表
     */
    List<TpApiDefinition> selectByStatus(@Param("status") Integer status);

    /**
     * 根据分类查询API定义列表
     *
     * @param category 分类
     * @return API列表
     */
    List<TpApiDefinition> selectByCategory(@Param("category") String category);

    /**
     * 根据API ID查询API定义
     *
     * @param apiId API ID
     * @return API定义
     */
    TpApiDefinition selectById(@Param("apiId") String apiId);

    /**
     * 根据API编码查询API定义
     *
     * @param apiCode API编码
     * @return API定义
     */
    TpApiDefinition selectByApiCode(@Param("apiCode") String apiCode);

    /**
     * 根据API路径和HTTP方法查询API定义
     *
     * @param apiPath API路径
     * @param httpMethod HTTP方法
     * @return API定义
     */
    TpApiDefinition selectByPathAndMethod(@Param("apiPath") String apiPath, @Param("httpMethod") String httpMethod);

    /**
     * 新增API定义
     *
     * @param bean API定义实体
     * @return 影响行数
     */
    int insert(TpApiDefinition bean);

    /**
     * 更新API定义
     *
     * @param bean API定义实体
     * @return 影响行数
     */
    int update(TpApiDefinition bean);

    /**
     * 删除API定义
     *
     * @param apiId API ID
     * @return 影响行数
     */
    int delete(@Param("apiId") String apiId);
}
