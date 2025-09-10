package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpTrace;
import com.jiuxi.admin.core.bean.vo.TpTraceVO;
import com.jiuxi.admin.core.bean.query.TpTraceQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: TpTraceMapper
 * @Description: 修改痕迹表
 * @Author yangp
 * @Date 2021-02-26 15:48:55
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpTraceMapper extends BaseMapper<TpTrace> {

    IPage<TpTraceVO> getPage(Page<TpTraceVO> page, @Param("query") TpTraceQuery query);

    int add(TpTrace bean);

    int update(TpTrace bean);

    TpTraceVO view(String id);

    int delete(String id);

}
