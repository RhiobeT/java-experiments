package fr.rhiobet.spoonanalysis.dataflow;

import java.util.stream.Collectors;

import spoon.reflect.code.CtIf;

public interface DFCtIf extends DFCtStatement {

  @Override
  public CtIf getOriginal();
  
  public DFCtExpression getCondition();
  
  public DFCtStatement getThenStatement();
  
  public DFCtStatement getElseStatement();

  
  public default String getRepresentation() {
    StringBuilder string = new StringBuilder();
    
    string.append("// deps: " + this.getDependencies()
        + " " + this.getStatementDependencies().stream()
        .map(e -> e.getOriginal()).collect(Collectors.toSet()).toString() + "\n");
    string.append("if (" + this.getCondition() + ") ");
    string.append(this.getThenStatement().toString());
    if (this.getElseStatement() != null) {
      string.append(" else " + this.getElseStatement().toString());
    }
    string.append(" // deps: " + this.getDependencies().toString());
    
    return string.toString();
  }
  
}
