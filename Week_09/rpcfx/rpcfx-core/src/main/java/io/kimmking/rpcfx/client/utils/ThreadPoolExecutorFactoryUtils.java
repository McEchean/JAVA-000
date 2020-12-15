package io.kimmking.rpcfx.client.utils;



import io.kimmking.rpcfx.client.service.nettyclient.util.NamedThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/2/19 0019
 * creat_time: 14:12
 **/
public class ThreadPoolExecutorFactoryUtils {

    private static final NamedThreadFactory NAMED_THREAD_FACTORY_FOR_SYNC_INCIDENT = new NamedThreadFactory("SYNC_INCIDENT");

    /**
     * 执行通用后台任务的线程池
     */
    private static final NamedThreadFactory NAMED_THREAD_FACTORY_FOR_BACKGROUND = new NamedThreadFactory("BACKGROUND");


    private static final NamedThreadFactory NAMED_THREAD_FACTORY_FOR_MDVR_RESOURCE_VEHICLE_ALARM = new NamedThreadFactory("MDVR_RESOURCE_VEHICLE_ALARM");


    /**
     * 无界队列，保证任务不被拒绝
     */
    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR_SYNC_INCIDENT = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()/2,
            Runtime.getRuntime().availableProcessors()/2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), NAMED_THREAD_FACTORY_FOR_SYNC_INCIDENT);

    /**
     * 无界队列，保证任务不被拒绝;
     */
    public static ThreadPoolExecutor THREAD_POOL_EXECUTOR_VEHICLE_RELATED_RMS;

    /**
     * 无界队列，保证任务不被拒绝
     */
    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR_MDVR_RESOURCE_VEHICLE_ALARM = new ThreadPoolExecutor(2,
            2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), NAMED_THREAD_FACTORY_FOR_MDVR_RESOURCE_VEHICLE_ALARM);

    /**
     * 无界队列，保证任务不被拒绝
     */
    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR_V2_CENTER_COMMON_USE = new ThreadPoolExecutor(7,
            7, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), NAMED_THREAD_FACTORY_FOR_BACKGROUND);

}
