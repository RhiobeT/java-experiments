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
public class BenchFinalArray {
  static int LOOPS = Integer.MAX_VALUE / 100;

  static Random random = new Random();

  static KeyCode[] staticKeyCodes = KeyCode.values();

  static final KeyCode[] staticFinalKeyCodes = KeyCode.values();

  KeyCode[] keyCodes = KeyCode.values();

  final KeyCode[] finalKeyCodes = KeyCode.values(); 
  
  @Benchmark
  public EnumSet<KeyCode> testEnumSetFinalLocal() {
      EnumSet<KeyCode> enumSet = EnumSet.noneOf(KeyCode.class);
      final KeyCode[] localKeyCodes = KeyCode.values();
      for (int i = 0; i < LOOPS; i++) {
          KeyCode add = getRandomKeyCode(localKeyCodes);
          if(!enumSet.contains(add)) enumSet.add(add);

          KeyCode remove = getRandomKeyCode(localKeyCodes);
          if(enumSet.contains(remove)) enumSet.remove(remove);
      }
      return enumSet;
  }
  
  
  @Benchmark
  public EnumSet<KeyCode> testEnumSetLocal() {
      EnumSet<KeyCode> enumSet = EnumSet.noneOf(KeyCode.class);
      KeyCode[] localKeyCodes = KeyCode.values();
      for (int i = 0; i < LOOPS; i++) {
          KeyCode add = getRandomKeyCode(localKeyCodes);
          if(!enumSet.contains(add)) enumSet.add(add);

          KeyCode remove = getRandomKeyCode(localKeyCodes);
          if(enumSet.contains(remove)) enumSet.remove(remove);
      }
      return enumSet;
  }
  
  
  @Benchmark
  public EnumSet<KeyCode> testEnumSet() {
      EnumSet<KeyCode> enumSet = EnumSet.noneOf(KeyCode.class);
      for (int i = 0; i < LOOPS; i++) {
          KeyCode add = getRandomKeyCode();
          if(!enumSet.contains(add)) enumSet.add(add);

          KeyCode remove = getRandomKeyCode();
          if(enumSet.contains(remove)) enumSet.remove(remove);
      }
      return enumSet;
  }

  
  @Benchmark
  public EnumSet<KeyCode> testEnumSetFinal() {
      EnumSet<KeyCode> enumSet = EnumSet.noneOf(KeyCode.class);
      for (int i = 0; i < LOOPS; i++) {
          KeyCode add = getRandomKeyCodeFinal();
          if(!enumSet.contains(add)) enumSet.add(add);

          KeyCode remove = getRandomKeyCodeFinal();
          if(enumSet.contains(remove)) enumSet.remove(remove);
      }
      return enumSet;
  }

   
  @Benchmark
  public EnumSet<KeyCode> testEnumSetStatic() {
      EnumSet<KeyCode> enumSet = EnumSet.noneOf(KeyCode.class);
      for (int i = 0; i < LOOPS; i++) {
          KeyCode add = getRandomKeyCodeStatic();
          if(!enumSet.contains(add)) enumSet.add(add);

          KeyCode remove = getRandomKeyCodeStatic();
          if(enumSet.contains(remove)) enumSet.remove(remove);
      }
      return enumSet;
  }

  
  @Benchmark
  public EnumSet<KeyCode> testEnumSetStaticFinal() {
      EnumSet<KeyCode> enumSet = EnumSet.noneOf(KeyCode.class);
      for (int i = 0; i < LOOPS; i++) {
          KeyCode add = getRandomKeyCodeStaticFinal();
          if(!enumSet.contains(add)) enumSet.add(add);

          KeyCode remove = getRandomKeyCodeStaticFinal();
          if(enumSet.contains(remove)) enumSet.remove(remove);
      }
      return enumSet;
  }
  
  
  static KeyCode getRandomKeyCode(KeyCode[] localKeyCodes) {
    return localKeyCodes[random.nextInt(localKeyCodes.length)];
  }
  
  static KeyCode getRandomKeyCodeStatic() {
      return staticKeyCodes[random.nextInt(staticKeyCodes.length)];
  }
  
  static KeyCode getRandomKeyCodeStaticFinal() {
      return staticFinalKeyCodes[random.nextInt(staticFinalKeyCodes.length)];
  }

  KeyCode getRandomKeyCode() {
      return keyCodes[random.nextInt(keyCodes.length)];
  }
  
   KeyCode getRandomKeyCodeFinal() {
      return finalKeyCodes[random.nextInt(finalKeyCodes.length)];
  }
  
}
