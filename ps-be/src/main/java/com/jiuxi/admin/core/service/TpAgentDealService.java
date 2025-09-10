package com.jiuxi.admin.core.service;


import com.jiuxi.admin.core.bean.query.TpAgentDealQuery;
import com.jiuxi.admin.core.bean.entity.TpAgentDeal;
import com.jiuxi.admin.core.bean.vo.TpAgentDealVO;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @ClassName: TpAgentDealService
 * @Description: 代办处理表
 * @Author pand
 * @Date 2021-06-03 14:28:23
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpAgentDealService {

    IPage<TpAgentDealVO>  queryPage(TpAgentDealQuery query);

    String add(TpAgentDealVO vo, String jwtpid);

    int update(TpAgentDealVO vo, String jwtpid);

    TpAgentDealVO view(String id, String jwtpid);

    int deleteByIds(List<String> ids, String jwtpid);
}

