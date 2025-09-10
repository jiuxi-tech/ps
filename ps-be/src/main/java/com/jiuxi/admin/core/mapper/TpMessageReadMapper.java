package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpMessageRead;
import com.jiuxi.admin.core.bean.vo.TpMessageReadVO;
import com.jiuxi.admin.core.bean.query.TpMessageReadQuery;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: TpMessageReadMapper
 * @Description: 消息、代办 已读表
 * @Author pand
 * @Date 2021-05-28 15:04:39
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpMessageReadMapper {

    IPage<TpMessageReadVO> getPage(Page<TpMessageReadVO> page, @Param("query") TpMessageReadQuery query);

    int add(TpMessageRead bean);

    int update(TpMessageRead bean);

    TpMessageReadVO view(@Param("personId") String personId, @Param("msgId") String msgId);

    int delete(String id);

}
