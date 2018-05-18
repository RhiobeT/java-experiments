package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLiteral;

public class DFCtLiteralImpl extends DFCtExpressionImpl implements DFCtLiteral {

  protected DFCtLiteralImpl(CtExpression<?> ctExpression, DFCtElement parent) {
    super(ctExpression, parent);
  }
  
  
  public static DFCtLiteral create(CtExpression<?> ctExpression, DFCtElement parent) {
    return new DFCtLiteralImpl(ctExpression, parent);
  }
  
  
  @Override
  public CtLiteral<?> getOriginal() {
    return (CtLiteral<?>) this.original;
  } 

}
