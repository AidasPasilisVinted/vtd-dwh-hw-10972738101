package com.mapreduce.framework.mapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Mapper<T, K, V> {

    private final Predicate<? super T> selectPredicate;
    private final Function<? super T, ? extends K> keyMapper;
    private final Function<? super T, ? extends V> valueMapper;

    public Mapper(Function<? super T, ? extends K> keyMapper,
                  Function<? super T, ? extends V> valueMapper) {
        this(null, keyMapper, valueMapper);
    }

    public Mapper(Predicate<? super T> selectPredicate,
                  Function<? super T, ? extends K> keyMapper,
                  Function<? super T, ? extends V> valueMapper) {
        this.selectPredicate = selectPredicate;
        this.keyMapper = keyMapper;
        this.valueMapper = valueMapper;
    }

    public Map<K, V> mapRecords(List<T> records) {
        return records.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public Map<K, V> selectAndMapRecords(List<T> records) {
        return records.stream()
                .filter(selectPredicate)
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }

}
