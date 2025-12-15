package com.jiuxi.admin.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.util.CommonDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.hutool.core.bean.BeanUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.common.exception.ExceptionUtils;
import cn.hutool.core.util.StrUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.jiuxi.admin.core.mapper.TpAgentDealMapper;
import com.jiuxi.admin.core.bean.entity.TpAgentDeal;
import com.jiuxi.admin.core.bean.vo.TpAgentDealVO;
import com.jiuxi.admin.core.bean.query.TpAgentDealQuery;
import com.jiuxi.admin.core.service.TpAgentDealService;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;

import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

/**
 * @ClassName: TpAgentDealServiceImpl
 * @Description: 代办处理表
 * @Author pand
 * @Date 2021-06-03 14:28:23
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpAgentDealService")
public class TpAgentDealServiceImpl implements TpAgentDealService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpAgentDealServiceImpl.class);

    @Autowired
    private  TpAgentDealMapper  tpAgentDealMapper;

    @Override
    public IPage<TpAgentDealVO> queryPage(TpAgentDealQuery query) {

        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);

            Page<TpAgentDealVO> page = new Page<>(pageNum, pageSize);
            IPage<TpAgentDealVO> iPage = tpAgentDealMapper.getPage(page, query);

            return iPage;
        } catch (Exception e) {
            LOGGER.error("列表信息查询失败！query:{}, 错误:{}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "列表信息查询失败！");
        }

    }

    @Override
    public String add(TpAgentDealVO vo, String jwtpid) {

        // 生成主键

        TpAgentDeal bean = new TpAgentDeal();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        // 创建人
        bean.setCreator(jwtpid);
        // 创建时间
        bean.setCreateTime(CommonDateUtil.now());
        // 是否有效
        bean.setActived(TpConstant.YES);

        try {
            tpAgentDealMapper.add(bean);
            return jwtpid;
        } catch (Exception e) {
            LOGGER.error("新增失败！vo:{}, 错误：{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增失败！");
        }
    }

    @Override
    public int update(TpAgentDealVO vo, String jwtpid) {

        TpAgentDeal bean = new TpAgentDeal();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        bean.setUpdator(jwtpid);
        bean.setUpdateTime(CommonDateUtil.now());
        // TODO

        try {
            int count = tpAgentDealMapper.update(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("修改失败！vo:{}, 错误：{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

    @Override
    public TpAgentDealVO view(String id, String jwtpid) {

        try {
            TpAgentDealVO vo = tpAgentDealMapper.view(id, jwtpid);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！id:{}, 错误：{}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

    @Override
    // @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int deleteByIds(List<String> ids, String jwtpid){

        try {
            if (null == ids || ids.isEmpty()) {
                throw new TopinfoRuntimeException(-1, "删除数据id不能为空！");
            }
            // TODO 删除也可以使用sql批量操作
            ids.forEach(id -> {
                tpAgentDealMapper.delete(id);
            });
            return ids.size();
        } catch (Exception e) {
            LOGGER.error("删除失败！ids:{}, 错误：{}", ids, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }

}
