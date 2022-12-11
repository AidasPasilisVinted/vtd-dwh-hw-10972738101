package com.mapreduce.examples.parameters;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class Parameters {

    public static final String PARALELLIZATION = "parallelization";
    public static final String PATH_USERS = "path.users";
    public static final String PATH_CLICKS = "path.clicks";
    private Map<String, Object> data;

    public Integer getParallelization() {
        return (Integer) data.get(PARALELLIZATION);
    }

    public String getUsersPath() {
        return (String) data.get(PATH_USERS);
    }

    public String getClicksPath() {
        return (String) data.get(PATH_CLICKS);
    }
}
