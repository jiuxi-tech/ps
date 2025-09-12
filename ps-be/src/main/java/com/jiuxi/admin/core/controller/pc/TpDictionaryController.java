package com.jiuxi.admin.core.controller.pc;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpDictionaryQuery;
import com.jiuxi.admin.core.bean.vo.TpDictionaryVO;
import com.jiuxi.admin.core.service.TpDictionaryService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: TpDictionaryController
 * @Description: 字典表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/dict")
@Authorization
public class TpDictionaryController {

    @Autowired
    private TpDictionaryService tpDictionaryService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list")
    public JsonResponse list(TpDictionaryQuery query) {
        IPage<TpDictionaryVO> page = tpDictionaryService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 树
     * 前端参数传pdicCode和dicCode都可以。如果都传，以dicCode为准。只查询启用，有效的
     */
    @RequestMapping("/tree")
    @IgnoreAuthorization
    public JsonResponse tree(@RequestParam(name = "pdicCode", required = false) String pdicCode, @RequestParam(name = "dicCode", required = false) String dicCode, @RequestParam(defaultValue = "0", required = false) boolean topFlag) {
        // 该判断是为了向前兼容，前端参数传pdicCode和dicCode都可以。
        if (StrUtil.isBlank(dicCode)) {
            if (StrUtil.isNotBlank(pdicCode)) {
                dicCode = pdicCode;
            } else {
                return JsonResponse.buildFailure("字典code不能为空！");
            }
        }

        List<TreeNode> treeNodeList = tpDictionaryService.tree(dicCode, topFlag);

        return JsonResponse.buildSuccess(treeNodeList);
    }


    /**
     * 树
     * 系统管理 -> 字典管理页查询字典树，包括未启用的。
     */
    @RequestMapping("/manage-tree")
    @IgnoreAuthorization
    public JsonResponse manageTree(@RequestParam(name = "dicCode", required = false) String dicCode, @RequestParam(defaultValue = "0", required = false) boolean topFlag) {
        List<TreeNode> treeNodeList = tpDictionaryService.manageTree(dicCode, topFlag);

        return JsonResponse.buildSuccess(treeNodeList);
    }

    /**
     * 父级code
     * 前端参数传pdicCode和dicCode都可以。如果都传，以dicCode为准。
     *
     * @return
     */
    @RequestMapping("/select")
    @IgnoreAuthorization
    public JsonResponse select(@RequestParam(name = "pdicCode", required = false) String pdicCode, @RequestParam(name = "dicCode", required = false) String dicCode) {
        // 该判断是为了向前兼容，前端参数传pdicCode和dicCode都可以。
        if (StrUtil.isBlank(dicCode)) {
            if (StrUtil.isNotBlank(pdicCode)) {
                dicCode = pdicCode;
            } else {
                return JsonResponse.buildFailure("字典code不能为空！");
            }
        }
        List<TreeNode> list = tpDictionaryService.select(dicCode);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 信息
     */
    @RequestMapping("/view")
    public JsonResponse view(String dicId) {
        TpDictionaryVO vo = tpDictionaryService.view(dicId);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 信息
     */
    @RequestMapping("/dic-name")
    @IgnoreAuthorization
    public JsonResponse dicName(String dicCode) {
        String dicName = tpDictionaryService.selectDicName(dicCode);
        return JsonResponse.buildSuccess(dicName);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/add")
    public JsonResponse save(@Validated(value = AddGroup.class) TpDictionaryVO vo, String jwtpid) {
        vo = tpDictionaryService.save(vo, jwtpid);

        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    public JsonResponse delete(String dicCodes, String jwtpid) {
        tpDictionaryService.delete(dicCodes, jwtpid);

        return JsonResponse.buildSuccess();
    }

}
