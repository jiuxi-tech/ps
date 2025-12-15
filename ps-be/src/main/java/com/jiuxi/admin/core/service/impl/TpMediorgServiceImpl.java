package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpMediorg;
import com.jiuxi.admin.core.bean.query.TpMediorgQuery;
import com.jiuxi.admin.core.bean.vo.TpDeptBasicinfoVO;
import com.jiuxi.admin.core.bean.vo.TpMediorgVO;
import com.jiuxi.admin.core.mapper.TpMediorgMapper;
import com.jiuxi.module.user.infra.persistence.mapper.UserPersonMapper;
import com.jiuxi.module.user.app.service.UserAccountService;
import com.jiuxi.admin.core.service.TpAccountService;
import com.jiuxi.admin.core.mapper.TpPersonBasicinfoMapper;
import com.jiuxi.admin.core.service.TpDeptBasicinfoService;
import com.jiuxi.admin.core.service.TpMediorgService;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.geo.GeoUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName: TpMediorgServiceImpl
 * @Description: 中介表
 * @Author pand
 * @Date 2021-05-25 17:32:41
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpMediorgService")
public class TpMediorgServiceImpl implements TpMediorgService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpMediorgServiceImpl.class);

    @Autowired
    private TpMediorgMapper tpMediorgMapper;

    @Autowired
    private TpDeptBasicinfoService tpDeptBasicinfoService;

    @Autowired
    private TpAccountService tpAccountService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private TpPersonBasicinfoMapper tpPersonBasicinfoMapper;

    @Override
    public IPage<TpMediorgVO> queryPage(TpMediorgQuery query) {

        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);

            Page<TpMediorgVO> page = new Page<>(pageNum, pageSize);
            IPage<TpMediorgVO> iPage = tpMediorgMapper.getPage(page, query);

            return iPage;
        } catch (Exception e) {
            LOGGER.error("列表信息查询失败! query:{}, 错误:{}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "列表信息查询失败！");
        }

    }


    /**
     * 根据统一社会信用代码查询企业id
     *
     * @param mediorgUnifiedCode:
     * @return java.lang.String
     * @author pand
     * @date 2021-03-03 14:27
     */
    @Override
    public String selectByEntUnifiedCode(String mediorgUnifiedCode) {
        String mediorgId = null;
        TpMediorg tpMediorg = tpMediorgMapper.selectByMediorgUnifiedCode(mediorgUnifiedCode);
        if (null != tpMediorg) {
            mediorgId = tpMediorg.getMediorgId();
        }

        return mediorgId;
    }

    @Override
    public String add(TpMediorgVO vo, String jwtpid) {

        // 判断统一社会信用代码是否存在
        if (StrUtil.isNotBlank(vo.getMediorgUnifiedCode())) {
            String mediorgId = this.selectByEntUnifiedCode(vo.getMediorgUnifiedCode());
            if (StrUtil.isNotBlank(mediorgId)) {
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
    public String addInfo(TpMediorgVO vo, String jwtpid) {

        // 企业注册，审核通过的，id会带过来
        String mediorgId = vo.getMediorgId();
        if (StrUtil.isBlank(vo.getMediorgId())) {
            mediorgId = SnowflakeIdUtil.nextIdStr();
        }

        TpMediorg bean = new TpMediorg();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        bean.setMediorgId(mediorgId);

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
        bean.setEnabled(TpConstant.YES);
        // 经纬度计算的geohash码
        if (StrUtil.isNotBlank(bean.getLatitude()) && StrUtil.isNotBlank(bean.getLongitude())) {
            double lon = Double.valueOf(bean.getLatitude());
            double lat = Double.valueOf(bean.getLatitude());
            String geoCode = GeoUtils.encodeLatLon(lat, lon);
            bean.setGeoCode(geoCode);
        }

        try {
            tpMediorgMapper.add(bean);

            TpDeptBasicinfoVO deptBasicinfoVO = new TpDeptBasicinfoVO();
            deptBasicinfoVO.setDeptId(mediorgId);
            deptBasicinfoVO.setPdeptId(TpConstant.NODE.TOP_NODE_ID);
            deptBasicinfoVO.setAscnId(mediorgId);
            deptBasicinfoVO.setDeptFullName(bean.getMediorgFullName());
            deptBasicinfoVO.setDeptSimpleName(bean.getMediorgSimpleName());
            // 单位类型
            deptBasicinfoVO.setDeptType("SYS0501");
            // 企业类别
            deptBasicinfoVO.setCategory(TpConstant.Category.MED);
            deptBasicinfoVO.setEnabled(TpConstant.YES);
            deptBasicinfoVO.setActived(TpConstant.YES);

            // 新增部门表的单位信息
            tpDeptBasicinfoService.add(deptBasicinfoVO, jwtpid, TpConstant.Category.MED);

            return mediorgId;
        } catch (Exception e) {
            LOGGER.error("新增中介基本信息失败! vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增中介基本信息失败！");
        }

    }

    @Override
    public int update(TpMediorgVO vo, String jwtpid) {

        TpMediorg bean = new TpMediorg();
        // 转换成数据库对象
        BeanUtil.copyProperties(vo, bean);
        bean.setUpdator(jwtpid);
        bean.setUpdateTime(CommonDateUtil.now());
        // TODO

        try {
            int count = tpMediorgMapper.update(bean);
            return count;
        } catch (Exception e) {
            LOGGER.error("修改中介基本信息失败! vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "修改失败！");
        }
    }

    @Override
    public TpMediorgVO view(String id) {

        try {
            TpMediorgVO vo = tpMediorgMapper.view(id);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看中介基本信息失败! id:{}, 错误:{}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看失败！");
        }
    }

    @Override
    @Transactional(rollbackFor = TopinfoRuntimeException.class)
    public int deleteById(String id) {

        try {
            // 查询出在该单位所有部门下的所有主部门关联关系的人员id
            List<String> list = tpMediorgMapper.selectPersonIdByAscnId(id);
            if (list.size() >= 1) {
                // 删除主部门关联关系的人员的账号
                for (String personId : list) {

                    // 删除时，唯一索引字段需要添加删除时间
                    userAccountService.deleteByPersonId(personId);
                }
            }

            // 删除企业id
            int count = tpMediorgMapper.delete(id);
            // 根据企业id删除部门
            tpDeptBasicinfoService.deleteDeptByAscnId(id, null);

            // 根据企业id删除人员
            tpPersonBasicinfoMapper.deletePersonDeptByAscnId(id, "system", CommonDateUtil.now());

            return count;
        } catch (Exception e) {
            LOGGER.error("删除中介基本信息失败! id:{}, 错误:{}", id, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除中介基本信息失败！");
        }
    }

}
