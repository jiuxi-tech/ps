package com.jiuxi.admin.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.mapper.TpTraceMapper;
import com.jiuxi.admin.core.service.TpTraceService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.hutool.core.bean.BeanUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.transaction.annotation.Transactional;

import com.jiuxi.admin.core.bean.entity.TpTrace;
import com.jiuxi.admin.core.bean.vo.TpTraceVO;
import com.jiuxi.admin.core.bean.query.TpTraceQuery;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;

/**
 * @ClassName: TpTraceServiceImpl
 * @Description: 修改痕迹表
 * @Author yangp
 * @Date 2021-02-26 15:48:55
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpTraceService")
public class TpTraceServiceImpl extends ServiceImpl<TpTraceMapper, TpTrace> implements TpTraceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpTraceServiceImpl.class);

    @Autowired
    private  TpTraceMapper  tpTraceMapper;

    @Override
    public IPage<TpTraceVO> queryPage(TpTraceQuery query) {

        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);

            Page<TpTraceVO> page = new Page<>(pageNum, pageSize);
            IPage<TpTraceVO> iPage = tpTraceMapper.getPage(page, query);

            return iPage;
        } catch (Exception e) {
            LOGGER.error("列表信息查询失败！query:{}, 错误: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "列表信息查询失败！");
        }

    }

    @Override
    public String add(TpTraceVO vo, String jwtpid) {

        // 生成主键
        String id = SnowflakeIdUtil.nextIdStr();

        TpTrace bean = new TpTrace();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        // 创建人
        bean.setCreator(jwtpid);
        // 创建时间
        bean.setCreateTime(CommonDateUtil.now());
        // 是否有效
        bean.setActived(TpConstant.YES);

        try {
            tpTraceMapper.add(bean);
            return id;
        } catch (Exception e) {
            LOGGER.error("新增失败！vo:{}, 错误: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "新增失败！" : e.getMessage());
        }
    }

    @Override
    public int update(TpTraceVO vo, String jwtpid) {

        TpTrace bean = new TpTrace();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);

        try {
            int count = tpTraceMapper.add(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("修改失败！vo:{}, 错误: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

    @Override
    public TpTraceVO view(String id) {

        try {
            TpTraceVO vo = tpTraceMapper.view(id);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！id:{}, 错误: {}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int deleteByIds(ArrayList<String> ids){

        try {
            if (null == ids || ids.isEmpty()) {
                throw new TopinfoRuntimeException(-1, "删除数据id不能为空！");
            }
            ids.forEach(id -> {
                tpTraceMapper.delete(id);
            });
            return ids.size();
        } catch (Exception e) {
            LOGGER.error("删除失败！ids:{}, 错误: {}", ids, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }

}
