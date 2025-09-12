package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.bean.entity.TpRichtext;
import com.jiuxi.admin.core.bean.vo.TpRichtextVO;
import com.jiuxi.admin.core.mapper.TpRichtextMapper;
import com.jiuxi.admin.core.service.TpRichtextService;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 富文本内容表，所有设计富文本的业务，富文本内容，存入该表。通过referId与业务数据一一对应
 *
 * @ClassName: TpRichtextServiceImpl
 * @Description:
 * @Author pand
 * @Date 2021-04-27 14:29:12
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpRichtextService")
public class TpRichtextServiceImpl implements TpRichtextService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpRichtextServiceImpl.class);

    @Autowired
    private TpRichtextMapper tpRichtextMapper;
    /**
     * 新增
     *
     * @param referId 业务id
     * @param content 内容
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    @Override
    public String add(String referId, String content) {

        return this.add(referId, "", content);
    }
    /**
     * 新增
     *
     * @param referId 业务id
     * @param content 内容
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    @Override
    public String add(String referId, String txtType, String content) {
        TpRichtext bean = new TpRichtext();
        // 生成主键
        String id = SnowflakeIdUtil.nextIdStr();
        // 设置主键id
        bean.setTxtId(id);
        bean.setReferId(referId);
        bean.setTxtType(txtType);
        bean.setContent(content);

        try {
            tpRichtextMapper.add(bean);
            return id;
        } catch (Exception e) {
            LOGGER.error("新增失败！referId:{}, txtType:{}, content:{}, e: {}", referId, txtType, content, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "新增失败！" : e.getMessage());
        }
    }

    /**
     * 根据业务id修改
     *
     * @param referId 业务id
     * @param content 内容
     * @return int
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    @Override
    public int update(String referId, String content) {
        return this.update(referId, "", content);
    }

    /**
     * 根据业务id和分类修改
     *
     * @param referId 业务id
     * @param txtType 业务分类
     * @param content 内容
     * @return int
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    @Override
    public int update(String referId, String txtType, String content) {
        TpRichtext bean = new TpRichtext();
        bean.setReferId(referId);
        bean.setTxtType(txtType);
        bean.setContent(content);
        try {
            int count = tpRichtextMapper.update(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("修改失败！referId:{}, txtType:{}, content:{}, e: {}", referId, txtType, content, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

    /**
     * 根据业务id查询
     *
     * @param referId
     * @return com.jiuxi.plugin.api.bean.dto.TpRichtextDTO
     * @author 杨占锐
     * @date 2024/9/2 15:25
     */
    @Override
    public TpRichtextVO selectByReferId(String referId) {

        try {
            TpRichtextVO vo = tpRichtextMapper.selectByReferId(referId);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！referId:{}, 错误: {}", referId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

    /**
     * 根据业务id和分类查询
     *
     * @param referId
     * @return com.jiuxi.plugin.api.bean.dto.TpRichtextDTO
     * @author 杨占锐
     * @date 2024/9/2 15:25
     */
    @Override
    public TpRichtextVO selectByReferId(String referId, String txtType) {
        try {
            TpRichtextVO vo = tpRichtextMapper.selectByReferIdAndType(referId, txtType);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！referId:{}, txtType:{}, 错误: {}", referId, txtType, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

    /**
     * 查看
     *
     * @param id
     * @return com.jiuxi.plugin.api.bean.dto.TpRichtextDTO
     * @author 杨占锐
     * @date 2024/9/2 15:24
     */
    @Override
    public TpRichtextVO view(String id) {

        try {
            TpRichtextVO vo = tpRichtextMapper.view(id);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看失败！id:{}, 错误: {}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

    /**
     * 根据id删除
     *
     * @param referId
     * @return void
     * @author 杨占锐
     * @date 2024/9/2 15:27
     */
    @Override
    public void deleteByReferId(String referId) {

        try {
            if (StrUtil.isBlank(referId)) {
                throw new TopinfoRuntimeException(-1, "删除数据id不能为空！");
            }

            tpRichtextMapper.deleteByReferId(referId);

        } catch (Exception e) {
            LOGGER.error("删除失败！referId:{}, 错误: {}", referId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }

    /**
     * 根据id和类型删除
     *
     * @param referId
     * @return void
     * @author 杨占锐
     * @date 2024/9/2 15:27
     */
    @Override
    public void deleteByReferId(String referId, String txtType) {

        try {
            if (StrUtil.isBlank(referId)) {
                throw new TopinfoRuntimeException(-1, "删除数据id不能为空！");
            }

            tpRichtextMapper.deleteByReferIdAndType(referId, txtType);

        } catch (Exception e) {
            LOGGER.error("删除失败！referId:{}, txtType:{}, 错误: {}", referId, txtType, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除失败！");
        }
    }

}
