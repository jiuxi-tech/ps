package com.jiuxi.module.user.app.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpAccount;
import com.jiuxi.admin.core.bean.entity.TpPersonBasicinfo;
import com.jiuxi.admin.core.bean.entity.TpPersonDept;
import com.jiuxi.admin.core.bean.entity.TpPersonExinfo;
import com.jiuxi.admin.core.bean.entity.TpPersonRole;
import com.jiuxi.admin.core.bean.query.TpPersonBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonDeptVO;
import com.jiuxi.admin.core.bean.vo.TpPersonExinfoVO;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.admin.core.event.TpPersonBasicinfoEvent;
import com.jiuxi.admin.core.listener.service.TpPersonBasicinfoEventService;
import com.jiuxi.module.user.infra.persistence.mapper.UserAccountMapper;
import com.jiuxi.admin.core.mapper.TpDeptBasicinfoMapper;
import com.jiuxi.module.user.infra.persistence.mapper.UserPersonMapper;
import com.jiuxi.admin.core.mapper.TpPersonDeptMapper;
import com.jiuxi.admin.core.mapper.TpPersonBasicinfoMapper;
import com.jiuxi.admin.core.service.PersonAccountService;
import com.jiuxi.admin.core.service.TpAccountService;
import com.jiuxi.admin.core.mapper.TpPersonExinfoMapper;
import com.jiuxi.admin.core.mapper.TpPersonRoleMapper;
import com.jiuxi.module.user.app.service.PersonAccountApplicationService;
import com.jiuxi.module.user.app.service.UserAccountService;
import com.jiuxi.admin.core.service.TpAttachinfoService;
import com.jiuxi.admin.core.service.TpSystemConfigService;
import com.jiuxi.module.user.app.service.UserPersonService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.SessionVO;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.CommonUniqueIndexUtil;
import com.jiuxi.common.util.PhoneEncryptionUtils;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.security.core.holder.SessionHolder;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
// import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName: TpPersonBasicinfoServiceImpl
 * @Description: 人员基本信息表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("userPersonService")
