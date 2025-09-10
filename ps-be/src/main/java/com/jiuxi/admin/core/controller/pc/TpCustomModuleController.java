package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpCustomModuleQuery;
import com.jiuxi.admin.core.bean.vo.TpCustomModuleVO;
import com.jiuxi.admin.core.service.TpCustomModuleService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.core.core.validator.group.AddGroup;
import com.jiuxi.core.core.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName: TpCustomModuleController
 * @Description: 自定义模块信息表 存储模块的信息，按钮信息、路由信息
 * @Author pand
 * @Date 2021-05-11 11:22:40
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/custom/module")
@Authorization
public class TpCustomModuleController {

    @Autowired
    private TpCustomModuleService tpCustomModuleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpCustomModuleQuery query) {
        IPage<TpCustomModuleVO> page = tpCustomModuleService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 保存
     */
    @RequestMapping("/add")
    public JsonResponse add(@Validated(value = {AddGroup.class}) TpCustomModuleVO tpCustomModule, String jwtpid) {
        String mid = tpCustomModuleService.add(tpCustomModule, jwtpid);
        return JsonResponse.buildSuccess(mid);
    }

    /**
     * 查看
     */
    @RequestMapping("/view")
    public JsonResponse view(String mid) {
        TpCustomModuleVO vo = tpCustomModuleService.view(mid);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpCustomModuleVO tpCustomModule, String jwtpid) {
        tpCustomModuleService.update(tpCustomModule, jwtpid);
        return JsonResponse.buildSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String mids) {
        tpCustomModuleService.deleteByIds(mids);
        return JsonResponse.buildSuccess();
    }

}
