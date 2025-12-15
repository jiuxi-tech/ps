package com.jiuxi.admin.core.controller.pc;

import com.jiuxi.admin.core.bean.vo.TpCustomFormVO;
import com.jiuxi.admin.core.service.TpCustomFormService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


/**
 * @ClassName: TpCustomFormController
 * @Description: 表单设计表 表单设计表
 * @Author pand
 * @Date 2021-05-11 11:22:40
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/custom/form")
@Authorization
public class TpCustomFormController {

    @Autowired
    private TpCustomFormService tpCustomFormService;


    /**
     * 新增/修改，如果该类型的表单已经存在，进行修改操作，如果不存在，进行新增操作。
     */
    @RequestMapping("/update")
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpCustomFormVO tpCustomForm, String token, String jwtpid) {
        String fid = tpCustomFormService.update(tpCustomForm, jwtpid);
        return JsonResponse.buildSuccess(fid);
    }

    /**
     * 信息
     */
    @RequestMapping("/view")
    @IgnoreAuthorization
    public JsonResponse view(String mid, String ftype, String fSource) {
        TpCustomFormVO vo = tpCustomFormService.view(mid, ftype, fSource);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 根据模块code查询表单设计信息
     */
    @RequestMapping("/mcode")
    @IgnoreAuthorization
    public JsonResponse viewByMcode(String mcode, String ftype, String fSource) {
        TpCustomFormVO vo = tpCustomFormService.viewByMcode(mcode, ftype, fSource);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String[] fid) {
        tpCustomFormService.deleteByIds(Arrays.asList(fid));
        return JsonResponse.buildSuccess();
    }

}
