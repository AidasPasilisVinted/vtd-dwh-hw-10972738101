package com.mapreduce;

import com.mapreduce.examples.model.Click;
import com.mapreduce.examples.model.User;
import com.mapreduce.examples.output.TotalClicks;
import com.mapreduce.framework.parameters.Parameters;
import com.mapreduce.framework.reducer.Reducer;
import com.mapreduce.framework.task.Task;
import com.mapreduce.framework.mapper.Mapper;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class MapReduceApp {

    public static final String APPLICATION_YML = "application.yml";

    public static void main(String[] args) {
        /**
         * Loads parameters from "resources/application.yml"
         */
        var params = loadParameters();
        var parallelization = params.getParallelization();

        /**
         * Task #1: using map-reduce framework for aggregation
         *
         * Having in mind "data/clicks" dataset with "date" column,
         * counts how many clicks there were for each date and writes
         * the results to "output/clicks_per_day.csv" dataset
         * with "date" and "clicksPerDay" columns.
         */

        var aggregationTask = new Task<>(parallelization,
                List.of(new Mapper<>(Click.class,
                        params.get("data.clicks"),
                        click -> click.getDate(),
                        click -> click)),
                new Reducer<>(params.get("output.clicks.per.day"),
                        (data) -> data.entrySet().parallelStream()
                                .flatMap(entry -> entry.getValue().entrySet().stream())
                                .map(entry -> new TotalClicks(entry.getKey(), entry.getValue().size()))
                                .toList())
        );

        runTask(aggregationTask);

        /**
         * Task #2: joining two datasets using map-reduce framework
         *
         * There are two datasets:
         *  -  "data/users" dataset with columns "id" and "country"
         *  -  "data/clicks" dataset with columns "date", "user_id" and "click_target"
         *  Task produces a new dataset "output/filtered_clicks.csv"
         *  that includes only those clicks that belong to users from Lithuania ("country"="LT").
         */
        var joinTask = new Task(parallelization,
                List.of(new Mapper<>(User.class,
                                params.get("data.users"),
                                user -> "LT".equals(user.getCountry()),
                                user -> user.getId(),
                                user -> user),
                        new Mapper<>(Click.class,
                                params.get("data.clicks"),
                                click -> click.getUser_id(),
                                click -> click)),
                new Reducer<>(params.get("output.filtered.clicks"),
                        (data) -> {
                            var users = data.get("user").entrySet().parallelStream()
                                    .map(user -> user.getKey())
                                    .toList();
                            return data.get("click").entrySet().parallelStream()
                                    .filter(click -> users.contains(click.getKey()))
                                    .flatMap(click -> click.getValue().stream())
                                    .toList();
                        }));
        runTask(joinTask);
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
