package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.declaration.CtConstructor;

public class DFCtConstructorImpl extends DFCtExecutableImpl implements DFCtConstructor {
  
  protected DFCtConstructorImpl(CtConstructor<?> ctConstructor, DFCtElement parent) {
    super(ctConstructor, parent);
  }
  
  
  public static DFCtConstructor create(CtConstructor<?> ctConstructor, DFCtElement parent) {
    return new DFCtConstructorImpl(ctConstructor, parent);
  }
  
  
  @Override
  public CtConstructor<?> getOriginal() {
    return (CtConstructor<?>) this.original;
  }
  
}
