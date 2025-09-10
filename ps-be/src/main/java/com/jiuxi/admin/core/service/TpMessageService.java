package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpMessageQuery;
import com.jiuxi.admin.core.bean.vo.TpMessageReadVO;
import com.jiuxi.admin.core.bean.vo.TpMessageVO;

import java.util.List;

/**
 * @ClassName: TpMessageService
 * @Description:
 * @Author yangp
 * @Date 2021-03-24 16:04:29
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpMessageService {

    IPage<TpMessageVO> queryPage(TpMessageQuery query, String jwtpid);

    List<TpMessageVO> remindlist(String jwtaid, String jwtdid, String jwtpid);

    int update(TpMessageVO vo, String jwtpid);

    TpMessageReadVO remind(String id, String jwtpid);

    int deleteByIds(List<String> ids);

    boolean send(TpMessageVO tpMessage);
}

