package fr.rhiobet.spoonanalysis.dataflow;

import java.util.HashMap;
import java.util.Map;

import spoon.reflect.declaration.CtExecutable;

public class DFCtExecutableDefinitions {

  private static Map<CtExecutable<?>, DFCtExecutable> executables = new HashMap<>();
  
  
  public static void addDefinition(DFCtExecutable executable) {
    executables.put(executable.getOriginal(), executable);
  }
  
  
  public static DFCtExecutable getDefinition(CtExecutable<?> executable) {
    return executables.get(executable);
  }
  
}
