package com.jiuxi.admin.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpDictionary;
import com.jiuxi.admin.core.bean.query.TpDictionaryQuery;
import com.jiuxi.admin.core.bean.vo.TpDictionaryVO;
import com.jiuxi.common.bean.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: TpDictionaryMapper
 * @Description: 字典表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Mapper
public interface TpDictionaryMapper {

    IPage<TpDictionaryVO> getPage(Page<TpDictionaryVO> page, @Param("query") TpDictionaryQuery query);

    List<TpDictionaryVO> tree(@Param("dicCode") String dicCode);

    List<TpDictionaryVO> manageTree(@Param("dicCode") String dicCode);

    List<TreeNode> select(String dicCode);

    /**
     * 根据字典编码查询字典
     *
     * @param dicCode 字典编码
     * @return
     */
    TpDictionaryVO selectBydicCode(String dicCode);

    /**
     * 根据字典code批量查询字典
     *
     * @param dicCodes 字典编码
     * @return
     */
    List<TpDictionaryVO> selectBydicCodes(@Param("dicCodes") Set<String> dicCodes);

    TpDictionaryVO selectBydicIdAndCode(@Param("dicId") String dicId, @Param("dicCode") String dicCode);

    int save(TpDictionary bean);

    int count(String dicId);

    /**
     * 根据父id查询子类字典
     *
     * @param pdicId 父级id
     * @return
     */
    int childrenCount(String pdicId);

    int update(TpDictionary bean);

    TpDictionaryVO view(String dicId);

    String selectDicName(String dicCode);

    List<TpDictionary> selectTestAll();

    int deleteBydicCode(@Param("updateor") String updateor, @Param("updateTime") String updateTime, @Param("dicCode") String dicCode);

    /**
     * 根据父级字典编码，查询所有下级
     *
     * @param dicCode 字典编码
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author jiuxx
     * @date 2024/5/27 17:11
     */
    List<TreeNode> treeByDicCode(@Param("dicCode") String dicCode);
}
