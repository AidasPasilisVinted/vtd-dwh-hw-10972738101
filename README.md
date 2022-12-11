# Simplified MapReduce framework

@Author Alina Gončarko 2022-12-11

## About

This is a simplified map-reduce framework aimed to execute generic map and reduce computations
in a parallel manner while input data is being provided in csv format and splitted to multiple
files. 

Framework usage is being demonstrated by the following example tasks:

* Task #1: data aggregation
* Task #2: joining two datasets

Input data for the task execution is being stored in the 'src/main/resources/data' folder.

MapReduce framework is written on Java and requires [JDK 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html).

## Configuration

Configuration could be performed by editing file 'src/main/resources/application.yml'

Example:
```
parallelization: 12
```

The value of parallelization is used to set the corePoolSize as the number of Threads to maintain in the pool
of the [ScheduledThreadPoolExecutor](https://docs.oracle.com/en/java/javase/18/docs/api/java.base/java/util/concurrent/ScheduledThreadPoolExecutor.html).
Reasonable number would be number of CPU cores that application is running on.


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


