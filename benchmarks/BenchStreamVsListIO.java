import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.stream.Collectors;
import java.io.PrintWriter;
import java.util.Random;

public class BenchStreamVsListIO {

  private static int N;

  private static List<List<Long>> results;
  private static Path path;

  public static void main(String args[]) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage: java BenchStreamVsListIO N");
      System.exit(1);
    } else {
      try {
        N = Integer.parseInt(args[0]);
      } catch (Exception e) {
        System.err.println("Error: Could not parse " + args[0] + " as an integer");
        System.exit(1);
      }
    }

    results = new ArrayList<>();
    path = Paths.get("data.txt");

    // Data generation
    PrintWriter writer = new PrintWriter(path.toFile());
    Random random = new Random();
    int N = Integer.parseInt(args[0]);
    for (int i = 0; i < N; i++) {
      writer.println(random.nextInt(1000));
    }
    writer.close();


    System.out.println("INIT DONE");
    long t;

    System.out.println("List:");
    for (int i = 0; i < 5; i++)
      results.add(computeWithList());
    System.out.println("  -- Warm up complete --");
    t = System.nanoTime();
    for (int i = 0; i < 5; i++)
      results.add(computeWithList());
    System.out.println("  Time: " + (System.nanoTime() - t)/5);


    System.out.println("Stream:");
    for (int i = 0; i < 5; i++)
      results.add(computeWithStream());
    t = System.nanoTime();
    System.out.println("  -- Warm up complete --");
    for (int i = 0; i < 5; i++)
      results.add(computeWithStream());
    System.out.println("  Time: " + (System.nanoTime() - t)/5);


    System.out.println("Parallel stream:");
    for (int i = 0; i < 5; i++)
      results.add(computeWithParallelStream());
    t = System.nanoTime();
    System.out.println("  -- Warm up complete --");
    for (int i = 0; i < 5; i++)
      results.add(computeWithParallelStream());
    System.out.println("  Time: " + (System.nanoTime() - t)/5);


    System.out.println("Iterator:");
    for (int i = 0; i < 5; i++)
      results.add(computeWithIterator());
    t = System.nanoTime();
    System.out.println("  -- Warm up complete --");
    for (int i = 0; i < 5; i++)
      results.add(computeWithIterator());
    System.out.println("  Time: " + (System.nanoTime() - t)/5);


    System.out.print("\nResults: {");
    for (List<Long> result : results) {
      System.out.print(result.size() + ",");
    }
    System.out.print("}\n");
  }

  
  private static List<Long> computeWithList() throws Exception {
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


  private static List<Long> computeWithStream() throws Exception {
    return Files.lines(path).mapToLong(Long::parseLong).filter(e -> e > 900).boxed().collect(Collectors.toList());
  }


  private static List<Long> computeWithParallelStream() throws Exception {
    return Files.lines(path).parallel().mapToLong(Long::parseLong).filter(e -> e > 900).boxed().collect(Collectors.toList());
  }


  private static List<Long> computeWithIterator() throws Exception {
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
