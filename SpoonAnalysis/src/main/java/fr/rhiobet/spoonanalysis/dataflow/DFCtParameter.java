package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.declaration.CtParameter;

public interface DFCtParameter extends DFCtVariable {

  @Override
  public CtParameter<?> getOriginal();
  
}
