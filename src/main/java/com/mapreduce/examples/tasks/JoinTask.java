package com.mapreduce.examples.tasks;

import com.mapreduce.framework.executor.Task;

public class JoinTask<T> extends Task<T> {

    public JoinTask(Class<T> clazz, Integer parallelization, String path) {
        super(clazz, parallelization, path);
    }
}
