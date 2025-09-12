package com.jiuxi.monitor.server.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.monitor.server.core.bean.entity.TpMonitorConfig;
import com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO;
import com.jiuxi.monitor.server.core.mapper.TpMonitorConfigMapper;
import com.jiuxi.monitor.server.core.service.MonitorCacheService;
import com.jiuxi.monitor.server.core.service.TpMonitorConfigService;
import com.jiuxi.monitor.server.core.util.MonitorMailUtil;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: TpMonitorConfigServiceImpl
 * @Description: 监控配置
 * @Author yangzr
 * @Date 2024-11-18 16:30:42
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service
public class TpMonitorConfigServiceImpl implements TpMonitorConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TpMonitorConfigServiceImpl.class);

    @Autowired
    private TpMonitorConfigMapper tpMonitorConfigMapper;

    @Autowired
    private MonitorCacheService monitorCacheService;

    /**
     * 保存
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    private String add(TpMonitorConfigVO vo, String jwtpid) {

        // 默认主键，防止新增多条
        String id = "1111111111111111111";
        String now = CommonDateUtil.now();

        TpMonitorConfig bean = new TpMonitorConfig();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        // 设置主键id
        bean.setConfigId(id);
        // 创建人
        bean.setCreator(jwtpid);
        // 创建时间
        bean.setCreateTime(now);
        // 修改人
        bean.setUpdator(jwtpid);
        // 修改时间
        bean.setUpdateTime(now);
        // 是否有效
        bean.setActived(TpConstant.YES);

        try {
            tpMonitorConfigMapper.add(bean);
            return id;
        } catch (Exception e) {
            LOGGER.error("新增失败！vo: {}, e: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增失败！");
        }
    }

    /**
     * 修改
     *
     * @Author yangzr
     * @Date 2024-11-18 16:30:42
     */
    @Override
    public int update(TpMonitorConfigVO vo, String jwtpid) {

        // 校验数据
        this.validate(vo);

        // 查询一条数据
        TpMonitorConfigVO one = this.getOne();
        if (null == one) {
            // 不存在则新增
            this.add(vo, jwtpid);
            monitorCacheService.clearAlarmConfig();
            return 1;
        }
        // 添加修改的信息
        one.setCpuThreshold(vo.getCpuThreshold());
        one.setMemoryThreshold(vo.getMemoryThreshold());
        one.setDiskThreshold(vo.getDiskThreshold());
        one.setSendMail(vo.getSendMail());
        one.setPrincipal(vo.getPrincipal());
        one.setMobile(vo.getMobile());
        one.setEmail(vo.getEmail());

        TpMonitorConfig bean = new TpMonitorConfig();
        // 转换成数据库对象
        BeanUtil.copyProperties(one, bean);
        bean.setUpdator(jwtpid);
        bean.setUpdateTime(CommonDateUtil.now());

        try {
            int count = tpMonitorConfigMapper.update(bean);
            monitorCacheService.clearAlarmConfig();
            return count;
        } catch (Exception e) {
            LOGGER.error("修改失败！vo: {}, e: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

    /**
     * 校验配置信息
     *
     * @param vo
     * @return void
     * @author 杨占锐
     * @date 2024/11/27 14:54
     */
    private void validate(TpMonitorConfigVO vo) {

        Integer sendMail = vo.getSendMail();
        if (sendMail == 0) {
            return;
        }

        String email = vo.getEmail();
        Validate.notBlank(email, "邮箱为空");
        email = processStr(email);
        vo.setEmail(email);

        String principal = vo.getPrincipal();
        Validate.notBlank(principal, "负责人为空");
        principal = processStr(principal);
        vo.setPrincipal(principal);

        String mobile = vo.getMobile();
        Validate.notBlank(mobile, "手机号为空");
        mobile = processStr(mobile);
        vo.setMobile(mobile);

        String[] emails = email.split(",");
        int emailLength = emails.length;
        int principalLength = principal.split(",").length;
        if (emailLength != principalLength) {
            throw new TopinfoRuntimeException(-1, "负责人个数与邮箱个数不符");
        }

        String[] mobiles = mobile.split(",");
        int mobileLength = mobiles.length;
        if (emailLength != mobileLength) {
            throw new TopinfoRuntimeException(-1, "手机号个数与邮箱个数不符");
        }

        for (String eml: emails) {
            if (!MonitorMailUtil.validEmail(eml)) {
                throw new TopinfoRuntimeException(-1, "邮箱格式错误");
            }
        }

        for (String mbl: mobiles) {
            if (mbl.length() != 11) {
                throw new TopinfoRuntimeException(-1, "手机号位数不对");
            }
        }



    }


    /**
     * 处理字符串
     *
     * <pre>
     *     1. 去掉前后空格
     *     2. 中文逗号修改为英文逗号
     * </pre>
     * @param str
     * @return java.lang.String
     * @author 杨占锐
     * @date 2024/11/27 14:51
     */
    private String processStr(String str) {
        str = str.trim();
        str = str.replaceAll("，", ",");
        return str;
    }

    /**
     * 查询唯一一条的配置信息
     *
     * @return com.jiuxi.monitor.server.core.bean.vo.TpMonitorConfigVO
     * @author 杨占锐
     * @date 2024/11/19 10:29
     */
    @Override
    public TpMonitorConfigVO getOne() {
        return tpMonitorConfigMapper.getOne();
    }
}
