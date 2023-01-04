package com.mapreduce.framework.csv;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.util.List;

public class CsvReader<T> {

    private final Class<T> clazz;

    public CsvReader(Class<T> clazz) {
        this.clazz = clazz;
    }

    public List<T> readFromFile(File file) throws IOException, NoSuchFieldException {
        try (Reader reader = new FileReader(file)) {
            return new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true).
                    build().
                    parse();
        }
    }

}
