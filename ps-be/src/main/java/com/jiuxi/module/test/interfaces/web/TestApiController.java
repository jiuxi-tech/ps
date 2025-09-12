package com.jiuxi.module.test.interfaces.web;

import com.jiuxi.common.bean.ApiResponse;
import com.jiuxi.common.bean.ErrorCode;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.PageResponse;
import com.jiuxi.common.constants.ApiConstants;
import com.jiuxi.common.exception.BusinessException;
import com.jiuxi.common.exception.ValidationException;
import com.jiuxi.common.util.ValidationUtils;
import com.jiuxi.shared.common.validation.groups.AddGroup;
import com.jiuxi.shared.common.validation.groups.UpdateGroup;
import com.jiuxi.module.user.app.dto.UserCreateDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: TestApiController
 * @Description: 测试API控制器 - 演示新的API规范和异常处理
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
@RestController
@RequestMapping(ApiConstants.API_VERSION_V1 + ApiConstants.TEST_PREFIX)
public class TestApiController {

    /**
     * 测试成功响应（ApiResponse）
     */
    @GetMapping("/test_json_success")
    public JsonResponse testJsonSuccess() {
        return JsonResponse.buildSuccess("测试成功");
    }

    /**
     * 测试成功响应（ApiResponse）
     */
    @GetMapping("/test_api_success")
    public ApiResponse<String> testApiSuccess() {
        return ApiResponse.success("测试成功");
    }

    /**
     * 测试业务异常
     */
    @GetMapping("/test_business_exception")
    public ApiResponse<String> testBusinessException() {
        throw BusinessException.of(ErrorCode.DATA_NOT_FOUND, "测试数据不存在");
    }

    /**
     * 测试参数验证异常
     */
    @GetMapping("/test_validation_exception")
    public ApiResponse<String> testValidationException(@RequestParam String email) {
        ValidationUtils.requireValidEmail(email, "邮箱");
        return ApiResponse.success("邮箱验证通过");
    }

    /**
     * 测试方法参数验证
     */
    @PostMapping("/test_method_validation")
    public ApiResponse<String> testMethodValidation(@Valid @RequestBody TestRequest request) {
        return ApiResponse.success("参数验证通过");
    }

    /**
     * 测试分页响应
     */
    @GetMapping("/test_page_response")
    public ApiResponse<PageResponse<String>> testPageResponse(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        List<String> data = Arrays.asList("数据1", "数据2", "数据3", "数据4", "数据5");
        PageResponse<String> pageResponse = PageResponse.of(data, 100, pageNum, pageSize);
        
        return ApiResponse.success(pageResponse);
    }

    /**
     * 测试系统异常
     */
    @GetMapping("/test_system_exception")
    public ApiResponse<String> testSystemException() {
        throw new RuntimeException("模拟系统异常");
    }

    /**
     * 测试空指针异常
     */
    @GetMapping("/test_null_pointer_exception")
    public ApiResponse<String> testNullPointerException() {
        String str = null;
        return ApiResponse.success(str.length() + ""); // 故意触发空指针异常
    }

    /**
     * 测试参数验证工具类
     */
    @PostMapping("/test_validation_utils")
    public ApiResponse<String> testValidationUtils(@RequestBody TestValidationRequest request) {
        // 测试各种验证方法
        ValidationUtils.requireNonEmpty(request.getName(), "姓名");
        ValidationUtils.requireValidEmail(request.getEmail(), "邮箱");
        ValidationUtils.requireValidPhone(request.getPhone(), "手机号");
        ValidationUtils.requireRange(request.getAge(), 1, 150, "年龄");
        ValidationUtils.requireLength(request.getPassword(), 8, 20, "密码");
        
        return ApiResponse.success("所有验证通过");
    }

    /**
     * 测试Bean Validation分组验证 - 新增用户
     */
    @PostMapping("/test_user_add")
    public ApiResponse<String> testUserAdd(@Validated(AddGroup.class) @RequestBody UserCreateDTO userDTO) {
        return ApiResponse.success("用户创建验证通过: " + userDTO.getUsername());
    }
    
    /**
     * 测试Bean Validation分组验证 - 更新用户
     */
    @PutMapping("/test_user_update")
    public ApiResponse<String> testUserUpdate(@Validated(UpdateGroup.class) @RequestBody UserCreateDTO userDTO) {
        return ApiResponse.success("用户更新验证通过: " + userDTO.getUsername());
    }
    
    /**
     * 测试自定义验证注解 - 手机号验证
     */
    @PostMapping("/test_phone_validation")
    public ApiResponse<String> testPhoneValidation(@Validated(AddGroup.class) @RequestBody UserCreateDTO userDTO) {
        return ApiResponse.success("手机号验证通过: " + userDTO.getPhone());
    }

    /**
     * 测试不同错误码
     */
    @GetMapping("/test_error_codes/{type}")
    public ApiResponse<String> testErrorCodes(@PathVariable String type) {
        switch (type) {
            case "unauthorized":
                return ApiResponse.unauthorized();
            case "forbidden":
                return ApiResponse.forbidden();
            case "not_found":
                return ApiResponse.notFound();
            case "param_error":
                return ApiResponse.paramError("参数错误示例");
            case "business_error":
                throw BusinessException.of(ErrorCode.DATA_ALREADY_EXISTS, "数据已存在");
            default:
                return ApiResponse.success("未知类型: " + type);
        }
    }

    /**
     * 测试请求对象
     */
    public static class TestRequest {
        @NotBlank(message = "姓名不能为空")
        private String name;
        
        @NotNull(message = "年龄不能为空")
        private Integer age;
        
        // Getters and Setters
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public Integer getAge() {
            return age;
        }
        
        public void setAge(Integer age) {
            this.age = age;
        }
    }

    /**
     * 测试验证请求对象
     */
    public static class TestValidationRequest {
        private String name;
        private String email;
        private String phone;
        private Integer age;
        private String password;
        
        // Getters and Setters
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPhone() {
            return phone;
        }
        
        public void setPhone(String phone) {
            this.phone = phone;
        }
        
        public Integer getAge() {
            return age;
        }
        
        public void setAge(Integer age) {
            this.age = age;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
}