package com.jiuxi.admin.core.controller.pc;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.vo.CJsonVO;
import com.jiuxi.admin.core.service.CJsonService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.core.core.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;


/**
 * @ClassName: CJsonCesController
 * @Description: ""
 * @Author pand
 * @Date 2021-05-12 13:48:38
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/c-json")
@Authorization
public class CJsonController {

    @Autowired
    private CJsonService cJsonCesService;

    /**
     * 列表
     */
    @SuppressWarnings("deprecation")
    @RequestMapping("/list")
    public JsonResponse list(HttpServletRequest request, String jwtaid, String jwtdid, String jwtCityCode) {
        JSONObject query = new JSONObject();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String element = parameterNames.nextElement();
            String value = request.getParameter(element);
            query.put(element, value);
        }

        IPage<Map> page = cJsonCesService.queryPage(query, jwtaid, jwtdid, jwtCityCode);
        return JsonResponse.buildSuccess(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/view")
    public JsonResponse view(String fid, String id) {
        CJsonVO vo = cJsonCesService.view(fid, id);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 保存
     */
    @RequestMapping("/add")
    public JsonResponse add(@Validated CJsonVO cJsonCes, String jwtaid, String jwtdid, String jwtpid, String jwtCityCode) {
        String id = cJsonCesService.add(cJsonCes, jwtaid, jwtdid, jwtpid, jwtCityCode);
        return JsonResponse.buildSuccess(id);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public JsonResponse update(@Validated CJsonVO cJsonCes, String jwtaid, String jwtdid, String jwtpid, String jwtCityCode) {
        cJsonCesService.update(cJsonCes, jwtaid, jwtdid, jwtpid, jwtCityCode);
        return JsonResponse.buildSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String fid, String[] ids) {
        cJsonCesService.deleteByIds(fid, Arrays.asList(ids));
        return JsonResponse.buildSuccess();
    }

}
