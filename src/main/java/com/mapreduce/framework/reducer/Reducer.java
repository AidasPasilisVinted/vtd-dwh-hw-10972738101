package com.mapreduce.framework.reducer;

import com.mapreduce.framework.csv.CsvWriter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Reducer<K, V, O> {

    private final String outputFileName;
    private final Function<Map<String, Map<K, List<V>>>, List<O>> valueReducer;

    public Reducer(String outputFileName, Function<Map<String, Map<K, List<V>>>, List<O>> valueReducer) {
        this.outputFileName = outputFileName;
        this.valueReducer = valueReducer;
    }

    public void reduce(Map<String, Map<K, List<V>>> data) {
        List<O> output = valueReducer.apply(data);
        //Uncomment if wishing to print reduced results to console
        //printOptput(output);
        CsvWriter.writeToCsv(outputFileName, output);
    }

    /*
     * Prints reducer result to console for debug purposes
     * */
    private void printOptput(List<O> list) {
        System.out.println("\nREDUCED DATA: ");
        list.stream()
                .forEach(v -> System.out.printf("\n{%s}", v));
    }
}
