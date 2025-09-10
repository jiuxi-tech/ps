package com.jiuxi.core.bean;


import com.jiuxi.common.bean.ErrorCode;

/**
 * @ClassName: TbsException
 * @Description: 自定义错误
 * @Author: 杨攀
 * @Date: 2020/1/10 11:38
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class TopinfoRuntimeException extends RuntimeException {

    private int errcode;

    private String message;



    /**
     * 自定义异常
     * @author 杨攀
     * @date 2024/8/12 13:45
     * @param errorCode 错误枚举
     * @return
     */
    public TopinfoRuntimeException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errcode = errorCode.getCode();
        this.message = errorCode.getMsg();
    }

    /**
     * 自定义异常
     * @author 杨攀
     * @date 2024/8/12 13:44
     * @param errCode 错误码
     * @param message 错误消息
     * @return
     */
    public TopinfoRuntimeException(int errCode, String message) {
        super (message);
        this.errcode = errCode;
        this.message = message;
    }

    /**
     * 自定义异常
     * @author 杨攀
     * @date 2024/8/12 13:44
     * @param errCode 错误码
     * @param message 错误消息
     * @param throwable 错误Exception
     * @return
     */
    public TopinfoRuntimeException(int errCode, String message, Throwable throwable) {
        super (message, throwable);
        this.errcode = errCode;
        this.message = message;
    }

    public int getErrcode(){
        return errcode;
    }


    public void setErrcode(int errcode){
        this.errcode = errcode;
    }

    @Override
    public String getMessage(){
        return message;
    }


    public void setMessage(String message){
        this.message = message;
    }
}
