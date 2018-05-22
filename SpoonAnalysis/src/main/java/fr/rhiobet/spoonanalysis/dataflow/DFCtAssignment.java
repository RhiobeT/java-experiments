package fr.rhiobet.spoonanalysis.dataflow;

import java.util.stream.Collectors;

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
          + " " + this.getStatementDependencies().stream()
                      .map(e -> e.getOriginal()).collect(Collectors.toSet()).toString());
    }
    
    return string.toString();
  }
  
}
