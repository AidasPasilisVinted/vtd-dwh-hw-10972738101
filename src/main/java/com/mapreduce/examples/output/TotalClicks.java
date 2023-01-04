package com.mapreduce.examples.output;

public record TotalClicks(
        String date,
        int clicksPerDay) {
}
