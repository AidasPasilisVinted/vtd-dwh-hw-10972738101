package com.mapreduce.framework.task;

import com.mapreduce.framework.mapper.Mapper;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Task<T, K> {

    private final ScheduledThreadPoolExecutor threadPool;
    private final List<Mapper<T, K, T>> mappers;

    public Task(Integer parallelization, List<Mapper<T, K, T>> mappers) {
        this.threadPool = new ScheduledThreadPoolExecutor(parallelization);
        this.mappers = mappers;
    }

    public void run() {
        mappers.stream()
                .forEach(mapper -> scheduleRunnable(() -> mapper.map()));
        threadPool.shutdown();
    }

    private void scheduleRunnable(Runnable runnable) {
        try {
            threadPool.schedule(runnable, 0, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.printf("\nFile processing exception: %s \n", e.getMessage());
            e.printStackTrace();
        }
    }
}
