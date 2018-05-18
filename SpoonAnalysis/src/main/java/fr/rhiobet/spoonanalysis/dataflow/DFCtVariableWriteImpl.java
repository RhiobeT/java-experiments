package fr.rhiobet.spoonanalysis.dataflow;

import java.util.Set;

import spoon.reflect.code.CtVariableWrite;

public class DFCtVariableWriteImpl extends DFCtVariableAccessImpl implements DFCtVariableWrite {

  protected DFCtVariableWriteImpl(CtVariableWrite<?> ctVariableWrite, DFCtElement parent) {
    super(ctVariableWrite, parent);
  }
  
  
  public static DFCtVariableWrite create(CtVariableWrite<?> ctVariableWrite, DFCtElement parent) {
    return new DFCtVariableWriteImpl(ctVariableWrite, parent);
  }
  
  
  @Override
  public CtVariableWrite<?> getOriginal() {
    return (CtVariableWrite<?>) this.original;
  }
  
  
  @Override
  public Set<Dependency> getDependencies() {
    Set<Dependency> dependencies = super.getDependencies();      
    dependencies.add(new Dependency(this, Dependency.Kind.WRITE, this.getVariable()));
    
    return dependencies;
  }

}
