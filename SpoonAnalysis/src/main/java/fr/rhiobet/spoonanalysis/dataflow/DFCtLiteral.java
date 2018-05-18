package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.code.CtLiteral;

public interface DFCtLiteral extends DFCtExpression {
  
  @Override
  public CtLiteral<?> getOriginal();

}
