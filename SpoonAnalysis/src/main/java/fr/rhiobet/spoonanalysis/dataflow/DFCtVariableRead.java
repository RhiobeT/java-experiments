package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.code.CtVariableRead;

public interface DFCtVariableRead extends DFCtVariableAccess {

  @Override
  public CtVariableRead<?> getOriginal();
  
}
