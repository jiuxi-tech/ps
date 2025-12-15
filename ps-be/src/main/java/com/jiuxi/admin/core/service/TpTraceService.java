package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiuxi.admin.core.bean.query.TpTraceQuery;
import com.jiuxi.admin.core.bean.entity.TpTrace;
import com.jiuxi.admin.core.bean.vo.TpTraceVO;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;

/**
 * @ClassName: TpTraceService
 * @Description: 修改痕迹表
 * @Author yangp
 * @Date 2021-02-26 15:48:55
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpTraceService extends IService<TpTrace> {

    IPage<TpTraceVO>  queryPage(TpTraceQuery query);

    String add(TpTraceVO vo, String jwtpid);

    int update(TpTraceVO vo, String jwtpid);

    TpTraceVO view(String id);

    int deleteByIds(ArrayList<String> ids);
}

