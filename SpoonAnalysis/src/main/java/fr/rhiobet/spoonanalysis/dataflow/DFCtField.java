package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.declaration.CtField;

public interface DFCtField extends DFCtVariable {

  @Override
  public CtField<?> getOriginal();
  
  public DFCtExpression getAssignment();
 
  
  @Override
  public default String getRepresentation() {
    StringBuilder string = new StringBuilder();
    string.append(this.getOriginal().toString());
    string.append(" // deps: ");
    for (Dependency dependency : this.getDependencies()) {
      string.append(dependency.toString() + ", ");
    }
    return string.toString();
  }
  
}
