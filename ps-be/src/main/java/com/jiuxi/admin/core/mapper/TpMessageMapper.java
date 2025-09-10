package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpMessage;
import com.jiuxi.admin.core.bean.query.TpMessageQuery;
import com.jiuxi.admin.core.bean.vo.TpMessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpMessageMapper
 * @Description:
 * @Author yangp
 * @Date 2021-03-24 16:04:29
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpMessageMapper extends BaseMapper<TpMessage> {

    IPage<TpMessageVO> getPage(Page<TpMessageVO> page, @Param("query") TpMessageQuery query);

    int add(TpMessage bean);

    List<TpMessageVO> remindlist(@Param("ids") List<String> ids);

    int update(TpMessage bean);

    TpMessageVO view(String id);

    int delete(String id);

}
