package com.jiuxi.common.enums;

/**
 * @Description: http附件请求类型枚举类
 * @ClassName: HttpContentTypeEnum
 * @Author: pand
 * @Date: 2021-04-28 16:54
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public enum FileContentTypeEnum {

    PREVIEW("application/pdf; charset=UTF-8", "inline; filename*=utf-8''"),
    MP4("video/mp4; charset=UTF-8", "inline; filename*=utf-8''"),
    FLV("video/x-flv; charset=UTF-8", "inline; filename*=utf-8''"),
    WMV("video/x-ms-wmv; charset=UTF-8", "inline; filename*=utf-8''"),
    AVI("video/x-msvideo; charset=UTF-8", "inline; filename*=utf-8''"),
    DOWNLOAD("application/octet-stream; charset=UTF-8", "attachment; filename*=utf-8''");



    FileContentTypeEnum(String contentType, String ContentDisposition) {
        this.contentType = contentType;
        this.ContentDisposition = ContentDisposition;
    }

    private String contentType;
    private String ContentDisposition;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentDisposition() {
        return ContentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        ContentDisposition = contentDisposition;
    }
}
