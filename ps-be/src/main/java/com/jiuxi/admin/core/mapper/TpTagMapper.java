package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpTag;
import com.jiuxi.admin.core.bean.query.TpTagQuery;
import com.jiuxi.admin.core.bean.vo.TpTagVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpTagMapper
 * @Description: 标签表
 * @Author system
 * @Date 2024-01-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpTagMapper {

    /**
     * 分页查询标签列表
     *
     * @param page 分页对象
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpTagVO> getPage(Page<TpTagVO> page, @Param("query") TpTagQuery query);

    /**
     * 查询标签列表
     *
     * @param query 查询条件
     * @return 标签列表
     */
    List<TpTagVO> getList(@Param("query") TpTagQuery query);

    /**
     * 新增标签
     *
     * @param bean 标签实体
     * @return 影响行数
     */
    int save(TpTag bean);

    /**
     * 更新标签
     *
     * @param bean 标签实体
     * @return 影响行数
     */
    int update(TpTag bean);

    /**
     * 查看标签详情
     *
     * @param tagId 标签ID
     * @return 标签详情
     */
    TpTagVO view(String tagId);

    /**
     * 删除标签
     *
     * @param bean 标签实体
     * @return 影响行数
     */
    int delete(TpTag bean);

    /**
     * 根据标签名称查询标签
     *
     * @param tagName 标签名称
     * @param tenantId 租户ID
     * @param orgId 机构ID
     * @return 标签信息
     */
    TpTag selectByTagName(@Param("tagName") String tagName, @Param("tenantId") String tenantId, @Param("orgId") String orgId);


}