package com.isor.aoc.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

abstract public class AOC_Runner {

    protected List<String> allLines;
    private final String project = "AdventOfCode2018";

    {
        try {
            this.allLines = readLines();
        } catch (Exception e) {
            System.out.println("Exception thrown: " + e.getMessage());
        }
    }

    public void executeGoals() {
        executeGoal_1();
        executeGoal_2();
    }

    private List<String> readLines() throws IOException {
        String day = this.getClass().getSimpleName();
        Path path = Paths.get(project,"resources",day);
        System.out.println(path.toAbsolutePath());
        return Files.readAllLines(path);
    }

    abstract protected void executeGoal_1();

    abstract protected void executeGoal_2();
}
