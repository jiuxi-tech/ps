package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.vo.TpPersonTagVO;

import java.util.List;

/**
 * @ClassName: TpPersonTagService
 * @Description: 人员标签服务
 * @Author system
 * @Date 2025-01-28
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpPersonTagService {

    /**
     * 根据人员ID和部门ID查询标签
     *
     * @param personId 人员ID
     * @param deptId   部门ID
     * @return List<TpPersonTagVO>
     */
    List<TpPersonTagVO> getPersonTags(String personId, String deptId);

    /**
     * 根据标签ID查询人员
     *
     * @param tagId 标签ID
     * @return List<TpPersonTagVO>
     */
    List<TpPersonTagVO> getTagPersons(String tagId);

    /**
     * 保存人员标签关系
     *
     * @param personId 人员ID
     * @param tagIds   标签ID列表
     * @param deptId   部门ID
     * @param creator  创建人
     * @return int
     */
    int savePersonTags(String personId, List<String> tagIds, String deptId, String creator);

    /**
     * 删除人员标签关系
     *
     * @param personId 人员ID
     * @param tagId    标签ID
     * @param deptId   部门ID
     * @return int
     */
    int deletePersonTag(String personId, String tagId, String deptId);

    /**
     * 根据标签ID删除所有关联关系
     *
     * @param tagId 标签ID
     * @return int
     */
    int deleteByTagId(String tagId);

    /**
     * 根据人员ID和部门ID列表删除关联关系
     *
     * @param personId 人员ID
     * @param deptIds  部门ID列表
     * @return int
     */
    int deleteByPersonIdAndDeptIds(String personId, List<String> deptIds);

}