package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.declaration.CtVariable;

public interface DFCtVariable extends DFCtElement {

  @Override
  public CtVariable<?> getOriginal();
  
  public DFCtExpression getDefaultExpression();
  
  public String getSimpleName();
  
}
