package com.jiuxi.security.core.service;

import com.jiuxi.security.core.entity.vo.PersonVO;

/**
 * @Description: TODO
 * @ClassName: PersonService
 * @Author: pand
 * @Date: 2021-02-03 21:34
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */

public interface PersonService {

    PersonVO getUserInfo(String deptId, String personId);
}
