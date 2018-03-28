package fr.rhiobet;

import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;

public class Main {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include("fr.rhiobet.benchmarks.BenchStreamVsListSum")
                .include("fr.rhiobet.benchmarks.BenchStreamVsListIO")
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .shouldFailOnError(true)
                .build();
        Collection<RunResult> results = new Runner(opt).run();
    }
}
