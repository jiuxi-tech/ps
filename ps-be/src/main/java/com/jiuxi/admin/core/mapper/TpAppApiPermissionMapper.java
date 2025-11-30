package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpAppApiPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpAppApiPermissionMapper
 * @Description: 应用API权限关联表
 * @Author system
 * @Date 2024-01-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpAppApiPermissionMapper {

    /**
     * 根据应用ID查询API权限列表
     *
     * @param appId 应用ID
     * @return API权限列表
     */
    List<TpAppApiPermission> selectByAppId(@Param("appId") String appId);

    /**
     * 根据应用ID查询API ID列表
     *
     * @param appId 应用ID
     * @return API ID列表
     */
    List<String> selectApiIdsByAppId(@Param("appId") String appId);

    /**
     * 根据API ID查询应用权限列表
     *
     * @param apiId API ID
     * @return 应用权限列表
     */
    List<TpAppApiPermission> selectByApiId(@Param("apiId") String apiId);

    /**
     * 检查应用是否有API访问权限
     *
     * @param appId 应用ID
     * @param apiId API ID
     * @return 权限记录
     */
    TpAppApiPermission checkPermission(@Param("appId") String appId, @Param("apiId") String apiId);

    /**
     * 新增应用API权限
     *
     * @param bean 权限实体
     * @return 影响行数
     */
    int insert(TpAppApiPermission bean);

    /**
     * 批量新增应用API权限
     *
     * @param list 权限列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<TpAppApiPermission> list);

    /**
     * 删除应用API权限
     *
     * @param permissionId 权限ID
     * @return 影响行数
     */
    int delete(@Param("permissionId") String permissionId);

    /**
     * 根据应用ID删除所有权限
     *
     * @param appId 应用ID
     * @return 影响行数
     */
    int deleteByAppId(@Param("appId") String appId);

    /**
     * 根据API ID删除所有权限
     *
     * @param apiId API ID
     * @return 影响行数
     */
    int deleteByApiId(@Param("apiId") String apiId);

    /**
     * 删除指定应用的指定API权限
     *
     * @param appId 应用ID
     * @param apiId API ID
     * @return 影响行数
     */
    int deleteByAppIdAndApiId(@Param("appId") String appId, @Param("apiId") String apiId);
}
