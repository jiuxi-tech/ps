package com.jiuxi.admin.core.service.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.jiuxi.admin.core.bean.vo.TpDictionaryVO;
import com.jiuxi.admin.core.service.TpDictionaryService;
import com.jiuxi.common.bean.TreeNode;
// import com.jiuxi.plugin.api.bean.dto.TpDictionaryDTO;
// import com.jiuxi.plugin.api.interfaces.TpDictionaryApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: TpDictionaryApiServiceImpl
 * @Description: 字典 api
 * @Author: 杨攀
 * @Date: 2023/11/15 15:03
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 */
@Service("tpDictionaryApiService")
public class TpDictionaryApiServiceImpl /* implements TpDictionaryApiService */ {

    @Autowired
    private TpDictionaryService tpDictionaryService;

    /**
     * 根据字典code查询字典名称
     *
     * @param dicCode
     * @return java.lang.String
     * @author 杨攀
     * @date 2023/11/15 10:13
     */
    public String getDicNameByCode(String dicCode) {
        return tpDictionaryService.selectDicName(dicCode);
    }

    /**
     * 根据字典code批量查询字典，多个都好隔开
     *
     * @param dicCodes 字典编码,逗号隔开，如：HZD12,HZD13...
     * @return java.lang.String
     * @author 杨攀
     * @date 2023/11/15 10:24
     */
    public String getDicNameByCodes(String dicCodes) {
        List<TpDictionaryVO> list = tpDictionaryService.selectBydicCodes(dicCodes);
        // 提取 name
        List<String> names = list.stream().map(item -> item.getDicName()).collect(Collectors.toList());
        return CollUtil.join(names, ",");
    }

    /**
     * 根据字典code批量查询字典名称，多个逗号隔开
     *
     * @param dicCodes 字典编码,map<dicCode, dicName>
     * @return java.lang.String
     * @author pand
     * @date 2024/03/15 10:24
     */
    public Map<String, String> getDicMapByCodes(String dicCodes) {
        List<TpDictionaryVO> list = tpDictionaryService.selectBydicCodes(dicCodes);
        // 提取 <dicCode, dicName>
        Map<String, String> map = list.stream().collect(Collectors.toMap(TpDictionaryVO::getDicCode, TpDictionaryVO::getDicName, (k1, k2) -> k2));

        return map;
    }

    /**
     * @param dicCode 字典编码
     * @return TpDictionaryVO 字典对象
     * 请使用： getDicByCode ，原因不应该返回TpDictionaryVO
     */
    public TpDictionaryVO getByCode(String dicCode) {
        TpDictionaryVO vo = tpDictionaryService.selectBydicCode(dicCode);
        return vo;
    }

    /*
     * 根据字典编码查询字典信息
     *
     * @param dicCode
     * @return com.jiuxi.plugin.api.bean.dto.TpDictionaryDTO
     * @author 杨占锐
     * @date 2024/6/26 10:51
     */
    public Object getDicByCode(String dicCode) {
        TpDictionaryVO vo = tpDictionaryService.selectBydicCode(dicCode);
        if (null == vo) {
            return null;
        }
        // return BeanUtil.copyProperties(vo, TpDictionaryDTO.class); // Commented out - TpDictionaryDTO not available
        return null;
    }

    /*
     * 根据id查询字典信息
     *
     * @param dicId
     * @return com.jiuxi.plugin.api.bean.dto.TpDictionaryDTO
     * @author 杨占锐
     * @date 2024/6/26 10:51
     */
    public Object getDicById(String dicId) {
        TpDictionaryVO vo = tpDictionaryService.view(dicId);
        if (null == vo) {
            return null;
        }
        // return BeanUtil.copyProperties(vo, TpDictionaryDTO.class); // Commented out - TpDictionaryDTO not available
        return null;
    }

    /**
     * 根据字典编码，查询直接下级
     *
     * @param dicCode 字典编码
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author 杨占锐
     * @date 2024/5/27 17:11
     */
    public List<TreeNode> listChildrenByDicCode(String dicCode) {
        return tpDictionaryService.select(dicCode);
    }

    /**
     * 根据字典编码，查询所有下级
     *
     * @param dicCode 字典编码
     * @return java.util.List<com.jiuxi.common.bean.TreeNode>
     * @author 杨占锐
     * @date 2024/5/27 17:11
     */
    /*
    @Override
    public List<TreeNode> treeByDicCode(String dicCode, boolean returnTopNode) {
        return tpDictionaryService.treeByDicCode(dicCode, returnTopNode);
    }
    */

}
