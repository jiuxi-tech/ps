package com.jiuxi.admin.core.mapper;

import com.jiuxi.admin.core.bean.entity.TpCity;
import com.jiuxi.admin.core.bean.vo.TpCityVO;
import com.jiuxi.common.bean.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpCityMapper
 * @Description: 行政区划表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Mapper
public interface TpCityMapper {

    List<TreeNode> selectAllByCityCode(@Param("cityCode") String cityCode);

    List<TpCityVO> selectByCityLevel(@Param("pcityId") String pcityId, @Param("cityLevel") String cityLevel);

    List<TreeNode> select(String cityId);

    List<TpCityVO> batchSelectByCityCode(@Param("cityCodes") List<String> cityCodes);

    String selectCityCodeByCityId(String cityId);

    /**
     * 根据行政区划code查询行政区划id
     *
     * @param cityCode
     * @return
     */
    String selectCityIdByCityCode(String cityCode);

    /**
     * 根据行政区划code查询行政区划pid
     *
     * @param cityCode
     * @return
     */
    String selectPcityIdByCityCode(String cityCode);

    /**
     * 查询cityId != cityId 并且cityCode == cityCode的数据
     *
     * @param cityId
     * @param cityCode
     * @return java.lang.String
     * @author pand
     * @date 2022-06-07 14:49
     */
    String selectByIdAndCityCode(@Param("cityId") String cityId, @Param("cityCode") String cityCode);

    /**
     * 根据行政区划code查询行政区划名称
     *
     * @param cityCode
     * @return
     */
    String selectCityNameByCityCode(String cityCode);

    /**
     * 根据行政区划code查询行政区划全名称
     *
     * @param cityCode
     * @return
     */
    String selectCityFullNameByCityCode(String cityCode);

    /**
     * 根据行政区划pid查询下级
     *
     * @param pcityId
     * @return
     */
    List<TreeNode> selectChildren(@Param("pcityId") String pcityId);

    /**
     * 根据行政区划pid查询两层下级
     *
     * @param pcityId
     * @return
     */
    List<TreeNode> selectTwoLevel(@Param("pcityId") String pcityId);

    int save(TpCity bean);

    int update(TpCity bean);

    TpCityVO view(String cityId);

    int deleteBycityId(TpCity bean);

    /**
     * 根据行政区划code查询行政区划
     * @author 杨攀
     * @date 2024/5/27 13:20
     * @param cityCode
     * @return com.jiuxi.admin.core.bean.vo.TpCityVO
     */
    TpCityVO viewByCode(@Param("cityCode") String cityCode);

    /**
     * 根据行政区划编码，查询本级及下级
     *
     * @param cityCode  行政区划编码，不能为空
     * @param cityLevel 行政区划，为空时查询所有
     * @return java.util.List<com.jiuxi.plugin.api.bean.dto.TpCityDTO>
     * @author 杨占锐
     * @date 2024/6/24 19:37
     */
    List<TpCityVO> listByCityCode(@Param("cityCode") String cityCode, @Param("cityLevel") List<String> cityLevel);

    /**
     * 根据行政区划id, 查询本级及下级
     *
     * @param cityId  行政区划id
     * @return List<TpCityVO>
     * @author 杨占锐
     * @date 2024/6/24 19:37
     */
    List<TpCityVO> listByCityId(@Param("cityId") String cityId);
}
