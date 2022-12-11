package com.mapreduce.examples.tasks;

import com.mapreduce.framework.executor.Task;

public class AggregationTask<T> extends Task<T> {

    public AggregationTask(Class<T> clazz, Integer parallelization, String path) {
        super(clazz, parallelization, path);
    }
}
