package com.jiuxi.admin.core.service;


import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.CJsonQuery;
import com.jiuxi.admin.core.bean.vo.CJsonVO;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: CJsonCesService
 * @Description: ""
 * @Author pand
 * @Date 2021-05-12 13:48:38
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface CJsonService {

    IPage<Map> queryPage(JSONObject query, String jwtaid, String jwtdid, String jwtCityCode);

    String add(CJsonVO vo, String jwtaid, String jwtdid, String jwtpid, String jwtCityCode);

    int update(CJsonVO vo, String jwtaid, String jwtdid, String jwtpid, String jwtCityCode);

    CJsonVO view(String fid, String id);

    int deleteByIds(String fid, List<String> ids);
}

