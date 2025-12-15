package com.jiuxi.admin.core.service.api;

import cn.hutool.core.bean.BeanUtil;
import com.jiuxi.admin.core.bean.vo.TpCityVO;
import com.jiuxi.admin.core.service.TpCityService;
import com.jiuxi.common.bean.TreeNode;
// import com.jiuxi.plugin.api.bean.dto.TpCityDTO;
// import com.jiuxi.plugin.api.interfaces.TpCityApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TpCityApiServiceImpl
 * @Description: 行政区划 api
 * @Author: Ypp
 * @Date: 2023/11/15 15:02
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpCityApiService")
public class TpCityApiServiceImpl /* implements TpCityApiService */ {

    @Autowired
    private TpCityService tpCityService;

    @Autowired
    private TpApiCommonService tpApiCommonService;

    /**
     * 根据行政区划id查询行政区划
     *
     * @param cityId 行政区划id
     * @param clazz  返回的目标对象类型
     * @return T     返回对象
     * @author jiuxx
     * @date 2024/5/27 18:40
     */
    public <T> T viewById(String cityId, Class<T> clazz) {
        TpCityVO tpCityVO = tpCityService.view(cityId);

        return tpApiCommonService.copy(tpCityVO, clazz);
    }

    /**
     * 根据行政区划id查询行政区划
     *
     * @param cityId 行政区划id
     * @return T     返回对象
     * @author jiuxx
     * @date 2024/5/27 18:40
     */
    /*@Override
    public TpCityDTO viewById(String cityId) {
        return viewById(cityId, TpCityDTO.class);
    }*/

    /**
     * 根据行政区划code查询行政区划
     *
     * @param cityCode
     * @param clazz
     * @return T
     * @author Ypp
     * @date 2024/5/27 13:12
     */
    public <T> T viewByCode(String cityCode, Class<T> clazz) {

        TpCityVO vo = tpCityService.viewByCode(cityCode);

        return tpApiCommonService.copy(vo, clazz);
    }

    /**
     * 根据行政区划code查询行政区划
     *
     * @author Ypp
     * @date 2023/11/15 17:13
     * @param cityCode 行政区划code
     * @return T
     */
    /*@Override
    public TpCityDTO viewByCode(String cityCode) {
        return viewByCode(cityCode, TpCityDTO.class);
    }*/

    /**
     * 根据行政区划code查询行政区划名称.
     *
     * @param cityCode 行政区划code
     * @return java.lang.String
     * @author Ypp
     * @date 2023/11/15 10:26
     */
    public String getCityNameByCityCode(String cityCode) {
        return tpCityService.selectCityNameByCityCode(cityCode);
    }

    /**
     * 根据行政区划code查询行政区划名称(全称).
     *
     * @param cityCode 行政区划code
     * @return java.lang.String
     * @author Ypp
     * @date 2023/11/15 10:26
     */
    public String getCityFullNameByCityCode(String cityCode) {
        return tpCityService.selectCityFullNameByCityCode(cityCode);
    }

    /**
     * 根据行政区划编码批量查询行政区划
     *
     * @param cityCodes 行政区划编码集合
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpCityVO>
     * @author jiuxx
     * @date 2024/6/12 14:12
     */
    /*@Override
    public List<TpCityDTO> batchSelectByCityCode(List<String> cityCodes) {

        List<TpCityVO> tpCityVOS = tpCityService.batchSelectByCityCode(cityCodes);

        return BeanUtil.copyToList(tpCityVOS, TpCityDTO.class);
    }*/

    /**
     * 根据行政区划编码，查询本级及下级
     *
     * @param cityCode  行政区划编码，不能为空
     * @param cityLevel 行政区划，为空时查询所有
     * @return java.util.List<com.jiuxi.plugin.api.bean.dto.TpCityDTO>
     * @author jiuxx
     * @date 2024/6/24 19:37
     */
    /*@Override
    public List<TpCityDTO> listByCityCode(String cityCode, List<String> cityLevel) {
        List<TpCityVO> tpCityVOS = tpCityService.listByCityCode(cityCode, cityLevel);
        return BeanUtil.copyToList(tpCityVOS, TpCityDTO.class);
    }*/

    /**
     * 根据行政区划id，查询本级及下级树 - 同步查询
     *
     * @param cityId 行政区划id
     * @return com.jiuxi.common.bean.TreeNode
     * @author jiuxx
     * @date 2024/7/16 9:06
     */
    public TreeNode syncTree(String cityId) {
        return tpCityService.syncTree(cityId, false);
    }

    /**
     * 根据行政区划id，查询直接下级
     *
     * @param cityId
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author jiuxx
     * @date 2024/7/16 9:14
     */
    public List<TreeNode> listChildren(String cityId) {
        return tpCityService.listChildren(cityId);
    }

    /**
     * 根据行政区划id，查询本级及下级树 - 同步查询
     *
     * @param cityId  行政区划id
     * @return java.util.List<com.jiuxi.plugin.api.bean.dto.TpCityDTO>
     * @author jiuxx
     * @date 2024/6/24 19:37
     */
    /*@Override
    public List<TpCityDTO> listByCityId(String cityId) {
        List<TpCityVO> tpCityVOS = tpCityService.listByCityId(cityId);
        return BeanUtil.copyToList(tpCityVOS, TpCityDTO.class);
    }*/
}
