package com.jiuxi.shared.common.controller;

import com.jiuxi.common.bean.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: HealthController
 * @Description: 健康检查
 * @Author jiuxx
 * @Date 2023/11/2 16:43
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/platform")
public class HealthController {

    /**
     * 健康检查
     *
     * @return com.jiuxi.common.bean.JsonResponse
     * @author jiuxx
     * @date 2023/8/2 9:07
     */
    @RequestMapping("/health")
    public JsonResponse health() {
        return JsonResponse.buildSuccess();
    }
}
