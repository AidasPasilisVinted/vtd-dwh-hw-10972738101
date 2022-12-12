package com.mapreduce.framework.task;

import com.mapreduce.framework.mapper.Mapper;
import com.mapreduce.framework.reader.CsvReader;

import java.io.File;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public abstract class Task<T, K> {

    private Integer parallelization;
    private ScheduledThreadPoolExecutor threadPool;
    private String path;
    protected Mapper<T,K,T> mapper;
    protected CsvReader<T> reader;

    public Task(Class<T> clazz, Integer parallelization, String path, Mapper<T,K,T> mapper) {
        this.parallelization = parallelization;
        this.threadPool = new ScheduledThreadPoolExecutor(parallelization);
        this.path = path;
        this.mapper = mapper;
        this.reader = new CsvReader<T>(clazz);
    }

    public void run() {
        Stream.of(getFile().listFiles())
                .forEach(this::scheduleProcessFile);
        threadPool.shutdown();
    }

    public abstract void processFile(File file);

    private File getFile() {
        return new File(getClass().getClassLoader().getResource(path).getPath());
    }

    private void scheduleProcessFile(File file) {
        try {
             Runnable runnable = () -> processFile(file);
             threadPool.schedule(runnable, 0, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.printf("\nFile %s processing exception: %s \n", file.getName(),  e.getMessage());
        }
    }
}
