package com.jiuxi.monitor.client.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * @ClassName: MonitorClientThreadPool
 * @Description: 监控客户端线程池
 * @Author 杨占锐
 * @Date 2024/11/14 17:47
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class MonitorClientThreadPool {

    private static Logger LOGGER = LoggerFactory.getLogger(MonitorClientThreadPool.class);

    /**
     * 核心线程池数量
     */
    private static final int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 最大线程池数量
     */
    private static final int maximumPoolSize = 100;

    /**
     * 队列长度
     */
    private static final int capacity = 10;

    /**
     * 线程最大空闲时间
     */
    private static final long keepAliveTime = 120;

    /**
     * 线程池
     */
    private static ThreadPoolExecutor poolExecutor;


    static {
        poolExecutor = newThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.SECONDS, capacity,"MonitorCilent-ThreadPool-" );
    }

    /**
     * 获取线程池实例
     *
     * @return java.util.concurrent.ThreadPoolExecutor
     * @author 杨占锐
     * @date 2024/11/21 9:38
     */
    public static ThreadPoolExecutor getPoolExecutor() {
        return poolExecutor;
    }


    /**
     * 新建一个线程池，默认拒绝策略为： CallerRunsPolicy，即要求所有的任务都必须被线程池执行,不会抛出异常，而是将任务退回给调用线程，让它自己执行任务。
     * @author 杨攀
     * @date 2024/1/8 17:45
     * @param corePoolSize 核心线程数
     * @param maximumPoolSize 最大线程池
     * @param keepAliveTime 空闲时间
     * @param unit 单位, TimeUnit.SECONDS
     * @param workQueueCapacity 等待队列大小
     * @param threadNamePrefix 线程池的前缀，如：Jiuxi
     * @return java.util.concurrent.ThreadPoolExecutor
     */
    public static ThreadPoolExecutor newThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, int workQueueCapacity, String threadNamePrefix) {

        // 自定义的拒绝策略,要求所有的任务都必须被线程池执行,不会抛出异常，而是将任务退回给调用线程，让它自己执行任务。
        RejectedExecutionHandler handler = (r, executor) -> {
            // 当于重写了 ThreadPoolExecutor.CallerRunsPolicy() 方法，添加了日志打印，方便排查问题
            if (!executor.isShutdown()) {
                LOGGER.error("{}线程-队列已经满了,请排查程序问题或调整连接池的参数...", threadNamePrefix);
                r.run();
            }
        };

        return newThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueueCapacity, threadNamePrefix, handler);
    }


    /**
     * 新建一个线程池
     * @author 杨攀
     * @date 2024/3/29 15:20
     * @param corePoolSize 核心线程数
     * @param maximumPoolSize 最大线程池
     * @param keepAliveTime 空闲时间
     * @param unit 单位, TimeUnit.SECONDS
     * @param workQueueCapacity 等待队列大小
     * @param threadNamePrefix 线程池的前缀，如：Jiuxi
     * @param rejectedExecutionHandler  自定义拒绝策略，例如：new ThreadPoolExecutor.CallerRunsPolicy()
     * @return java.util.concurrent.ThreadPoolExecutor
     *
     * <pre>
     *    拒绝策略:
     *      AbortPolicy (默认策略,抛弃新增任务，并给调用线程抛出异常)
     *      DiscardPolicy（抛弃新增任务,不抛异常）
     *      DiscardOldestPolicy(喜新厌旧【把新任务添加到线程池执行，抛弃线程池里面待的最久的任务】)
     *      CallerRunsPolicy(自己处理不了，直接返还调用者)
     *
     * 原文链接：https://blog.csdn.net/WindwirdBird/article/details/120268622
     * </pre>
     *
     */
    public static ThreadPoolExecutor newThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, int workQueueCapacity, String threadNamePrefix, RejectedExecutionHandler rejectedExecutionHandler) {

        // 固定容量的线程
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(workQueueCapacity);

        // 自定义线程
        CustomizableThreadFactory threadFactory = new CustomizableThreadFactory(threadNamePrefix);

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, rejectedExecutionHandler);

        return poolExecutor;
    }

}
