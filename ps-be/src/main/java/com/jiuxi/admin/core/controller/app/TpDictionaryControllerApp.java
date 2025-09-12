package com.jiuxi.admin.core.controller.app;

import cn.hutool.core.util.StrUtil;
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
@RequestMapping("/app/sys/dict")
@Authorization
public class TpDictionaryControllerApp {

    @Autowired
    private TpDictionaryService tpDictionaryService;


    /**
     * 树
     * 前端参数传pdicCode和dicCode都可以。如果都传，以dicCode为准。
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

    /**
     * 根据多个字典code批量查询字典
     *
     * @param dicCodes 字典编码,逗号隔开，如：HZD12,HZD13...
     * @author 杨占锐
     * @date 2024/10/8 13:51
     */
    @RequestMapping(value = "/list-by-dic-codes")
    @IgnoreAuthorization
    public JsonResponse listByDicCodes(String dicCodes){
        List<TpDictionaryVO> list = tpDictionaryService.selectBydicCodes(dicCodes);
        return JsonResponse.buildSuccess(list);
    }
}
