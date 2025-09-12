package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpCity;
import com.jiuxi.admin.core.bean.vo.TpCityVO;
import com.jiuxi.admin.core.event.TpCityEvent;
import com.jiuxi.admin.core.listener.service.TpCityEventService;
import com.jiuxi.admin.core.mapper.TpCityMapper;
import com.jiuxi.admin.core.service.TpCityService;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.CommonUniqueIndexUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
// import com.jiuxi.plugin.api.interfaces.TpCityApiService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: TpCityServiceImpl
 * @Description: 行政区划表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("tpCityService")
public class TpCityServiceImpl implements TpCityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpCityServiceImpl.class);

    @Autowired
    private TpCityMapper tpCityMapper;

    /**
     * 上下文对象
     */
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private TpCityEventService tpCityEventService;

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
    @Override
    public List<TreeNode> tree(String cityId, int sync, boolean expand, String showTop) {
        TreeNode topNode;
        // 菜单树
        if (1 == sync) {
            // 同步
            topNode = this.syncTree(cityId, expand);
            if (StrUtil.equals(showTop, "off")) {
                return topNode.getChildren();
            } else {
                List<TreeNode> list = new ArrayList<>();
                list.add(topNode);
                return list;
            }
        } else {
            // 异步
            // 先查询根节点，正常情况下，树只有一个根节点，所以根据 cityId：-1 查询只有一条数据
            List<TreeNode> topList = tpCityMapper.selectTwoLevel(cityId);

            if (StrUtil.equals(showTop, "off")) {
                // 不返回根节点，即查询根节点的下级节点，正常情况下只有一个根节点,如果有多个根节点，必须连带根节点一起返回，否则数据容易混乱。
                // 如：浙江省，江苏省为根节点，不返回根节点的话，直接返回杭州市，宁波市，苏州市。。。，数据很乱。
                List<TreeNode> list = new ArrayList<>();
                topList.forEach(item -> {
                    List<TreeNode> children = tpCityMapper.selectTwoLevel(item.getId());
                    // 判断是否是叶子结点
                    children.forEach(iitem-> {
                        List<TreeNode> ichildren = iitem.getChildren();
                        if (null != ichildren && !ichildren.isEmpty()) {
                            iitem.setLeaf(false);
                        }else{
                            iitem.setLeaf(true);
                        }
                        iitem.cleanChildren();
                    });
                    list.addAll(children);
                });

                return list;
            } else {
                // 判断是否是叶子结点
                topList.forEach(item -> {
                    List<TreeNode> children = item.getChildren();
                    if (null != children && !children.isEmpty()) {
                        item.setLeaf(false);
                    }else{
                        item.setLeaf(true);
                    }
                    item.cleanChildren();
                });
                return topList;
            }
        }
    }

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
    @Override
    @Cacheable(cacheNames = "platform.{TpCityService}$[86400]", key = "#root.methodName + ':' + #cityId + ':' + #sync + ':' + #expand + ':' + #showTop + ':' + #jwtCityCode")
    public List<TreeNode> treeByCode(String cityId, int sync, boolean expand, String showTop, String jwtCityCode) {
        List<TreeNode> list;
        if (StrUtil.isBlank(cityId)) {
            // 如果当前登陆人没有根据行政区划pid查询，默认查询本行政区划的数据，根据当前登陆人所在行政区划code查询行政区划pid
            cityId = tpCityMapper.selectPcityIdByCityCode(jwtCityCode);
            if (StrUtil.isBlank(cityId)) {
                throw new TopinfoRuntimeException(-1, "查询不到行政区划数据！");
            }
            list = this.tree(cityId, sync, expand, showTop);
            // 由于向上查了一层，所以返回的时候，只需要返回与当前登陆人所在行政区划相同的即可
            list = list.stream().filter(item -> StrUtil.equals(item.getValue(), jwtCityCode)).collect(Collectors.toList());
        } else {
            String cityCode = tpCityMapper.selectCityCodeByCityId(cityId);
            if (!cityCode.startsWith(jwtCityCode)) {
                throw new TopinfoRuntimeException(-1, "不能查询非本行政区划之外的数据！");
            }
            list = this.tree(cityId, sync, expand, showTop);
        }

        return list;
    }

    /**
     * 同步获取数据，故一次性读取所有的行政区划
     *
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author 杨攀
     * @date 2020/11/20 14:44
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpCityService}$[86400]", key = "#root.methodName + ':' + #cityId + ':' + #expand")
    public TreeNode syncTree(String cityId, boolean expand) {
        // 如果cityId = -1，表示查询全部的行政区划。
        String cityCode = "";
        String pcityId;
        if (!StrUtil.equals(cityId, TpConstant.NODE.TOP_NODE_PID)) {
            // 非顶级节点，根据pcityId查询出所有的子类cityCode
            cityCode = tpCityMapper.selectCityCodeByCityId(cityId);
            // 非顶级节点查询，查询节点，即为顶级节点，如查杭州市本级及下级，杭州市3301为顶级，杭州市的本级及下级，根据pcityId判断即pcityId == 33的为顶级节点。根据pcityId判断顶级节点，是为了兼容查询全部，即pcityId=-1的情况。
            TpCityVO tpCityVO = tpCityMapper.view(cityId);
            pcityId = tpCityVO.getPcityId();
        } else {
            // 查询顶级节点，即查询pcityId = -1的节点，如全国及下级，全国为顶级。
            pcityId = cityId;
        }
        // 查询本级及下级
        List<TreeNode> list = tpCityMapper.selectAllByCityCode(cityCode);
        // 按pid分组
        Map<String, List<TreeNode>> map = list.stream().collect(Collectors.groupingBy(TreeNode::getPid, Collectors.toList()));
        // 组装树结构，避免使用递归
        TreeNode topNode = new TreeNode();
        for (TreeNode treeNode : list) {
            List<TreeNode> children = map.get(treeNode.getId());
            if (null != children && !children.isEmpty()) {
                treeNode.setLeaf(false);
                treeNode.addChildrens(children);
            } else {
                treeNode.setLeaf(true);
            }
            // 根据pcityId判断是否顶级节点，如全国为-1，杭州的pcityId为33
            if (StrUtil.equals(treeNode.getPid(), pcityId)) {
                topNode = treeNode;
            }
        }

        return topNode;
    }


    /**
     * 根据父行政区划id查询子节点,适用于只有两层结构的查询.前端用来填充select下拉选择框的.
     *
     * @param cityId: 行政区划id
     * @return java.util.List<com.jiuxi.common.bean.SelectVO>
     * @author pand
     * @date 2020-12-25 15:56
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpCityService}$[86400]", key = "#root.methodName + ':' + #cityId")
    public List<TreeNode> select(String cityId) {
        try {
            List<TreeNode> list = tpCityMapper.select(cityId);

            return list;
        } catch (Exception e) {
            LOGGER.error("查询下级行政区划信息失败！cityId:{}, 错误：{}", cityId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询下级行政区划信息失败！");
        }
    }

    /**
     * 查询本级及下一级行政区划
     *
     * @param cityId:
     * @return com.jiuxi.common.bean.TreeNode
     * @author pand
     * @date 2021-03-18 19:33
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpCityService}$[86400]", key = "#root.methodName + ':' + #cityId")
    public List<TreeNode> selectTwoLevel(String cityId) {

        try {
            List<TreeNode> list = tpCityMapper.selectTwoLevel(cityId);

            return list;
        } catch (Exception e) {
            LOGGER.error("查询下级行政区划信息失败！cityId:{}, 错误：{}", cityId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询下级行政区划信息失败！");
        }
    }

    /**
     * 根据行政区划层级查询.
     *
     * @param cityLevel: 行政区划层级
     * @return java.util.List<com.jiuxi.common.bean.SelectVO>
     * @author pand
     * @date 2020-12-25 15:56
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpCityService}$[86400]", key = "#root.methodName + ':' + #pcityId + ':' + #cityLevel")
    public List<TpCityVO> selectByCityLevel(String pcityId, String cityLevel) {
        try {
            List<TpCityVO> list = tpCityMapper.selectByCityLevel(pcityId, cityLevel);

            return list;
        } catch (Exception e) {
            LOGGER.error("查询行政区划信息失败！pcityId:{}, cityLevel:{}, 错误:{}", pcityId, cityLevel, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询行政区划信息失败！");
        }
    }

    /**
     * 根据行政区划code查询行政区划名称.
     *
     * @param cityCode: 行政区划code
     * @return java.util.List<com.jiuxi.common.bean.SelectVO>
     * @author pand
     * @date 2020-12-25 15:56
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpCityService}$[86400]", key = "#root.methodName + ':' + #cityCode")
    public String selectCityNameByCityCode(String cityCode) {
        try {
            String cityName = tpCityMapper.selectCityNameByCityCode(cityCode);
            return cityName;
        } catch (Exception e) {
            LOGGER.error("根据code查询地方名称失败！cityCode:{}, 错误：{}", cityCode, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询行政区划名称失败！");
        }
    }

    /**
     * 根据行政区划code查询行政区划名称(全称).
     *
     * @param cityCode
     * @return java.lang.String
     * @author 杨攀
     * @date 2022/6/27 15:52
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpCityService}$[86400]", key = "#root.methodName + ':' + #cityCode")
    public String selectCityFullNameByCityCode(String cityCode) {
        try {
            String cityName = tpCityMapper.selectCityFullNameByCityCode(cityCode);
            return cityName;
        } catch (Exception e) {
            LOGGER.error("根据code查询地方名称失败！cityCode:{}, 错误：{}", cityCode, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询行政区划名称失败！");
        }
    }

    /**
     * 新增行政区划
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2020-11-26 16:37
     */
    @Override
    @CacheEvict(cacheNames = "platform.{TpCityService}$[86400]", allEntries = true)
    public String save(TpCityVO vo) {
        // 判断cityCode是否已经存在
        String oldCityId = tpCityMapper.selectCityIdByCityCode(vo.getCityCode());
        if (StrUtil.isNotBlank(oldCityId)) {
            throw new TopinfoRuntimeException(-1, "code已存在！");
        }
        try {
            TpCity bean = new TpCity();
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            String cityId = bean.getCityId();

            if (StrUtil.isBlank(cityId)) {
                cityId = SnowflakeIdUtil.nextIdStr();
                bean.setCityId(cityId);
            }
            String now = CommonDateUtil.now();
            bean.setCreateTime(now);
            // 修改时间
            bean.setUpdateTime(now);
            tpCityMapper.save(bean);

            // 发布事件，推送账号给第三方系统
            if (null != tpCityEventService) {
                applicationContext.publishEvent(new TpCityEvent("行政区划信息新增同步监听", tpCityEventService, bean, 1));
            }

            return cityId;
        } catch (Exception e) {
            LOGGER.error("新增行政区划失败！vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "新增行政区划失败！");
        }
    }

    /**
     * 更新行政区划
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2020-12-04 14:29
     */
    @Override
    @CacheEvict(cacheNames = "platform.{TpCityService}$[86400]", allEntries = true)
    public int update(TpCityVO vo) {
        // 判断cityCode是否已经存在
        String cityId = tpCityMapper.selectByIdAndCityCode(vo.getCityId(), vo.getCityCode());
        if (StrUtil.isNotBlank(cityId)) {
            throw new TopinfoRuntimeException(-1, "code已存在！");
        }
        try {
            TpCity bean = new TpCity();
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            bean.setUpdateTime(CommonDateUtil.now());

            int count = tpCityMapper.update(bean);

            // 发布事件，推送账号给第三方系统
            if (null != tpCityEventService) {
                applicationContext.publishEvent(new TpCityEvent("账号信息新增同步监听", tpCityEventService, bean, 2));
            }

            return count;
        } catch (Exception e) {
            LOGGER.error("行政区划修改失败！vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "行政区划失败！");
        }
    }

    /**
     * 查看行政区划详情
     *
     * @param cityId:
     * @return com.jiuxi.admin.core.bean.vo.TpCityVO
     * @author pand
     * @date 2020-12-04 14:29
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpCityService}$[86400]", key = "#root.methodName + ':' + #cityId")
    public TpCityVO view(String cityId) {
        try {
            TpCityVO vo = tpCityMapper.view(cityId);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看行政区划失败！cityId:{}, 错误:{}", cityId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看行政区划失败！");
        }
    }

    /**
     * 删除行政区划
     *
     * @param cityId:
     * @return int
     * @author pand
     * @date 2020-12-04 14:30
     */
    @Override
    @CacheEvict(cacheNames = "platform.{TpCityService}$[86400]", allEntries = true)
    public int delete(String cityId) {

        try {

            TpCityVO view = tpCityMapper.view(cityId);

            TpCity bean = new TpCity();
            bean.setCityId(cityId);
            bean.setActived(0);
            bean.setUpdateTime(CommonDateUtil.now());
            // 删除时，唯一索引字段需要添加删除时间
            bean.setCityCode(CommonUniqueIndexUtil.addDeleteTime(view.getCityCode()));
            int count = tpCityMapper.deleteBycityId(bean);
            // 发布事件，推送账号给第三方系统
            if (null != tpCityEventService) {
                applicationContext.publishEvent(new TpCityEvent("账号信息新增同步监听", tpCityEventService, bean, 3));
            }
            return count;
        } catch (Exception e) {
            LOGGER.error("删除行政区划失败！cityId:{}, 错误:{}", cityId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "删除行政区划失败！");
        }
    }


    /**
     * 根据行政区划code查询行政区划
     * @author 杨攀
     * @date 2024/5/27 13:15
     * @param cityCode
     * @return com.jiuxi.admin.core.bean.vo.TpCityVO
     */
    @Cacheable(cacheNames = "platform.{TpCityService}$[86400]", key = "#root.methodName + ':' + #cityCode")
    @Override
    public TpCityVO viewByCode(String cityCode) {
        try {
            TpCityVO vo = tpCityMapper.viewByCode(cityCode);
            return vo;
        } catch (Exception e) {
            LOGGER.error("根据行政区划code查询行政区划失败！行政区划编码:{}, 错误：{}", cityCode, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "根据行政区划code查询行政区划失败！");
        }
    }

    /**
     * 根据多个行政区划编码查询基本信息
     *
     * @param cityCodes 多个行政区划编码
     * @return java.util.List<com.jiuxi.admin.core.bean.vo.TpCityVO>
     * @author 杨占锐
     * @date 2024/6/12 14:12
     */
    @Override
    public List<TpCityVO> batchSelectByCityCode(List<String> cityCodes) {
        if (CollectionUtil.isEmpty(cityCodes)) {
            return new ArrayList<>();
        }
        return tpCityMapper.batchSelectByCityCode(cityCodes);
    }

    /**
     * 根据行政区划编码，查询本级及下级
     *
     * @param cityCode  行政区划编码，不能为空
     * @param cityLevel 行政区划，为空时查询所有
     * @return java.util.List<com.jiuxi.plugin.api.bean.dto.TpCityDTO>
     * @author 杨占锐
     * @date 2024/6/24 19:37
     */
    @Override
    public List<TpCityVO> listByCityCode(String cityCode, List<String> cityLevel) {
        return tpCityMapper.listByCityCode(cityCode, cityLevel);
    }

    /**
     * 根据行政区划id，查询直接下级
     *
     * @param cityId
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author 杨占锐
     * @date 2024/7/16 9:14
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpCityService}$[86400]", key = "#root.methodName + ':' + #cityId")
    public List<TreeNode> listChildren(String cityId) {
        return tpCityMapper.selectChildren(cityId);
    }

    /**
     * 根据行政区划id, 查询本级及下级
     *
     * @param cityId  行政区划id
     * @return List<TpCityVO>
     * @author 杨占锐
     * @date 2024/6/24 19:37
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpCityService}$[86400]", key = "#root.methodName + ':' + #cityId")
    public List<TpCityVO> listByCityId(String cityId) {
        return tpCityMapper.listByCityId(cityId);
    }
}
