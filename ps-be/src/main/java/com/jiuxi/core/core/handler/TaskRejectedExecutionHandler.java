package com.jiuxi.core.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: 线程池任务队列拒绝策略
 * @ClassName: TaskRejectedExecutionHandler
 * @Author: pand
 * @Date: 2020-09-09 10:20
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class TaskRejectedExecutionHandler implements RejectedExecutionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRejectedExecutionHandler.class);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (!executor.isShutdown()) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
