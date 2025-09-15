package com.jiuxi.module.user.intf.web.controller;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import com.jiuxi.module.user.app.dto.UserCreateDTO;
import com.jiuxi.module.user.app.dto.UserQueryDTO;
import com.jiuxi.module.user.app.dto.UserResponseDTO;
import com.jiuxi.module.user.app.dto.UserUpdateDTO;
import com.jiuxi.module.user.app.service.UserApplicationService;
import com.jiuxi.module.user.intf.web.dto.PageResult;
import com.jiuxi.security.core.entity.vo.PersonVO;
import com.jiuxi.security.core.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 用户管理控制器
 * 采用DDD架构的用户管理接口
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@RestController
@RequestMapping("/api/v1/users")
@Authorization
@Slf4j
public class UserController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "personId";

    @Autowired
    private UserApplicationService userApplicationService;

    @Autowired
    private PersonService personService;

    /**
     * 登录成功后,获取用户信息
     */
    @GetMapping("/me")
    @IgnoreAuthorization
    public JsonResponse getCurrentUserInfo(
            @RequestHeader(value = "X-User-Dept-Id", required = false) String jwtdid,
            @RequestHeader(value = "X-User-Person-Id", required = false) String jwtpid,
            @RequestHeader(value = "X-User-Role-Ids", required = false) String jwtrids,
            @RequestHeader(value = "X-User-City-Code", required = false) String jwtCityCode) {
        
        PersonVO vo = personService.getUserInfo(jwtdid, jwtpid);
        vo.setRoleIds(jwtrids);
        vo.setCityCode(jwtCityCode);
        
        return JsonResponse.buildSuccess(vo);
    }

    // 已将以下方法迁移到UserCommandController和UserQueryController中，避免URL映射冲突
    // - updateUser (PUT /api/v1/users/{personId})
    // - getUserDetail (GET /api/v1/users/{personId})
    // - getUserByUsername (GET /api/v1/users/username/{username})
    // - getUsersByDepartment (GET /api/v1/users/departments/{deptId})
    // - searchUsers (POST /api/v1/users/search)
}