package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.code.CtAssignment;

public interface DFCtAssignment extends DFCtStatement, DFCtExpression {

  @Override
  public CtAssignment<?, ?> getOriginal();
  
  public DFCtExpression getAssignment(); 

  public DFCtExpression getAssigned(); 
 
  
  @Override
  public default String getRepresentation() {
    StringBuilder string = new StringBuilder();
    
    string.append(this.getOriginal().toString());
    
    if (this.getParent() instanceof DFCtBlock) {
      string.append("; // deps: " + this.getDependencies().toString()
          + " " + this.getStatementDependencies().toString());
    }
    
    return string.toString();
  }
  
}
