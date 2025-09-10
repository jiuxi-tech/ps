package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.CJson;
import com.jiuxi.admin.core.bean.query.CJsonQuery;
import com.jiuxi.admin.core.bean.vo.CJsonVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @ClassName: CJsonCesMapper
 * @Description: ""
 * @Author pand
 * @Date 2021-05-12 13:48:38
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface CJsonMapper {

    IPage<Map> getPage(Page<Map> page, @Param("query") Map query);

    int add(CJson bean);

    int update(CJson bean);

    CJsonVO view(@Param("mcode") String mcode, @Param("id") String id);

    int delete(@Param("mcode") String mcode, @Param("id") String id);

}
