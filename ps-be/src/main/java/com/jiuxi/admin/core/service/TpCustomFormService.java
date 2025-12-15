package com.jiuxi.admin.core.service;


import com.jiuxi.admin.core.bean.vo.TpCustomFormVO;

import java.util.List;

/**
 * @ClassName: TpCustomFormService
 * @Description: 表单设计表 表单设计表
 * @Author pand
 * @Date 2021-05-11 11:22:40
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpCustomFormService {

    String update(TpCustomFormVO vo, String jwtpid);

    TpCustomFormVO view(String mid, String ftype, String fSource);

    int deleteByIds(List<String> ids);

    TpCustomFormVO viewByMcode(String mcode, String ftype, String fSource);
}

