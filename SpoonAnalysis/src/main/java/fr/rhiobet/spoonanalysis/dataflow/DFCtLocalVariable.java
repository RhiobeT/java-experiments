package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.code.CtLocalVariable;

public interface DFCtLocalVariable extends DFCtVariable, DFCtStatement {

  @Override
  public CtLocalVariable<?> getOriginal();
  
  public DFCtExpression getAssignment();
  
}
