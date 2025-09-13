package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.vo.*;
import com.jiuxi.module.user.app.service.UserAccountService;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.constant.enums.OpertionTypeEnum;
import com.jiuxi.admin.core.bean.entity.TpEntBasicinfo;
import com.jiuxi.admin.core.bean.query.TpEntAccountQuery;
import com.jiuxi.admin.core.bean.query.TpEntBasicQuery;
import com.jiuxi.admin.core.event.TpEntBasicinfoEvent;
import com.jiuxi.admin.core.listener.TpEntBasicinfoEventCollection;
import com.jiuxi.admin.core.listener.service.TpEntBasicinfoEventService;
import com.jiuxi.module.user.infra.persistence.mapper.UserAccountMapper;
import com.jiuxi.admin.core.mapper.TpCityMapper;
import com.jiuxi.admin.core.mapper.TpDeptBasicinfoMapper;
import com.jiuxi.admin.core.mapper.TpEntBasicinfoMapper;
import com.jiuxi.module.user.infra.persistence.mapper.UserPersonMapper;
import com.jiuxi.admin.core.service.TpDeptBasicinfoService;
import com.jiuxi.admin.core.service.TpAccountService;
import com.jiuxi.admin.core.mapper.TpPersonBasicinfoMapper;
import com.jiuxi.admin.core.service.TpEntBasicinfoService;
import com.jiuxi.common.bean.ErrorCode;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.geo.GeoUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.CommonUniqueIndexUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
// import com.jiuxi.plugin.api.bean.query.TpEntDeleteQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: TpEntBasicinfoServiceImpl
 * @Description: 企业基本信息表
 * @Author 杨攀
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpEntBasicinfoService")
public class TpEntBasicinfoServiceImpl implements TpEntBasicinfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpEntBasicinfoServiceImpl.class);

    @Autowired
    private TpEntBasicinfoMapper tpEntBasicinfoMapper;

    @Autowired
    private TpAccountService tpAccountService;
    
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private TpCityMapper tpCityMapper;

    @Autowired
    private TpDeptBasicinfoService tpDeptBasicinfoService;

    @Autowired
    private TpPersonBasicinfoMapper tpPersonBasicinfoMapper;

    @Autowired
    private TpDeptBasicinfoMapper tpDeptBasicinfoMapper;

    /**
     * 上下文对象
     */
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TpEntBasicinfoEventCollection tpEntBasicinfoEventCollection;

    @Autowired(required = false)
    private TpEntBasicinfoEventService tpEntBasicinfoEventService;

    @PostConstruct
    private void init() {
        if (tpEntBasicinfoEventService != null){
            tpEntBasicinfoEventCollection.addEvent(tpEntBasicinfoEventService);
        }
    }

    /**
     * @param query
     * @param ascnId
     * @return com.baomidou.mybatisplus.core.metadata.IPage
     *
     * <pre>
     * 我是超管： <br/>
     *    查询所有企业 <br/>
     * 我是分管： <br/>
     *    查询自己辖区的企业<br/>
     * 我是企业分管：<br/>
     *    不能进入该列表界面
     *    </pre>
     * @author 杨攀
     * @date 2020/11/27 14:14
     */
    @Override
    public IPage<TpEntBasicinfoVO> queryPage(TpEntBasicQuery query, String ascnId) {

        Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);
        Page<TpEntBasicinfoVO> page = new Page<>(pageNum, pageSize);

        // 超级管理员的人员id
        if (StrUtil.equals(TpConstant.ADMIN.ASCNID, ascnId)) {

            // 如果有选择行政区划ID，则根据行政区划查询
            if (StrUtil.isNotBlank(query.getEntAddrCode())) {
                // 政府分级管理员， 查询自己辖区的企业
                // 查询行政区划code
                String cityCode = tpCityMapper.selectCityCodeByCityId(query.getEntAddrCode());
                query.setCityCode(cityCode);
            }

            // 查询所有的企业
            IPage<TpEntBasicinfoVO> iPage = tpEntBasicinfoMapper.selectPageByCityCode(page, query);
            return iPage;
        } else {

            // 政府分级管理员， 查询自己辖区的企业
            String cityCode = tpDeptBasicinfoMapper.selectCityCodeByAscnId(ascnId);

            // 政府分级管理员，如果有选择行政区划ID，则根据行政区划查询行政区划code
            if (StrUtil.isNotBlank(query.getEntAddrCode())) {
                // 根据行政区划查询行政区划code
                String selectCityCode = tpCityMapper.selectCityCodeByCityId(query.getEntAddrCode());

                if (StrUtil.isNotBlank(selectCityCode) && selectCityCode.indexOf(cityCode) == 0) {
                    query.setCityCode(cityCode);
                } else {
                    throw new TopinfoRuntimeException(ErrorCode.ERROR.getCode(), "参数异常...");
                }
            } else {
                query.setCityCode(cityCode);
            }

            // 查询本辖区的企业列表
            IPage<TpEntBasicinfoVO> iPage = tpEntBasicinfoMapper.selectPageByCityCode(page, query);
            return iPage;
        }
    }

    /**
     * 分页查询企业账号
     * <p>
     * 2021.12.10 过期原因，企业管理 -> 账号信息，根据企业查询管理账号，只管理企业管理员账号。该接口过期，使用entAdminList接口。
     *
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.jiuxi.admin.core.bean.vo.TpEntAccountVO>
     * @author pand
     * @date 2021-09-13 10:24
     */
    @Deprecated
    @Override
    public IPage<TpEntAccountVO> accountQueryPage(TpEntAccountQuery query, String jwtpid, String jwtaid) {
        // 根据企业id查询企业的顶级部门信息
        TpDeptBasicinfoVO tpDeptBasicinfoVO = tpDeptBasicinfoService.getById(query.getEntId(), jwtpid, jwtaid);
        if (null == tpDeptBasicinfoVO) {
            LOGGER.error("根据企业id {} 查询不到顶级部门", query.getEntId());
            throw new TopinfoRuntimeException(-1, "请选择企业查询账号");
        }
        String deptLevelcode = tpDeptBasicinfoVO.getDeptLevelcode();
        query.setDeptLevelcode(deptLevelcode);
        Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);
        Page<TpEntAccountVO> page = new Page<>(pageNum, pageSize);
        IPage<TpEntAccountVO> iPage = tpEntBasicinfoMapper.accountQueryPage(page, query);
        return iPage;
    }


    /**
     * 分页查询企业顶级部门账号，即可以当作企业管理员的账号
     *
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.jiuxi.admin.core.bean.vo.TpEntAccountVO>
     * @author pand
     * @date 2021-09-13 10:24
     */
    @Override
    public IPage<TpEntAccountVO> adminQueryPage(TpEntAccountQuery query) {
        Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
        Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);
        Page<TpEntAccountVO> page = new Page<>(pageNum, pageSize);
        IPage<TpEntAccountVO> iPage = tpEntBasicinfoMapper.adminQueryPage(page, query);
        return iPage;
    }

    /**
     * 查看企业基本信息
     *
     * @param entId
     * @return com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO
     * @author 杨攀
     * @date 2020/11/30 10:14
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpEntBasicinfoService}$[86400]", key = "#root.methodName + ':' + #entId")
    public TpEntBasicinfoVO view(String entId) {
        TpEntBasicinfoVO vo = tpEntBasicinfoMapper.selectByEntId(entId);
        return vo;
    }

    /**
     * 根据统一信用代码查询企业基本信息
     *
     * @param entUnifiedCode 统一信用代码
     * @return com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO
     * @author 杨占锐
     * @date 2024/5/28 13:56
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpEntBasicinfoService}$[86400]", key = "#root.methodName + ':' + #entUnifiedCode")
    public TpEntBasicinfoVO getBaseInfoByEntUnifiedCode(String entUnifiedCode) {
        TpEntBasicinfoVO vo = tpEntBasicinfoMapper.getBaseInfoByEntUnifiedCode(entUnifiedCode);
        return vo;
    }

    /**
     * 根据统一社会信用代码查询企业id
     *
     * @param entUnifiedCode:
     * @return java.lang.String
     * @author pand
     * @date 2021-03-03 14:27
     */
    @Override
    public String selectByEntUnifiedCode(String entUnifiedCode) {

        String entId = tpEntBasicinfoMapper.selectByEntUnifiedCode(entUnifiedCode);

        return entId;
    }


    /**
     * 判断【统一社会信用代码】是否存在
     *
     * @param entUnifiedCode  统一社会信用代码
     * @param entId           企业id，不为空时，排除当前企业
     * @return boolean        存在时返回true
     * @author 杨占锐
     * @date 2024/5/28 15:46
     */
    @Override
    public boolean existsEntUnifiedCode(String entUnifiedCode, String entId) {
        String id = tpEntBasicinfoMapper.existsEntUnifiedCode(entUnifiedCode, entId);
        // id 存在时返回true
        return StrUtil.isNotBlank(id);
    }

    /**
     * 新增企业基本信息,分两步，第一步检查统一社会信用代码是否已经存在
     * 第一步，检查统一社会信用代码是否已经存在
     * 第二步，数据入库逻辑处理
     *
     * @param vo
     * @return void
     * @author 杨攀
     * @date 2020/11/30 17:30
     */
    @Override
    public String add(TpEntBasicinfoVO vo, String jwtpid) {

        // 判断统一社会信用代码是否存在
        if (StrUtil.isNotBlank(vo.getEntUnifiedCode())) {
            boolean exists = this.existsEntUnifiedCode(vo.getEntUnifiedCode(), "");
            if (exists) {
                throw new TopinfoRuntimeException(-1, "统一社会信用代码已存在！");
            }
        }
        return this.addInfo(vo, jwtpid);
    }

    /**
     * 新增企业基本信息,分两步，第二步，数据入库逻辑处理
     * 第一步，检查统一社会信用代码是否已经存在
     * 第二步，数据入库逻辑处理
     *
     * @param vo:
     * @param jwtpid:
     * @return java.lang.String
     * @author pand
     * @date 2021-03-03 14:20
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public String addInfo(TpEntBasicinfoVO vo, String jwtpid) {

        // 企业注册，审核通过的，id会带过来
        String entid = vo.getEntId();
        if (StrUtil.isBlank(vo.getEntId())) {
            entid = SnowflakeIdUtil.nextIdStr();
        }

        TpEntBasicinfo bean = new TpEntBasicinfo();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        bean.setEntId(entid);

        String now = CommonDateUtil.now();
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
        // 经纬度计算的geohash码
        if (StrUtil.isNotBlank(bean.getLatitude()) && StrUtil.isNotBlank(bean.getLongitude())) {
            double lon = Double.valueOf(bean.getLatitude());
            double lat = Double.valueOf(bean.getLatitude());
            String geoCode = GeoUtils.encodeLatLon(lat, lon);
            bean.setGeoCode(geoCode);
        }

        try {
            tpEntBasicinfoMapper.save(bean);

            TpDeptBasicinfoVO deptBasicinfoVO = new TpDeptBasicinfoVO();
            deptBasicinfoVO.setDeptId(entid);
            deptBasicinfoVO.setPdeptId(TpConstant.NODE.TOP_NODE_ID);
            deptBasicinfoVO.setAscnId(entid);
            deptBasicinfoVO.setDeptFullName(bean.getEntFullName());
            deptBasicinfoVO.setDeptSimpleName(bean.getEntSimpleName());
            // 单位类型
            deptBasicinfoVO.setDeptType(TpConstant.DEPT_TYPE);
            // 企业类别
            deptBasicinfoVO.setCategory(TpConstant.Category.ENT);
            deptBasicinfoVO.setEnabled(TpConstant.YES);
            deptBasicinfoVO.setActived(TpConstant.YES);
            deptBasicinfoVO.setCityCode(bean.getProdAddrCode());

            // 新增部门表的单位信息
            tpDeptBasicinfoService.add(deptBasicinfoVO, jwtpid, TpConstant.Category.ENT);

            // 发布监听事件集合
            applicationContext.publishEvent(new TpEntBasicinfoEvent("企业基本信息新增同步监听", tpEntBasicinfoEventCollection, bean, OpertionTypeEnum.ADD));

            return entid;
        } catch (Exception e) {
            LOGGER.error("新增企业基本信息失败! vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增企业基本信息失败！");
        }
    }


    /**
     * 修改企业基本信息
     *
     * @param vo
     * @param jwtpid
     * @return int
     * @author 杨攀
     * @date 2020/12/1 10:24
     */
    @Override
    @CacheEvict(cacheNames = "platform.{TpEntBasicinfoService}$[86400]", allEntries = true)
    public int update(TpEntBasicinfoVO vo, String jwtpid) {
        // 判断【统一社会信用代码】是否存在
        boolean exists = this.existsEntUnifiedCode(vo.getEntUnifiedCode(), vo.getEntId());
        if (exists) {
            throw new TopinfoRuntimeException(-1, "统一社会信用代码重复");
        }
        TpEntBasicinfo bean = new TpEntBasicinfo();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        // 修改人
        bean.setUpdator(jwtpid);
        // 修改时间
        bean.setUpdateTime(CommonDateUtil.now());

        // 经纬度计算的geohash码
        if (StrUtil.isNotBlank(bean.getLatitude()) && StrUtil.isNotBlank(bean.getLongitude())) {
            double lon = Double.valueOf(bean.getLatitude());
            double lat = Double.valueOf(bean.getLatitude());
            String geoCode = GeoUtils.encodeLatLon(lat, lon);
            bean.setGeoCode(geoCode);
        }

        int count = tpEntBasicinfoMapper.updateByEntId(bean);

        // 更新部门表对应的企业名称，行政区划code
        TpDeptBasicinfoVO deptBasicinfoVO = new TpDeptBasicinfoVO();
        deptBasicinfoVO.setDeptId(vo.getEntId());
        deptBasicinfoVO.setDeptFullName(bean.getEntFullName());
        deptBasicinfoVO.setDeptSimpleName(bean.getEntSimpleName());
        deptBasicinfoVO.setCityCode(bean.getProdAddrCode());
        // 修改部门表的单位信息
        tpDeptBasicinfoService.update(deptBasicinfoVO, jwtpid);

        // 发布监听事件集合
        applicationContext.publishEvent(new TpEntBasicinfoEvent("企业基本信息新增同步监听", tpEntBasicinfoEventCollection, bean, OpertionTypeEnum.UPDATE));

        return count;
    }

    /**
     * 删除企业
     * <pre>
     *     1、企业基本信息表修改有效标记为删除<br/>
     *     2、修改该单位下的部门有效标记为删除<br/>
     *     3、修改该单位下的人员有效标记为删除<br/>
     * </pre>
     * @param entId   企业id
     * @param jwtpid  操作人员
     * @return int
     * @author 杨占锐
     * @date 2024/5/28 17:16
     */
    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    @CacheEvict(cacheNames = "platform.{TpEntBasicinfoService}$[86400]", allEntries = true)
    public int delete(String entId, String jwtpid) {

        try {
            String updateTime = CommonDateUtil.now();
            // 查询出在该单位所有部门下的所有主部门关联关系的人员id
            List<String> list = tpPersonBasicinfoMapper.selectPersonIdByAscnId(entId);
            if (list.size() >= 1) {
                // 删除主部门关联关系的人员的账号
                for (String personId: list) {
                    userAccountService.deleteByPersonId(personId);
                }
            }

            TpEntBasicinfoVO view = tpEntBasicinfoMapper.view(entId);

            // 删除企业id，删除时，唯一索引字段需要添加删除时间
            int count = tpEntBasicinfoMapper.deleteByEntId(entId, updateTime, CommonUniqueIndexUtil.addDeleteTime(view.getEntUnifiedCode()));

            // 根据企业id删除部门
            tpDeptBasicinfoService.deleteDeptByAscnId(entId, jwtpid);

            // 根据企业id删除人员
            tpPersonBasicinfoMapper.deletePersonDeptByAscnId(entId, jwtpid, updateTime);

            // 发布监听事件集合
            TpEntBasicinfo bean = new TpEntBasicinfo();
            bean.setEntId(entId);
            applicationContext.publishEvent(new TpEntBasicinfoEvent("企业基本信息删除同步监听", tpEntBasicinfoEventCollection, bean, OpertionTypeEnum.DELETE));

            return count;
        } catch (Exception e) {
            LOGGER.error("删除企业失败! entId:{}, 错误:{}", entId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除企业失败！");
        }
    }

    /**
     * 根据企业id获取企业基本信息-包含已删除的数据
     *
     * @param entId 企业id
     * @author 杨占锐
     * @date 2023/11/15 17:13
     */
    @Override
    public TpEntBasicinfoVO getBaseInfoIncludeNotActive(String entId) {
        TpEntBasicinfoVO vo = tpEntBasicinfoMapper.getBaseInfoIncludeNotActive(entId);
        // 处理统一信用代码，删除掉时间后缀
        vo.setEntUnifiedCode(CommonUniqueIndexUtil.removeDeleteTime(vo.getEntUnifiedCode()));
        return vo;
    }

    /**
     * 查询删除的企业信息
     *
     * @param query#prodAddrCode     生成经营地址行政区划编码，根据编码查询本级及下级
     * @param query#updateTimeStart  修改时间-开始  格式：yyyyMMddHHmmss   updateTimeStart <= updateTime <= updateTimeEnd
     * @param query#updateTimeEnd    修改时间-结束  格式：yyyyMMddHHmmss   updateTimeStart <= updateTime <= updateTimeEnd
     * @param query#current          当前页码，默认1
     * @param query#size             默认返回条数，默认10
     * @return java.util.List<com.jiuxi.plugin.api.bean.dto.TpEntBasicinfoDTO>
     * @author 杨占锐
     * @date 2024//5 16:13
     */
    // @Override
    // public List<TpEntBasicinfoVO> listDelete(TpEntDeleteQuery query) {
    //     query.setCurrent(Optional.ofNullable(query.getCurrent()).orElse(1));
    //     query.setSize(Optional.ofNullable(query.getSize()).orElse(10));
    //     return tpEntBasicinfoMapper.listDelete(query);
    // }

    /**
     * 根据企业id判断数据是否存在 (忽略actived字段)
     *
     * @param entId
     * @return boolean 存在返回true, 不存在返回false
     * @author 杨占锐
     * @date 2024/8/5 15:39
     */
    @Override
    public boolean existsByEntId(String entId) {
        String id = tpEntBasicinfoMapper.existsByEntId(entId);
        return StrUtil.isNotBlank(id);
    }

}
