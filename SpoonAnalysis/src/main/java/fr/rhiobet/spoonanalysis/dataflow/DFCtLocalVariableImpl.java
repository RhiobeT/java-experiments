package fr.rhiobet.spoonanalysis.dataflow;

import java.util.HashSet;
import java.util.Set;

import spoon.reflect.code.CtLocalVariable;

public class DFCtLocalVariableImpl extends DFCtVariableImpl implements DFCtLocalVariable {
  
  private Set<DFCtStatement> statementDependencies;
  
  
  protected DFCtLocalVariableImpl(CtLocalVariable<?> ctLocalVariable, DFCtElement parent) {
    super(ctLocalVariable, parent);
    
  }
  
  
  public static DFCtLocalVariable create(CtLocalVariable<?> ctLocalVariable, DFCtElement parent) {
    return new DFCtLocalVariableImpl(ctLocalVariable, parent);
  }
  
  
  @Override
  public CtLocalVariable<?> getOriginal() {
    return (CtLocalVariable<?>) this.original;
  }


  @Override
  public DFCtExpression getAssignment() {
    return this.getDefaultExpression();
  }

  
  @Override
  public void checkStatementDependencies() {
    this.statementDependencies = new HashSet<>();
  }
  
  
  @Override
  public void addStatementDependency(DFCtStatement statement) {
    this.statementDependencies.add(statement);
  }

  
  @Override
  public void removeStatementDependency(DFCtStatement statement) {
    this.statementDependencies.remove(statement);
  }
  
  
  @Override
  public Set<DFCtStatement> getStatementDependencies() {
    return this.statementDependencies;
  }

}
