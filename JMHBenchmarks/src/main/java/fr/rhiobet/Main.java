package fr.rhiobet;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Main {
  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
//        .include("fr.rhiobet.benchmarks.BenchStreamVsListSum")
//        .include("fr.rhiobet.benchmarks.BenchStreamVsListIO")
        .include("fr.rhiobet.benchmarks.BenchFinalNormal")
        .include("fr.rhiobet.benchmarks.BenchFinalArray")
        .include("fr.rhiobet.benchmarks.BenchFinalAll")
        .warmupIterations(10)
        .measurementIterations(20)
        .forks(1)
        .shouldFailOnError(true)
        .build();
    
    new Runner(opt).run();
  }
}