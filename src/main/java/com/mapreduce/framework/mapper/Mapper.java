package com.mapreduce.framework.mapper;

import com.mapreduce.framework.csv.CsvReader;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Mapper<T, K, V> {

    private final Class<T> clazz;
    private final String filePath;
    private final Predicate<? super T> selectPredicate;
    private final Function<? super T, ? extends K> keyMapper;
    private final Function<? super T, ? extends V> valueMapper;
    private final CsvReader<T> reader;

    public Mapper(Class<T> clazz,
                  String filePath,
                  Function<? super T, ? extends K> keyMapper,
                  Function<? super T, ? extends V> valueMapper) {
        this(clazz, filePath, null, keyMapper, valueMapper);
    }

    public Mapper(Class<T> clazz,
                  String filePath,
                  Predicate<? super T> selectPredicate,
                  Function<? super T, ? extends K> keyMapper,
                  Function<? super T, ? extends V> valueMapper) {
        this.clazz = clazz;
        this.filePath = filePath;
        this.selectPredicate = selectPredicate;
        this.keyMapper = keyMapper;
        this.valueMapper = valueMapper;
        this.reader = new CsvReader<T>(clazz);
    }

    public Map<K, List<V>> map() {
        Map<K, List<V>> result;
        if (selectPredicate != null) {
            result = selectAndMapRecords();
        } else {
            result = mapRecords();
        }
        return sort(result);
    }

    public String getTypeName() {
        return clazz.getSimpleName().toLowerCase();
    }

    private Map<K, List<V>> mapRecords() {
        return readRecords().parallelStream()
                .collect(Collectors.groupingBy(keyMapper,
                        Collectors.mapping(valueMapper, Collectors.toList())));
    }

    private Map<K, List<V>> selectAndMapRecords() {
        return readRecords().parallelStream()
                .filter(selectPredicate)
                .collect(Collectors.groupingBy(keyMapper,
                        Collectors.mapping(valueMapper, Collectors.toList())));
    }

    private Map<K, List<V>> sort(Map<K, List<V>> map) {
        return map.entrySet().parallelStream()
                .sorted((c1, c2) -> ((Comparable) (c1.getKey())).compareTo(c2.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private List<T> readRecords() {
        var filesDir = new File(System.getProperty("user.dir"), filePath);
        return Stream.of(filesDir.listFiles()).parallel()
                .map(this::readFromFile)
                .flatMap(f -> f.stream())
                .collect(Collectors.toList());
    }

    private List<T> readFromFile(File file) {
        try {
            return reader.readFromFile(file);
        } catch (IOException | NoSuchFieldException e) {
            System.err.printf("\nReading from file %s failed due to exception %s", file.getName(), e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}



