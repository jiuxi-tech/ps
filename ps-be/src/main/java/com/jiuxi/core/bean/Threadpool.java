package com.jiuxi.core.bean;

/**
 * @Description: 线程池配置项
 * @ClassName: Threadpool
 * @Author: pdd
 * @Date: 2020-11-23 09:42
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class Threadpool {

    /**
     * 核心线程池数量
     */
    private int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 定时任务核心线程池数量
     */
    private int schedulerCorePoolSize = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 默认：最大线程池数量
     */
    private int maxPoolSize = 3000;

    /**
     * 默认：队列长度
     */
    private int capacity = 200;

    /**
     * 默认：线程最大空闲时间；单位：秒
     */
    private int keepAliveTime = 60;

    private Boolean allowCoreThreadTimeOut = true;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getSchedulerCorePoolSize() {
        return schedulerCorePoolSize;
    }

    public void setSchedulerCorePoolSize(int schedulerCorePoolSize) {
        this.schedulerCorePoolSize = schedulerCorePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public boolean getAllowCoreThreadTimeOut() {
        return allowCoreThreadTimeOut;
    }

    public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

}
