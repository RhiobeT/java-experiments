package fr.rhiobet.spoonanalysis.dataflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

public class DFCtExecutableImpl extends DFCtElementImpl implements DFCtExecutable {

  private List<DFCtParameter> parameters;
  private DFCtBlock body;
  private boolean deferredDependenciesChecked;
  private boolean deferredDependenciesGenerated;
  private boolean statementDependenciesChecked;
  
  
  protected DFCtExecutableImpl(CtExecutable<?> ctExecutable, DFCtElement parent) {
    super(ctExecutable, parent);
    
    this.parameters = new ArrayList<>();
    this.body = null;
    this.deferredDependenciesChecked = false;
    this.deferredDependenciesGenerated = false;
    this.statementDependenciesChecked = false;
  }
  
  
  public static DFCtExecutable create(CtExecutable<?> ctExecutable, DFCtElement parent) {
    if (ctExecutable instanceof CtConstructor<?>) {
      return DFCtConstructorImpl.create((CtConstructor<?>) ctExecutable, parent);
    } else if (ctExecutable instanceof CtMethod<?>) {
      return DFCtMethodImpl.create((CtMethod<?>) ctExecutable, parent);
    } else {
      System.err.println("Error: " + ctExecutable.getClass() + " not implemented yet");
      return new DFCtExecutableImpl(ctExecutable, parent);
    }
  }
  
  
  @Override
  public CtExecutable<?> getOriginal() {
    return (CtExecutable<?>) this.original;
  }
  
  
  public List<DFCtParameter> getParameters() {
    return this.parameters;
  }
  
  
  public DFCtBlock getBody() {
    return this.body;
  }
  
  
  @Override
  public void checkDependencies() {
    super.checkDependencies();
    for (CtParameter<?> ctParameter : this.getOriginal().getParameters()) {
      DFCtParameter parameter = DFCtParameterImpl.create(ctParameter, this);
      parameter.checkDependencies();
      this.parameters.add(parameter);
    }
    DFCtBlock block = DFCtBlockImpl.create(this.getOriginal().getBody(), this);
    block.checkDependencies();
    this.body = block;    
    
    DFCtExecutableDefinitions.addDefinition(this);
  }
  
  
  @Override
  public void checkDeferredDependencies() {    
    if (!this.deferredDependenciesChecked) {
      this.deferredDependenciesChecked = true;
      super.checkDeferredDependencies();
      for (DFCtParameter parameter : this.parameters) {
        parameter.checkDeferredDependencies();
      }
      this.body.checkDeferredDependencies();
    }
  }
  
  
  @Override
  public Set<Dependency> getDependencies() {    
    Set<Dependency> dependencies = super.getDependencies();
    if (!this.deferredDependenciesGenerated) {
      this.deferredDependenciesGenerated = true;
      for (Dependency dependency : this.body.getDependencies()) {
        // We remove the dependencies on the parameters
        if (!this.parameters.contains(dependency.getVariable())) {
          dependencies.add(dependency);
        }
      }
      this.deferredDependenciesGenerated = false;
    }
    return dependencies;
  }
  
   
  @Override
  public void checkStatementDependencies() {
    super.checkStatementDependencies();
    if (!this.statementDependenciesChecked) {
      this.statementDependenciesChecked = true;
      this.body.checkStatementDependencies();
    }
  }
  
}
