package com.jiuxi.security.core.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.json.JSONObject;
import com.jiuxi.captcha.core.service.CaptchaService;
import com.jiuxi.common.util.JwtTokenUtils;
import com.jiuxi.common.util.SmUtils;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import com.jiuxi.security.core.entity.vo.AccountVO;
import com.jiuxi.security.core.entity.vo.DeptVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 登陆抽象类
 * @ClassName: AbstractLoginService
 * @Author: pand
 * @Date: 2020-08-27 18:25
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public abstract class AbstractLoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLoginService.class);

    @Autowired
    private SecurityConfigurationProperties properties;

    @Autowired
    private CaptchaService captchaService;

    @Autowired(required = false)
    protected JdbcTemplate jdbcTemplate;

    /**
     * 查询单位id sql
     */
    private static final String ASCNID_SQL = "SELECT ASCN_ID, CITY_CODE from tp_dept_basicinfo where dept_id = ?";

    /**
     * 根据人员id、部门id查询角色列表
     */
    private static final String ROLE_SQL = "SELECT role_id FROM tp_person_role WHERE person_id = ? and dept_id = ?";

    /**
     * 查询部门信息
     */
    private static final String DEPTBASICINFO_SQL = "SELECT td.dept_id, td.dept_full_name, td.dept_simple_name, td.CITY_CODE, td.ASCN_ID from tp_person_dept tp left join tp_dept_basicinfo td on tp.DEPT_ID = td.DEPT_ID where td.ENABLED = 1 and td.ACTIVED = 1 and PERSON_ID = ?";


    /**
     * @return com.jiuxi.security.core.entity.vo.PersonVO
     * @description: 登录
     * @author 杨攀
     * @date 2020/7/21 11:16
     */
    public abstract AccountVO login(AccountVO vo);

    /**
     * 图片验证码登陆
     *
     * @param vo:
     * @return com.jiuxi.security.core.entity.vo.AccountVO
     * @author pand
     * @date 2021-04-02 10:24
     */
    public AccountVO captchaLogin(AccountVO vo) {

        // 校验验证码是否正确
        String ticket = vo.getTicket();

        boolean result = captchaService.checkTicket(ticket);
        if (result) {
            String userName = vo.getUserName();
            String passWord = vo.getUserpwd();
            if (properties.isPasswordEncryption()) {
                try {
                    // 先将账号密码解密，username,passWord是经过sm2加密的密文传输
                    // 前端传入的需要在 密文前 加 "04"
                    userName = SmUtils.decryptStrSM2("04" + userName, KeyType.PrivateKey);
                    passWord = SmUtils.decryptStrSM2("04" + passWord, KeyType.PrivateKey);
                    vo.setUserName(userName);
                    vo.setUserpwd(passWord);
                } catch (Exception e) {
                    throw new TopinfoRuntimeException(-1, "登录失败，用户名或密码错误");
                }
            }
            return this.login(vo);
        } else {
            throw new TopinfoRuntimeException(-500, "验证失败，请重试！");
        }
    }

    /**
     * 查询登陆人员所在部门，权限，如果有多部门，先返回所在部门，然后重新选择部门登陆
     *
     * @param personId: 登陆人员id
     * @return cn.hutool.json.JSONObject
     * @author pand
     * @date 2020-08-27 15:25
     */
    protected JSONObject personDeptInfo(String personId) {
        // 查询用户所在部门
        List<DeptVO> dept_list = jdbcTemplate.query(DEPTBASICINFO_SQL, new Object[]{personId}, new BeanPropertyRowMapper<>(DeptVO.class));

        JSONObject jsonObject = new JSONObject();

        // 没有兼职部门
        if (dept_list != null && dept_list.size() == 1) {

            DeptVO item = dept_list.get(0);

            String deptId = item.getDeptId();
            String cityCode = item.getCityCode();
            String ascnId = item.getAscnId();
            jsonObject.set("depts", deptId);
            jsonObject.set("cityCode", Optional.ofNullable(cityCode).orElse(""));
            jsonObject.set("ascnId", ascnId);

            String roles = queryRoles(personId, deptId);
            if (StrUtil.isBlank(roles) && !StrUtil.equals("1111111111111111111", personId)) {
                LOGGER.error("{} 未分配角色", personId);
                throw new TopinfoRuntimeException(-1, "该用户未分配角色！");
            }
            jsonObject.put("roles", roles);

        } else if (dept_list != null && dept_list.size() > 1) {
            StringBuffer depts = new StringBuffer();
            dept_list.forEach(deptVO -> {
                depts.append(deptVO.getDeptId()).append(",");
            });

            jsonObject.set("depts", depts.toString());
            jsonObject.set("cityCode", "");
            jsonObject.set("voList", dept_list);
        } else {
            LOGGER.error("登录失败，用户需要挂在单位或部门下，当前登录用户id:{}", personId);
            throw new TopinfoRuntimeException(-1, "登录失败，未查询到用户所在部门");
        }
        return jsonObject;
    }

    /**
     * 根据人员id和部门id查询角色
     *
     * @param personid:
     * @param dept_id:
     * @return java.lang.String
     * @author pand
     * @date 2020-08-27 15:27
     */
    public String queryRoles(String personid, String dept_id) {
        // 查询用户的角色
        List role_list = jdbcTemplate.queryForList(ROLE_SQL, new Object[]{personid, dept_id}, Long.class);
        // 逗号分隔
        String roles = CollUtil.join(role_list, ",");
        return roles;
    }

    /**
     * 组装登陆用户信息，包括所在部门，拥有角色。
     *
     * @param vo: 用户信息
     * @return com.jiuxi.security.core.entity.vo.PersonVO
     * @author pand
     * @date 2020-08-28 15:36
     */
    public AccountVO initPersonVo(AccountVO vo) {
        JSONObject jsonObject = this.personDeptInfo(vo.getPersonId());
        String depts = jsonObject.getStr("depts");
        String cityCode = jsonObject.getStr("cityCode");
        String roles = jsonObject.getStr("roles");
        String ascnId = jsonObject.getStr("ascnId");
        vo.setDeptIds(depts);
        vo.setCityCode(cityCode);
        vo.setRoleIds(roles);
        vo.setAscnId(ascnId);
        Object voList = jsonObject.get("voList");
        if (null != voList) {
            vo.setDeptVOList((List<DeptVO>) voList);
        }
        return vo;
    }


    public DeptVO selectAscnIdByDeptId(String deptId) {
        List<DeptVO> list = jdbcTemplate.query(ASCNID_SQL, new Object[]{deptId}, new BeanPropertyRowMapper<>(DeptVO.class));
        if (null == list || list.size() != 1) {
            throw new TopinfoRuntimeException(-1, "查询不到单位信息！");
        }
        return list.get(0);
    }

}
