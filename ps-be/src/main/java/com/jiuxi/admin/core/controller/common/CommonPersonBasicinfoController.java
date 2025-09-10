package com.jiuxi.admin.core.controller.common;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpPersonBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.admin.core.service.TpPersonBasicinfoService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.core.core.annotation.IgnoreAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: CommonPersonBasicinfoController
 * @Description: 业务系统使用-人员信息查询
 * @Author 杨占锐
 * @Date 2025/6/10 17:44
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/common/person")
@Authorization
public class CommonPersonBasicinfoController {

    @Autowired
    private TpPersonBasicinfoService tpPersonBasicinfoService;

    /**
     * 政府人员列表
     */
    @RequestMapping(value = "/org/list")
    @IgnoreAuthorization
    public JsonResponse orgList(TpPersonBasicQuery query) {
        query.setCategory(TpConstant.Category.ORG);
        IPage<TpPersonBasicinfoVO> page = tpPersonBasicinfoService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 企业人员列表
     */
    @RequestMapping(value = "/ent/list")
    @IgnoreAuthorization
    public JsonResponse entList(TpPersonBasicQuery query) {
        if (StrUtil.isBlank(query.getDeptId())) {
            return JsonResponse.buildFailure("请选择部门查询！");
        }
        query.setCategory(TpConstant.Category.ENT);
        IPage<TpPersonBasicinfoVO> page = tpPersonBasicinfoService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }
}
