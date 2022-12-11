package com.mapreduce.framework.mapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mapper<T, K, V> {

    //void map(KEYIN key, VALUEIN value);
    //void write(KEYOUT key, VALUEOUT value);
    //void select(String path, Supplier<Map<K, V>> supplier);
    //void map(String path, Supplier<Map<K, V>> supplier);

    public Map<K, V> mapRecords(List<T> records,
                                Function<? super T, ? extends K> keyMapper,
                                Function<? super T, ? extends V> valueMapper) {
        return records.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

}
