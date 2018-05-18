package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.declaration.CtConstructor;

public interface DFCtConstructor extends DFCtExecutable {

  @Override
  public CtConstructor<?> getOriginal();
  
}
