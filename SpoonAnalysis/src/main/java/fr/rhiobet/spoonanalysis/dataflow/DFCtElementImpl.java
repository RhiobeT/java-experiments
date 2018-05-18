package fr.rhiobet.spoonanalysis.dataflow;

import java.util.LinkedHashSet;
import java.util.Set;

import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtVariable;

public class DFCtElementImpl implements DFCtElement {
  
  protected CtElement original;
  private DFCtElement parent;
  
  
  protected DFCtElementImpl(CtElement ctElement, DFCtElement parent) {
    this.original = ctElement;
    this.parent = parent;
  }
  
 
  public static DFCtElement create(CtElement ctElement, DFCtElement parent) {
    if (ctElement instanceof CtStatement) {
      return DFCtStatementImpl.create((CtStatement) ctElement, parent);
    } else if (ctElement instanceof CtExecutable<?>) {
      return DFCtExecutableImpl.create((CtExecutable<?>) ctElement, parent);
    } else if (ctElement instanceof CtVariable) {
      return DFCtVariableImpl.create((CtVariable<?>) ctElement, parent);
    } else if (ctElement instanceof CtExpression) {
      return DFCtExpressionImpl.create((CtExpression<?>) ctElement, parent);
    } else {
      System.err.println("Error: " + ctElement.getClass() + " not implemented yet");
      return new DFCtElementImpl(ctElement, parent);
    }
  }
  
  
  @Override
  public CtElement getOriginal() {
    return this.original;
  }
  
  
  @Override
  public DFCtElement getParent() {
    return this.parent;
  }
  
  
  @Override
  public Set<Dependency> getDependencies() {
    return new LinkedHashSet<>();
  }
  
  
  @Override
  public void checkDependencies() {
    
  }
  
  
  @Override
  public void checkDeferredDependencies() {
  
  }
  
  
  @Override
  public void checkStatementDependencies() {
  
  }
  
  
  @Override
  public String toString() {
    return this.getRepresentation();
  }

}
