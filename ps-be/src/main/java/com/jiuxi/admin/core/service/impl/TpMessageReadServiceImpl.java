package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpMessageRead;
import com.jiuxi.admin.core.bean.query.TpMessageReadQuery;
import com.jiuxi.admin.core.bean.vo.TpMessageReadVO;
import com.jiuxi.admin.core.mapper.TpMessageReadMapper;
import com.jiuxi.admin.core.service.TpMessageReadService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: TpMessageReadServiceImpl
 * @Description: 消息、代办 已读表
 * @Author pand
 * @Date 2021-05-28 15:04:39
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpMessageReadService")
public class TpMessageReadServiceImpl implements TpMessageReadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpMessageReadServiceImpl.class);

    @Autowired
    private TpMessageReadMapper tpMessageReadMapper;

    @Override
    public IPage<TpMessageReadVO> queryPage(TpMessageReadQuery query) {

        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);

            Page<TpMessageReadVO> page = new Page<>(pageNum, pageSize);
            IPage<TpMessageReadVO> iPage = tpMessageReadMapper.getPage(page, query);

            return iPage;
        } catch (Exception e) {
            LOGGER.error("列表信息查询失败！query:{}, 错误:{}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "列表信息查询失败！");
        }

    }

    @Override
    public String add(TpMessageReadVO vo, String jwtpid) {


        TpMessageRead bean = new TpMessageRead();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        // 创建时间
        bean.setCreateTime(CommonDateUtil.now());
        // 是否有效
        bean.setActived(TpConstant.YES);

        try {
            tpMessageReadMapper.add(bean);
            return jwtpid;
        } catch (Exception e) {
            LOGGER.error("新增失败！vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增失败！");
        }
    }

    @Override
    public int update(TpMessageReadVO vo, String jwtpid) {

        TpMessageRead bean = new TpMessageRead();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        // TODO

        try {
            int count = tpMessageReadMapper.update(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("修改失败！vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

    @Override
    public TpMessageReadVO view(String personId, String msgId) {

        try {
            TpMessageReadVO vo = tpMessageReadMapper.view(personId, msgId);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

    @Override
    // @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int deleteByIds(List<String> ids) {

        try {
            if (null == ids || ids.isEmpty()) {
                throw new TopinfoRuntimeException(-1, "删除数据id不能为空！");
            }
            // TODO 删除也可以使用sql批量操作
            ids.forEach(id -> {
                tpMessageReadMapper.delete(id);
            });
            return ids.size();
        } catch (Exception e) {
            LOGGER.error("删除失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }

}
