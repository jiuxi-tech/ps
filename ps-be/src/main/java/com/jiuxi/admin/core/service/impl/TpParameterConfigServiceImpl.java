package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpParameterConfig;
import com.jiuxi.admin.core.bean.query.TpParameterConfigQuery;
import com.jiuxi.admin.core.bean.vo.TpParameterConfigVO;
import com.jiuxi.admin.core.mapper.TpParameterConfigMapper;
import com.jiuxi.admin.core.service.TpParameterConfigService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @ClassName: TpParameterConfigServiceImpl
 * @Description: 参数配置表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpParameterConfigService")
public class TpParameterConfigServiceImpl implements TpParameterConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpParameterConfigServiceImpl.class);

    @Autowired
    private TpParameterConfigMapper tpParameterConfigMapper;

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
    @Override
    public IPage<TpParameterConfigVO> queryPage(TpParameterConfigQuery query) {

        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);

            Page<TpParameterConfigVO> page = new Page<>(pageNum, pageSize);
            IPage<TpParameterConfigVO> iPage = tpParameterConfigMapper.getPage(page, query);

            return iPage;
        } catch (Exception e) {
            LOGGER.error("列表信息查询失败！query:{}, e: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "列表信息查询失败！");
        }
    }

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
    @Override
    @CacheEvict(cacheNames = "platform.{TpParameterConfigService}$[86400]", allEntries = true)
    public String add(TpParameterConfigVO vo) {

        TpParameterConfigVO oldVO = this.viewByPmKey(vo.getPmKey());
        if (null != oldVO) {
            throw new TopinfoRuntimeException(-1, "pmKey已存在！");
        }

        // 生成主键
        String id = SnowflakeIdUtil.nextIdStr();

        TpParameterConfig bean = new TpParameterConfig();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        // 设置主键id
        bean.setPmId(id);
        // 是否有效
        bean.setActived(TpConstant.YES);

        try {
            tpParameterConfigMapper.add(bean);
            return id;
        } catch (DuplicateKeyException ke){
            LOGGER.error("新增失败, pmKey已存在！pmKey:{}, e: {}", vo.getPmKey(), ExceptionUtils.getStackTrace(ke));
            throw new TopinfoRuntimeException(-1, "pmKey已存在！");
        } catch (Exception e) {
            LOGGER.error("新增失败！vo: {}, e: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增失败！");
        }
    }

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
    @Override
    @CacheEvict(cacheNames = "platform.{TpParameterConfigService}$[86400]", allEntries = true)
    public int update(TpParameterConfigVO vo) {

        TpParameterConfigVO oldVO = this.viewByPmKey(vo.getPmKey());
        if (null != oldVO && !StrUtil.equals(vo.getPmId(), oldVO.getPmId())) {
            throw new TopinfoRuntimeException(-1, "pmKey已存在！");
        }

        TpParameterConfig bean = new TpParameterConfig();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);

        try {
            int count = tpParameterConfigMapper.update(bean);
            return count;
        } catch (DuplicateKeyException ke){
            LOGGER.error("新增失败, pmKey已存在！pmKey:{}, e: {}", vo.getPmKey(), ExceptionUtils.getStackTrace(ke));
            throw new TopinfoRuntimeException(-1, "pmKey已存在！");
        } catch (Exception e) {
            LOGGER.error("修改失败！vo: {}, e: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

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
    @Override
    @Cacheable(cacheNames = "platform.{TpParameterConfigService}$[86400]", key = "#root.methodName + ':' + #id")
    public TpParameterConfigVO view(String id) {
        try {

            TpParameterConfigVO vo = tpParameterConfigMapper.view(id);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！id: {}, e: {}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

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
    @Override
    @CacheEvict(cacheNames = "platform.{TpParameterConfigService}$[86400]", allEntries = true)
    public String deleteById(String id, String jwtpid) {
        try {
            LOGGER.info("jwtpid: {} 删除了id = {} 的配置", jwtpid, id);

            TpParameterConfigVO view = tpParameterConfigMapper.view(id);
            if (view == null) {
                return id;
            }

            // 删除也可以使用sql批量操作
            tpParameterConfigMapper.delete(id, view.getPmKey() + CommonDateUtil.now());

            return id;
        } catch (Exception e) {
            LOGGER.error("删除失败！id: {}, jwtpid:{}, e: {}", id, jwtpid, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }

    /**
     * 根据pmKey查询配置信息
     *
     * @param pmKey
     * @return com.jiuxi.admin.core.bean.vo.TpParameterConfigVO
     * @author pand
     * @date 2021-08-12 17:15
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpParameterConfigService}$[86400]", key = "#root.methodName + ':' + #pmKey")
    public TpParameterConfigVO viewByPmKey(String pmKey) {
        TpParameterConfigVO tpParameterConfigVO = tpParameterConfigMapper.viewByPmKey(pmKey);
        return tpParameterConfigVO;
    }

}
