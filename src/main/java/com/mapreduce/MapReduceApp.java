package com.mapreduce;

import com.mapreduce.examples.model.User;
import com.mapreduce.examples.parameters.Parameters;
import com.mapreduce.examples.tasks.AggregationTask;
import com.mapreduce.framework.task.Task;
import com.mapreduce.framework.mapper.Mapper;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class MapReduceApp {

    public static final String APPLICATION_YML = "application.yml";

    public static void main(String[] args) {
        var params = loadParameters();
        var parallelization = params.getParallelization();

        var aggregationTask = new AggregationTask<>(User.class,
                parallelization,
                params.getUsersPath(),
                new Mapper<>(user -> user.getId(),
                        user -> user));
        runTask(aggregationTask);
        //var joinTask = new JoinTask(User.class,
        //                parallelization,
        //                params.getUsersPath(),
        //                new Mapper<>(user -> "LT".equals(user.getCountry()),
        //                        user -> user.getId(),
        //                        user -> user));
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
