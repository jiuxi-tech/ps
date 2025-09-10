package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpCustomForm;
import com.jiuxi.admin.core.bean.vo.TpCustomFormVO;
import com.jiuxi.admin.core.mapper.TpCustomFormMapper;
import com.jiuxi.admin.core.service.TpCustomFormService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: TpCustomFormServiceImpl
 * @Description: 表单设计表 表单设计表
 * @Author pand
 * @Date 2021-05-11 11:22:40
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpCustomFormService")
public class TpCustomFormServiceImpl implements TpCustomFormService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpCustomFormServiceImpl.class);

    @Autowired
    private TpCustomFormMapper tpCustomFormMapper;

    /**
     * 创建业务表单，如list表单，edit表单等。这里list表单需要同时生成查询sql。
     *
     * @param vo
     * @param jwtpid
     * @return java.lang.String
     * @author pand
     * @date 2021-08-18 15:51
     */
    @Override
    public String update(TpCustomFormVO vo, String jwtpid) {
        TpCustomForm bean = new TpCustomForm();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);

        String currentTime = CommonDateUtil.now();
        String fid = vo.getFid();

        if (StrUtil.isBlank(fid)) {
            // 判断同类型的是否已存在
            TpCustomFormVO tpCustomFormVO = tpCustomFormMapper.view(bean.getMid(), bean.getFtype(), bean.getFSource());
            if (null != tpCustomFormVO) {
                throw new TopinfoRuntimeException(-1, StrUtil.format("该模块已存在 {} 的 {} 类型表单！", bean.getFSource(), bean.getFtype()));
            }

            fid = SnowflakeIdUtil.nextIdStr();
            // 设置主键id
            bean.setFid(fid);
            // 创建人
            bean.setCreator(jwtpid);
            // 创建时间
            bean.setCreateTime(currentTime);
            // 是否有效
            bean.setActived(TpConstant.YES);

            try {
                tpCustomFormMapper.add(bean);
            } catch (Exception e) {
                LOGGER.error("新增失败！vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
                throw new TopinfoRuntimeException(-1, "新增失败！");
            }
        } else {
            bean.setUpdator(jwtpid);
            bean.setUpdateTime(currentTime);
            try {
                tpCustomFormMapper.update(bean);
            } catch (Exception e) {
                LOGGER.error("修改失败！vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
                throw new TopinfoRuntimeException(-1, "修改失败！");
            }
        }

        return fid;
    }

    @Override
    public TpCustomFormVO view(String mid, String ftype, String fSource) {

        try {
            TpCustomFormVO vo = tpCustomFormMapper.view(mid, ftype, fSource);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！mid:{}, ftype:{}, 错误:{}", mid, ftype, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

    /**
     * 根据模块编码，菜单类型，资源类型查询表单设计信息以及数据信息
     *
     * @param mcode   模块编码
     * @param ftype
     * @param fSource
     * @return com.jiuxi.admin.core.bean.vo.TpCustomFormVO
     * @author pand
     * @date 2021-09-24 11:05
     */
    @Override
    public TpCustomFormVO viewByMcode(String mcode, String ftype, String fSource) {
        try {
            TpCustomFormVO vo = tpCustomFormMapper.viewByMcode(mcode, ftype, fSource);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！mcode:{}, ftype:{}, fSource:{}, 错误:{}", mcode, ftype, fSource, ExceptionUtils.getStackTrace(e));

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
                tpCustomFormMapper.delete(id);
            });
            return ids.size();
        } catch (Exception e) {
            // 手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            LOGGER.error("删除失败！ids:{}, 错误: {}", ids, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }

}
