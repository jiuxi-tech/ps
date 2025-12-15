package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpAgentQuery;
import com.jiuxi.admin.core.bean.vo.TpAgentDealVO;
import com.jiuxi.admin.core.bean.vo.TpAgentVO;

import java.util.List;

/**
 * @ClassName: TpAgentService
 * @Description:
 * @Author yangp
 * @Date 2021-03-24 16:04:29
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpAgentService {

    IPage<TpAgentVO> queryPage(TpAgentQuery query);

    List<TpAgentVO> remindlist(String jwtaid, String jwtdid, String jwtpid);

    String send(TpAgentVO vo, String jwtpid);

    int update(TpAgentVO vo, String jwtpid);

    TpAgentDealVO view(String id, String jwtpid);

    int deleteByIds(List<String> ids);
}

