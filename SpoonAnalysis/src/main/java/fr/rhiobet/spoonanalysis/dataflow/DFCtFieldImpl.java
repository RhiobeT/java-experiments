package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.declaration.CtField;

public class DFCtFieldImpl extends DFCtVariableImpl implements DFCtField {

  protected DFCtFieldImpl(CtField<?> ctField, DFCtElement parent) {
    super(ctField, parent);
  }

  
  public static DFCtField create(CtField<?> ctField, DFCtElement parent) {
    return new DFCtFieldImpl(ctField, parent);
  }
  
 
  @Override
  public CtField<?> getOriginal() {
    return (CtField<?>) this.original;
  }
  
  
  public DFCtExpression getAssignment() {
    return this.getDefaultExpression();
  }
  
}
