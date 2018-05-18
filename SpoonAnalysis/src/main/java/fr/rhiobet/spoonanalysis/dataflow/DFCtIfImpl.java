package fr.rhiobet.spoonanalysis.dataflow;

import java.util.Set;

import spoon.reflect.code.CtIf;

public class DFCtIfImpl extends DFCtStatementImpl implements DFCtIf {

  private DFCtExpression condition;
  private DFCtStatement thenStatement;
  private DFCtStatement elseStatement;
  
  
  protected DFCtIfImpl(CtIf ctIf, DFCtElement parent) {
    super(ctIf, parent);
    
    this.condition = null;
    this.thenStatement = null;
    this.elseStatement = null;
  }
  
  
  public static DFCtIf create(CtIf ctIf, DFCtElement parent) {
    return new DFCtIfImpl(ctIf, parent);
  }

  
  @Override
  public DFCtExpression getCondition() {
    return this.condition;
  }

  
  @Override
  public DFCtStatement getThenStatement() {
    return this.thenStatement;
  }

  
  @Override
  public DFCtStatement getElseStatement() {
    return this.elseStatement;
  }

  
  @Override
  public CtIf getOriginal() {
    return (CtIf) this.original;
  }
  
  
  @Override
  public void checkDependencies() {
    this.condition = DFCtExpressionImpl.create(this.getOriginal().getCondition(), this);
    this.condition.checkDependencies();
    this.thenStatement = DFCtStatementImpl.create(this.getOriginal().getThenStatement(), this);
    this.thenStatement.checkDependencies();
    if (this.getOriginal().getElseStatement() != null) {
      this.elseStatement = DFCtStatementImpl.create(this.getOriginal().getElseStatement(), this);
      this.elseStatement.checkDependencies();
    }
  }
  
  
  @Override
  public void checkDeferredDependencies() {
    this.condition.checkDeferredDependencies();
    this.thenStatement.checkDeferredDependencies();
    if (this.elseStatement != null) {
      this.elseStatement.checkDeferredDependencies();
    }
  }
  
  
  @Override
  public Set<Dependency> getDependencies() {
    Set<Dependency> dependencies = super.getDependencies();
    dependencies.addAll(this.condition.getDependencies());    
    dependencies.addAll(this.thenStatement.getDependencies());  
    if (this.elseStatement != null) {
      dependencies.addAll(this.elseStatement.getDependencies());
    }
    return dependencies;
  }
  
  
  @Override
  public void checkStatementDependencies() {
    super.checkStatementDependencies();
    this.thenStatement.checkStatementDependencies();
    this.elseStatement.checkStatementDependencies();
  }
  
}
