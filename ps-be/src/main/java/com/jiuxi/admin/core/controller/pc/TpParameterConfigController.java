package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpParameterConfigQuery;
import com.jiuxi.admin.core.bean.vo.TpParameterConfigVO;
import com.jiuxi.admin.core.service.TpParameterConfigService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TpParameterConfigController
 * @Description: 参数配置表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/parameterconfig")
@Authorization
public class TpParameterConfigController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "pmId";

    @Autowired
    private TpParameterConfigService tpParameterConfigService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpParameterConfigQuery query, String jwtpid) {
        IPage<TpParameterConfigVO> page = tpParameterConfigService.queryPage(query);
        return JsonResponse.buildSuccess(page).buildPassKey(jwtpid, PASS_KEY);
    }


    /**
     * 信息
     */
    @RequestMapping("/view")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse view(String pmId) {
        TpParameterConfigVO vo = tpParameterConfigService.view(pmId);

        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 保存
     */
    @RequestMapping("/add")
    public JsonResponse save(@Validated(AddGroup.class) TpParameterConfigVO vo) {
        tpParameterConfigService.add(vo);

        return JsonResponse.buildSuccess();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse update(TpParameterConfigVO vo) {
        tpParameterConfigService.update(vo);

        return JsonResponse.buildSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse delete(String pmId, String jwtpid) {
        tpParameterConfigService.deleteById(pmId, jwtpid);

        return JsonResponse.buildSuccess();
    }

}
