package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpParameterConfigQuery;
import com.jiuxi.admin.core.bean.vo.TpParameterConfigVO;

/**
 * @ClassName: TpParameterConfigService
 * @Description: 参数配置表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpParameterConfigService {

    /**
     * 需求分析:
     * 1、查询全局参数列表
     * <p>
     * 业务逻辑:
     * 1、根据查询条件查询公共参数或者全局动态参数,
     * <p>
     * 实现逻辑:
     * 1、根据 pmKey 精确查询 tp_parameter_config 表对应的数据
     * 2、根据 pmName 模糊查询 tp_parameter_config 表对应的数据
     *
     * @param query
     * @return
     * @author pand
     * @date 2023-09-12 17:15
     */
    IPage<TpParameterConfigVO> queryPage(TpParameterConfigQuery query);

    /**
     * 需求分析:
     * 1、配置系统一些公共参数或者全局动态参数,如app下载地址,iot全局接入路数配置等.
     * <p>
     * 业务逻辑:
     * 1、新增公共参数或者全局动态参数,
     * <p>
     * 实现逻辑:
     * 1、操作 tp_parameter_config 表,插入新增数据
     * 2、有效的pmKey保持唯一
     *
     * @param vo
     * @return
     * @author pand
     * @date 2023-09-12 17:15
     */
    String add(TpParameterConfigVO vo);

    /**
     * 需求分析:
     * 1、修改系统一些公共参数或者全局动态参数,如app下载地址,iot全局接入路数配置等.
     * <p>
     * 业务逻辑:
     * 1、修改公共参数或者全局动态参数,
     * <p>
     * 实现逻辑:
     * 1、根据 pmId 主键修改 tp_parameter_config 表对应的数据
     * 2、pmKey可以修改,但必须保持唯一
     *
     * @param vo
     * @return
     * @author pand
     * @date 2023-09-12 17:15
     */
    int update(TpParameterConfigVO vo);

    /**
     * 需求分析:
     * 1、查看全局参数
     * <p>
     * 业务逻辑:
     * 1、根据id查看公共参数或者全局动态参数,
     * <p>
     * 实现逻辑:
     * 1、根据 pmId 查询 tp_parameter_config 表对应的数据
     *
     * @param id
     * @return
     * @author pand
     * @date 2023-09-12 17:15
     */
    TpParameterConfigVO view(String id);

    /**
     * 需求分析:
     * 1、删除全局参数
     * <p>
     * 业务逻辑:
     * 1、根据id查看公共参数或者全局动态参数,
     * <p>
     * 实现逻辑:
     * 1、根据 pmId 删除 tp_parameter_config 表对应的数据
     * 2、不支持批量删除
     *
     * @param id
     * @param jwtpid
     * @return
     * @author pand
     * @date 2023-09-12 17:15
     */
    String deleteById(String id, String jwtpid);

    /**
     * 根据pmKey查询配置信息
     *
     * @param pmKey
     * @return com.jiuxi.admin.core.bean.vo.TpParameterConfigVO
     * @author pand
     * @date 2021-08-12 17:15
     */
    TpParameterConfigVO viewByPmKey(String pmKey);
}

