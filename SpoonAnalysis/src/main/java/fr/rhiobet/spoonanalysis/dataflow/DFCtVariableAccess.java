package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.code.CtVariableAccess;

public interface DFCtVariableAccess extends DFCtExpression {

  @Override
  public CtVariableAccess<?> getOriginal();
  
  public DFCtVariable getVariable();
  
}
