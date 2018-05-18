package fr.rhiobet.spoonanalysis.dataflow;

import java.util.List;

import spoon.reflect.declaration.CtExecutable;

public interface DFCtExecutable extends DFCtElement {

  @Override
  public CtExecutable<?> getOriginal();
  
  public List<DFCtParameter> getParameters();
  
  public DFCtBlock getBody();
 
  
  @Override
  public default String getRepresentation() {
    String originalString = this.getOriginal().toString();
    StringBuilder string = new StringBuilder();
    
    string.append(originalString.substring(0, originalString.indexOf("{")));
    
    string.append(this.getBody().toString());
    
    return string.toString();
  }
  
}
