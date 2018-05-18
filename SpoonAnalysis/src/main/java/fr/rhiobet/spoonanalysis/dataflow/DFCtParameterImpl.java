package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.declaration.CtParameter;

public class DFCtParameterImpl extends DFCtVariableImpl implements DFCtParameter {

  protected DFCtParameterImpl(CtParameter<?> ctParameter, DFCtElement parent) {
    super(ctParameter, parent);
  }
  
  
  public static DFCtParameter create(CtParameter<?> ctParameter, DFCtElement parent) {    
    return new DFCtParameterImpl(ctParameter, parent);
  }
  
  
  @Override
  public CtParameter<?> getOriginal() {
    return (CtParameter<?>) this.original;
  }

}
