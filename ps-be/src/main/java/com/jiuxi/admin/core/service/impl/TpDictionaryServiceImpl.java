package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.entity.TpDictionary;
import com.jiuxi.admin.core.bean.query.TpDictionaryQuery;
import com.jiuxi.admin.core.bean.vo.TpDictionaryVO;
import com.jiuxi.admin.core.mapper.TpDictionaryMapper;
import com.jiuxi.admin.core.service.TpDictionaryService;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.CommonTreeUtil;
import com.jiuxi.common.util.CommonUniqueIndexUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
// import com.jiuxi.plugin.api.interfaces.TpDictionaryApiService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @ClassName: TpDictionaryServiceImpl
 * @Description: 字典表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service("tpDictionaryService")
public class TpDictionaryServiceImpl implements TpDictionaryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpDictionaryServiceImpl.class);

    @Autowired
    private TpDictionaryMapper tpDictionaryMapper;

    /**
     * 是否叶子节点
     */
    private static final Boolean LEAF_YES = true;
    private static final Boolean LEAF_NO = false;

    /**
     * 分页查询所有字典
     *
     * @param query:
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.jiuxi.admin.core.bean.vo.TpDictionaryVO>
     * @author pand
     * @date 2020-11-26 17:18
     */
    @Override
    public IPage<TpDictionaryVO> queryPage(TpDictionaryQuery query) {
        try {
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);
            query.setPdicId(StringUtils.isBlank(query.getPdicId()) ? TpConstant.NODE.TOP_NODE_PID : query.getPdicId());

            Page<TpDictionaryVO> page = new Page<>(pageNum, pageSize);
            IPage<TpDictionaryVO> iPage = tpDictionaryMapper.getPage(page, query);

            return iPage;
        } catch (Exception e) {
            LOGGER.error("字典列表信息失败! query:{}, 错误:{}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "字典列表信息失败！");
        }
    }

    /**
     * 根据父级字典编码，查询所有下级
     *
     * @param dicCode 字典编码
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author jiuxx
     * @date 2024/5/27 17:11
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpDictionaryService}$[86400]", key = "#root.methodName + ':' + #dicCode + ':' + #returnTopNode")
    public List<TreeNode> treeByDicCode(String dicCode, boolean returnTopNode) {
        // 转换对象
        List<TreeNode> list = tpDictionaryMapper.treeByDicCode(dicCode);
        // 构造树结构数据
        List<TreeNode> treeNodes = CommonTreeUtil.buildTree(list);
        for (TreeNode treeNode: treeNodes) {
            if (treeNode.getValue().equals(dicCode)) {
                if (returnTopNode) {
                    List<TreeNode> nodes = new ArrayList<>();
                    nodes.add(treeNode);
                    return nodes;
                } else {
                    return treeNode.getChildren();
                }
            }
        }
        return treeNodes;
    }

    /**
     * 根据dicCode查询下级字典树，本级节点同时返回。缓存半个小时
     *
     * @param dicCode:
     * @param topFlag: 是否需要返回顶级 true：返回顶级  false：不反回顶级，默认
     * @return com.jiuxi.common.bean.TreeNode
     * @author pand
     * @date 2020-12-01 20:35
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpDictionaryService}$[86400]", key = "#root.methodName + ':' + #dicCode + ':' + #topFlag")
    public List<TreeNode> tree(String dicCode, boolean topFlag) {
        try {
            // 查询跟节点
            TpDictionaryVO root = tpDictionaryMapper.selectBydicCode(dicCode);
            TreeNode rootNode = new TreeNode();
            rootNode.setId(root.getDicCode());
            rootNode.setValue(root.getDicCode());
            rootNode.setText(root.getDicName());
            rootNode.setLabel(root.getDicName());
            rootNode.setPid(root.getPdicId());
            rootNode.setExpand(false);
            rootNode.setExtend(root.getDicId());
            rootNode.setExtend01(root.getExtend01());
            rootNode.setExtend02(root.getExtend02());
            rootNode.setExtend03(root.getExtend03());

            // 查询所有下级菜单
            List<TpDictionaryVO> list = tpDictionaryMapper.tree(root.getDicCode());
            this.convertTreeNode(list, rootNode);
            if (topFlag) {
                // 返回顶级
                List rootList = new ArrayList();
                rootList.add(rootNode);
                return rootList;
            } else {
                // 不返回顶级
                return rootNode.getChildren();
            }
        } catch (Exception e) {
            LOGGER.error("查询下级字典树信息失败! dicCode:{}, topFlag:{}, 错误:{}", dicCode, topFlag, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询下级字典树信息失败！");
        }
    }

    /**
     * 系统管理 -> 字典管理页查询字典树，包括未启用的。
     *
     * @param dicCode
     * @param topFlag
     * @return
     */
    @Override
    public List<TreeNode> manageTree(String dicCode, boolean topFlag) {
        try {
            // 查询跟节点
            TpDictionaryVO root = tpDictionaryMapper.selectBydicCode(dicCode);
            TreeNode rootNode = new TreeNode();
            rootNode.setId(root.getDicCode());
            rootNode.setValue(root.getDicCode());
            rootNode.setText(root.getDicName());
            rootNode.setLabel(root.getDicName());
            rootNode.setPid(root.getPdicId());
            rootNode.setExpand(false);
            rootNode.setExtend(root.getDicId());
            rootNode.setExtend01(root.getExtend01());
            rootNode.setExtend02(root.getExtend02());
            rootNode.setExtend03(root.getExtend03());

            // 查询所有下级菜单
            List<TpDictionaryVO> list = tpDictionaryMapper.manageTree(root.getDicCode());
            this.convertTreeNode(list, rootNode);
            if (topFlag) {
                // 返回顶级
                List rootList = new ArrayList();
                rootList.add(rootNode);
                return rootList;
            } else {
                // 不返回顶级
                return rootNode.getChildren();
            }
        } catch (Exception e) {
            LOGGER.error("查询下级字典树信息失败! dicCode:{}, topFlag:{}, 错误:{}", dicCode, topFlag, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询下级字典树信息失败！");
        }
    }

    /**
     * 根据pcode查询下一级
     *
     * @param dicCode:
     * @return com.jiuxi.common.bean.SelectVO
     * @author pand
     * @date 2020-12-16 13:51
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpDictionaryService}$[86400]", key = "#root.methodName + ':' + #dicCode")
    public List<TreeNode> select(String dicCode) {
        try {
            List<TreeNode> list = tpDictionaryMapper.select(dicCode);
            // 循环list，判断是否叶子节点
            for (TreeNode treeNode : list) {
                int count = tpDictionaryMapper.childrenCount(treeNode.getId());
                if (count >= 1){
                    treeNode.setLeaf(LEAF_NO);
                }else{
                    treeNode.setLeaf(LEAF_YES);
                }
            }

            return list;
        } catch (Exception e) {
            LOGGER.error("查询下级字典树信息失败! dicCode:{}, 错误:{}", dicCode, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询下级字典信息失败！");
        }
    }

    /**
     * 递归查询字典树
     *
     * @param list:
     * @param rootNode:
     * @return void
     * @author pand
     * @date 2020-12-02 14:49
     */
    private void convertTreeNode(List<TpDictionaryVO> list, TreeNode rootNode) {
        for (TpDictionaryVO vo : list) {
            if (StrUtil.equals(vo.getPdicId(), rootNode.getExtend())) {
                TreeNode treeNode = new TreeNode();
                treeNode.setId(vo.getDicCode());
                treeNode.setValue(vo.getDicCode());
                treeNode.setText(vo.getDicName());
                treeNode.setLabel(vo.getDicName());
                treeNode.setPid(vo.getPdicId());
                treeNode.setExpand(false);
                treeNode.setExtend(vo.getDicId());
                treeNode.setExtend01(vo.getExtend01());
                treeNode.setExtend02(vo.getExtend02());
                treeNode.setExtend03(vo.getExtend03());
                rootNode.addChildren(treeNode);
                convertTreeNode(list, treeNode);
            }
        }
    }

    /**
     * 新增字典,判断字典是否存在，已存在进行修改。
     *
     * @param vo:
     * @param pid:
     * @return int
     * @author pand
     * @date 2020-12-02 14:50
     */
    @Override
    @CacheEvict(cacheNames = "platform.{TpDictionaryService}$[86400]", allEntries = true)
    public TpDictionaryVO save(TpDictionaryVO vo, String pid) {
        try {

            int count = tpDictionaryMapper.count(vo.getDicId());
            TpDictionary bean = new TpDictionary();

            vo.setDicId(StrUtil.isBlank(vo.getDicId()) ? SnowflakeIdUtil.nextIdStr() : vo.getDicId());
            // 转换成数据库对象
            BeanUtil.copyProperties(vo, bean);
            if (count == 0) {
                // 根据dicCode查询，如果已经存在，提示字典code已存在
                TpDictionaryVO dictVO = tpDictionaryMapper.selectBydicCode(bean.getDicCode());
                if (null != dictVO) {
                    throw new TopinfoRuntimeException(-1, "字典code已存在！");
                }
                // 新增
                bean.setPdicId(Optional.ofNullable(vo.getPdicId()).orElse("-1"));

                String now = CommonDateUtil.now();
                bean.setCreateor(pid);
                bean.setCreateTime(now);
                // 修改人
                bean.setUpdateor(pid);
                // 修改时间
                bean.setUpdateTime(now);
                bean.setActived(1);
                bean.setEnabled(1);
                tpDictionaryMapper.save(bean);
            } else {
                // 根据dicId和dicCode查询，如果存在dicId不同，dicCode相同的字典，提示字典code已存在，
                TpDictionaryVO dictVO = tpDictionaryMapper.selectBydicIdAndCode(bean.getDicId(), bean.getDicCode());
                if (null != dictVO) {
                    throw new TopinfoRuntimeException(-1, "字典code已存在！");
                }
                // 修改
                bean.setUpdateor(pid);
                bean.setUpdateTime(CommonDateUtil.now());

                tpDictionaryMapper.update(bean);
            }

            return vo;
        } catch (Exception e) {
            LOGGER.error("新增字典失败! vo:{}, 错误:{}", JSONObject.toJSONString(vo), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, StrUtil.isBlank(e.getMessage()) ? "新增字典失败！" : e.getMessage());
        }
    }

    /**
     * 查看字典
     *
     * @param dicId:
     * @return com.jiuxi.admin.core.bean.vo.TpDictionaryVO
     * @author pand
     * @date 2020-12-02 14:52
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpDictionaryService}$[86400]", key = "#root.methodName + ':' + #dicId")
    public TpDictionaryVO view(String dicId) {
        try {
            TpDictionaryVO vo = tpDictionaryMapper.view(dicId);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看字典值失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看字典值失败！");
        }
    }

    /**
     * 根据字典编码查看字典
     *
     * @param dicCode: 字典编码
     * @return com.jiuxi.admin.core.bean.vo.TpDictionaryVO
     * @author pand
     * @date 2020-12-02 14:52
     */
    @Override
    public TpDictionaryVO selectBydicCode(String dicCode) {
        try {
            TpDictionaryVO vo = tpDictionaryMapper.selectBydicCode(dicCode);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看字典值失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看字典值失败！");
        }
    }

    /**
     * 根据字典code查询字典名称
     *
     * @return int
     * @author pand
     * @date 2020-12-02 14:52
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpDictionaryService}$[86400]", key = "#root.methodName + ':' + #dicCode")
    public String selectDicName(String dicCode) {
        try {
            String dicName = tpDictionaryMapper.selectDicName(dicCode);
            return dicName;
        } catch (Exception e) {
            LOGGER.error("查询字典名称失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查询字典名称失败！");
        }
    }

    /**
     * 根据字典code批量查询字典
     *
     * @param dicCodes 字典编码,逗号隔开，如：HZD12,HZD13...
     * @return
     */
    @Override
    @Cacheable(cacheNames = "platform.{TpDictionaryService}$[86400]", key = "#root.methodName + ':' + #dicCodes")
    public List<TpDictionaryVO> selectBydicCodes(String dicCodes) {
        try {

            List<String> dicCodeList = StrUtil.splitTrim(dicCodes, ",");

            if (CollUtil.isEmpty(dicCodeList)) {
                return new ArrayList<>(0);
            }

            Set<String> dicCodeSet = new HashSet<>();
            dicCodeSet.addAll(dicCodeList);
            List<TpDictionaryVO> list = tpDictionaryMapper.selectBydicCodes(dicCodeSet);
            return list;
        } catch (Exception e) {
            LOGGER.error("批量查询字典失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "批量查询字典失败！");
        }
    }

    /**
     * 删除字典
     *
     * @param dicCodes:
     * @param pid:
     * @return int
     * @author pand
     * @date 2020-12-02 14:52
     */
    @Override
    @CacheEvict(cacheNames = "platform.{TpDictionaryService}$[86400]", allEntries = true)
    public void delete(String dicCodes, String pid) {
        try {
            if (StrUtil.isBlank(dicCodes)) {
                throw new TopinfoRuntimeException(-1, "字典code不能为空！");
            }
            String[] dicCodeArr = StringUtils.split(dicCodes, ",");
            String updateTime = CommonDateUtil.now();
            for (String dicCode : dicCodeArr) {
                tpDictionaryMapper.deleteBydicCode(pid, updateTime, dicCode);
            }
        } catch (Exception e) {
            LOGGER.error("删除字典值失败！{}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, Optional.ofNullable(e.getMessage()).orElse("删除字典值失败！"));
        }
    }

}
