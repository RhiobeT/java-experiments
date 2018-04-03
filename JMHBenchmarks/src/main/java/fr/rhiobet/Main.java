package fr.rhiobet;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Main {
  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
//        .include("fr.rhiobet.benchmarks.BenchStreamVsListSum")
//        .include("fr.rhiobet.benchmarks.BenchStreamIO")
        .include("fr.rhiobet.benchmarks.BenchFinalArray")
//        .include("fr.rhiobet.benchmarks.BenchStreamMatrixMultiplication")
        .warmupIterations(10)
        .measurementIterations(10)
        .forks(1)
        .shouldFailOnError(true)
        .build();
    
    new Runner(opt).run();
  }
}