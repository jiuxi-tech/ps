package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpAgentDeal;
import com.jiuxi.admin.core.bean.vo.TpAgentDealVO;
import com.jiuxi.admin.core.bean.query.TpAgentDealQuery;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: TpAgentDealMapper
 * @Description: 代办处理表
 * @Author pand
 * @Date 2021-06-03 14:28:23
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpAgentDealMapper {

    IPage<TpAgentDealVO> getPage(Page<TpAgentDealVO> page, @Param("query") TpAgentDealQuery query);

    int add(TpAgentDeal bean);

    int update(TpAgentDeal bean);

    TpAgentDealVO view(@Param("personId") String personId, @Param("agId") String agId);

    int delete(String id);

}
