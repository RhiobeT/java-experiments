package fr.rhiobet.benchmarks;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class BenchStreamVsListSum {
  private List<Long> list;
  
  @Param({"10000000"})
  private int N;

  @Setup
  public void setup() {
    list = new ArrayList<>();
    Random random = new Random();

    for (int j = 0; j < N; j++) {
      list.add(Long.valueOf(random.nextInt(1000)));
    }
  }

  @Benchmark
  public long computeSumWithFor() {
    long sum = 0;
    int size = list.size();
    for (int i = 0; i < size; i++) {
      sum += list.get(i);
    }
    return sum;
  }

  @Benchmark
  public long computeSumWithForeach() {
    long sum = 0;
    for (long i : list) {
      sum += i;
    }
    return sum;
  }

  @Benchmark
  public long computeSumWithStream() {
    return list.stream().mapToLong(Long::longValue).sum();
  }

  @Benchmark
  public long computeSumWithParallelStream() {
    return list.parallelStream().mapToLong(Long::longValue).sum();
  }

}
