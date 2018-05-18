package fr.rhiobet.spoonanalysis.dataflow;

import java.util.List;

import spoon.reflect.code.CtBlock;

public interface DFCtBlock extends DFCtStatement {
  
  @Override
  public CtBlock<?> getOriginal();
 
  public List<DFCtStatement> getStatements();
 
  
  @Override
  public default String getRepresentation() {
    StringBuilder string = new StringBuilder();
    string.append("{ // deps: " + this.getDependencies().toString() + "\n");
    for (DFCtStatement statement : this.getStatements()) {
      string.append(statement.toString().replaceAll("(?m)^", "    ") + "\n");
    }
    string.append("}");
    return string.toString();
  }
  
}
