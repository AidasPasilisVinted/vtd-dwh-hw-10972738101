package com.mapreduce.examples.tasks;

import com.mapreduce.framework.task.Task;
import com.mapreduce.framework.mapper.Mapper;

import java.io.File;
import java.io.IOException;

public class JoinTask<T, K> extends Task<T,K> {

    public JoinTask(Class<T> clazz, Integer parallelization, String path, Mapper<T, K, T> mapper) {
        super(clazz, parallelization, path, mapper);
    }

    @Override
    public void processFile(File file) {
        try{
            var records = mapper.selectAndMapRecords(reader.readFromFile(file));
            System.out.printf("\nFile %s has %s records", file.getName(), records.size());
        } catch (IOException | NoSuchFieldException e) {
            System.err.printf("\nProcessing of the file %s failed due to exception %s", file.getName(), e.getMessage());
        }
    }
}
