package com.mapreduce.framework.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public final class CsvWriter {

    private CsvWriter() {
    }

    public static void writeToCsv(String fileName, List data) {
        var file = new File(System.getProperty("user.dir"), fileName);
        file.getParentFile().mkdirs();
        try (Writer writer = new FileWriter(file)) {
            new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build()
                    .write(data);
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            System.err.printf("\nWriting to file %s failed due to exception %s", fileName, e.getMessage());
            e.printStackTrace();
        }
    }
}
