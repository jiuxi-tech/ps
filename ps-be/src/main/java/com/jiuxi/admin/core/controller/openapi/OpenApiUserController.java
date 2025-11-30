package com.jiuxi.admin.core.controller.openapi;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.vo.OpenApiUserVO;
import com.jiuxi.admin.core.service.OpenApiUserService;
import com.jiuxi.common.bean.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 开放API - 用户信息查询接口
 * 
 * 所有接口需要通过API Key验证，在拦截器中处理
 * 返回的数据均已脱敏处理
 * 
 * @author system
 * @date 2025-01-30
 */
@RestController
@RequestMapping("/open-api/v1/users")
public class OpenApiUserController {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiUserController.class);

    @Autowired
    private OpenApiUserService openApiUserService;

    /**
     * 查询单个用户信息
     * 
     * 接口路径: GET /open-api/v1/users/{personId}
     * 
     * @param personId 人员ID
     * @return 脱敏后的用户信息
     */
    @GetMapping("/{personId}")
    public JsonResponse getUserById(@PathVariable("personId") String personId) {
        logger.info("开放API - 查询用户信息: personId={}", personId);

        if (!StringUtils.hasText(personId)) {
            return JsonResponse.buildFailure("人员ID不能为空");
        }

        OpenApiUserVO user = openApiUserService.getUserById(personId);
        
        if (user == null) {
            return JsonResponse.buildFailure("用户不存在");
        }

        return JsonResponse.buildSuccess(user);
    }

    /**
     * 分页查询用户列表
     * 
     * 接口路径: GET /open-api/v1/users
     * 
     * @param deptId 部门ID（可选）
     * @param page 页码，默认1
     * @param size 每页条数，默认20，最大100
     * @return 脱敏后的用户列表
     */
    @GetMapping
    public JsonResponse getUserList(
            @RequestParam(value = "deptId", required = false) String deptId,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        
        logger.info("开放API - 查询用户列表: deptId={}, page={}, size={}", deptId, page, size);

        IPage<OpenApiUserVO> result = openApiUserService.getUserList(deptId, page, size);

        Map<String, Object> data = new HashMap<>();
        data.put("total", result.getTotal());
        data.put("page", result.getCurrent());
        data.put("size", result.getSize());
        data.put("records", result.getRecords());

        return JsonResponse.buildSuccess(data);
    }

    /**
     * 搜索用户
     * 
     * 接口路径: POST /open-api/v1/users/search
     * 
     * @param searchParams 搜索参数
     * @return 脱敏后的用户列表
     */
    @PostMapping("/search")
    public JsonResponse searchUsers(@RequestBody Map<String, Object> searchParams) {
        String keyword = (String) searchParams.get("keyword");
        String deptId = (String) searchParams.get("deptId");
        Integer page = searchParams.get("page") != null ? (Integer) searchParams.get("page") : 1;
        Integer size = searchParams.get("size") != null ? (Integer) searchParams.get("size") : 20;

        logger.info("开放API - 搜索用户: keyword={}, deptId={}, page={}, size={}", keyword, deptId, page, size);

        if (!StringUtils.hasText(keyword)) {
            return JsonResponse.buildFailure("搜索关键词不能为空");
        }

        IPage<OpenApiUserVO> result = openApiUserService.searchUsers(keyword, deptId, page, size);

        Map<String, Object> data = new HashMap<>();
        data.put("total", result.getTotal());
        data.put("page", result.getCurrent());
        data.put("size", result.getSize());
        data.put("records", result.getRecords());

        return JsonResponse.buildSuccess(data);
    }
}
