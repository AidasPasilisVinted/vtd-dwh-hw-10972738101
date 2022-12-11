package com.mapreduce.framework.reducer;

import java.util.Map;
import java.util.function.Supplier;

public interface Reducer<K,V> {
    void reduce(String outputPath, Supplier<Map<K,V>> supplier);
}
