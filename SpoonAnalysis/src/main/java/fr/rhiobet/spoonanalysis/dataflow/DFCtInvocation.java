package fr.rhiobet.spoonanalysis.dataflow;

import java.util.List;

import spoon.reflect.code.CtInvocation;

public interface DFCtInvocation extends DFCtStatement, DFCtExpression {
  
  public List<DFCtExpression> getArguments();
  
  public DFCtExecutable getExecutableDeclaration();

  @Override
  public CtInvocation<?> getOriginal();
  
  
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
