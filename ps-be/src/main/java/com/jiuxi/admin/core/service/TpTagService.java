package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpTagQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonTagVO;
import com.jiuxi.admin.core.bean.vo.TpTagVO;

import java.util.List;

/**
 * @ClassName: TpTagService
 * @Description: 标签表
 * @Author system
 * @Date 2024-01-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpTagService {

    /**
     * 分页查询标签列表
     *
     * @param query 查询条件
     * @param tenantId 租户ID
     * @param orgId 机构ID
     * @return 分页结果
     */
    IPage<TpTagVO> queryPage(TpTagQuery query, String tenantId, String orgId);

    /**
     * 查询标签列表
     *
     * @param query 查询条件
     * @param tenantId 租户ID
     * @param orgId 机构ID
     * @return 标签列表
     */
    List<TpTagVO> getList(TpTagQuery query, String tenantId, String orgId);

    /**
     * 新增标签
     *
     * @param vo 标签信息
     * @param creator 创建人
     * @param tenantId 租户ID
     * @param orgId 机构ID
     * @return 影响行数
     */
    int add(TpTagVO vo, String creator, String tenantId, String orgId);

    /**
     * 更新标签
     *
     * @param vo 标签信息
     * @param updator 修改人
     * @return 影响行数
     */
    int update(TpTagVO vo, String updator);

    /**
     * 查看标签详情
     *
     * @param tagId 标签ID
     * @return 标签详情
     */
    TpTagVO view(String tagId);

    /**
     * 删除前查询是否有人员绑定标签
     *
     * @param tagId 标签ID
     * @return 人员标签关系列表
     */
    List<TpPersonTagVO> selectByTagId(String tagId);

    /**
     * 删除标签
     *
     * @param tagId 标签ID
     * @param creator 创建人
     * @param operator 操作人
     * @return 影响行数
     */
    int delete(String tagId, String creator, String operator);



    /**
     * 为人员分配标签
     *
     * @param personId 人员ID
     * @param tagIds 标签ID列表
     * @param operator 操作人
     * @return 影响行数
     */
    int assignPersonTags(String personId, List<String> tagIds, String operator);

    /**
     * 查询人员的标签列表
     *
     * @param personId 人员ID
     * @return 人员标签列表
     */
    List<TpPersonTagVO> getPersonTags(String personId);

    /**
     * 移除人员的指定标签
     *
     * @param personId 人员ID
     * @param tagIds 标签ID列表
     * @return 影响行数
     */
    int removePersonTags(String personId, List<String> tagIds);
}