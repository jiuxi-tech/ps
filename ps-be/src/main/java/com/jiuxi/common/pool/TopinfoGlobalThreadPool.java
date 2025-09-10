package com.jiuxi.common.pool;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName: TopinfoGlobalThreadPool
 * @Description: 全局的线程池, 异步方法在这个线程池中执行。
 * @Author: 杨攀
 * @Date: 2020/3/19 14:02
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class TopinfoGlobalThreadPool {

    private static Logger logger = LoggerFactory.getLogger(TopinfoGlobalThreadPool.class);

    /**
     * 核心线程池数量
     */
    private static final int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;;

    /**
     * 最大线程池数量
     */
    private static final int maximumPoolSize = 3000;

    /**
     * 队列长度
     */
    private static final int capacity = 200;

    /**
     * 线程最大空闲时间
     */
    private static final long keepAliveTime = 60;


    /**
     * 单例
     */
    private static TopinfoGlobalThreadPool instance;

    /**
     * 线程名称前缀
     */
    private static final String NAME_PREFIX = "TopinfoGlobalThread-";

    /**
     * 自动递增的线程ID
     */
    private final AtomicLong atomicLong = new AtomicLong(1);


    /**
     * 线程池
     */
    private ThreadPoolExecutor poolExecutor;


    /**
     * @param
     * @return
     * @description: 私有化
     * @author 杨攀
     * @date 2020/6/2 10:13
     */
    private TopinfoGlobalThreadPool() {
        init();
    }


    /**
     * @param
     * @return com.jiuxi.core.pool.TopinfoGlobalThreadPool
     * @description: 单例
     * @author 杨攀
     * @date 2020/6/2 15:57
     */
    public static TopinfoGlobalThreadPool getInstance() {
        if (instance == null) {
            synchronized (TopinfoGlobalThreadPool.class) {
                if (instance == null) {
                    instance = new TopinfoGlobalThreadPool();
                }
            }
        }
        return instance;
    }


    /**
     * @param
     * @return void
     * @description: 线程安全的初始化
     * @author 杨攀
     * @date 2020/3/19 14:19
     */
    private void init() {
        if (poolExecutor == null) {
            initPool();
        }
    }

    /**
     * @param
     * @return void
     * @description: 初始化线程池
     * @author 杨攀
     * @date 2020/3/19 14:20
     */
    private void initPool() {

        // 固定容量的线程
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(capacity);

        // 自定义线程
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNamePrefix(NAME_PREFIX).build();

        // 自定义的拒绝策略,要求所有的任务都必须被线程池执行,而且都要在线程池中执行
        RejectedExecutionHandler handler = (r, executor) -> {
            if (!executor.isShutdown()) {
                try {
                    logger.error("拒绝策略中队列阻塞,请排查问题或调整连接池的参数...");
                    executor.getQueue().put(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.SECONDS, workQueue, threadFactory, handler);

    }


    /**
     * @param runnable
     * @return void
     * @description: 异步执行
     * @author 杨攀
     * @date 2020/3/19 14:22
     */
    public void excAsync(Runnable runnable) {
        poolExecutor.execute(runnable);
    }


    /**
     * @param task
     * @return Future
     * @description: 同步执行
     * @author 杨攀
     * @date 2020/3/19 14:22
     */
    public <T> Future<T> excSync(Callable<T> task) {
        Future<T> future = poolExecutor.submit(task);
        return future;
    }

}
