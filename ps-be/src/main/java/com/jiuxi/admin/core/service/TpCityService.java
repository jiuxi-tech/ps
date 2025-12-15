package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.vo.TpCityVO;
import com.jiuxi.common.bean.TreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: TpCityService
 * @Description: 行政区划表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpCityService {

    /**
     * 行政区划树查询
     *
     * @param cityId: 上级行政区划id
     * @param sync:   1：同步   0：异步
     * @param expand: 是否展开
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author pand
     * @date 2021-02-20 10:14
     */
    List<TreeNode> tree(String cityId, int sync, boolean expand, String showTop);

    /**
     * 行政区划树查询
     *
     * @param cityId:      上级行政区划id
     * @param sync:        1：同步   0：异步
     * @param expand:      是否展开
     * @param jwtCityCode: 当前登陆人所在行政区划
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author pand
     * @date 2021-02-20 10:14
     */
    List<TreeNode> treeByCode(String cityId, int sync, boolean expand, String showTop, String jwtCityCode);

    /**
     * 递归组装树
     *
     * @param cityId:
     * @param expand:
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author pand
     * @date 2021-07-02 13:25
     */
    TreeNode syncTree(String cityId, boolean expand);

    /**
     * 根据父行政区划id查询子节点,适用于只有两层结构的查询.前端用来填充select下拉选择框的.
     *
     * @param cityId: 行政区划id
     * @return java.util.List<com.jiuxi.common.bean.SelectVO>
     * @author pand
     * @date 2020-12-25 15:56
     */
    List<TreeNode> select(String cityId);

    /**
     * 查询本级及下一级行政区划
     *
     * @param cityId:
     * @return com.jiuxi.common.bean.TreeNode
     * @author pand
     * @date 2021-03-18 19:33
     */
    List<TreeNode> selectTwoLevel(String cityId);

    /**
     * 根据行政区划层级查询.
     *
     * @param cityLevel: 行政区划层级
     * @return java.util.List<com.jiuxi.common.bean.SelectVO>
     * @author pand
     * @date 2020-12-25 15:56
     */
    List<TpCityVO> selectByCityLevel(String pcityId, String cityLevel);

    /**
     * 根据行政区划code查询行政区划名称.
     *
     * @param cityCode: 行政区划code
     * @return java.util.List<com.jiuxi.common.bean.SelectVO>
     * @author pand
     * @date 2020-12-25 15:56
     */
    String selectCityNameByCityCode(String cityCode);

    /**
     * 根据行政区划code查询行政区划名称(全称).
     *
     * @param cityCode
     * @return java.lang.String
     * @author Ypp
     * @date 2022/6/27 15:52
     */
    String selectCityFullNameByCityCode(String cityCode);

    /**
     * 新增行政区划
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2020-11-26 16:37
     */
    String save(TpCityVO vo);

    /**
     * 更新行政区划
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2020-12-04 14:29
     */
    int update(TpCityVO vo);

    /**
     * 查看行政区划详情
     *
     * @param cityId:
     * @return com.jiuxi.admin.core.bean.vo.TpCityVO
     * @author pand
     * @date 2020-12-04 14:29
     */
    TpCityVO view(String cityId);

    /**
     * 删除行政区划
     *
     * @param cityId:
     * @return int
     * @author pand
     * @date 2020-12-04 14:30
     */
    int delete(String cityId);


    /**
     * 根据行政区划code查询行政区划
     * @author Ypp
     * @date 2024/5/27 13:14
     * @param cityCode
     * @return com.jiuxi.admin.core.bean.vo.TpCityVO
     */
    TpCityVO viewByCode(String cityCode);

    /**
     * 根据多个行政区划编码查询基本信息
     *
     * @param cityCodes 多个行政区划编码
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpCityVO>
     * @author jiuxx
     * @date 2024/6/12 14:12
     */
    List<TpCityVO> batchSelectByCityCode(List<String> cityCodes);

    /**
     * 根据行政区划编码，查询本级及下级
     *
     * @param cityCode  行政区划编码，不能为空
     * @param cityLevel 行政区划，为空时查询所有
     * @return java.util.List<com.jiuxi.plugin.api.bean.dto.TpCityDTO>
     * @author jiuxx
     * @date 2024/6/24 19:37
     */
    List<TpCityVO> listByCityCode(String cityCode, List<String> cityLevel);

    /**
     * 根据行政区划id，查询直接下级
     *
     * @param cityId
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author jiuxx
     * @date 2024/7/16 9:14
     */
    List<TreeNode> listChildren(String cityId);

    /**
     * 根据行政区划id, 查询本级及下级
     *
     * @param cityId  行政区划id
     * @return List<TpCityVO>
     * @author jiuxx
     * @date 2024/6/24 19:37
     */
    List<TpCityVO> listByCityId(String cityId);

}

