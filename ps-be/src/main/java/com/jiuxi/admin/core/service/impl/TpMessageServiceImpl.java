package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpMessage;
import com.jiuxi.admin.core.bean.query.TpMessageQuery;
import com.jiuxi.admin.core.bean.vo.TpMessageReadVO;
import com.jiuxi.admin.core.bean.vo.TpMessageVO;
import com.jiuxi.admin.core.mapper.TpMessageMapper;
import com.jiuxi.admin.core.service.TpAttachinfoService;
import com.jiuxi.admin.core.service.TpMessageReadService;
import com.jiuxi.admin.core.service.TpMessageService;
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
 * @ClassName: TpMessageServiceImpl
 * @Description:
 * @Author yangp
 * @Date 2021-03-24 16:04:29
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpMessageService")
public class TpMessageServiceImpl implements TpMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpMessageServiceImpl.class);

    @Autowired
    private TpMessageMapper tpMessageMapper;

    @Autowired
    private TpAttachinfoService tpAttachinfoService;

    @Autowired
    private TpMessageReadService tpMessageReadService;

    @Override
    public IPage<TpMessageVO> queryPage(TpMessageQuery query, String jwtpid) {

        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);

            // 查询自己创建的
            query.setCreator(jwtpid);

            Page<TpMessageVO> page = new Page<>(pageNum, pageSize);
            IPage<TpMessageVO> iPage = tpMessageMapper.getPage(page, query);

            return iPage;
        } catch (Exception e) {
            LOGGER.error("列表信息查询失败！query:{}", e.getCause());
            throw new TopinfoRuntimeException(-1, "列表信息查询失败！");
        }

    }


    /**
     * 消息发送
     *
     * @param tpMessage
     * @return boolean
     * @author 杨攀
     * @date 2021/3/25 20:44
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean send(TpMessageVO tpMessage) {

        // 生成主键
        String id = SnowflakeIdUtil.nextIdStr();
        String createTime = CommonDateUtil.now();

        TpMessage bean = new TpMessage();
        // 转换成数据库对象
        BeanUtil.copyProperties(tpMessage, bean);
        // 设置主键id
        bean.setMsgId(id);
        // 创建时间
        bean.setCreateTime(createTime);
        // 是否有效
        bean.setActived(TpConstant.YES);
        // 修改时间
        bean.setUpdateTime(createTime);

        try {

            // 附件绑定
            tpAttachinfoService.band(tpMessage.getFile(), id, createTime);

            tpMessageMapper.add(bean);
            return true;
        } catch (Exception e) {
            LOGGER.error("新增失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "新增失败！" : e.getMessage());
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
    public List<TpMessageVO> remindlist(String jwtaid, String jwtdid, String jwtpid) {
        List<String> list = new ArrayList<>();
        list.add(jwtaid);
        list.add(jwtdid);
        list.add(jwtpid);

        try {
            List<TpMessageVO> tpMessageVOS = tpMessageMapper.remindlist(list);
            return tpMessageVOS;
        } catch (Exception e) {
            LOGGER.error("{} 的消息查询失败，{}", jwtpid, e.getMessage());
            throw new TopinfoRuntimeException(-1, "查询消息失败！");
        }

    }

    @Override
    public int update(TpMessageVO vo, String jwtpid) {

        TpMessage bean = new TpMessage();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        bean.setUpdator(jwtpid);
        bean.setUpdateTime(CommonDateUtil.now());
        // TODO

        try {
            int count = tpMessageMapper.update(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("修改失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public TpMessageReadVO remind(String id, String jwtpid) {
        TpMessageReadVO tpMessageReadVo;
        try {
            // 先查询已读表，当前登陆人如果已经读过消息，直接返回已读记录
            tpMessageReadVo = tpMessageReadService.view(jwtpid, id);
            if (null != tpMessageReadVo) {
                return tpMessageReadVo;
            }

            // 如果当前登陆人未读，查出当前消息，存入已读表，同时返回消息信息
            TpMessageVO vo = tpMessageMapper.view(id);
            if (null != vo) {
                tpMessageReadVo = new TpMessageReadVO();
                // 相同的字段进行转换
                BeanUtil.copyProperties(vo, tpMessageReadVo);
                tpMessageReadVo.setPersonId(jwtpid);
                tpMessageReadVo.setReadTime(CommonDateUtil.now());
                tpMessageReadService.add(tpMessageReadVo, jwtpid);
            }

        } catch (Exception e) {
            LOGGER.error("查看失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
        return tpMessageReadVo;
    }

    @Override
    public int deleteByIds(List<String> ids) {

        try {
            if (null == ids || ids.isEmpty()) {
                throw new TopinfoRuntimeException(-1, "删除数据id不能为空！");
            }
            // TODO 删除也可以使用sql批量操作
            ids.forEach(id -> {
                tpMessageMapper.delete(id);
            });
            return ids.size();
        } catch (Exception e) {
            LOGGER.error("删除失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }

}
