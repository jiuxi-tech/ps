package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpAgent;
import com.jiuxi.admin.core.bean.query.TpAgentQuery;
import com.jiuxi.admin.core.bean.vo.TpAgentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpAgentMapper
 * @Description:
 * @Author yangp
 * @Date 2021-03-24 16:04:29
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpAgentMapper extends BaseMapper<TpAgent> {

    IPage<TpAgentVO> getPage(Page<TpAgentVO> page, @Param("query") TpAgentQuery query);

    List<TpAgentVO> remindlist(@Param("ids") List<String> ids);

    int add(TpAgent bean);

    int update(TpAgent bean);

    TpAgentVO view(String id);

    int delete(String id);

}
