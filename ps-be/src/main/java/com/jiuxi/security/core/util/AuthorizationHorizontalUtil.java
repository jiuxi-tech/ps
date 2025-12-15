package com.jiuxi.security.core.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.bean.ErrorCode;
import com.jiuxi.common.bean.SessionVO;
import com.jiuxi.common.util.CommonRequestUtil;
import com.jiuxi.common.util.PassKeyUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.security.core.holder.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName: AuthorizationHorizontalUtil
 * @Description: 横向越权 工具
 * @Author: Ypp
 * @Date: 2023/11/21 13:50
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class AuthorizationHorizontalUtil {


    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationHorizontalUtil.class);

    private static String METHOD = "GET";

    /**
     * 横向越权校验
     * @author Ypp
     * @date 2023/11/21 13:52
     * @param businessKey
     * @param request
     * @return void
     */
    public static void authHorizontal(String businessKey, HttpServletRequest request) throws IOException{


        String method = request.getMethod();
        // 如果是 get 请求，则Content-Type为空
        if (METHOD.equalsIgnoreCase(method)) {
            handleRequest(businessKey, request);
            return;
        }

        // 获取请求类型
        String contentType = request.getContentType();
        // 尝试解析为 MediaType
        MediaType mediaType = MediaType.parseMediaType(contentType);

        // 判断请求类型
        if (MediaType.APPLICATION_JSON.includes(mediaType)) {

            // 判断请求类型是否是 application/json
            handleJsonRequest(businessKey, request);

        } else if(MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType)) {

            // 判断请求类型是否是application/x-www-form-urlencoded
            handleRequest(businessKey, request);

        } else {
            // 其他请求，放行，如：上传附件等
        }

    }

    /**
     * 处理 get  post 请求
     * @author Ypp
     * @date 2023/11/21 13:47
     * @param businessKey
     * @param request
     * @return void
     */
    private static void handleRequest(String businessKey, HttpServletRequest request) throws IOException {
        // 入参 业务主键
        String businessKeyVal = request.getParameter(businessKey);
        // 入参 passKey
        String passKey = request.getParameter(PassKeyUtil.PASSKEY_NAME);
        // 校验横向越权
        checkAuthorizationHorizontal(businessKey, businessKeyVal, passKey);
    }

    /**
     * 处理 json 请求
     * @author Ypp
     * @date 2023/11/21 13:43
     * @param businessKey
     * @param request
     * @return void
     */
    private static void handleJsonRequest(String businessKey, HttpServletRequest request) throws IOException {

        // 获取 json
        String jsonBody =  CommonRequestUtil.getBodyString(request);

        // 验证一个对象是否是有效的JSONObject
        if (JSON.isValidObject(jsonBody)) {

            JSONObject jsonObject = JSON.parseObject(jsonBody);
            // 入参 业务主键
            String businessKeyVal =  jsonObject.getString(businessKey);
            // 入参 passKey
            String passKey = jsonObject.getString(PassKeyUtil.PASSKEY_NAME);

            // 校验横向越权
            checkAuthorizationHorizontal(businessKey, businessKeyVal, passKey);

        } else if (JSON.isValidArray(jsonBody)) {

            JSONArray jsonArray = JSON.parseArray(jsonBody);

            for (Object item : jsonArray) {
                // 判断是否是数组，如果还是数组，则不处理
                if (item instanceof JSONArray) {
                    break;
                }

                // 如果是对象是JSONObject 对象
                JSONObject jsonObject = (JSONObject) item;
                // 入参 业务主键
                String businessKeyVal =  jsonObject.getString(businessKey);
                // 入参 passKey
                String passKey = jsonObject.getString(PassKeyUtil.PASSKEY_NAME);

                // 校验横向越权
                checkAuthorizationHorizontal(businessKey, businessKeyVal, passKey);
            }
        }

    }

    /**
     * 校验 横向越权
     * @author Ypp
     * @date 2023/11/1 13:35
     * @param businessKey 业务主键
     * @param businessKeyVal 业务主键的值
     * @param passKey 前端传入的passKey
     * @return void
     */
    private static void checkAuthorizationHorizontal(String businessKey, String businessKeyVal, String passKey) {

        SessionVO sessionVO = SessionHolder.get();

        // 获取 人员id
        String jwtpid = sessionVO.getPersonId();
        if (StrUtil.isBlank(jwtpid)) {
            return;
        }

        // 如果业务主键的值为空，则不进行 横向鉴权操作
        if(StrUtil.isBlank(businessKeyVal)){
            return;
        }

        // 如果为null, 抛出异常，终止后续执行
        if (StrUtil.isBlank(passKey)) {
            LOGGER.error("业务主键:{}或passKey为NULL，水平越权验证失败!", businessKey);
            throw new TopinfoRuntimeException(ErrorCode.AUTHHORIZONTAL_ERROR.getCode(), ErrorCode.AUTHHORIZONTAL_ERROR.getMsg());
        }

        // 与前端传来的 passKey 比较
        if (!PassKeyUtil.check(passKey, businessKeyVal, jwtpid)) {
            LOGGER.error("passKey不匹配，水平越权验证失败!", businessKey);
            throw new TopinfoRuntimeException(ErrorCode.AUTHHORIZONTAL_ERROR.getCode(), ErrorCode.AUTHHORIZONTAL_ERROR.getMsg());
        }

        // 如果匹配，则没抛出异常，继续执行目标方法。
    }

}
