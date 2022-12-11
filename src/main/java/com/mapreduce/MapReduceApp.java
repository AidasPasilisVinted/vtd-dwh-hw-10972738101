package com.mapreduce;

import com.mapreduce.examples.model.User;
import com.mapreduce.examples.parameters.Parameters;
import com.mapreduce.examples.tasks.AggregationTask;
import com.mapreduce.examples.tasks.JoinTask;
import com.mapreduce.framework.executor.Task;
import com.mapreduce.framework.reader.CsvReader;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class MapReduceApp {

    public static final String APPLICATION_YML = "application.yml";

    public static void main(String[] args) {
        var params = loadParameters();
        var parallelization = params.getParallelization();

        var aggregationTask = new AggregationTask<>(User.class, parallelization, params.getUsersPath());
        runTask(aggregationTask);
        //var joinTask = new JoinTask(parallelization, params.getClicksPath());
        //runTask(joinTask);
    }

    private static void runTask(Task task) {
        try {
            task.run();
        } catch (Exception e) {
            System.err.printf("\nException during task %s execution: %s \n",
                    task.getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        }
    }

    private static Parameters loadParameters() {
        Yaml yaml = new Yaml();
        InputStream is = MapReduceApp.class.getClassLoader().getResourceAsStream(APPLICATION_YML);
        Map<String, Object> data = yaml.load(is);
        return new Parameters(data);
    }
}
