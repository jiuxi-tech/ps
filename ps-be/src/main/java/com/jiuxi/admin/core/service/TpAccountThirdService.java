package com.jiuxi.admin.core.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpAccountThirdQuery;
import com.jiuxi.admin.core.bean.vo.TpAccountThirdVO;

import java.util.List;

/**
 * @ClassName: TpAccountThridService
 * @Description: 合作方管理表
 * @Author pand
 * @Date 2022-04-20 15:02:39
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpAccountThirdService {

    IPage<TpAccountThirdVO> queryPage(TpAccountThirdQuery query);

    String add(TpAccountThirdVO vo, String jwtpid);

    int reset(String accountId, String jwtpid);

    TpAccountThirdVO view(String id);

    int deleteByIds(List<String> ids, String jwtpid);

}

