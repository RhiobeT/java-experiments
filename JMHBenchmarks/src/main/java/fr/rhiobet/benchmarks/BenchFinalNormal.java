package fr.rhiobet.benchmarks;

import java.util.EnumSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import javafx.scene.input.KeyCode;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class BenchFinalNormal {
  static /*final*/ int LOOPS = Integer.MAX_VALUE / 100;

  static /*final*/ KeyCode[] keyCodes = KeyCode.values();

  static /*final*/ EnumSet<KeyCode> enumSet = EnumSet.noneOf(KeyCode.class);

  @Benchmark
  public void testEnumSet() {
      for (int i = 0; i < LOOPS; i++) {
          /*final*/ KeyCode add = getRandomKeyCode();
          if(!enumSet.contains(add)) enumSet.add(add);

          /*final*/ KeyCode remove = getRandomKeyCode();
          if(enumSet.contains(remove)) enumSet.remove(remove);
      }
  }

  /*final*/ static Random random = new Random();
  static KeyCode getRandomKeyCode() {
      return keyCodes[random.nextInt(keyCodes.length)];
  }
}
