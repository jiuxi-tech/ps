package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpDictionaryQuery;
import com.jiuxi.admin.core.bean.vo.TpDictionaryVO;
import com.jiuxi.common.bean.TreeNode;
// import com.jiuxi.plugin.api.interfaces.TpDictionaryApiService;

import java.util.List;

/**
 * @ClassName: TpDictionaryService
 * @Description: 字典表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface TpDictionaryService {

    /**
     * 分页查询所有字典
     *
     * @param query:
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.jiuxi.admin.core.bean.vo.TpDictionaryVO>
     * @author pand
     * @date 2020-11-26 17:18
     */
    IPage<TpDictionaryVO> queryPage(TpDictionaryQuery query);

    /**
     * 下拉树查询
     *
     * @param dicCode
     * @param topFlag
     * @return
     */
    List<TreeNode> tree(String dicCode, boolean topFlag);

    /**
     * 系统管理 -> 字典管理页查询字典树，包括未启用的。
     *
     * @param dicCode
     * @param topFlag
     * @return
     */
    List<TreeNode> manageTree(String dicCode, boolean topFlag);

    /**
     * 根据pcode查询下一级
     *
     * @param code:
     * @return com.jiuxi.common.bean.SelectVO
     * @author pand
     * @date 2020-12-16 13:51
     */
    List<TreeNode> select(String code);

    /**
     * 新增字典,判断字典是否存在，已存在进行修改。
     *
     * @param vo:
     * @param pid:
     * @return int
     * @author pand
     * @date 2020-12-02 14:50
     */
    TpDictionaryVO save(TpDictionaryVO vo, String pid);

    /**
     * 查看字典
     *
     * @param dicId:
     * @return com.jiuxi.admin.core.bean.vo.TpDictionaryVO
     * @author pand
     * @date 2020-12-02 14:52
     */
    TpDictionaryVO view(String dicId);

    /**
     * 根据字典编码查看字典
     *
     * @param dicCode: 字典编码
     * @return com.jiuxi.admin.core.bean.vo.TpDictionaryVO
     * @author pand
     * @date 2020-12-02 14:52
     */
    TpDictionaryVO selectBydicCode(String dicCode);

    /**
     * 根据字典code查询字典名称
     *
     * @return int
     * @author pand
     * @date 2020-12-02 14:52
     */
    String selectDicName(String dicCode);

    /**
     * 根据字典code批量查询字典
     *
     * @param dicCodes 字典编码,逗号隔开，如：HZD12,HZD13...
     * @return
     */
    List<TpDictionaryVO> selectBydicCodes(String dicCodes);

    /**
     * 删除字典
     *
     * @param dicCodes:
     * @param pid:
     * @return int
     * @author pand
     * @date 2020-12-02 14:52
     */
    void delete(String dicCodes, String pid);

    /**
     * 根据字典编码，查询所有下级
     *
     * @param dicCode 字典编码
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author jiuxx
     * @date 2024/5/27 17:11
     */
    List<TreeNode> treeByDicCode(String dicCode, boolean returnTopNode);
}

