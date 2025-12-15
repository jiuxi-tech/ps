package com.jiuxi.common.bean;

import com.jiuxi.common.util.PassKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @ClassName: JsonResponse
 * @Description: 普通结果返回
 * @Author: Ypp
 * @Date: 2020/1/10 11:28
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResponse<T> implements Serializable {

    private static final long serialVersionUID = 8188545500009772620L;


    /**
     * 返回状态编码
     */
    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonResponse.class);

    private int code;

    /**
     * 返回信息
     */
    private String message;


    /**
     * 返回数据
     */
    private T data;


    /**
     * @Fields expand : 扩展信息
     */
    private Object expand;


    public JsonResponse() {
    }


    /**
     * @return
     * @Description: 构建一个空的JsonResponse对象
     * @return: JsonResponse
     */
    public static <T> JsonResponse<T> build() {
        return new JsonResponse<T>();
    }

    /**
     * @param logId 
     * @param string 
     * @return
     * @Description: 构建一个成功的JsonResponse对象
     * @Author:Ypp
     * @Since: 2020年3月10日上午10:41:30
     */
    public static <T> JsonResponse<T> buildSuccess(String string, String logId) {
        JsonResponse<T> response = new JsonResponse<>();
        response.setCode(ErrorCode.SUCCESS.getCode());
        return response;
    }

    /**
     * @return
     * @Description: 构建一个成功的JsonResponse对象
     * @Author:Ypp
     * @Since: 2020年3月10日上午10:41:30
     */
    public static <T> JsonResponse<T> buildSuccess(T data) {
        JsonResponse<T> response = new JsonResponse<>();
        response.setCode(ErrorCode.SUCCESS.getCode());
        response.setMessage(ErrorCode.SUCCESS.getMsg());
        response.setData(data);
        return response;
    }

    /**
     * @return
     * @Description: 构建一个成功的JsonResponse对象
     * @Author:Ypp
     * @Since: 2020年3月10日上午10:41:30
     */
    public static <T> JsonResponse<T> buildSuccess() {
        JsonResponse<T> response = new JsonResponse<>();
        response.setCode(ErrorCode.SUCCESS.getCode());
        response.setMessage(ErrorCode.SUCCESS.getMsg());
        return response;
    }

    /**
     * @return
     * @Description: 构建一个失败的JsonResponse对象
     * @Author:Ypp
     * @Since: 2020年3月10日上午10:41:50
     */
    public static JsonResponse buildFailure() {
        JsonResponse response = new JsonResponse();
        response.setCode(ErrorCode.ERROR.getCode());
        return response;
    }

    /**
     * @param message
     * @return
     * @Description: 构建一个失败的JsonResponse对象
     * @Author:Ypp
     * @Since: 2020年3月10日上午10:41:50
     */
    public static JsonResponse buildFailure(String message) {
        JsonResponse response = new JsonResponse();
        response.setCode(ErrorCode.ERROR.getCode());
        response.setMessage(message);
        return response;
    }

    /**
     * @param message
     * @return
     * @Description: 构建一个失败的JsonResponse对象（别名方法）
     * @Author:Ypp
     * @Since: 2020年3月10日上午10:41:50
     */
    public static JsonResponse failByMsg(String message) {
        return buildFailure(message);
    }

    /**
     * @param code
     * @param message
     * @return
     * @Description: 构建一个JsonResponse对象
     * @Author:Ypp
     * @Since: 2020年3月10日上午10:42:00
     */
    public static JsonResponse build(int code, String message) {

        JsonResponse response = new JsonResponse();
        response.setCode(code);
        response.setMessage(message);

        return response;
    }

    /**
     * @param code
     * @param message
     * @param data
     * @return
     * @Description: 构建一个JsonResponse对象
     * @Author:Ypp
     * @Since: 2020年3月10日上午10:42:11
     */
    public static <T> JsonResponse<T> build(int code, String message, T data) {
        JsonResponse<T> response = new JsonResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    /**
     * @param code
     * @param message
     * @param data
     * @param expandl
     * @return
     * @Description: 构建一个JsonResponse对象
     * @Author:Ypp
     * @Since: 2020年3月10日上午10:42:11
     */
    public static <T> JsonResponse<T> build(int code, String message, T data, Object expandl) {
        JsonResponse<T> response = new JsonResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        response.setExpand(expandl);
        return response;
    }


    /**
     * 生成 passKey，横向越权使用，需要确保 返回的实体中有 passKey 字段。
     *
     * @param jwtpid      当前登录人ID
     * @param businessKey 业务主键名称
     * @return com.jiuxi.common.bean.JsonResponse
     * @author Ypp
     * @date 2023/10/23 17:51
     */
    public JsonResponse buildPassKey(String jwtpid, String businessKey) {

        if (null != data) {
            PassKeyUtil.buildPassKey(data, jwtpid, businessKey);
        }

        return this;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public T getData() {
        return data;
    }


    public void setData(T data) {
        this.data = data;
    }


    public Object getExpand() {
        return expand;
    }

    public void setExpand(Object expand) {
        this.expand = expand;
    }
}
