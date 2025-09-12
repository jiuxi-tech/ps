package com.jiuxi.security.core.service.impl;

import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.security.core.entity.vo.PersonVO;
import com.jiuxi.security.core.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @Description: 用户信息接口
 * @ClassName: PersonServiceImpl
 * @Author: pand
 * @Date: 2021-02-03 21:35
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class PersonServiceImpl implements PersonService {

    private static final String loginSql = "select tpb.PERSON_ID, tpb.PERSON_NAME, tpb.PROFILE_PHOTO, tpb.PERSON_NO, " +
            "tpb.SEX, CASE tpb.SEX when 0 then '保密' when 1 then '男' else '女' END as SEX_NAME, tpb.IDCARD, tpb.NATIVE_PLACE, " +
            "tpb.RESUME, tpb.BIRTHDAY, tpb.PHONE, tpb.EMAIL, tpb.OFFICE, tdp.ASCN_ID, tdp1.DEPT_FULL_NAME as ascnName, tdp.DEPT_ID as deptId, " +
            "tdp.DEPT_FULL_NAME as deptFullName, ta.USERNAME as username, tdp1.CATEGORY as category " +
            "from tp_person_basicinfo tpb left join tp_person_dept tpd on tpb.PERSON_ID = tpd.PERSON_ID " +
            "left join tp_dept_basicinfo tdp on tpd.dept_id = tdp.dept_id " +
            "left join tp_dept_basicinfo tdp1 on tdp.ASCN_ID = tdp1.DEPT_ID " +
            "left join tp_account ta on tpb.PERSON_ID = ta.PERSON_ID and ta.ACTIVED = 1 " +
            "where tpb.ACTIVED = 1 and tdp.ACTIVED = 1 and tpb.PERSON_ID = ? and tpd.DEPT_ID = ?";

    @Autowired(required = false)
    protected JdbcTemplate jdbcTemplate;

    @Override
    public PersonVO getUserInfo(String deptId, String personId) {
        try {
            PersonVO vo = jdbcTemplate.queryForObject(loginSql, new Object[]{personId, deptId}, new BeanPropertyRowMapper<>(PersonVO.class));
            return vo;
        } catch (Exception e) {
            throw new TopinfoRuntimeException(-500, "用户基本信息查询失败！");
        }

    }
}
