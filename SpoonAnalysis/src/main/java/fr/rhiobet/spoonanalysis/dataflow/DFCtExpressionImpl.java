package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtVariableAccess;

public class DFCtExpressionImpl extends DFCtElementImpl implements DFCtExpression {

  protected DFCtExpressionImpl(CtExpression<?> ctExpression, DFCtElement parent) {
    super(ctExpression, parent);
  }

  
  public static DFCtExpression create(CtExpression<?> ctExpression, DFCtElement parent) {
    if (ctExpression instanceof CtInvocation) {
      return DFCtInvocationImpl.create((CtInvocation<?>) ctExpression, parent);
    } else if (ctExpression instanceof CtLiteral) {
      return DFCtLiteralImpl.create((CtLiteral<?>) ctExpression, parent);
    } else if (ctExpression instanceof CtBinaryOperator) {
      return DFCtBinaryOperatorImpl.create((CtBinaryOperator<?>) ctExpression, parent);
    } else if (ctExpression instanceof CtVariableAccess) {
      return DFCtVariableAccessImpl.create((CtVariableAccess<?>) ctExpression, parent);
    } else if (ctExpression instanceof CtAssignment) {
      return DFCtAssignmentImpl.create((CtAssignment<?, ?>) ctExpression, parent);
    } else {
      System.err.println("Error: " + ctExpression.getClass() + " not implemented yet");
      return new DFCtExpressionImpl(ctExpression, parent);
    }
  }
  
  
  @Override
  public CtExpression<?> getOriginal() {
    return (CtExpression<?>) this.original;
  }
  
}
