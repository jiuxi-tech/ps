package com.jiuxi.security.core.service;

import com.jiuxi.security.core.entity.vo.PersonVO;

/**
 * @Description: TODO
 * @ClassName: PersonService
 * @Author: pdd
 * @Date: 2021-02-03 21:34
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */

public interface PersonService {

    PersonVO getUserInfo(String deptId, String personId);
}
