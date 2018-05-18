package fr.rhiobet.spoonanalysis.dataflow;

import java.util.Set;

import spoon.reflect.code.CtAssignment;

public class DFCtAssignmentImpl extends DFCtStatementImpl implements DFCtAssignment {

  private DFCtExpression assignment;
  private DFCtExpression assigned;
  
  
  protected DFCtAssignmentImpl(CtAssignment<?, ?> ctAssignment, DFCtElement parent) {
    super(ctAssignment, parent);
    
    this.assignment = null;
    this.assigned = null;
  }
  
  
  public static DFCtAssignment create(CtAssignment<?, ?> ctAssignment, DFCtElement parent) {
    return new DFCtAssignmentImpl(ctAssignment, parent);
  }
  
 
  @Override
  public DFCtExpression getAssignment() {
    return this.assignment;
  }


  @Override
  public DFCtExpression getAssigned() {
    return this.assigned;
  }

  
  @Override
  public CtAssignment<?, ?> getOriginal() {
    return (CtAssignment<?, ?>) this.original;
  }


  @Override
  public void checkDependencies() {
    super.checkDependencies();
    this.assignment = DFCtExpressionImpl.create(this.getOriginal().getAssignment(), this);
    this.assigned = DFCtExpressionImpl.create(this.getOriginal().getAssigned(), this);
    this.assignment.checkDependencies();
    this.assigned.checkDependencies();
  }
  
  
  @Override
  public void checkDeferredDependencies() {
    super.checkDeferredDependencies();
    this.assignment.checkDeferredDependencies();
    this.assigned.checkDeferredDependencies();
  }
  
  
  @Override
  public Set<Dependency> getDependencies() {
    Set<Dependency> dependencies = super.getDependencies();
    dependencies.addAll(this.assignment.getDependencies());
    dependencies.addAll(this.assigned.getDependencies());
    
    return dependencies;
  }
  
}
