package com.neuq.question.domain.excal.support;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * 专用线程池
 * @author wangshyi
 * @date 2018/12/27  13:36
 */
public class CommonUpesnForkJoinPool {
    protected ForkJoinPool pool;

    public CommonUpesnForkJoinPool(int parallelism, String threadPrefix) {

        final ForkJoinPool.ForkJoinWorkerThreadFactory factory = po -> {
            final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(po);
            worker.setName(threadPrefix + worker.getPoolIndex());
            return worker;
        };

        pool = new ForkJoinPool(parallelism, factory, null, false);
    }

    public ForkJoinTask<?> submit(Runnable task) {
        return pool.submit(task);
    }

    public <T> ForkJoinTask<T> submit(Callable<T> task) {
        return pool.submit(task);
    }

    public <T> ForkJoinTask<T> submit(ForkJoinTask<T> task) {
        return pool.submit(task);
    }

}
