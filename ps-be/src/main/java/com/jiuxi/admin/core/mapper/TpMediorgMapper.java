package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpMediorg;
import com.jiuxi.admin.core.bean.query.TpMediorgQuery;
import com.jiuxi.admin.core.bean.vo.TpMediorgVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpMediorgMapper
 * @Description: 中介表
 * @Author pand
 * @Date 2021-05-25 17:32:41
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Mapper
public interface TpMediorgMapper {

    IPage<TpMediorgVO> getPage(Page<TpMediorgVO> page, @Param("query") TpMediorgQuery query);

    TpMediorg selectByMediorgUnifiedCode(String mediorgUnifiedCode);

    int add(TpMediorg bean);

    int update(TpMediorg bean);

    TpMediorgVO view(String id);

    List<String> selectPersonIdByAscnId(String ascnId);

    int delete(String id);

}
