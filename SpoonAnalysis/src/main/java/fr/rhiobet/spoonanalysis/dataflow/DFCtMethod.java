package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.declaration.CtMethod;

public interface DFCtMethod extends DFCtExecutable {

  @Override
  public CtMethod<?> getOriginal();
  
}
