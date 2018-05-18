package fr.rhiobet.spoonanalysis.dataflow;

import spoon.reflect.code.CtExpression;

public interface DFCtExpression extends DFCtElement {

  @Override
  public CtExpression<?> getOriginal();
 
  
  @Override
  public default String getRepresentation() {
    StringBuilder string = new StringBuilder();
    
    string.append(this.getOriginal().toString());
    
    return string.toString();
  }
  
}
