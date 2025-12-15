package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpRole;
import com.jiuxi.admin.core.bean.query.TpRoleAuthQuery;
import com.jiuxi.admin.core.bean.query.TpRoleQuery;
import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * @ClassName: TpRoleMapper
 * @Description: 角色表
 * @Author pand
 * @Date 2020-11-18 11:05:17
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Mapper
public interface TpRoleMapper {

    /**
     * 查询当前登录人拥有的角色
     *
     * @param query
     * @return
     */
    LinkedHashSet<TpRoleVO> roleAuthList(@Param("query") TpRoleAuthQuery query);

    /**
     * 查询当前登录人创建的角色
     *
     * @param query
     * @return
     */
    LinkedHashSet<TpRoleVO> roleAuthListByCreateRole(@Param("query") TpRoleAuthQuery query);

    IPage<TpRoleVO> getPage(Page<TpRoleVO> page, @Param("query") TpRoleQuery query);

    List<TpRoleVO> getList(@Param("query") TpRoleQuery query);

    int save(TpRole bean);

    int update(TpRole bean);

    TpRoleVO view(String roleId);

    int delete(TpRole bean);
}
