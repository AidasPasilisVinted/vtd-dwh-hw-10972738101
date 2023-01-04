# Simplified MapReduce framework

@Author Alina Gončarko 2023-01-05

## About

This is a simplified map-reduce framework aimed to execute generic map and reduce computations
in a parallel manner while input data is being provided in csv format and splitted to multiple
files. 

Framework usage is being demonstrated by the following example tasks:

* Task #1: data aggregation
  Having in mind data/clicks dataset with "date" column,
  counts how many clicks there were for each date and writes
  the results to "output/clicks_per_day.csv" dataset
  with "date" and "clicksPerDay" columns.

* Task #2: joining two datasets
  When having two datasets:
     - "data/users" dataset with columns "id" and "country"
     - "data/clicks" dataset with columns "date", "user_id" and "click_target"
  Task produces a new dataset "output/filtered_clicks.csv"
  that includes only those clicks that belong to users from Lithuania ("country"="LT").

Input data for the task execution is being stored in the 'data' folder.

Output data for the task execution is being stored in the 'output' folder.

MapReduce framework is written on Java and requires [JDK 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html).

## Configuration

Configuration could be performed by editing file 'src/main/resources/application.yml'

Example:
```
parallelization: 4
data.users: data/users
data.clicks: data/clicks
output.clicks.per.day: output\clicks_per_day.csv
output.filtered.clicks: output\filtered_clicks.csv
```

The value of parallelization is used to set the corePoolSize as the number of Threads to maintain in the pool
of the [ForkJoinPool](https://docs.oracle.com/en/java/javase/18/docs/api/java.base/java/util/concurrent/ForkJoinPool.html).
Reasonable number would be number of CPU cores that application is running on.

Other values are used to configure input/output files paths for the tasks.

## Build

To build the project [Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) is being used.
Gradle wrapper is included in the project. The command to run the build:
```
./gradlew clean build
```

## Run

MapReduce framework run command:
```
java -jar build/libs/mapreduce.jar
```


