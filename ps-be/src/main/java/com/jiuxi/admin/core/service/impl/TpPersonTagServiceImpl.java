package com.jiuxi.admin.core.service.impl;

import com.jiuxi.admin.core.bean.entity.TpPersonTag;
import com.jiuxi.admin.core.bean.vo.TpPersonTagVO;
import com.jiuxi.admin.core.mapper.TpPersonTagMapper;
import com.jiuxi.admin.core.service.TpPersonTagService;
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

/**
 * @ClassName: TpPersonTagServiceImpl
 * @Description: 人员标签服务实现
 * @Author system
 * @Date 2025-01-28
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpPersonTagService")
public class TpPersonTagServiceImpl implements TpPersonTagService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpPersonTagServiceImpl.class);

    @Autowired
    private TpPersonTagMapper tpPersonTagMapper;

    /**
     * 根据人员ID和部门ID查询标签
     *
     * @param personId 人员ID
     * @param deptId   部门ID
     * @return List<TpPersonTagVO>
     */
    @Override
    public List<TpPersonTagVO> getPersonTags(String personId, String deptId) {
        try {
            return tpPersonTagMapper.selectByPersonId(personId);
        } catch (Exception e) {
            LOGGER.error("查询人员标签失败！personId:{}, deptId:{}, 错误: {}", personId, deptId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询人员标签失败！");
        }
    }

    /**
     * 根据标签ID查询人员
     *
     * @param tagId 标签ID
     * @return List<TpPersonTagVO>
     */
    @Override
    public List<TpPersonTagVO> getTagPersons(String tagId) {
        try {
            return tpPersonTagMapper.selectByTagId(tagId);
        } catch (Exception e) {
            LOGGER.error("查询标签人员失败！tagId:{}, 错误: {}", tagId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询标签人员失败！");
        }
    }

    /**
     * 保存人员标签关系
     *
     * @param personId 人员ID
     * @param tagIds   标签ID列表
     * @param deptId   部门ID
     * @param creator  创建人
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int savePersonTags(String personId, List<String> tagIds, String deptId, String creator) {
        try {
            if (tagIds == null || tagIds.isEmpty()) {
                return 0;
            }

            // 先删除现有的标签关系
            List<String> deptIds = new ArrayList<>();
            deptIds.add(deptId);
            tpPersonTagMapper.deleteByPersonId(personId);

            // 批量插入新的标签关系
            List<TpPersonTag> personTags = new ArrayList<>();
            String currentTime = CommonDateUtil.now();
            
            for (String tagId : tagIds) {
                TpPersonTag personTag = new TpPersonTag();
                personTag.setPersonId(personId);
                personTag.setTagId(tagId);
                personTag.setDeptId(deptId);
                personTag.setCreator(creator);
                personTag.setCreateTime(currentTime);
                personTags.add(personTag);
            }

            return tpPersonTagMapper.batchSave(personTags);
        } catch (Exception e) {
            LOGGER.error("保存人员标签失败！personId:{}, tagIds:{}, deptId:{}, 错误: {}", personId, tagIds, deptId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "保存人员标签失败！");
        }
    }

    /**
     * 删除人员标签关系
     *
     * @param personId 人员ID
     * @param tagId    标签ID
     * @param deptId   部门ID
     * @return int
     */
    @Override
    public int deletePersonTag(String personId, String tagId, String deptId) {
        try {
            return tpPersonTagMapper.delete(personId, tagId);
        } catch (Exception e) {
            LOGGER.error("删除人员标签失败！personId:{}, tagId:{}, deptId:{}, 错误: {}", personId, tagId, deptId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除人员标签失败！");
        }
    }

    /**
     * 根据标签ID删除所有关联关系
     *
     * @param tagId 标签ID
     * @return int
     */
    @Override
    public int deleteByTagId(String tagId) {
        try {
            return tpPersonTagMapper.deleteByTagId(tagId);
        } catch (Exception e) {
            LOGGER.error("根据标签ID删除关联关系失败！tagId:{}, 错误: {}", tagId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "根据标签ID删除关联关系失败！");
        }
    }

    /**
     * 根据人员ID和部门ID列表删除关联关系
     *
     * @param personId 人员ID
     * @param deptIds  部门ID列表
     * @return int
     */
    @Override
    public int deleteByPersonIdAndDeptIds(String personId, List<String> deptIds) {
        try {
            return tpPersonTagMapper.deleteByPersonId(personId);
        } catch (Exception e) {
            LOGGER.error("根据人员ID和部门ID列表删除关联关系失败！personId:{}, deptIds:{}, 错误: {}", personId, deptIds, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "根据人员ID和部门ID列表删除关联关系失败！");
        }
    }

}