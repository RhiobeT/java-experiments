package fr.rhiobet.spoonanalysis.dataflow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;

public class DFCtInvocationImpl extends DFCtExpressionImpl implements DFCtInvocation {
  
  private Set<DFCtStatement> statementDependencies;
  private List<DFCtExpression> arguments;
  private DFCtExecutable executableDeclaration;
  
  
  protected DFCtInvocationImpl(CtInvocation<?> ctInvocation, DFCtElement parent) {
    super(ctInvocation, parent);
    this.arguments = new ArrayList<>();
    this.executableDeclaration = null;
  }
  
  
  public static DFCtInvocation create(CtInvocation<?> ctInvocation, DFCtElement parent) {
    return new DFCtInvocationImpl(ctInvocation, parent);
  }
  
  
  public List<DFCtExpression> getArguments() {
    return this.arguments;
  }
  
  
  public DFCtExecutable getExecutableDeclaration() {
    return this.executableDeclaration;
  }


  @Override
  public CtInvocation<?> getOriginal() {
    return (CtInvocation<?>) this.original;
  }


  @Override
  public void checkDependencies() {
    super.checkDependencies();
    for (CtExpression<?> ctExpression : this.getOriginal().getArguments()) {
      DFCtExpression argument = DFCtExpressionImpl.create(ctExpression, this);
      argument.checkDependencies();
      this.arguments.add(argument);
    }
  }
 
  
  @Override
  public void checkDeferredDependencies() {
    super.checkDeferredDependencies();
    for (DFCtExpression argument : this.getArguments()) {
      argument.checkDeferredDependencies();
    }
    DFCtExecutable executable =
        DFCtExecutableDefinitions.getDefinition(this.getOriginal().getExecutable().getExecutableDeclaration());
    if (executable == null) {
      executable = DFCtExecutableImpl.create(this.getOriginal().getExecutable().getExecutableDeclaration(), null);
      executable.checkDependencies();
      DFCtExecutableDefinitions.addDefinition(executable);
    }
    executable.checkDeferredDependencies();
    this.executableDeclaration = executable;
  }
  
  
  @Override
  public Set<Dependency> getDependencies() {
    Set<Dependency> dependencies = super.getDependencies();
    for (DFCtExpression argument : this.getArguments()) {
      dependencies.addAll(argument.getDependencies());
    }
    dependencies.addAll(this.executableDeclaration.getDependencies());
   
    return dependencies;
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
