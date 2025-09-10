package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpPersonTag;
import com.jiuxi.admin.core.bean.vo.TpPersonTagVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpPersonTagMapper
 * @Description: 人员标签关系表
 * @Author system
 * @Date 2024-01-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpPersonTagMapper {

    /**
     * 新增人员标签关系
     *
     * @param bean 人员标签关系实体
     * @return 影响行数
     */
    int save(TpPersonTag bean);

    /**
     * 根据人员ID删除标签关系
     *
     * @param personId 人员ID
     * @return 影响行数
     */
    int deleteByPersonId(@Param("personId") String personId);

    /**
     * 根据标签ID删除人员标签关系
     *
     * @param tagId 标签ID
     * @return 影响行数
     */
    int deleteByTagId(@Param("tagId") String tagId);

    /**
     * 删除指定人员的指定标签
     *
     * @param personId 人员ID
     * @param tagId 标签ID
     * @return 影响行数
     */
    int delete(@Param("personId") String personId, @Param("tagId") String tagId);

    /**
     * 根据人员ID查询标签列表
     *
     * @param personId 人员ID
     * @return 人员标签列表
     */
    List<TpPersonTagVO> selectByPersonId(@Param("personId") String personId);

    /**
     * 根据标签ID查询人员列表
     *
     * @param tagId 标签ID
     * @return 标签人员列表
     */
    List<TpPersonTagVO> selectByTagId(@Param("tagId") String tagId);

    /**
     * 查询指定人员是否已有指定标签
     *
     * @param personId 人员ID
     * @param tagId 标签ID
     * @return 关系记录
     */
    TpPersonTag selectByPersonIdAndTagId(@Param("personId") String personId, @Param("tagId") String tagId);

    /**
     * 批量新增人员标签关系
     *
     * @param personTags 人员标签关系列表
     * @return 影响行数
     */
    int batchSave(@Param("personTags") List<TpPersonTag> personTags);

    /**
     * 批量删除人员标签关系
     *
     * @param personId 人员ID
     * @param tagIds 标签ID列表
     * @return 影响行数
     */
    int batchDeleteByPersonIdAndTagIds(@Param("personId") String personId, @Param("tagIds") List<String> tagIds);
}