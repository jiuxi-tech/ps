package com.jiuxi.admin.core.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpCustomModuleQuery;
import com.jiuxi.admin.core.bean.vo.TpCustomModuleVO;

/**
 * @ClassName: TpCustomModuleService
 * @Description: 自定义模块信息表 存储模块的信息，按钮信息、路由信息
 * @Author pand
 * @Date 2021-05-11 11:22:40
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface TpCustomModuleService {

    IPage<TpCustomModuleVO> queryPage(TpCustomModuleQuery query);

    String add(TpCustomModuleVO vo, String jwtpid);

    TpCustomModuleVO view(String id);

    int update(TpCustomModuleVO vo, String jwtpid);

    void deleteByIds(String id);
}

