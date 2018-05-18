package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.declaration.CtMethod;

public class DFCtMethodImpl extends DFCtExecutableImpl implements DFCtMethod {

  protected DFCtMethodImpl(CtMethod<?> ctMethod, DFCtElement parent) {
    super(ctMethod, parent);
  }
  
  
  public static DFCtMethod create(CtMethod<?> ctMethod, DFCtElement parent) {
    return new DFCtMethodImpl(ctMethod, parent);
  }
  
  
  @Override
  public CtMethod<?> getOriginal() {
    return (CtMethod<?>) this.original;
  }

}
