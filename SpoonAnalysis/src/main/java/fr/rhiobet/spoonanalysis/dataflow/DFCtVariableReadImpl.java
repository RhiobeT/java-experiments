package fr.rhiobet.spoonanalysis.dataflow;

import java.util.Set;

import spoon.reflect.code.CtVariableRead;

public class DFCtVariableReadImpl extends DFCtVariableAccessImpl implements DFCtVariableRead {

  protected DFCtVariableReadImpl(CtVariableRead<?> ctVariableRead, DFCtElement parent) {
    super(ctVariableRead, parent);
  }
  
  
  public static DFCtVariableAccess create(CtVariableRead<?> ctVariableRead, DFCtElement parent) {
    return new DFCtVariableReadImpl(ctVariableRead, parent);
  }
  
  
  @Override
  public CtVariableRead<?> getOriginal() {
    return (CtVariableRead<?>) this.original;
  }
  
  
  @Override
  public Set<Dependency> getDependencies() {
    Set<Dependency> dependencies = super.getDependencies();      
    dependencies.add(new Dependency(this, Dependency.Kind.READ, this.getVariable()));
    return dependencies;
  }
  
}
