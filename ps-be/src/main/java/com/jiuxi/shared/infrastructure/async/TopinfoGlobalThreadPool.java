package com.jiuxi.shared.infrastructure.async;

import com.jiuxi.core.autoconfig.CoreConfigurationProperties;
import com.jiuxi.core.bean.Threadpool;
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

/**
 * 全局的线程池, 异步方法在这个线程池中执行。该线程池交予spring管理
 *
 * @author pand
 * @date 2023-04-12
 */
public class TopinfoGlobalThreadPool {

    private static Logger logger = LoggerFactory.getLogger(TopinfoGlobalThreadPool.class);

    /**
     * 线程池
     */
    private ThreadPoolExecutor poolExecutor;

    /**
     * 线程名称
     */
    private static final String THREAD_NAME_PREFIX = "Global-Thread-";

    /**
     * 构造函数初始化线程池
     *
     * @param
     * @return
     * @author pand
     * @date
     */
    public TopinfoGlobalThreadPool(CoreConfigurationProperties properties) {
        initPool(properties);
    }

    /**
     * @param
     * @return void
     * @description: 初始化线程池
     * @author Ypp
     * @date 2020/3/19 14:20
     */
    private void initPool(CoreConfigurationProperties properties) {

        Threadpool threadpool = properties.getThreadpool();

        // 固定容量的线程
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(threadpool.getCapacity());

        // 自定义线程
        ThreadFactory threadFactory = r -> {
            Thread thread = new Thread(r);
            String name = thread.getName();
            thread.setName(THREAD_NAME_PREFIX + name);
            return thread;
        };

        // 自定义的拒绝策略,要求所有的任务都必须被线程池执行,而且都要在线程池中执行
        RejectedExecutionHandler handler = (r, executor) -> {
            if (!executor.isShutdown()) {
                try {
                    logger.warn("Global-Thread-拒绝策略中队列阻塞,请排查问题或调整连接池的参数...");
                    executor.getQueue().put(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        poolExecutor = new ThreadPoolExecutor(threadpool.getCorePoolSize(), threadpool.getMaxPoolSize(), threadpool.getKeepAliveTime(),
                TimeUnit.SECONDS, workQueue, threadFactory, handler);

    }


    /**
     * @param runnable
     * @return void
     * @description: 异步执行
     * @author Ypp
     * @date 2020/3/19 14:22
     */
    public void excAsync(Runnable runnable) {
        poolExecutor.execute(runnable);
    }


    /**
     * @param task
     * @return Future
     * @description: 同步执行
     * @author Ypp
     * @date 2020/3/19 14:22
     */
    public <T> Future<T> excSync(Callable<T> task) {
        Future<T> future = poolExecutor.submit(task);
        return future;
    }

}
