package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.code.CtVariableWrite;

public interface DFCtVariableWrite extends DFCtVariableAccess {

  @Override
  public CtVariableWrite<?> getOriginal();
  
}
