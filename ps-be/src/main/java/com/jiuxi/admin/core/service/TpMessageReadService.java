package com.jiuxi.admin.core.service;


import com.jiuxi.admin.core.bean.query.TpMessageReadQuery;
import com.jiuxi.admin.core.bean.entity.TpMessageRead;
import com.jiuxi.admin.core.bean.vo.TpMessageReadVO;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @ClassName: TpMessageReadService
 * @Description: 消息、代办 已读表
 * @Author pand
 * @Date 2021-05-28 15:04:39
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpMessageReadService {

    IPage<TpMessageReadVO>  queryPage(TpMessageReadQuery query);

    String add(TpMessageReadVO vo, String jwtpid);

    int update(TpMessageReadVO vo, String jwtpid);

    TpMessageReadVO view(String personId, String msgId);

    int deleteByIds(List<String> ids);
}

