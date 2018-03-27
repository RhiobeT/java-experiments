import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class BenchStreamVsList {

  private static List<Long> results;

  private static int N;

  public static void main(String args[]) {
    if (args.length != 1) {
      System.err.println("Usage: java BenchStreamVsList N");
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

    List<Long> list = new ArrayList<>();
    Random random = new Random();
    long t;

    for (int j = 0; j < N; j++) {
      list.add(new Long(random.nextInt(1000)));
    }

    System.out.println("INIT OK");

    System.out.println("For loop:");
    for (int i = 0; i < 5; i++)
      results.add(computeSumWithFor(list));
    System.out.println("  -- Warm up complete --");
    t = System.nanoTime();
    for (int i = 0; i < 5; i++)
      results.add(computeSumWithFor(list));
    System.out.println("  Time: " + (System.nanoTime() - t)/5);


    System.out.println("Foreach loop:");
    for (int i = 0; i < 5; i++)
      results.add(computeSumWithForeach(list));
    t = System.nanoTime();
    System.out.println("  -- Warm up complete --");
    for (int i = 0; i < 5; i++)
      results.add(computeSumWithForeach(list));
    System.out.println("  Time: " + (System.nanoTime() - t)/5);


    System.out.println("Stream:");
    for (int i = 0; i < 5; i++)
      results.add(computeSumWithStream(list));
    t = System.nanoTime();
    System.out.println("  -- Warm up complete --");
    for (int i = 0; i < 5; i++)
      results.add(computeSumWithStream(list));
    System.out.println("  Time: " + (System.nanoTime() - t)/5);


    System.out.println("Parallel stream:");
    for (int i = 0; i < 5; i++)
      results.add(computeSumWithParallelStream(list));
    t = System.nanoTime();
    System.out.println("  -- Warm up complete --");
    for (int i = 0; i < 5; i++)
      results.add(computeSumWithParallelStream(list));
    System.out.println("  Time: " + (System.nanoTime() - t)/5);

    System.out.print("\nResults: {");
    for (Long result : results) {
      System.out.print(result + ",");
    }
    System.out.print("}\n");
  }

  
  private static long computeSumWithFor(List<Long> values) {
    long sum = 0;
    int size = values.size();
    for (int i = 0; i < size; i++) {
      sum += values.get(i);
    }
    return sum;
  }


  private static long computeSumWithForeach(List<Long> values) {
    long sum = 0;
    for (long i : values) {
      sum += i;
    }
    return sum;
  }


  private static long computeSumWithStream(List<Long> values) {
    return values.stream().mapToLong(Long::longValue).sum();
  }


  private static long computeSumWithParallelStream(List<Long> values) {
    return values.parallelStream().mapToLong(Long::longValue).sum();
  }

}
