package com.mapreduce.framework.executor;

import com.mapreduce.examples.model.User;
import com.mapreduce.framework.reader.CsvReader;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public abstract class Task<T> {

    private Class<T> clazz;
    private Integer parallelization;
    private ScheduledThreadPoolExecutor threadPool;
    private String path;


    public Task(Class<T> clazz, Integer parallelization, String path) {
        this.clazz = clazz;
        this.parallelization = parallelization;
        this.threadPool = new ScheduledThreadPoolExecutor(parallelization);
        this.path = path;
    }

    public void run() {
        Stream.of(getFile().listFiles())
                .forEach(this::processFile);
        threadPool.shutdown();
    }

    private File getFile() {
        return new File(getClass().getClassLoader().getResource(path).getPath());
    }

    private void processFile(File file) {
        try {
             Runnable runnable = () -> doJob(file);
             threadPool.schedule(runnable, 0, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.printf("\nFile %s processing exception: %s \n", file.getName(),  e.getMessage());
        }
    }

    private void doJob(File file) {
        try{
          var reader = new CsvReader<T>(clazz);
          var records = reader.readFromFile(file);
          System.out.printf("\nFile %s has %s records", file.getName(), records.size());
        } catch (IOException | NoSuchFieldException e) {
            System.err.printf("\nProcessing of the file %s failed due to exception %s", file.getName(), e.getMessage());
        }
    }

}
