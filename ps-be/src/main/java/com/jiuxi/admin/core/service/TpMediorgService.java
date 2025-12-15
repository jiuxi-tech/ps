package com.jiuxi.admin.core.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpMediorgQuery;
import com.jiuxi.admin.core.bean.vo.TpMediorgVO;

import java.util.List;

/**
 * @ClassName: TpMediorgService
 * @Description: 中介表
 * @Author pand
 * @Date 2021-05-25 17:32:41
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpMediorgService {

    IPage<TpMediorgVO> queryPage(TpMediorgQuery query);

    String selectByEntUnifiedCode(String mediorgUnifiedCode);

    String add(TpMediorgVO vo, String jwtpid);

    String addInfo(TpMediorgVO vo, String jwtpid);

    int update(TpMediorgVO vo, String jwtpid);

    TpMediorgVO view(String id);

    int deleteById(String id);
}

