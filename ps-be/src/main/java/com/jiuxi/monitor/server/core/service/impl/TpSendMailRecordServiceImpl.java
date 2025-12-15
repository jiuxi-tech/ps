package com.jiuxi.monitor.server.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.monitor.server.core.bean.entity.TpSendMailRecord;
import com.jiuxi.monitor.server.core.bean.query.TpSendMailRecordQuery;
import com.jiuxi.monitor.server.core.bean.vo.TpSendMailRecordVO;
import com.jiuxi.monitor.server.core.mapper.TpSendMailRecordMapper;
import com.jiuxi.monitor.server.core.service.TpSendMailRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName: TpSendMailRecordServiceImpl
 * @Description: 邮件发送记录表
 * @Author yangzr
 * @Date 2024-11-20 10:36:59
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service
public class TpSendMailRecordServiceImpl implements TpSendMailRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TpSendMailRecordServiceImpl.class);

    @Autowired
    private TpSendMailRecordMapper tpSendMailRecordMapper;

    /**
     * 列表
     *
     * @Author yangzr
     * @Date 2024-11-20 10:36:59
     */
    @Override
    public IPage<TpSendMailRecordVO> queryPage(TpSendMailRecordQuery query) {

        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);

            Page<TpSendMailRecordVO> page = new Page<>(pageNum, pageSize);
            IPage<TpSendMailRecordVO> iPage = tpSendMailRecordMapper.getPage(page, query);

            return iPage;
        } catch (Exception e) {
            LOGGER.error("列表信息查询失败！query:{}, e: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "列表信息查询失败！");
        }

    }

    /**
     * 查看
     *
     * @Author yangzr
     * @Date 2024-11-20 10:36:59
     */
    @Override
    public TpSendMailRecordVO view(String id) {

        try {
            TpSendMailRecordVO vo = tpSendMailRecordMapper.view(id);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！id: {}, e: {}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }


    /**
     * 删除
     *
     * @Author yangzr
     * @Date 2024-11-20 10:36:59
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int deleteByIds(List<String> ids, String jwtpid) {
        String now = CommonDateUtil.now();
        if (null == ids || ids.isEmpty()) {
            throw new TopinfoRuntimeException(-1, "删除数据id不能为空！");
        }
        try {

            ids.forEach(id -> {
                tpSendMailRecordMapper.delete(id, now, jwtpid);
            });
            return ids.size();
        } catch (Exception e) {
            LOGGER.error("删除失败！ids: {}, jwtpid:{}, e: {}", ids, jwtpid, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }

    /**
     * 保存
     *
     * @param user    收件人姓名
     * @param email   收件人邮箱
     * @param bool    是否发送成功
     * @param content 发送内容
     * @return void
     * @author jiuxx
     * @date 2024/11/20 16:45
     */
    @Override
    public void add(String user, String email, boolean bool, String content) {

        TpSendMailRecord bean = new TpSendMailRecord();
        // 设置主键id
        bean.setRecordId(SnowflakeIdUtil.nextIdStr());
        // 创建人
        bean.setCreator("heartbeat");
        String now = CommonDateUtil.now();
        // 创建时间
        bean.setCreateTime(now);
        // 修改人
        bean.setUpdator("heartbeat");
        // 修改时间
        bean.setUpdateTime(now);
        // 是否有效
        bean.setActived(TpConstant.YES);
        bean.setSendTime(now);
        bean.setEmail(email);
        bean.setPersonName(user);
        bean.setStatus(bool ? 1 : 0);
        bean.setMessage(content);
        tpSendMailRecordMapper.add(bean);
    }
}
