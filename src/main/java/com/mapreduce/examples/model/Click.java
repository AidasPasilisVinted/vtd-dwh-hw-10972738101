package com.mapreduce.examples.model;

import lombok.Data;

@Data
public class Click {
    private String date;
    private String screen;
    private String user_id;
    private String click_target;
}
