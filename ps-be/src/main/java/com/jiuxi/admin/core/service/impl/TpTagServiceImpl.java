package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpPersonTag;
import com.jiuxi.admin.core.bean.entity.TpTag;
import com.jiuxi.admin.core.bean.query.TpTagQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonTagVO;
import com.jiuxi.admin.core.bean.vo.TpTagVO;
import com.jiuxi.admin.core.mapper.TpPersonTagMapper;
import com.jiuxi.admin.core.mapper.TpTagMapper;
import com.jiuxi.admin.core.service.TpTagService;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: TpTagServiceImpl
 * @Description: 标签表
 * @Author system
 * @Date 2024-01-18 11:05:17
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpTagService")
public class TpTagServiceImpl implements TpTagService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpTagServiceImpl.class);

    @Autowired
    private TpTagMapper tpTagMapper;

    @Autowired
    private TpPersonTagMapper tpPersonTagMapper;

    /**
     * 分页查询标签列表
     *
     * @param query 查询条件
     * @param tenantId 租户ID
     * @param orgId 机构ID
     * @return 分页结果
     */
    @Override
    public IPage<TpTagVO> queryPage(TpTagQuery query, String tenantId, String orgId) {
        try {
            // 设置租户和机构信息
            query.setTenantId(tenantId);
            query.setOrgId(orgId);
            
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);
            
            Page<TpTagVO> page = new Page<>(pageNum, pageSize);
            IPage<TpTagVO> iPage = tpTagMapper.getPage(page, query);
            return iPage;
        } catch (Exception e) {
            LOGGER.error("标签列表查询失败！query:{}, 错误: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "标签列表查询失败！");
        }
    }

    /**
     * 查询标签列表
     *
     * @param query 查询条件
     * @param tenantId 租户ID
     * @param orgId 机构ID
     * @return 标签列表
     */
    @Override
    public List<TpTagVO> getList(TpTagQuery query, String tenantId, String orgId) {
        try {
            // 设置租户和机构信息
            query.setTenantId(tenantId);
            query.setOrgId(orgId);
            
            List<TpTagVO> list = tpTagMapper.getList(query);
            return list;
        } catch (Exception e) {
            LOGGER.error("标签列表查询失败！query:{}, 错误: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "标签列表查询失败！");
        }
    }

    /**
     * 新增标签
     *
     * @param vo 标签信息
     * @param creator 创建人
     * @param tenantId 租户ID
     * @param orgId 机构ID
     * @return 影响行数
     */
    @Override
    public int add(TpTagVO vo, String creator, String tenantId, String orgId) {
        try {
            // 检查标签名称是否重复
            TpTag existTag = tpTagMapper.selectByTagName(vo.getTagName(), tenantId, orgId);
            if (existTag != null) {
                throw new TopinfoRuntimeException(-1, "标签名称已存在！");
            }
            
            TpTag bean = new TpTag();
            String tagId = SnowflakeIdUtil.nextIdStr();
            
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            bean.setTagId(tagId);
            bean.setTenantId(tenantId);
            bean.setAscnId(orgId);
            
            String now = CommonDateUtil.now();
            bean.setCreator(creator);
            bean.setCreateTime(now);
            bean.setUpdator(creator);
            bean.setUpdateTime(now);
            
            // 设置默认值
            if (bean.getOrderIndex() == null) {
                bean.setOrderIndex(0.0); // 默认排序
            }
            
            int count = tpTagMapper.save(bean);
            return count;
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("新增标签失败！vo:{}, 错误: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增标签失败！");
        }
    }

    /**
     * 更新标签
     *
     * @param vo 标签信息
     * @param updator 修改人
     * @return 影响行数
     */
    @Override
    public int update(TpTagVO vo, String updator) {
        try {
            // 检查标签名称是否重复（排除当前标签）
            TpTag existTag = tpTagMapper.selectByTagName(vo.getTagName(), vo.getTenantId(), vo.getAscnId());
            if (existTag != null && !existTag.getTagId().equals(vo.getTagId())) {
                throw new TopinfoRuntimeException(-1, "标签名称已存在！");
            }
            
            TpTag bean = new TpTag();
            BeanUtil.copyProperties(vo, bean);
            
            String now = CommonDateUtil.now();
            bean.setUpdator(updator);
            bean.setUpdateTime(now);
            
            int count = tpTagMapper.update(bean);
            return count;
        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("更新标签失败！vo:{}, 错误: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "更新标签失败！");
        }
    }

    /**
     * 查看标签详情
     *
     * @param tagId 标签ID
     * @return 标签详情
     */
    @Override
    public TpTagVO view(String tagId) {
        try {
            TpTagVO vo = tpTagMapper.view(tagId);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看标签详情失败！tagId:{}, 错误: {}", tagId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看标签详情失败！");
        }
    }

    /**
     * 删除前查询是否有人员绑定标签
     *
     * @param tagId 标签ID
     * @return 人员标签关系列表
     */
    @Override
    public List<TpPersonTagVO> selectByTagId(String tagId) {
        try {
            List<TpPersonTagVO> personTagVOS = tpPersonTagMapper.selectByTagId(tagId);
            return personTagVOS;
        } catch (Exception e) {
            LOGGER.error("查询标签关联人员失败！tagId:{}, 错误: {}", tagId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询标签关联人员失败！");
        }
    }

    /**
     * 删除标签
     *
     * @param tagId 标签ID
     * @param creator 创建人
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int delete(String tagId, String creator, String operator) {
        try {
            LOGGER.warn("操作人：{}, 删除了：{} 标签.", operator, tagId);
            
            // 1、删除关联关系，tp_person_tag
            tpPersonTagMapper.deleteByTagId(tagId);
            
            // 2、逻辑删除标签信息
            TpTag bean = new TpTag();
            bean.setTagId(tagId);
            bean.setUpdator(operator);
            bean.setUpdateTime(CommonDateUtil.now());
            
            int count = tpTagMapper.delete(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("删除标签失败！tagId:{}, 错误: {}", tagId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除标签失败！");
        }
    }



    /**
     * 为人员分配标签
     *
     * @param personId 人员ID
     * @param tagIds 标签ID列表
     * @param operator 操作人
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int assignPersonTags(String personId, List<String> tagIds, String operator) {
        try {
            // 先删除该人员的所有标签
            tpPersonTagMapper.deleteByPersonId(personId);
            
            // 批量新增标签关系
            if (tagIds != null && !tagIds.isEmpty()) {
                List<TpPersonTag> personTags = new ArrayList<>();
                String now = CommonDateUtil.now();
                
                for (String tagId : tagIds) {
                    TpPersonTag personTag = new TpPersonTag();
                    personTag.setPersonId(personId);
                    personTag.setTagId(tagId);
                    personTag.setCreator(operator);
                    personTag.setCreateTime(now);
                    personTags.add(personTag);
                }
                
                return tpPersonTagMapper.batchSave(personTags);
            }
            
            return 0;
        } catch (Exception e) {
            LOGGER.error("为人员分配标签失败！personId:{}, tagIds:{}, 错误: {}", personId, tagIds, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "为人员分配标签失败！");
        }
    }

    /**
     * 查询人员的标签列表
     *
     * @param personId 人员ID
     * @return 人员标签列表
     */
    @Override
    public List<TpPersonTagVO> getPersonTags(String personId) {
        try {
            List<TpPersonTagVO> list = tpPersonTagMapper.selectByPersonId(personId);
            return list;
        } catch (Exception e) {
            LOGGER.error("查询人员标签失败！personId:{}, 错误: {}", personId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询人员标签失败！");
        }
    }

    /**
     * 移除人员的指定标签
     *
     * @param personId 人员ID
     * @param tagIds 标签ID列表
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int removePersonTags(String personId, List<String> tagIds) {
        try {
            if (tagIds != null && !tagIds.isEmpty()) {
                return tpPersonTagMapper.batchDeleteByPersonIdAndTagIds(personId, tagIds);
            }
            return 0;
        } catch (Exception e) {
            LOGGER.error("移除人员标签失败！personId:{}, tagIds:{}, 错误: {}", personId, tagIds, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "移除人员标签失败！");
        }
    }
}