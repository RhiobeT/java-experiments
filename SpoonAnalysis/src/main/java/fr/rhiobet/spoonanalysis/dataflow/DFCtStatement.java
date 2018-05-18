package fr.rhiobet.spoonanalysis.dataflow;

import java.util.Set;
import java.util.stream.Collectors;

import spoon.reflect.code.CtStatement;

public interface DFCtStatement extends DFCtElement {

  @Override
  public CtStatement getOriginal();
   
  public void addStatementDependency(DFCtStatement statement);

  public void removeStatementDependency(DFCtStatement statement);
  
  public Set<DFCtStatement> getStatementDependencies();
  
  
  @Override
  public default String getRepresentation() {
    StringBuilder string = new StringBuilder();
    
    string.append(this.getOriginal().toString() + ";");
    string.append(" // deps: " + this.getDependencies().toString()
        + " " + this.getStatementDependencies().stream()
                    .map(e -> e.getOriginal()).collect(Collectors.toSet()).toString());
    
    return string.toString();
  }
  
}
