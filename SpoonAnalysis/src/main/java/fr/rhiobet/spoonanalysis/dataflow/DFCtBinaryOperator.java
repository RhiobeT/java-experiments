package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.code.CtBinaryOperator;

public interface DFCtBinaryOperator extends DFCtExpression {
  
  @Override
  public CtBinaryOperator<?> getOriginal();
  
  public DFCtExpression getLeftHandOperand();
    
  public DFCtExpression getRightHandOperand();
  
}
