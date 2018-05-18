package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.code.CtVariableWrite;

public class DFCtVariableAccessImpl extends DFCtExpressionImpl implements DFCtVariableAccess {
  
  private DFCtVariable variable;

  
  protected DFCtVariableAccessImpl(CtVariableAccess<?> ctVariableAccess, DFCtElement parent) {
    super(ctVariableAccess, parent);
    
    this.variable = null;
  }
  
  
  public static DFCtVariableAccess create(CtVariableAccess<?> ctVariableAccess, DFCtElement parent) {
    if (ctVariableAccess instanceof CtVariableRead) {
      return DFCtVariableReadImpl.create((CtVariableRead<?>) ctVariableAccess, parent);
    } else if (ctVariableAccess instanceof CtVariableWrite) {
      return DFCtVariableWriteImpl.create((CtVariableWrite<?>) ctVariableAccess, parent);
    } else {
      System.err.println("Error: " + ctVariableAccess.getClass() + " not implemented yet");
      return new DFCtVariableAccessImpl(ctVariableAccess, parent);
    }
  }
  
  
  @Override
  public DFCtVariable getVariable() {
    return this.variable;
  }
  
  
  @Override
  public CtVariableAccess<?> getOriginal() {
    return (CtVariableAccess<?>) this.original;
  }
  
  
  @Override
  public void checkDeferredDependencies() {
    super.checkDeferredDependencies();
    this.variable = DFCtVariableDeclarations.getDeclaration(this.getOriginal().getVariable().getDeclaration());
    if (this.variable == null) {
      this.variable = DFCtVariableImpl.create(this.getOriginal().getVariable().getDeclaration(), null);
      this.variable.checkDeferredDependencies();
      DFCtVariableDeclarations.addDeclaration(this.variable);
    }
  }
  
}
