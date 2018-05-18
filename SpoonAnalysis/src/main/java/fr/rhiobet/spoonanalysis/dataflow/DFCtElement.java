package fr.rhiobet.spoonanalysis.dataflow;

import java.util.Set;

import spoon.reflect.declaration.CtElement;

public interface DFCtElement {
  
  public CtElement getOriginal();
   
  public DFCtElement getParent();
   
  public Set<Dependency> getDependencies();
  
  public void checkDependencies();
  
  public void checkDeferredDependencies();
    
  public void checkStatementDependencies();

  
  public default String getRepresentation() {
    return "REPRESENTATION NOT IMPLEMENTED FOR " + this.getClass();
  }
  
}
