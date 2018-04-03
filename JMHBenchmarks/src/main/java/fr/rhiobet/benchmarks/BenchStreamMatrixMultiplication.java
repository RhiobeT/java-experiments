package fr.rhiobet.benchmarks;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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
public class BenchStreamMatrixMultiplication {
  private int[][] matrix;
  
  @Param({"1000"})
  private int N;

  @Setup
  public void setup() {
    matrix = new int[N][N];
    Random random = new Random();

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        matrix[i][j] = random.nextInt();
      }
    }
  }

  @Benchmark
  public int[][] computeSquareWithFor() {
    int result[][] = new int[N][N];
    
    int temp;
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        temp = 0;
        for (int k = 0; k < N; k++) {
          temp += result[i][k] * result[k][j];
        }
        result[i][j] = temp;
      }
    }
    
    return result;
  }

  @Benchmark
  public int[][] computeSquareWithStream() {
    return Arrays.stream(matrix).map(
        row -> IntStream.range(0, N).map(
            i -> IntStream.range(0, N).map(
                j -> row[j] * matrix[j][i]
            ).sum()
        ).toArray()
    ).toArray(int[][]::new);
  }

  @Benchmark
  public int[][] computeSquareWithParallelStream() {
    return Arrays.stream(matrix).parallel().map(
        row -> IntStream.range(0, N).map(
            i -> IntStream.range(0, N).map(
                j -> row[j] * matrix[j][i]
            ).sum()
        ).toArray()
    ).toArray(int[][]::new);
  }
}
