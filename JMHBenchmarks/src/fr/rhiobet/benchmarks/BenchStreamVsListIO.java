package fr.rhiobet.benchmarks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class BenchStreamVsListIO {

  @Param({"10000000"})
  private int N;
    
  private Path path;

  public void setup() throws Exception {
    path = Paths.get("data.txt");

    // Data generation
    PrintWriter writer = new PrintWriter(path.toFile());
    Random random = new Random();
    for (int i = 0; i < N; i++) {
      writer.println(random.nextInt(1000));
    }
    writer.close();
  }

  @Benchmark
  public List<Long> computeWithList() throws Exception {
    List<String> values;
    List<Long> result = new ArrayList<>();
    long temp;

    values = Files.readAllLines(path);

    for (int i = 0; i < values.size(); i++) {
      temp = Long.parseLong(values.get(i));
      if (temp > 900) {
        result.add(temp);
      }
    }

    return result;
  }


  public List<Long> computeWithStream() throws Exception {
    return Files.lines(path).mapToLong(Long::parseLong).filter(e -> e > 900).boxed().collect(Collectors.toList());
  }


  public List<Long> computeWithParallelStream() throws Exception {
    return Files.lines(path).parallel().mapToLong(Long::parseLong).filter(e -> e > 900).boxed().collect(Collectors.toList());
  }


  public List<Long> computeWithIterator() throws Exception {
    String line;
    long temp;
    BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
    List<Long> result = new ArrayList<>();

    while ((line = reader.readLine()) != null) {
      if ((temp = Long.parseLong(line)) > 900) {
        result.add(temp);
      }
    }

    return result;
  }

}
