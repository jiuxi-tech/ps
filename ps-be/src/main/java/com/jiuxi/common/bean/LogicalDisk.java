package com.jiuxi.common.bean;

import java.io.Serializable;

/**
 * @ClassName: LogicalDisk
 * @Description: 上传附件的VO
 * @Author: Ypp
 * @Date: 2021/4/28 9:55
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class LogicalDisk implements Serializable {

    /**
     * 磁盘目录
     */
    private String dir;


    /**
     * 磁盘状态, rw 读写； r 只读
     */
    private String status = "rw";
    

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
