package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpAccountThird;
import com.jiuxi.admin.core.bean.query.TpAccountThirdQuery;
import com.jiuxi.admin.core.bean.vo.TpAccountThirdVO;
import com.jiuxi.admin.core.mapper.TpAccountThirdMapper;
import com.jiuxi.admin.core.service.TpAccountThirdService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.PwdRegularUtils;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: TpAccountThridServiceImpl
 * @Description: 合作方管理表
 * @Author pand
 * @Date 2022-04-20 15:02:39
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("iotThirdAccountService")
public class TpAccountThirdServiceImpl implements TpAccountThirdService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TpAccountThirdServiceImpl.class);

    @Autowired
    private TpAccountThirdMapper tpAccountThirdMapper;

    @Override
    public IPage<TpAccountThirdVO> queryPage(TpAccountThirdQuery query) {

        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);

            Page<TpAccountThirdVO> page = new Page<>(pageNum, pageSize);
            IPage<TpAccountThirdVO> iPage = tpAccountThirdMapper.getPage(page, query);

            return iPage;
        } catch (Exception e) {
            LOGGER.error("列表信息查询失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "列表信息查询失败！");
        }

    }

    @Override
    public String add(TpAccountThirdVO vo, String jwtpid) {

        // 生成主键
        String id = SnowflakeIdUtil.nextIdStr();
        String now = CommonDateUtil.now();

        TpAccountThird bean = new TpAccountThird();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        // 设置主键id
        bean.setAppKey(id);
        // 创建人
        bean.setCreator(jwtpid);
        // 创建时间
        bean.setCreateTime(now);
        // 修改人
        bean.setUpdator(jwtpid);
        // 修改时间
        bean.setUpdateTime(now);
        // 是否有效
        bean.setActived(TpConstant.YES);
        // 生成appSecret
        bean.setAppSecret(PwdRegularUtils.randomPwd(32));
        try {
            tpAccountThirdMapper.add(bean);
            return id;
        } catch (Exception e) {
            LOGGER.error("新增失败！vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增失败！");
        }
    }

    @Override
    public int reset(String appKey, String jwtpid) {

        TpAccountThird bean = new TpAccountThird();
        bean.setAppKey(appKey);
        bean.setAppSecret(PwdRegularUtils.randomPwd(32));
        bean.setUpdator(jwtpid);
        bean.setUpdateTime(CommonDateUtil.now());
        try {
            int count = tpAccountThirdMapper.reset(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("重置secret修改失败！appKey:{}, 错误：{}", appKey, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

    @Override
    public TpAccountThirdVO view(String id) {

        try {
            TpAccountThirdVO vo = tpAccountThirdMapper.view(id);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！id:{}, 错误:{}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

    @Override
    // @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int deleteByIds(List<String> ids, String jwtpid) {
        String now = CommonDateUtil.now();

        try {
            if (null == ids || ids.isEmpty()) {
                throw new TopinfoRuntimeException(-1, "删除数据id不能为空！");
            }
            // TODO 删除也可以使用sql批量操作
            ids.forEach(id -> {
                tpAccountThirdMapper.delete(id, now, jwtpid);
            });
            return ids.size();
        } catch (Exception e) {
            LOGGER.error("删除失败！ids:{}, 错误: {}0", ids, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }

}