public class UserPersonServiceImpl implements UserPersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPersonServiceImpl.class);

    @Autowired
    private TpPersonBasicinfoMapper tpPersonBasicinfoMapper;

    @Autowired
    private PersonAccountService personAccountService;

    @Autowired
    private TpPersonExinfoMapper tpPersonExinfoMapper;

    @Autowired
    private TpPersonDeptMapper tpPersonDeptMapper;

    @Autowired
    private TpPersonRoleMapper tpPersonRoleMapper;

    @Autowired
    private UserAccountMapper tpAccountMapper;

    @Autowired
    private TpAccountService tpAccountService;

    @Autowired
    private TpDeptBasicinfoMapper tpDeptBasicinfoMapper;

    @Autowired
    private TpAttachinfoService tpAttachinfoService;

    @Autowired
    private UserAccountService userAccountService;

    /**
     * 上下文对象
     */
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private TpPersonBasicinfoEventService tpPersonBasicinfoEventService;

    @Autowired
    private TpSystemConfigService tpSystemConfigService;

    /**
     * 用户信息分页查询
     *
     * @param query:
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO>
     * @author pand
     * @date 2020-12-04 13:38
     */
    @Override
    public IPage<TpPersonBasicinfoVO> queryPage(TpPersonBasicQuery query) {
        try {
            if (StrUtil.isBlank(query.getDeptId())) {
                throw new TopinfoRuntimeException(500, "请选择部门查询数据!");
            }

            // 检查物理删除开关状态，决定是否显示ACTIVED=0的人员
            boolean isPhysicalDeleteEnabled = tpSystemConfigService.isPhysicalDeleteEnabled();
            query.setIncludeInactive(isPhysicalDeleteEnabled);

            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);

            Page<TpPersonBasicinfoVO> page = new Page<>(pageNum, pageSize);
            IPage<TpPersonBasicinfoVO> iPage;
            if (query.getLevelFlag() == 0) {
                // 0：本级
                iPage = tpPersonBasicinfoMapper.getPage(page, query);
            } else {
                // 1：本级及下级
                String deptId = query.getDeptId();
                // 先根据deptId查询出部门信息，设置查询的层级code，根据层级code模糊查询
                TpDeptBasicinfoVO deptBasicinfoVO = tpDeptBasicinfoMapper.view(deptId);
                query.setDeptId("");
                query.setDeptLevelcode(deptBasicinfoVO.getDeptLevelcode());

                iPage = tpPersonBasicinfoMapper.getPage(page, query);
            }

            List<TpPersonBasicinfoVO> list = iPage.getRecords();
            if (!list.isEmpty()){
                list.forEach(vo -> {
                    TpDeptBasicinfoVO tpDeptBasicinfoVO = tpDeptBasicinfoMapper.selectDeptById(vo.getAscnId());
                    vo.setAscnName(tpDeptBasicinfoVO.getDeptFullName());
                    
                    // 解密手机号
                    if (StrUtil.isNotBlank(vo.getPhone())) {
                        String decryptedPhone = PhoneEncryptionUtils.safeDecrypt(vo.getPhone());
                        vo.setPhone(decryptedPhone);
                    }
                });
            }

            return iPage;
        } catch (Exception e) {
            LOGGER.error("用户列表信息失败！query:{}, e: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "用户列表信息失败！" : e.getMessage());
        }
    }

    /**
     * 新增用户基本信息，维护用户部门关系表，默认主部门。
     *
     * @param vo:       部门信息
     * @param pid:      当前登陆人id
     * @param category: 0政府 1企业 2其他
     * @author pand
     * @date 2020-11-23 15:37
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public TpPersonBasicinfoVO add(TpPersonBasicinfoVO vo, String pid, int category) {
        // 校验手机号唯一
        if (StrUtil.isNotBlank(vo.getPhone()) && personAccountService.selectByPhone(vo.getPhone())) {
            LOGGER.error("手机号已被占用phone = {}", vo.getPhone());
            throw new TopinfoRuntimeException(-1, "手机号已被占用");
        }

        try {

            String personId = SnowflakeIdUtil.nextIdStr();
            String createTime = CommonDateUtil.now();

            vo.setPersonId(personId);
            TpPersonBasicinfo bean = new TpPersonBasicinfo();
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            
            // 对手机号进行加密
            if (StrUtil.isNotBlank(vo.getPhone())) {
                String encryptedPhone = PhoneEncryptionUtils.encrypt(vo.getPhone());
                bean.setPhone(encryptedPhone); // 将加密后的手机号存储到phone字段
                LOGGER.debug("手机号加密处理完成，personId: {}, 原始手机号: {}", personId, vo.getPhone());
            }
            
            bean.setCreator(pid);
            bean.setCreateTime(createTime);
            bean.setUpdator(pid);
            bean.setUpdateTime(createTime);

            // 设置人员类别 政府人员or企业人员or其他
            bean.setCategory(category);
            TpPersonDept personDeptBean = new TpPersonDept();
            personDeptBean.setDeptId(vo.getDeptId());
            personDeptBean.setPersonId(personId);
            personDeptBean.setDefaultDept(1);

            // 附件绑定
            tpAttachinfoService.band(vo.getFile(), personId, createTime);

            // 新增人员
            LOGGER.info("准备插入用户基本信息，personId：{}, personName：{}, phone：{}", personId, vo.getPersonName(), vo.getPhone());
            int count = tpPersonBasicinfoMapper.save(bean);
            LOGGER.info("用户基本信息插入结果：{}, 影响行数：{}", count > 0 ? "成功" : "失败", count);
            
            // 维护部门，人员关系
            LOGGER.info("准备插入用户部门关系，personId：{}, deptId：{}", personId, vo.getDeptId());
            int deptCount = tpPersonDeptMapper.save(personDeptBean);
            LOGGER.info("用户部门关系插入结果：{}, 影响行数：{}", deptCount > 0 ? "成功" : "失败", deptCount);

            // 保存职称、职务职级、参加工作时间到扩展信息表（如果有）
            if (StrUtil.isNotBlank(vo.getZhicheng()) || StrUtil.isNotBlank(vo.getZwzj()) || StrUtil.isNotBlank(vo.getPartWorkDate())) {
                LOGGER.info("准备保存扩展信息，personId：{}, zhicheng：{}, zwzj：{}, partWorkDate：{}", 
                            personId, vo.getZhicheng(), vo.getZwzj(), vo.getPartWorkDate());
                TpPersonExinfo exinfo = new TpPersonExinfo();
                exinfo.setPersonId(personId);
                exinfo.setZhicheng(vo.getZhicheng());
                exinfo.setZwzj(vo.getZwzj());
                exinfo.setPartWorkDate(vo.getPartWorkDate());
                exinfo.setTenantId(bean.getTenantId());
                tpPersonExinfoMapper.save(exinfo);
                LOGGER.info("扩展信息保存成功");
            }

            // 发布事件，推送人员给第三方系统
            if (null != tpPersonBasicinfoEventService) {
                applicationContext.publishEvent(new TpPersonBasicinfoEvent("人员信息新增同步监听", tpPersonBasicinfoEventService, bean, 1));
            }

            return vo;
        } catch (Exception e) {
            LOGGER.error("新增用户基本信息失败！vo:{}, e: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增用户基本信息失败！");
        }
    }

    /**
     * 添加人员扩展信息
     *
     * @param vo: 扩展信息
     * @author pand
     * @date 2020-11-23 15:37
     */
    @Override
    public TpPersonExinfoVO expAdd(TpPersonExinfoVO vo) {
        try {
            TpPersonExinfo bean = new TpPersonExinfo();
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            int count = tpPersonExinfoMapper.count(vo.getPersonId());
            if (count == 0) {
                tpPersonExinfoMapper.save(bean);
            } else {
                tpPersonExinfoMapper.update(bean);
            }

            return vo;
        } catch (Exception e) {
            LOGGER.error("新增用户扩展信息失败！vo:{}, e: {}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增用户扩展信息失败！");
        }
    }

    /**
     * 查看用户基本信息
     *
     * @param personId: 用户id
     * @return com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO
     * @author pand
     * @date 2020-11-23 16:55
     */
    @Override
    public TpPersonBasicinfoVO view(String personId, String deptId) {
        try {
            TpPersonBasicinfoVO vo = tpPersonBasicinfoMapper.view(personId);
            
            // 解密手机号
            if (vo != null && StrUtil.isNotBlank(vo.getPhone())) {
                String decryptedPhone = PhoneEncryptionUtils.safeDecrypt(vo.getPhone());
                vo.setPhone(decryptedPhone);
            }

            // 查询扩展信息，并将zwzj、zhicheng、partWorkDate等字段赋值到基本信息VO
            TpPersonExinfoVO exinfoVO = tpPersonExinfoMapper.view(personId);
            if (exinfoVO != null) {
                vo.setZwzj(exinfoVO.getZwzj());
                vo.setZhicheng(exinfoVO.getZhicheng());
                vo.setPartWorkDate(exinfoVO.getPartWorkDate());
                LOGGER.debug("查询扩展信息成功，personId: {}, zwzj: {}, zhicheng: {}, partWorkDate: {}", 
                            personId, exinfoVO.getZwzj(), exinfoVO.getZhicheng(), exinfoVO.getPartWorkDate());
            } else {
                LOGGER.debug("扩展信息不存在，personId: {}", personId);
            }

            // 根据用户id查询所在部门
            List<TpDeptBasicinfoVO> list = tpDeptBasicinfoMapper.personDepts(personId);
            StringBuffer deptIds = new StringBuffer(100);
            StringBuffer deptNames = new StringBuffer(100);
            if (!list.isEmpty()) {
                for (TpDeptBasicinfoVO bean : list) {
                    if (StrUtil.equals(bean.getDeptId(), deptId)) {
                        vo.setDeptId(deptId);
                        vo.setDeptFullName(bean.getDeptFullName());
                        vo.setDefaultDept(bean.getDefaultDept());
                    }
                    if (bean.getDefaultDept() == 0) {
                        deptIds.append(bean.getDeptId()).append(",");
                        deptNames.append(bean.getDeptFullName()).append(",");
                    }
                }
            }
            vo.setDeptIds(StrUtil.isNotBlank(deptIds.toString()) ? deptIds.toString().substring(0, deptIds.length() - 1) : "");
            vo.setDeptFullNames(StrUtil.isNotBlank(deptNames.toString()) ? deptNames.toString().substring(0, deptNames.length() - 1) : "");

            return vo;
        } catch (Exception e) {
            LOGGER.error("查看用户基本信息失败！personId:{}, deptId:{}, e: {}", personId, deptId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看用户基本信息失败！");
        }
    }

    /**
     * 查看用户扩展信息
     *
     * @param personId: 用户id
     * @return com.jiuxi.admin.core.bean.vo.TpDeptExinfoVO
     * @author pand
     * @date 2020-11-23 17:13
     */
    @Override
    public TpPersonExinfoVO expView(String personId) {
        try {
            TpPersonExinfoVO vo = tpPersonExinfoMapper.view(personId);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看用户扩展信息失败！personId:{}, e: {}", personId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看用户扩展信息失败！");
        }
    }

    /**
     * 更新用户基本信息
     * 1、修改用户信息，如果手机号码有修改，需要同步修改账号的手机号码，及用户表手机号码与账号表手机号码需要保持一致。
     * 2、如果人员主部门变更，需要将原来人员的主部门删除，重新绑定
     * 3、如果主部门发生变更，将人员部门角色删除，如果主部门没有变更，人员部门角色不删除
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2020-11-24 20:13
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    @CacheEvict(cacheNames = "platform.{TpPersonBasicinfoService}$[86400]", allEntries = true)
    public int update(TpPersonBasicinfoVO vo, String pid) {
        String phone = vo.getPhone();
        // 人员id
        String personId = vo.getPersonId();
        // 新的主部门id
        String deptId = vo.getDeptId();
        String updateTime = CommonDateUtil.now();

        // 除了当前登陆人，有其他人员手机号等于前端传过来的手机号，说明手机号已被别人占用。
        if (StrUtil.isNotBlank(phone)){
            TpPersonBasicinfo personBasicinfo = tpPersonBasicinfoMapper.selectByPhoneAndPersonId(phone, personId);
            if (null != personBasicinfo) {
                LOGGER.error("手机号已被占用phone : {}, personId: {}", phone, personId);
                throw new TopinfoRuntimeException(-1, "手机号已被占用");
            }
        }

        // 维护人员部门角色关系，如果主部门没有变化，人员部门角色不用删除，所以根据人员id先查询主部门信息
        List<TpPersonDeptVO> tpPersonDeptVOS = tpPersonDeptMapper.select("", personId, 1);
        if (null == tpPersonDeptVOS || tpPersonDeptVOS.size() != 1) {
            LOGGER.error("修改用户的主部门有问题:tpPersonDeptVOS = {}", JSONUtil.toJsonStr(tpPersonDeptVOS));
            throw new TopinfoRuntimeException(-1, "修改用户的主部门有问题");
        }
        // 获取未变化前的主部门id
        String oldDeptId = tpPersonDeptVOS.get(0).getDeptId();

        TpPersonBasicinfo bean = new TpPersonBasicinfo();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        
        // 对手机号进行加密
        if (StrUtil.isNotBlank(vo.getPhone())) {
            String encryptedPhone = PhoneEncryptionUtils.encrypt(vo.getPhone());
            bean.setPhone(encryptedPhone); // 将加密后的手机号存储到phone字段
            LOGGER.debug("手机号加密处理完成，personId: {}, 原始手机号长度: {}, 加密后长度: {}", 
                personId, vo.getPhone().length(), encryptedPhone.length());
        }
        
        bean.setUpdator(pid);
        bean.setUpdateTime(updateTime);

        try {
            if (StrUtil.isNotBlank(deptId)){
                // 如果主部门发生变化，删除人员部门角色关联关系，同时更新人员表的单位id字段ASCN_ID
                if (!StrUtil.equals(oldDeptId, deptId)) {
                    List<String> roleDepts = new ArrayList<>();
                    roleDepts.add(oldDeptId);
                    LOGGER.warn("操作人:{}, 被操作人: {} 的部门发生变化，删除部门: {} 关联关系.",pid, personId, deptId);
                    tpPersonRoleMapper.deleteByPersonIdAndDeptIds(personId, roleDepts);
                    // 查询新部门的单位id,并赋值
                    TpDeptBasicinfoVO tpDeptBasicinfoVO = tpDeptBasicinfoMapper.selectDeptById(deptId);
                    bean.setAscnId(tpDeptBasicinfoVO.getAscnId());

                    // 维护人员部门关系
                    TpPersonDept personDeptBean = new TpPersonDept();
                    personDeptBean.setPersonId(personId);
                    personDeptBean.setDeptId(deptId);
                    personDeptBean.setDefaultDept(1);
                    // 更新主部门信息
                    tpPersonDeptMapper.update(personDeptBean);
                }
            }
            // 如果手机号有更改，需要将账号表的手机号更新掉
            TpAccountVO tpAccountVO = userAccountService.accountView(personId);
            if (null != tpAccountVO && !StrUtil.equals(tpAccountVO.getPhone(), phone)) {
                // 更新该人员账号的手机号，使用PersonAccountService来保持一致性
                personAccountService.updatePhone(personId, vo.getPhone());
            }

            // 附件绑定
            tpAttachinfoService.band(vo.getFile(), personId, updateTime);

            // 更新人员基本信息
            int count = tpPersonBasicinfoMapper.update(bean);

            // 更新或保存职务职级、职称、参加工作时间到扩展信息表
            if (StrUtil.isNotBlank(vo.getZwzj()) || StrUtil.isNotBlank(vo.getZhicheng()) || StrUtil.isNotBlank(vo.getPartWorkDate())) {
                LOGGER.info("准备更新扩展信息，personId：{}, zwzj：{}, zhicheng：{}, partWorkDate：{}", 
                            personId, vo.getZwzj(), vo.getZhicheng(), vo.getPartWorkDate());
                
                // 检查扩展信息是否存在
                int exinfoCount = tpPersonExinfoMapper.count(personId);
                TpPersonExinfo exinfo = new TpPersonExinfo();
                exinfo.setPersonId(personId);
                exinfo.setZwzj(vo.getZwzj());
                exinfo.setZhicheng(vo.getZhicheng());
                exinfo.setPartWorkDate(vo.getPartWorkDate());
                
                if (exinfoCount == 0) {
                    // 不存在则新增
                    exinfo.setTenantId(bean.getTenantId());
                    tpPersonExinfoMapper.save(exinfo);
                    LOGGER.info("扩展信息新增成功");
                } else {
                    // 存在则更新
                    tpPersonExinfoMapper.update(exinfo);
                    LOGGER.info("扩展信息更新成功");
                }
            }

            // 发布事件，推送人员给第三方系统
            if (null != tpPersonBasicinfoEventService) {
                applicationContext.publishEvent(new TpPersonBasicinfoEvent("人员信息修改同步监听", tpPersonBasicinfoEventService, bean, 2));
            }

            return count;
        } catch (Exception e) {
            // 事务回滚
            LOGGER.error("用户基本信息修改失败！原始异常: {}, 异常消息: {}, vo:{}, 堆栈: {}", 
                e.getClass().getName(), e.getMessage(), JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "用户基本信息修改失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除用户信息，只删除以当前部门为主部门的人员和账号信息，如果是兼职部门，只删除关联关系
     * 1、删除用户信息，用户账户设置为无效
     * <p>
     * 3、删除用户信息，根据系统配置决定是逻辑删除还是物理删除
     *
     * @param personIds:
     * @return void
     * @author pand
     * @date 2020-11-25 11:07
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    @CacheEvict(cacheNames = "platform.{TpPersonBasicinfoService}$[86400]", allEntries = true)
    public void deletes(String deptIds, String personIds, String pid) {

        // 判断删除的人员是否包含自己, 可以删除兼职部门中的自己
        if (personIds.contains(pid)) {
            SessionVO sessionVO = SessionHolder.get();
            if (deptIds.contains(sessionVO.getDeptId())){
                throw new TopinfoRuntimeException(-1, "不能删除自己！");
            }
        }

        String[] personIdArr = StringUtils.split(personIds, ",");
        String[] deptIdArr = StringUtils.split(deptIds, ",");
        String updateTime = CommonDateUtil.now();

        // 检查是否启用物理删除
        boolean isPhysicalDeleteEnabled = tpSystemConfigService.isPhysicalDeleteEnabled();
        LOGGER.info("物理删除开关状态: {}", isPhysicalDeleteEnabled ? "启用" : "禁用");

        TpPersonBasicinfo tpPersonBasicinfo = new TpPersonBasicinfo();

        for (int i = 0; i < personIdArr.length; i++) {
            try {
                String personId = personIdArr[i];
                String deptId = deptIdArr[i];

                int defaultDept = tpPersonDeptMapper.selectByDeptIdAndPersonId(deptId, personId);
                if (defaultDept == 1) {
                    if (isPhysicalDeleteEnabled) {
                        // 物理删除模式
                        LOGGER.info("执行物理删除，操作人:{}, 被操作人: {} , 部门：{}", pid, personId, deptId);
                        
                        // 1. 用户账户物理删除
                        userAccountService.physicalDeleteByPersonId(personId);

                        // 2. 用户信息物理删除
                        physicalDeleteByPersonId(personId);
                    } else {
                        // 逻辑删除模式（原有逻辑）
                        LOGGER.info("执行逻辑删除，操作人:{}, 被操作人: {} , 部门：{}", pid, personId, deptId);
                        
                        // 1. 用户账户设置为无效
                        userAccountService.deleteByPersonId(personId);

                        // 2. 用户信息逻辑删除
                        tpPersonBasicinfo.setPersonId(personId);
                        tpPersonBasicinfo.setUpdator(pid);
                        tpPersonBasicinfo.setActived(0);
                        tpPersonBasicinfo.setUpdateTime(updateTime);
                        tpPersonBasicinfo.setPhone(CommonUniqueIndexUtil.addDeleteTime(tpPersonBasicinfo.getPhone()));
                        tpPersonBasicinfoMapper.deleteByPersonId(tpPersonBasicinfo);
                    }
                }

                // 3. 用户，部门关联关系删除
                tpPersonDeptMapper.deleteByDeptIdPersonId(deptId, personId);

                // 4. 用户，角色关联关系删除
                LOGGER.warn("操作人:{}, 被操作人: {} , 部门：{}, 删除用户信息，删除人员部门角色关联关系.",pid, personId, deptId);
                tpPersonRoleMapper.delete(deptId, personId);


                // 发布事件，推送人员给第三方系统
                if (null != tpPersonBasicinfoEventService) {
                    TpPersonBasicinfo bean = new TpPersonBasicinfo();
                    bean.setPersonId(personId);
                    applicationContext.publishEvent(new TpPersonBasicinfoEvent("人员信息删除同步监听", tpPersonBasicinfoEventService, bean, 3));
                }
            } catch (Exception e) {
                // 事务回滚
                LOGGER.error("删除用户信息失败！原始异常: {}, 异常消息: {}, deptIds:{}, personIds:{}, 堆栈: {}", 
                    e.getClass().getName(), e.getMessage(), deptIds, personIds, ExceptionUtils.getStackTrace(e));
                throw new TopinfoRuntimeException(-1, "删除用户信息失败: " + e.getMessage());
            }
        }
    }

    /**
     * 人员兼职/解除兼职，人员挂职到兼职部门/人员取消兼职部门
     *
     * @param personId: 人员id
     * @param deptIds:  部门id
     * @return int
     * @author pand
     * @date 2020-11-25 11:19
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int parttime(String personId, String deptIds) {
        try {
            // 兼职部门标识
            int parttimeJobDept = 0;
            int defaultDept = 1;
            // 前端传回部门ids
            String[] deptIdArr = StringUtils.split(deptIds, ",");
            int len = deptIdArr.length;
            // 查询tp_person_dept,查处所有兼职部门
            List<TpPersonDeptVO> parttimeDept = tpPersonDeptMapper.select("", personId, 0);
            // 所有兼职部门的ids
            List<String> oldDeptIds = parttimeDept.stream().map(TpPersonDeptVO::getDeptId).collect(Collectors.toList());
            if (len == 0) {
                // 兼职部门不为空时执行删除
                if (CollectionUtil.isNotEmpty(oldDeptIds)) {
                    // 清空人员部门关联数据以及清空人员角色关联数据
                    // 1、根据人员id删除兼职部门
                    tpPersonDeptMapper.deleteByPersonId(personId, parttimeJobDept);
                    // 2、根据人员id和部门id删除兼职部门
                    LOGGER.warn("被操作人: {} , 部门：{}, 兼职/解除兼职，删除人员部门角色关联关系1.",personId, parttimeJobDept);
                    tpPersonRoleMapper.deleteByPersonIdAndDeptIds(personId, oldDeptIds);
                }
            } else {
                List<String> newDeptIds = Arrays.asList(deptIdArr);
                // 重新绑定人员部门关联关系和人员角色关联关系
                List<String> oldDept = new ArrayList<>();
                for (String oldDeptId : oldDeptIds) {
                    if (!newDeptIds.contains(oldDeptId)) {
                        // 删除oldDeptId
                        // 1、根据人员id删除兼职部门
                        tpPersonDeptMapper.delete(oldDeptId, personId, parttimeJobDept);
                        // 2、根据人员id和部门id删除兼职部门
                        oldDept.add(oldDeptId);
                    }
                }
                if (oldDept.size() > 0) {
                    LOGGER.warn("被操作人: {} , 部门：{}, 兼职/解除兼职，删除人员部门角色关联关系2.",personId, parttimeJobDept);
                    tpPersonRoleMapper.deleteByPersonIdAndDeptIds(personId, oldDept);
                }

                // 查询主部门ID，如果传过来的兼职部门包含主部门，不做任何操作
                List<TpPersonDeptVO> list = tpPersonDeptMapper.select("", personId, defaultDept);
                if (list.size() != 1) {
                    throw new TopinfoRuntimeException(-1, "用户默认所在部门为空！");
                }

                for (String newDeptId : newDeptIds) {
                    if (!StrUtil.equals(newDeptId, list.get(0).getDeptId()) && !oldDeptIds.contains(newDeptId)) {
                        // 新增newDeptId
                        // 1、新增人员部门关系数据
                        TpPersonDept bean = new TpPersonDept();
                        bean.setDeptId(newDeptId);
                        bean.setPersonId(personId);
                        bean.setDefaultDept(parttimeJobDept);
                        tpPersonDeptMapper.save(bean);
                    }
                }
            }

            return 0;
        } catch (Exception e) {
            // 事务回滚
            LOGGER.error("用户兼职部门失败！ personId:{}, deptIds:{}, e: {}", personId, deptIds, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "用户兼职部门失败！");
        }
    }

    /**
     * 查询用户所拥有的角色
     *
     * @param personId:
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpPersonRoleVO>
     * @author pand
     * @date 2020-11-25 13:43
     */
    @Override
    public List<TpPersonRoleVO> personRoles(String deptId, String personId) {
        try {
            List<TpPersonRoleVO> list = tpPersonRoleMapper.selectByPersonIdAndDeptId(deptId, personId);
            return list;
        } catch (Exception e) {
            LOGGER.error("查询用户角色失败！ deptId:{}, personId:{}, e: {}", deptId, personId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询用户角色失败！");
        }
    }

    /**
     * 用户授权
     *
     * @param personId: 用户id
     * @param deptId:   部门id
     * @param roleIds:  角色id
     * @return void
     * @author pand
     * @date 2020-11-25 14:00
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int auth(String personId, String deptId, String roleIds) {

        Validate.notBlank(personId, "人员id不能为空！");
        Validate.notBlank(deptId, "部门id不能为空！");

        try {

            // 先删除拥有角色

            // 人员id和部门id不能为空！
            if (StrUtil.isBlank(personId) || StrUtil.isBlank(deptId)) {
                throw new TopinfoRuntimeException(-1, "人员id和部门id不能为空！");
            }
            LOGGER.warn("被操作人: {} , 部门：{}, 用户授权，删除人员部门角色关联关系.",personId, deptId);
            tpPersonRoleMapper.delete(deptId, personId);

            // 处理roleIds为空的情况
            if (StrUtil.isNotBlank(roleIds)) {
                String[] roleIdArr = StringUtils.split(roleIds, ",");
                if (roleIdArr != null && roleIdArr.length > 0) {
                    int len = roleIdArr.length;

                    for (int i = 0; i < len; i++) {
                        TpPersonRole bean = new TpPersonRole();
                        bean.setPersonId(personId);
                        bean.setDeptId(deptId);
                        bean.setRoleId(roleIdArr[i]);
                        tpPersonRoleMapper.save(bean);
                    }
                    return len;
                }
            }
            
            // 如果roleIds为空，只删除角色，不添加新角色
            LOGGER.info("roleIds为空，只删除用户 {} 在部门 {} 的所有角色", personId, deptId);

            return 0;
        } catch (Exception e) {
            // 事务回滚
            LOGGER.error("用户授权失败！deptId:{}, personId:{}, roleIds:{}, e: {}", deptId, personId, roleIds, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "用户授权失败！");
        }
    }

    /**
     * 根据人员id 获取人员基本信息
     * @author 杨攀
     * @date 2023/11/15 16:28
     * @param personId
     * @return com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpPersonBasicinfoService}$[86400]", key = "#root.methodName + ':' + #personId")
    public TpPersonBasicinfoVO getPersonBasicinfo(String personId) {
        TpPersonBasicinfoVO vo = tpPersonBasicinfoMapper.view(personId);
        
        // 解密手机号
        if (vo != null && StrUtil.isNotBlank(vo.getPhone())) {
            String decryptedPhone = PhoneEncryptionUtils.safeDecrypt(vo.getPhone());
            vo.setPhone(decryptedPhone);
        }
        
        return vo;
    }

    @Override
    public TpPersonBasicinfoVO getBaseInfoByIdCard(String idcard) {
        List<TpPersonBasicinfoVO> list = tpPersonBasicinfoMapper.getBaseInfoByIdCard(idcard);

        if (list.size() > 1){
            LOGGER.error("根据身份证号，查询到多条数据。idcard: {}", idcard);
            throw new TopinfoRuntimeException(-1, "根据身份证号，查询到多条数据");
        }
        if (list.size() == 1){
            TpPersonBasicinfoVO vo = list.get(0);
            // 解密手机号
            if (vo != null && StrUtil.isNotBlank(vo.getPhone())) {
                String decryptedPhone = PhoneEncryptionUtils.safeDecrypt(vo.getPhone());
                vo.setPhone(decryptedPhone);
            }
            return vo;
        }
        return null;
    }

    /**
     * 校验手机号是否存在
     *
     * @param phone     手机号
     * @param personId  人员id（修改校验时必填）
     * @return boolean 存在返回 true, 不存在返回 false
     * @author 杨占锐
     * @date 2024/5/30 15:49
     */
    @Override
    public boolean existsPhone(String phone, String personId) {
        Validate.notBlank(phone, "手机号不能为空！");
        TpPersonBasicinfo tpPersonBasicinfo = tpPersonBasicinfoMapper.selectByPhoneAndPersonId(phone, personId);

        return tpPersonBasicinfo != null;
    }

    /**
     * 查询出在该单位所有部门下的所有主部门关联关系的人员id
     *
     * @param ascnId 单位id
     * @return java.util.List<java.lang.String>
     * @author 杨占锐
     * @date 2024/6/4 19:12
     */
    @Override
    public List<String> selectPersonIdByAscnId(String ascnId) {
        return tpPersonBasicinfoMapper.selectPersonIdByAscnId(ascnId);
    }

    /**
     * 根据企业id删除人员
     *
     * @param ascnId 单位id
     * @param jwtpid 操作人id
     * @return void
     * @author 杨占锐
     * @date 2024/6/4 19:24
     */
    @Override
    @CacheEvict(cacheNames = "platform.{TpPersonBasicinfoService}$[86400]", allEntries = true)
    public void deletePersonDeptByAscnId(String ascnId, String jwtpid) {
        tpPersonBasicinfoMapper.deletePersonDeptByAscnId(ascnId, jwtpid, CommonDateUtil.now());
    }

    /**
     * 修改手机号
     *
     * @param personId 人员id
     * @param phone    手机号
     * @param jwtpid   操作人id
     * @return int
     * @author 杨占锐
     * @date 2024/5/30 17:10
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "platform.{TpPersonBasicinfoService}$[86400]", allEntries = true)
    public void updatePhone(String personId, String phone, String jwtpid) {

        // 查询手机号是否重复
        TpPersonBasicinfo tpPersonBasicinfo = tpPersonBasicinfoMapper.selectByPhoneAndPersonId(phone, personId);
        if (tpPersonBasicinfo != null) {
            LOGGER.error("手机号已被占用phone : {}, personId: {}", phone, personId);
            throw new TopinfoRuntimeException(-1, "手机号已被占用");
        }

        try {
            // 更新人员表手机号
            TpPersonBasicinfo bean = new TpPersonBasicinfo();
            bean.setPersonId(personId);
            bean.setPhone(phone);
            bean.setUpdateTime(CommonDateUtil.now());
            bean.setUpdator(jwtpid);
            tpPersonBasicinfoMapper.update(bean);

            // 更新账号表手机号
            personAccountService.updatePhone(personId, phone);
        } catch (Exception e){
            LOGGER.error("修改手机号失败 phone: {}, personId: {}，e: {}", phone, personId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改手机号失败");
        }

    }

    /**
     * 导出人员信息到Excel
     *
     * @param query    查询条件
     * @param jwtpid   操作人id
     * @param response HTTP响应对象
     * @author Trae AI
     * @date 2024/12/19
     */
    @Override
    public void exportExcel(TpPersonBasicQuery query, String jwtpid, HttpServletResponse response) throws Exception {
        try {
            List<TpPersonBasicinfoVO> personList;
            
            // 如果有选中的用户ID列表，则只导出选中的用户
            if (query.getSelectedUserIds() != null && !query.getSelectedUserIds().isEmpty()) {
                personList = new ArrayList<>();
                for (String userId : query.getSelectedUserIds()) {
                    TpPersonBasicinfoVO person = tpPersonBasicinfoMapper.view(userId);
                    if (person != null) {
                        // 设置部门名称
                        TpDeptBasicinfoVO tpDeptBasicinfoVO = tpDeptBasicinfoMapper.selectDeptById(person.getAscnId());
                        if (tpDeptBasicinfoVO != null) {
                            person.setAscnName(tpDeptBasicinfoVO.getDeptFullName());
                        }
                        personList.add(person);
                    }
                }
            } else {
                // 查询所有数据（不分页）
                query.setCurrent(1);
                query.setSize(Integer.MAX_VALUE);
                IPage<TpPersonBasicinfoVO> pageResult = queryPage(query);
                personList = pageResult.getRecords();
            }

            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("人员信息");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"姓名", "性别", "身份证号", "手机号", "邮箱", "所属部门", "职务", "创建时间"};
            
            // 设置标题样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 创建数据行
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < personList.size(); i++) {
                Row row = sheet.createRow(i + 1);
                TpPersonBasicinfoVO person = personList.get(i);
                
                // 解密手机号
                String decryptedPhone = "";
                if (StrUtil.isNotBlank(person.getPhone())) {
                    decryptedPhone = PhoneEncryptionUtils.safeDecrypt(person.getPhone());
                }
                
                row.createCell(0).setCellValue(StrUtil.isNotBlank(person.getPersonName()) ? person.getPersonName() : "");
                row.createCell(1).setCellValue(person.getSex() != null ? (person.getSex() == 1 ? "男" : "女") : "");
                row.createCell(2).setCellValue(StrUtil.isNotBlank(person.getIdcard()) ? person.getIdcard() : "");
                row.createCell(3).setCellValue(decryptedPhone);
                row.createCell(4).setCellValue(StrUtil.isNotBlank(person.getEmail()) ? person.getEmail() : "");
                row.createCell(5).setCellValue(StrUtil.isNotBlank(person.getAscnName()) ? person.getAscnName() : "");
                row.createCell(6).setCellValue(StrUtil.isNotBlank(person.getOffice()) ? person.getOffice() : "");
                row.createCell(7).setCellValue(StrUtil.isNotBlank(person.getCreateTime()) ? person.getCreateTime() : "");
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 设置响应头
            String fileName = "人员信息_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

            // 写入响应流
            workbook.write(response.getOutputStream());
            workbook.close();
            
        } catch (IOException e) {
            LOGGER.error("导出Excel失败！query:{}, e: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "导出Excel失败！");
        } catch (Exception e) {
            LOGGER.error("导出Excel失败！query:{}, e: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "导出Excel失败！" : e.getMessage());
        }
    }

    /**
     * 导入用户信息从Excel
     *
     * @param file   Excel文件
     * @param deptId 部门ID
     * @param jwtpid 操作人id
     * @return 导入结果
     * @throws Exception 导入异常
     * @author Trae AI
     * @date 2024/12/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse importExcel(MultipartFile file, String deptId, String jwtpid) throws Exception {
        try {
            if (file == null || file.isEmpty()) {
                return JsonResponse.buildFailure("请选择要导入的Excel文件");
            }

            // 验证文件格式
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                return JsonResponse.buildFailure("请上传Excel格式文件(.xlsx或.xls)");
            }

            // 验证文件大小（限制10MB）
            if (file.getSize() > 10 * 1024 * 1024) {
                return JsonResponse.buildFailure("文件大小不能超过10MB");
            }

            Workbook workbook = null;
            try {
                // 读取Excel文件
                if (fileName.endsWith(".xlsx")) {
                    workbook = new XSSFWorkbook(file.getInputStream());
                } else {
                    workbook = WorkbookFactory.create(file.getInputStream());
                }

                Sheet sheet = workbook.getSheetAt(0);
                if (sheet == null) {
                    return JsonResponse.buildFailure("Excel文件中没有找到工作表");
                }

                // 验证表头
                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    return JsonResponse.buildFailure("Excel文件格式错误：缺少表头");
                }

                // 预期的表头
                String[] expectedHeaders = {"姓名", "手机号", "身份证号", "性别", "邮箱"};
                
                // 验证表头是否匹配
                for (int i = 0; i < expectedHeaders.length; i++) {
                    Cell cell = headerRow.getCell(i);
                    if (cell == null || !expectedHeaders[i].equals(getCellValue(cell))) {
                        return JsonResponse.buildFailure("Excel文件格式错误：表头不匹配。期望格式：" + String.join(", ", expectedHeaders));
                    }
                }

                // 先检测Excel中是否有重复的手机号
                List<String> phoneList = new ArrayList<>();
                List<String> duplicatePhones = new ArrayList<>();
                int totalRows = sheet.getLastRowNum();
                
                // 第一遍扫描：收集所有手机号并检测重复
                for (int i = 1; i <= totalRows; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    
                    String phone = getCellValue(row.getCell(1)); // 手机号在第2列
                    if (StrUtil.isNotBlank(phone)) {
                        if (phoneList.contains(phone)) {
                            if (!duplicatePhones.contains(phone)) {
                                duplicatePhones.add(phone);
                            }
                        } else {
                            phoneList.add(phone);
                        }
                    }
                }
                
                // 如果发现重复手机号，取消导入并提醒用户
                if (!duplicatePhones.isEmpty()) {
                    StringBuilder errorMsg = new StringBuilder();
                    errorMsg.append("Excel文件中存在重复的手机号，请修正后重新导入：\n");
                    for (String phone : duplicatePhones) {
                        errorMsg.append("- ").append(phone).append("\n");
                    }
                    return JsonResponse.buildFailure(errorMsg.toString());
                }

                List<String> successList = new ArrayList<>();
                List<String> errorList = new ArrayList<>();
                
                // 第二遍扫描：正式导入数据
                for (int i = 1; i <= totalRows; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }

                    try {
                        // 读取单元格数据
                        String personName = getCellValue(row.getCell(0));
                        String phone = getCellValue(row.getCell(1));
                        String idcard = getCellValue(row.getCell(2));
                        String sexStr = getCellValue(row.getCell(3));
                        String email = getCellValue(row.getCell(4));

                        // 验证必填字段
                        if (StrUtil.isBlank(personName)) {
                            errorList.add("第" + (i + 1) + "行：姓名不能为空");
                            continue;
                        }
                        if (StrUtil.isBlank(phone)) {
                            errorList.add("第" + (i + 1) + "行：手机号不能为空");
                            continue;
                        }

                        // 验证手机号格式
                        if (!phone.matches("^1[3-9]\\d{9}$")) {
                            errorList.add("第" + (i + 1) + "行：手机号格式不正确");
                            continue;
                        }

                        // 检查手机号是否已存在
                        if (existsPhone(phone, null)) {
                            errorList.add("第" + (i + 1) + "行：手机号" + phone + "已存在");
                            continue;
                        }

                        // 根据传入的部门ID查找部门信息
                        TpDeptBasicinfoVO dept = tpDeptBasicinfoMapper.selectDeptById(deptId);
                        if (dept == null) {
                            errorList.add("第" + (i + 1) + "行：指定的部门不存在");
                            continue;
                        }

                        // 转换性别
                        Integer sex = 0; // 默认保密
                        if ("男".equals(sexStr)) {
                            sex = 1;
                        } else if ("女".equals(sexStr)) {
                            sex = 2;
                        }

                        // 创建用户对象
                        TpPersonBasicinfoVO personVO = new TpPersonBasicinfoVO();
                        personVO.setPersonName(personName);
                        personVO.setPhone(phone);
                        personVO.setIdcard(idcard);
                        personVO.setSex(sex);
                        personVO.setEmail(email);
                        personVO.setDeptId(dept.getDeptId());
                        personVO.setAscnId(dept.getAscnId());
                        // 导入时不设置附件
                        personVO.setFile(null);

                        // 添加用户
                        LOGGER.info("开始导入第{}行用户：{}, 手机号：{}, 部门ID：{}", i + 1, personName, phone, deptId);
                        // 确保category不为null，默认为0
                        Integer category = dept.getCategory() != null ? dept.getCategory() : 0;
                        TpPersonBasicinfoVO result = add(personVO, jwtpid, category);
                        LOGGER.info("第{}行用户导入完成，返回结果：{}", i + 1, result != null ? "成功" : "失败");
                        successList.add("第" + (i + 1) + "行：" + personName + "导入成功");

                    } catch (Exception e) {
                        LOGGER.error("导入第{}行数据失败: {}", i + 1, ExceptionUtils.getStackTrace(e));
                        String errorMsg = "导入失败";
                        if (StrUtil.isNotBlank(e.getMessage())) {
                            errorMsg += " - " + e.getMessage();
                        } else if (e instanceof NullPointerException) {
                            errorMsg += " - 数据为空或格式错误";
                        } else {
                            errorMsg += " - 系统异常";
                        }
                        errorList.add("第" + (i + 1) + "行：" + errorMsg);
                    }
                }

                // 构建返回结果
                StringBuilder resultMsg = new StringBuilder();
                resultMsg.append("导入完成！");
                resultMsg.append("成功：").append(successList.size()).append("条，");
                resultMsg.append("失败：").append(errorList.size()).append("条");

                if (!errorList.isEmpty()) {
                    resultMsg.append("\n失败详情：\n");
                    for (String error : errorList) {
                        resultMsg.append(error).append("\n");
                    }
                }

                // 根据导入结果决定返回成功还是失败
                if (successList.isEmpty() && !errorList.isEmpty()) {
                    // 全部失败
                    return JsonResponse.buildFailure(resultMsg.toString());
                } else if (!errorList.isEmpty()) {
                    // 部分失败
                    return JsonResponse.buildFailure(resultMsg.toString());
                } else {
                    // 全部成功
                    return JsonResponse.buildSuccess(resultMsg.toString());
                }

            } finally {
                if (workbook != null) {
                    workbook.close();
                }
            }

        } catch (Exception e) {
            LOGGER.error("导入Excel失败！e: {}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "导入Excel失败！" : e.getMessage());
        }
    }

    /**
     * 获取单元格值
     *
     * @param cell 单元格
     * @return 单元格值
     */
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) throws Exception {
        try {
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("人员信息导入模板");

            // 创建表头
            String[] headers = {"姓名", "手机号", "身份证号", "性别", "邮箱"};
            Row headerRow = sheet.createRow(0);
            
            // 设置表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 添加示例数据行
            Row exampleRow = sheet.createRow(1);
            String[] exampleData = {"张三", "13800138000", "110101199001011234", "男", "zhangsan@example.com"};
            
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            
            for (int i = 0; i < exampleData.length; i++) {
                Cell cell = exampleRow.createCell(i);
                cell.setCellValue(exampleData[i]);
                cell.setCellStyle(dataStyle);
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // 设置最小列宽
                if (sheet.getColumnWidth(i) < 3000) {
                    sheet.setColumnWidth(i, 3000);
                }
            }

            // 设置响应头
            String fileName = "人员信息导入模板_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + ".xlsx";
            
            // 清除缓存相关头信息
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            
            // 设置内容类型和编码
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            
            // 设置文件下载头信息
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"; filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            
            // 写入响应流并确保完整输出
            response.getOutputStream().flush();
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
            workbook.close();
            
        } catch (IOException e) {
            LOGGER.error("下载Excel模板失败！e: {}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "下载Excel模板失败！");
        }
    }

    /**
     * 根据人员ID物理删除人员信息
     *
     * @param personId 人员ID
     * @return void
     * @author 系统生成
     * @date 2024/12/24
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    @CacheEvict(cacheNames = "platform.{TpPersonBasicinfoService}$[86400]", allEntries = true)
    public void physicalDeleteByPersonId(String personId) {
        try {
            TpPersonBasicinfoVO view = tpPersonBasicinfoMapper.view(personId);
            if (view == null) {
                LOGGER.warn("根据人员id未查询到人员信息，跳过人员物理删除。personId:{}", personId);
                return;
            }
            int deletedCount = tpPersonBasicinfoMapper.physicalDeleteByPersonId(personId);
            if (deletedCount > 0) {
                LOGGER.info("成功物理删除人员信息。personId:{}, 删除记录数:{}", personId, deletedCount);
            } else {
                LOGGER.warn("物理删除人员信息失败，未找到匹配记录。personId:{}", personId);
            }
        } catch (Exception e) {
            LOGGER.error("物理删除人员信息时发生异常。personId:{}, 异常:{}", personId, e.getMessage(), e);
            throw new TopinfoRuntimeException(-1, "物理删除人员信息失败：" + e.getMessage());
        }
    }
}