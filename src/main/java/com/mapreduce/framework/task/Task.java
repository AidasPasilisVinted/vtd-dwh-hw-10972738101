package com.mapreduce.framework.task;

import com.mapreduce.framework.mapper.Mapper;
import com.mapreduce.framework.reducer.Reducer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class Task<T, K, O> {

    private final ForkJoinPool forkJoinPool;
    private final List<Mapper<T, K, T>> mappers;
    private final Reducer<K, T, O> reducer;

    public Task(int parallelization, List<Mapper<T, K, T>> mappers, Reducer<K, T, O> reducer) {
        this.forkJoinPool = new ForkJoinPool(parallelization);
        this.mappers = mappers;
        this.reducer = reducer;
    }

    public void run() {
        try {
            Map<String, Map<K, List<T>>> mappersResult = forkJoinPool.submit(
                    () -> mappers.parallelStream()
                            .map(mapper -> Map.of(mapper.getTypeName(), mapper.map()))
                            .reduce(new HashMap<>(), (a, b) -> {
                                a.putAll(b);
                                return a;
                            })).get();
            //Uncomment if wishing to print mappers results to console
            //printResult(mappersResult);
            reducer.reduce(mappersResult);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Exception when executing mapping operation in parallel manner " + e.getMessage());
            e.printStackTrace();
        } finally {
            forkJoinPool.shutdown();
        }
    }

    /*
     * Prints mappers result to console for debug purposes
     * */
    private void printResult(Map<String, Map<K, List<T>>> map) {
        System.out.println("\n\nMAPPED DATA: ");
        map.forEach((k, v) -> {
            System.out.println("{\"" + k + "\":");
            v.forEach((key, value) -> System.out.println("\t{" + key + "," + value + "}"));
            System.out.println("}");
        });
    }
}
