package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpAgent;
import com.jiuxi.admin.core.bean.query.TpAgentQuery;
import com.jiuxi.admin.core.bean.vo.TpAgentDealVO;
import com.jiuxi.admin.core.bean.vo.TpAgentVO;
import com.jiuxi.admin.core.mapper.TpAgentMapper;
import com.jiuxi.admin.core.service.TpAgentDealService;
import com.jiuxi.admin.core.service.TpAgentService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: TpAgentServiceImpl
 * @Description:
 * @Author yangp
 * @Date 2021-03-24 16:04:29
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpAgentService")
public class TpAgentServiceImpl implements TpAgentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpAgentServiceImpl.class);

    @Autowired
    private TpAgentMapper tpAgentMapper;

    @Autowired
    private TpAgentDealService tpAgentDealService;

    @Override
    public IPage<TpAgentVO> queryPage(TpAgentQuery query) {

        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);

            Page<TpAgentVO> page = new Page<>(pageNum, pageSize);
            IPage<TpAgentVO> iPage = tpAgentMapper.getPage(page, query);

            return iPage;
        } catch (Exception e) {
            LOGGER.error("列表信息查询失败！query:{}, 错误:{}", JSONObject.toJSONString(query), e.getCause());
            throw new TopinfoRuntimeException(-1, "列表信息查询失败！");
        }

    }

    /**
     * 消息提醒
     *
     * @param jwtpid
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpMessageVO>
     * @author 杨攀
     * @date 2021/3/26 10:47
     */
    @Override
    public List<TpAgentVO> remindlist(String jwtaid, String jwtdid, String jwtpid) {
        List<String> list = new ArrayList<>();
        list.add(jwtaid);
        list.add(jwtdid);
        list.add(jwtpid);

        try {
            List<TpAgentVO> tpMessageVOS = tpAgentMapper.remindlist(list);
            return tpMessageVOS;
        } catch (Exception e) {
            LOGGER.error("{} 的消息查询失败，{}", jwtpid, e.getMessage());
            throw new TopinfoRuntimeException(-1, "查询消息失败！");
        }

    }

    @Override
    public String send(TpAgentVO vo, String jwtpid) {

        // 生成主键
        String id = SnowflakeIdUtil.nextIdStr();

        TpAgent bean = new TpAgent();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        // 设置主键id
        bean.setAgId(id);
        // 创建人
        bean.setCreator(jwtpid);
        // 创建时间
        bean.setCreateTime(CommonDateUtil.now());
        // 是否有效
        bean.setActived(TpConstant.YES);

        try {
            tpAgentMapper.add(bean);
            return id;
        } catch (Exception e) {
            LOGGER.error("新增失败！vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "新增失败！" : e.getMessage());
        }
    }

    @Override
    public int update(TpAgentVO vo, String jwtpid) {

        TpAgent bean = new TpAgent();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        bean.setUpdator(jwtpid);
        bean.setUpdateTime(CommonDateUtil.now());
        // TODO

        try {
            int count = tpAgentMapper.update(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("修改失败！vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

    @Override
    public TpAgentDealVO view(String id, String jwtpid) {

        TpAgentDealVO tpAgentDealVO;
        try {
            // 先查询已读表，当前登陆人如果已经读过消息，直接返回已读记录
            tpAgentDealVO = tpAgentDealService.view(jwtpid, id);
            if (null != tpAgentDealVO) {
                return tpAgentDealVO;
            }

            // 如果当前登陆人未读，查出当前消息，存入已读表，同时返回消息信息
            TpAgentVO vo = tpAgentMapper.view(id);
            if (null != vo) {
                tpAgentDealVO = new TpAgentDealVO();
                // 相同的字段进行转换
                BeanUtil.copyProperties(vo, tpAgentDealVO);
                tpAgentDealVO.setPersonId(jwtpid);
                tpAgentDealVO.setDealTime(CommonDateUtil.now());
                tpAgentDealService.add(tpAgentDealVO, jwtpid);
            }

        } catch (Exception e) {
            LOGGER.error("查看失败！id:{}, 错误:{}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
        return tpAgentDealVO;    }

    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int deleteByIds(List<String> ids) {

        try {
            if (null == ids || ids.isEmpty()) {
                throw new TopinfoRuntimeException(-1, "删除数据id不能为空！");
            }
            // TODO 删除也可以使用sql批量操作
            ids.forEach(id -> {
                tpAgentMapper.delete(id);
            });
            return ids.size();
        } catch (Exception e) {
            // 手动事务回滚
            LOGGER.error("删除失败！ids:{}, 错误:{}", ids, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }

}
