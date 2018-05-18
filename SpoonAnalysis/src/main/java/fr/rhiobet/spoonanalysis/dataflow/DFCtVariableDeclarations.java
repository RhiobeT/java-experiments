package fr.rhiobet.spoonanalysis.dataflow;

import java.util.HashMap;
import java.util.Map;

import spoon.reflect.declaration.CtVariable;

public class DFCtVariableDeclarations {

  private static Map<CtVariable<?>, DFCtVariable> variables = new HashMap<>();
  
  
  public static void addDeclaration(DFCtVariable variable) {
    variables.put(variable.getOriginal(), variable);
  }
  
  
  public static DFCtVariable getDeclaration(CtVariable<?> variable) {
    return variables.get(variable);
  } 
  
}
