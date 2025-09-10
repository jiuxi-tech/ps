package com.jiuxi.common.server.bean;


import com.jiuxi.common.util.CommonArithmeticUtil;

/**
 * @Description: 內存相关信息
 * @Author 杨占锐
 * @Date 2024/3/12 13:27
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class Memory {
    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getUsed() {
        return used;
    }

    public void setUsed(double used) {
        this.used = used;
    }

    public double getFree() {
        return free;
    }

    public void setFree(double free) {
        this.free = free;
    }

    public double getUsage() {
        return CommonArithmeticUtil.mul(CommonArithmeticUtil.div(used, total, 4), 100);
    }
}
