package com.mapreduce.framework.parameters;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class Parameters {

    public static final String PARALLELIZATION = "parallelization";
    private Map<String, Object> data;

    public Integer getParallelization() {
        return (Integer) data.get(PARALLELIZATION);
    }

    public String get(String paramName) {
        return (String) data.get(paramName);
    }

}
